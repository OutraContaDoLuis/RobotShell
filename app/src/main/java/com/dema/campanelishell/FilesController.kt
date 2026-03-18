package com.dema.campanelishell

import android.os.Environment
import java.io.File

class FilesController {

    companion object {
        fun returnRootStoragePath(): String {
            return Environment.getExternalStorageDirectory().path
        }

        fun returnFilesInTheSpecifyFile(path: String?): Array<File?> {
            if (path == null)
                return arrayOf()

            val files = File(path).listFiles()

            return files
        }
    }

}