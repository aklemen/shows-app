package com.aklemen.shows

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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

    var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initViewsAndVariables()
        startNextActivityWithDelay(2000)
    }

    private fun initViewsAndVariables(){
        val username: String = intent.getStringExtra(EXTRA_USER_NAME)
        welcomeTextWelcome.text = "Welcome $username"
    }

    private fun startNextActivityWithDelay(delay: Long){
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
