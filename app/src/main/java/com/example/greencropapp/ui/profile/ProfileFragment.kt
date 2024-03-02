package com.example.greencropapp.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.greencropapp.Auth.Login
import com.example.greencropapp.MainActivity
import com.example.greencropapp.R
import com.example.greencropapp.api.api_resource
import com.example.greencropapp.databinding.FragmentProfileBinding
import com.example.greencropapp.ui.home.GreenHouseInfo
import com.makeramen.roundedimageview.RoundedImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (activity as? MainActivity)?.act_bar()

        val container = binding.containerBlock
        val nickname = binding.nickName
        val button_exit = binding.buttonExit
        val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        nickname.text =  "Логин - ${sharedPreferences.getString(" user_name ", "")}"

        button_exit.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.remove("user_id")
            editor.remove("user_name")
            editor.putString("login", "false")
            editor.apply()

            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val sharedPreferences = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
                val user_id = sharedPreferences.getString("user_id", "")?.toIntOrNull()

                val data = api_resource()
                val result = data.get_all_user_gr_hs(user_id)
                Log.e("666", result.toString())
                if (result != null) {
                    for (res in result) {

                        val block = createGrHouseBlock(
                            res.pk,
                            res.name,
                            res.imei,
                            res.location,
                            res.ventilation,
                            res.watering,
                            res.light,

                            )
                        container.addView(block)
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

    private fun createGrHouseBlock(id: Int?, name: String, imei: String, location: String, ventilation: String, watering: String, light: String): LinearLayout {
        val block = LinearLayout(requireContext())


        val blockParams = LinearLayout.LayoutParams(
            1000,
            200
        )
        blockParams.setMargins(0,0,125,0)
        blockParams.bottomMargin = 100
        block.layoutParams = blockParams
        block.orientation = LinearLayout.HORIZONTAL
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.rouned_gr_hs)
        block.background = backgroundDrawable

        val name_text = TextView(requireContext())
        name_text.text = "Теплица - $name"
        name_text.setTextAppearance(R.style.Text_style)
        name_text.gravity = Gravity.CENTER

        name_text.background = ContextCompat.getDrawable(requireContext(), R.drawable.rounded_title)

        val name_Params = LinearLayout.LayoutParams (
            500,
            200
        )
        name_Params.setMargins(0, 20 ,20, 20)
        name_text.layoutParams = name_Params


        val name_del = TextView(requireContext())
        name_del.text = "Удалить"
        name_del.setTextAppearance(R.style.Text_locate)
        name_del.gravity = Gravity.CENTER


        val del_Params = LinearLayout.LayoutParams (
            500,
            200
        )
        del_Params.setMargins(0, 20 ,20, 20)
        name_del.layoutParams = del_Params

        block.addView(name_text)
        block.addView(name_del)

        name_del.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val data = api_resource()
                    val result = data.delete_grh_user(id)

                    if (result != null) {
                        Toast.makeText(requireContext(), "Удалено !", Toast.LENGTH_LONG).show()
                    } else {
                        // Обработка случая, когда result равен null
                        Log.e("LoginActivity", "Login failed - result is null")
                        Toast.makeText(requireContext(), "Ошибка !", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    // Ловим и обрабатываем исключения, например, связанные с сетевыми ошибками
                    Log.e("LoginActivity", "Error during login", e)
                    e.printStackTrace()
                }
            }
        }

        return block
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}