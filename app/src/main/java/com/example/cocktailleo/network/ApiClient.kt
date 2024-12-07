package com.example.cocktailleo.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    val instance: CocktailApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CocktailApi::class.java)
    }
}
