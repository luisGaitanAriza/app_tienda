package com.example.tienda_online_proyecto.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import java.io.IOException
import java.util.*

class LocationHelper(
    private val activity: Activity,
    private val permissionLauncher: ActivityResultLauncher<String>
) {

    init {
        // Inicializar Places API
        if (!Places.isInitialized()) {
            Places.initialize(activity.applicationContext, "TU_API_KEY")
        }
    }

    fun checkLocationPermission(textView: TextView) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation(textView)
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun getCurrentLocation(textView: TextView) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(activity, "Permiso no otorgado", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latLng = LatLng(location.latitude, location.longitude)
                getPlaceFromLocation(latLng, textView)
            } else {
                Toast.makeText(activity, "No se pudo obtener ubicación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPlaceFromLocation(latLng: LatLng, textView: TextView) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0].getAddressLine(0)
                textView.text = address
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
