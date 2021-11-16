package com.android.appcomponents.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.android.appcomponents.interfaces.APIListener
import com.android.appcomponents.interfaces.NetworkAPI
import com.android.appcomponents.repository.ElasticSearchRepository
import kotlinx.coroutines.*
import okhttp3.Dispatcher

class ElasticSearchViewModel(activity: Activity, val networkAPI: NetworkAPI) : ViewModel() {
    private var elasticSearchRepository: ElasticSearchRepository? = null

    var apiListener: APIListener? = null


    fun getRepositoryFromServer(endPoint: String?, queryParam: HashMap<String, String>?) {
        apiListener?.onStarted()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                //Call get request from NetworkAPI interface
                val response = networkAPI.getRequest(endPoint, queryParam)
                apiListener?.onSuccessResponse(response)

            } catch (e: Exception) {
                apiListener?.onErrorResponse("Error Occurred: $e.localizedMessage")
            }
        }
    }


}