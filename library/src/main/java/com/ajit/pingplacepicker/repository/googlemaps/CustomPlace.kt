package com.ajit.pingplacepicker.repository.googlemaps

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.model.*
class CustomPlace(
    var placeId: String?,
    var placeName: String?,
    var placePhotos: MutableList<PhotoMetadata>,
    var placeAddress: String?,
    var placeTypes: MutableList<Type>,
    var placeLatLng: LatLng?
) : Place() {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        TODO("placePhotos"),
        parcel.readString(),
        TODO("placeTypes"),
        parcel.readParcelable(LatLng::class.java.classLoader)
    ) {
    }

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
        return placeName
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

    override fun getTakeout(): BooleanPlaceAttributeValue? {
        return null
    }

    override fun getId(): String? {
        return placeId
    }

    override fun getPhotoMetadatas(): MutableList<PhotoMetadata> {
        return placePhotos
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

    override fun getAttributions(): MutableList<String> {
        return mutableListOf()
    }

    override fun getAddress(): String? {
        return placeAddress
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
        return placeTypes
    }

    override fun getViewport(): LatLngBounds? {
        return null
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(placeId)
        dest.writeString(placeName)
        dest.writeString(placeAddress)
        dest.writeParcelable(placeLatLng, flags)
    }

    override fun getLatLng(): LatLng? {
        return placeLatLng
    }

    companion object CREATOR : Parcelable.Creator<CustomPlace> {
        override fun createFromParcel(parcel: Parcel): CustomPlace {
            return CustomPlace(parcel)
        }

        override fun newArray(size: Int): Array<CustomPlace?> {
            return arrayOfNulls(size)
        }
    }
}