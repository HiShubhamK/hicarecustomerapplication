package com.ab.hicareservices.ui.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ab.hicareservices.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: GoogleMap
    var mapFragment: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment!!.getMapAsync(this@MapActivity)

//        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
//        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapView = googleMap

        // Add a marker at a specific location
        val location = LatLng(19.7749, 72.4194)  // Example: San Francisco coordinates
        mapView.addMarker(MarkerOptions().position(location).title("Marker in San Francisco"))
        mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }
}