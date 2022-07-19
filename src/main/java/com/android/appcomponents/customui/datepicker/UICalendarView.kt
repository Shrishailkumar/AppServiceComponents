package com.android.appcomponents.customui.datepicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.appcomponents.R
import java.text.DateFormatSymbols
import java.util.*


const val DAY_OF_WEEK = "dayOfWeek"
const val DAY_OF_MONTH_TEXT = "dayOfMonthText"
const val DAY_OF_MONTH_CONTAINER = "dayOfMonthContainer"

class UICalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mContext: Context?
    private var view: View? = null
    private var previousMonthButton: ImageView? = null
    private var nextMonthButton: ImageView? = null
    private var uiCalendarListener: UICalendarListener? = null
    private var currentCalendar: Calendar? = null
    private var locale: Locale = Locale.ENGLISH
    private var lastSelectedDay: Date? = null
    private var customTypeface: Typeface? = null

    var initDayOfWeek: Int = Calendar.SUNDAY
    private var decorators: List<DayDecorator>? = emptyList()

    var disabledDayBackgroundColor = 0
    var disabledDayTextColor = 0
    var calendarBackgroundColor = 0
    var calendarHeaderBackgroundColor = 0
    var calendarTitleBackgroundColor = 0
    var selectedDayBackground = 0
    var weekLayoutBackgroundColor = 0
    var selectedDayTextColor = 0
    var calendarTitleTextColor = 0
    var dayOfWeekTextColor = 0
    var dayOfMonthTextColor = 0
    var currentDayOfMonth = 0

    var currentMonthIndex = 0
    var isOverflowDateVisible: Boolean = true


    /*constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)*/

    init {
        this.mContext = context
        initializeCalendar()
    }

    /**
     * Method is to initialize calendar layout with custom input
     */
    private fun initializeCalendar() {
        val inflate = mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflate.inflate(R.layout.ui_calendar_layout, this, true)
        previousMonthButton = view!!.findViewById<View>(R.id.leftButton) as ImageView
        nextMonthButton = view!!.findViewById<View>(R.id.rightButton) as ImageView

        previousMonthButton?.setImageDrawable(ContextCompat.getDrawable(mContext!!, R.drawable.previous_month_selector))
        nextMonthButton?.setImageDrawable(ContextCompat.getDrawable(mContext!!, R.drawable.next_month_selector))

        previousMonthButton!!.setOnClickListener {
            currentMonthIndex--
            currentCalendar = Calendar.getInstance(Locale.getDefault())
            currentCalendar?.add(Calendar.MONTH, currentMonthIndex)
            refreshCalendar(currentCalendar)
            if (uiCalendarListener != null) {
                uiCalendarListener?.onMonthChanged(currentCalendar!!.time)
            }
        }
        nextMonthButton!!.setOnClickListener {
            currentMonthIndex++
            currentCalendar = Calendar.getInstance(Locale.getDefault())
            currentCalendar?.add(Calendar.MONTH, currentMonthIndex)
            refreshCalendar(currentCalendar)
            if (uiCalendarListener != null) {
                uiCalendarListener?.onMonthChanged(currentCalendar!!.time)
            }
        }

        // Initialize calendar for current month
        val locale = mContext!!.resources.configuration.locale
        val currentCalendar = Calendar.getInstance(locale)
//        setinitDayOfWeek(Calendar.SUNDAY)
        refreshCalendar(currentCalendar)
    }


    /**
     * Display calendar title with next previous month button
     */
    private fun initializeTitleLayout() {
        val titleLayout = view!!.findViewById<View>(R.id.titleLayout)
        titleLayout.setBackgroundColor(calendarHeaderBackgroundColor)
        var dateText: String =
            DateFormatSymbols(locale).shortMonths[currentCalendar!![Calendar.MONTH]]
                .toString()
        dateText = dateText.substring(0, 1).uppercase(Locale.getDefault()) + dateText.subSequence(
            1,
            dateText.length
        )
        val dateTitle = view!!.findViewById<View>(R.id.dateTitle) as TextView
        dateTitle.setTextColor(calendarTitleTextColor)
        dateTitle.text = dateText + " " + currentCalendar!![Calendar.YEAR]
        dateTitle.setBackgroundColor(calendarTitleBackgroundColor)
        if (null != customTypeface) {
            dateTitle.setTypeface(customTypeface, Typeface.BOLD)
        }
    }

    /**
     * Initialize the calendar week layout, considers start day
     */
    @SuppressLint("DefaultLocale")
    private fun initializeWeekLayout() {
        var dayOfWeek: TextView
        var dayOfTheWeekString: String

        //Setting background color white
        val titleLayout = view!!.findViewById<View>(R.id.weekLayout)
        titleLayout.setBackgroundColor(weekLayoutBackgroundColor)
        val weekDaysArray: Array<String> = DateFormatSymbols(locale).shortWeekdays
        for (i in 1 until weekDaysArray.size) {
            dayOfTheWeekString = weekDaysArray[i]
            if (dayOfTheWeekString.length > 3) {
                dayOfTheWeekString =
                    dayOfTheWeekString.substring(0, 3).uppercase(Locale.getDefault())
            }
            dayOfWeek = view!!.findViewWithTag<View>(
                DAY_OF_WEEK + getWeekIndex(
                    i,
                    currentCalendar!!
                )
            ) as TextView
            dayOfWeek.text = dayOfTheWeekString
            dayOfWeek.setTextColor(dayOfWeekTextColor)
            if (null != customTypeface) {
                dayOfWeek.typeface = customTypeface
            }
        }
    }

    /**
     * Method is to set days in the calendar layout
     */
    private fun setDaysInCalendar() {
        val calendar = Calendar.getInstance(locale)
        calendar.time = currentCalendar!!.time
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar.firstDayOfWeek = initDayOfWeek
        val firstDayOfMonth = calendar[Calendar.DAY_OF_WEEK]

        // Calculate dayOfMonthIndex
        var dayOfMonthIndex = getWeekIndex(firstDayOfMonth, calendar)
        val actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val startCalendar = calendar.clone() as Calendar
        //Add required number of days
        startCalendar.add(Calendar.DATE, -(dayOfMonthIndex - 1))
        val monthEndIndex = 42 - (actualMaximum + dayOfMonthIndex - 1)
        var dayView: DayViewCell
        var dayOfMonthContainer: ViewGroup
        for (i in 1..42) {
            dayOfMonthContainer =
                view!!.findViewWithTag<View>(DAY_OF_MONTH_CONTAINER + i) as ViewGroup
            dayView = view!!.findViewWithTag<View>(DAY_OF_MONTH_TEXT + i) as DayViewCell

            //Apply the default styles
            dayOfMonthContainer.setOnClickListener(null)
            dayView.bind(startCalendar.time, decorators)
            dayView.visibility = VISIBLE
            if (null != customTypeface) {
                dayView.typeface = customTypeface
            }
            if (UICalendarUtil.isSameMonth(calendar, startCalendar)) {
                dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener)
                dayOfMonthContainer.setOnLongClickListener(onDayLongPressListener)
                dayView.setBackgroundColor(calendarBackgroundColor)
                dayView.setTextColor(dayOfWeekTextColor)
                //Set the current day color
                markDayAsCurrentDay(startCalendar)
            } else {
                dayView.setBackgroundColor(disabledDayBackgroundColor)
                dayView.setTextColor(disabledDayTextColor)
                if (!isOverflowDateVisible) dayView.visibility =
                    GONE else if (i >= 36 && monthEndIndex.toFloat() / 7.0f >= 1) {
                    dayView.visibility = GONE
                }
            }
            dayView.decorate()
            startCalendar.add(Calendar.DATE, 1)
            dayOfMonthIndex++
        }

        // If the last week row has no visible days, hide it or show it in case
        val weekRow = view!!.findViewWithTag<View>("weekRow6") as ViewGroup
        dayView = view!!.findViewWithTag<View>("dayOfMonthText36") as DayViewCell
        if (dayView.visibility !== VISIBLE) {
            weekRow.visibility = GONE
        } else {
            weekRow.visibility = VISIBLE
        }
    }

    /**
     * Method will either show all days in a particular month or highlight previous month's days with particular month
     *  @param currentDate - current date
     */
    private fun clearDayOfTheMonthStyle(currentDate: Date?) {
        if (currentDate != null) {
            val calendar = getTodaysCalendar()
            calendar.firstDayOfWeek = initDayOfWeek
            calendar.time = currentDate
            val dayView: DayViewCell = getDayOfMonthText(calendar)
            dayView.setBackgroundColor(calendarBackgroundColor)
            dayView.setTextColor(dayOfWeekTextColor)
            dayView.decorate()
        }
    }

    /**
     * Method to get days of month
     * @param currentCalendar - calendar of current month
     */
    private fun getDayOfMonthText(currentCalendar: Calendar): DayViewCell {
        return getView(currentCalendar) as DayViewCell
    }

    /**
     * Method to get day index by date
     * @param currentCalendar - calendar of current month
     */
    private fun getDayIndexByDate(currentCalendar: Calendar): Int {
        val monthOffset = getMonthOffset(currentCalendar)
        val currentDay = currentCalendar[Calendar.DAY_OF_MONTH]
        return currentDay + monthOffset
    }

    /**
     * Method to get month
     * @param currentCalendar - calendar of current month
     */
    private fun getMonthOffset(currentCalendar: Calendar): Int {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = initDayOfWeek
        calendar.time = currentCalendar.time
        calendar[Calendar.DAY_OF_MONTH] = 1
        val firstDayWeekPosition = calendar.firstDayOfWeek
        val dayPosition = calendar[Calendar.DAY_OF_WEEK]
        return if (firstDayWeekPosition == 1) {
            dayPosition - 1
        } else {
            if (dayPosition == 1) {
                6
            } else {
                dayPosition - 2
            }
        }
    }

    /**
     * Method to get week based on index
     * @param weekIndex - week index of a month
     * @param currentCalendar - current month calendar
     */
    private fun getWeekIndex(weekIndex: Int, currentCalendar: Calendar): Int {
        val firstDayWeekPosition = currentCalendar.firstDayOfWeek
        return if (firstDayWeekPosition == 1) {
            weekIndex
        } else {
            if (weekIndex == 1) {
                7
            } else {
                weekIndex - 1
            }
        }
    }

    /**
     * Method to get view of calendar
     * @param currentCalendar - current month calendar
     * @return view of a calendar
     */
    private fun getView(currentCalendar: Calendar): View {
        val index = getDayIndexByDate(currentCalendar)
        return view!!.findViewWithTag(DAY_OF_MONTH_TEXT + index)
    }

    /**
     * Method to get today's calendar
     */
    private fun getTodaysCalendar(): Calendar {
        val currentCalendar = Calendar.getInstance(mContext!!.resources.configuration.locale)
        currentCalendar.firstDayOfWeek = initDayOfWeek
        return currentCalendar
    }

    /**
     * Method to notify calendar changes
     * @param currentCalendar - current month calendar
     */
    @SuppressLint("DefaultLocale")
    fun refreshCalendar(currentCalendar: Calendar?) {
        this.currentCalendar = currentCalendar
        this.currentCalendar!!.firstDayOfWeek = initDayOfWeek
        locale = mContext!!.resources.configuration.locale

        // Set date title
        initializeTitleLayout()

        // Set weeks days titles
        initializeWeekLayout()

        // Initialize and set days in calendar
        setDaysInCalendar()
    }

    /**
     * Method to mark day as current day
     * @param calendar - current month calendar
     */
    private fun markDayAsCurrentDay(calendar: Calendar?) {
        if (calendar != null && UICalendarUtil.isToday(calendar)) {
            val dayOfMonth: DayViewCell = getDayOfMonthText(calendar)
            dayOfMonth.setTextColor(currentDayOfMonth)
        }
    }

    /**
     * Method to mark selected day
     * @param currentDate - current date
     */
    private fun markDayAsSelectedDay(currentDate: Date) {
        val currentCalendar = getTodaysCalendar()
        currentCalendar.firstDayOfWeek = initDayOfWeek
        currentCalendar.time = currentDate

        // Clear previous marks
        clearDayOfTheMonthStyle(lastSelectedDay)

        // Store current values as last values
        storeLastValues(currentDate)

        // Mark current day as selected
        val view: DayViewCell = getDayOfMonthText(currentCalendar)
        view.setBackgroundColor(selectedDayBackground)
        view.setTextColor(selectedDayTextColor)
    }

    /**
     * Method to store last selected date value
     * @param currentDate - date
     */
    private fun storeLastValues(currentDate: Date) {
        lastSelectedDay = currentDate
    }

    /**
     * Method to set listener for calendar event click
     * @param uiCalendarListener - callback method for click, long press & month changed
     */
    fun setUICalendarListener(uiCalendarListener: UICalendarListener) {
        this.uiCalendarListener = uiCalendarListener
    }

    private val onDayOfMonthClickListener =
        OnClickListener { view -> // Extract day selected
            val dayOfMonthContainer = view as ViewGroup
            var tagId = dayOfMonthContainer.tag as String
            tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length, tagId.length)
            val dayOfMonthText = view.findViewWithTag<View>(DAY_OF_MONTH_TEXT + tagId) as TextView

            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = initDayOfWeek
            calendar.time = currentCalendar!!.time
            calendar[Calendar.DAY_OF_MONTH] = Integer.valueOf(dayOfMonthText.text.toString())
            markDayAsSelectedDay(calendar.time)

            //Set the current day color
            markDayAsCurrentDay(currentCalendar)
            if (uiCalendarListener != null) uiCalendarListener?.onDateSelected(calendar.time)
        }

    private val onDayLongPressListener =
        OnLongClickListener { view -> // Extract day selected
            val dayOfMonthContainer = view as ViewGroup
            var tagId = dayOfMonthContainer.tag as String
            tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length, tagId.length)
            val dayOfMonthText = view.findViewWithTag<View>(DAY_OF_MONTH_TEXT + tagId) as TextView

            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = initDayOfWeek
            calendar.time = currentCalendar!!.time
            calendar[Calendar.DAY_OF_MONTH] = Integer.valueOf(dayOfMonthText.text.toString())
            markDayAsSelectedDay(calendar.time)

            //Set the current day color
            markDayAsCurrentDay(currentCalendar)
            if (uiCalendarListener != null) uiCalendarListener?.onLongClick(calendar.time)
            true
        }


}