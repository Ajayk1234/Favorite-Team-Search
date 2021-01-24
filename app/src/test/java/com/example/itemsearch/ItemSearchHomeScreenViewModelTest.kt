package com.example.itemsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.itemsearch.models.Item
import com.example.itemsearch.models.SearchResponse
import com.example.itemsearch.models.ServiceException
import com.example.itemsearch.models.ServiceResult
import com.example.itemsearch.repository.ItemSearchRepository
import com.example.itemsearch.ui.ItemSearchHomeScreenViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.regex.Pattern

@RunWith(MockitoJUnitRunner::class)
class ItemSearchHomeScreenViewModelTest {

    private var viewModel: ItemSearchHomeScreenViewModel
    private val itemSearchRepository: ItemSearchRepository = mock(ItemSearchRepository::class.java)
    private lateinit var searchResponse: ServiceResult<SearchResponse>

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    init {
        viewModel = ItemSearchHomeScreenViewModel(
            itemSearchRepository = itemSearchRepository
        )
    }

    @Test
    fun fetchRetroInfoTest_success() {
        val itemModel = Item(
            "DSCF6603", "https://www.flickr.com/photos/tags/car/", null,
            "2020-04-10T13:24:13-08:00",
            "posted a photo", "2021-01-21T18:45:02Z",
            "nobody", "96016676", "tromsoya troms"
        )
        val itemList = ArrayList<Item>()
        itemList.add(itemModel)

        val searchResponseModel = SearchResponse(
            "Recent Uploads tags", "https://www.flickr.com/photos/tags/car/",
            "", null, null, itemList
        )

        searchResponse = ServiceResult.Success(searchResponseModel)

        runBlocking {
            Mockito.`when`(itemSearchRepository.activateTeamSearch("car")).thenReturn(searchResponse)
            viewModel.handleApiResult("car")
        }

        Assert.assertEquals(1, viewModel.searchLiveData.value?.items?.size)
    }

    @Test
    fun fetchRetroInfoTest_Failure_Scenario() {
        val searchResponseModel = ServiceException("500", "Bad response")
        searchResponse = ServiceResult.Error(searchResponseModel)
        runBlocking {
            Mockito.`when`(itemSearchRepository.activateTeamSearch("car")).thenReturn(searchResponse)
            viewModel.handleApiResult("")
        }
        Assert.assertNull(viewModel.searchLiveData.value)
    }

    @Test
    fun testNotAValidSearch() {
        val userInput = "dog&8"
        if (userInput.isSpecialCharExist()) {
            assertEquals(viewModel.validateUI(userInput), true)
        }
    }

    @Test
    fun testValidSearch() {
        val userInput = "dog"
        if (userInput.isSpecialCharExist()) {
            assertEquals(viewModel.validateUI(userInput), false)
        }
    }

    private fun String.isSpecialCharExist(): Boolean {
        val pattern = Pattern.compile("[a-zA-Z0-9 ]*")
        val matcher = pattern.matcher(this)
        if (!matcher.matches()) {
            return true
        }
        return false
    }
}