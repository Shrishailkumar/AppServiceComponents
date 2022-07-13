package com.android.appcomponents.customui.datepicker

import android.content.Context
import android.util.AttributeSet
import java.text.SimpleDateFormat
import java.util.*

class DayViewCell : androidx.appcompat.widget.AppCompatTextView {

    private var date: Date? = null
    private var decorators: List<DayDecorator>? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun bind(date: Date, decorators: List<DayDecorator>?) {
        this.date = date
        this.decorators = decorators
        val df = SimpleDateFormat("d")
        val day: Int = df.format(date).toInt()
        text = day.toString()
    }

    fun decorate() {
        if (null != decorators) {
            for (decorator in decorators!!) {
                decorator.decorate(this)
            }
        }
    }

    fun getDate(): Date? {
        return date
    }
}