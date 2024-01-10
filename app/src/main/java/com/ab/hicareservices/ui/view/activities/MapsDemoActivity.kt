package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.viewmodel.PlaceApi
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import com.ab.hicareservices.utils.UserData
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.*
import java.util.*

class MapsDemoActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var progressDialog: ProgressDialog
    private var markerUpdateJob: Job? = null
    private val markerUpdateDelay = 1000L // Adjust the delay as needed

    private var mMap: GoogleMap? = null
    private var initialLocationFetched = false
    private var isSuccess = true
    private var _stopBWasNotPressed = true
    private var lastUpdatedPosition: LatLng? = null


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

    private var launcher=  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
//            Toast.makeText(this,"Hello akshay",Toast.LENGTH_SHORT).show()
        } else {
            AppUtils2.ISChecklocationpermission=true
//            Toast.makeText(this,"Hello akshay fails ",Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        try {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                // When permission is granted
                // Call method
//            getCurrentLocations()

            } else {
                enableLoc()

                // When permission is not granted
                // Call method
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.POST_NOTIFICATIONS
                        ),
                        100
                    )
                }
            }










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
            val clearButton = findViewById<ImageView>(R.id.clearButton)

            clearButton.setOnClickListener {
                autoCompleteTextView.text.clear()
            }
            var btnCurrentLocation = findViewById<CardView>(R.id.btnCurrentLocation)
            btnCurrentLocation.setOnClickListener {
                fetchInitialLocation()
            }



            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationResult ?: return
                    for (location in locationResult.locations) {

//                        getAddressFromLocation(location.latitude,location.longitude)
                        // Update the map and address information here with the received location
                        updateMarkerPosition(location.latitude, location.longitude)
                    }
                }
            }


            autoCompleteTextView = findViewById(R.id.autoCompleteTextView)

            val autocompleteSessionToken = AutocompleteSessionToken.newInstance()
            val placeAutocompleteAdapter =
                PlaceAutocompleteAdapter(this@MapsDemoActivity, android.R.layout.simple_list_item_1)
            autoCompleteTextView.setAdapter(placeAutocompleteAdapter)

            // Set a listener for item click on the AutoCompleteTextView
            // Inside your AutoCompleteTextView's item click listener
            autoCompleteTextView.onItemClickListener =
                OnItemClickListener { parent, view, position, id ->
                    Log.d("Address : ", autoCompleteTextView.text.toString())
                    val latLng: LatLng? = getLatLngFromAddress(autoCompleteTextView.text.toString())
                    if (latLng != null) {
                        Log.d("Lat Lng : ", " " + latLng.latitude + " " + latLng.longitude)
                        val address: Address? = getAddressFromLatLng(latLng)
                        if (address != null) {
                            updateMarkerPosition(latLng.latitude, latLng.longitude)
                            val imm =
                                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            // Check if there's a focused view before hiding the keyboard
                            if (this is Activity && this.currentFocus != null) {
                                imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
                            }


                            // Handle the address when it's not null
                            Log.d("Address : ", "" + address.toString())

                            // Other operations with the address
                        } else {
                            Log.d("Address", "Address Not Found")
                        }
                    } else {
                        Log.d("Lat Lng", "Lat Lng Not Found")
                    }
                }
