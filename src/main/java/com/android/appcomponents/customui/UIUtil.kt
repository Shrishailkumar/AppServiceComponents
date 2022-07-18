package com.android.appcomponents.customui

import android.content.Context

fun Int.toPx(context: Context): Float {
    return this * context.resources.displayMetrics.density
}