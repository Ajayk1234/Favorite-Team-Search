package com.example.itemsearch.repository

import com.example.itemsearch.models.SearchResponse
import com.example.itemsearch.models.ServiceResult

interface ItemSearchRepository {

    suspend fun activateTeamSearch(
        itemName: String
    ): ServiceResult<SearchResponse>
}