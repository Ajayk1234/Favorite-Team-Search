package com.example.favoriteteamsearch.ui

import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.favoriteteamsearch.navigationcomponents.LiveNavigationField
import com.example.favoriteteamsearch.navigationcomponents.NavigationEvent
import com.example.favoriteteamsearch.R
import com.example.favoriteteamsearch.repository.FavSearchRepositoryImpl
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FavoriteSearchHomeScreenFragment : BaseFragment() {


    override val layoutResourceId: Int = R.layout.favorite_search_homescreen

    private val viewModel by lazy {
        obtainViewModel {
            FavoriteSearchHomeScreenViewModel(
                FavSearchRepositoryImpl()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewModel()
        configureUI(view)
    }

    private fun configureViewModel() {
        configureNavigationListener(viewModel.navigationLiveDataField)
        viewModel.showProgress.observe(viewLifecycleOwner, Observer { showOrHideSpinner(it) })
        viewModel.errorMsg.observe(
            viewLifecycleOwner,
            Observer {
                showFieldError(R.id.Edit_text_group, it)
            }
        )
        viewModel.dialog.observe(viewLifecycleOwner, Observer {
            showMessageDialogImpl(it, actionBlock = { viewModel.clearDialogError() })
        })
    }


    private fun configureUI(view: View) {
        view.apply {
            startGradientAnimation(this)
            startCloudAnimation(this)

            findViewById<AppCompatTextView>(R.id.Header_text)?.text = context.getString(R.string.search_fav_team_text)

            findViewById<TextInputEditText>(R.id.Edit_text)?.afterTextChanged {
                viewModel.clearPnrError()
                viewModel.teamName.value = it.trim()
            }
            findViewById<AppCompatButton>(R.id.search_btn)?.setOnClickListener {
                setFocusabilityOnError(this)
                viewModel.onContinueClick()
            }
        }
    }

    /**
     * this function will be displayed error when UI validation is fail
     * @param textInput id of the textInputLayout
     * @param errorMsg error that has to be shown in input layout
     */
    private fun showFieldError(@IdRes textInput: Int, @StringRes errorMsg: Int?) {
        view?.findViewById<TextInputLayout>(textInput)?.apply {
            if (errorMsg != null) {
                errorIconDrawable = ContextCompat.getDrawable(
                    context,
                    android.R.drawable.screen_background_light_transparent
                )
                error = getString(errorMsg)
                isErrorEnabled = true
            } else {
                isErrorEnabled = false
            }
        }
    }

    private fun setFocusabilityOnError(view: View) {
        if (viewModel.teamName.value.isNullOrEmpty()) {
            view.findViewById<TextInputEditText>(R.id.Edit_text).requestFocus()
        }
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun startGradientAnimation(view: View) {
        view.findViewById<ScrollView>(R.id.homescreen_layout).apply {
            val animationDrawable = view.background as AnimationDrawable
            animationDrawable.setEnterFadeDuration(2000)
            animationDrawable.setExitFadeDuration(7000)
            animationDrawable.start()
        }
    }

    private fun startCloudAnimation(view: View) {
        val cloudAnim =
            AnimatorInflater.loadAnimator(
                this.context,
                R.animator.cloud_anim
            )

        val cloudAnim1 =
            AnimatorInflater.loadAnimator(
                this.context,
                R.animator.cloud_anim_1
            )

        view.findViewById<ImageView>(R.id.cloud).apply {
            cloudAnim?.setTarget(this)
            cloudAnim?.start()
        }

        view.findViewById<ImageView>(R.id.cloud1).apply {
            cloudAnim1?.setTarget(this)
            cloudAnim1?.start()
        }
    }

    override fun onResume() {
        super.onResume()
        hideActionBar()
    }
}