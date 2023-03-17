package com.technometrics.data.repository

import com.technometrics.data.api.RetrofitClient

class AppRepository {

    suspend fun movieListRepository(api_key: String, language: String, page: String) =
        RetrofitClient.apiInterface.movieList(api_key, language, page)

    suspend fun movieDetailRepository(movie_id: String, api_key: String) =
        RetrofitClient.apiInterface.movieDetail(movie_id, api_key)

}
