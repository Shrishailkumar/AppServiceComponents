package com.android.appcomponents.customui.progress

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.Window
import com.android.appcomponents.R

class UIProgressDialog : Dialog {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ui_progressbar)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}