package com.example.cocktailleo.network

import com.example.cocktailleo.data.models.CocktailResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {

    @GET("search.php")
    fun getCocktail(@Query("s") name: String): Call<CocktailResponse>

    @GET("random.php")
    fun getRandomCocktail(): Call<CocktailResponse>
}
