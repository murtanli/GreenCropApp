package com.example.greencropapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.greencropapp.Auth.Login
import com.example.greencropapp.MainActivity
import com.example.greencropapp.R
import com.example.greencropapp.api.api_resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class GreenHouseInfo : AppCompatActivity() {

    private var id = 0

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_green_house_info)

        val name = intent.getStringExtra("name")
        id = intent.getIntExtra("id", 0)
        val ventilation = intent.getStringExtra("ventilation").toBoolean()
        val watering = intent.getStringExtra("watering").toBoolean()
        val light = intent.getStringExtra("light").toBoolean()

        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val switch_ventilaion = findViewById<Switch>(R.id.switch_ventilaion)
        val switch_watering = findViewById<Switch>(R.id.switch_watering)
        val switch_light = findViewById<Switch>(R.id.switch_light)

        switch_ventilaion.isChecked = ventilation
        switch_watering.isChecked = watering
        switch_light.isChecked = light

        switch_ventilaion.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                send_panel(id, "ventilation", "True")
            } else {
                send_panel(id, "ventilation", "False")
            }
        }

        switch_watering.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                send_panel(id, "watering", "True")
            } else {
                send_panel(id, "watering", "False")
            }
        }

        switch_light.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                send_panel(id, "light", "True")
            } else {
                send_panel(id, "light", "False")
            }
        }


    }

    fun send_panel(id: Int,  control_name:String, control_value: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.change_control(
                    id,
                    control_name,
                    control_value
                )

                if (result != null) {
                    Toast.makeText(this@GreenHouseInfo, result.message, Toast.LENGTH_LONG).show()
                } else {
                    // Обработка случая, когда result равен null
                    Log.e("LoginActivity", "Login failed - result is null")
                    Toast.makeText(this@GreenHouseInfo, result.message, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                Log.e("LoginActivity", "Error during login", e)
                e.printStackTrace()
            }
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val data = api_resource()
                    val result = data.create_data(
                        id,
                        Random.nextInt(100),
                        Random.nextInt(100),
                        Random.nextInt(34),
                        Random.nextInt(100),
                        Random.nextInt(1000),
                        Random.nextInt(100)
                    )

                    if (result != null) {
                        Toast.makeText(this@GreenHouseInfo, "Создано!", Toast.LENGTH_SHORT).show()

                    } else {
                        // Обработка случая, когда result равен null
                        Log.e("LoginActivity", "Login failed - result is null")
                        Toast.makeText(this@GreenHouseInfo, "ошибка!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                    Log.e("LoginActivity", "Error during login", e)
                    e.printStackTrace()
                }
            }
            return true // Возвращаем true, чтобы сообщить системе, что событие было обработано
        } else if (event?.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            // Обработка нажатия кнопки уменьшения громкости
            return true // Возвращаем true, чтобы сообщить системе, что событие было обработано
        }
        return super.dispatchKeyEvent(event) // Передаем событие дальше, если это не кнопки громкости
    }


}