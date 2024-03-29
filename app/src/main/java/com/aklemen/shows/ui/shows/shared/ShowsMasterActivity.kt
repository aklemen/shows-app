package com.aklemen.shows.ui.shows.shared

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.transition.Slide
import android.transition.Transition
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.aklemen.shows.R
import com.aklemen.shows.ui.login.LoginActivity
import com.aklemen.shows.ui.shows.episode.add.AddEpisodeFragment
import com.aklemen.shows.ui.shows.episode.add.AddEpisodeFragmentInterface
import com.aklemen.shows.ui.shows.detail.ShowDetailFragment
import com.aklemen.shows.ui.shows.detail.ShowDetailFragmentInterface
import com.aklemen.shows.ui.shows.episode.comments.CommentsFragment
import com.aklemen.shows.ui.shows.episode.detail.EpisodeDetailFragment
import com.aklemen.shows.ui.shows.episode.detail.EpisodeDetailInterface
import com.aklemen.shows.ui.shows.list.ShowsListFragment
import com.aklemen.shows.ui.shows.list.ShowsListInterface
import com.aklemen.shows.util.ShowsApp
import kotlinx.android.synthetic.main.activity_shows_master.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ShowsMasterActivity :
    AppCompatActivity(),
    ShowsListInterface,
    ShowDetailFragmentInterface,
    AddEpisodeFragmentInterface,
    EpisodeDetailInterface {

    //TODO Add error handling everywhere - errorLiveData
    //TODO Add animations for fragments and activities

    companion object {

        const val PERMISSION_REQUEST_CAMERA = 222
        const val PERMISSION_REQUEST_READ_EXT_STORAGE = 444
        const val ACTIVITY_REQUEST_TAKE_PHOTO = 333
        const val ACTIVITY_REQUEST_CHOOSE_PHOTO = 555

        fun newStartIntent(context: Context): Intent =
            Intent(context, ShowsMasterActivity::class.java)

    }

    private lateinit var showsSharedViewModel: ShowsSharedViewModel

    private var currentPhotoPath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows_master)

        showsSharedViewModel = ViewModelProviders.of(this).get(ShowsSharedViewModel::class.java)

        if (savedInstanceState == null) {
            addShowsListFragment()
        }
    }

    private fun addShowsListFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.showsFragmentContainer, ShowsListFragment.newStartFragment())
            .commit()
    }

    override fun onShowClicked(showId: String) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.showsFragmentContainer, ShowDetailFragment.newStartFragment(showId))
            .addToBackStack("ShowDetailFragment")
            .commit()
    }

    override fun logout() {
        AlertDialog.Builder(this)
            .setTitle("Log out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                ShowsApp.clearPrefs()
                startActivity(LoginActivity.newStartIntent(this))
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                finish()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    override fun onEpisodeClick(episodeId: String) {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.showsFragmentContainer, EpisodeDetailFragment.newStartFragment(episodeId))
            .addToBackStack("EpisodeDetailFragment")
            .commit()
    }

    override fun onCommentsClick(episodeId: String) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.showsFragmentContainer, CommentsFragment.newStartFragment(episodeId))
            .addToBackStack("CommentsFragment")
            .commit()
    }

    override fun onAddEpisodeClick(showId: String) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.showsFragmentContainer, AddEpisodeFragment.newStartFragment(showId))
            .addToBackStack("AddEpisodeFragment")
            .commit()
    }

    override fun onUploadPhotoClick() {
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
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
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
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
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
                    Toast.makeText(this, "Camera permissions not granted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            PERMISSION_REQUEST_READ_EXT_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT)
                        .show()
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
        showsSharedViewModel.setImageUri(uri)
    }

    override fun hideKeyboard() {
        try {
            showsFragmentContainer.apply {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(showsFragmentContainer.windowToken, 0)
            }
        } catch (e: Exception) {

        }
    }

}
