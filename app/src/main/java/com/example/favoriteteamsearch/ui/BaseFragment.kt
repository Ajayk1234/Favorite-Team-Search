package com.example.favoriteteamsearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.favoriteteamsearch.R
import com.example.favoriteteamsearch.customspinner.CustomSpinnerConfig
import com.example.favoriteteamsearch.customspinner.CustomSpinnerFragment
import com.example.favoriteteamsearch.models.ServiceException
import com.example.favoriteteamsearch.navigationcomponents.LiveNavigationField
import com.example.favoriteteamsearch.navigationcomponents.NavigationEvent
import com.example.favoriteteamsearch.utils.findSpinnerFragment
import com.example.favoriteteamsearch.utils.presentSpinner
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseFragment : Fragment() {

    /**
     * The Id of the resource file to be inflated in the fragment.
     */
    protected abstract val layoutResourceId: Int
    private var spinner: CustomSpinnerFragment? = null


    final override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, state: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(layoutResourceId, container, false)
    }

    /**
     * Sets up the fragment to listen for navigation events from [navigationLiveDataField]
     */
    protected fun configureNavigationListener(navigationLiveDataField: LiveNavigationField<NavigationEvent>) {
        navigationLiveDataField.observe(viewLifecycleOwner, Observer { event ->
            hideSpinner()
            findNavController().navigate(event.navId, event.argumentsBundle())
        })
    }

    /**
     * Function callback will be invoked when spinner fragment cancelled action been triggered indicated by [tag]
     */
    open fun spinnerCanceled(tag: String) = Unit

    /**
     * Present Spinner fragment with configurations specified.
     *
     * @param isCancellable whether to show cancel button
     * @param isTransparent whether to show complete transparent background
     * @param tag tag for spinner fragment
     * @param cancelContentDescription content description for dismiss button
     * @param hintMessage hint message to be shown in spinner
     */
    fun showSpinner(
        isCancellable: Boolean = false,
        isTransparent: Boolean = true,
        tag: String = "",
        cancelContentDescription: String = "",
        hintMessage: Int = R.string.spinner_wait_text
    ) {
        CustomSpinnerConfig(
            isCancellable = isCancellable,
            isTransparent = isTransparent,
            spinnerTag = tag,
            cancelContentDescription = cancelContentDescription,
            hintMessageId = hintMessage
        ).run {
            spinner = presentSpinner(this)
        }
    }

    /**
     * Function to hide spinner fragment if exist
     */
    @Suppress("unused")
    fun hideSpinner() {
        spinner?.dismiss().also {
            spinner = null
        } ?: this.findSpinnerFragment()?.dismiss()
    }

    /**
     * Show and hide the progress based on boolean flag
     * @param status of the show progress live data property
     */
    fun showOrHideSpinner(status: Boolean) {
        if (status) {
            showSpinner(isTransparent = false)
        } else {
            hideSpinner()
        }
    }

    /**
     * Fragment extension function for obtaining an [instance] of the view model through ViewModelProvider
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified V : ViewModel> Fragment.obtainViewModel(crossinline instance: () -> V): V {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return instance() as T
            }
        }
        return ViewModelProvider(this, factory).get(V::class.java)
    }


    fun showMessageDialogImpl(
        serviceException: ServiceException?,
        actionBlock: () -> Unit = {}
    ) {
        serviceException?.let {
            showMessageDialog(
                (it.string).getFormattedString(requireContext()),
                actionBlock = { actionBlock() }
            )
        }
    }

    private fun showMessageDialog(
        message: String?,
        actionBlock: () -> Unit = {}
    ) {
        message?.let {
            val builder = MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.error_title))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.error_btn_txt)) { dialog, _ ->
                    actionBlock()
                    dialog.dismiss()
                }

            val alertDialog = builder.create()
            alertDialog.show()
        }

    }

    fun hideActionBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.run {
            if (isShowing)
                hide()
        }
    }

    fun showActionBar() {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.run {
            show()
        }
    }

}