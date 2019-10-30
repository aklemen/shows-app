package com.aklemen.shows

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import kotlinx.android.synthetic.main.activity_add_episode.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AddEpisodeActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_ADD_TITLE = "AddEpisodeActivity.addTitle"
        const val EXTRA_ADD_DESCRIPTION = "AddEpisodeActivity.addDescription"
        const val EXTRA_ADD_IMAGE = "AddEpisodeActivity.image"
        const val PERMISSION_REQUEST_CAMERA = 222
        const val PERMISSION_REQUEST_READ_EXT_STORAGE = 444
        const val ACTIVITY_REQUEST_TAKE_PHOTO = 333
        const val ACTIVITY_REQUEST_CHOOSE_PHOTO = 555

        fun newStartIntent(context: Context): Intent {
            return Intent(context, AddEpisodeActivity::class.java)
        }
    }

    private var currentImageUri: Uri? = null
    private var currentPhotoPath: String? = ""

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_ADD_IMAGE, currentImageUri?.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_episode)

        if (savedInstanceState != null) {
            currentImageUri = Uri.parse(savedInstanceState.getString(EXTRA_ADD_IMAGE))
            replaceImage(currentImageUri)
        }

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
            showPickerDialog()
        }
        addTextUploadImage.setOnClickListener {
            showPickerDialog()
        }
        addImageEpisode.setOnClickListener {
            showPickerDialog()
        }
        addTextChangeImage.setOnClickListener {
            showPickerDialog()
        }
    }

    private fun showPickerDialog() {
        val items = arrayOf("Take a photo", "Choose from gallery")
        AlertDialog.Builder(this)
            .setItems(items) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun openGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, ACTIVITY_REQUEST_CHOOSE_PHOTO)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder(this)
                    .setMessage("We just need your permission so you can choose a photo.")
                    .setPositiveButton("Ok") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_READ_EXT_STORAGE
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_READ_EXT_STORAGE)
            }
        }
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, ACTIVITY_REQUEST_TAKE_PHOTO)
                    }
                }
            }
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this)
                    .setMessage("We just need your permission so you can take a photo.")
                    .setPositiveButton("Ok") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CAMERA
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CAMERA
                )
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Camera permissions not granted", Toast.LENGTH_SHORT).show()
                }
            }
            PERMISSION_REQUEST_READ_EXT_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ACTIVITY_REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    replaceImage(Uri.parse(currentPhotoPath))
                }
            }
            ACTIVITY_REQUEST_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    replaceImage(data?.data)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun replaceImage(uri: Uri?) {
        addGroupEpisodePlaceholder.visibility = View.GONE
        addGroupEpisodeImage.visibility = View.VISIBLE
        addImageEpisode.setImageURI(uri)
        currentImageUri = uri
    }


    // Alert on back button

    override fun onBackPressed() {
        if (addEditTitle.text.toString().isNotEmpty() || addEditDescription.text.toString().isNotEmpty() || addImageEpisode.drawable != null) {
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
