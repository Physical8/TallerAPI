package com.example.cocktailleo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cocktailleo.network.ApiClient
import com.example.cocktailleo.data.models.CocktailResponse
import com.example.cocktailleo.data.models.Drink
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var cocktailName: EditText
    private lateinit var searchButton: Button
    private lateinit var randomButton: Button
    private lateinit var cocktailImage: ImageView
    private lateinit var cocktailDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // Si usabas esta función, descoméntala
        setContentView(R.layout.activity_main)

        cocktailName = findViewById(R.id.cocktailName)
        searchButton = findViewById(R.id.searchButton)
        randomButton = findViewById(R.id.randomButton)
        cocktailImage = findViewById(R.id.cocktailImage)
        cocktailDetails = findViewById(R.id.cocktailDetails)

        searchButton.setOnClickListener {
            val name = cocktailName.text.toString().trim()
            if (name.isNotEmpty()) {
                fetchCocktail(name)
            } else {
                cocktailDetails.text = "Please enter a cocktail name."
            }
        }

        randomButton.setOnClickListener {
            fetchRandomCocktail()
        }
    }

    private fun fetchCocktail(name: String) {
        ApiClient.instance.getCocktail(name).enqueue(object : Callback<CocktailResponse> {
            override fun onResponse(call: Call<CocktailResponse>, response: Response<CocktailResponse>) {
                try {
                    if (response.isSuccessful) {
                        val drinks = response.body()?.drinks
                        if (drinks != null && drinks.isNotEmpty()) {
                            val drink = drinks.random() // Puedes elegir uno aleatorio si hay varios resultados
                            displayCocktail(drink)
                        } else {
                            cocktailDetails.text = "No cocktail found with that name."
                            cocktailImage.setImageResource(0) // Limpia la imagen
                        }
                    } else {
                        cocktailDetails.text = "Failed to fetch data. Code: ${response.code()}"
                    }
                } catch (e: Exception) {
                    cocktailDetails.text = "Error parsing response: ${e.message}"
                }
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                cocktailDetails.text = "Error fetching data: ${t.message}"
            }
        })
    }

    private fun fetchRandomCocktail() {
        ApiClient.instance.getRandomCocktail().enqueue(object : Callback<CocktailResponse> {
            override fun onResponse(call: Call<CocktailResponse>, response: Response<CocktailResponse>) {
                try {
                    if (response.isSuccessful) {
                        val drinks = response.body()?.drinks
                        if (drinks != null && drinks.isNotEmpty()) {
                            val drink = drinks[0]
                            displayCocktail(drink)
                        } else {
                            cocktailDetails.text = "No cocktail found."
                            cocktailImage.setImageResource(0)
                        }
                    } else {
                        cocktailDetails.text = "Failed to fetch data. Code: ${response.code()}"
                    }
                } catch (e: Exception) {
                    cocktailDetails.text = "Error parsing response: ${e.message}"
                }
            }

            override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                cocktailDetails.text = "Error fetching data: ${t.message}"
            }
        })
    }

    private fun displayCocktail(drink: Drink) {
        cocktailDetails.text = """
            Name: ${drink.strDrink}
            Ingredients: ${drink.strIngredient1}, ${drink.strIngredient2}, ${drink.strIngredient3}
            Instructions: ${drink.strInstructions}
        """.trimIndent()

        Picasso.get().load(drink.strDrinkThumb).into(cocktailImage)
    }
}
