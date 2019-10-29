package com.aklemen.shows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_detail.*

class ShowDetailActivity : AppCompatActivity() {


    companion object {

        private const val EXTRA_SHOW_INDEX = "ShowDetailActivity.showList"
        private const val ACTIVITY_REQUEST_ADD_EPISODE = 111

        fun newStartIntent(context: Context, index: Int): Intent {
            val intent = Intent(context, ShowDetailActivity::class.java)
            intent.putExtra(EXTRA_SHOW_INDEX, index)
            return intent
        }

    }

    private var show: Show? = null
    private var episodesAdapter: EpisodesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        initViewsAndVariables()
        initListeners()
        refreshEpisodesList()
    }

    private fun initViewsAndVariables() {
        show = ShowsActivity.listOfShows[intent.getIntExtra(EXTRA_SHOW_INDEX, 0)]
        episodesAdapter = show?.listOfEpisodes?.let { EpisodesAdapter(it) }

        detailToolbar.title = show?.name
        detailTextDescription.text = show?.description

        detailRecyclerview.layoutManager = LinearLayoutManager(this)
        detailRecyclerview.adapter = episodesAdapter
    }

    private fun initListeners() {
        detailToolbar.setNavigationOnClickListener { onBackPressed() }

        detailFab.setOnClickListener { startActivityForResult(AddEpisodeActivity.newStartIntent(this), ACTIVITY_REQUEST_ADD_EPISODE) }
        detailTextAddEpisodes.setOnClickListener { startActivityForResult(AddEpisodeActivity.newStartIntent(this), ACTIVITY_REQUEST_ADD_EPISODE) }
    }

    // Receiving the activity result from AddEpisodeActivity

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_REQUEST_ADD_EPISODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (show?.listOfEpisodes?.add(
                            Episode(
                                data.getStringExtra(AddEpisodeActivity.EXTRA_ADD_TITLE),
                                data.getStringExtra(AddEpisodeActivity.EXTRA_ADD_DESCRIPTION)
                            )
                        ) == true
                    ) {
                        show?.listOfEpisodes?.size?.let { episodesAdapter?.notifyItemInserted(it) }
                        Toast.makeText(this, "Episode successfully added.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Adding the episode failed!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        refreshEpisodesList()
    }

    private fun refreshEpisodesList() {
        if (show?.listOfEpisodes?.isNotEmpty() == true) {
            detailGroup.visibility = View.GONE
            detailRecyclerview.visibility = View.VISIBLE
        } else {
            detailRecyclerview.visibility = View.GONE
            detailGroup.visibility = View.VISIBLE
        }
    }
}
