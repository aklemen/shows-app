package com.aklemen.shows.ui.shows.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.aklemen.shows.R
import com.aklemen.shows.data.model.EpisodeNumber
import com.aklemen.shows.ui.shows.shared.ShowsSharedViewModel
import kotlinx.android.synthetic.main.dialog_number_picker.*


class NumberPickerDialog : DialogFragment() {

    companion object {

        fun newStartFragment(): NumberPickerDialog =
            NumberPickerDialog()

    }


    private lateinit var showsSharedViewModel: ShowsSharedViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showsSharedViewModel = ViewModelProviders.of(requireActivity()).get(ShowsSharedViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_number_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews() {
        dialogPickerSeason.apply {
            minValue = 1
            maxValue = 20
            wrapSelectorWheel = false
        }

        dialogPickerEpisode.apply {
            minValue = 1
            maxValue = 99
            wrapSelectorWheel = false
        }
    }

    private fun initListeners() {
        dialogPickerSave.setOnClickListener {
            showsSharedViewModel.setEpisodeNumber(
                EpisodeNumber(
                    dialogPickerSeason.value,
                    dialogPickerEpisode.value
                )
            )
            dialog?.dismiss()
        }
    }

}
