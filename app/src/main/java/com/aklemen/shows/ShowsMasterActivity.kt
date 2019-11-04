package com.aklemen.shows

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_add_episode.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ShowsMasterActivity : AppCompatActivity(), ShowsListInterface, ShowDetailFragmentInterface, AddEpisodeFragmentInterface {

    companion object {

        const val PERMISSION_REQUEST_CAMERA = 222
        const val PERMISSION_REQUEST_READ_EXT_STORAGE = 444
        const val ACTIVITY_REQUEST_TAKE_PHOTO = 333
        const val ACTIVITY_REQUEST_CHOOSE_PHOTO = 555

        fun newStartIntent(context: Context): Intent {
            return Intent(context, ShowsMasterActivity::class.java)
        }

        var listOfShows = mutableListOf(
            Show(
                "0",
                "The Big Bang Theory",
                "2007-2019", """A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows
                    | them how little they know about life outside of the laboratory.""".trimMargin(),
                mutableListOf(),
                R.drawable.bigbang
            ),
            Show(
                "1",
                "The Office",
                "2005-2013",
                """A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium.""",
                mutableListOf(),
                R.drawable.office
            ),
            Show(
                "2",
                "House M.D.",
                "2004-2012",
                """An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases
                    | that come his way using his crack team of doctors and his wits.""".trimMargin(),
                mutableListOf(),
                R.drawable.house
            ),
            Show(
                "3",
                "Jane the Virgin",
                "2014 - ",
                """A young, devout Catholic woman discovers that she was accidentally artificially inseminated.""",
                mutableListOf(),
                R.drawable.jane
            ),
            Show(
                "4",
                "Sherlock",
                "2010 - ",
                """A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.""",
                mutableListOf(),
                R.drawable.sherlock
            ),
            Show(
                "5",
                "The Big Bang Theory",
                "2007-2019", """A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows
                    | them how little they know about life outside of the laboratory.""".trimMargin(),
                mutableListOf(),
                R.drawable.bigbang
            ),
            Show(
                "6",
                "The Office",
                "2005-2013",
                """A mockumentary on a group of typical office workers, where the workday consists of ego clashes, inappropriate behavior, and tedium.""",
                mutableListOf(),
                R.drawable.office
            ),
            Show(
                "7",
                "House M.D.",
                "2004-2012",
                """An antisocial maverick doctor who specializes in diagnostic medicine does whatever it takes to solve puzzling cases
                    | that come his way using his crack team of doctors and his wits.""".trimMargin(),
                mutableListOf(),
                R.drawable.house
            ),
            Show(
                "8",
                "Jane the Virgin",
                "2014 - ",
                """A young, devout Catholic woman discovers that she was accidentally artificially inseminated.""",
                mutableListOf(),
                R.drawable.jane
            ),
            Show(
                "9",
                "Sherlock",
                "2010 - ",
                """A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.""",
                mutableListOf(),
                R.drawable.sherlock
            )
        )
    }

    private lateinit var showsViewModel: ShowsViewModel

    //    private var currentImageUri: Uri? = null
    private var currentPhotoPath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_master)

        showsViewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        if (savedInstanceState == null) {
            addShowsListFragment()
        }
    }

    private fun addShowsListFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.showsFragmentContainer, ShowsListFragment.newStartFragment())
            .commit()
    }

    override fun onShowClicked() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.showsFragmentContainer, ShowDetailFragment.newStartFragment())
            .addToBackStack("ShowDetailFragment")
            .commit()
    }

    override fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Log out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPreferences.edit()
                editor.clear().apply()
                startActivity(LoginActivity.newStartIntent(this))
                finish()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onAddEpisodeClick() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.showsFragmentContainer, AddEpisodeFragment.newStartFragment())
            .addToBackStack("AddEpisodeFragment")
            .commit()
    }

    override fun onSaveEpisodeClick(show: Show, title: String, description: String) {
        if (show.listOfEpisodes.add(
                Episode(
                    title,
                    description
                )
            )
        ) {
            Toast.makeText(this, "Episode successfully added.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Adding the episode failed!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onUploadPhotoClick() {
        showPickerDialog()
    }


    private fun showPickerDialog() {
        val items = arrayOf("Take a photo", "Choose from gallery")
        android.app.AlertDialog.Builder(this)
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
                android.app.AlertDialog.Builder(this)
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
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_READ_EXT_STORAGE
                )
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
                            "com.aklemen.shows",
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
        showsViewModel.currentImage.value = uri
    }


    // Alert on back button
//
//    override fun onBackPressed() {
//        if (addEditTitle.text.toString().isNotEmpty() || addEditDescription.text.toString().isNotEmpty() || addImageEpisode.drawable != null) {
//            AlertDialog.Builder(this)
//                .setTitle("Watch out")
//                .setMessage("Your changes will be lost. Are you sure you want to continue?")
//                .setPositiveButton("Yes") { _, _ ->
//                    setResult(Activity.RESULT_CANCELED)
//                    super.onBackPressed()
//                }
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show()
//        } else {
//            super.onBackPressed()
//        }
//    }


}
