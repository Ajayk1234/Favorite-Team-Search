package com.example.itemsearch

import com.example.itemsearch.repository.ItemSearchRepository
import com.example.itemsearch.ui.ItemSearchHomeScreenViewModel
import junit.framework.TestCase
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.regex.Pattern

class ItemSearchHomeScreenViewModelTest: TestCase() {

    private var viewModel: ItemSearchHomeScreenViewModel
    private val itemSearchRepository: ItemSearchRepository = mock(ItemSearchRepository::class.java)
    init {
        viewModel = ItemSearchHomeScreenViewModel(
            itemSearchRepository = itemSearchRepository
        )
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