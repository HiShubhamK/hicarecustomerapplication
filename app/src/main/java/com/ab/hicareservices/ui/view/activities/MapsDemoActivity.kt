package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
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
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsDemoActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private lateinit var cardView: CardView
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var addressTextView: TextView
    private lateinit var tvAddressdetail: TextView
    private lateinit var btnNext: Button
    private lateinit var locationCallback: LocationCallback
    private lateinit var lat:String
    private lateinit var longg:String
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
    val userData = UserData()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

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


        userData.ServiceArea="Home Type"
        userData.LeadSource="MobileApp"


        addressTextView = findViewById(R.id.addressTextView)
        tvAddressdetail = findViewById(R.id.tvAddressdetail)
        btnNext = findViewById(R.id.btnNext)
        cardView = findViewById(R.id.cardView)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update the map and address information here with the received location
                    updateMapAndAddress(location)
                }
            }
        }
        btnNext.setOnClickListener {
            userData.Pincode=AppUtils2.pincode
            userData.ServiceType="Pest"
            val intent=Intent(this,ServicesAddresslistActivity::class.java)
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


    }

    override fun onMapReady(googleMap: GoogleMap) {
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

        mMap!!.setOnMapLongClickListener { latLng ->
        }
    }

    private fun startLocationUpdates() {
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
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
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
            addressTextView.text = industryArea
            tvAddressdetail.text = "$nearbyArea, $state, India"
            if (addressTextView.text.isNotEmpty()&&tvAddressdetail.text.isNotEmpty()){
                cardView.visibility= View.VISIBLE
            }else{
                cardView.visibility= View.GONE

            }

        }
    }

    private fun updateMapAndAddress(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("Current Position")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        mMap?.apply {
            clear()
            addMarker(markerOptions)
            getAddressFromLocation(latLng.latitude, latLng.longitude)
            lat=latLng.latitude.toString()
            longg=latLng.longitude.toString()
            AppUtils2.Latt=latLng.latitude.toString()
            AppUtils2.Longg=latLng.longitude.toString()
            userData.Lat=AppUtils2.Latt
            userData.Long= AppUtils2.Longg
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        }
    }

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
}
