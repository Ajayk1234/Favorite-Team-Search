package com.example.itemsearch.repository

import com.example.itemsearch.models.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItemSearchApi {
    @GET("photos_public.gne?nojsoncallback=1&tagmode=any&format=json")
    suspend fun searchData(@Query("tags") searchTag: String): Response<SearchResponse>
}