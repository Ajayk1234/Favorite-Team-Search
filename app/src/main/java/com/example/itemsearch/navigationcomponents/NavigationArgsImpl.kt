package com.example.itemsearch.navigationcomponents

import android.os.Bundle
import android.os.Parcelable

/**
 * A class that wraps Bundle in a way that it is unit testable. All functions map one to one to the Bundle interface.
 *
 * Always create these with [NavigationArguments.create] so that the implementation can be swapped
 * out for testing.
 */
internal class NavigationArgsImpl(private val bundle: Bundle = Bundle()) :
    NavigationArguments {
    override fun asBundle() = bundle
    override fun putParcelableInt(
        key: String,
        value: Int
    ): NavigationArguments {
        bundle.putInt(key, value)
        return this
    }

    override fun putParcelableArrayList(
        key: String,
        value: ArrayList<out Parcelable>
    ): NavigationArguments {
        bundle.putParcelableArrayList(key, value)
        return this
    }

}
