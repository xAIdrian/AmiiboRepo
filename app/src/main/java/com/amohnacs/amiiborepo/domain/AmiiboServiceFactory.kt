package com.amohnacs.amiiborepo.domain

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AmiiboServiceFactory {
    private const val AMIIBO_URL = "https://amiiboapi.com/"

    private val amiiboBuilder = Retrofit.Builder()
            .baseUrl(AMIIBO_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

    private var retrofit = amiiboBuilder.build()

    private val httpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            TODO("Not yet implemented")
        }

    })

    private val logging = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    fun <S> createAmiiboService(serviceClass: Class<S>): S {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging)
            amiiboBuilder.client(httpClient.build())
            retrofit = amiiboBuilder.build()
        }
        return retrofit.create(serviceClass)
    }
}