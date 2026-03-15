package com.dema.campanelishell

import android.os.Environment

class FilesController {

    companion object {
        fun returnRootStoragePath(): String {
            return Environment.getExternalStorageDirectory().path
        }
    }

}