package com.platinumassistant.core.workers

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

/**
 * Helper to enqueue model download jobs using WorkManager.
 */
object ModelDownloadManager {
    /**
     * Enqueue a download job and return the unique work name so callers can observe it.
     */
    fun enqueueDownload(context: Context, url: String, filename: String): String {
        val data = Data.Builder()
            .putString("model_url", url)
            .putString("model_filename", filename)
            .build()

        val request = OneTimeWorkRequestBuilder<ModelDownloadWorker>()
            .setInputData(data)
            .build()

        val workName = "download_$filename"
        WorkManager.getInstance(context)
            .enqueueUniqueWork(workName, ExistingWorkPolicy.KEEP, request)

        return workName
    }

    fun cancelDownload(context: Context, filename: String) {
        val workName = "download_$filename"
        WorkManager.getInstance(context).cancelUniqueWork(workName)
    }
}
