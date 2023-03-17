package com.technometrics.ui.viewmodel.moviedetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technometrics.data.api.Resource
import com.technometrics.data.model.moviedetail.MovieDetailResponse
import com.technometrics.data.repository.AppRepository
import com.technometrics.utils.AppUtils
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieDetailViewModel(
    var application: Application,
    private var appRepository: AppRepository
) : ViewModel() {
    var no_internet_connection: String = ""

    private val movieDetailApiCall = MutableLiveData<Resource<MovieDetailResponse>>()
    val movieDetailResponse: LiveData<Resource<MovieDetailResponse>> =
        movieDetailApiCall

    fun movieDetailApi(movie_id: String, api_key: String) =
        viewModelScope.launch { movieDetailApiCall(movie_id, api_key) }

    private suspend fun movieDetailApiCall(
        movie_id: String, api_key: String
    ) {
        movieDetailApiCall.postValue(Resource.Loading())
        if (AppUtils.hasInternetConnection(application)) {
            val response = appRepository.movieDetailRepository(movie_id, api_key)
            movieDetailApiCall.postValue(movieDetailResponse(response))
        } else {
            movieDetailApiCall.postValue(Resource.Error(no_internet_connection))
        }
    }

    //handle the response success or fail
    private fun movieDetailResponse(response: Response<MovieDetailResponse>): Resource<MovieDetailResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        } else if (response.errorBody() != null) {
            AppUtils.getErrorMessage(response.errorBody()!!)?.let { it1 ->
                return Resource.Error(it1)
            }
        }
        return Resource.Error(response.message())
    }

}