package com.aklemen.shows

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_episode.*
import kotlinx.android.synthetic.main.activity_show_detail.*

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

    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)

        if (add_edit_title.text.toString().isNotEmpty() ){

        }

        super.onBackPressed()
    }

}
