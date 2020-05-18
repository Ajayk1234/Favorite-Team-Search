package com.example.favoriteteamsearch.ui

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.favoriteteamsearch.R
import com.example.favoriteteamsearch.models.ServiceException
import com.example.favoriteteamsearch.models.ServiceResult
import com.example.favoriteteamsearch.models.Teams
import com.example.favoriteteamsearch.navigationcomponents.FavSearchResultDestination
import com.example.favoriteteamsearch.navigationcomponents.NavigationViewModel
import com.example.favoriteteamsearch.repository.FavSearchRepository
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class FavoriteSearchHomeScreenViewModel(public val favSearchRepository: FavSearchRepository) :
    NavigationViewModel() {

    var teamName = MutableLiveData<String>()
    private var _showProgress = MutableLiveData<Boolean>()
    var showProgress: LiveData<Boolean> = _showProgress
    private var _errorMsg = MutableLiveData<@StringRes Int?>()
    var errorMsg: LiveData<Int?> = _errorMsg
    private var _dialog = MutableLiveData<ServiceException>()
    var dialog: LiveData<ServiceException> = _dialog

    /**
     * Set live data property to display message
     * @param message - could be a string or StringWrapper
     */
    fun setMessageForDialog(message: ServiceException?) {
        _dialog.value = message
    }

    /**
     * Resets the error to null. It wont be shown by default on the page resume
     */
    fun clearDialogError() = run {
        _dialog.value = null
    }


    fun onContinueClick() {
        if (validateUI()) {
            setProgress(true)
            viewModelScope.launch {
                handleApiResult(favSearchRepository.activateTeamSearch(teamName.value.orEmpty()))
            }
        }
    }

    private fun handleApiResult(activateTeamSearch: ServiceResult<ArrayList<Teams>>) {

        when (activateTeamSearch) {
            is ServiceResult.Error -> {
                setProgress(false)
                setMessageForDialog(
                    ServiceException(
                        activateTeamSearch.exception.code.orEmpty(),
                        activateTeamSearch.exception.string
                    )
                )
            }
            is ServiceResult.Success -> {
                val data = activateTeamSearch.data
                setProgress(false)
                navigateTo(FavSearchResultDestination(data))
            }
        }

    }

    private fun validateUI(): Boolean {

        if (teamName.value.isNullOrEmpty()) {

            _errorMsg.value = R.string.empty_field_error
            return false
        }
        if (teamName.value.toString().isSpecialCharExist()) {
            _errorMsg.value = R.string.special_char_field_error

            return false
        }
        return true
    }

    fun String.isSpecialCharExist(): Boolean {
        val pattern = Pattern.compile("[a-zA-Z0-9 ]*")
        val matcher = pattern.matcher(this)
        if (!matcher.matches()) {
            return true
        }
        return false
    }

    /**
     * Set live data property to show/hide status flag
     * @param status - boolean to enable/disable
     */
    fun setProgress(status: Boolean) {
        _showProgress.value = status
    }

    /**
     * The below function invoke when tap and typing characters in PNR edit text
     */
    fun clearPnrError() = run {
        _errorMsg.value = null
    }
}