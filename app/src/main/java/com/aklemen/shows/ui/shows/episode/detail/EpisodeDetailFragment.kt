package com.aklemen.shows.ui.shows.episode.detail

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aklemen.shows.R
import kotlinx.android.synthetic.main.episode_detail_fragment.*

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

    private var episodeId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        episodeDetailViewModel = ViewModelProviders.of(this).get(EpisodeDetailViewModel::class.java)

        episodeId = arguments?.getString(EXTRA_EPISODE_ID, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.episode_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        test.text = episodeId
    }
}
