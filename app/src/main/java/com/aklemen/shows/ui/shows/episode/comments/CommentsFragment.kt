package com.aklemen.shows.ui.shows.episode.comments

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.aklemen.shows.R
import com.aklemen.shows.data.model.Comment
import kotlinx.android.synthetic.main.activity_shows_master.*
import kotlinx.android.synthetic.main.fragment_comments.*
import kotlinx.android.synthetic.main.view_comment_item.*

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

    private var commentsAdapter: CommentsAdapter? = null

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

        activity?.showsProgressBarHolder?.visibility = View.VISIBLE

        initListeners()
        initObservers()
        initRecyclerView()
        getCommentsData()
    }

    private fun initListeners() {
        commentsToolbar.setNavigationOnClickListener { fragmentManager?.popBackStack() }
        commentsRefresh.setOnRefreshListener { getCommentsData() }

        commentsTextPost.setOnClickListener {
            if (commentsEdit.text.isNotEmpty()) {
                postComment()
            }
        }
    }

    private fun initObservers() {
        commentsViewModel.commentListLiveData.observe(this, Observer {
            updateCommentsList(it)
            commentsRefresh.isRefreshing = false
            activity?.showsProgressBarHolder?.visibility = View.GONE

        })
    }

    private fun initRecyclerView() {
        commentsGroup.visibility = View.GONE
        commentsRecyclerview.visibility = View.GONE

        commentsRecyclerview.layoutManager = LinearLayoutManager(activity)
        commentsAdapter = CommentsAdapter(emptyList<Comment>().toMutableList())
        commentsRecyclerview.adapter = commentsAdapter

        commentsRecyclerview.addItemDecoration(
            DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        )
    }

    private fun getCommentsData() {
        episodeId?.let { commentsViewModel.getCommentsList(it) }
    }

    private fun updateCommentsList(comments: List<Comment>) {
        commentsAdapter?.setData(comments)

        if (comments.isNotEmpty()) {
            commentsGroup.visibility = View.GONE
            commentsRecyclerview.visibility = View.VISIBLE
        } else {
            commentsRecyclerview.visibility = View.GONE
            commentsGroup.visibility = View.VISIBLE
        }
    }

    private fun postComment() {
        val comment =
            episodeId?.let {
                Comment(
                    text = commentsEdit.text.toString(),
                    episodeId = it
                )
            }

        if (comment != null) {
            commentsViewModel.addNewComment(comment)
            commentsAdapter?.addItem(comment)
            commentsEdit.setText("")
            commentsRecyclerview.scrollToPosition((commentsAdapter?.itemCount ?: 1) - 1)
        }
    }

}
