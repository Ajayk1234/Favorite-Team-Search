package com.example.itemsearch.service

import com.example.itemsearch.repository.ItemSearchApi
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ItemSearchRetrofit {

    /**
     * @return an instance of [ItemService]
     */
    fun getFavTeamSearchService(): ItemSearchApi =
        getProvider(
            baseUrl = "https://api.flickr.com/services/feeds/",
            serviceClass = ItemSearchApi::class.java
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