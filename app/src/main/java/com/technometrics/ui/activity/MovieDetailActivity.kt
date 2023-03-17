package com.technometrics.ui.activity

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.technometrics.R
import com.technometrics.base.BaseActivity
import com.technometrics.data.api.Resource
import com.technometrics.data.api.RestConstant
import com.technometrics.data.api.UserParams
import com.technometrics.data.model.moviedetail.MovieDetailResponse
import com.technometrics.data.repository.MovieDatabase
import com.technometrics.databinding.ActivityMoviedetailBinding
import com.technometrics.ui.factory.MovieDetailFactory
import com.technometrics.ui.viewmodel.moviedetail.MovieDetailViewModel
import com.technometrics.utils.AppUtils
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.Objects


class MovieDetailActivity : BaseActivity(), KodeinAware, View.OnClickListener {
    private val movieDatabase by lazy { MovieDatabase.getDatabase(this).movieDao() }
    private lateinit var viewmodel: MovieDetailViewModel
    private val factory: MovieDetailFactory by instance()
    override val kodein: Kodein by kodein()
    private lateinit var binding: ActivityMoviedetailBinding
    var movie_id: String? = ""
    private var like: Boolean = false

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            activity, R.layout.activity_moviedetail
        )
        setupViewModel()
        getIntentData()
        setupObservers()
        clickListener()
        setCode()

    }

    private fun setupObservers() {
        viewmodel.movieDetailResponse.observe(this) { response ->
            when (response) {
                is Resource.Success -> {
                    Log.d("Response", "====== movieDetailResponse Response ====> $response")
                    response.data?.let { movieDetailResponse ->
                        setMoviedetails(movieDetailResponse)
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

    private fun setMoviedetails(data: MovieDetailResponse) {
        val rString = StringBuilder()
        var separator = ""
        for (each in data.genres!!) {
            rString.append(separator).append(each!!.name)
            separator = ", "
        }
        Glide.with(Objects.requireNonNull(this))
            .load(RestConstant.image_path + data!!.backdropPath)
            .into(binding.ivBackdrop)

        binding.txtMovieName.text = data.title.plus(" - " + rString.toString())
        binding.tvDesc.text = data.overview
        binding.tvtagline.text = data.tagline
        binding.txtMoviestar.text =
            data.voteAverage?.let { AppUtils.roundSingleDecimals(it).toString() }

        binding.tvReleaseDate.text =
            resources.getString(R.string.release_date).plus(AppUtils.dateChange(data.releaseDate!!))

    }

    private fun setupViewModel() {
        viewmodel = ViewModelProvider(this, factory).get(MovieDetailViewModel::class.java)
        binding.viewmodel = viewmodel
    }

    //Click
    override fun clickListener() {
        binding.toolBar.ivBack.setOnClickListener(this)
        binding.ivFavorite.setOnClickListener(this)
    }

    override fun setCode() {

    }

    override fun getIntentData() {
        movie_id = intent.getStringExtra(UserParams.movie_id)
        if (AppUtils.hasInternetConnection(application)) {
            movie_id?.let { viewmodel.movieDetailApi(it, RestConstant.api_key) }
        } else {
            movie_id?.let { getMoviewListFromDB(it) }
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }

            R.id.ivFavorite -> {
                if (like) {
                    binding.ivFavorite.setColorFilter(
                        ContextCompat.getColor(
                            activity!!, R.color.black
                        ), PorterDuff.Mode.MULTIPLY
                    )
                    like = false
                } else {

                    binding.ivFavorite.setColorFilter(
                        ContextCompat.getColor(
                            activity!!, R.color.red
                        ), PorterDuff.Mode.MULTIPLY
                    )
                    like = true
                }

            }
        }
    }

    private fun getMoviewListFromDB(movie_id: String) {
        lifecycleScope.launch {
            movieDatabase.getMovieDetail(movie_id).collect { movieList ->
                if (movieList.isNotEmpty()) {
                    Glide.with(Objects.requireNonNull(this@MovieDetailActivity))
                        .load(RestConstant.image_path + movieList[0]!!.backdropPath)
                        .into(binding.ivBackdrop)

                    binding.txtMovieName.text = movieList[0].title
                    binding.tvDesc.text = movieList[0].overview
                    binding.txtMoviestar.text = movieList[0].voteAverage
                    binding.tvReleaseDate.text =
                        resources.getString(R.string.release_date)
                            .plus(AppUtils.dateChange(movieList[0].releaseDate!!))
                }
            }
        }
    }
}