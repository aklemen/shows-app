package com.aklemen.shows

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class LoginActivity : AppCompatActivity(), RegisterFragmentInterface {

    companion object {
        private const val MIN_PASSWORD_LENGTH: Int = 6
        private const val PREF_USERNAME = "LoginActivity.username"
        private const val PREF_PASSWORD = "LoginActivity.password"

        fun newStartIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    private lateinit var showsViewModel: ShowsViewModel

    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        showsViewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)

        checkLoginStatus()
        initViews()
        initListeners()
    }

    private fun checkLoginStatus() {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)

        if (sharedPrefs?.getString(PREF_USERNAME, "")?.isNotEmpty() == true && sharedPrefs?.getString(PREF_PASSWORD, "")?.isNotEmpty() == true) {
            startActivity(ShowsMasterActivity.newStartIntent(this))
            finish()
        }
    }

    private fun initViews() {
        // Need to set this here (via extension fun) instead of XML, so the right font is applied
        loginEditPassword.setInputTypeToPassword()
    }

    private fun initListeners() {
        loginEditUsername.doOnTextChanged { _, _, _, _ -> validateLoginInput() }
        loginEditPassword.doOnTextChanged { _, _, _, _ -> validateLoginInput() }

        loginButtonLogin.setOnClickListener { login() }

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

    private fun login() {
//TODO
//        if (loginCheckboxRemember.isChecked) {
//            val editor: SharedPreferences.Editor? = sharedPrefs?.edit()
//            editor?.putString(PREF_USERNAME, loginEditUsername.text.toString())
//            editor?.putString(PREF_PASSWORD, loginEditPassword.text.toString())
//            editor?.apply()
//        }
//
//        startActivity(WelcomeActivity.newStartIntent(this, loginEditUsername.text.toString()))
//        finish()


//        Singleton.service.login(Credentials("y@f.s", "string"))
//            .enqueue(object : Callback<ResponseBody>{
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Toast.makeText(this@LoginActivity, "Failed", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    Toast.makeText(this@LoginActivity, response.raw().toString(), Toast.LENGTH_SHORT).show()
//                }
//
//            })


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
            loginEditPassword.error = null
        }

        loginButtonLogin.isEnabled = isUsernameOk && isPasswordOk

    }

    override fun onRegister(email: String, password: String) {
        Singleton.service.register(Credentials(email, password)).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                showsViewModel.errorLiveData.postValue(t)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        var user = body.let {
                            User(
                                id = it.id,
                                email = it.email,
                                type = it.type
                            )
                        }
                        Log.d("Uporabnik", user.email)
                    }
                }
            }

        })
    }

    override fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun isPasswordValid(password: String): Boolean = password.length >= MIN_PASSWORD_LENGTH
}
