package com.android.appcomponents.customui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter

class DataBindingRecyclerAdapter : ListAdapter<RecyclerItem, BindingViewHolder>(
    DiffCallback()
) {
    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), viewType, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BindingViewHolder,
        position: Int
    ) {
        holder.run {
            getItem(position).bind(binding)
            if (binding.hasPendingBindings()) {
                binding.executePendingBindings()
            }
        }
    }
}

private fun RecyclerItem.bind(binding: ViewDataBinding) {
    val isVariableFound = binding.setVariable(variableId, data)
    if (isVariableFound.not()) {
        throw IllegalStateException(
            buildErrorMessage(variableId, binding)
        )
    }
}
