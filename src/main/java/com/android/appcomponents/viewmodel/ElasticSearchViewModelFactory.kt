package com.android.appcomponents.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.appcomponents.interfaces.NetworkAPI

class ElasticSearchViewModelFactory(private val ctx: Activity, private val networkAPI: NetworkAPI) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ElasticSearchViewModel(ctx, networkAPI) as T
    }
}