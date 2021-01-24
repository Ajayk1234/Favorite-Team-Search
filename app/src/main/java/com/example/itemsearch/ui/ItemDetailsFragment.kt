package com.example.itemsearch.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import com.example.itemsearch.R
import com.example.itemsearch.databinding.SearchItemDetailsBinding
import com.example.itemsearch.models.Item
import com.example.itemsearch.navigationcomponents.FavSearchResultDestination
import com.example.itemsearch.repository.ItemSearchRepositoryImpl
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class ItemDetailsFragment : BaseFragment() {

    private val viewModel by lazy {
        obtainViewModel {
            ItemSearchHomeScreenViewModel(
                ItemSearchRepositoryImpl()
            )
        }
    }

    private lateinit var binding: SearchItemDetailsBinding

    private var itemPosition: Int = 0
    private lateinit var data: List<Item>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewModel()
        configureUI()
    }

    private fun configureViewModel() {
        configureNavigationListener(viewModel.navigationLiveDataField)
        itemPosition = FavSearchResultDestination.getPosition(requireArguments())
        data = FavSearchResultDestination.getData(requireArguments()) as ArrayList<Item>
    }

    private fun configureUI() {
        val item = data[itemPosition]
        val url = URL(item.media.mediaLink)
        val bottomUp: Animation = AnimationUtils.loadAnimation(
            context,
            R.anim.anim_bottom_up
        )
        binding.detailsLayout.startAnimation(bottomUp)

        lifecycleScope.launch(Dispatchers.IO) {
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            withContext(Dispatchers.Main) {
                binding.imageWidthValue.text = bitmap.width.toString()
                binding.imageHeightValue.text = bitmap.height.toString()
            }
        }

            binding.titleName.text = item.title
                val picasso = Picasso.get()
                picasso?.apply {
                    load(item.media.mediaLink).placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_error).into(binding.image)
                }
        binding.descriptionText.text = Html.fromHtml(item.description)
        binding.authorTitle.text = item.author
    }

    override fun onResume() {
        super.onResume()
        showActionBar()
    }
}