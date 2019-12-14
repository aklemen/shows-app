package com.aklemen.shows.ui.shows.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.aklemen.shows.R
import com.aklemen.shows.data.model.Show
import kotlinx.android.synthetic.main.fragment_shows_list.*


class ShowsListFragment : Fragment() {

    companion object {

        fun newStartFragment(): ShowsListFragment = ShowsListFragment()
    }

    private lateinit var showsListViewModel: ShowsListViewModel
    private var showsListInterface: ShowsListInterface? = null

    private var showsAdapter: ShowsAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showsListViewModel = ViewModelProviders.of(this).get(ShowsListViewModel::class.java)

        if (context is ShowsListInterface) {
            showsListInterface = context
        } else {
            throw RuntimeException("Please implement ShowsListInterface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shows_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
        initRecyclerView()
    }

    private fun initListeners() {
        showsImageLogout.setOnClickListener {
            showsListInterface?.logout()
        }
    }

    private fun initRecyclerView() {
        showsGroup.visibility = View.GONE
        showsRecyclerview.visibility = View.GONE

        showsRecyclerview.layoutManager = LinearLayoutManager(activity)
        showsAdapter =  ShowsAdapter(emptyList<Show>().toMutableList()) {
            showsListInterface?.onShowClicked(it.id)
        }
        showsRecyclerview.adapter = showsAdapter
        showsListViewModel.getShowsList()
    }

    private fun initObservers() {
        showsListViewModel.showListLiveData.observe(this, Observer {
            updateShowList(it)
        })
    }

    private fun updateShowList(shows: List<Show>) {
        showsAdapter?.setData(shows)

        if (shows.isNotEmpty()) {
            showsGroup.visibility = View.GONE
            showsRecyclerview.visibility = View.VISIBLE
        } else {
            showsRecyclerview.visibility = View.GONE
            showsGroup.visibility = View.VISIBLE
        }
    }
}

interface ShowsListInterface {
    fun onShowClicked(showId: String)
    fun logout()
}
