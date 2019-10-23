package com.aklemen.shows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*

// Adding the extension function to make it easy to listen to the changes in EditText

fun EditText.addTextListener(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }
    })
}

class LoginActivity : AppCompatActivity() {

    companion object{
        private const val MIN_PASSWORD_LENGTH : Int = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // Listening to text changes

        login_edit_username.addTextListener {

            if (isEmailValid(login_edit_username.text.toString())){
                login_layout_username.error = null
                login_button_login.isEnabled = isPasswordValid(login_edit_password.text.toString())
            }
            else{
                login_layout_username.error = "Please type in a valid email or you shall not pass."
                login_button_login.isEnabled = false
            }

        }

        login_edit_password.addTextListener {

            if (isPasswordValid(it)) {
                login_layout_password.error = null
                login_button_login.isEnabled = isEmailValid(login_edit_username.text.toString())
            }
            else {
                login_layout_password.error = "At least six characters needed. You can do it!"
                login_button_login.isEnabled = false
            }
        }


        // Login button listener to start new activity

        login_button_login.setOnClickListener{
            startActivity(WelcomeActivity.newStartIntent(this, login_edit_username.text.toString()))
        }



    }

    // Checks if email is valid

    fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordValid(password: String) : Boolean = password.length >= MIN_PASSWORD_LENGTH

}
