package com.dema.campanelishell

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
        val command = allTheEditText.subSequence("$currentPath> ".length, allTheEditText.length)
        //Toast.makeText(this, command, Toast.LENGTH_LONG).show()
        Log.v("Command text", command.toString())

        val newText = "Command Not Founded"

        setTextCmdAndEditTextCmd(newText)
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

    fun returnCurrentFilePath() {

    }
}