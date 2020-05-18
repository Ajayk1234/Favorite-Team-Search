package com.example.favoriteteamsearch.repository

import com.example.favoriteteamsearch.models.ServiceResult
import com.example.favoriteteamsearch.models.Teams

interface FavSearchRepository {

    suspend fun activateTeamSearch(
        favTeamName: String
    ): ServiceResult<ArrayList<Teams>>
}