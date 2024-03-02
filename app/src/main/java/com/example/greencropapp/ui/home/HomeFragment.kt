package com.example.greencropapp.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.greencropapp.Auth.Login
import com.example.greencropapp.MainActivity
import com.example.greencropapp.R
import com.example.greencropapp.api.api_resource
import com.example.greencropapp.databinding.FragmentHomeBinding
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var num = 0

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var drawable: Drawable? = null

    /*private var air = 0
    private var soil = 0
    private var tempereture = 0
    private var light_sens = 0
    private var sensor_co2 = 0
    private var water_level = 0*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as? MainActivity)?.act_bar()

        val container = binding.containerBlock

        val title = TextView(requireContext())
        title.text = "Теплицы"
        title.setPadding(0,150,210,100)
        title.gravity = Gravity.CENTER
        title.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        title.textSize = 30F

        container.addView(title)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val user_id = sharedPreferences.getString("user_id", "")?.toIntOrNull()

                val data = api_resource()
                val result = data.get_all_user_gr_hs(user_id)
                Log.e("666", result.toString())
                if (result != null) {
                    for (res in result) {

                        num += 1
                        val imageview = RoundedImageView(requireContext())
                        when(num) {
                            1 -> {
                                imageview.setImageResource(R.drawable.one)
                            }
                            2 -> {
                                imageview.setImageResource(R.drawable.two)
                            }
                            3 -> {
                                imageview.setImageResource(R.drawable.three)
                                num = 0
                            }
                        }

                        GlobalScope.launch(Dispatchers.Main) {
                            try {
                                val data = api_resource()
                                val result2 = data.get_last_inf_sensors(
                                    res.pk
                                )
                                Log.e("777", result.toString())

                                if (result != null)  {
                                    val block = createGrHouseBlock(
                                        imageview,
                                        res.pk,
                                        res.name,
                                        res.imei,
                                        res.location,
                                        res.ventilation,
                                        res.watering,
                                        res.light,
                                        result2.air_humidity,
                                        result2.soil_moisture,
                                        result2.light,
                                        result2.sensor_co2,
                                        result2.water_level,
                                        result2.temperature
                                    )

                                    container.addView(block)
                                    container.gravity = Gravity.CENTER
                                } else {
                                    // Обработка случая, когда result равен null
                                    Log.e("LoginActivity", "Login failed - result is null") }
                            } catch (e: Exception) {
                                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                                Log.e("LoginActivity", "Error during login", e)
                                e.printStackTrace() }
                        }





                    }

                } else {
                    // Обработка случая, когда result равен null
                    Log.e("LoginActivity", "Login failed - result is null")
                }
            } catch (e: Exception) {
                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                Log.e("LoginActivity", "Error during login", e)
                e.printStackTrace()
            }
        }

        return root
    }

    private fun createGrHouseBlock(image: RoundedImageView, id: Int?, name: String, imei: String, location: String, ventilation: String, watering: String, light: String, air: Int, soil: Int, light_sens: Int, sensor_co2:Int, water_level: Int, tempereture:Int): LinearLayout {
        val block = LinearLayout(requireContext())
        //var air = 0
        //var soil = 0
        //var tempereture =0
        //var light_sens = 0
        //var sensor_co2 = 0
        //var water_level = 0



        Log.e("666", "$air $soil $tempereture $light_sens $sensor_co2 $water_level")

        val blockParams = LinearLayout.LayoutParams(
            1000,
            1200
        )
        blockParams.setMargins(0,0,125,0)
        blockParams.bottomMargin = 100
        block.layoutParams = blockParams
        block.orientation = LinearLayout.VERTICAL
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rouned_gr_hs)
        block.background = backgroundDrawable


        val imageLayputParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            500
        )

        //imageLayputParams.setMargins(20, 20, 20, 20)
        image.cornerRadius = 20F
        image.layoutParams = imageLayputParams
        image.scaleType = ImageView.ScaleType.CENTER_CROP

        //title
        val name_location = TextView(requireContext())
        name_location.text = "Мостоположение - $location"
        name_location.setTextAppearance(R.style.Text_locate)
        name_location.gravity = Gravity.CENTER
        //name_location.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_title)

        val location_Params = LinearLayout.LayoutParams (
            900,
            100
        )
        location_Params.setMargins(30, 20 ,20, 20)
        name_location.layoutParams = location_Params

        //title
        val name_text = TextView(requireContext())
        name_text.text = name
        name_text.setTextAppearance(R.style.Text_style)
        name_text.gravity = Gravity.CENTER
        name_text.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_title)

        val name_Params = LinearLayout.LayoutParams (
            200,
            100
        )
        name_Params.setMargins(30, 20 ,20, 20)
        name_text.layoutParams = name_Params

        //block znach
        val block_inf = LinearLayout(requireContext())
        val block_inf_params = LinearLayout.LayoutParams(
            900,
            100
        )
        block_inf_params.setMargins(50, 40, 0, 0)
        block_inf_params.bottomMargin = 10
        block_inf.layoutParams = block_inf_params
        //block.gravity = Gravity.CENTER
        block_inf.orientation = LinearLayout.HORIZONTAL

        //images

        val mini_imagesParams = LinearLayout.LayoutParams(
            180,
            100
        )

        val sun = RoundedImageView(requireContext())
        sun.setImageResource(R.drawable.sun)
        sun.layoutParams = mini_imagesParams

        val drops = RoundedImageView(requireContext())
        drops.setImageResource(R.drawable.fhumidity)
        drops.layoutParams = mini_imagesParams

        val soil_moisture = RoundedImageView(requireContext())
        soil_moisture.setImageResource(R.drawable.soil_moisture)
        soil_moisture.layoutParams = mini_imagesParams

        val co2 = RoundedImageView(requireContext())
        co2.setImageResource(R.drawable.co2)
        co2.layoutParams = mini_imagesParams

        val temperature_img = RoundedImageView(requireContext())
        temperature_img.setImageResource(R.drawable.temperature)
        temperature_img.layoutParams = mini_imagesParams

        //block znach inf
        val block_znach = LinearLayout(requireContext())
        val block_znach_params = LinearLayout.LayoutParams(
            900,
            1000
        )
        //block_znach_params.setMargins(50, 20, 0, 0)
        block_znach_params.bottomMargin = 10
        block_znach.layoutParams = block_inf_params
        //block.gravity = Gravity.CENTER
        block_znach.orientation = LinearLayout.HORIZONTAL

        val mini_inf = LinearLayout.LayoutParams(
            150,
            100
        )

        mini_inf.setMargins(22, 20, 0, 0)

        val text_sun = TextView(requireContext())
        val text_air = TextView(requireContext())
        val text_soil = TextView(requireContext())
        val text_co2 = TextView(requireContext())
        val text_temp = TextView(requireContext())

        text_sun.layoutParams = mini_inf
        text_air.layoutParams = mini_inf
        text_soil.layoutParams = mini_inf
        text_co2.layoutParams = mini_inf
        text_temp.layoutParams = mini_inf

        text_sun.text = "${light_sens.toString()}%"
        text_air.text = "${air.toString()}%"
        text_soil.text = "${soil.toString()}%"
        text_co2.text = "${sensor_co2.toString()}%"
        text_temp.text = "${tempereture.toString()}C"

        text_sun.setTextAppearance(R.style.Title_style)
        text_air.setTextAppearance(R.style.Title_style)
        text_soil.setTextAppearance(R.style.Title_style)
        text_co2.setTextAppearance(R.style.Title_style)
        text_temp.setTextAppearance(R.style.Title_style)

        block_znach.addView(text_sun)
        block_znach.addView(text_air)
        block_znach.addView(text_soil)
        block_znach.addView(text_co2)
        block_znach.addView(text_temp)

        block_inf.addView(sun)
        block_inf.addView(drops)
        block_inf.addView(soil_moisture)
        block_inf.addView(co2)
        block_inf.addView(temperature_img)



        block.addView(image)
        block.addView(name_text)
        block.addView(name_location)
        block.addView(block_inf)
        block.addView(block_znach)

        block.setOnClickListener {
            val intent = Intent(requireContext(), GreenHouseInfo::class.java)
            intent.putExtra("name", name)
            intent.putExtra("id", id)
            Log.e("7777", id.toString())
            intent.putExtra("ventilation", ventilation)
            intent.putExtra("watering", watering)
            intent.putExtra("light", light)
            startActivity(intent)


        }


        return block
    }

    /*override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = api_resource()
                val result = data.get_last_inf_sensors(
                    id
                )
                Log.e("777", result.toString())

                if (result != null)  {
                    air = result.air_humidity
                    soil = result.soil_moisture
                    tempereture = result.temperature
                    light_sens = result.light
                    sensor_co2 = result.sensor_co2
                    water_level = result.water_level
                } else {
                    // Обработка случая, когда result равен null
                    Log.e("LoginActivity", "Login failed - result is null") }
            } catch (e: Exception) {
                // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                Log.e("LoginActivity", "Error during login", e)
                e.printStackTrace() }
        }

    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}