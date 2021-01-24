package com.example.itemsearch.ui

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.database.MatrixCursor
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.itemsearch.R
import com.example.itemsearch.databinding.MainSearchFragmentBinding
import com.example.itemsearch.models.Item
import com.example.itemsearch.models.SearchResponse
import com.example.itemsearch.navigationcomponents.FavSearchResultDestination
import com.example.itemsearch.repository.ItemSearchRepositoryImpl

class ItemSearchHomeScreenFragment : BaseFragment(), ImagelClickListener,
    RecentSearchAdapter.AdapterInterface {

    private lateinit var binding: MainSearchFragmentBinding
    private lateinit var searchView: SearchView
    private lateinit var responseData: SearchResponse
    var recentSearchList: ArrayList<String> = arrayListOf()

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "save"
    lateinit var sharedPref: SharedPreferences

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
        sharedPref = binding.content.context.getSharedPreferences(
            PREF_NAME,
            PRIVATE_MODE
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        configureViewModel()
        configureUI()
    }

    private fun configureViewModel() {
        configureNavigationListener(viewModel.navigationLiveDataField)
        viewModel.showProgress.observe(viewLifecycleOwner, { showOrHideSpinner(it) })
        viewModel.errorMsg.observe(
            viewLifecycleOwner, {
                showMessageDialogImpl(it, actionBlock = { viewModel.clearErrorMsg() })
            }
        )
        viewModel.dialog.observe(viewLifecycleOwner, {
            showMessageDialogImpl(it, actionBlock = { viewModel.clearDialogError() })
        })
    }

    private fun configureUI() {
        val fadeInAnimation: Animation =
            AnimationUtils.loadAnimation(context, R.anim.anim_fade_in)
        binding.HeaderText.startAnimation(fadeInAnimation)
        binding.HeaderText.text = context?.getString(R.string.search_item_text)
        bindRecyclerView("")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val mSearchMenuItem = menu.findItem(R.id.search)
        val searchManager =
            activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = mSearchMenuItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        fromView()
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

    private fun fromView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(input: String?): Boolean {
                searchView.clearFocus()
                input?.let {
                    bindRecyclerView(it)
                    recentSearchList.add(it)
                }
                val editor = sharedPref.edit()
                sharedPref.all.size
                for (i in 0 until recentSearchList.size) {
                    editor.putString(PREF_NAME + i, recentSearchList[i])
                    editor.apply()
                }
                if (recentSearchList.size == 5) {
                    recentSearchList.clear()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadHistory(searchView)
                return true
            }
        })
    }

    fun loadHistory(searchView: SearchView) {
        val savedSearchList: ArrayList<String> = arrayListOf()
        for (i in 4 downTo 0) {
            sharedPref.getString(PREF_NAME + i, null)?.let {
                savedSearchList.add(it)
            }
        }
        // Cursor
        val columns = arrayOf("_id", "text")
        val temp = arrayOf<Any>(0, "default")
        val cursor = MatrixCursor(columns)
        for (i in 0 until savedSearchList.size) {
            temp[0] = i
            temp[1] = savedSearchList[i]
            cursor.addRow(temp)
        }
        searchView.suggestionsAdapter = RecentSearchAdapter(
            context, cursor, savedSearchList, this
        )
    }

    override fun onImageClickListener(position: Int) {
        viewModel.navigateTo(
            FavSearchResultDestination(
                position,
                responseData.items as ArrayList<Item>
            )
        )
    }

    fun bindRecyclerView(userInput: String) {
        if (userInput.isNotEmpty()) {
            viewModel.onContinueClick(userInput.trim())
        }

        binding.content.layoutManager = GridLayoutManager(context, 2)
        viewModel.searchLiveData.observe(viewLifecycleOwner, {
            responseData = it
            it.items?.let { response ->
                binding.content.adapter = ImageListAdapter(
                    response,
                    this@ItemSearchHomeScreenFragment
                )
                binding.HeaderText.visibility = GONE
            }
        })
        hideKeyboard()
    }

    override fun recentItemClick(title: String) {
        binding.content.layoutManager = GridLayoutManager(context, 2)
        bindRecyclerView(title)
        searchView.clearFocus()
    }

    override fun onResume() {
        super.onResume()
        showActionBar()
    }
}