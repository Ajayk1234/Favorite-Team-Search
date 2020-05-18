package com.example.favoriteteamsearch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.favoriteteamsearch.models.Teams
import com.example.favoriteteamsearch.navigationcomponents.NavigationViewModel

class FavoriteTeamSearchResultViewModel: NavigationViewModel() {


    private val _details = MutableLiveData<ArrayList<Teams>>()
    val details: LiveData<ArrayList<Teams>>? = _details

    fun buildTeamDetails(data: ArrayList<Teams>?) {
        _details.value = data
    }

}