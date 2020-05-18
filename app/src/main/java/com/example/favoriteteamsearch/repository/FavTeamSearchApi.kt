package com.example.favoriteteamsearch.repository

import com.example.favoriteteamsearch.models.FavTeamSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FavTeamSearchApi {
    @GET("searchteams.php?")
    suspend fun getFavTeamDetails(@Query("t") str: String): Response<FavTeamSearchResponse>
}