package com.android.appcomponents.customui.recyclerview

import androidx.annotation.LayoutRes

/**
 * For storing details for adapter layout
 * @param data - data type of the list
 * @param layoutId - layout id of each item
 * @param variableId - variable name that end user declare in their xml file
 */
data class RecyclerItem(
    val data: Any,
    @LayoutRes val layoutId: Int,
    val variableId: Int
)