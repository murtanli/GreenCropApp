package com.example.greencropapp.Auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.greencropapp.MainActivity
import com.example.greencropapp.R
import com.example.greencropapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val errorText = findViewById<TextView>(R.id.error_text)
        val button_auth = findViewById<Button>(R.id.button_auth)
        val Edit_text_password = findViewById<TextView>(R.id.Edit_text_password)
        val Edit_text_login = findViewById<TextView>(R.id.Edit_text_login)
        val sign_in_text = findViewById<TextView>(R.id.sign_in_text)

        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val login_save = sharedPreferences.getString("login", "")

        if (login_save == "true"){
            val intent = Intent(this@Login, MainActivity::class.java)
            startActivity(intent)
        }

        sign_in_text.setOnClickListener {
            val intent = Intent(this, Sign_in::class.java)
            startActivity(intent)
        }

        button_auth.setOnClickListener {
            val loginText = Edit_text_login?.text?.toString()
            val passwordText = Edit_text_password?.text?.toString()

            if (loginText.isNullOrBlank() || passwordText.isNullOrBlank()) {
                errorText.text = "Введите данные в поля"
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        // Вызываем функцию logIn для выполнения запроса
                        val data = api_resource()
                        val result = data.log_in(loginText, passwordText)

                        if (result != null) {
                            if (result.message != "Пароль или логин введен неверно" && result.message != "Данного пользователя не существует") {
                                // Если успешно авторизованы, выводим сообщение об успешной авторизации и обрабатываем данные
                                Log.d("LoginActivity", "Login successful")
                                //Log.d("LoginActivity", "User ID: ${result.user_data.user_id}")
                                errorText.text = result.message

                                val sharedPreferences =
                                    getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                try {
                                    editor.putString("user_id", result.user_id.toString())

                                    Log.e("555", "${result.user_id}  ${result.message}")
                                    editor.putString("user_name", loginText)
                                    editor.putString("login", "true")
                                    editor.apply()
                                } catch (e: Exception) {
                                    editor.putString("user_id", result.user_id.toString())
                                    editor.putString("user_name", loginText)
                                    editor.putString("login", "true")
                                    editor.apply()
                                }

                                val intent = Intent(this@Login, MainActivity::class.java)
                                startActivity(intent)
                                //ErrorText.setTextColor(R.color.blue)

                            } else {
                                // Если произошла ошибка, выводим сообщение об ошибке
                                Log.e("LoginActivity", "Login failed")
                                errorText.text = result.message
                                val sharedPreferences =
                                    getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("login", "false")
                                editor.apply()
                            }
                        } else {
                            // Обработка случая, когда result равен null
                            Log.e("LoginActivity", "Login failed - result is null")
                            errorText.text = "Ошибка в процессе авторизации ${result.message}"
                        }
                    } catch (e: Exception) {
                        // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                        Log.e("LoginActivity", "Error during login", e)
                        e.printStackTrace()
                        errorText.text = "Ошибка входа: Неправильный пароль или профиль не найден"
                        val sharedPreferences =
                            getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("login", "false")
                        editor.apply()
                    }
                }
            }
        }

    }
    override fun onBackPressed() {

    }

}