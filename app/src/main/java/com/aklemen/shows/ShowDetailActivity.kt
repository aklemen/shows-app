package com.aklemen.shows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_detail.*

class ShowDetailActivity : AppCompatActivity() {


    companion object {

        private const val EXTRA_SHOW_OBJECT = "ShowDetailActivity.showList"

        private const val REQUEST_ADD_EPISODE = 111

        fun newStartIntent(context: Context, show: Show): Intent {
            val intent = Intent(context, ShowDetailActivity::class.java)
            intent.putExtra(EXTRA_SHOW_OBJECT, show)
            return intent
        }

    }


    var currentShow: Show? = null

    var episodesAdapter: EpisodesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        detail_toolbar.setNavigationOnClickListener { onBackPressed() }

        currentShow = intent.getParcelableExtra(EXTRA_SHOW_OBJECT)
        Log.d("currentShow", currentShow.toString())

        episodesAdapter = currentShow?.let {
            EpisodesAdapter(it.listOfEpisodes)
        }

        detail_toolbar.title = currentShow?.name
        detail_text_desciption.text = currentShow?.description

        detail_recyclerview.layoutManager = LinearLayoutManager(this)
        detail_recyclerview.adapter = episodesAdapter


        detail_fab.setOnClickListener {
            startActivityForResult(AddEpisodeActivity.newStartIntent(this), REQUEST_ADD_EPISODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_ADD_EPISODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (currentShow?.listOfEpisodes?.add(
                            Episode(
                                data.getStringExtra(AddEpisodeActivity.EXTRA_ADD_TITLE),
                                data.getStringExtra(AddEpisodeActivity.EXTRA_ADD_DESCRIPTION)
                            )
                        ) == true
                    ) {
                        episodesAdapter?.notifyItemInserted(currentShow?.listOfEpisodes?.size ?: 0)
                        Toast.makeText(this, "Episode successfully added.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Adding the episode failed!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}
