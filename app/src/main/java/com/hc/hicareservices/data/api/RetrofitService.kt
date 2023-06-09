package com.hc.hicareservices.data.api

import com.google.gson.GsonBuilder
import com.hc.hicareservices.utils.AppUtils2
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    var retrofitService: IRetrofit? = null

    fun getOtpInstance(): IRetrofit {
        val gson = GsonBuilder().setLenient().create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://connect.hicare.in/mobileapi_uat/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            retrofitService = retrofit.create(IRetrofit::class.java)
        }
        return retrofitService!!
    }

    fun getInstance(): IRetrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer ${AppUtils2.TOKEN}").build()
                chain.proceed(request)
            }.addInterceptor(interceptor).build()
        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://connect.hicare.in/mobileapi_uat/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            retrofitService = retrofit.create(IRetrofit::class.java)
        }
        return retrofitService!!
    }
}