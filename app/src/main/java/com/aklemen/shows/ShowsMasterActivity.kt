package com.aklemen.shows

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ShowsMasterActivity : AppCompatActivity() {

    companion object{

        fun newStartIntent(context: Context): Intent {
            return Intent(context, ShowsMasterActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_master)

        addShowsListFragment()
    }

    private fun addShowsListFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.showsFragmentContainer, ShowsListFragment.newStartFragment())
            .addToBackStack("ShowsListFragment")
            .commit()
    }
}
