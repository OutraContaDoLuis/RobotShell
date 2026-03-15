package com.dema.campanelishell

import android.content.Context

class CommandsExecution() {

    companion object {

        fun listFilesInTheCurrentDirectory(context: Context?, filePath: String?) {
            val files = FilesController.returnFilesInTheSpecifyFile(filePath)

            val listOfFilesSb = buildString {
                files.forEach { it ->
                    appendLine(it.name)
                }
            }

            (context as? HomeActivity)?.setTextCmdAndEditTextCmd(listOfFilesSb)
        }

    }

}