package com.dema.campanelishell

import android.content.Context
import android.util.Log
import java.io.File

class CommandsController {

    companion object {

        fun getCommand(context: Context, cmd: String?) {
            val homeActivity = (context as? HomeActivity)

            if (cmd.toString().startsWith("ls")) {
                val currentPath = homeActivity?.returnCurrentFilePath()
                listFilesInTheCurrentDirectory(context, currentPath)
            } else if (cmd.toString().startsWith("cd")) {
//                (context as? HomeActivity)?.setTextCmdAndEditTextCmd("Sim")
                val directory = cmd?.trim()?.subSequence(2, cmd.length)
                Log.v("CD Directory", directory.toString())

                val currentPath = homeActivity?.returnCurrentFilePath()

                val setNewPath = buildString {
                    append(currentPath)
                    append("/")
                    append(directory?.trim())
                }

                val file = File(setNewPath)

                Log.v("CD Directory", file.path)

                if (!file.exists()) {
                    commandNotFounded(context)
                    return
                }

                if (!file.isDirectory) {
                    commandNotFounded(context)
                    return
                }

                changeDirectory(context, true, file.path)

            } else if (cmd.toString().trim() == "clear") {
                clearTerminal(context)
            } else {
                commandNotFounded(context)
            }
        }

        private fun listFilesInTheCurrentDirectory(context: Context?, filePath: String?) {
            val files = FilesController.returnFilesInTheSpecifyFile(filePath)

            val listOfFilesSb = buildString {
                files.forEach { it ->
                    appendLine(it.name)
                }
            }

            (context as? HomeActivity)?.setTextCmdAndEditTextCmd(listOfFilesSb)
        }

        private fun changeDirectory(context: Context?, enterDirectory: Boolean, directoryName: String?) {
            (context as? HomeActivity)?.setNewCurrentFilePath(directoryName.toString())
            (context as? HomeActivity)?.setTextCmdAndEditTextCmd("")
        }

        private fun clearTerminal(context: Context?) {
            (context as? HomeActivity)?.resetTerminal()
        }

        private fun commandNotFounded(context: Context?) {
            (context as? HomeActivity)?.setTextCmdAndEditTextCmd("Cmd Not Founded")
        }

    }

}