package com.aklemen.shows

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    companion object {
        private const val MIN_PASSWORD_LENGTH: Int = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initListeners()
    }

    private fun initListeners() {
        loginEditUsername.doOnTextChanged { _, _, _, _ -> validateUserInput() }
        loginEditPassword.doOnTextChanged { _, _, _, _ -> validateUserInput() }

        loginButtonLogin.setOnClickListener { login() }
    }

    private fun login() {
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
