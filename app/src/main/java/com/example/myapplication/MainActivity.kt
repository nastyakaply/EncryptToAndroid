package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private var encryptMessage: String? = null
    private lateinit var resTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    fun onEncryptBtnClick(message: String) {
        try {
            if(checkMessage(message)) {
                val deferred = GlobalScope.async() {
                    RSA.encrypt(message)
                }
                runBlocking {
                    encryptMessage = deferred.await()
                    resTextView.text = encryptMessage
                    Toast.makeText(
                        applicationContext,
                        "Время шифрования: ${RSA.timeRun}",
                        LENGTH_LONG
                    ).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, LENGTH_LONG).show()
        }
    }

    fun onDecryptBtnClick() {
        try {
            if(encryptMessage.isNullOrEmpty()) {
                Toast.makeText(applicationContext, "Расшифровывать нечего!!!", LENGTH_SHORT).show()
            } else {
                val deferred = GlobalScope.async() {
                    RSA.decrypt(encryptMessage!!)
                }
                runBlocking {
                    resTextView.text = deferred.await()
                    encryptMessage = null
                    Toast.makeText(applicationContext, "Время расшифрования: ${RSA.timeRun}", LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, LENGTH_LONG).show()
        }

    }

    fun checkMessage(message: String): Boolean {
        val isValidMessage = message.isNotEmpty()
        if(!isValidMessage) {
            Toast.makeText(applicationContext, "Зашифровывать нечего!!!", LENGTH_SHORT).show()
        }
        return isValidMessage
    }

    fun initUI() {
        val encryptBtn = findViewById<Button>(R.id.encryptBtn)
        val decryptBtn = findViewById<Button>(R.id.decryptBtn)
        val editText = findViewById<EditText>(R.id.editText)
        val encryptTestBtn = findViewById<Button>(R.id.encryptTestBtn)
        resTextView = findViewById<TextView>(R.id.resEncrypt)

        encryptBtn.setOnClickListener { onEncryptBtnClick(editText.text.toString()) }
        encryptTestBtn.setOnClickListener { onEncryptBtnClick(testText) }
        decryptBtn.setOnClickListener { onDecryptBtnClick() }

    }
}