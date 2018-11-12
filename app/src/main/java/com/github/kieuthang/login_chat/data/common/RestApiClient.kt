package com.github.kieuthang.login_chat.data.common

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestApiClient {
    val BASE_PRODUCTION_URL = "http://192.168.0.124:3000/"
//    val BASE_PRODUCTION_URL = "http://192.168.1.187:3000/"
    private var retrofit: Retrofit? = null


    val client: Retrofit
        get() {
            if (retrofit == null) {
                val okHttpClient = OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build()
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_PRODUCTION_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build()
            }
            return retrofit!!
        }
}
