package com.aklemen.shows.ui.login


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aklemen.shows.R
import com.aklemen.shows.data.model.Credentials
import com.aklemen.shows.util.setInputTypeToPassword
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    companion object {

        fun newStartFragment(): RegisterFragment =
            RegisterFragment()
    }

    private lateinit var loginViewModel: LoginViewModel
    private var registerFragmentInterface: RegisterFragmentInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        loginViewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel::class.java)

        if (context is RegisterFragmentInterface) {
            registerFragmentInterface = context
        } else {
            throw RuntimeException("Please implement registerFragmentInterface")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() {
        registerEditPassword.setInputTypeToPassword()
        registerEditPasswordAgain.setInputTypeToPassword()
    }

    private fun initObservers() {
        loginViewModel.tokenLiveData.observe(this, Observer {
            startActivity(WelcomeActivity.newStartIntent(requireContext(), registerEditEmail.text.toString()))
            activity?.finish()
        })
    }

    private fun initListeners() {
        registerToolbar.setNavigationOnClickListener { registerFragmentInterface?.onBackNavigation() }
        registerEditEmail.doOnTextChanged { _, _, _, _ -> validateRegisterInput() }
        registerEditPassword.doOnTextChanged { _, _, _, _ -> validateRegisterInput() }
        registerEditPasswordAgain.doOnTextChanged { _, _, _, _ -> validateRegisterInput() }

        registerButton.setOnClickListener {
            val credentials =
                Credentials(
                    registerEditEmail.text.toString(),
                    registerEditPassword.text.toString()
                )
            loginViewModel.registerUser(credentials)
        }
    }

    private fun validateRegisterInput() {
        val isEmailOk = registerFragmentInterface?.isEmailValid(registerEditEmail.text.toString()) ?: false
        val isPasswordOk = registerFragmentInterface?.isPasswordValid(registerEditPassword.text.toString()) ?: false
        var isPasswordAgainOk = false

        if (!isEmailOk) {
            registerLayoutEmail.error = "Please type in a valid email or you shall not pass."
        } else {
            registerLayoutEmail.error = null
        }

        if (!isPasswordOk) {
            registerLayoutPassword.error = "At least six characters needed. You can do it!"
        } else {
            registerLayoutPassword.error = null
            if (registerEditPassword.text.toString() != registerEditPasswordAgain.text.toString()) {
                registerLayoutPasswordAgain.error = "Passwords need to be the same."
                isPasswordAgainOk = false
            } else {
                registerLayoutPasswordAgain.error = null
                isPasswordAgainOk = true
            }
        }

        registerButton.isEnabled = isEmailOk && isPasswordOk && isPasswordAgainOk
    }

}


interface RegisterFragmentInterface {
    fun onBackNavigation()
    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
}

