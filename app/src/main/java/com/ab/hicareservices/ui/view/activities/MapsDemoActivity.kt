package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Filter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.orders.Locality
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.UserData
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Context
import com.ab.hicareservices.location.MyLocationListener
import com.google.android.libraries.places.api.Places

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapsDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var progressDialog: ProgressDialog

    private var mMap: GoogleMap? = null
    private var initialLocationFetched = false
    private lateinit var placesClient: PlacesClient
    private lateinit var autoCompleteTextView: AutoCompleteTextView


    private lateinit var cardView: CardView
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var btnChangeLoc: TextView
    private lateinit var addressTextView: TextView
    private lateinit var tvAddressdetail: TextView
    private lateinit var btnNext: Button
    private lateinit var locationCallback: LocationCallback
    private lateinit var lat: String
    private lateinit var longg: String
    private var ServiceCenter_Id = ""
    private var SkillId = ""
    private var SlotDate = ""
    private var TaskId = ""
    private var Latt = ""
    private var Longg = ""
    private var ServiceType = ""
    private var Pincode = ""
    private var Service_Code = ""
    private var Unit = ""
    private var spcode = ""
    private lateinit var marker: Marker
    val userData = UserData()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        try {
            progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
            progressDialog.setCancelable(false)
            Places.initialize(applicationContext, "AIzaSyDxWpNjeNRl8_62bKRfS57AzgcvDZ-DxXM")
            placesClient = Places.createClient(this)


            ServiceCenter_Id = intent.getStringExtra("ServiceCenter_Id").toString()
            SlotDate = intent.getStringExtra("SlotDate").toString()
            TaskId = intent.getStringExtra("TaskId").toString()
            SkillId = intent.getStringExtra("SkillId").toString()
            Latt = intent.getStringExtra("Lat").toString()
            Longg = intent.getStringExtra("Long").toString()
            ServiceType = intent.getStringExtra("ServiceType").toString()
            Pincode = intent.getStringExtra("Pincode").toString()
            Service_Code = intent.getStringExtra("Service_Code").toString()
            Unit = intent.getStringExtra("Unit").toString()
            spcode = intent.getStringExtra("SPCode").toString()


            userData.ServiceArea = "Home Type"
            userData.LeadSource = "MobileApp"


            btnChangeLoc = findViewById(R.id.btnChangeLoc)
            tvAddressdetail = findViewById(R.id.tvAddressdetail)
            btnNext = findViewById(R.id.btnNext)
            cardView = findViewById(R.id.cardView)
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

//            locationCallback = object : LocationCallback() {
//                override fun onLocationResult(locationResult: LocationResult) {
//                    super.onLocationResult(locationResult)
//                    locationResult ?: return
//                    for (location in locationResult.locations) {
//
////                        getAddressFromLocation(location.latitude,location.longitude)
//                        // Update the map and address information here with the received location
//                        updateMapAndAddress(location)
//                    }
//                }
//            }


            autoCompleteTextView = findViewById(R.id.autoCompleteTextView)

            val autocompleteSessionToken = AutocompleteSessionToken.newInstance()
            val placeAutocompleteAdapter =
                PlaceAutocompleteAdapter(this, placesClient, autocompleteSessionToken)
            autoCompleteTextView.setAdapter(placeAutocompleteAdapter)

            // Set a listener for item click on the AutoCompleteTextView
            // Inside your AutoCompleteTextView's item click listener
            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                val item = placeAutocompleteAdapter.getItem(position)
                val placeId = item?.placeId
                val placeDescription = placeAutocompleteAdapter.getPlaceDescription(position)

                // Set the full description in the AutoCompleteTextView
                autoCompleteTextView.setText(placeDescription)

                // Use the placeId to fetch details about the selected place
                val placeFields = listOf(Place.Field.LAT_LNG) // Include required fields
                val request = FetchPlaceRequest.newInstance(placeId!!, placeFields)

                placesClient.fetchPlace(request).addOnSuccessListener { response ->
                    val place = response.place

                    // Extract the LatLng object from the fetched details
                    val latLng = place.latLng

                    // Update the marker position on the map with the fetched coordinates
                    latLng?.let {
                        updateMarkerPosition(it.latitude, it.longitude)

                        // Move the camera to the selected location
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                    }

                    // Other actions with the selected place details
                    // ...
                }.addOnFailureListener { exception ->
                    // Handle failure in fetching place details
                }
            }

