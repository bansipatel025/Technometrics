package com.technometrics.data.api


object RestConstant {

    const val BASE_URLS = "https://api.themoviedb.org/3/"

    const val image_path = "https://image.tmdb.org/t/p/original"

    const val movie = "movie/top_rated"
    const val movieDetail = "movie/{movie_id}"

    const val TableName = "movie"

    var api_key = "82991e79869955df69a6fc8c190720e9"
    var language = "en-US"

    var isNextApiCall = false

}