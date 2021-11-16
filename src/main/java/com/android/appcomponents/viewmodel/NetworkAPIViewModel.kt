package com.android.appcomponents.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.appcomponents.util.NetworkUtility
import retrofit2.Retrofit

class NetworkAPIViewModel(private val base_url: String): ViewModel() {

    fun getNetworkClient(): Retrofit {
        return NetworkUtility.getRetrofitInstance(base_url)
    }
}

class NetworkAPIViewModelFactory(private val base_url: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NetworkAPIViewModel(base_url) as T
    }

}