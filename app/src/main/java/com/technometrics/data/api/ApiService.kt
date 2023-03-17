package com.technometrics.data.api

import com.technometrics.data.model.movie.MoviesResponse
import com.technometrics.data.model.moviedetail.MovieDetailResponse
import retrofit2.Response

import retrofit2.http.*

interface ApiService {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET(RestConstant.movie)
    suspend fun movieList(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: String
    ): Response<MoviesResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET(RestConstant.movieDetail)
    suspend fun movieDetail(
        @Path("movie_id") movie_id: String,
        @Query("api_key") api_key: String
    ): Response<MovieDetailResponse>
}