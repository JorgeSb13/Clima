package com.examen.clima.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.examen.clima.R
import com.examen.clima.adapters.SearchAdapter
import com.examen.clima.databinding.ActivitySearchBinding
import com.examen.clima.entities.Search
import com.examen.clima.listener.SearchListener
import com.examen.clima.network.HelperUtil
import com.examen.clima.searchBox
import com.examen.clima.utils.*
import com.examen.clima.utils.Constants.Companion.LOCATION_EXTRA
import com.examen.clima.viewModels.WeatherViewModel
import com.projects.mylibrary.activities.ToolbarActivity

class SearchActivity : ToolbarActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var weatherViewModel: WeatherViewModel

    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SearchAdapter
    private var searchList: ArrayList<Search> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar.root

        toolbarToLoad(toolbar)
        enableHomeDisplay(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        // ----- Initialize ViewModel and call Observers -----
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        attachObservers()

        // Load the weather of next days
        mLayoutManager = LinearLayoutManager(this)
        recycler = binding.recycler

        loadSearch()

        // ----- Search function -----
        binding.ivSearch.setOnClickListener {
            val search = binding.etSearch.text.toString()

            if (search != "")
                weatherViewModel.searchLocation(search)
        }
    }

    private fun loadSearch() {
        searchList.clear()

        binding.lblMessage.visibility = if (searchBox.all.isEmpty()) VISIBLE else GONE

        for (search in searchBox.all)
            searchList.add(search)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        recycler.setHasFixedSize(true)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.layoutManager = mLayoutManager
        adapter = (SearchAdapter(searchList, object: SearchListener{

            override fun onClick(search: Search) {
                alertDialog(this@SearchActivity, getString(R.string.ad_see_location_title),
                    getString(R.string.ad_see_location_message, search.name), getString(R.string.accept), { _, _ ->

                        goToActivity<MainActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            this.putExtra(LOCATION_EXTRA, search.name)
                        }
                        transitionRight()

                    }, getString(R.string.cancel))
            }
        }))
        recycler.adapter = adapter
    }

    private fun addLocation(location: Search) {

    }

    private fun attachObservers() {
        weatherViewModel.isLoading.observe(this, {

            if (it != null)
            // Using "root" after component name to call view reference
                enableLoading(binding.frameLoading.root, View.VISIBLE, it)
            else
            // Using "root" after component name to call view reference
                enableLoading(binding.frameLoading.root, View.INVISIBLE)
        })

        weatherViewModel.searchSuccess.observe(this, { response ->
            binding.etSearch.text.clear()

            if (response != null) {
                searchBox.removeAll()

                for (search in response)
                    searchBox.put(Search(search.id.toLong(), search.name, search.region, search.country))

                loadSearch()
            }
        })

        weatherViewModel.searchFailure.observe(this, { throwable ->
            binding.etSearch.text.clear()
            searchBox.removeAll()

            if (throwable != null) {
                HelperUtil().parseError(throwable, this)
                weatherViewModel.weatherFailure.postValue(null)
            }

            loadSearch()
        })
    }

}