/*
  Copyright 2025 Adobe. All rights reserved.
  This file is licensed to you under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software distributed under
  the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
  OF ANY KIND, either express or implied. See the License for the specific language
  governing permissions and limitations under the License.
*/

package com.adobe.luma.tutorial.android.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.adobe.luma.tutorial.android.MainActivity
import com.adobe.luma.tutorial.android.R
import com.adobe.luma.tutorial.android.models.Beacon
import com.adobe.luma.tutorial.android.models.MobileSDK
import com.adobe.marketing.mobile.places.PlacesPOI
import com.adobe.marketing.mobile.services.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

class LocationManager(context: Context) {
    val context: Context = context
    var mapCenter = LatLng(52.37109, 4.8919)
    var zoom = 2.0f
    var pointsOfInterest = mutableStateListOf<PlacesPOI>()

    val currentLocation: MutableLiveData<Location?> = MutableLiveData()
    var beacons: MutableLiveData<List<Beacon>> = MutableLiveData(listOf())
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private lateinit var locationCallback: LocationCallback

    init {
        requestLocation()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation(): MutableLiveData<Location?>? {
        if (!trackLocationPermissionGranted()) {
            return null
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                currentLocation.value = locationResult.lastLocation
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        return currentLocation
    }

    suspend fun setMapCenter() {
        pointsOfInterest.clear()
        val location = Location("current").apply {
            latitude = mapCenter.latitude
            longitude = mapCenter.longitude
            accuracy = 100f
        }

        location.let {
            pointsOfInterest.addAll(MobileSDK.shared.getNearbyPointsOfInterest(location))
        }
    }


    @SuppressLint("MissingPermission")
    fun startScanning() {
        beacons.value?.forEach { beacon ->
            val geocoder =
                Geocoder(context, Locale.getDefault()).getFromLocationName(beacon.location, 1)
            val latitude = geocoder?.firstOrNull()?.latitude
            val longitude = geocoder?.firstOrNull()?.longitude

            val geofence = latitude?.let {
                longitude?.let {
                    Geofence.Builder()
                        .setRequestId(beacon.identifier)
                        .setCircularRegion(latitude, longitude, 100f)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build()
                }
            }
            val geofencingRequest = geofence?.let {
                GeofencingRequest.Builder()
                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                    .addGeofence(it)
                    .build()
            }

            if (!trackLocationPermissionGranted()) {
                return
            }
            if (geofencingRequest != null) {
                geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                    .addOnCompleteListener({
                        if (it.isSuccessful) {
                            Log.debug(
                                "LocationManager",
                                "startScanning",
                                "Geofence added successfully"
                            )
                        } else {
                            Log.debug("LocationManager", "startScanning", "Failed to add geofence")
                        }
                    })
            }
        }
    }

    private fun trackLocationPermissionGranted(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun getGeofencePendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun updateIBeacon(uuid: String, major: Int, minor: Int, distance: Int) {
        val beaconStatus: String
        val beaconSymbol: Int

        when (distance) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                beaconStatus = "immediate"
                beaconSymbol = R.drawable.ic_filled_square
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                beaconStatus = "far"
                beaconSymbol = R.drawable.ic_star_fill
            }

            else -> {
                beaconStatus = "unknown"
                beaconSymbol = R.drawable.ic_location_circle_fill
            }
        }

        beacons.value = beacons.value?.map { beacon ->
            if (beacon.uuid == uuid && beacon.major == major && beacon.minor == minor) {
                beacon.copy(status = beaconStatus, symbol = beaconSymbol.toString())
            } else {
                beacon
            }
        }
    }

    fun convertBeaconToLatLng(context: Context, beacon: Beacon): LatLng? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addressList = geocoder.getFromLocationName(beacon.location, 1)
        if (addressList != null && addressList.isNotEmpty()) {
            val address = addressList[0]
            return LatLng(address.latitude, address.longitude)
        }
        return null
    }
}