package com.aklemen.shows

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val MIN_PASSWORD_LENGTH: Int = 6
        private const val PREF_USERNAME = "LoginActivity.username"
        private const val PREF_PASSWORD = "LoginActivity.password"

        fun newStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkLoginStatus()
        initListeners()
    }

    private fun checkLoginStatus() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)

        if (sharedPrefs?.getString(PREF_USERNAME, "")?.isNotEmpty() == true && sharedPrefs?.getString(PREF_PASSWORD, "")?.isNotEmpty() == true) {
            startActivity(ShowsMasterActivity.newStartIntent(this))
            finish()
        }
    }

    private fun initListeners() {
        loginEditUsername.doOnTextChanged { _, _, _, _ -> validateUserInput() }
        loginEditPassword.doOnTextChanged { _, _, _, _ -> validateUserInput() }

        loginButtonLogin.setOnClickListener { login() }
    }

    private fun login() {

        if (loginCheckboxRemember.isChecked) {
            val editor: SharedPreferences.Editor? = sharedPrefs?.edit()
            editor?.putString(PREF_USERNAME, loginEditUsername.text.toString())
            editor?.putString(PREF_PASSWORD, loginEditPassword.text.toString())
            editor?.apply()
        }

        startActivity(WelcomeActivity.newStartIntent(this, loginEditUsername.text.toString()))
        finish()
    }

    private fun validateUserInput() {
        val isUsernameOk = isEmailValid(loginEditUsername.text.toString())
        val isPasswordOk = isPasswordValid(loginEditPassword.text.toString())

        if (!isUsernameOk) {
            loginLayoutUsername.error = "Please type in a valid email or you shall not pass."
        } else {
            loginLayoutUsername.error = null
        }

        if (!isPasswordOk) {
            loginLayoutPassword.error = "At least six characters needed. You can do it!"
        } else {
            loginEditPassword.error = null
        }

        loginButtonLogin.isEnabled = isUsernameOk && isPasswordOk

    }

    private fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
}
