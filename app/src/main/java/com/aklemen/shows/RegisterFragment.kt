package com.aklemen.shows


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.fragment_register.*
import java.lang.RuntimeException


class RegisterFragment : Fragment() {

    companion object {

        fun newStartFragment(): RegisterFragment = RegisterFragment()
    }

    private var registerFragmentInterface: RegisterFragmentInterface? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

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
    }

    private fun initViews() {
        // Need to set this here instead of XML so the right font is applied
        registerEditPassword.setInputTypeToPassword()
        registerEditPasswordAgain.setInputTypeToPassword()
    }

    private fun initListeners() {
        registerEditEmail.doOnTextChanged { _, _, _, _ -> validateRegisterInput() }
        registerEditPassword.doOnTextChanged { _, _, _, _ -> validateRegisterInput() }
        registerEditPasswordAgain.doOnTextChanged { _, _, _, _ -> validateRegisterInput() }

        registerButton.setOnClickListener {
            val email = registerEditEmail.text.toString()
            val password = registerEditPassword.text.toString()
            registerFragmentInterface?.onRegister(email, password)
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
    fun onRegister(email: String, password: String)
    fun isEmailValid(email: String): Boolean
    fun isPasswordValid(password: String): Boolean
}

