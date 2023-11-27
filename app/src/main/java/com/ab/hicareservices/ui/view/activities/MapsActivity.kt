package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.ab.hicareservices.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ab.hicareservices.databinding.ActivityMapsBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mapView:MapView
    private  var mCurrLocationMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiKey = "AIzaSyDxWpNjeNRl8_62bKRfS57AzgcvDZ-DxXM"
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        mapView = findViewById<MapView>(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            // Use googleMap object here after initialization
        }

        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        autocompleteFragment?.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Handle selected place
                Log.i("TAG", "Place: ${place.name}, ${place.id}, ${place.latLng}")
                // Update map with selected place's location
                val location = place.latLng

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker!!.remove()
                }
                val markerOptions = MarkerOptions()
                markerOptions.position(location)
                markerOptions.title("Current Position")
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                mCurrLocationMarker = mMap.addMarker(markerOptions)
                // Initialize mMap and move the camera to the selected location
                if (::mMap.isInitialized) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

                }
            }

            override fun onError(status: Status) {
                // Handle error
                Log.e("TAG", "Error: ${status.statusMessage}")
            }
        })
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                // Perform place search here using Places API
//                // Update map marker with the retrieved location
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                // Handle search suggestions or live updates here
//                return false
//            }
//        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Initialize map settings and default location
        // mMap.moveCamera(...)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

}