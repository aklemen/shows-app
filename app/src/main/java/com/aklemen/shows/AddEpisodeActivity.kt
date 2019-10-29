package com.aklemen.shows

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_add_episode.*
import android.net.Uri


class AddEpisodeActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_ADD_TITLE = "AddEpisodeActivity.addTitle"
        const val EXTRA_ADD_DESCRIPTION = "AddEpisodeActivity.addDescription"
        const val PERMISSION_REQUEST_CAMERA = 222
        const val ACTIVITY_REQUEST_TAKE_PHOTO = 333

        fun newStartIntent(context: Context): Intent {
            return Intent(context, AddEpisodeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        initListeners()
    }

    private fun initListeners() {
        addToolbar.setNavigationOnClickListener { onBackPressed() }

        addButtonSave.setOnClickListener {
            if (addEditTitle.text.toString().isNotEmpty() && addEditDescription.text.toString().isNotEmpty()) {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_ADD_TITLE, addEditTitle.text.toString())
                    putExtra(EXTRA_ADD_DESCRIPTION, addEditDescription.text.toString())
                })
                finish()
            }
        }

        addEditTitle.doOnTextChanged { text, _, _, _ -> setSaveButtonState(text.toString(), addEditDescription) }
        addEditDescription.doOnTextChanged { text, _, _, _ -> setSaveButtonState(text.toString(), addEditTitle) }

        addImageCamera.setOnClickListener {
            openCamera()
        }
        addTextUploadImage.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), ACTIVITY_REQUEST_TAKE_PHOTO)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this)
                    .setMessage("We just need your permission so you can take a photo.")
                    .setPositiveButton("Ok") { _, _ ->
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permission not granted", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    // Alert on back button

    override fun onBackPressed() {
        if (addEditTitle.text.toString().isNotEmpty() || addEditDescription.text.toString().isNotEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Watch out")
                .setMessage("Your changes will be lost. Are you sure you want to continue?")
                .setPositiveButton("Yes") { _, _ ->
                    setResult(Activity.RESULT_CANCELED)
                    super.onBackPressed()
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        } else {
            super.onBackPressed()
        }
    }

    // Function for handling button state depending on EditTexts

    private fun setSaveButtonState(text: String, editText: EditText) {
        addButtonSave.isEnabled = text.isNotEmpty() && editText.text.toString().isNotEmpty()
    }
}
