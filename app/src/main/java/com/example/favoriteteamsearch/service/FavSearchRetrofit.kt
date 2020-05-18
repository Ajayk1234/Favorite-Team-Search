package com.example.favoriteteamsearch.service

import com.example.favoriteteamsearch.repository.FavTeamSearchApi
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FavSearchRetrofit {

    /**
     * @return an instance of [CheckInService]
     */
    fun getFavTeamSearchService(): FavTeamSearchApi =
        getProvider(
            baseUrl = "https://www.thesportsdb.com/api/v1/json/1/",
            serviceClass = FavTeamSearchApi::class.java
        )

    /**
     * Returns the appropriate [serviceClass] which will use the provided [client]. The [baseUrl] sets the URL that it points at.
     */

    private fun <T> getProvider(
        baseUrl: String,
        serviceClass: Class<T>

    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

        return retrofit.create(serviceClass)
    }
}