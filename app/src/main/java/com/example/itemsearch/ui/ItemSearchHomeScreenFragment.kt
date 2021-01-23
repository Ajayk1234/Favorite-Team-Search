package com.example.itemsearch.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.itemsearch.R
import com.example.itemsearch.databinding.MainSearchFragmentBinding
import com.example.itemsearch.models.Item
import com.example.itemsearch.models.SearchResponse
import com.example.itemsearch.navigationcomponents.FavSearchResultDestination
import com.example.itemsearch.repository.ItemSearchRepositoryImpl
import com.google.android.material.textfield.TextInputLayout

class ItemSearchHomeScreenFragment : BaseFragment(), CellClickListener {

    private lateinit var binding: MainSearchFragmentBinding
    private lateinit var responseData: SearchResponse

    private val viewModel by lazy {
        obtainViewModel {
            ItemSearchHomeScreenViewModel(
                ItemSearchRepositoryImpl()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainSearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewModel()
        configureUI(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val mSearchMenuItem = menu.findItem(R.id.search)
        val searchManager =
            activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = mSearchMenuItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        fromView(searchView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search ->
                return false
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fromView(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                binding.content.layoutManager = GridLayoutManager(context, 2)
                viewModel.teamName.value = query?.trim()
                viewModel.onContinueClick()
                viewModel.searchLiveData.observe(viewLifecycleOwner, {
                    responseData = it
                    Log.d("data", "Test links ${it?.link}")
                    it.items?.let { response ->
                        binding.content.adapter = ImageListAdapter(response, this@ItemSearchHomeScreenFragment)
                        binding.HeaderText.visibility = GONE
                    }
                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun configureViewModel() {
        configureNavigationListener(viewModel.navigationLiveDataField)
        viewModel.showProgress.observe(viewLifecycleOwner, Observer { showOrHideSpinner(it) })
        viewModel.errorMsg.observe(
            viewLifecycleOwner,
            Observer {
            }
        )
        viewModel.dialog.observe(viewLifecycleOwner, Observer {
            showMessageDialogImpl(it, actionBlock = { viewModel.clearDialogError() })
        })
    }


    private fun configureUI(view: View) {
        view.apply {
            binding.HeaderText.text = context.getString(R.string.search_item_text)
        }
    }

    override fun onResume() {
        super.onResume()
        showActionBar()
    }

    override fun onCellClickListener(position: Int) {
        viewModel.navigateTo(FavSearchResultDestination(position, responseData.items as ArrayList<Item>))
    }
}