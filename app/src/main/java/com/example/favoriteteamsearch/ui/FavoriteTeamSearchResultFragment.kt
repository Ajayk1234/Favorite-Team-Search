package com.example.favoriteteamsearch.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Observer
import com.example.favoriteteamsearch.R
import com.example.favoriteteamsearch.navigationcomponents.FavSearchResultDestination
import com.squareup.picasso.Picasso

class FavoriteTeamSearchResultFragment : BaseFragment() {

    override val layoutResourceId: Int = R.layout.favorite_team_search_result

    private val viewModel by lazy {
        obtainViewModel {
            FavoriteTeamSearchResultViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViewModel()
        configureUI(view)
    }

    private fun configureViewModel() {
        configureNavigationListener(viewModel.navigationLiveDataField)
        viewModel.buildTeamDetails(FavSearchResultDestination.getData(requireArguments()))
    }

    private fun configureUI(view: View) {
        view.apply {
            viewModel.details?.observe(viewLifecycleOwner, Observer {

                if (!it[0].strStadium.isNullOrEmpty()) {
                    findViewById<LinearLayout>(R.id.stadium_container).visibility = View.VISIBLE
                }
                if (!it[0].strTeam.isNullOrEmpty()) {
                    findViewById<LinearLayout>(R.id.team_container).visibility = View.VISIBLE
                }
                if (!it[0].intStadiumCapacity.toString().isNullOrEmpty()) {
                    findViewById<LinearLayout>(R.id.stadium_capcity_container).visibility =
                        View.VISIBLE
                }
                if (!it[0].intFormedYear.toString().isNullOrEmpty()) {
                    findViewById<LinearLayout>(R.id.team_formed_container).visibility = View.VISIBLE
                }
                if (!it[0].strLeague.isNullOrEmpty()) {
                    findViewById<LinearLayout>(R.id.team_league_container).visibility = View.VISIBLE
                }
                if (!it[0].strCountry.isNullOrEmpty()) {
                    findViewById<LinearLayout>(R.id.team_country_container).visibility =
                        View.VISIBLE
                }
                if (!it[0].strDescriptionEN.isNullOrEmpty()) {
                    findViewById<AppCompatTextView>(R.id.team_description_text).visibility =
                        View.VISIBLE
                    findViewById<AppCompatTextView>(R.id.description_text).visibility = View.VISIBLE
                }
                if (!it[0].strTeamBadge.isNullOrEmpty()) {
                    findViewById<AppCompatImageView>(R.id.team_logo_img).visibility =
                        View.VISIBLE
                }
                if (!it[0].strStadiumThumb.isNullOrEmpty()) {
                    findViewById<AppCompatImageView>(R.id.team_stadium_img).visibility =
                        View.VISIBLE
                }

                findViewById<AppCompatTextView>(R.id.team_name_text).text = it[0].strTeam
                findViewById<AppCompatTextView>(R.id.stadium_capcity_text).text =
                    it[0].intStadiumCapacity.toString()
                findViewById<AppCompatTextView>(R.id.team_formed_text).text =
                    it[0].intFormedYear.toString()
                findViewById<AppCompatTextView>(R.id.team_league_text).text = it[0].strLeague
                findViewById<AppCompatTextView>(R.id.team_stadium_text).text = it[0].strStadium
                findViewById<AppCompatTextView>(R.id.team_description_text).text =
                    it[0].strDescriptionEN
                findViewById<AppCompatTextView>(R.id.team_country_text).text = it[0].strCountry

                val stadium: AppCompatImageView = findViewById(R.id.team_stadium_img)
                val teamLogo: AppCompatImageView = findViewById(R.id.team_logo_img)
                val picasso = Picasso.get()
                picasso?.apply {
                    load(it[0].strTeamBadge).placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground).into(teamLogo)
                    load(it[0].strStadiumThumb).placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground).into(stadium)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        showActionBar()
    }

}