//            autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
//                val item = placeAutocompleteAdapter.getItem(position)
////                val placeId = item?.placeId
////                val placeDescription = placeAutocompleteAdapter.getPlaceDescription(position)
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
//
//                        // Move the camera to the selected location
//                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
//                    }
//
//                    // Other actions with the selected place details
//                    // ...
//                }.addOnFailureListener { exception ->
//                    // Handle failure in fetching place details
//                }
//            }

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
                intent.putExtra("Latt", AppUtils2.Latt)
                intent.putExtra("Longg", AppUtils2.Longg)
                intent.putExtra("ServiceType", "pest")
                intent.putExtra(
                    "Pincode",
                    SharedPreferenceUtil.getData(this, "Pincode", "").toString()
                )
                intent.putExtra("Service_Code", AppUtils2.servicecode)
                intent.putExtra("Unit", Unit)
                intent.putExtra("SPCode", spcode)

                startActivity(intent)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    private fun getLatLngFromAddress(address: String): LatLng? {
        val geocoder = Geocoder(this)
        val addressList: List<Address>?

        return try {
            addressList = geocoder.getFromLocationName(address, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val singleAddress = addressList[0]
                LatLng(singleAddress.latitude, singleAddress.longitude)
            } else {
                // Return null if addressList is null or empty
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Return null in case of any exception
            null
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.e("TAGDRAG", "googlemapstart")// Cleaning all the markers.

        try {
            mMap = googleMap

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap!!.isMyLocationEnabled = true
                mMap?.uiSettings?.isMyLocationButtonEnabled = false
                mMap?.setOnMyLocationButtonClickListener {
                    fetchInitialLocation()
                    // Handle the click event of the default "My Location" button
                    // Perform your custom action here
                    // Return 'false' to allow the default behavior (centering the map on the user's location)
                    // Return 'true' to prevent the default behavior
                    true // Return 'false' to allow default behavior
                }


//                startLocationUpdates()
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

            val defaultPosition = LatLng(
                AppUtils2.Latt.toDouble(),
                AppUtils2.Longg.toDouble()
            ) // default current
            marker = mMap?.addMarker(MarkerOptions().position(defaultPosition).draggable(true))!!

            // Set marker drag listener to update position as the marker is moved


            // Set camera move listener to update marker position as the map moves
//            mMap?.setOnCameraMoveListener {
//                // Update marker position when the camera moves
//                marker?.position = mMap?.cameraPosition?.target!!
//                // Get updated marker position and coordinates
//                val newPosition = marker?.position
//                val updatedLat = newPosition?.latitude
//                val updatedLng = newPosition?.longitude
////                getAddressFromLocation(updatedLat, updatedLng)
//                updateMarkerPosition(updatedLat, updatedLng)
//
//                // Perform actions with the updated coordinates
//                // For example, update UI elements with the new coordinates
//            }
//            mMap!!.setOnCameraMoveStartedListener(GoogleMap.OnCameraMoveStartedListener {
//
//            })
            mMap!!.setOnCameraMoveStartedListener(OnCameraMoveStartedListener {
//                mDragTimer.start()
//                mTimerIsRunning = true

            })
            val handler = Handler()


            mMap!!.setOnCameraChangeListener(GoogleMap.OnCameraChangeListener {
                Log.e("OnCam:", "1233")
//                marker?.position = mMap?.cameraPosition?.target!!
//                // Get updated marker position and coordinates
//                val newPosition = marker?.position
//                val updatedLat = newPosition?.latitude
//                val updatedLng = newPosition?.longitude
////                getAddressFromLocation(updatedLat, updatedLng)
//
//                updateMarkerPosition(updatedLat, updatedLng)
                if (mMap != null) {
                    // Get the new position
                    val newPosition = mMap!!.cameraPosition.target

                    // Check if the marker is null or if the position has changed
                    if (marker == null || lastUpdatedPosition == null || lastUpdatedPosition != newPosition) {
                        mMap!!.clear() // Clear the map
                        lastUpdatedPosition = newPosition // Update the last known position

                        // Create or update the marker position
                        if (marker == null) {
                            marker = mMap!!.addMarker(MarkerOptions().position(newPosition))!!
                        } else {
                            marker!!.position = newPosition
                        }

                        // Update other functionalities with the new position
                        isSuccess = false
                        val updatedLat = newPosition.latitude
                        val updatedLng = newPosition.longitude
                        if (autoCompleteTextView.text.toString().isNotEmpty()){
                            autoCompleteTextView.text.clear()
                        }

                            updateMarkerPosition(updatedLat, updatedLng)
                    }
                }
            })
            mMap?.uiSettings?.isScrollGesturesEnabled = true

            mMap?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {

                override fun onMarkerDragStart(p0: Marker) {
                    // Optional: Perform actions when marker drag starts
                    Log.e("draglog", "1 start")
                }

                override fun onMarkerDrag(marker: Marker) {
                    val newPosition = marker.position
                    val updatedLat = newPosition.latitude
                    val updatedLng = newPosition.longitude

                    lat = updatedLat.toString()
                    longg = updatedLng.toString()
                    autoCompleteTextView.text.clear()
//                    updateMarkerPosition(updatedLat, updatedLng)
                    Log.e("draglog", "2 start")

//                    markerUpdateJob?.cancel()
//                    markerUpdateJob = CoroutineScope(Dispatchers.Main).launch {
//                        delay(markerUpdateDelay)
//
//
//                    }
                    // Optional: Perform actions as the marker is dragged
                }

                override fun onMarkerDragEnd(marker: Marker) {
                    Log.e("draglog", "3 start")


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
//            mMap!!.setOnCameraIdleListener(OnCameraIdleListener { //
//                Log.e("TAGDRAG", "draglistner")// Cleaning all the markers.
////                var updatedLat = 0.0
////                var updatedLng = 0.0
////                if (mMap != null) {
////                    mMap!!.clear()
////                    marker.position = mMap!!.cameraPosition.target
////
//////                                marker?.position = mMap?.cameraPosition?.target!!
////                    // Get updated marker position and coordinates
////                    isSuccess = false
////                    val newPosition = marker.position
////                    updatedLat = newPosition.latitude
////                    updatedLng = newPosition.longitude
//////                getAddressFromLocation(updatedLat, updatedLng)
////                    updateMarkerPosition(updatedLat, updatedLng)
//                if (mMap != null) {
//                    // Get the new position
//                    val newPosition = mMap!!.cameraPosition.target
//
//                    // Check if the marker is null or if the position has changed
//                    if (marker == null || lastUpdatedPosition == null || lastUpdatedPosition != newPosition) {
//                        mMap!!.clear() // Clear the map
//                        lastUpdatedPosition = newPosition // Update the last known position
//
//                        // Create or update the marker position
//                        if (marker == null) {
//                            marker = mMap!!.addMarker(MarkerOptions().position(newPosition))!!
//                        } else {
//                            marker!!.position = newPosition
//                        }
//
//                        // Update other functionalities with the new position
//                        isSuccess = false
//                        val updatedLat = newPosition.latitude
//                        val updatedLng = newPosition.longitude
//                        updateMarkerPosition(updatedLat, updatedLng)
//                    }
//                }
//
//
//
//
//
////                mPosition = mMap!!.cameraPosition.target
////                mZoom = mMap!!.getCameraPosition().zoom
////                if (mTimerIsRunning) {
////                    mDragTimer.cancel()
////                }
//            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateMarkerPosition(latitude: Double, longitude: Double) {
        // ... Your existing code ...
        lat = latitude.toString()
        longg = longitude.toString()
        AppUtils2.Latt = lat
        AppUtils2.Longg = longg
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
                tvAddressdetail.text = "$nearbyArea, $state, India, $postalCode"
                SharedPreferenceUtil.setData(this,"Pincode","")
                Pincode = postalCode
                SharedPreferenceUtil.setData(this, "Pincode", postalCode).toString()
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
        val defaultLocation = LatLng(40.7128, -74.0060)

        val markerOptions = MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(customMarkerView)))
        marker.remove() // Remove old marker
        marker = mMap?.addMarker(markerOptions)!!
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))


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
//                    startLocationUpdates()
                }
            } else {
//                Toast.makeText(
//                    this,
//                    "Location permission denied",
//                    Toast.LENGTH_SHORT
//                ).show()
                DesignToast.makeText(
                    this,
                    "Location permission denied",
                    Toast.LENGTH_SHORT,
                    DesignToast.TYPE_ERROR
                ).show()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private fun getAddressFromLatLng(latLng: LatLng): Address? {
        lateinit var mGeocoder: Geocoder
        mGeocoder = Geocoder(this, Locale.getDefault())
        if (mGeocoder != null) {
            var postalcode: List<Address> =
                mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 5) as List<Address>
            if (postalcode != null) {
                return postalcode.get(0)
            } else {
                return null
            }
        }
        return null // Return a default value or null if conditions aren't met
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


    class PlaceAutocompleteAdapter(
        private val context: MapsDemoActivity,
        private val resource: Int
    ) : ArrayAdapter<String>(context, resource), Filterable {

        private var results: ArrayList<String>? = null
        private val placeApi: PlaceApi = PlaceApi()

        override fun getCount(): Int {
            return results?.size ?: 0
        }

        override fun getItem(position: Int): String? {
            return results?.get(position)
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val filterResults = FilterResults()
                    constraint?.let {
                        if (it.isNotEmpty()) {
                            results = placeApi.autoComplete(constraint.toString())
                            filterResults.values = results
                            filterResults.count = results?.size ?: 0
                        }
                    }
                    return filterResults
                }

                override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                    this@PlaceAutocompleteAdapter.results = results.values as? ArrayList<String>
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun enableLoc() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000
        locationRequest.fastestInterval = 5 * 1000
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(exception.status.resolution!!!!).build()
                        launcher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                    }
                }
