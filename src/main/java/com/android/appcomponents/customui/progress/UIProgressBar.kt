package com.android.appcomponents.customui.progress

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.android.appcomponents.R


class UIProgressBar {
    companion object {
        var uiProgressDialog: UIProgressDialog? = null

        fun showProgressBar(
            context: Context?,
            progressColor: Int,
        ): UIProgressDialog? {
            try {
                if (context != null) {
                    if (uiProgressDialog == null) {
                        uiProgressDialog = UIProgressDialog(context)
                    }
                    if (!uiProgressDialog?.isShowing!!) {
                        uiProgressDialog!!.setCancelable(true)
                        if (context is Activity && !(context).isFinishing) {
                            val progressBar: ProgressBar =
                                uiProgressDialog!!.findViewById(R.id.progressBar)
                            progressBar.indeterminateDrawable.colorFilter =
                                PorterDuffColorFilter(progressColor, PorterDuff.Mode.MULTIPLY)
                            uiProgressDialog!!.show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return uiProgressDialog
        }

        fun showProgressBar(
            context: Context?,
            message: String?,
            isCancellable: Boolean
        ): UIProgressDialog? {
            try {
                if (context != null) {
                    if (uiProgressDialog == null) {
                        uiProgressDialog = UIProgressDialog(context)
                    }
                    if (!uiProgressDialog?.isShowing!!) {
                        uiProgressDialog!!.setCancelable(isCancellable)
                        if (context is Activity && !(context).isFinishing) {
                            val textMessage: TextView? =
                                uiProgressDialog?.findViewById(R.id.progressMesg)
                            textMessage?.text = message
                            textMessage?.textSize = 18f
                            textMessage?.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    android.R.color.white
                                )
                            )
                            uiProgressDialog!!.show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return uiProgressDialog
        }

        fun showProgressBar(
            context: Context?,
            message: String?,
            progressColor: Int,
            isCancellable: Boolean
        ): UIProgressDialog? {
            try {
                if (context != null) {
                    if (uiProgressDialog == null) {
                        uiProgressDialog = UIProgressDialog(context)
                    }
                    if (!uiProgressDialog?.isShowing!!) {
                        uiProgressDialog!!.setCancelable(isCancellable)
                        if (context is Activity && !(context).isFinishing) {
                            val progressBar: ProgressBar =
                                uiProgressDialog!!.findViewById(R.id.progressBar)
                            val textMessage: TextView? =
                                uiProgressDialog?.findViewById(R.id.progressMesg)
                            textMessage?.text = message
                            textMessage?.textSize = 18f
                            textMessage?.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    android.R.color.white
                                )
                            )
                            progressBar.indeterminateDrawable.colorFilter =
                                PorterDuffColorFilter(progressColor, PorterDuff.Mode.MULTIPLY)
                            uiProgressDialog!!.show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return uiProgressDialog
        }


        fun showProgressBar(
            context: Context?,
            message: String?,
            progressColor: Int
        ): UIProgressDialog? {
            try {
                if (context != null) {
                    if (uiProgressDialog == null) {
                        uiProgressDialog = UIProgressDialog(context)
                    }
                    if (!uiProgressDialog?.isShowing!!) {
                        uiProgressDialog!!.setCancelable(true)
                        if (context is Activity && !(context).isFinishing) {
                            val progressBar: ProgressBar =
                                uiProgressDialog!!.findViewById(R.id.progressBar)
                            val textMessage: TextView? =
                                uiProgressDialog?.findViewById(R.id.progressMesg)
                            textMessage?.text = message
                            textMessage?.textSize = 18f
                            textMessage?.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    android.R.color.white
                                )
                            )
                            progressBar.indeterminateDrawable.colorFilter =
                                PorterDuffColorFilter(progressColor, PorterDuff.Mode.MULTIPLY)
                            uiProgressDialog!!.show()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return uiProgressDialog
        }

        /**
         * It dismiss/hide the progress dialog if it's showing
         */
        fun hideProgressBar() {
            if (uiProgressDialog != null && uiProgressDialog!!.isShowing) {
                try {
                    uiProgressDialog!!.dismiss()
                    uiProgressDialog = null
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                } finally {
                    uiProgressDialog = null
                }
            } else {
                uiProgressDialog = null
            }
        }

    }
}