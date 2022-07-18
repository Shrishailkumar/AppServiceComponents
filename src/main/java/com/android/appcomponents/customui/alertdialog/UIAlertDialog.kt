package com.android.appcomponents.customui.alertdialog

import android.app.Activity
import android.graphics.Typeface
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import com.android.appcomponents.R
import kotlinx.android.synthetic.main.ui_alert_dilaog.*

class UIAlertDialog {
    /***
     * Positions For Alert Dialog
     * */
    enum class POSITIONS {
        TOP, CENTER, BOTTOM
    }

    companion object {

        /***
         * core method For Alert Dialog
         * @param context
         * @return AlertDialog
         * */
        fun build(
            context: Activity
        ): AlertDialog {
            val alertDialog =
                AlertDialog.Builder(context, R.style.full_screen_dialog)
                    .setView(R.layout.ui_alert_dilaog)
            val alert: AlertDialog = alertDialog.create()
            alert.show()
            return alert
        }

        /***
         * core method For Alert Dialog
         * @param context
         * @return AlertDialog
         * */
        fun build(
            context: Activity,
            view: View
        ): AlertDialog {
            val alertDialog =
                AlertDialog.Builder(context, R.style.full_screen_dialog)
                    .setView(view)
            val alert: AlertDialog = alertDialog.create()
            alert.show()
            return alert
        }
    }
}

/***
 * Title Properties For Alert Dialog
 * @param title - Set title for the alert
 * @param fontStyle - Set text font style for the text
 * @param titleColor - set color for the title
 * @return AlertDialog
 * */
fun AlertDialog.title(
    title: String,
    fontStyle: Typeface? = null,
    titleColor: Int = 0
): AlertDialog {
    this.title.text = title.trim()
    if (fontStyle != null) {
        this.title.typeface = fontStyle
    }
    if (titleColor != 0) {
        this.title.setTextColor(titleColor)
    }
    this.title.show()
    return this
}

/***
 * Dialog Background properties For Alert Dialog
 * @param dialogBackgroundColor - set background color of the dialog
 * */
fun AlertDialog.background(
    dialogBackgroundColor: Int? = null
): AlertDialog {
    if (dialogBackgroundColor != null) {
        this.mainLayout.setBackgroundResource(dialogBackgroundColor)
    }
    return this
}

/***
 * Positions of Alert Dialog
 * @param position - set the position for the alert dialog by default it will be bottom
 * */
fun AlertDialog.position(
    position: UIAlertDialog.POSITIONS = UIAlertDialog.POSITIONS.BOTTOM
): AlertDialog {
    val layoutParams = mainLayout.layoutParams as RelativeLayout.LayoutParams
    when (position) {
        UIAlertDialog.POSITIONS.CENTER -> {
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        }
        UIAlertDialog.POSITIONS.BOTTOM -> {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        }
        UIAlertDialog.POSITIONS.TOP -> {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        }
    }
    mainLayout!!.layoutParams = layoutParams
    return this
}

/***
 * Sub Title or Body of Alert Dialog
 * @param body - set body content for the alert dialog
 * @param fontStyle - set font style of the textview
 * @param color - set color for the textview
 * */
fun AlertDialog.body(
    body: String,
    fontStyle: Typeface? = null,
    color: Int = 0
): AlertDialog {
    this.subHeading.text = body.trim()
    this.subHeading.show()
    if (fontStyle != null) {
        this.subHeading.typeface = fontStyle
    }
    if (color != 0) {
        this.subHeading.setTextColor(color)
    }
    return this
}

/***
 * Icon of  Alert Dialog
 * @param icon - set icon for the dialog
 * @param animateIcon - set animation icon for the dialog
 * */
fun AlertDialog.icon(
    icon: Int,
    animateIcon: Boolean = false
): AlertDialog {
    this.image.setImageResource(icon)
    this.image.show()
    // Pulse Animation for Icon
    if (animateIcon) {
        val pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_pulse)
        image.startAnimation(pulseAnimation)
    }
    return this
}

/***
 * onPositive Button Properties For Alert Dialog
 *
 * No Need to call dismiss(). It is calling already
 *
 * @param text - set text for the positive button
 * @param buttonBackgroundColor - set positive button background
 * @param textColor - set text color for the positive button
 * @param typeface - set font style for the positive button
 * @param action - anonymous function for click listener
 * */
fun AlertDialog.onPositive(
    text: String,
    buttonBackgroundColor: Int? = null,
    textColor: Int? = null,
    typeface: Int? = null,
    action: (() -> Unit)? = null
): AlertDialog {
    this.yesButton.show()
    if (buttonBackgroundColor != null) {
        this.yesButton.setBackgroundResource(buttonBackgroundColor)
    }
    if (textColor != null) {
        this.yesButton.setTextColor(textColor)
    }
    if (typeface != null) {
        this.yesButton.setTypeface(null, typeface)
    }
    this.yesButton.text = text.trim()
    this.yesButton.setOnClickListener {
        action?.invoke()
        dismiss()
    }
    return this
}

/***
 * onNegative Button Properties For Alert Dialog
 *
 * No Need to call dismiss(). It is calling already
 *
 * @param text - set text for the negative button
 * @param buttonBackgroundColor - set negative button background
 * @param textColor - set text color for the negative button
 * @param typeface - set font style for the negative button
 * @param action - anonymous function for click listener
 * */
fun AlertDialog.onNegative(
    text: String,
    buttonBackgroundColor: Int? = null,
    textColor: Int? = null,
    typeface: Int? = null,
    action: (() -> Unit)? = null
): AlertDialog {
    this.noButton.show()
    this.noButton.text = text.trim()
    if (textColor != null) {
        this.noButton.setTextColor(textColor)
    }
    if (buttonBackgroundColor != null) {
        this.noButton.setBackgroundResource(buttonBackgroundColor)
    }
    if (typeface != null) {
        this.noButton.setTypeface(null, typeface)
    }
    this.noButton.setOnClickListener {
        action?.invoke()
        dismiss()
    }
    return this
}

/***
 * show visibility for the dialog
 */
private fun View.show() {
    this.visibility = View.VISIBLE
}