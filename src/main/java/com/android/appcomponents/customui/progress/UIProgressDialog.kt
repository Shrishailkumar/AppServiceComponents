package com.android.appcomponents.customui.progress

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.appcomponents.R
import com.android.appcomponents.customui.setColorFilter

class UIProgressDialog constructor(private val context: Context) {

    private var progressDialog: Dialog? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgress: TextView
    var isCancellable: Boolean = false
    var canShowMessage: Boolean = false
    var progressMessageText: String = ""
    var tcProgressMessage: Int? = null
    var progressBarColor: Int = ContextCompat.getColor(context, android.R.color.black)
    var tsProgressMessage: Float = 0F
    var tfMessageText: Typeface? = null


    fun showProgressBar() {
        progressDialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.ui_progressbar)
            setCancelable(isCancellable)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressBar = progressDialog!!.findViewById(R.id.progressBar) as ProgressBar
        setColorFilter(
            progressBar.indeterminateDrawable,
            ResourcesCompat.getColor(context.resources, progressBarColor, null)
        )
        tvProgress = progressDialog!!.findViewById(R.id.progressMesg) as TextView

        if (canShowMessage) {
            tvProgress.text = progressMessageText
            if (tcProgressMessage != null)
                tvProgress.setTextColor(ContextCompat.getColor(context, tcProgressMessage!!))
            if (tfMessageText != null)
                tvProgress.typeface = tfMessageText

            tvProgress.textSize = tsProgressMessage

        }
        progressDialog!!.show()
    }

    fun hideProgressBar() {
        progressDialog?.dismiss()
    }


}
