package com.platinumassistant.core.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.work.workDataOf
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import timber.log.Timber
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * Worker to download model files in background and save into app filesDir or external files dir.
 * This is a minimal example — production code should validate checksums, handle retries,
 * and securely store files if required.
 */
class ModelDownloadWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val client = OkHttpClient()

    override suspend fun doWork(): Result {
        val url = inputData.getString("model_url") ?: return Result.failure()
        val filename = inputData.getString("model_filename") ?: url.substringAfterLast("/")
        val expectedSha256 = inputData.getString("model_sha256")

        return try {
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) return Result.retry()

                val contentLength = resp.body?.contentLength() ?: -1L
                val targetDir = File(applicationContext.filesDir, "models")
                if (!targetDir.exists()) targetDir.mkdirs()
                val outFile = File(targetDir, filename)

                resp.body?.byteStream()?.use { input ->
                    FileOutputStream(outFile).use { output ->
                        val buffer = ByteArray(8 * 1024)
                        var bytesRead: Int
                        var totalCopied = 0L
                        var lastProgress = -1

                        val digest = MessageDigest.getInstance("SHA-256")

                        while (true) {
                            bytesRead = input.read(buffer)
                            if (bytesRead == -1) break
                            output.write(buffer, 0, bytesRead)
                            digest.update(buffer, 0, bytesRead)
                            totalCopied += bytesRead

                            if (contentLength > 0) {
                                val progress = ((totalCopied * 80) / contentLength).toInt() // 0..80 for download
                                if (progress != lastProgress) {
                                    setProgress(workDataOf("progress" to progress))
                                    lastProgress = progress
                                }
                            }
                        }
                        output.flush()
                        Timber.i("Downloaded model to %s", outFile.absolutePath)

                        // verify checksum if provided
                        if (!expectedSha256.isNullOrBlank()) {
                            val actual = digest.digest().toHexString()
                            if (!actual.equals(expectedSha256, ignoreCase = true)) {
                                Timber.e("Checksum mismatch for %s (expected=%s actual=%s)", filename, expectedSha256, actual)
                                outFile.delete()
                                return Result.retry()
                            }
                        }
                    }
                }

                // If it's a zip, extract it and mark ready
                if (filename.endsWith(".zip", ignoreCase = true)) {
                    val extractDirName = filename.removeSuffix(".zip")
                    val extractDir = File(applicationContext.filesDir, "models/${extractDirName}")
                    if (!extractDir.exists()) extractDir.mkdirs()

                    // unzip
                    try {
                        setProgress(workDataOf("progress" to 85))
                        unzipFile(outFile, extractDir)
                        // optionally delete the zip to save space
                        outFile.delete()
                        // create a ready marker
                        File(extractDir, ".ready").writeText("ready")
                        setProgress(workDataOf("progress" to 100))
                    } catch (t: Throwable) {
                        Timber.e(t, "Failed to extract zip %s", outFile.absolutePath)
                        return Result.retry()
                    }
                } else {
                    // non-zip: mark ready file next to file
                    val readyFile = File(applicationContext.filesDir, "models/${filename}.ready")
                    readyFile.parentFile?.mkdirs()
                    readyFile.writeText("ready")
                }
            }

            setProgress(workDataOf("progress" to 100))
            Result.success()
        } catch (t: Throwable) {
            Timber.e(t, "Model download failed")
            Result.retry()
        }
    }
}

// helper to convert bytes to hex
fun ByteArray.toHexString(): String {
    val sb = StringBuilder()
    for (b in this) {
        sb.append(String.format("%02x", b))
    }
    return sb.toString()
}

/**
 * Unzip a file to targetDir. Simple implementation — extracts entries preserving directories.
 */
fun unzipFile(zipFile: File, targetDir: File) {
    ZipInputStream(zipFile.inputStream()).use { zis ->
        var entry: ZipEntry? = zis.nextEntry
        val buffer = ByteArray(8 * 1024)
        while (entry != null) {
            val outFile = File(targetDir, entry.name)
            if (entry.isDirectory) {
                outFile.mkdirs()
            } else {
                outFile.parentFile?.mkdirs()
                FileOutputStream(outFile).use { out ->
                    var len: Int
                    while (zis.read(buffer).also { len = it } > 0) {
                        out.write(buffer, 0, len)
                    }
                }
            }
            zis.closeEntry()
            entry = zis.nextEntry
        }
    }
}
