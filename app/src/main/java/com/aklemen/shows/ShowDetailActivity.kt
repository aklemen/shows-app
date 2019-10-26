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

        // Constants and function to start the new activity

        private const val EXTRA_SHOW_INDEX = "ShowDetailActivity.showList"

        private const val REQUEST_ADD_EPISODE = 111

        fun newStartIntent(context: Context, index : Int): Intent {
            val intent = Intent(context, ShowDetailActivity::class.java)
            intent.putExtra(EXTRA_SHOW_INDEX, index)
            return intent
        }

    }

    var show : Show? = null

    var episodesAdapter: EpisodesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)


        // Back button toolbar navigation

        detailToolbar.setNavigationOnClickListener { onBackPressed() }


        // Defining vars, setting views

        val currentShowIndex = intent.getIntExtra(EXTRA_SHOW_INDEX, 0)
        show  = ShowsActivity.listOfShows[currentShowIndex]

        episodesAdapter = show?.listOfEpisodes?.let { EpisodesAdapter(it) }

        detailToolbar.title = show?.name
        detailTextDescription.text = show?.description


        // Handling the layout of the screen, depending on the list of episodes

        if (show?.listOfEpisodes?.isNotEmpty() == true){
            detailGroup.visibility = View.GONE
            detailRecyclerview.visibility = View.VISIBLE
            detailRecyclerview.layoutManager = LinearLayoutManager(this)
            detailRecyclerview.adapter = episodesAdapter
        }
        else{
            detailRecyclerview.visibility = View.GONE
            detailGroup.visibility = View.VISIBLE
        }


        // RecyclerView and some listeners to add episode

        detailRecyclerview.layoutManager = LinearLayoutManager(this)
        detailRecyclerview.adapter = episodesAdapter


        detailFab.setOnClickListener {
            startActivityForResult(AddEpisodeActivity.newStartIntent(this), REQUEST_ADD_EPISODE)
        }


        detailTextAddEpisodes.setOnClickListener {
            startActivityForResult(AddEpisodeActivity.newStartIntent(this), REQUEST_ADD_EPISODE)
        }

    }


    // Receiving the activity result from AddEpisodeActivity

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_ADD_EPISODE) {
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
    }


    // Refreshing the layout onStart

    override fun onStart() {
        super.onStart()
        if (show?.listOfEpisodes?.isNotEmpty() == true){
            detailGroup.visibility = View.GONE
            detailRecyclerview.visibility = View.VISIBLE
            detailRecyclerview.layoutManager = LinearLayoutManager(this)
            detailRecyclerview.adapter = episodesAdapter
        }
        else{
            detailRecyclerview.visibility = View.GONE
            detailGroup.visibility = View.VISIBLE
        }
    }
}
