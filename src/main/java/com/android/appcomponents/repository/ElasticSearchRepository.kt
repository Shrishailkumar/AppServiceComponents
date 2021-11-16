package com.android.appcomponents.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.appcomponents.model.RepositoryData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ElasticSearchRepository {
    private var elasticSearchRepository: ElasticSearchRepository? = null
    private var repoData: MutableLiveData<RepositoryData?> = MutableLiveData()
    private var retrofitService = RetrofitService()
    private var BASE_URL: String? = null

    val repoLiveData: LiveData<RepositoryData?>
        get() = repoData

    fun getInstance(baseUrl: String): ElasticSearchRepository? {
        if (elasticSearchRepository == null) {
            BASE_URL = baseUrl
            elasticSearchRepository = ElasticSearchRepository()
        }
        return elasticSearchRepository
    }

    fun getRepoList(searchTerm: String, sort: String, order: String, page: Int, perPage: Int) {
        val apiCall = retrofitService?.getRepoApiService()
            ?.getRepoList(searchTerm, sort, order, page, perPage)

        apiCall?.enqueue(object : Callback<RepositoryData?> {
            override fun onFailure(call: Call<RepositoryData?>, t: Throwable) {
                repoData.value = null
            }

            override fun onResponse(
                call: Call<RepositoryData?>,
                response: Response<RepositoryData?>
            ) {
                repoData.value = response.body()
            }
        })

    }

}