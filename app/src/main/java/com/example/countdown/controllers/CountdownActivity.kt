@file:Suppress("NAME_SHADOWING")

package com.example.countdown.controllers

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.countdown.databinding.ActivityAgecalculatorBinding
import com.example.countdown.models.User
import com.example.countdown.utilities.EXTRA_USER
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.floor
import kotlin.math.roundToInt

class CountdownActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAgecalculatorBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgecalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(EXTRA_USER)
        ageCalculator(user?.dob.toString())

        binding.tvUserAgeInMicro.text = "${user?.name}'s micro age"

        binding.dateSelectorBtn.setOnClickListener {
            dateSelectorClicked()
        }
    }


     @SuppressLint("SetTextI18n")
     private fun dateSelectorClicked() {
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"

            ageCalculator(selectedDate)

            binding.tvUserAgeInMicro.text = "Age Calculator"

        }, year, month, day)

        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()
    }

     private fun ageCalculator(selectedDate : String) {
        //getting current & selected date in ms
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val theDate = simpleDateFormat.parse(selectedDate)
        val currentDate = simpleDateFormat.parse(simpleDateFormat.format(System.currentTimeMillis()))

        //total time from the date we selected till today in ms,day,years
        val totalMilliSeconds = currentDate!!.time - theDate!!.time
        val totalDays = TimeUnit.MILLISECONDS.toDays(totalMilliSeconds)
        val yearsInInt = (totalDays / 365).toDouble().roundToInt()
        val yearsInDecimal = totalDays / 365.toDouble()

        // months which does not make a full year eg. 10
        val remainingMonths = if (yearsInInt != 0) yearsInDecimal % yearsInInt * 12
        else totalDays / 30.toDouble()

        binding.ageYears.text = (totalDays / 365).toString()
        binding.ageMonths.text = floor(remainingMonths).toInt().toString()

        // so if the days are less than 30 that would be a zero month ( 22day => 0 Month 22 Days )
        // else they are distributed between months and days ( 70days => 2 Months and 10 Days)
        if (remainingMonths < 1) {
            binding.ageDays.text = floor(remainingMonths * 30).toInt().toString()
        } else {
            binding.ageDays.text = floor(remainingMonths % remainingMonths.toInt() * 30).toInt().toString()
        }

        binding.totalYears.text = yearsInInt.toString()
        binding.totalMonths.text = (totalDays / 30).toString()
        binding.totalWeeks.text = (totalDays / 7).toString()
        binding.totalDays.text = totalDays.toString()
        binding.totalHours.text = (totalMilliSeconds / 3600003).toString()
        binding.totalMinutes.text = (totalMilliSeconds / 60000).toString()
        binding.totalSeconds.text = (totalMilliSeconds / 1000).toString()

    }
}