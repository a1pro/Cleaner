package com.applications.cleaner.Retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {


    private const val BASE_URL = "https://crm.thebespokecleanercrm.com/"
  //  private const val BASE_URL = "https://magento.a1professionals.net/crm/"


    val instance: Api by lazy {


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
        retrofit.create(Api::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .readTimeout(500, TimeUnit.SECONDS)
            .connectTimeout(500, TimeUnit.SECONDS)
            .build();
        return okHttpClient
    }


}



