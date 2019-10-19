package com.aklemen.shows

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // Listening to text changes

        main_username.addTextListener {

            if (it.isEmpty()){
                main_username_layout.error = "Please type in a valid email or you shall not pass."
                main_button_login.isEnabled = false
            }
            else{
                if (isEmailValid(main_username.text.toString())){
                    main_username_layout.error = null
                    if (main_password.text!!.length >= 6){
                        main_button_login.isEnabled = true
                    }
                }
                else{
                    main_username_layout.error = "Please type in a valid email or you shall not pass."
                    main_button_login.isEnabled = false
                }
            }

        }

        main_password.addTextListener {

            if (it.length < 6) {
                main_password_layout.error = "At least six characters needed. You can do it!"
                main_button_login.isEnabled = false
            }
            else {
                main_password_layout.error = null
                main_button_login.isEnabled = isEmailValid(main_username.text.toString())
            }
        }
    }

    // Checks if email is valid

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Adding the extension function to make it easy to listen to the changes in EditText

    fun EditText.addTextListener(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
}
