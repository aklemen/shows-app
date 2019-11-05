package com.aklemen.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_show_detail.*


class ShowDetailFragment : Fragment() {


    companion object {

        fun newStartFragment(): ShowDetailFragment = ShowDetailFragment()

    }

    private lateinit var showsViewModel: ShowsViewModel
    private var showDetailFragmentInterface: ShowDetailFragmentInterface? = null

    private var show: Show? = null
    private var episodesAdapter: EpisodesAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showsViewModel = ViewModelProviders.of(requireActivity()).get(ShowsViewModel::class.java)

        if (context is ShowDetailFragmentInterface) {
            showDetailFragmentInterface = context
        } else {
            throw RuntimeException("Please implement ShowDetailFragmentInterface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        showsViewModel.showLiveData.observe(this, Observer {
            show = it
            initViewsAndVariables()
            refreshEpisodesList()
        })
    }

    private fun initViewsAndVariables() {
        episodesAdapter = show?.listOfEpisodes?.let { EpisodesAdapter(it) }

        detailToolbar.title = show?.name
        detailTextDescription.text = show?.description

        detailRecyclerview.layoutManager = LinearLayoutManager(activity)
        detailRecyclerview.adapter = episodesAdapter
    }

    private fun initListeners() {
        detailToolbar.setNavigationOnClickListener { fragmentManager?.popBackStack() }

        detailFab.setOnClickListener { showDetailFragmentInterface?.onAddEpisodeClick() }

        detailTextAddEpisodes.setOnClickListener { showDetailFragmentInterface?.onAddEpisodeClick() }
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

interface ShowDetailFragmentInterface {
    fun onAddEpisodeClick()
}