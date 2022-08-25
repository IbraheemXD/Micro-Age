package com.example.countdown.controllers

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.countdown.databinding.ActivityMainBinding
import com.example.countdown.models.User
import com.example.countdown.utilities.EXTRA_USER
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = User("", "")

        binding.DOBSelector.setOnClickListener {
            val myCalender = Calendar.getInstance()
            val year = myCalender.get(Calendar.YEAR)
            val month = myCalender.get(Calendar.MONTH)
            val day = myCalender.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                user.dob = "$dayOfMonth/${month + 1}/$year"
                binding.DOBSelector.text = user.dob
            }, year, month, day)

            dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
            dpd.show()
        }

        binding.mainNextBtn.setOnClickListener {
            user.name = binding.userName.text.toString()

            if (user.name != "" && user.dob != "") {
               val countdownActivity = Intent(this, CountdownActivity::class.java)
                countdownActivity.putExtra(EXTRA_USER, user)
                startActivity(countdownActivity)
           } else {
               Toast.makeText(this, "Please enter a valid NAME and DOB", Toast.LENGTH_SHORT).show()
           }
        }
    }
}


