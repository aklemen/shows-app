package com.aklemen.shows.ui.shows.episode.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aklemen.shows.R
import com.aklemen.shows.data.model.EpisodePost
import com.aklemen.shows.ui.shows.shared.ShowsSharedViewModel
import kotlinx.android.synthetic.main.fragment_add_episode.*
import java.text.DecimalFormat


class AddEpisodeFragment : Fragment() {

    companion object {

        private const val EXTRA_SHOW_ID = "AddEpisodeFragment.showId"
        private const val MIN_DESCRIPTION_LENGTH: Int = 50

        fun newStartFragment(showId: String): AddEpisodeFragment {
            val args = Bundle()
            args.putString(EXTRA_SHOW_ID, showId)
            val fragment = AddEpisodeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var showsSharedViewModel: ShowsSharedViewModel
    private lateinit var addEpisodeViewModel: AddEpisodeViewModel
    private var addEpisodeFragmentInterface: AddEpisodeFragmentInterface? = null

    private var showId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)


        showsSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShowsSharedViewModel::class.java)
        addEpisodeViewModel = ViewModelProviders.of(requireActivity()).get(AddEpisodeViewModel::class.java)

        if (context is AddEpisodeFragmentInterface) {
            addEpisodeFragmentInterface = context
        } else {
            throw RuntimeException("Please implement ShowDetailFragmentInterface")
        }

        showId = arguments?.getString(EXTRA_SHOW_ID, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_episode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        addToolbar.setNavigationOnClickListener { navigateBack() }

        addButtonSave.setOnClickListener { saveEpisode() }

        addEditTitle.doOnTextChanged { _, _, _, _ -> validateInput() }
        addEditDescription.doOnTextChanged { _, _, _, _ -> validateInput() }

        addImageCamera.setOnClickListener { addEpisodeFragmentInterface?.onUploadPhotoClick() }
        addTextUploadImage.setOnClickListener { addEpisodeFragmentInterface?.onUploadPhotoClick() }
        addImageEpisode.setOnClickListener { addEpisodeFragmentInterface?.onUploadPhotoClick() }
        addTextChangeImage.setOnClickListener { addEpisodeFragmentInterface?.onUploadPhotoClick() }

        addTextEpisodeNumber.setOnClickListener { openNumberPickerDialog() }
    }

    private fun initObservers() {
        showsSharedViewModel.imageLiveData.observe(this, Observer {
            if (it != null) {
                addGroupEpisodePlaceholder.visibility = View.GONE
                addGroupEpisodeImage.visibility = View.VISIBLE
                addImageEpisode.setImageURI(it)
                validateInput()
            } else {
                addGroupEpisodePlaceholder.visibility = View.VISIBLE
                addGroupEpisodeImage.visibility = View.GONE
            }
        })

        showsSharedViewModel.episodeNumberLiveData.observe(this, Observer {
            if (it != null) {
                addTextEpisodeNumber.text = formatEpisodeWithComma(it.season, it.episode)
                validateInput()
            }
        })
    }

    private fun saveEpisode() {
        val episodeNumbers = showsSharedViewModel.episodeNumberLiveData.value

        showId?.let {
            EpisodePost(
                title = addEditTitle.text.toString(),
                description = addEditDescription.text.toString(),
                episodeNumber = episodeNumbers?.season.toString(),
                season = episodeNumbers?.episode.toString(),
                showId = it
            )
        }?.let {
            addEpisodeViewModel.addNewEpisode(it)
            showsSharedViewModel.uploadImage()
        }

        fragmentManager?.popBackStack()
        showsSharedViewModel.setImageUri(null)
        showsSharedViewModel.setEpisodeNumber(null)
        addEpisodeFragmentInterface?.hideKeyboard()
    }

    private fun navigateBack() {
        if (addEditTitle.text.toString().isNotEmpty() ||
            addEditDescription.text.toString().isNotEmpty() ||
            showsSharedViewModel.imageLiveData.value != null ||
            showsSharedViewModel.episodeNumberLiveData.value != null
        ) {
            AlertDialog.Builder(requireContext())
                .setTitle("Watch out")
                .setMessage("Your changes will be lost. Are you sure you want to continue?")
                .setPositiveButton("Yes") { _, _ ->
                    fragmentManager?.popBackStack()
                    showsSharedViewModel.setImageUri(null)
                    showsSharedViewModel.setEpisodeNumber(null)
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        } else {
            fragmentManager?.popBackStack()
            showsSharedViewModel.setImageUri(null)
            showsSharedViewModel.setEpisodeNumber(null)
        }
        addEpisodeFragmentInterface?.hideKeyboard()
    }

    private fun openNumberPickerDialog() {
        fragmentManager?.let { NumberPickerDialog.newStartFragment().show(it, "NumberPickerDialog") }
    }

    private fun validateInput() {
        val imageUri = showsSharedViewModel.imageLiveData.value
        val episodeNumbers = showsSharedViewModel.episodeNumberLiveData.value

        addLayoutTitle.apply {
            error = if (addEditTitle.text.toString().isEmpty()) "Title is required."
            else null
        }

        addLayoutDescription.apply {
            error = if (addEditDescription.text.toString().length < MIN_DESCRIPTION_LENGTH) "Descriptions need to have at least 50 characters."
            else null
        }


        addButtonSave.isEnabled =
            addEditTitle.text.toString().isNotEmpty() && addEditDescription.text.toString().isNotEmpty() && imageUri != null && episodeNumbers != null
    }

    private fun formatEpisodeWithComma(season: Int, episode: Int): String {
        val df = DecimalFormat("00")
        val seasonFormatted = df.format(season)
        val episodeFormatted = df.format(episode)
        return "S $seasonFormatted, E $episodeFormatted"
    }
}


interface AddEpisodeFragmentInterface {
    fun hideKeyboard()
    fun onUploadPhotoClick()
}