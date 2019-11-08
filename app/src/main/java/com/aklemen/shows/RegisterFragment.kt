package com.aklemen.shows


import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    companion object {

        fun newStartFragment(): RegisterFragment = RegisterFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Need to set this here instead of XML so the right font is applied
        setPasswordInputType()
    }

    private fun setPasswordInputType() {
        registerEditPassword.apply {
            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            transformationMethod = PasswordTransformationMethod.getInstance()
        }
        registerEditPasswordAgain.apply {
            registerEditPasswordAgain.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            loginEditPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
    }
}


