package com.dema.campanelishell

import android.content.Context

class CommandsController {

    companion object {

        fun getCommand(context: Context, cmd: String?) {
            when (cmd) {
                "ls" -> {
                    val currentPath = (context as? HomeActivity)?.returnCurrentFilePath()
                    CommandsExecution.listFilesInTheCurrentDirectory(context, currentPath)
                }
                else -> {
                    (context as? HomeActivity)?.setTextCmdAndEditTextCmd("Cmd Not Founded")
                }
            }
        }

    }

}