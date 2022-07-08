package com.android.appcomponents.customui.recyclerview

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

internal fun buildErrorMessage(
    variableId: Int,
    binding: ViewDataBinding
): String {
    val variableName = DataBindingUtil.convertBrIdToString(variableId)
    val className = binding::class.simpleName
    return "Failed to find variable='$variableName' in the following databinding layout: $className"
}