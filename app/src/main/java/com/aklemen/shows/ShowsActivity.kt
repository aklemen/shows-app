package com.aklemen.shows

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_shows.*


class ShowsActivity : AppCompatActivity() {

    companion object{
        fun newStartIntent(context: Context): Intent {
            val intent = Intent(context, ShowsActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        var listOfShows = mutableListOf(
            Show("0",
                "The Big Bang Theory",
                "2007-2019", """A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows
                    | them how little they know about life outside of the laboratory.""".trimMargin(),
                mutableListOf(),
                R.drawable.bigbang),
            Show("1",
                "The Office",
                "2005-2013",
                """A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium.""",
                mutableListOf(),
                R.drawable.office),
            Show("2",
                "House M.D.",
                "2004-2012",
                """An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases
                    | that come his way using his crack team of doctors and his wits.""".trimMargin(),
                mutableListOf(),
                R.drawable.house),
            Show("3",
                "Jane the Virgin",
                "2014 - ",
                """A young, devout Catholic woman discovers that she was accidentally artificially inseminated.""",
                mutableListOf(),
                R.drawable.jane),
            Show("4",
                "Sherlock",
                "2010 - ",
                """A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.""",
                mutableListOf(),
                R.drawable.sherlock),
            Show("5",
                "The Big Bang Theory",
                "2007-2019", """A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows
                    | them how little they know about life outside of the laboratory.""".trimMargin(),
                mutableListOf(),
                R.drawable.bigbang),
            Show("6",
                "The Office",
                "2005-2013",
                """A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium.""",
                mutableListOf(),
                R.drawable.office),
            Show("7",
                "House M.D.",
                "2004-2012",
                """An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases
                    | that come his way using his crack team of doctors and his wits.""".trimMargin(),
                mutableListOf(),
                R.drawable.house),
            Show("8",
                "Jane the Virgin",
                "2014 - ",
                """A young, devout Catholic woman discovers that she was accidentally artificially inseminated.""",
                mutableListOf(),
                R.drawable.jane),
            Show("9",
                "Sherlock",
                "2010 - ",
                """A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.""",
                mutableListOf(),
                R.drawable.sherlock)
        )


        shows_recyclerview.layoutManager = LinearLayoutManager(this)
        shows_recyclerview.adapter = ShowsAdapter(listOfShows) {
            Toast.makeText(this, it.name,Toast.LENGTH_LONG).show()
            it.listOfEpisodes.add(Episode("Sher", "Lorem ipsum"))
            startActivity(ShowDetailActivity.newStartIntent(this, it))
        }

        shows_recyclerview.addItemDecoration(MarginItemDecoration(25))

    }
}
