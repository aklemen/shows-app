package com.aklemen.shows

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_welcome.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


private const val EXTRA_USER_NAME = "WelcomeActivity.username"

class WelcomeActivity : AppCompatActivity() {

    companion object{
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

        welcome_welcometext.text = "Welcome ${username}"

        Handler().postDelayed({
            startActivity(ShowsActivity.newStartIntent(this))
        }, 2000)


    }
}
