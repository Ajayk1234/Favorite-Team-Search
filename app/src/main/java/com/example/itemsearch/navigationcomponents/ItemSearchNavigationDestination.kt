package com.example.itemsearch.navigationcomponents

import android.os.Bundle
import com.example.itemsearch.R
import com.example.itemsearch.models.Item


internal class FavSearchResultDestination(position: Int, data: ArrayList<Item>) : NavigationDestination(
    R.id.search_result, getArgument(position, data)
) {
    companion object {
        private const val ITEM_POSITION_KEY = "ITEM_POSITION_KEY"
        private const val DATA_ITEM = "DATA_ITEM"

        internal fun getPosition(arguments: Bundle) =
            arguments.getInt(ITEM_POSITION_KEY)

        internal fun getData(arguments: Bundle) =
            arguments.getParcelableArrayList<Item>(DATA_ITEM)


        internal fun getArgument(position: Int, data: ArrayList<Item>) =
            NavigationArguments.create {
                putParcelableInt(ITEM_POSITION_KEY, position)
                putParcelableArrayList(DATA_ITEM, data)
            }
    }
}
