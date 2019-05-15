package com.hoc.uploadimage

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val retrofitModule = applicationContext {
    bean<OkHttpClient> {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val level = HttpLoggingInterceptor.Level.BASIC
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(level))
        }

        builder.build()
    }
    bean<Retrofit> {
        Retrofit.Builder()
                .client(get())
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
    bean<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}
