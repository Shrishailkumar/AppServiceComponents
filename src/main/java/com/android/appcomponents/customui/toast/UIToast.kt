package com.android.appcomponents.customui.toast

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.android.appcomponents.R


class UIToast(private val context: Context) {

    private var icon: Int = -1
    private var message = "No text!"
    private var cardBackgroundColor = R.color.black
    private var textColor = R.color.white
    private var cardElevation = 4f
    private var cardCornerRadius = 8f
    private var textSize = 16f
    private var typeface = Typeface.DEFAULT
    private var gravity = -91
    private var xOffset = 0
    private var yOffset = 0

    /**
     * Icon for Toast
     * @param icon - set icon if it is set
     */
    fun setIcon(icon: Int): UIToast {
        this.icon = icon
        return this
    }

    /**
     * Message to show in toast
     * @param message - Message to show
     */
    fun setMessage(message: String?): UIToast {
        this.message = message!!
        return this
    }

    /**
     * cardview background color
     * @param cardBackgroundColor - set background color for toast cardview
     */
    fun setCardBackgroundColor(cardBackgroundColor: Int): UIToast {
        this.cardBackgroundColor = cardBackgroundColor
        return this
    }

    /**
     * Text color
     * @param textColor - set text color for textview
     */
    fun setTextColor(textColor: Int): UIToast {
        this.textColor = textColor
        return this
    }

    /**
     * Toast cardview elevation
     * @param elevation - set elevation for toast
     */
    fun setCardElevation(elevation: Float): UIToast {
        cardElevation = elevation
        return this
    }

    /**
     * cardview radius
     * @param radius - set curved edges for toast
     */
    fun setCardRadius(radius: Float): UIToast {
        cardCornerRadius = radius
        return this
    }

    fun setTextSize(textSize: Float): UIToast {
        this.textSize = textSize
        return this
    }

    fun setTypeFace(typeface: Typeface?): UIToast {
        this.typeface = typeface
        return this
    }

    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int): UIToast {
        this.gravity = gravity
        this.xOffset = xOffset
        this.yOffset = yOffset
        return this
    }

    fun createToast(duration: Int): Toast {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.ui_toast, null)
        val cardView = view.findViewById<CardView>(R.id.cardView)
        val toastIcon = view.findViewById<ImageView>(R.id.toastIcon)
        val toastMessage = view.findViewById<TextView>(R.id.toastMessage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cardView.setCardBackgroundColor(context.getColor(cardBackgroundColor))
        } else {
            cardView.setBackgroundColor(ContextCompat.getColor(context, cardBackgroundColor))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(icon != -1)
            toastIcon.setImageDrawable(context.getDrawable(icon))
        }
        cardView.cardElevation = cardElevation
        cardView.radius = cardCornerRadius
        toastMessage.textSize = textSize
        toastMessage.text = message
        toastMessage.setTextColor(ContextCompat.getColor(context, textColor))
        toastMessage.typeface = typeface
        val toast = Toast(context)
        toast.view = view
        toast.duration = duration
        if (gravity != -91) toast.setGravity(gravity, xOffset, yOffset)
        return toast
    }

    companion object {

        /**
         * @param context - activity context
         * @param message - text you want to show
         * @param duration - duration of toast
         */
        fun showToast(context: Context, message: String, duration: Int): Toast {
            return UIToast(context)
                .setMessage(message)
                .createToast(duration)
        }

        /**
         * @param context - activity context
         * @param message - text you want to show
         * @param duration - duration of toast
         */
        fun showToastPosition(context: Context, message: String, duration: Int, position: Int): Toast {
            return UIToast(context)
                .setMessage(message)
                .setGravity(position, 0,0)
                .createToast(duration)
        }

        /**
         * @param context - activity context
         * @param message - text you want to show
         * @param duration - duration of toast
         * @param icon - icon from a drawable folder
         */
        fun showToast(context: Context, message: String, duration: Int, icon: Int): Toast {
            return UIToast(context)
                .setIcon(icon)
                .setMessage(message)
                .createToast(duration)
        }

        /**
         * @param context - activity context
         * @param message - text you want to show
         * @param duration - duration of toast
         * @param icon - icon from a drawable folder
         * @param textColor - color of a toast text
         * @param cardBackgroundColor - background color of a toast
         */
        fun showToast(
            context: Context,
            message: String,
            duration: Int,
            icon: Int,
            textColor: Int,
            cardBackgroundColor: Int
        ): Toast {
            return UIToast(context)
                .setIcon(icon)
                .setMessage(message)
                .setTextColor(textColor)
                .setCardBackgroundColor(cardBackgroundColor)
                .createToast(duration)
        }

        /**
         * @param context - activity context
         * @param message - text you want to show
         * @param duration - duration of toast
         * @param icon - icon from a drawable folder
         * @param textColor - color of a toast text
         * @param cardBackgroundColor - background color of a toast
         * @param cornerRadius - corner radius of the toast makes the corner curvy
         */
        fun showToast(
            context: Context,
            message: String?,
            duration: Int,
            icon: Int,
            textColor: Int,
            cardBackgroundColor: Int,
            cornerRadius: Float
        ): Toast {
            return UIToast(context)
                .setIcon(icon)
                .setMessage(message)
                .setTextColor(textColor)
                .setCardBackgroundColor(cardBackgroundColor)
                .setCardElevation(5f)
                .setCardRadius(cornerRadius)
                .createToast(duration)
        }

        /**
         * @param context - activity context
         * @param message - text you want to show
         * @param duration - duration of toast
         * @param textColor - color of a toast text
         * @param cardBackgroundColor - background color of a toast
         * @param cornerRadius - corner radius of the toast makes the corner curvy
         * @param elevation - it will give a shadow effect of the toast
         */
        fun showToast(
            context: Context,
            message: String?,
            duration: Int,
            textColor: Int,
            cardBackgroundColor: Int,
            cornerRadius: Float,
            elevation: Float
        ): Toast {
            return UIToast(context)
                .setMessage(message)
                .setTextColor(textColor)
                .setCardBackgroundColor(cardBackgroundColor)
                .setCardElevation(elevation)
                .setCardRadius(cornerRadius)
                .createToast(duration)
        }
    }
}