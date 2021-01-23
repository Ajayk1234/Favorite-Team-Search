package com.example.itemsearch.customspinner

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.itemsearch.R
import java.util.*

private const val CONFIG = "PROGRESS_DIALOG_CONFIG"

class CustomSpinnerFragment : DialogFragment() {

    private lateinit var config: CustomSpinnerConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readSpinnerConfig()
        configWindowStyle()
        configSpinner()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.spinner, container, false).apply {
            configProgressBar(this)
            configHintMessage(this)
        }

    private fun configWindowStyle() {
        setStyle(STYLE_NO_FRAME, config.getWindowStyle())
    }

    private fun readSpinnerConfig() {
        config = (arguments?.getParcelable(CONFIG) as? CustomSpinnerConfig) ?: DEFAULT_CONFIG
    }

    private fun configSpinner() {
        isCancelable = config.isCancellable
    }

    private fun configProgressBar(parentView: View) {
        val progressBar = parentView.findViewById(R.id.spinner_progressBar) as ProgressBar
        parentView.isVisible = true
        progressBar.indeterminateDrawable.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.blue
            ),
            PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun configHintMessage(parentView: View) {
        parentView.findViewById<TextView>(R.id.spinner_hintText)
            .setText(config.hintMessageId)
    }

    companion object {
        fun newInstance(config: CustomSpinnerConfig) =
            CustomSpinnerFragment().apply {
                arguments =
                    newBundle(
                        config
                    )
            }

        fun newBundle(config: CustomSpinnerConfig) =
            Bundle().apply {
                putParcelable(CONFIG, config)
            }

        const val UNIQUE_TAG = "Unique CustomProgress"

        private val DEFAULT_CONFIG =
            CustomSpinnerConfig(
                isCancellable = false,
                isTransparent = false,
                spinnerTag = UUID.randomUUID().toString(),
                cancelContentDescription = ""
            )
    }
}
