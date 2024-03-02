package com.example.greencropapp.Auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.greencropapp.R
import com.example.greencropapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Sign_in : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        supportActionBar?.hide()

        val button_auth = findViewById<Button>(R.id.button_auth)
        val Edit_text_password = findViewById<EditText>(R.id.Edit_text_password)
        val Edit_text_login = findViewById<EditText>(R.id.Edit_text_login)
        val Edit_text_password_two = findViewById<TextView>(R.id.Edit_text_password_two)
        val error_text = findViewById<TextView>(R.id.error_text_sign)

        button_auth.setOnClickListener {
            if (!Edit_text_login.text.isNullOrEmpty() && !Edit_text_password.text.isNullOrEmpty() && !Edit_text_password_two.text.isNullOrEmpty()) {

                val loginText = Edit_text_login?.text?.toString()
                val passwordText = Edit_text_password?.text?.toString()
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val data = api_resource()
                        val result = data.sign_in(
                            loginText.toString(),
                            passwordText.toString())

                        if (result != null) {
                            val intent = Intent(this@Sign_in, Login::class.java)
                            startActivity(intent)
                            error_text.text = result.message

                            val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("login", "false")
                            editor.apply()

                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            error_text.text = "Ошибка в процессе авторизации ${result.message}"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        error_text.text = "Ошибка входа: Неправильный пароль или профиль уже существует"
                    }
                }
            } else {
                error_text.text = "Пустые поля ! либо пороли не совпадают"
            }
        }
    }
}