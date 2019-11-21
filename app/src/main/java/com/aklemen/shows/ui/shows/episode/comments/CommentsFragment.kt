package com.aklemen.shows.ui.shows.episode.comments

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aklemen.shows.R
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentsFragment : Fragment() {

    companion object {
        private const val EXTRA_EPISODE_ID = "CommentsFragment.episodeId"

        fun newStartFragment(episodeId: String): CommentsFragment {
            val args = Bundle()
            args.putString(EXTRA_EPISODE_ID, episodeId)
            val fragment = CommentsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var commentsViewModel: CommentsViewModel

    private var episodeId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        commentsViewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)

        episodeId = arguments?.getString(EXTRA_EPISODE_ID, "") ?: ""

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commentsTextPost.setOnClickListener {
            commentsEdit.setText("")
        }
    }

}
