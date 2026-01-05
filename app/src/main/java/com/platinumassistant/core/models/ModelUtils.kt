package com.platinumassistant.core.models

import android.content.Context
import java.io.File

object ModelUtils {
    /**
     * Returns the directory or file that represents the installed model, or null if not installed.
     */
    fun getInstalledModelPath(context: Context, entry: ModelEntry): File? {
        val filesDir = context.filesDir
        // if it's a zip extraction, directory name is filename without .zip
        if (entry.filename.endsWith(".zip")) {
            val dirname = entry.filename.removeSuffix(".zip")
            val dir = File(filesDir, "models/$dirname")
            val ready = File(dir, ".ready")
            if (dir.exists() && ready.exists()) return dir
        }

        // For single file models
        val single = File(filesDir, "models/${entry.filename}")
        val readySingle = File(filesDir, "models/${entry.filename}.ready")
        if (single.exists() && readySingle.exists()) return single

        return null
    }

    fun deleteModel(context: Context, entry: ModelEntry): Boolean {
        val path = getInstalledModelPath(context, entry) ?: return false
        return if (path.isDirectory) {
            path.deleteRecursively()
        } else {
            path.delete()
        }
    }
}
