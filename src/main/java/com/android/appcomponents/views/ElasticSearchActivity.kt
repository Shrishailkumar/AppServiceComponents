package com.android.appcomponents.views

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.appcomponents.R
import com.android.appcomponents.adapter.RepoItemAdapter
import com.android.appcomponents.interfaces.APIListener
import com.android.appcomponents.interfaces.NetworkAPI
import com.android.appcomponents.model.Item
import com.android.appcomponents.util.Util
import com.android.appcomponents.viewmodel.ElasticSearchViewModel
import com.android.appcomponents.viewmodel.ElasticSearchViewModelFactory
import com.android.appcomponents.viewmodel.NetworkAPIViewModel
import com.android.appcomponents.viewmodel.NetworkAPIViewModelFactory
import kotlinx.android.synthetic.main.activity_elastic_search.*
import okhttp3.ResponseBody
import org.json.JSONObject


class ElasticSearchActivity : AppCompatActivity(), APIListener {
    var repoList = mutableListOf<Item>()
    var repoAdapter: RepoItemAdapter? = null
    var elasticSearchViewModel: ElasticSearchViewModel? = null
    private var repoSearchList = mutableListOf<Item>()
    var page = 1
    var baseURL: String? = null
    var endPoint: String? = null
    var queryHashMap: HashMap<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elastic_search)

        val intent = intent
        val layoutType = intent.getStringExtra("layout")
        baseURL = intent.getStringExtra("baseUrl")
        endPoint = intent.getStringExtra("endPoint")

        queryHashMap = intent.getSerializableExtra("map") as HashMap<String, String>?

        if (layoutType.equals("linear", true)) {
            rvGrid.visibility = View.GONE
            rvLinear.visibility = View.VISIBLE
        } else if (layoutType.equals("grid", true)) {
            rvLinear.visibility = View.GONE
            rvGrid.visibility = View.VISIBLE
        }

        configureViewModel()


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        Log.e("onConfigurationChanged", newConfig.toString())
    }


    private fun addSearchListener() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable.toString().isNotBlank()) {
                    filterByRepoName(editable)
                } else if (editable.toString().isEmpty()) {
                    repoAdapter?.addList(repoList)
                    repoSearchList.clear()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    private fun filterByRepoName(s: Editable?) {
        val newList = repoList.filter { repo ->
            repo.name.contains(s.toString(), true)
        }
        repoSearchList = newList as MutableList<Item>

        repoAdapter?.clear()
        repoAdapter?.addList(repoSearchList)

    }


    private fun configureViewModel() {
        val networkAPIViewModel: NetworkAPIViewModel = ViewModelProvider(
            this, NetworkAPIViewModelFactory(baseURL!!)
        ).get(NetworkAPIViewModel::class.java)

        val retrofitInstance = networkAPIViewModel.getNetworkClient().create(NetworkAPI::class.java)

        elasticSearchViewModel =
            ViewModelProvider(this, ElasticSearchViewModelFactory(this, retrofitInstance)).get(
                ElasticSearchViewModel::class.java
            )

        elasticSearchViewModel?.apiListener = this

    }

    override fun onResume() {
        super.onResume()
        getRepoAPI()
    }

    private fun getRepoAPI() {
        if (Util.checkNetwork(this)) {
            if (!TextUtils.isEmpty(etSearch.text)) {
                etSearch.setText("")
            }
            elasticSearchViewModel?.getRepositoryFromServer(endPoint, queryHashMap)
        } else {
            Toast.makeText(this, "Check Internet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStarted() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuccessResponse(responseBody: ResponseBody) {
        progressBar.visibility = View.GONE
        Log.e("TAG", "onSuccessResponse: ${responseBody.string()}")
        Log.e("TAG", "onSuccessResponse: ${responseBody.string()}")


        val jsonObj = JSONObject(responseBody.string())

        repoAdapter = RepoItemAdapter(jsonObj)
        rvLinear.adapter = repoAdapter
        rvGrid.adapter = repoAdapter
        addSearchListener()
//        Log.e("TAG", "jsonObj: $jsonObj}")


    }

    override fun onErrorResponse(errorMessage: String) {
//        progressBar.visibility = View.GONE
        Log.e("TAG", "onErrorResponse: ${errorMessage}")

    }

}