package com.shan.library.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import lk.vci.venturacollector.common.Constant
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitServiceBuilder {
    val retrofitService: RetrofitServices by lazy {
        retrofit.create(RetrofitServices::class.java)
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL_TEST)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}