//                when (exception.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
//                        // user a dialog.
//                        try {
//                            // Cast to a resolvable exception.
//                            val resolvable = exception as ResolvableApiException
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            resolvable.startResolutionForResult(
//                                this,
//                                REQUEST_CODE_PERMISSIONS
//                            )
//
//                        } catch (e: IntentSender.SendIntentException) {
//                            // Ignore the error.
//                        } catch (e: ClassCastException) {
//                            // Ignore, should be an impossible error.
//                        }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
//                }
            }
        }
    }


}


//class PlaceAutocompleteAdapter(
//    context: Context,
//    private val placesClient: PlacesClient,
//    private val token: AutocompleteSessionToken
//) : ArrayAdapter<AutocompletePrediction>(context, android.R.layout.simple_dropdown_item_1line) {

//    private val resultList = mutableListOf<AutocompletePrediction>()
//
//    override fun getCount(): Int {
//        return resultList.size
//    }
//
//    override fun getItem(position: Int): AutocompletePrediction? {
//        return resultList.getOrNull(position)
//    }
//
//    // Add a new method to get the full description of the place
//    fun getPlaceDescription(position: Int): String {
//        return resultList.getOrNull(position)?.getFullText(null)?.toString() ?: ""
//    }
//
//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val filterResults = FilterResults()
//                constraint?.let {
//                    val scope = CoroutineScope(Dispatchers.IO)
//                    scope.launch {
//                        val predictions = getAutocomplete(constraint)
//                        withContext(Dispatchers.Main) {
//                            filterResults.values = predictions
//                            filterResults.count = predictions.size
//                            publishResults(constraint, filterResults)
//                        }
//                    }
//                }
//                return filterResults
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                results?.let {
//                    resultList.clear()
//                    if (results.count > 0) {
//                        val predictions = results.values as List<AutocompletePrediction>
//                        resultList.addAll(predictions)
//                        notifyDataSetChanged()
//                    } else {
//                        notifyDataSetInvalidated()
//                    }
//                }
//            }
//        }
//    }
//
//    private suspend fun getAutocomplete(constraint: CharSequence): List<AutocompletePrediction> {
//        return withContext(Dispatchers.IO) {
//            val request = FindAutocompletePredictionsRequest.builder()
//                .setSessionToken(token)
//                .setQuery(constraint.toString())
//                .build()
//
//            try {
//                val response = placesClient.findAutocompletePredictions(request).await()
//
//                if (response != null && response.autocompletePredictions != null) {
//                    return@withContext response.autocompletePredictions
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//            return@withContext emptyList()
//        }
//    }
//}


