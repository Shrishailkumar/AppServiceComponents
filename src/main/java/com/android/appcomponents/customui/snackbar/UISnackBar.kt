package com.android.appcomponents.customui.snackbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.android.appcomponents.customui.toPx
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout

class UISnackBar(private val context: Context) {

    private var customView: View? = null
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var duration: Int = Snackbar.LENGTH_SHORT
    private var message: String = ""
    private var borderColor: Int = Color.TRANSPARENT
    private var cornerRadius: Float = 0f
    private var backgroundColor: Int = Color.TRANSPARENT
    private var borderWidth: Int = 0
    private var textColor: Int = ContextCompat.getColor(context, android.R.color.black)
    private var actionTextColor: Int = ContextCompat.getColor(context, android.R.color.black)
    private var padding: Int = 0.toPx(context).toInt()
    private var customDrawable: GradientDrawable? = null
    private var drawable = GradientDrawable()
    private var tfTextView: Typeface? = null
    private var tfActionBtn: Typeface? = null
    private var action: ((Snackbar) -> Unit)? = null
    private var customViewAction: ((View) -> Unit)? = null
    private var buttonName: String = ""
    private var coordinateView: View? = null
    private lateinit var snackbar: Snackbar

    constructor(context: Context, view: View) : this(context) {
        customView = view
    }

    fun customView(view: View) {
        this.customView = view
    }

    fun customView(@LayoutRes layoutResource: Int) {
        customView(inflater.inflate(layoutResource, LinearLayout(context), false))
    }

    fun duration(duration: Int) {
        this.duration = duration
    }

    fun messageRes(@StringRes message: Int) {
        message(context.getString(message))
    }

    fun message(message: String) {
        this.message = message
    }

    fun border(width: Int, @ColorInt color: Int) {
        this.borderWidth = width
        this.borderColor = color
    }

    fun cornerRadius(cornerRadius: Float) {
        this.cornerRadius = cornerRadius
    }

    fun cornerRadiusRes(@DimenRes dimenId: Int) {
        cornerRadius(context.resources.getDimension(dimenId))
    }

    fun backgroundColorRes(@ColorRes colorId: Int) {
        backgroundColor(ContextCompat.getColor(context, colorId))
    }

    fun backgroundColor(@ColorInt color: Int) {
        this.backgroundColor = color
    }

    fun padding(padding: Int) {
        this.padding = padding
    }

    fun paddingRes(@DimenRes dimenId: Int) {
        padding(context.resources.getDimension(dimenId).toInt())
    }

    fun textColor(@ColorInt color: Int) {
        this.textColor = color
    }

    fun textColorRes(@ColorRes colorId: Int) {
        textColor(ContextCompat.getColor(context, colorId))
    }

    fun actionTextColor(@ColorInt actionTextColor: Int) {
        this.actionTextColor = actionTextColor
    }

    fun actionTextColorRes(@ColorRes colorId: Int) {
        actionTextColor(ContextCompat.getColor(context, colorId))
    }

    fun textTypeface(textTypeface: Typeface) {
        tfTextView = textTypeface
    }

    fun actionTypeface(actionTypeface: Typeface) {
        tfActionBtn = actionTypeface
    }

    private fun makeSnackBar(view: View) {
        snackbar = Snackbar.make(view, message, duration)
        val snackBarLayout = snackbar.view as Snackbar.SnackbarLayout // Frame Layout
        val snackContentLayout =
            snackBarLayout.getChildAt(0) as SnackbarContentLayout // Linear Layout`

        if (customDrawable == null) {
            if (backgroundColor != 0) {
                drawable.setColor(backgroundColor)
            } else {
                drawable = snackBarLayout.background as GradientDrawable
            }
            drawable.cornerRadius = cornerRadius
            drawable.setStroke(borderWidth, borderColor)
        } else {
            drawable = customDrawable as GradientDrawable
        }

        val pLeft = snackBarLayout.paddingLeft
        val pRight = snackBarLayout.paddingRight

        snackBarLayout.setBackgroundColor(Color.TRANSPARENT)
        snackBarLayout.setPadding(0, 0, 0, 0)
        snackContentLayout.setPadding(pLeft, 0, pRight, 0)
        snackContentLayout.background = drawable

        if (padding > 0) {
            snackBarLayout.setPadding(padding, 0, padding, padding)
        }

        if (customView == null) {
            val tvSnackBarTextView = snackContentLayout.getChildAt(0) as AppCompatTextView
            tvSnackBarTextView.setTextColor(textColor)
            if (tfTextView != null) {
                tvSnackBarTextView.typeface = tfTextView
            }

            val btnSnackBarActionButton = snackContentLayout.getChildAt(1) as AppCompatButton
            btnSnackBarActionButton.setTextColor(actionTextColor)
            if (tfActionBtn != null) {
                btnSnackBarActionButton.typeface = tfActionBtn
            }

            if (action != null) {
                snackbar.setAction(buttonName) {
                    action?.invoke(snackbar)
                }
            }
        } else {
            snackContentLayout.visibility = View.GONE
            snackBarLayout.addView(customView)
            customViewAction?.invoke(customView!!)
        }
    }

    fun show(): UISnackBar {
        if (coordinateView != null && coordinateView is CoordinatorLayout) {
            makeSnackBar(coordinateView!!)
        } else {
            (context as Activity).findViewById<View>(android.R.id.content)?.apply {
                makeSnackBar(this)
            }
        }
        snackbar.show()
        return this
    }

    inline fun show(func: UISnackBar.() -> Unit): UISnackBar {
        this.func()
        return this.show()
    }

    fun getView(): View? {
        return customView
    }

    fun dismiss() {
        if (::snackbar.isInitialized) {
            snackbar.dismiss()
        }
    }
}