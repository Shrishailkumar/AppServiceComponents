package com.android.appcomponents.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.appcomponents.network.interfaces.NetworkAPI

class ElasticSearchViewModelFactory(private val networkAPI: NetworkAPI) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ElasticSearchViewModel(networkAPI) as T
    }
}