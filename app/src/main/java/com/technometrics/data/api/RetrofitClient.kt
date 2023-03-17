package com.technometrics.data.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        private const val TIME = 5
        private val retrofitClient: Retrofit.Builder by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val client = OkHttpClient.Builder()
                .connectTimeout(TIME.toLong(), TimeUnit.MINUTES)
                .readTimeout(TIME.toLong(), TimeUnit.MINUTES)
                .writeTimeout(TIME.toLong(), TimeUnit.MINUTES)
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(RestConstant.BASE_URLS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
        }

        val apiInterface: ApiService by lazy {
            retrofitClient
                .build()
                .create(ApiService::class.java)
        }
    }
}
