package com.aklemen.shows

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_shows.*


class ShowsListFragment : Fragment() {

    companion object {

        fun newStartFragment(): ShowsListFragment {
            return ShowsListFragment()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewsAndVariables()
        initListeners()
    }


    private fun initViewsAndVariables() {
        showsRecyclerview.layoutManager = LinearLayoutManager(activity)
        showsRecyclerview.adapter = ShowsAdapter(listOfShows) {
            addShowsDetailFragment()
        }
    }

    private fun addShowsDetailFragment() {
        //TODO Not sure if it's ok to add fragment from a fragment?
        fragmentManager?.beginTransaction()
            ?.add(R.id.showsFragmentContainer, ShowDetailFragment.newStartFragment())
            ?.addToBackStack("ShowDetailFragment")
            ?.commit()
    }

    private fun initListeners() {
        showsImageLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Log out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
                val editor = sharedPreferences.edit()
                editor.clear().apply()
                startActivity(LoginActivity.newStartIntent(requireContext()))
                activity?.finish()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}
