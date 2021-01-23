package com.example.itemsearch.repository

import com.example.itemsearch.models.*
import com.example.itemsearch.service.ItemSearchRetrofit
import com.example.itemsearch.service.ItemSearchRetrofitCallbackHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class  ItemSearchRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val itemSearchApi: ItemSearchApi = ItemSearchRetrofit.getFavTeamSearchService()
) : ItemSearchRepository {

    override suspend fun activateTeamSearch(itemName: String): ServiceResult<SearchResponse> {

        val result = withContext(ioDispatcher) {
            ItemSearchRetrofitCallbackHandler.processCall {
                itemSearchApi.searchData(itemName)
            }

        }
        return when (result) {
            is ServiceResult.Success -> transformResponseToTeamsObject(result.data)
            is ServiceResult.Error -> result
        }
    }

    private fun transformResponseToTeamsObject(response: SearchResponse): ServiceResult<SearchResponse> {
        response.apply {
            if (response.items.isNullOrEmpty()) {
                return ServiceResult.Error(
                    ServiceException(
                        null,
                        "Please enter valid team name"
                    )
                )
            }
            response.let {
                return ServiceResult.Success(it)
            }
        }
    }
}