package com.ab.hicareservices.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.Address
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.utils.AppUtils2
import java.util.*
import kotlin.collections.List


@SuppressLint("MissingPermission")
class MyLocationListener(context: Context) : LocationListener {
    var latitude = 0.0
    var context: Context? = null
    var location: Location? = null
    var locationManager: LocationManager? = null
    var isGPSEnabled = false
    var isNetworkEnabled = false
    var longitude = 0.0
    lateinit var  mGeocoder:Geocoder

    init {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!
            isNetworkEnabled =
                locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!
            if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            if (isGPSEnabled) {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
                location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            if (isNetworkEnabled) {
                locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0,
                    0f,
                    this
                )
                location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            latitude = location?.latitude.toString().toDouble()
            longitude = location?.longitude.toString().toDouble()
//            AppUtils2.postalcode="Lat and long"+latitude.toString()+" "+longitude.toString()

          mGeocoder = Geocoder(context, Locale.getDefault())
            if (mGeocoder != null) {
                var postalcode:List<Address> = mGeocoder.getFromLocation(latitude, longitude, 5) as List<Address>
                if (postalcode != null && postalcode.size > 0) {
                    for (i in 0 until postalcode.size){
                        SharedPreferenceUtil.setData(context, "pincode",postalcode.get(i).postalCode.toString())
//                        Toast.makeText(context,postalcode.get(i).postalCode.toString(),Toast.LENGTH_LONG).show()
                        break
                    }
                }
            }


        } catch (e: Exception) {
            Log.d("TAG", "$e")
        }
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
//        getchangeloaction()
    }

//    private fun getchangeloaction() {
//        if (mGeocoder != null) {
//            var postalcode:List<Address> = mGeocoder.getFromLocation(latitude, longitude, 5) as List<Address>
//            if (postalcode != null && postalcode.size > 0) {
//                for (i in 0 until postalcode.size){
//                    Toast.makeText(context,postalcode.get(i).postalCode.toString(),Toast.LENGTH_LONG).show()
//                    break
//                }
//            }
//        }
//    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }


}