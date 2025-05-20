package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.midterm22nh12.androidstudio_coffeeshopapp.R
import com.midterm22nh12.androidstudio_coffeeshopapp.databinding.ActivityMapsBinding
import java.util.*

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val storeLocation = LatLng(16.072035, 108.149180)

    private var selectedLatLng: LatLng? = null
    private var selectedAddress: String? = null
    private var distanceKm: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 🟡 Xác nhận vị trí
        findViewById<Button>(R.id.confirmButton).setOnClickListener {
            if (selectedLatLng != null && !selectedAddress.isNullOrEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra("lat", selectedLatLng!!.latitude)
                    putExtra("lon", selectedLatLng!!.longitude)
                    putExtra("address", selectedAddress)
                    putExtra("distanceKm", distanceKm)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Please select address!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 14f))

        mMap.setOnMapClickListener { latLng ->
            mMap.clear()

            val address = getAddressFromLatLng(latLng.latitude, latLng.longitude)

            distanceKm = calculateDistanceInKm(storeLocation, latLng)

            selectedLatLng = latLng
            selectedAddress = address

            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Selected Location")
                    .snippet(address)
            )?.showInfoWindow()

            findViewById<TextView>(R.id.addressText).text =
                "$address\n📏 Distance: %.2f km".format(distanceKm)
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
                "Cannot find address"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Cannot find address"
        }
    }

    private fun calculateDistanceInKm(from: LatLng, to: LatLng): Double {
        val result = FloatArray(1)
        android.location.Location.distanceBetween(
            from.latitude, from.longitude,
            to.latitude, to.longitude,
            result
        )
        return result[0] / 1000.0
    }
}
