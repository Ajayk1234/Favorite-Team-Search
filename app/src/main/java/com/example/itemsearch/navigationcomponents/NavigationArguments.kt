package com.example.itemsearch.navigationcomponents

import android.os.Bundle
import android.os.Parcelable

/**
 * A class that wraps Bundle in a way that it is unit testable. All functions map one to one to the Bundle interface.
 */
interface NavigationArguments {
    fun asBundle(): Bundle? = null
    fun putParcelableInt(key: String, value: Int): NavigationArguments
    fun putParcelableArrayList(key: String, value: ArrayList<out Parcelable>): NavigationArguments

    companion object {
        var create: (NavigationArguments.() -> Unit) -> NavigationArguments =
            { block -> NavigationArgsImpl().apply(block) }
    }
}
