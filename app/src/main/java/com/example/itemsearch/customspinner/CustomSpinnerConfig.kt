package com.example.itemsearch.customspinner

import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.itemsearch.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomSpinnerConfig(
    val isCancellable: Boolean = false,
    val isTransparent: Boolean = true,
    val spinnerTag: String = "",
    val cancelContentDescription: String = "",
    @StringRes val hintMessageId: Int = R.string.spinner_wait_text
) : Parcelable {

    /**
     * Function to get Window style need for custom spinner fragment
     */
    fun getWindowStyle() = if (isTransparent) {
        R.style.spinner_translucent_dim
    } else {
        R.style.spinner_translucent
    }
}