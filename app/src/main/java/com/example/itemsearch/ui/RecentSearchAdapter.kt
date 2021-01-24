package com.example.itemsearch.ui

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.example.itemsearch.R

class RecentSearchAdapter(context: Context?, cursor: Cursor?, private val items: ArrayList<String>,
                          private val adapterInterface: AdapterInterface) :
    CursorAdapter(context, cursor, false) {

    private lateinit var recentSearchTextLayout: View
    private var recentSearchTextView: TextView? = null


    override fun bindView(view: View?, context: Context?, cursor: Cursor) {
        val pos = cursor.position
        recentSearchTextView?.tag = pos
        recentSearchTextView?.text = items[cursor.position]
        recentSearchTextView?.setOnClickListener {
            adapterInterface.recentItemClick(items[it.tag as Int])
        }
    }

    override fun newView(context: Context, cursor: Cursor?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        recentSearchTextLayout= inflater.inflate(R.layout.recent_searches, parent, false)
        recentSearchTextView = recentSearchTextLayout.findViewById(R.id.item)
        return recentSearchTextLayout
    }

    interface AdapterInterface {
        fun recentItemClick(title: String)
    }
}