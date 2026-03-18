package com.dema.campanelishell

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class HomeActivity : AppCompatActivity() {

    private lateinit var txtCmd: TextView
    private lateinit var txtEditCmd: EditText

    private var currentPath: String = ""
    private var currentTextCmd: String = ""

    private val tag = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtCmd = findViewById(R.id.txt_cmd)
        txtEditCmd = findViewById(R.id.txt_edit_cmd)
        txtEditCmd.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                Log.v("Clicked in done keyboard", "He clicked")

                getTheCommandInCmd()

                true
            } else {
                false
            }
        }
    }

    override fun onStart() {
        super.onStart()

        setInitialTextCmd()
        txtCmd.setOnClickListener {
            openKeyboard()
        }
    }

    private fun openKeyboard() {
        Log.i("Shell click test", "Clicked")

        txtCmd.requestFocus()
        val imm = getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(txtCmd, InputMethodManager.SHOW_IMPLICIT)
        Log.v("Test input from keyboard", imm.lastInputMethodSubtype!!.extraValue.toString())
    }

    private fun setInitialTextCmd() {
        val rootPath = FilesController.returnRootStoragePath()
        currentPath = rootPath

        var txtCmdSb = buildString {
            appendLine("Init android system")
            appendLine("Exit code 0")
        }

        currentTextCmd = txtCmdSb

        var txtEditCmdSb = buildString {
            append(rootPath)
            append("> ")
        }

        txtCmd.text = currentTextCmd
        txtEditCmd.setText(txtEditCmdSb)
    }

    private fun getTheCommandInCmd() {
        val allTheEditText = txtEditCmd.text.toString()
        val command =
            allTheEditText.subSequence("$currentPath> ".length, allTheEditText.length).toString()

        getCommand(command)
    }

    fun setTextCmdAndEditTextCmd(newText: String) {
        val newTextCmdSb = buildString {
            appendLine(currentTextCmd)
            appendLine(txtEditCmd.text.toString())
            appendLine()
            appendLine(newText)
        }

        currentTextCmd = newTextCmdSb

        txtCmd.text = newTextCmdSb

        val newTextEditTextCmd = buildString {
            append(currentPath)
            append("> ")
        }

        txtEditCmd.setText(newTextEditTextCmd)
    }

    private fun getCommand(cmd: String?) {
        if (cmd.toString().startsWith("ls")) {
            listFilesInTheCurrentDirectory()
        } else if (cmd.toString().startsWith("cd")) {
            val directory = cmd?.trim()?.subSequence(2, cmd.length)

            Log.v("CD Directory", directory.toString())

            Log.v("CD Directory", directory.toString().contains("..").toString())

            val numberOfDotnet = directory.toString().trim().count { it ->
                it == '.'
            }

            Log.v("CD Directory", directory.toString().trim())
            Log.v("CD Directory", directory.toString().trim().length.toString())
            Log.v("CD Directory", numberOfDotnet.toString())
            Log.v("CD Directory",
                (numberOfDotnet == directory.toString().trim().replace(" ", "").length).toString())
            Log.v("CD Directory", (numberOfDotnet % 2 == 0).toString())

            if (directory.toString().contains('.')
                && numberOfDotnet == directory.toString().trim().replace(" ", "").length
                && numberOfDotnet % 2 == 0) {
                Log.v("Last dir", "Last")

//                Log.v("CD Directory", splitCommandToDotnet.toString())

                var indexWhile = numberOfDotnet / 2

                if (indexWhile == 0)
                    return

                var pathSplit : ArrayList<String?> = arrayListOf()
                var currentIndexToRemove = 1

                while (indexWhile > 0) {
                    if (currentIndexToRemove == 1) {
                        val dirSplit = currentPath.subSequence(1, currentPath.length) .split('/')
                                as ArrayList<String?>

                        dirSplit.removeAt(dirSplit.size - 1)

                        pathSplit = dirSplit
                    } else {
                        pathSplit.removeAt(pathSplit.size - 1)
                    }

                    Log.v("CD Directory", pathSplit.toString())

                    currentIndexToRemove = currentIndexToRemove + 1
                    indexWhile = indexWhile - 1
                }

                var newPath = ""

                pathSplit.forEach { it ->
                    newPath += "/$it"
                }

                currentPath = newPath

                changeDirectory(currentPath)
            } else {
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

                changeDirectory(file.path)
            }

        } else if (cmd.toString().trim() == "clear") {
            resetTerminal()
        } else {
            commandNotFounded(context)
        }
    }

    private fun listFilesInTheCurrentDirectory() {
        val files = FilesController.returnFilesInTheSpecifyFile(currentPath)

        val listOfFilesSb = buildString {
            files.forEach { it ->
                appendLine(it.name)
            }
        }

        val newTextCmdSb = buildString {
            appendLine(currentTextCmd)
            appendLine(txtEditCmd.text.toString())
            appendLine()
            appendLine(listOfFilesSb)
        }

        currentTextCmd = newTextCmdSb

        txtCmd.text = newTextCmdSb

        val newTextEditTextCmd = buildString {
            append(currentPath)
            append("> ")
        }

        txtEditCmd.setText(newTextEditTextCmd)
    }

    private fun changeDirectory(directoryName: String?) {
        currentPath = directoryName.toString()

        val newTextCmdSb = buildString {
            appendLine(currentTextCmd)
            appendLine(txtEditCmd.text.toString())
        }

        currentTextCmd = newTextCmdSb

        txtCmd.text = newTextCmdSb

        val newTextEditTextCmd = buildString {
            append(currentPath)
            append("> ")
        }

        txtEditCmd.setText(newTextEditTextCmd)
    }

    private fun resetTerminal() {
        val newTextCmdSb = buildString {}

        currentTextCmd = newTextCmdSb.toString()

        txtCmd.text = newTextCmdSb

        val newTextEditTextCmd = buildString {
            append(currentPath)
            append("> ")
        }

        txtEditCmd.setText(newTextEditTextCmd)
    }

    private fun commandNotFounded(cmd: String?) {

    }
}