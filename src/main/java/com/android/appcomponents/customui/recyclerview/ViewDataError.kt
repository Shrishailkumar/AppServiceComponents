package com.android.appcomponents.customui.recyclerview

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Method to show error message if any issue in binding variable mismatches
 * @param variableId - variable ID passed to convert BR class to String
 * @param binding - view binding object
 */
internal fun buildErrorMessage(
    variableId: Int,
    binding: ViewDataBinding
): String {
    val variableName = DataBindingUtil.convertBrIdToString(variableId)
    val className = binding::class.simpleName
    return "Failed to find variable='$variableName' in the following databinding layout: $className"
}