package com.aklemen.shows

import android.content.Context
import android.os.Bundle
import android.util.Log
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

        private const val ACTIVITY_REQUEST_ADD_EPISODE = 111

        fun newStartFragment(): ShowDetailFragment {
            return ShowDetailFragment()
        }

    }

    private var show: Show? = null
    private var episodesAdapter: EpisodesAdapter? = null

    lateinit var showsViewModel : ShowsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showsViewModel = ViewModelProviders.of(requireActivity()).get(ShowsViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_show_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        refreshEpisodesList()

        showsViewModel.indexLiveData.observe(this, Observer {
            initViewsAndVariables(it)
        })

    }

    private fun initViewsAndVariables(showIndex : Int) {
        show = ShowsMasterActivity.listOfShows[showIndex]

        episodesAdapter = show?.listOfEpisodes?.let { EpisodesAdapter(it) }

        detailToolbar.title = show?.name
        detailTextDescription.text = show?.description

        detailRecyclerview.layoutManager = LinearLayoutManager(activity)
        detailRecyclerview.adapter = episodesAdapter
    }

    private fun initListeners() {
        detailToolbar.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }

        detailFab.setOnClickListener {
            addEpisodeFragment()
        }
        detailTextAddEpisodes.setOnClickListener {
            addEpisodeFragment()
        }


    }

    private fun addEpisodeFragment() {
        //TODO Not sure if it's ok to add fragment from a fragment? Create interface?
        fragmentManager?.beginTransaction()
            ?.replace(R.id.showsFragmentContainer, AddEpisodeFragment.newStartFragment())
            ?.addToBackStack("AddEpisodeFragment")
            ?.commit()
    }

    // Receiving the activity result from AddEpisodeFragment
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_REQUEST_ADD_EPISODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (show?.listOfEpisodes?.add(
                            Episode(
                                data.getStringExtra(AddEpisodeFragment.EXTRA_ADD_TITLE),
                                data.getStringExtra(AddEpisodeFragment.EXTRA_ADD_DESCRIPTION)
                            )
                        ) == true
                    ) {
                        show?.listOfEpisodes?.size?.let { episodesAdapter?.notifyItemInserted(it) }
                        Toast.makeText(this, "Episode successfully added.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Adding the episode failed!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        refreshEpisodesList()
    }
*/
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
