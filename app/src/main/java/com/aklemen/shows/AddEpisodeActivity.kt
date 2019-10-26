package com.aklemen.shows

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_add_episode.*

class AddEpisodeActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_ADD_TITLE = "AddEpisodeActivity.addTitle"
        const val EXTRA_ADD_DESCRIPTION = "AddEpisodeActivity.addDescription"

        fun newStartIntent(context: Context): Intent {
            return Intent(context, AddEpisodeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        // Back button toolbar navigation

        addToolbar.setNavigationOnClickListener { onBackPressed() }


        // Save button listener and states

        addButtonSave.setOnClickListener {

            if (addEditTitle.text.toString().isNotEmpty() && addEditDescription.text.toString().isNotEmpty()) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_ADD_TITLE, addEditTitle.text.toString())
                    putExtra(EXTRA_ADD_DESCRIPTION, addEditDescription.text.toString())
                })
                finish()
            }

        }

        addEditTitle.doOnTextChanged { text, _, _, _ ->
            setSaveButtonState(text.toString(), addEditDescription)
        }

        addEditDescription.doOnTextChanged { text, _, _, _ ->
            setSaveButtonState(text.toString(), addEditTitle)}

    }


    // Alert dialog when user presses back button

    override fun onBackPressed() {

        if (addEditTitle.text.toString().isNotEmpty() && addEditDescription.text.toString().isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Watch out")
                .setMessage("Your changes will be lost. Are you sure you want to continue?")
                .setPositiveButton("Yes") { _, _ ->
                    setResult(Activity.RESULT_CANCELED)
                    super.onBackPressed()
                }
                .setNegativeButton("No, cancel", null)
                .create()
                .show()
        } else {
            setResult(Activity.RESULT_CANCELED)
            super.onBackPressed()
        }
    }


    // Function for handling button state depending on EditTexts

    fun setSaveButtonState(text: String, editText: EditText) {
        if (text.isNotEmpty() && editText.text.toString().isNotEmpty()) {
            addButtonSave.isEnabled = true
        } else {
            addButtonSave.isEnabled = false
        }
    }
}
