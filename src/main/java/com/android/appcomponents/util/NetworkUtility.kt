package com.android.appcomponents.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtility {
    fun getRetrofitInstance(base_url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}