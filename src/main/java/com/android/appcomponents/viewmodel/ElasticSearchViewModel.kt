package com.android.appcomponents.viewmodel

import androidx.lifecycle.ViewModel
import com.android.appcomponents.network.interfaces.APIListener
import com.android.appcomponents.network.interfaces.NetworkAPI
import kotlinx.coroutines.*

class ElasticSearchViewModel(val networkAPI: NetworkAPI) : ViewModel() {
    var apiListener: APIListener? = null

    fun getDataFromServer(endPoint: String?, queryParam: HashMap<String, String>?) {
        apiListener?.onStarted()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = endPoint?.let { networkAPI.getRequest(it, queryParam) }
                if (response != null) {
                    apiListener?.onSuccessResponse(response)
                }

            } catch (e: Exception) {
                apiListener?.onErrorResponse("Error Occurred: $e.localizedMessage")
            }
        }
    }


}