package com.dema.campanelishell

import android.os.Environment
import android.util.Log
import java.io.File

class FilesController {

    companion object {
        fun returnRootStoragePath(): String {
            return Environment.getExternalStorageDirectory().path
        }

        suspend fun returnFilesInTheSpecifyFile(path: String?): Array<File?> {
            if (path == null)
                return arrayOf()

            val files = File(path).listFiles()

            Log.v("Reading archives", File(path).list().toString())
            Log.v("Reading archives", File(path).listFiles().toString())

            if (files == null)
                return arrayOf()

            return files
        }
    }

}