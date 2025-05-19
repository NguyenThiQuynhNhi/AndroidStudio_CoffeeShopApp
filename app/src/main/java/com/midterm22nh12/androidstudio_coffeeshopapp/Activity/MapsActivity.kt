package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityMapsBinding
import java.util.Locale

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        val defaultLocation = LatLng(16.047079, 108.206230)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))

        mMap.setOnMapClickListener { latLng ->
            mMap.clear()

            val address = getAddressFromLatLng(latLng.latitude, latLng.longitude)

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
                    .snippet(address)
            )?.showInfoWindow()

            val resultIntent = Intent().apply {
                putExtra("lat", latLng.latitude)
                putExtra("lon", latLng.longitude)
                putExtra("address", address)
            }

            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun getAddressFromLatLng(lat: Double, lon: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                buildString {
                    addr.featureName?.let { append(it).append(", ") }
                    addr.thoroughfare?.let { append(it).append(", ") }
                    addr.subLocality?.let { append(it).append(", ") }
                    addr.locality?.let { append(it).append(", ") }
                    addr.adminArea?.let { append(it).append(", ") }
                    addr.countryName?.let { append(it) }
                }
            } else {
                "Cannot get address"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Cannot get address"
        }
    }
}
