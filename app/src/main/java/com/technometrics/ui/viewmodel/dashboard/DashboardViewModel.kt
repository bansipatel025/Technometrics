package com.technometrics.ui.viewmodel.dashboard

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technometrics.data.api.Resource
import com.technometrics.data.model.movie.MoviesResponse
import com.technometrics.data.repository.AppRepository
import com.technometrics.utils.AppUtils
import kotlinx.coroutines.launch
import retrofit2.Response

class DashboardViewModel(
    var application: Application,
    private var appRepository: AppRepository
) : ViewModel() {
    var no_internet_connection: String = ""

    private val movieListApiCall = MutableLiveData<Resource<MoviesResponse>>()
    val movieListResponse: LiveData<Resource<MoviesResponse>> =
        movieListApiCall

    fun movieListApi(api_key: String, language: String, page: String) =
        viewModelScope.launch { movieListApiCall(api_key, language, page) }

    private suspend fun movieListApiCall(
        api_key: String, language: String, page: String
    ) {
        movieListApiCall.postValue(Resource.Loading())
        if (AppUtils.hasInternetConnection(application)) {
            val response = appRepository.movieListRepository(api_key, language, page)
            movieListApiCall.postValue(movieListResponse(response))
        } else {
            movieListApiCall.postValue(Resource.Error(no_internet_connection))
        }
    }

    //handle the response success or fail
    private fun movieListResponse(response: Response<MoviesResponse>): Resource<MoviesResponse> {
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