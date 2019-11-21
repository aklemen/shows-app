package com.aklemen.shows.ui.shows.episode.detail

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.aklemen.shows.R
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.model.Episode
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_episode_detail.*

class EpisodeDetailFragment : Fragment() {

    companion object {
        private const val EXTRA_EPISODE_ID = "EpisodeDetailFragment.episodeId"

        fun newStartFragment(episodeId: String): EpisodeDetailFragment {
            val args = Bundle()
            args.putString(EXTRA_EPISODE_ID, episodeId)
            val fragment = EpisodeDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var episodeDetailViewModel: EpisodeDetailViewModel
    private var episodeDetailInterface: EpisodeDetailInterface? = null

    private var episodeId: String? = null
    private var episode: Episode? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        episodeDetailViewModel = ViewModelProviders.of(this).get(EpisodeDetailViewModel::class.java)

        if (context is EpisodeDetailInterface) {
            episodeDetailInterface = context
        } else {
            throw RuntimeException("Please implement EpisodeDetailInterface")
        }

        episodeId = arguments?.getString(EXTRA_EPISODE_ID, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_episode_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
        episodeId?.let { episodeDetailViewModel?.getEpisode(it) }
    }

    private fun initListeners() {
        episodeDetailToolbar.setNavigationOnClickListener { fragmentManager?.popBackStack() }
        episodeDetailImageComment.setOnClickListener { episodeId?.let { episodeDetailInterface?.onCommentsClick(it) } }
        episodeDetailTextComments.setOnClickListener { episodeId?.let { episodeDetailInterface?.onCommentsClick(it) } }
    }

    private fun initObservers() {
        episodeDetailViewModel.episodeLiveData.observe(this, Observer {
            episode = it
            initViews()
        })
    }

    private fun initViews() {
        Picasso.get().load(RestClient.BASE_URL + episode?.imageUrl).into(episodeDetailImage)
        episodeDetailTextTitle.text = episode?.title
        episodeDetailNumber.text = "S${episode?.episodeNumber} E${episode?.season}"
        episodeDetailTextDescription.text = episode?.description
    }
}

interface EpisodeDetailInterface{
    fun onCommentsClick(episodeId: String)
}
