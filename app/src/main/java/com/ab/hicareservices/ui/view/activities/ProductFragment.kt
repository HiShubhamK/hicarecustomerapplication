package com.ab.hicareservices.ui.view.activities

import android.Manifest
//import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import java.text.SimpleDateFormat
import java.util.Date


class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = ""
    var pincode: String? = ""
    private lateinit var mAdapter: ProductAdapter
    lateinit var progressDialog: ProgressDialog
    val REQUEST_CODE_PERMISSIONS =101

    private var launcher=  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
//            Toast.makeText(this,"Hello akshay",Toast.LENGTH_SHORT).show()
        } else {
            AppUtils2.ISChecklocationpermission=true
//            Toast.makeText(this,"Hello akshay fails ",Toast.LENGTH_SHORT).show()
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

        val lm = context!!.getSystemService(LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        if (!gps_enabled && !network_enabled) {
            if(AppUtils2.ISChecklocationpermission==true){

            }else{
                enableLoc()
            }
        }else if (!gps_enabled){
            if(AppUtils2.ISChecklocationpermission==true){

            }else{
                enableLoc()
            }
        }else if(!network_enabled){
            if(AppUtils2.ISChecklocationpermission==true){

            }else{
                enableLoc()
            }
        }else{
            enableLoc()
        }


//        if (ContextCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.POST_NOTIFICATIONS
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            // When permission is granted
//            // Call method
////            getCurrentLocations()
//
//        } else {
//            enableLoc()
//
//            // When permission is not granted
//            // Call method
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.POST_NOTIFICATIONS
//                    ),
//                    100
//                )
//            }
//        }

        MyLocationListener(requireActivity())

        AppUtils2.customerid =
            SharedPreferenceUtil.getData(requireActivity(), "customerid", "").toString()
        AppUtils2.pincode =
            SharedPreferenceUtil.getData(requireActivity(), "pincode", "400080").toString()

        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        viewProductModel.productcount.observe(requireActivity(), Observer {
            progressDialog.show()
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

        if (AppUtils2.pincode.equals("")) {
            AppUtils2.pincode="400080"
            SharedPreferenceUtil.setData(requireActivity(), "pincode","400080")
            AppUtils2.pincode = SharedPreferenceUtil.getData(requireActivity(), "pincode", "400080").toString()

            binding.getpincodetext.setText(AppUtils2.pincode)
            getProductslist(AppUtils2.pincode!!)
//            Toast.makeText(requireActivity(), "please enter correct pincode", Toast.LENGTH_LONG).show()
        } else {
            binding.getpincodetext.setText(AppUtils2.pincode)
            getProductslist(AppUtils2.pincode!!)
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
            val intent = Intent(requireActivity(), AddToCartActivity::class.java)
            startActivity(intent)
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
        viewProductModel.CreateEventNotificationResponse.observe(requireActivity(), Observer {
            if (it.IsSuccess == true) {
                Toast.makeText(
                    requireActivity(),
                    "Thank You! For Notifying Us",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

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
}