package com.myassociation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ajit.pingplacepicker.PingPlacePicker
import com.ajit.pingplacepicker.galleryimagepicker.ImagePicker
import com.ajit.pingplacepicker.galleryimagepicker.RedBookPresenter
import com.ajit.pingplacepicker.galleryimagepicker.bean.MimeType
import com.ajit.pingplacepicker.galleryimagepicker.data.OnImagePickCompleteListener
import com.ajit.pingsample.R
import com.google.android.libraries.places.api.model.Place
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    var waitResult: ActivityResultLauncher<Intent>? = null

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

    fun gallery(view: View) {  ImagePicker.withCrop(RedBookPresenter())
        .setMaxCount(5)
        .showCamera(true)
        .setColumnCount(4)
        .mimeTypes(MimeType.ofImage())
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



            } as OnImagePickCompleteListener?)}
    fun ping(view: View) { showPlacePicker()}

}
