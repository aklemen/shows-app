package com.aklemen.shows.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.aklemen.shows.R
import com.aklemen.shows.ui.shows.shared.ShowsMasterActivity
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_USER_NAME = "WelcomeActivity.username"

        fun newStartIntent(context: Context, username: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(EXTRA_USER_NAME, username)
            return intent
        }

    }

    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initViewsAndVariables()
        startNextActivityWithDelay(2000)
    }

    private fun initViewsAndVariables() {
        var username: String = intent.getStringExtra(EXTRA_USER_NAME)
        username = username.substring(0,1).toUpperCase() + username.substring(1, username.indexOf("@"))
        welcomeTextWelcome.text = "Welcome $username"
    }

    private fun startNextActivityWithDelay(delay: Long) {
        handler = Handler()
        handler?.postDelayed({
            startActivity(ShowsMasterActivity.newStartIntent(this))
            finish()
        }, delay)
    }

    override fun onBackPressed() {
        handler?.removeCallbacksAndMessages(null)
        super.onBackPressed()
    }
}