//            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
//                val item = placeAutocompleteAdapter.getItem(position)
//                val placeId = item?.placeId
//                val placeDescription = placeAutocompleteAdapter.getPlaceDescription(position)
//
//                // Set the full description in the AutoCompleteTextView
//                autoCompleteTextView.setText(placeDescription)
//
//                // Use the placeId to fetch details about the selected place
//                val placeFields = listOf(Place.Field.LAT_LNG) // Include required fields
//                val request = FetchPlaceRequest.newInstance(placeId!!, placeFields)
//
//                placesClient.fetchPlace(request).addOnSuccessListener { response ->
//                    val place = response.place
//
//                    // Extract the LatLng object from the fetched details
//                    val latLng = place.latLng
//
//                    // Update the marker position on the map with the fetched coordinates
//                    latLng?.let {
//                        updateMarkerPosition(it.latitude, it.longitude)
//                    }
//
//                    // Other actions with the selected place details
//                    // ...
//                }.addOnFailureListener { exception ->
//                    // Handle failure in fetching place details
//                }
//            }
            btnNext.setOnClickListener {
                userData.Pincode = AppUtils2.pincode
                userData.ServiceType = "Pest"
                val intent = Intent(this, AddAddressActivity::class.java)
                intent.putExtra("ServiceCenter_Id", "")
                intent.putExtra("SlotDate", "")
                intent.putExtra("TaskId", "")
                intent.putExtra("SkillId", "")
                intent.putExtra("Latt", lat)
                intent.putExtra("Longg", longg)
                intent.putExtra("ServiceType", "pest")
                intent.putExtra("Pincode", AppUtils2.pincode)
                intent.putExtra("Service_Code", AppUtils2.servicecode)
                intent.putExtra("Unit", Unit)
                intent.putExtra("SPCode", spcode)

                startActivity(intent)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mMap = googleMap

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap!!.isMyLocationEnabled = true
                startLocationUpdates()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
            mMap?.uiSettings?.apply {
                isZoomControlsEnabled = true // Enable zoom controls
                isZoomGesturesEnabled = true // Enable zoom gestures
                isScrollGesturesEnabledDuringRotateOrZoom = true // Enable scroll gestures
                isTiltGesturesEnabled = true // Enable tilt gestures
                isRotateGesturesEnabled = true // Enable rotation gestures
            }
            MyLocationListener(this)

            val defaultPosition = LatLng(AppUtils2.Latt.toDouble(), AppUtils2.Longg.toDouble()) // New York City coordinates
            marker = mMap?.addMarker(MarkerOptions().position(defaultPosition).draggable(true))!!

            // Set marker drag listener to update position as the marker is moved


            // Set camera move listener to update marker position as the map moves
            mMap?.setOnCameraMoveListener {
                // Update marker position when the camera moves
                marker?.position = mMap?.cameraPosition?.target!!
                // Get updated marker position and coordinates
                val newPosition = marker?.position
                val updatedLat = newPosition?.latitude
                val updatedLng = newPosition?.longitude
//                getAddressFromLocation(updatedLat, updatedLng)
                updateMarkerPosition(updatedLat, updatedLng)

                // Perform actions with the updated coordinates
                // For example, update UI elements with the new coordinates
            }
            mMap?.uiSettings?.isScrollGesturesEnabled = true

            mMap?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDragStart(p0: Marker) {
                    // Optional: Perform actions when marker drag starts
                }

                override fun onMarkerDrag(marker: Marker) {
                    val newPosition = marker.position
                    val updatedLat = newPosition.latitude
                    val updatedLng = newPosition.longitude

                    // Update your variables or perform actions with the updated coordinates
                    // For example, you can update lat and long variables and fetch location details
                    lat = updatedLat.toString()
                    longg = updatedLng.toString()
//                    getAddressFromLocation(updatedLat, updatedLng)
                    updateMarkerPosition(updatedLat, updatedLng)
                    val latLng = LatLng(updatedLat, updatedLng)
                    // Optional: Perform actions as the marker is dragged
                }

                override fun onMarkerDragEnd(marker: Marker) {


//                    if (marker == null) {
//                        val markerOptions = MarkerOptions()
//                            .position(latLng)
//                            .title("Current Position")
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//
//                        this@MapsDemoActivity.marker = mMap?.addMarker(markerOptions)!!
//                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//
//                    } else {
//                        marker?.position = latLng
//                    }
                }

            })
            mMap?.setOnCameraMoveListener {
                if (initialLocationFetched) {
                    val newPosition = mMap?.cameraPosition?.target
                    newPosition?.let {
                        updateMarkerPosition(it.latitude, it.longitude)
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun updateMarkerPosition(latitude: Double, longitude: Double) {
        // ... Your existing code ...
        lat = latitude.toString()
        longg = longitude.toString()
//        getAddressFromLocation(latitude, longitude)
        try {
//            progressDialog.show()

            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val street = address.thoroughfare // Street name
                val state = address.adminArea // State name
                val postalCode = address.postalCode // Postal code
                val nearbyArea = address.subLocality ?: address.locality // Nearby area or locality
                val companyName = address.featureName // Nearby company name or business name
                val landmark = address.featureName // Landmark
                val locality = address.subLocality // Locality
                val buildingName = address.featureName // Building name
                val industryArea = address.subAdminArea ?: address.locality // Industry area

                val addressDetails = StringBuilder()


                locality?.let {
                    addressDetails.append("Locality: $it\n")
                }
                buildingName?.let {
                    addressDetails.append("Building Name: $it\n")
                }
                state?.let {
                    addressDetails.append("State: $it\n")
                }
                postalCode?.let {
                    addressDetails.append("Postal Code: $it\n")
                }
                nearbyArea?.let {
                    addressDetails.append("Nearby Area: $it\n")
                }
                companyName?.let {
                    addressDetails.append("Company Name: $it\n")
                }
                landmark?.let {
                    addressDetails.append("Landmark: $it\n")
                }
//                addressTextView.text = industryArea
                tvAddressdetail.text = "$nearbyArea, $state, India"
                if (tvAddressdetail.text.isNotEmpty() && tvAddressdetail.text.isNotEmpty()) {
                    cardView.visibility = View.VISIBLE
                    progressDialog.dismiss()
                } else {
                    cardView.visibility = View.GONE
                    progressDialog.dismiss()


                }

                progressDialog.dismiss()

            }
        } catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()

        }

        val latLng = LatLng(latitude, longitude)

        val customMarkerView = layoutInflater.inflate(R.layout.custom_marker_layout, null)
        val markerTextView = customMarkerView.findViewById<TextView>(R.id.markerTextView)
        markerTextView.text = "Your service will be delivered here!" // Update text dynamically

        val markerOptions = MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(customMarkerView)))
        marker?.remove() // Remove old marker
        marker = mMap?.addMarker(markerOptions)!!
        progressDialog.dismiss()

    }

    private fun createDrawableFromView(view: View): Bitmap {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    //    private fun updateMarkerPosition(latitude: Double, longitude: Double) {
//        // Update the marker position and fetch the address information
//        lat = latitude.toString()
//        longg = longitude.toString()
//        getAddressFromLocation(latitude, longitude)
//        val latLng = LatLng(latitude, longitude)
//
//        if (marker == null) {
//            val markerOptions = MarkerOptions()
//                .position(latLng)
//                .title("Current Position")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//
//            marker = mMap?.addMarker(markerOptions)!!
//        } else {
//            marker?.position = latLng
//        }
//    }
    private fun startLocationUpdates() {
        try {
            val locationRequest = LocationRequest.create().apply {
                interval = 1000
                fastestInterval = 1000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mFusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {

    }

//    private fun updateMapAndAddress(location: Location) {
//        try {
////            progressDialog.show()
//
//            val latLng = LatLng(location.latitude, location.longitude)
//
//            if (marker == null) {
//                val markerOptions = MarkerOptions()
//                    .position(latLng)
//                    .title("Current Position")
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//
//                marker = mMap?.addMarker(markerOptions)!!
//            } else {
//                marker?.position = latLng
//            }
//
//            getAddressFromLocation(latLng.latitude, latLng.longitude)
//            lat = latLng.latitude.toString()
//            longg = latLng.longitude.toString()
//            AppUtils2.Latt = latLng.latitude.toString()
//            AppUtils2.Longg = latLng.longitude.toString()
//            userData.Lat = AppUtils2.Latt
//            userData.Long = AppUtils2.Longg
//            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//
//
////            progressDialog.dismiss()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mMap!!.isMyLocationEnabled = true
                    startLocationUpdates()
                }
            } else {
                Toast.makeText(
                    this,
                    "Location permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private fun fetchInitialLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    updateMarkerPosition(it.latitude, it.longitude)
//                    updateMapAndAddress(it)

                    initialLocationFetched = true
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (!initialLocationFetched) {
            fetchInitialLocation()
        }
    }
}

class PlaceAutocompleteAdapter(
    context: Context,
    private val placesClient: PlacesClient,
    private val token: AutocompleteSessionToken
) : ArrayAdapter<AutocompletePrediction>(context, android.R.layout.simple_dropdown_item_1line) {

    private val resultList = mutableListOf<AutocompletePrediction>()

    override fun getCount(): Int {
        return resultList.size
    }

    override fun getItem(position: Int): AutocompletePrediction? {
        return resultList.getOrNull(position)
    }

    // Add a new method to get the full description of the place
    fun getPlaceDescription(position: Int): String {
        return resultList.getOrNull(position)?.getFullText(null)?.toString() ?: ""
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                constraint?.let {
                    val scope = CoroutineScope(Dispatchers.IO)
                    scope.launch {
                        val predictions = getAutocomplete(constraint)
                        withContext(Dispatchers.Main) {
                            filterResults.values = predictions
                            filterResults.count = predictions.size
                            publishResults(constraint, filterResults)
                        }
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let {
                    resultList.clear()
                    if (results.count > 0) {
                        val predictions = results.values as List<AutocompletePrediction>
                        resultList.addAll(predictions)
                        notifyDataSetChanged()
                    } else {
                        notifyDataSetInvalidated()
                    }
                }
            }
        }
    }

    private suspend fun getAutocomplete(constraint: CharSequence): List<AutocompletePrediction> {
        return withContext(Dispatchers.IO) {
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build()

            try {
                val response = placesClient.findAutocompletePredictions(request).await()

                if (response != null && response.autocompletePredictions != null) {
                    return@withContext response.autocompletePredictions
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return@withContext emptyList()
        }
    }
}
