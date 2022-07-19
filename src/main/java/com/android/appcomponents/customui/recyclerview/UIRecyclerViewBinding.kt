package com.android.appcomponents.customui.recyclerview

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Method to set items to recyclerview
 * @param recyclerView - instance of an recyclerview
 * @param items - list of data you want to send to recyclerview
 */
@BindingAdapter("items")
fun setRecyclerViewItems(
    recyclerView: RecyclerView,
    items: List<RecyclerItem>?
) {
    var adapter = (recyclerView.adapter as? DataBindingRecyclerAdapter)
    if (adapter == null) {
        adapter = DataBindingRecyclerAdapter()
        recyclerView.adapter = adapter
    }

    adapter.submitList(
        items.orEmpty()
    )
}