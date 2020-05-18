package com.example.favoriteteamsearch.navigationcomponents

import android.os.Bundle
import com.example.favoriteteamsearch.R
import com.example.favoriteteamsearch.models.Teams


internal class FavSearchResultDestination(data: ArrayList<Teams>) : NavigationDestination(
    R.id.search_result, getArgument(data)
) {
    companion object {
        private const val TEAM_DATA_KEY = "KEY_SELECTED_CREDIT_CARD"
        internal fun getData(arguments: Bundle) =
            arguments.getParcelableArrayList<Teams>(TEAM_DATA_KEY)

        internal fun getArgument(data: ArrayList<Teams>) =
            NavigationArguments.create {
                putParcelableArrayList(TEAM_DATA_KEY, data)
            }
    }
}
