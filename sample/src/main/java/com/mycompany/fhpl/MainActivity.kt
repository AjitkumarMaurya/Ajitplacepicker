package com.mycompany.fhpl

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ajit.pingplacepicker.galleryimagepicker.ImagePicker
import com.ajit.pingplacepicker.galleryimagepicker.RedBookPresenter
import com.ajit.pingplacepicker.galleryimagepicker.bean.MimeType
import com.ajit.pingplacepicker.galleryimagepicker.data.OnImagePickCompleteListener
import com.ajit.pingplacepicker.pix.Options
import com.ajit.pingplacepicker.pix.Pix
import com.ajit.pingsample.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class MainActivity : AppCompatActivity() {

    var options: Options? = null
    var returnValue = java.util.ArrayList<String>()


    private val requestIdMultiplePermissions = 1
    private val permissionsRequest: ArrayList<String> =
        arrayListOf(Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES,Manifest.permission.READ_MEDIA_VIDEO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
    fun ping(view: View) {

        options = Options.init()
            .setRequestCode(111)
            .setCount(1)
            .setFrontfacing(false)
            .setExcludeVideos(false)
            .setExcludeGallery(true)
            .setMode(Options.Mode.All)
            .setVideoDurationLimitinSeconds(59)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/storage/self/primary")
        Pix.start(this@MainActivity, options)

    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Log.e("val", "requestCode ->  " + requestCode+"  resultCode "+resultCode);
        if (requestCode == 111) {
            if (resultCode == RESULT_OK && data != null) {
                returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)!!
                val f: File = File(returnValue.get(0))
                val returnIntent = Intent()
                returnIntent.putStringArrayListExtra("listPic", returnValue)
                returnIntent.putExtra("onPhotoTaken", f.absolutePath)
                setResult(RESULT_OK, returnIntent)

                Toast.makeText(this,""+f.absolutePath,Toast.LENGTH_LONG).show()

            }
        }
    }


}
