package com.android.appcomponents.customui.datepicker

import java.util.*

interface UICalendarListener {
    fun onDateSelected(date: Date)
    fun onLongClick(date: Date)
    fun onMonthChanged(date: Date)
}