package com.ajit.pingplacepicker.repository.googlemaps

import android.location.Location
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.*
import kotlin.math.absoluteValue

/**
 * Place without any additional info. Just latitude and longitude.
 */
internal class PlaceFromCoordinates(
    private val latitude: Double,
    private val longitude: Double
) : Place() {

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun getUserRatingsTotal(): Int? {
        return null
    }

    /**
     * Default value only.
     * Clients shouldn't rely on this.
     */
    override fun getBusinessStatus(): BusinessStatus? {
        return BusinessStatus.OPERATIONAL
    }

    override fun getName(): String? {
        return "${latitude}, $longitude"
    }

    override fun getOpeningHours(): OpeningHours? {
        return null
    }

    override fun getCurbsidePickup(): BooleanPlaceAttributeValue? {
        return null

    }

    override fun getDelivery(): BooleanPlaceAttributeValue? {
        return null
    }

    override fun getDineIn(): BooleanPlaceAttributeValue? {
        return null
    }

    override fun getReservable(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getServesBeer(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getServesBreakfast(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getServesBrunch(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getServesDinner(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getServesLunch(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getServesVegetarianFood(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getServesWine(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getTakeout(): BooleanPlaceAttributeValue? {
        return null
    }

    override fun getWheelchairAccessibleEntrance(): BooleanPlaceAttributeValue {
        TODO("Not yet implemented")
    }

    override fun getId(): String? {
        return null
    }

    override fun getPhotoMetadatas(): MutableList<PhotoMetadata> {
        return mutableListOf()
    }

    override fun getSecondaryOpeningHours(): MutableList<OpeningHours>? {
        TODO("Not yet implemented")
    }

    override fun getWebsiteUri(): Uri? {
        return null
    }

    override fun getPhoneNumber(): String? {
        return null
    }

    override fun getRating(): Double? {
        return null
    }

    override fun getIconBackgroundColor(): Int? {
        TODO("Not yet implemented")
    }

    override fun getPriceLevel(): Int? {
        return null
    }

    override fun getAddressComponents(): AddressComponents? {
        return null
    }

    override fun getCurrentOpeningHours(): OpeningHours? {
        TODO("Not yet implemented")
    }

    override fun getAttributions(): MutableList<String> {
        return mutableListOf()
    }

    override fun getAddress(): String? {
        return null
    }

    override fun getEditorialSummary(): String? {
        TODO("Not yet implemented")
    }

    override fun getEditorialSummaryLanguageCode(): String? {
        TODO("Not yet implemented")
    }

    override fun getIconUrl(): String? {
        TODO("Not yet implemented")
    }


    override fun getPlusCode(): PlusCode? {
        return null
    }

    override fun getUtcOffsetMinutes(): Int? {
        return null
    }

    override fun getTypes(): MutableList<Type> {
        return mutableListOf()
    }

    override fun getViewport(): LatLngBounds? {
        return null
    }

    override fun getLatLng(): LatLng? {
        return LatLng(latitude, longitude)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceFromCoordinates> {
        override fun createFromParcel(parcel: Parcel): PlaceFromCoordinates {
            return PlaceFromCoordinates(parcel)
        }

        override fun newArray(size: Int): Array<PlaceFromCoordinates?> {
            return arrayOfNulls(size)
        }
    }

    // formatting methods -----------------------------------------------------------------

    private fun formatLatitude(latitude: Double): String {
        val direction = if (latitude > 0) "N" else "S"
        return "${
            replaceDelimiters(
                Location.convert(
                    latitude.absoluteValue,
                    Location.FORMAT_SECONDS
                )
            )
        } $direction"
    }

    private fun formatLongitude(longitude: Double): String {
        val direction = if (longitude > 0) "W" else "E"
        return "${
            replaceDelimiters(
                Location.convert(
                    longitude.absoluteValue,
                    Location.FORMAT_SECONDS
                )
            )
        } $direction"
    }

    private fun replaceDelimiters(original: String): String {

        val parts: List<String> = original.split(":")

        val degrees: String = parts[0]
        val minutes: String = parts[1]
        var seconds: String = parts[2]

        val idx = seconds.indexOfAny(charArrayOf(',', '.'))
        if (idx >= 0) {
            seconds = seconds.substring(0, idx)
        }

        return "${degrees}° ${minutes}' ${seconds}\""
    }
}