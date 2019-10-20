package com.aklemen.shows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val username : String = intent.getStringExtra("USER_NAME")

        welcome_welcometext.text = "Welcome ${username}"

        Handler().postDelayed({
            val intent = Intent(this, ShowsActivity::class.java)
            startActivity(intent)
        }, 2000)


    }
}
