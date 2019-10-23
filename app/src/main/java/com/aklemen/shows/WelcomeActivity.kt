package com.aklemen.shows

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    companion object{

        // Constants and function to start the new activity

        private const val EXTRA_USER_NAME = "WelcomeActivity.username"

        fun newStartIntent(context: Context, username: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_USER_NAME, username)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val username : String = intent.getStringExtra(EXTRA_USER_NAME)

        welcome_text_welcome.text = "Welcome $username"

        Handler().postDelayed({
            startActivity(ShowsActivity.newStartIntent(this))
        }, 2000)


    }
}
