package com.aklemen.shows.ui.shows.detail

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
import com.aklemen.shows.data.model.Episode
import com.aklemen.shows.data.model.Show
import kotlinx.android.synthetic.main.activity_shows_master.*
import kotlinx.android.synthetic.main.fragment_show_detail.*


class ShowDetailFragment : Fragment() {


    companion object {

        private const val EXTRA_SHOW_ID = "ShowDetailFragment.showId"

        fun newStartFragment(showId: String): ShowDetailFragment {
            val args = Bundle()
            args.putString(EXTRA_SHOW_ID, showId)
            val fragment = ShowDetailFragment()
            fragment.arguments = args
            return fragment
        }

    }

    private lateinit var showsDetailViewModel: ShowsDetailViewModel
    private var showDetailFragmentInterface: ShowDetailFragmentInterface? = null

    private var episodesAdapter: EpisodesAdapter? = null

    private var show: Show? = null
    private var showId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showsDetailViewModel = ViewModelProviders.of(this).get(ShowsDetailViewModel::class.java)

        if (context is ShowDetailFragmentInterface) {
            showDetailFragmentInterface = context
        } else {
            throw RuntimeException("Please implement ShowDetailFragmentInterface")
        }

        showId = arguments?.getString(EXTRA_SHOW_ID, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.showsProgressBarHolder?.visibility = View.VISIBLE

        initListeners()
        initObservers()
        initRecyclerView()
        getShowData()
    }

    private fun initListeners() {
        detailToolbar.setNavigationOnClickListener { fragmentManager?.popBackStack() }
        detailFab.setOnClickListener { showId?.let { showDetailFragmentInterface?.onAddEpisodeClick(it) } }
        detailTextAddEpisodes.setOnClickListener { showId?.let { showDetailFragmentInterface?.onAddEpisodeClick(it) } }
    }

    private fun initObservers() {
        showsDetailViewModel.showLiveData.observe(this, Observer {
            show = it
            initViews()
        })

        showsDetailViewModel.episodeListLiveData.observe(this, Observer {
            updateEpisodesList(it)
            activity?.showsProgressBarHolder?.visibility = View.GONE

        })
    }

    private fun initRecyclerView() {
        detailGroup.visibility = View.GONE
        detailRecyclerview.visibility = View.GONE

        detailRecyclerview.layoutManager = LinearLayoutManager(activity)
        episodesAdapter =  EpisodesAdapter(emptyList<Episode>().toMutableList()){
            showDetailFragmentInterface?.onEpisodeClick(it.id)
        }
        detailRecyclerview.adapter = episodesAdapter
    }

    private fun getShowData() {
        showId?.let { showsDetailViewModel.getShow(it) }
        showId?.let { showsDetailViewModel.getEpisodesList(it) }
    }

    private fun initViews() {
        detailToolbar.title = show?.title
        detailTextDescription.text = show?.description
    }

    private fun updateEpisodesList(episodes: List<Episode>) {
        episodesAdapter?.setData(episodes)

        if (episodes.isNotEmpty()) {
            detailGroup.visibility = View.GONE
            detailRecyclerview.visibility = View.VISIBLE
        } else {
            detailRecyclerview.visibility = View.GONE
            detailGroup.visibility = View.VISIBLE
        }
    }
}

interface ShowDetailFragmentInterface {
    fun onAddEpisodeClick(showId: String)
    fun onEpisodeClick(episodeId: String)
}