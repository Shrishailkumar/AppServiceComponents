package com.android.appcomponents.customui.timepicker

import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat
import java.util.*

class UITimePicker {

    /**
     * Method is to build time picker
     * @param title - show title at the top of your time picker
     * @return MaterialTimePicker object
     */
    fun materialTimeBuilder(
        title: String?
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

    /**
     * Method to show material time picker
     * @param hours - hours should range from 0 to 23
     * @param minute - minute should range from 0 to 59
     */
    fun materialTimeBuilder(
        @IntRange(from = 0, to = 23) hours: Int,
        @IntRange(from = 0, to = 59) minute: Int,
    ): MaterialTimePicker.Builder {
        return MaterialTimePicker.Builder()
            .setHour(hours)
            .setMinute(minute)
    }

    /**
     * Method to show material time picker
     * @param title - Title to show in the time picker dialog
     * @param hours - hours should range from 0 to 23
     * @param minute - minute should range from 0 to 59
     */
    fun materialTimeBuilder(
        title: String?,
        @IntRange(from = 0, to = 23) hours: Int,
        @IntRange(from = 0, to = 59) minute: Int
    ): MaterialTimePicker.Builder {

        return MaterialTimePicker.Builder()
            .setTitleText(title)
            .setHour(hours)
            .setMinute(minute)
    }

    /**
     * Method to show material time picker
     * @param hours - hours should range from 0 to 23
     * @param minute - minute should range from 0 to 59
     * @param theme - custom theme
     */
    fun materialTimeBuilder(
        @IntRange(from = 0, to = 23) hours: Int,
        @IntRange(from = 0, to = 59) minute: Int,
        @StyleRes theme: Int
    ): MaterialTimePicker.Builder {

        return MaterialTimePicker.Builder()
            .setHour(hours)
            .setMinute(minute)
            .setTheme(theme)
    }

    /**
     * Method to show material time picker
     * @param theme - custom theme
     */
    fun materialTimeBuilder(
        @StyleRes theme: Int
    ): MaterialTimePicker.Builder {

        val calendar: Calendar = Calendar.getInstance()
        val hmFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)

        val list: List<String> = hmFormat.format(calendar.time).split(":")

        val cHours: Int = list[0].toInt()
        val cMinute: Int = list[1].toInt()

        return MaterialTimePicker.Builder()
            .setHour(cHours)
            .setMinute(cMinute)
            .setTheme(theme)
    }

    /**
     * Method to show material time picker
     * @param title - Title to show in the time picker dialog
     * @param hours - hours should range from 0 to 23
     * @param minute - minute should range from 0 to 59
     * @param timeFormat - timeFormat should be TimeFormat.CLOCK_12H or TimeFormat.CLOCK_24H
     */
    fun materialTimeBuilder(
        title: String?,
        @IntRange(from = 0, to = 23) hours: Int,
        @IntRange(from = 0, to = 59) minute: Int,
        @com.google.android.material.timepicker.TimeFormat timeFormat: Int,
    ): MaterialTimePicker.Builder {

        return MaterialTimePicker.Builder()
            .setTitleText(title)
            .setHour(hours)
            .setMinute(minute)
            .setTimeFormat(timeFormat)
    }

    /**
     * Method to show material time picker
     * @param title - Title to show in the time picker dialog
     * @param hours - hours should range from 0 to 23
     * @param minute - minute should range from 0 to 59
     * @param timeFormat - timeFormat should be TimeFormat.CLOCK_12H or TimeFormat.CLOCK_24H
     * @param inputFormat - inputFormat should be MaterialTimePicker.INPUT_MODE_KEYBOARD or MaterialTimePicker.INPUT_MODE_CLOCK
     */
    fun materialTimeBuilder(
        title: String?,
        @IntRange(from = 0, to = 23) hours: Int,
        @IntRange(from = 0, to = 59) minute: Int,
        @com.google.android.material.timepicker.TimeFormat timeFormat: Int,
        inputFormat: Int
    ): MaterialTimePicker.Builder {

        return if (inputFormat != MaterialTimePicker.INPUT_MODE_KEYBOARD
            || inputFormat != MaterialTimePicker.INPUT_MODE_CLOCK
        )
            throw IllegalArgumentException("Invalid inputFormat")
        else
            MaterialTimePicker.Builder()
                .setTitleText(title)
                .setHour(hours)
                .setMinute(minute)
                .setTimeFormat(timeFormat)
                .setInputMode(inputFormat)
    }

}