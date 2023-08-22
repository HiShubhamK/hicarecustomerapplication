package com.ab.hicareservices.data.api

import com.google.gson.GsonBuilder
import com.ab.hicareservices.utils.AppUtils2
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
//                .baseUrl("http://connect.hicare.in/mobileapi_uat/api/") //uat
//                .baseUrl("http://connect.hicare.in/mobileapi/api/") //prod
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            retrofitService = retrofit.create(IRetrofit::class.java)
        }
        return retrofitService!!
    }

    fun getProductInstance(): IRetrofit {
        val gson = GsonBuilder().setLenient().create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().readTimeout(120, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).addInterceptor(interceptor).build()
//        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
//                .baseUrl("http://connect.hicare.in/product/api/mobile/") //prod
                .baseUrl("http://3.111.154.100/UATProductAdmin/api/mobile/")//uat

                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            retrofitService = retrofit.create(IRetrofit::class.java)
//        }
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
//        if (retrofitService == null) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://connect.hicare.in/mobileapi/api/") //prod
//                .baseUrl("http://connect.hicare.in/mobileapi_uat/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            retrofitService = retrofit.create(IRetrofit::class.java)
//        }
        return retrofitService!!
    }
}