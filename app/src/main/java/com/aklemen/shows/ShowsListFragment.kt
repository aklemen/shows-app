package com.aklemen.shows

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_shows.*


class ShowsListFragment : Fragment() {

    companion object {

        fun newStartFragment(): ShowsListFragment = ShowsListFragment()
    }

    private lateinit var showsViewModel: ShowsViewModel
    private var showsListInterface: ShowsListInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showsViewModel = ViewModelProviders.of(requireActivity()).get(ShowsViewModel::class.java)

        if (context is ShowsListInterface) {
            showsListInterface = context
        } else {
            throw RuntimeException("Please implement ShowsListInterface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showsViewModel.showListLiveData.observe(this, Observer {
            initRecyclerView(it)
        })

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        showsViewModel.getShowsList(sharedPrefs.getString(LoginActivity.PREF_TOKEN, "") ?: "")

        initListeners()
    }


    private fun initRecyclerView(shows: List<Show>) {
        showsRecyclerview.layoutManager = LinearLayoutManager(activity)
        showsRecyclerview.adapter = ShowsAdapter(shows.toMutableList()) {
            showsListInterface?.onShowClicked(it.id)
        }
    }

    private fun initListeners() {
        showsImageLogout.setOnClickListener {
            showsListInterface?.logout()
        }
    }
}

interface ShowsListInterface {
    fun onShowClicked(showId: String)
    fun logout()
}
