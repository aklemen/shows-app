package com.aklemen.shows

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.HttpException


class LoginActivity : AppCompatActivity(), RegisterFragmentInterface {

    companion object {
        private const val MIN_PASSWORD_LENGTH: Int = 6
        private const val PREF_USERNAME = "LoginActivity.username"
        private const val PREF_PASSWORD = "LoginActivity.password"
        const val PREF_TOKEN = "LoginActivity.token"

        fun newStartIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private lateinit var showsViewModel: ShowsViewModel

    private var sharedPrefs: SharedPreferences? = null

    private var welcomeScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        showsViewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)

        initViewsAndVariables()
        checkLoginStatus()
        initListeners()

        showsViewModel.errorLiveData.observe(this, Observer { error ->
            when (error) {
                is HttpException -> Toast.makeText(
                    this,
                    "Response error: ${error.code()} ${error.message()}",
                    Toast.LENGTH_SHORT
                ).show()
                is Throwable -> Toast.makeText(
                    this,
                    "Error occurred: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        showsViewModel.credentialsLiveData.observe(this, Observer {
            login(it)
        })

        showsViewModel.tokenLiveData.observe(this, Observer {
            sharedPrefs?.edit()?.putString(PREF_TOKEN, it)?.apply()
            if (welcomeScreen) {
                val username = showsViewModel.credentialsLiveData.value?.email
                startActivity(WelcomeActivity.newStartIntent(this, username ?: "to Shows App!"))
            } else {
                startActivity(ShowsMasterActivity.newStartIntent(this))
            }
            finish()
        })
    }

    private fun checkLoginStatus() {
        val credentials = Credentials(
            sharedPrefs?.getString(PREF_USERNAME, "") ?: "",
            sharedPrefs?.getString(PREF_PASSWORD, "") ?: ""
        )

        if (credentials.email.isNotEmpty() && credentials.password.isNotEmpty()) {
            loginEditUsername.setText(credentials.email)
            loginEditPassword.setText(credentials.password)
            showsViewModel.loginUser(credentials)
        }
    }

    private fun initViewsAndVariables() {
        // Need to set this here (via extension fun) instead of XML, so the right font is applied
        loginEditPassword.setInputTypeToPassword()

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun initListeners() {
        loginEditUsername.doOnTextChanged { _, _, _, _ -> validateLoginInput() }
        loginEditPassword.doOnTextChanged { _, _, _, _ -> validateLoginInput() }

        loginButtonLogin.setOnClickListener {
            val credentials =
                Credentials(loginEditUsername.text.toString(), loginEditPassword.text.toString())
            login(credentials)
        }

        loginTextCreate.setOnClickListener {
            openRegisterFragment()
        }
    }

    private fun openRegisterFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.loginFragmentContainer, RegisterFragment.newStartFragment())
            .addToBackStack("RegisterFragment")
            .commit()
    }

    private fun login(credentials: Credentials) {

        if (loginCheckboxRemember.isChecked) {
            val editor: SharedPreferences.Editor? = sharedPrefs?.edit()
            editor?.putString(PREF_USERNAME, loginEditUsername.text.toString())
            editor?.putString(PREF_PASSWORD, loginEditPassword.text.toString())
            editor?.apply()
        }

        showsViewModel.loginUser(credentials)
    }

    private fun validateLoginInput() {
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
            loginLayoutPassword.error = null
        }

        loginButtonLogin.isEnabled = isUsernameOk && isPasswordOk

    }

    override fun onRegister(email: String, password: String) {
        showsViewModel.registerUser(Credentials(email, password))
        welcomeScreen = true
    }

    override fun isEmailValid(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun isPasswordValid(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
}
