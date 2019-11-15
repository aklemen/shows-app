package com.aklemen.shows.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aklemen.shows.R
import com.aklemen.shows.data.model.Credentials
import com.aklemen.shows.ui.shows.shared.ShowsMasterActivity
import com.aklemen.shows.util.ShowsApplication
import com.aklemen.shows.util.setInputTypeToPassword
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.HttpException

class LoginActivity : AppCompatActivity(), RegisterFragmentInterface {

    companion object {
        private const val MIN_PASSWORD_LENGTH: Int = 6
        const val PREF_TOKEN = "token"
        const val PREF_REMEMBER_ME = "remember_me"

        fun newStartIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViewsAndVariables()
        checkLoginStatus()
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        loginViewModel.errorLiveData.observe(this, Observer { error ->
            when (error) {
                is HttpException -> Toast.makeText(
                    this,
                    "Something didn't go as planned. :( Try again later.",
                    Toast.LENGTH_LONG
                ).show()
                is Throwable -> Toast.makeText(
                    this,
                    "Something didn't go as planned. :( Try again later.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        loginViewModel.tokenLiveData.observe(this, Observer {
            login()
        })
    }

    private fun checkLoginStatus() {
        if (ShowsApplication.getToken().isNotEmpty() && ShowsApplication.getRememberMe()) {
            login()
        }
    }

    private fun initViewsAndVariables() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        loginEditPassword.setInputTypeToPassword()
    }

    private fun initListeners() {
        loginEditUsername.doOnTextChanged { _, _, _, _ -> validateLoginInput() }
        loginEditPassword.doOnTextChanged { _, _, _, _ -> validateLoginInput() }

        loginButtonLogin.setOnClickListener {
            val credentials =
                Credentials(
                    loginEditUsername.text.toString(),
                    loginEditPassword.text.toString()
                )
            loginViewModel.loginUser(credentials, loginCheckboxRemember.isChecked)
        }

        loginTextCreate.setOnClickListener {
            openRegisterFragment()
        }
    }

    private fun openRegisterFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.loginFragmentContainer,
                RegisterFragment.newStartFragment()
            )
            .addToBackStack("RegisterFragment")
            .commit()
        // Removing the observers so the login is not called twice (from RegisterFragment an LoginActivity) when the tokenLiveData is posted
        loginViewModel.tokenLiveData.removeObservers(this)
    }

    private fun login() {
        startActivity(ShowsMasterActivity.newStartIntent(this))
        finish()
    }

    override fun onBackNavigation() {
        supportFragmentManager.popBackStack()
        initObservers()
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

    override fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun isPasswordValid(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
}
