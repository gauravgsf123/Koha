package com.bbb.koha.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationHelper() {
    var locationRequest:LocationRequest = LocationRequest.create()


    @SuppressLint("MissingPermission")
    fun startListeningUserLocation(context: Context, myListener: MyLocationListener) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations){
                    // Update UI with location data
                    myListener.onLocationChanged(location)
                }
            }
        }
        locationRequest = LocationRequest.create().apply {
            interval = 6000/*TimeUnit.SECONDS.toMillis(2)*/
            fastestInterval = 6000/*TimeUnit.SECONDS.toMillis(2)*/
            maxWaitTime = 6000/*TimeUnit.MINUTES.toMillis(1)*/
            // priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }
}

interface MyLocationListener {
    fun onLocationChanged(location: Location?)
}