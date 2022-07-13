package com.android.appcomponents.customui.datepicker

import java.util.*

class UICalendarUtil {
    companion object {
        fun isSameMonth(calendar1: Calendar?, calendar2: Calendar?): Boolean {
            if (calendar1 == null || calendar2 == null)
                return false;
            return (calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA)
                    && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                    && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH));
        }

        fun isToday(calendar: Calendar): Boolean {
            return isSameDay(calendar, Calendar.getInstance())
        }

        fun getTotalWeeks(date: Date): Int {
            val cal = Calendar.getInstance()
            cal.time = date
            return getTotalWeeks(cal)
        }

        private fun getTotalWeeks(calendar: Calendar?): Int {
            return calendar?.getActualMaximum(Calendar.WEEK_OF_MONTH) ?: 0
        }

        private fun isSameDay(calendar1: Calendar?, calendar2: Calendar?): Boolean {
            if (calendar1 == null || calendar2 == null)
                throw IllegalArgumentException("The dates must not be null");
            return (calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA) &&
                    calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                    calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));
        }

        fun isPastDay(date: Date): Boolean {
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            return date.before(calendar.time)
        }
    }
}