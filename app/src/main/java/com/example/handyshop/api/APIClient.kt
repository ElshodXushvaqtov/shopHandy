package com.example.handyshop.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {

    private const val BASE_URL = "http://handybook.uz"

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private var client: OkHttpClient = OkHttpClient()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

}