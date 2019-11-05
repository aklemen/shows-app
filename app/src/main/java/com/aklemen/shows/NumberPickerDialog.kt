package com.aklemen.shows

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.dialog_number_picker.*


class NumberPickerDialog : DialogFragment() {

    companion object {

        fun newStartFragment(): NumberPickerDialog = NumberPickerDialog()

    }


    lateinit var showsViewModel : ShowsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        showsViewModel = ViewModelProviders.of(requireActivity()).get(ShowsViewModel::class.java)
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
            minValue = 0
            maxValue = 99
            wrapSelectorWheel = false
        }
    }

    private fun initListeners() {
        dialogPickerSave.setOnClickListener {
            showsViewModel.currentSeasonLiveData.value = dialogPickerSeason.value
            showsViewModel.currentEpisodeLiveData.value = dialogPickerEpisode.value
        }
    }

}

