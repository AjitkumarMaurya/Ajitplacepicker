package com.ajit.pingplacepicker.inject

import com.google.android.libraries.places.api.Places
import com.ajit.pingplacepicker.PingPlacePicker
import com.ajit.pingplacepicker.repository.PlaceRepository
import com.ajit.pingplacepicker.repository.googlemaps.GoogleMapsAPI
import com.ajit.pingplacepicker.repository.googlemaps.GoogleMapsRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val repositoryModule = module {

    // PlacesClient
    single {
        Places.initialize(androidContext(), PingPlacePicker.androidApiKey)
        return@single Places.createClient(androidContext())
    }

    // GoogleMapsAPI
    single(createdAtStart = true) {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.NONE

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

        return@single retrofit.create(GoogleMapsAPI::class.java)
    }

    // GoogleMapsRepository
    single {
        GoogleMapsRepository(googleClient = get(), googleMapsAPI = get())
    } bind PlaceRepository::class
}