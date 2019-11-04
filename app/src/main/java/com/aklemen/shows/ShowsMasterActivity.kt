package com.aklemen.shows

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_add_episode.*

class ShowsMasterActivity : AppCompatActivity(), ShowsListInterface, ShowDetailFragmentInterface, AddEpisodeFragmentInterface {

    companion object {

        fun newStartIntent(context: Context): Intent {
            return Intent(context, ShowsMasterActivity::class.java)
        }

        var listOfShows = mutableListOf(
            Show(
                "0",
                "The Big Bang Theory",
                "2007-2019", """A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows
                    | them how little they know about life outside of the laboratory.""".trimMargin(),
                mutableListOf(),
                R.drawable.bigbang
            ),
            Show(
                "1",
                "The Office",
                "2005-2013",
                """A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium.""",
                mutableListOf(),
                R.drawable.office
            ),
            Show(
                "2",
                "House M.D.",
                "2004-2012",
                """An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases
                    | that come his way using his crack team of doctors and his wits.""".trimMargin(),
                mutableListOf(),
                R.drawable.house
            ),
            Show(
                "3",
                "Jane the Virgin",
                "2014 - ",
                """A young, devout Catholic woman discovers that she was accidentally artificially inseminated.""",
                mutableListOf(),
                R.drawable.jane
            ),
            Show(
                "4",
                "Sherlock",
                "2010 - ",
                """A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.""",
                mutableListOf(),
                R.drawable.sherlock
            ),
            Show(
                "5",
                "The Big Bang Theory",
                "2007-2019", """A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows
                    | them how little they know about life outside of the laboratory.""".trimMargin(),
                mutableListOf(),
                R.drawable.bigbang
            ),
            Show(
                "6",
                "The Office",
                "2005-2013",
                """A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium.""",
                mutableListOf(),
                R.drawable.office
            ),
            Show(
                "7",
                "House M.D.",
                "2004-2012",
                """An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases
                    | that come his way using his crack team of doctors and his wits.""".trimMargin(),
                mutableListOf(),
                R.drawable.house
            ),
            Show(
                "8",
                "Jane the Virgin",
                "2014 - ",
                """A young, devout Catholic woman discovers that she was accidentally artificially inseminated.""",
                mutableListOf(),
                R.drawable.jane
            ),
            Show(
                "9",
                "Sherlock",
                "2010 - ",
                """A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.""",
                mutableListOf(),
                R.drawable.sherlock
            )
        )
    }

    private lateinit var showsViewModel: ShowsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_master)

        showsViewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)

        addShowsListFragment()
    }

    private fun addShowsListFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.showsFragmentContainer, ShowsListFragment.newStartFragment())
            .commit()
    }

    override fun onShowClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.showsFragmentContainer, ShowDetailFragment.newStartFragment())
            .addToBackStack("ShowDetailFragment")
            .commit()
    }

    override fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Log out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPreferences.edit()
                editor.clear().apply()
                startActivity(LoginActivity.newStartIntent(this))
                finish()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onAddEpisodeClick() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.showsFragmentContainer, AddEpisodeFragment.newStartFragment())
            .addToBackStack("AddEpisodeFragment")
            .commit()
    }

    override fun onSaveEpisodeClick(title : String, description :  String) {
        if (showsViewModel.currentShowLiveData.value?.listOfEpisodes?.add(
                Episode(
                    title,
                    description
                )
            ) == true
        ) {
            Toast.makeText(this, "Episode successfully added.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Adding the episode failed!", Toast.LENGTH_SHORT).show()
        }
    }
}
