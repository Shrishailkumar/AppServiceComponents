package com.android.appcomponents.customui.timepicker

import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat
import java.util.*

class UITimePicker {

    fun materialTimeBuilder(
        title: String = ""
    ): MaterialTimePicker.Builder {
        val calendar: Calendar = Calendar.getInstance()
        val hmFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

        val list: List<String> = hmFormat.format(calendar.time).split(":")

        val cHours: Int = list[0].toInt()
        val cMinute: Int = list[1].toInt()

        return MaterialTimePicker.Builder()
            .setTitleText(title)
            .setHour(cHours)
            .setMinute(cMinute)
    }

    /*fun materialTimeBuilder(
        title: String = "",

    ): MaterialTimePicker.Builder {
        val calendar: Calendar = Calendar.getInstance()
        val hmFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

        val list: List<String> = hmFormat.format(calendar.time).split(":")

        val cHours: Int = list[0].toInt()
        val cMinute: Int = list[1].toInt()

        return MaterialTimePicker.Builder()
            .setTitleText(title)
            .setHour(cHours)
            .setMinute(cMinute)
    }*/

}