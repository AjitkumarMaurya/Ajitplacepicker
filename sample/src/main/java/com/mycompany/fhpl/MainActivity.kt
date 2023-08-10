package com.mycompany.fhpl

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ajit.pingplacepicker.PingPlacePicker
import com.ajit.pingplacepicker.galleryimagepicker.ImagePicker
import com.ajit.pingplacepicker.galleryimagepicker.RedBookPresenter
import com.ajit.pingplacepicker.galleryimagepicker.bean.MimeType
import com.ajit.pingplacepicker.galleryimagepicker.data.OnImagePickCompleteListener
import com.ajit.pingsample.R
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    var waitResult: ActivityResultLauncher<Intent>? = null
    private val requestIdMultiplePermissions = 1
    private val permissionsRequest: ArrayList<String> =
        arrayListOf(Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VIDEO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        waitResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

                val place: Place? = PingPlacePicker.getPlace(result.data!!)
                toast("You selected: ${place?.name}\n ${place?.id}")

            }
        }
    }

    private fun showPlacePicker() {

        val builder = PingPlacePicker.IntentBuilder()

        builder.setAndroidApiKey(getString(R.string.key_google_apis_android))
            .setMapsApiKey(getString(R.string.key_google_apis_maps))

        // If you want to set a initial location
        // rather then the current device location.
        // pingBuilder.setLatLng(LatLng(37.4219999, -122.0862462))

        try {
            val placeIntent = builder.build(this)
            waitResult!!.launch(placeIntent)
        } catch (ex: Exception) {
            toast("Google Play Services is not Available")
        }
    }

    fun gallery(view: View) {


        if (checkMultipleRequestPermissions()) {
            ImagePicker.withCrop(RedBookPresenter())
                .setMaxCount(5)
                .showCamera(true)
                .setColumnCount(4)
                .mimeTypes(MimeType.ofAll())
                .filterMimeTypes(MimeType.GIF)
                .assignGapState(true)
                .setFirstImageItem(null)
                .setFirstImageItemSize(1, 1)
                .setVideoSinglePick(true)
                .setMaxVideoDuration(60000L)
                .setMinVideoDuration(3000L)
                .pick(
                    this@MainActivity,
                    OnImagePickCompleteListener { items ->



                    } as OnImagePickCompleteListener?)

        }

    }
    fun ping(view: View) { showPlacePicker()}

    private fun doOperation() {
        Toast.makeText(this, "Successfully granted", Toast.LENGTH_LONG).show()
    }
    private fun checkMultipleRequestPermissions(): Boolean {
        val listPermissionsNeeded: MutableList<String> = ArrayList()

        for (p in permissionsRequest) {
            val result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                requestIdMultiplePermissions
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestIdMultiplePermissions) {
            if (grantResults.isNotEmpty()) {
                var isGrant = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    doOperation()
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission
                            )
                        )  {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }
                    if (someDenied) {
                        settingActivityOpen()
                    }else{
                        showDialogOK { _: DialogInterface?, which: Int ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> checkMultipleRequestPermissions()
                                DialogInterface.BUTTON_NEGATIVE -> { }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun settingActivityOpen() {
        Toast.makeText(
            this,
            "Go to settings and enable permissions",
            Toast.LENGTH_LONG
        )
            .show()
        val i = Intent()
        i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        i.addCategory(Intent.CATEGORY_DEFAULT)
        i.data = Uri.parse("package:$packageName")
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        startActivity(i)
    }

    private fun showDialogOK(okListener: DialogInterface.OnClickListener) {
        MaterialAlertDialogBuilder(this)
            .setMessage("All Permission required for this app")
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", okListener)
            .create()
            .show()
    }

}
