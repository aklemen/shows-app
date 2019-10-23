package com.aklemen.shows

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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


        add_toolbar.setNavigationOnClickListener { onBackPressed() }

        add_button_save.setOnClickListener {

            if (add_edit_title.text.toString().isNotEmpty() && add_edit_description.text.toString().isNotEmpty()) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_ADD_TITLE, add_edit_title.text.toString())
                    putExtra(EXTRA_ADD_DESCRIPTION, add_edit_description.text.toString())
                })
                finish()
            }

        }

        add_edit_title.addTextListener {
            setSaveButtonState(it, add_edit_description)
        }

        add_edit_description.addTextListener {
            setSaveButtonState(it, add_edit_title)
        }

    }

    override fun onBackPressed() {

        if (add_edit_title.text.toString().isNotEmpty() && add_edit_description.text.toString().isNotEmpty()) {
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

    fun setSaveButtonState(text : String, editText: EditText) {
        if (text.isNotEmpty() && editText.text.toString().isNotEmpty()) {
            add_button_save.isEnabled = true
        } else {
            add_button_save.isEnabled = false
        }
    }
}
