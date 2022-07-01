package com.android.appcomponents.AppContext

import android.content.Context

abstract class AppContext {
    companion object {

        private lateinit var context: Context

        fun setContext(con: Context) {
            context=con
        }
        fun getContext(): Context? {
            if (context!=null)
            return context;
            else
                return null
        }
    }


}