package com.android.appcomponents.views

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.appcomponents.R
import com.android.appcomponents.adapter.ElasticSearchAdapter
import com.android.appcomponents.network.interfaces.APIListener
import com.android.appcomponents.network.interfaces.NetworkAPI
import com.android.appcomponents.util.ElasticSearchUtil
import com.android.appcomponents.viewmodel.ElasticSearchViewModel
import com.android.appcomponents.viewmodel.ElasticSearchViewModelFactory
import com.android.appcomponents.viewmodel.NetworkAPIViewModel
import com.android.appcomponents.viewmodel.NetworkAPIViewModelFactory
import kotlinx.android.synthetic.main.activity_elastic_search.*
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject


class ElasticSearchActivity : AppCompatActivity(), APIListener {
    var repoAdapter: ElasticSearchAdapter? = null
    var elasticSearchViewModel: ElasticSearchViewModel? = null
    var baseURL: String? = null
    var endPoint: String? = null
    var queryHashMap: HashMap<String, String>? = null
    var searchQuery: String? = null
    var listData: JSONArray? =null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elastic_search)
        baseURL = intent.getStringExtra("baseUrl")
        endPoint = intent.getStringExtra("endPoint")
        repoAdapter = ElasticSearchAdapter(this)
        queryHashMap = intent.getSerializableExtra("map") as HashMap<String, String>?
        searchQuery = queryHashMap?.get("q")
        rvLinear.adapter = repoAdapter

        configureViewModel()
    }


    private fun addSearchListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable.toString().isNotBlank()) {
                    filterBySearchText(editable)
                } else if (editable.toString().isEmpty()) {
                    searchQuery?.let { queryHashMap?.put("q", it) }
                    elasticSearchViewModel?.getDataFromServer(endPoint, queryHashMap)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun filterBySearchText(searchText: Editable?) {

        queryHashMap?.put("q", searchText.toString())
        elasticSearchViewModel?.getDataFromServer(endPoint, queryHashMap)

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun configureViewModel() {
        val networkAPIViewModel: NetworkAPIViewModel = ViewModelProvider(
            this, NetworkAPIViewModelFactory(baseURL!!, this)
        ).get(NetworkAPIViewModel::class.java)

        val retrofitInstance = networkAPIViewModel.getNetworkClient().create(NetworkAPI::class.java)

        elasticSearchViewModel =
            ViewModelProvider(this, ElasticSearchViewModelFactory(retrofitInstance)).get(
                ElasticSearchViewModel::class.java
            )

        elasticSearchViewModel?.apiListener = this

    }

    override fun onResume() {
        super.onResume()
        getRepoAPI()
    }

    private fun getRepoAPI() {
        if (ElasticSearchUtil.checkNetwork(this)) {
            if (!TextUtils.isEmpty(etSearch.text)) {
                etSearch.setText("")
            }
            elasticSearchViewModel?.getDataFromServer(endPoint, queryHashMap)
        } else {
            Toast.makeText(this, "Check Internet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStarted() {
        progressBar.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onSuccessResponse(responseBody: ResponseBody) {
        progressBar.visibility = View.GONE
        val jsonObjStr = responseBody.string()
        val jsonObj = JSONObject(jsonObjStr)
        val keysItr: Iterator<String> = jsonObj.keys()
        while (keysItr.hasNext()) {
            val key = keysItr.next()
            val value: Any = jsonObj.get(key)
            if (value is JSONArray) {
                repoAdapter?.addList(value)
                repoAdapter?.notifyDataSetChanged()
            }
        }
        addSearchListener()
    }

    override fun onErrorResponse(errorMessage: String) {
        progressBar.visibility = View.GONE
    }

}