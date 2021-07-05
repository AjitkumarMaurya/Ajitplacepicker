package com.ajit.pingplacepicker.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.transition.TransitionManager
import com.google.android.libraries.places.api.model.Place
import com.ajit.pingplacepicker.Config
import com.ajit.pingplacepicker.PingPlacePicker
import com.ajit.pingplacepicker.R
import com.ajit.pingplacepicker.helper.UrlSignerHelper
import com.ajit.pingplacepicker.inject.PingKoinComponent
import com.ajit.pingplacepicker.viewmodel.PlaceConfirmDialogViewModel
import com.ajit.pingplacepicker.viewmodel.Resource
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class PlaceConfirmDialogFragment : AppCompatDialogFragment(), PingKoinComponent {

    companion object {

        private const val TAG = "Ping#PlaceConfirmDialog"
        private const val ARG_PLACE = "arg_place"

        fun newInstance(
            place: Place,
            listener: OnPlaceConfirmedListener
        ): PlaceConfirmDialogFragment {

            val args = Bundle()
            args.putParcelable(ARG_PLACE, place)

            return PlaceConfirmDialogFragment().apply {
                arguments = args
                confirmListener = listener
            }
        }
    }

    var confirmListener: OnPlaceConfirmedListener? = null

    private val viewModel: PlaceConfirmDialogViewModel by viewModel()

    private lateinit var place: Place
    private lateinit var tvPlaceName: TextView
    private lateinit var tvPlaceAddress: TextView
    private lateinit var ivPlaceMap: ImageView
    private lateinit var ivPlacePhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check mandatory parameters for this fragment
        if (requireArguments().getParcelable<Place>(ARG_PLACE) == null) {
            throw IllegalArgumentException("You must pass a Place as argument to this fragment")
        }

        arguments?.run {
            place = getParcelable(ARG_PLACE)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle(R.string.picker_place_confirm)
            .setView(getContentView(requireContext()))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                confirmListener?.onPlaceConfirmed(place)
                dismiss()
            }
            .setNegativeButton(R.string.picker_place_confirm_cancel) { _, _ ->
                // Just dismiss here...
                dismiss()
            }

        return builder.create()
    }

    @SuppressLint("InflateParams")
    private fun getContentView(context: Context): View {

        val content = LayoutInflater.from(context)
            .inflate(R.layout.fragment_dialog_place_confirm, null)


        tvPlaceName = content.findViewById(R.id.tvPlaceName)
        tvPlaceAddress = content.findViewById(R.id.tvPlaceAddress)
        ivPlaceMap = content.findViewById(R.id.ivPlaceMap)
        ivPlacePhoto = content.findViewById(R.id.ivPlacePhoto)


        if (place.name.isNullOrEmpty()) {
            tvPlaceName.isVisible = false
        } else {
            tvPlaceName.text = place.name
        }

        tvPlaceAddress.text = place.address

        fetchPlaceMap(content)
        fetchPlacePhoto(content)

        return content
    }

    private fun fetchPlaceMap(contentView: View) {

        if (resources.getBoolean(R.bool.show_confirmation_map)) {
            val staticMapUrl = getFinalMapUrl()
            Picasso.get().load(staticMapUrl).into(ivPlaceMap, object : Callback {

                override fun onSuccess() {
                    ivPlaceMap.visibility = View.VISIBLE
                }

                override fun onError(e: Exception?) {
                    Log.e(TAG, "Error loading map image", e)
                    ivPlaceMap.visibility = View.GONE
                }
            })
        } else {
            ivPlaceMap.visibility = View.GONE
        }
    }

    private fun fetchPlacePhoto(contentView: View) {

        val photoMetadatas = place.photoMetadatas

        if (resources.getBoolean(R.bool.show_confirmation_photo)
            && photoMetadatas != null
            && photoMetadatas.isNotEmpty()
        ) {
            val photoMetadata = photoMetadatas[0]
            viewModel.getPlacePhoto(photoMetadata).observe(this,
                Observer { handlePlacePhotoLoaded(contentView, it) })
        } else {
            handlePlacePhotoLoaded(contentView, Resource.noData())
        }
    }

    private fun getFinalMapUrl(): String {

        var mapUrl = Config.STATIC_MAP_URL
            .format(
                place.latLng?.latitude,
                place.latLng?.longitude,
                PingPlacePicker.mapsApiKey,
                Locale.getDefault().language
            )

        if (UiUtils.isNightModeEnabled(requireContext())) {
            mapUrl += Config.STATIC_MAP_URL_STYLE_DARK
        }

        if (PingPlacePicker.urlSigningSecret.isNotEmpty()) {
            // Sign the URL
            return UrlSignerHelper.signUrl(mapUrl, PingPlacePicker.urlSigningSecret)
        }

        return mapUrl
    }

    private fun handlePlacePhotoLoaded(contentView: View, result: Resource<Bitmap>) {
        if (result.status == Resource.Status.SUCCESS) {
            TransitionManager.beginDelayedTransition(contentView as ViewGroup)
            ivPlacePhoto.visibility = View.VISIBLE
            ivPlacePhoto.setImageBitmap(result.data)
        } else {
            ivPlacePhoto.visibility = View.GONE
        }
    }

    /**
     * Listener called when a place is updated.
     */
    interface OnPlaceConfirmedListener {
        fun onPlaceConfirmed(place: Place)
    }
}
