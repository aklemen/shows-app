package com.aklemen.shows

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_detail.*
import kotlinx.android.synthetic.main.activity_shows.*

class ShowDetailActivity : AppCompatActivity() {

    companion object{

        private const val EXTRA_SHOW_OBJECT = "ShowDetailActivity.showList"

        fun newStartIntent(context: Context, show : Show): Intent {
            val intent = Intent(context, ShowDetailActivity::class.java)
            intent.putExtra(EXTRA_SHOW_OBJECT, show)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        detail_toolbar.setNavigationOnClickListener { onBackPressed() }

        val currentShow = intent.getParcelableExtra<Show>(EXTRA_SHOW_OBJECT)

        detail_toolbar.title = currentShow.name
        detail_text_desciption.text = currentShow.description

        detail_recyclerview.layoutManager = LinearLayoutManager(this)
        detail_recyclerview.adapter = EpisodesAdapter(currentShow.listOfEpisodes)

    }
}
