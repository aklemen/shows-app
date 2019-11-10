package com.aklemen.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_add_episode.*
import java.text.DecimalFormat


class AddEpisodeFragment : Fragment() {

    companion object {

        fun newStartFragment(): AddEpisodeFragment = AddEpisodeFragment()
    }

    private lateinit var showsViewModel: ShowsViewModel
    private var addEpisodeFragmentInterface: AddEpisodeFragmentInterface? = null

    private var episodesAdapter: EpisodesAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showsViewModel = ViewModelProviders.of(requireActivity()).get(ShowsViewModel::class.java)

        if (context is AddEpisodeFragmentInterface) {
            addEpisodeFragmentInterface = context
        } else {
            throw RuntimeException("Please implement ShowDetailFragmentInterface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showsViewModel.showLiveData.observe(this, Observer {
            initListeners(it)
            initVariables(it)
        })

        showsViewModel.imageLiveData.observe(this, Observer {
            if (it != null) {
                addGroupEpisodePlaceholder.visibility = View.GONE
                addGroupEpisodeImage.visibility = View.VISIBLE
                addImageEpisode.setImageURI(it)
            } else {
                addGroupEpisodePlaceholder.visibility = View.VISIBLE
                addGroupEpisodeImage.visibility = View.GONE
            }
        })

        showsViewModel.episodeNumberLiveData.value = EpisodeNumber(0,1)

        showsViewModel.episodeNumberLiveData.observe(this, Observer {
            if (it != null) {
                addTextEpisodeNumber.text = addEpisodeFragmentInterface?.formatEpisodeWithComma(it.season, it.episode)
            }
        })
    }

    private fun initVariables(currentShow: Show) {
        //TODO
//        episodesAdapter = EpisodesAdapter(currentShow.listOfEpisodes)
    }

    private fun initListeners(currentShow: Show) {
        addToolbar.setNavigationOnClickListener {
            addEpisodeFragmentInterface?.onBackNavigation(addEditTitle.text.toString(), addEditDescription.text.toString())
        }

        addButtonSave.setOnClickListener {
            addEpisodeFragmentInterface?.onSaveEpisodeClick(currentShow, addEditTitle.text.toString(), addEditDescription.text.toString())
        }

        addEditTitle.doOnTextChanged { text, _, _, _ -> setSaveButtonState(text.toString(), addEditDescription) }
        addEditDescription.doOnTextChanged { text, _, _, _ -> setSaveButtonState(text.toString(), addEditTitle) }

        addImageCamera.setOnClickListener { addEpisodeFragmentInterface?.onUploadPhotoClick() }
        addTextUploadImage.setOnClickListener { addEpisodeFragmentInterface?.onUploadPhotoClick() }
        addImageEpisode.setOnClickListener { addEpisodeFragmentInterface?.onUploadPhotoClick() }
        addTextChangeImage.setOnClickListener { addEpisodeFragmentInterface?.onUploadPhotoClick() }

        addTextEpisodeNumber.setOnClickListener { addEpisodeFragmentInterface?.onChooseEpisodeNumber() }

    }

    private fun setSaveButtonState(text: String, editText: EditText) {
        addButtonSave.isEnabled = text.isNotEmpty() && editText.text.toString().isNotEmpty()
    }
}


interface AddEpisodeFragmentInterface {
    fun onSaveEpisodeClick(show: Show, title: String, description: String)
    fun onUploadPhotoClick()
    fun onChooseEpisodeNumber()
    fun onBackNavigation(title: String, description: String)
    fun formatEpisodeWithComma(season: Int, episode: Int) : String
}