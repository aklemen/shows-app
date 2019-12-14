package com.aklemen.shows.util

import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.EditText

fun EditText.setInputTypeToPassword(){
    this.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
    this.transformationMethod = PasswordTransformationMethod.getInstance()
}