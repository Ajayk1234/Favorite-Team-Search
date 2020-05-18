package com.example.favoriteteamsearch.repository

import com.example.favoriteteamsearch.models.FavTeamSearchResponse
import com.example.favoriteteamsearch.models.ServiceException
import com.example.favoriteteamsearch.models.ServiceResult
import com.example.favoriteteamsearch.models.Teams
import com.example.favoriteteamsearch.service.FavSearchRetrofit
import com.example.favoriteteamsearch.service.FavSearchRetrofitCallbackHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavSearchRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val favTeamSearchApi: FavTeamSearchApi = FavSearchRetrofit.getFavTeamSearchService()
) : FavSearchRepository {

    override suspend fun activateTeamSearch(favTeamName: String): ServiceResult<ArrayList<Teams>> {

        val result = withContext(ioDispatcher) {
            FavSearchRetrofitCallbackHandler.processCall {
                favTeamSearchApi.getFavTeamDetails(favTeamName)
            }

        }
        return when (result) {
            is ServiceResult.Success -> transformResponseToTeamsObject(result.data)
            is ServiceResult.Error -> result
        }
    }

    internal fun transformResponseToTeamsObject(response: FavTeamSearchResponse): ServiceResult<ArrayList<Teams>> {
        response.apply {
            if (response.teams.isNullOrEmpty()) {
                return ServiceResult.Error(
                    ServiceException(
                        null,
                        "Please enter valid team name"
                    )
                )
            }
            response.teams.let {
                return ServiceResult.Success(it)
            }
        }
    }
}