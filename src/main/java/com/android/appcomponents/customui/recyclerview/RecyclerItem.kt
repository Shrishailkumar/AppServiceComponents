package com.android.appcomponents.customui.recyclerview

import androidx.annotation.LayoutRes

/**
 * For storing details for adapter layout
 */
data class RecyclerItem(
    val data: Any,
    @LayoutRes val layoutId: Int,
    val variableId: Int
)