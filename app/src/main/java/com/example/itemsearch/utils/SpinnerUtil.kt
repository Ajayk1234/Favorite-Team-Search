package com.example.itemsearch.utils


import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.itemsearch.customspinner.CustomSpinnerConfig
import com.example.itemsearch.customspinner.CustomSpinnerFragment

private const val REQUEST_SPINNER = 987

@Synchronized
internal fun Fragment.presentSpinner(config: CustomSpinnerConfig): CustomSpinnerFragment? {
    if (!isAdded) return null
    if (findSpinnerFragment() != null) return null
    activity?.hideKeyboard()
    val spinner = CustomSpinnerFragment.newInstance(config)
    spinner.setTargetFragment(this, REQUEST_SPINNER)
    spinner.show(parentFragmentManager, CustomSpinnerFragment.UNIQUE_TAG)
    return spinner
}

internal fun Fragment.findSpinnerFragment(): CustomSpinnerFragment? {
    return if (isAdded) parentFragmentManager.findFragmentByTag(CustomSpinnerFragment.UNIQUE_TAG) as? CustomSpinnerFragment
    else null
}

internal fun Activity.hideKeyboard() {
    val token = currentFocus?.windowToken ?: return
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
}
