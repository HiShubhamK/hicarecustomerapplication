package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityAddToCartBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.CartAdapter
import com.ab.hicareservices.ui.handler.onCartClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.util.*

class AddToCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddToCartBinding
    private lateinit var mAdapter: CartAdapter
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = null
    var pincode: String? = null
    lateinit var progressDialog: ProgressDialog
    var client: FusedLocationProviderClient? = null
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)
        binding = ActivityAddToCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyLocationListener(this)
//
//        client = LocationServices
//            .getFusedLocationProviderClient(
//                this
//            )
//
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            // When permission is granted
//            // Call method
//            getCurrentLocations()
//        } else {
//            // When permission is not granted
//            // Call method
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ),
//                    100
//                )
//            }
//        }


        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()



        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        getproductlist()
        getSummarydata()
        pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()
        if (!pincode.isNullOrEmpty()){
            binding.tvPincode.text="Deliver to pincode "+pincode

        }else {
            binding.tvPincode.visibility=View.GONE
        }
        binding.txtplcaeorder.setOnClickListener{
            val intent= Intent(this,AddressActivity::class.java)
            startActivity(intent)
        }
        binding.imgLogo.setOnClickListener{
            onBackPressed()
        }
    }

    private fun getproductlist() {

        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = CartAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.cartlist.observe(this, Observer {

            if(it!=null) {
                mAdapter.setCartList(it, this, viewProductModel,progressDialog,AppUtils2.changebuttonstatus)
                progressDialog.dismiss()
            }else{
                progressDialog.dismiss()
                binding.cardviewprice.visibility= View.GONE
                binding.lnrbuttoncart.visibility=View.GONE
            }
        })

        mAdapter.setOnOrderItemClicked(object : onCartClickedHandler {
            override fun setondeleteclicklistener(position: Int, cartId: Int?, userId: Int?) {

                progressDialog.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    viewProductModel.getDeleteProductCart.observe(this@AddToCartActivity, Observer {
                        if(it.IsSuccess==true){
                            progressDialog.dismiss()
                            AppUtils2.cartcounts=""
                            val intent=intent
                            AppUtils2.changebuttonstatus=false
                            finish()
                            startActivity(intent)
                        }
                    })
                    viewProductModel.getDeleteProductCart(cartId!!.toInt(), AppUtils2.customerid.toInt())

                }, 1000)

            }

            override fun setonaddclicklistener(
                position: Int,
                productid: Int,
                i: Int,
                imgadd: ImageView
            ) {


//                progressDialog.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    viewProductModel.addtocart.observe(this@AddToCartActivity, Observer {
                        progressDialog.dismiss()
                        if(it.IsSuccess==true){
                            imgadd.isClickable=true
                            imgadd.isEnabled=true
//                        progressDialog.dismiss()
                            getSummarydata()
                            AppUtils2.changebuttonstatus=false
                        }else{
//                        progressDialog.dismiss()
                            Toast.makeText(this@AddToCartActivity,"Something went to wromg",Toast.LENGTH_LONG).show()
                        }

                    })

                    viewProductModel.getAddProductInCart(i, productid, AppUtils2.customerid.toInt())
                }, 1000)
            }

        })

//        viewProductModel.getProductCartByUserId(customerid!!.toInt())
        viewProductModel.getProductCartByUserId(AppUtils2.customerid.toInt())
    }

    fun getSummarydata() {

        progressDialog.show()

//        Handler(Looper.getMainLooper()).postDelayed({

        viewProductModel.getsummarydata.observe(this, Observer {


            if (it.TotalAmount != 0) {
                progressDialog.dismiss()
                binding.txttotoalvalue.text = "\u20B9" + it.TotalAmount.toString()
                binding.txtdiscount.text = "-"+"\u20B9" + it.TotalDiscount.toString()
                binding.txttoalamount.text = "\u20B9" + it.FinalAmount.toString()
                binding.txtfinaltext.text = "\u20B9" + it.FinalAmount.toString()

            } else {
                binding.lnrbuttoncart.visibility = View.GONE
                binding.cardviewprice.visibility = View.GONE
                progressDialog.dismiss()

            }
        })
//        },1000)
        viewProductModel.getCartSummary(AppUtils2.customerid.toInt(), AppUtils2.pincode, "")

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onResume() {
        super.onResume()
        getSummarydata()
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocations() {
        val locationManager: LocationManager =
            this.getSystemService(LOCATION_SERVICE) as LocationManager
        // Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        ) {
            // When location service is enabled
            // Get last location
            client!!.lastLocation.addOnCompleteListener(
                object : OnCompleteListener<Location?> {

                    override fun onComplete(
                        task: Task<Location?>
                    ) {

                        // Initialize location
                        val location: Location = task.getResult()!!
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
                                    .setInterval(10000).setFastestInterval(1000).setNumUpdates(1)

                            // Initialize location call back
                            val locationCallback: LocationCallback = object : LocationCallback() {
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
                                    val mGeocoder = Geocoder(this@AddToCartActivity, Locale.getDefault())
                                    if (mGeocoder != null) {
                                        var postalcode: MutableList<Address>? = mGeocoder.getFromLocation(lastlat!!.toDouble(), lastlongg!!.toDouble(), 5)
                                        if (postalcode != null && postalcode.size > 0) {
                                            for (i in 0 until postalcode.size){
                                                AppUtils2.pincode=postalcode.get(i).postalCode.toString()
                                                SharedPreferenceUtil.setData(this@AddToCartActivity, "pincode",postalcode.get(i).postalCode.toString())
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
                                    this@AddToCartActivity,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this@AddToCartActivity,
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
        } else {
            // When location service is not enabled
            // open location setting
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

}