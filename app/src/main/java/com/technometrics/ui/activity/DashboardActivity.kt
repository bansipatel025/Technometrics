package com.technometrics.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.technometrics.R
import com.technometrics.base.BaseActivity
import com.technometrics.data.api.Resource
import com.technometrics.data.api.RestConstant
import com.technometrics.data.api.UserParams
import com.technometrics.data.model.movie.ResultsItem
import com.technometrics.data.repository.MovieDatabase
import com.technometrics.databinding.ActivityDashboardBinding
import com.technometrics.ui.adapter.MovieListAdapter
import com.technometrics.ui.factory.DashboardFactory
import com.technometrics.ui.viewmodel.dashboard.DashboardViewModel
import com.technometrics.utils.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.Collections

class DashboardActivity : BaseActivity(), KodeinAware, View.OnClickListener {
    private val movieDatabase by lazy { MovieDatabase.getDatabase(this).movieDao() }

    private lateinit var viewmodel: DashboardViewModel
    private val factory: DashboardFactory by instance()
    override val kodein: Kodein by kodein()
    private lateinit var binding: ActivityDashboardBinding
    var page = 1
    var postingList: ArrayList<ResultsItem> = ArrayList<ResultsItem>()
    private lateinit var adapter: MovieListAdapter

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            activity, R.layout.activity_dashboard
        )
        setupViewModel()
        setMovieListAdapter()
        setupObservers()
        clickListener()
        setCode()
    }

    private fun setupObservers() {
        viewmodel.movieListResponse.observe(this) { response ->
            when (response) {
                is Resource.Success -> {

                    Log.d("Response", "====== movieListResponse Response ====> $response")
                    response.data?.let { movieListResponse ->
                        if (movieListResponse.results != null) {
                            val i: Int = movieListResponse.results.size!! *
                                    (movieListResponse.page
                                    !!.toInt() + 1)

                            RestConstant.isNextApiCall =
                                i < movieListResponse.totalResults!!

                            setMovieList(movieListResponse.results!!)
                        }
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.some_error_ocurred),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {

                }
            }
        }
    }

    private fun setupViewModel() {
        viewmodel = ViewModelProvider(this, factory).get(DashboardViewModel::class.java)
        binding.viewmodel = viewmodel
    }

    @SuppressLint("InflateParams", "MissingInflatedId")
    private fun dialogSortBy() {
        val dialog = BottomSheetDialog(activity!!, R.style.BottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.layout_bottom_sort_by, null)
        val ivClose: AppCompatImageView = view.findViewById(R.id.ivClose)
        val tvPopularityDSC: AppCompatTextView = view.findViewById(R.id.tvPopularityDSC)
        val tvPopularityASC: AppCompatTextView = view.findViewById(R.id.tvPopularityASC)
        val tvRatingDSC: AppCompatTextView = view.findViewById(R.id.tvRatingDSC)
        val tvRatingASC: AppCompatTextView = view.findViewById(R.id.tvRatingASC)
        val tvReleaseDateDSC: AppCompatTextView = view.findViewById(R.id.tvReleaseDateDSC)
        val tvReleaseDateASC: AppCompatTextView = view.findViewById(R.id.tvReleaseDateASC)
        ivClose.setOnClickListener {
            dialog.dismiss()
        }

        tvPopularityDSC.setOnClickListener {
            dialog.dismiss()
            Collections.sort(postingList,
                java.util.Comparator<ResultsItem?> { rhs, lhs -> lhs.popularity!!.compareTo(rhs.popularity!!) })
            adapter.notifyDataSetChanged()
        }
        tvPopularityASC.setOnClickListener {
            dialog.dismiss()
            Collections.sort(postingList,
                java.util.Comparator<ResultsItem?> { lhs, rhs -> lhs.popularity!!.compareTo(rhs.popularity!!) })
            adapter.notifyDataSetChanged()
        }
        tvRatingDSC.setOnClickListener {
            dialog.dismiss()
            Collections.sort(postingList,
                java.util.Comparator<ResultsItem?> { rhs, lhs -> lhs.voteAverage!!.compareTo(rhs.voteAverage!!) })
            adapter.notifyDataSetChanged()
        }
        tvRatingASC.setOnClickListener {
            dialog.dismiss()
            Collections.sort(postingList,
                java.util.Comparator<ResultsItem?> { lhs, rhs -> lhs.voteAverage!!.compareTo(rhs.voteAverage!!) })
            adapter.notifyDataSetChanged()
        }

        tvReleaseDateDSC.setOnClickListener {
            dialog.dismiss()
            Collections.sort(postingList,
                java.util.Comparator<ResultsItem?> { rhs, lhs -> lhs.releaseDate!!.compareTo(rhs.releaseDate!!) })
            adapter.notifyDataSetChanged()
        }
        tvReleaseDateASC.setOnClickListener {
            dialog.dismiss()
            Collections.sort(postingList,
                java.util.Comparator<ResultsItem?> { lhs, rhs -> lhs.releaseDate!!.compareTo(rhs.releaseDate!!) })
            adapter.notifyDataSetChanged()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    //Click
    override fun clickListener() {
        binding.llFilterSort.setOnClickListener(this)
    }

    override fun setCode() {
        if (AppUtils.hasInternetConnection(application)) {
            viewmodel.movieListApi(RestConstant.api_key, RestConstant.language, page.toString())
            binding.nestedSV.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
                if (scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight) {
                    if (RestConstant.isNextApiCall) {
                        page++
                        viewmodel.movieListApi(
                            RestConstant.api_key,
                            RestConstant.language,
                            page.toString()
                        )
                    }
                }
            }
        } else {
            getMoviewListFromDB()
        }

    }

    override fun getIntentData() {

    }

    private fun setMovieList(data: List<ResultsItem>) {
        if (data.isNotEmpty()) {
            postingList.addAll(data)
            adapter.notifyDataSetChanged()
            for (iData in postingList) {
                CoroutineScope(Dispatchers.IO).launch {
                    iData?.let { movieDatabase.addMovie(it) }
                }
            }
        }
    }

    private fun getMoviewListFromDB() {
        lifecycleScope.launch {
            movieDatabase.getMovie().collect { movieList ->
                if (movieList.isNotEmpty()) {
                    adapter = MovieListAdapter(this@DashboardActivity, movieList)
                    binding.recyclerview.layoutManager = GridLayoutManager(activity, 2)
                    binding.recyclerview.itemAnimator = DefaultItemAnimator()
                    binding.recyclerview.adapter = adapter

                    adapter.setOnItemClickLister(object :
                        MovieListAdapter.OnItemClickLister {
                        override fun itemClicked(view: View?, position: Int, catId: Int) {
                            startActivity(
                                Intent(activity, MovieDetailActivity::class.java)
                                    .putExtra(
                                        UserParams.movie_id,
                                        movieList[position]!!.id.toString()
                                    )
                            )
                        }
                    })
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setMovieListAdapter() {
        adapter = MovieListAdapter(this, postingList)
        binding.recyclerview.layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerview.itemAnimator = DefaultItemAnimator()
        binding.recyclerview.adapter = adapter

        adapter.setOnItemClickLister(object :
            MovieListAdapter.OnItemClickLister {
            override fun itemClicked(view: View?, position: Int, catId: Int) {
                startActivity(
                    Intent(activity, MovieDetailActivity::class.java)
                        .putExtra(UserParams.movie_id, postingList[position]!!.id.toString())
                )
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llFilterSort -> {
                dialogSortBy()
            }
        }
    }
}