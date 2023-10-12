package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.product.ProductListResponseData
import com.ab.hicareservices.databinding.FragmentProductBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.LocationPermissionManager
import java.text.SimpleDateFormat
import java.util.Date
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.Locale

class ProductFragment : Fragment(),LocationListener {

    private lateinit var binding: FragmentProductBinding
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = ""
    var pincode: String? = ""
    private lateinit var mAdapter: ProductAdapter
    lateinit var progressDialog: ProgressDialog
    var client: FusedLocationProviderClient? = null
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""
    var latitude = 0.0
    var location: Location? = null
    var locationManager: LocationManager? = null
    var isGPSEnabled = false
    var isNetworkEnabled = false
    var longitude = 0.0
    lateinit var  mGeocoder:Geocoder


    private var launcher=  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {

            if (!LocationPermissionManager.checkLocationPermission(requireActivity())) {
                // Request the permission
                LocationPermissionManager.requestLocationPermission(requireActivity())
            } else {
                // Call this function to get postal code
                MyLocationListener(requireActivity())
                // Permission already granted, proceed with your location-related tasks
                // ...
            }


        } else {
//            requireContext().toast("Please Accept Location enable for use this App.")
        }
    }

    private fun MyLocationListenerclass() {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            try {
                client!!.lastLocation.addOnCompleteListener(
                    object : OnCompleteListener<Location?> {

                        override fun onComplete(
                            task: Task<Location?>
                        ) {


                            // Initialize location
                            val location: Location? = task.result
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
//                            Toasty.success(
//                                this@Checkin_Out_Home,
//                                "Lat: " + location.getLatitude() + "long: " + location.getLongitude()
//                            )
                                lat = location.latitude.toString()
                                longg = location.longitude.toString()

//                            tvLatitude.setText(java.lang.String.valueOf(location.getLatitude()))
//                            // set longitude
//                            tvLongitude.setText(java.lang.String.valueOf(location.getLongitude()))
                            } else {
                                // When location result is null
                                // initialize location request
                                val locationRequest =
                                    LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000).setFastestInterval(1000)
                                        .setNumUpdates(1)

                                // Initialize location call back
                                val locationCallback: LocationCallback =
                                    object : LocationCallback() {
                                        fun voidonLocationResult(
                                            locationResult: LocationResult
                                        ) {
                                            // Initialize
                                            // location
                                            val location1: Location = locationResult.lastLocation
                                            // Set latitude
//                                    Toasty.success(
//                                        this@Checkin_Out_Home,
//                                        "Lat: " + location1.getLatitude() + "long: " + location1.getLongitude()
//                                    )
                                            lastlat = location1.latitude.toString()
                                            lastlongg = location1.longitude.toString()
                                            val mGeocoder =
                                                Geocoder(requireActivity(), Locale.getDefault())
                                            if (mGeocoder != null) {
                                                var postalcode: MutableList<Address>? =
                                                    mGeocoder.getFromLocation(
                                                        lastlat!!.toDouble(),
                                                        lastlongg!!.toDouble(),
                                                        5
                                                    )
                                                if (postalcode != null && postalcode.size > 0) {
                                                    for (i in 0 until postalcode.size) {
                                                        AppUtils2.pincode =
                                                            postalcode.get(i).postalCode.toString()
                                                        SharedPreferenceUtil.setData(
                                                            requireActivity(),
                                                            "pincode",
                                                            postalcode.get(i).postalCode.toString()
                                                        )
                                                        break
                                                    }
                                                }
                                            }
//                                    tvLatitude.setText(java.lang.String.valueOf(location1.getLatitude()))
//                                    // Set longitude
//                                    tvLongitude.setText(java.lang.String.valueOf(location1.getLongitude()))
                                        }
                                    }

                                // Request location updates
                                if (ActivityCompat.checkSelfPermission(
                                        requireActivity(),
                                        Manifest.permission.ACCESS_FINE_LOCATION
                                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                        requireActivity(),
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
                                Looper.myLooper()?.let {
                                    client!!.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        it
                                    )
                                }
                            }
                        }
                    })
            }catch (e:Exception){
                e.printStackTrace()
            }
            // When location service is enabled
            // Get last location

        }else{
            Toast.makeText(requireActivity(),"wrong",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyLocationListener(requireActivity())

        client = LocationServices
            .getFusedLocationProviderClient(
                requireActivity()
            )


        if (!LocationPermissionManager.checkLocationPermission(requireActivity())) {
            // Request the permission
            LocationPermissionManager.requestLocationPermission(requireActivity())
        } else {
            getCurrentLocations()
            // Permission already granted, proceed with your location-related tasks
            // ...
        }


        AppUtils2.customerid =
            SharedPreferenceUtil.getData(requireActivity(), "customerid", "").toString()
        AppUtils2.pincode =
            SharedPreferenceUtil.getData(requireActivity(), "pincode", "").toString()

        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        progressDialog.show()


        viewProductModel.productcount.observe(requireActivity(), Observer {
            if (it.IsSuccess == true) {

                progressDialog.dismiss()
                if (it.Data == 0) {
                    binding.cartmenu.visibility = View.GONE
                } else {
                    binding.cartmenu.visibility = View.VISIBLE
                    binding.appCompatImageViewd.text = it.Data.toString()
                }
            } else {
                progressDialog.dismiss()
                binding.cartmenu.visibility = View.GONE
            }

            progressDialog.dismiss()
        })

        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())

        binding.getpincodetext.setText(AppUtils2.pincode)

        if (AppUtils2.pincode.equals("")) {
            Toast.makeText(requireActivity(), "please enter correct pincode", Toast.LENGTH_LONG).show()
        } else {
//            Handler(Looper.getMainLooper()).postDelayed({
//                progressDialog.show()
                getProductslist(AppUtils2.pincode!!)
//            },2000)
        }

        binding.imgsearch.setOnClickListener {
            if (binding.getpincodetext.text.equals("") || binding.getpincodetext.text.length != 6) {
                Toast.makeText(requireActivity(), "Please enter your pincode", Toast.LENGTH_LONG).show()
            } else if(binding.getpincodetext.text.toString().trim().length<6){
                Toast.makeText(requireActivity(), "Invalid pincode", Toast.LENGTH_LONG).show()
            }else{
                AppUtils2.pincode = binding.getpincodetext.text.trim().toString()
                SharedPreferenceUtil.setData(
                    requireActivity(),
                    "pincode",
                    binding.getpincodetext.text.toString()
                )
                getProductslist2(binding.getpincodetext.text.trim().toString())
            }

        }

        binding.cartmenu.setOnClickListener {
            if (AppUtils2.pincode.equals("")) {
                Toast.makeText(requireActivity(), "please enter correct pincode", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(requireActivity(), AddToCartActivity::class.java)
                startActivity(intent)
            }
        }
        progressDialog.dismiss()

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getProductslist(pincode: String) {
//        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
//        progressDialog.setCancelable(false)
        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = ProductAdapter()

        binding.recycleviewproduct.adapter = mAdapter

//        Handler(Looper.getMainLooper()).postDelayed({

            viewProductModel.productlist.observe(requireActivity(), Observer {

                if (it != null) {

                    binding.recycleviewproduct.visibility = View.VISIBLE
                    binding.textnotfound.visibility = View.GONE
                    mAdapter.setProductList(it, requireActivity(), viewProductModel)
                    progressDialog.dismiss()

                } else {
                    binding.recycleviewproduct.visibility = View.GONE
                    binding.textnotfound.visibility = View.VISIBLE
                    progressDialog.dismiss()

                }
            })

            viewProductModel.responseMessage.observe(requireActivity(), Observer {
                progressDialog.dismiss()
                binding.recycleviewproduct.visibility = View.GONE
                Toast.makeText(requireActivity(), "Invalid Pincode", Toast.LENGTH_LONG).show()
            })
                viewProductModel.getProductlist(pincode)

//        },3000)


        progressDialog.dismiss()

        mAdapter.setOnOrderItemClicked(object : OnProductClickedHandler {
            override fun onProductClickedHandler(position: Int, productid: Int) {

                progressDialog.show()

//                Handler(Looper.getMainLooper()).postDelayed({

                    viewProductModel.addtocart.observe(requireActivity(), Observer {
                        if (it.IsSuccess == true) {
                            getproductcount()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Something went wrong! Unable to add product into cart",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })


                    viewProductModel.getAddProductInCart(1, productid, AppUtils2.customerid.toInt())

                    viewProductModel.productcount.observe(requireActivity(), Observer {
                        progressDialog.dismiss()
                        if (it.IsSuccess == true) {
                            binding.cartmenu.visibility = View.VISIBLE
                            binding.appCompatImageViewd.text = it.Data.toString()
                        } else {
                            binding.cartmenu.visibility = View.GONE
                        }
                    })


                    viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())
//                }, 1500)
            }

            override fun onProductView(position: Int, productid: OrderSummeryData) {

            }

            override fun onNotifyMeclick(position: Int, response: ProductListResponseData) {
                viewProductModel.CreateEventNotificationResponse.observe(requireActivity(), Observer {
                    if (it.IsSuccess == true) {
                        Toast.makeText(
                            requireActivity(),
                            "Thank You! For Notifying Us",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                val data = HashMap<String, Any>()
                data["Id"] = 0
                data["Mobile_No"] = AppUtils2.mobileno
                data["Event_Source"] = "Product"
                data["Event_Type"] = "Out of stock"
                data["Reference_Id"] = response.ProductId.toString()
                data["Additional_Data"] = "Product|"+response.ProductId.toString()+"|"+AppUtils2.customerid+"|"+AppUtils2.pincode+"|"+AppUtils2.mobileno+"|"+response.ProductThumbnail
                data["NextNotified_On"] = getCurrentDate()
                data["Is_Notify"] = true
                data["Created_By"] =0
                data["Created_On"] =getCurrentDate()
                data["Notification_Tag"] ="string"
                data["Notification_Title"] ="string"
                data["Notification_Body"] ="string"
                viewProductModel.CreateEventForMobileAppNotification(data)
            }
        })
        progressDialog.dismiss()
    }

    private fun getProductslist2(pincode: String) {
//        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
//        progressDialog.setCancelable(false)
        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = ProductAdapter()

        binding.recycleviewproduct.adapter = mAdapter


        Handler(Looper.getMainLooper()).postDelayed({

        viewProductModel.productlist.observe(requireActivity(), Observer {

            if (it != null) {
//                Log.e("TAG","DataUi:"+it)
                progressDialog.dismiss()
                mAdapter.setProductList(it, requireActivity(), viewProductModel)
                binding.recycleviewproduct.visibility = View.VISIBLE
                binding.textnotfound.visibility = View.GONE

            } else {
                binding.recycleviewproduct.visibility = View.GONE
                binding.textnotfound.visibility = View.VISIBLE
                progressDialog.dismiss()

            }
        })

        viewProductModel.responseMessage.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            binding.recycleviewproduct.visibility = View.GONE
            Toast.makeText(requireActivity(), "Invalid Pincode", Toast.LENGTH_LONG).show()
        })
        viewProductModel.getProductlist(pincode)

        },1000)

        progressDialog.dismiss()

        mAdapter.setOnOrderItemClicked(object : OnProductClickedHandler {
            override fun onProductClickedHandler(position: Int, productid: Int) {

                progressDialog.show()

//                Handler(Looper.getMainLooper()).postDelayed({

                viewProductModel.addtocart.observe(requireActivity(), Observer {
                    if (it.IsSuccess == true) {
                        getproductcount()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Something went wrong! Unable to add product into cart",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })


                viewProductModel.getAddProductInCart(1, productid, AppUtils2.customerid.toInt())

                viewProductModel.productcount.observe(requireActivity(), Observer {
                    progressDialog.dismiss()
                    if (it.IsSuccess == true) {
                        binding.cartmenu.visibility = View.VISIBLE
                        binding.appCompatImageViewd.text = it.Data.toString()
                    } else {
                        binding.cartmenu.visibility = View.GONE
                    }
                })


                viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())
//                }, 1500)
            }

            override fun onProductView(position: Int, productid: OrderSummeryData) {

            }

            override fun onNotifyMeclick(position: Int, response: ProductListResponseData) {
                viewProductModel.CreateEventNotificationResponse.observe(requireActivity(), Observer {
                    if (it.IsSuccess == true) {
                        Toast.makeText(
                            requireActivity(),
                            "Thank You! For Notifying Us",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
                val data = HashMap<String, Any>()
                data["Id"] = 0
                data["Mobile_No"] = AppUtils2.mobileno
                data["Event_Source"] = "Product"
                data["Event_Type"] = "Out of stock"
                data["Reference_Id"] = response.ProductId.toString()
                data["Additional_Data"] = "Product|"+response.ProductId.toString()+"|"+AppUtils2.customerid+"|"+AppUtils2.pincode+"|"+AppUtils2.mobileno+"|"+response.ProductThumbnail
                data["NextNotified_On"] = getCurrentDate()
                data["Is_Notify"] = true
                data["Created_By"] =0
                data["Created_On"] =getCurrentDate()
                data["Notification_Tag"] ="string"
                data["Notification_Title"] ="string"
                data["Notification_Body"] ="string"
                viewProductModel.CreateEventForMobileAppNotification(data)

                viewProductModel.CreateEventNotificationResponse.observe(requireActivity(), Observer {
                    if (it.IsSuccess == true) {
                        Toast.makeText(
                            requireActivity(),
                            "Thank You! For Notifying Us",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
//                {
//                    "Id": 0,
//                    "Mobile_No": "string",
//                    "Event_Source": "Product",
//                    "Event_Type": "Out of stock",
//                    "Reference_Id": "productid",
//                    "Additional_Data": "json",
//                    "NextNotified_On": "2023-08-02T12:36:32.987Z",
//                    "Is_Notify": true,
//                    "Created_By": 0,
//                    "Created_On": "2023-08-02T12:36:32.987Z",
//                    "Notification_Tag": "string",
//                    "Notification_Title": "string",
//                    "Notification_Body": "string"
//                }

            }
        })

        progressDialog.dismiss()
    }


    private fun getproductcount() {

        progressDialog.show()

        viewProductModel.productcount.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            if (it.IsSuccess == true) {
                progressDialog.dismiss()
                binding.cartmenu.visibility = View.VISIBLE
                binding.appCompatImageViewd.text = it.Data.toString()
            } else {
                binding.cartmenu.visibility = View.GONE
            }
        })

        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())
        progressDialog.dismiss()

    }

    override fun onResume() {
        super.onResume()
        viewProductModel.productcount.observe(requireActivity(), Observer {
            if (it.IsSuccess == true) {

                if (it.Data == 0) {
                    binding.cartmenu.visibility = View.GONE
                } else {
                    binding.cartmenu.visibility = View.VISIBLE
                    AppUtils2.cartcounts = it.Data.toString()
                    binding.appCompatImageViewd.text = it.Data.toString()
                }
            } else {
                binding.cartmenu.visibility = View.GONE
            }
        })
        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())
        progressDialog.dismiss()

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProductFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        return sdf.format(Date())
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocations() {

        try {
            val locationManager: LocationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // Check condition
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
            ) {
                try {
                    client!!.lastLocation.addOnCompleteListener(
                        object : OnCompleteListener<Location?> {

                            override fun onComplete(
                                task: Task<Location?>
                            ) {


                                // Initialize location
                                val location: Location? = task.result
                                // Check condition
                                if (location != null) {
                                    // When location result is not
                                    // null set latitude
//                            Toasty.success(
//                                this@Checkin_Out_Home,
//                                "Lat: " + location.getLatitude() + "long: " + location.getLongitude()
//                            )
                                    lat = location.latitude.toString()
                                    longg = location.longitude.toString()

//                            tvLatitude.setText(java.lang.String.valueOf(location.getLatitude()))
//                            // set longitude
//                            tvLongitude.setText(java.lang.String.valueOf(location.getLongitude()))
                                } else {
                                    // When location result is null
                                    // initialize location request
                                    val locationRequest =
                                        LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setInterval(10000).setFastestInterval(1000)
                                            .setNumUpdates(1)

                                    // Initialize location call back
                                    val locationCallback: LocationCallback =
                                        object : LocationCallback() {
                                            fun voidonLocationResult(
                                                locationResult: LocationResult
                                            ) {
                                                // Initialize
                                                // location
                                                val location1: Location = locationResult.lastLocation
                                                // Set latitude
//                                    Toasty.success(
//                                        this@Checkin_Out_Home,
//                                        "Lat: " + location1.getLatitude() + "long: " + location1.getLongitude()
//                                    )
                                                lastlat = location1.latitude.toString()
                                                lastlongg = location1.longitude.toString()
                                                val mGeocoder =
                                                    Geocoder(requireActivity(), Locale.getDefault())
                                                if (mGeocoder != null) {
                                                    var postalcode: MutableList<Address>? =
                                                        mGeocoder.getFromLocation(
                                                            lastlat!!.toDouble(),
                                                            lastlongg!!.toDouble(),
                                                            5
                                                        )
                                                    if (postalcode != null && postalcode.size > 0) {
                                                        for (i in 0 until postalcode.size) {
                                                            AppUtils2.pincode =
                                                                postalcode.get(i).postalCode.toString()
                                                            SharedPreferenceUtil.setData(
                                                                requireActivity(),
                                                                "pincode",
                                                                postalcode.get(i).postalCode.toString()
                                                            )
                                                            break
                                                        }
                                                    }
                                                }
//                                    tvLatitude.setText(java.lang.String.valueOf(location1.getLatitude()))
//                                    // Set longitude
//                                    tvLongitude.setText(java.lang.String.valueOf(location1.getLongitude()))
                                            }
                                        }

                                    // Request location updates
                                    if (ActivityCompat.checkSelfPermission(
                                            requireActivity(),
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                            requireActivity(),
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
                                    Looper.myLooper()?.let {
                                        client!!.requestLocationUpdates(
                                            locationRequest,
                                            locationCallback,
                                            it
                                        )
                                    }
                                }
                            }
                        })
                }catch (e:Exception){
                    e.printStackTrace()
                }
                // When location service is enabled
                // Get last location

            } else {
                enableLoc()

//                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
        val result = LocationServices.getSettingsClient(requireActivity()).checkLocationSettings(builder.build())
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
//                                requireActivity(),
//                                REQUEST_CODE_PERMISSIONS
//                            )
//                        } catch (e: SendIntentException) {
//                            // Ignore the error.
//                        } catch (e: ClassCastException) {
//                            // Ignore, should be an impossible error.
//                        }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
//                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
    }


}
