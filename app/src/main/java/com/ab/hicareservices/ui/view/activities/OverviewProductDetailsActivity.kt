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
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.databinding.ActivityOverviewProductDetailsBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.AddressAdapter
import com.ab.hicareservices.ui.adapter.OverviewDetailAdapter
import com.ab.hicareservices.ui.handler.onAddressClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.util.*
import kotlin.collections.ArrayList

class OverviewProductDetailsActivity : AppCompatActivity() {

    var billdata = ""
    var shipdaata = ""
    private lateinit var binding: ActivityOverviewProductDetailsBinding
    private val viewProductModel: ProductViewModel by viewModels()
    private lateinit var mAdapter: OverviewDetailAdapter
    var shippingdata: String? = ""
    var billingdata: String? = ""
    lateinit var datalist: ArrayList<CartlistResponseData>
    lateinit var progressDialog: ProgressDialog
    var client: FusedLocationProviderClient? = null
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_product_details)
        binding = ActivityOverviewProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyLocationListener(this)

        MyLocationListener(this)
        client = LocationServices
            .getFusedLocationProviderClient(
                this
            )

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
        ) {
            // When permission is granted
            // Call method
            getCurrentLocations()
        } else {
            // When permission is not granted
            // Call method
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    100
                )
            }
        }





        progressDialog =
            ProgressDialog(this, com.ab.hicareservices.R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        shippingdata = SharedPreferenceUtil.getData(this, "Shippingdata", "").toString()
        billingdata = SharedPreferenceUtil.getData(this, "Billingdata", "").toString()
        AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()
        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()

        AppUtils2.email = SharedPreferenceUtil.getData(this, "EMAIL", "").toString()


        val intent = intent
        billdata = intent.getStringExtra("Billdata").toString()
        shipdaata = intent.getStringExtra("Shipdata").toString()

        getproductlist()
        getSummarydata()
        getAddressList()
        getAddressforbilling()
        if (!AppUtils2.pincode.isNullOrEmpty()){
            binding.tvPincode.text="Deliver to pincode "+AppUtils2.pincode
        }else {
            binding.tvPincode.visibility=View.GONE
        }

        binding.txtplcaeorder.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("Product", true)
            startActivity(intent)
        }




    }

    private fun getAddressforbilling() {
        progressDialog.show()
        viewProductModel.getaddressbydetailid.observe(this, Observer {
            progressDialog.dismiss()

            AppUtils2.flat = it.FlatNo.toString()
            AppUtils2.builingname = it.BuildingName.toString()
            AppUtils2.street = it.Street.toString()
            AppUtils2.locality = it.Locality.toString()
            AppUtils2.landmark = it.Landmark.toString()
            AppUtils2.pincodelast = it.Pincode.toString()
            AppUtils2.city=it.City.toString()
            AppUtils2.state=it.State.toString()
            binding.txtbilling.visibility = View.VISIBLE
            binding.txtbilling.text =
                it.FlatNo.toString() + "," + it.BuildingName.toString() + "," + it.Street.toString() + "," +
                        it.Locality.toString() + "," + it.Landmark.toString() + "," + it.Pincode.toString()
        })
        viewProductModel.getAddressDetailbyId(billdata!!.toInt())
    }


    private fun getAddressList() {

        progressDialog.show()

        viewProductModel.cutomeraddress.observe(this, Observer {

            progressDialog.dismiss()

            for (i in 0 until it.size) {
                var data = it.get(i).Id.toString()
                if (data.equals(shipdaata)) {
                    AppUtils2.cutomername = it.get(i).ContactPersonName.toString()
                    AppUtils2.customermobile = it.get(i).ContactPersonMobile.toString()
                    AppUtils2.customeremail = it.get(i).ContactPersonEmail.toString()
                    binding.txtshipping.text =
                        it.get(i).FlatNo.toString() + "," + it.get(i).BuildingName.toString() + "," + it.get(
                            i
                        ).Street.toString() + "," +
                                it.get(i).Locality.toString() + "," + it.get(i).Landmark.toString() + "," + it.get(
                            i
                        ).City.toString() + "," + it.get(i).State.toString() + "," + it.get(i).Pincode.toString()
                }
//                else if(data.equals(billdata)){
//                    binding.txtbilling.text=it.get(i).FlatNo.toString()+","+it.get(i).BuildingName.toString()+","+it.get(i).Street.toString()+","+
//                            it.get(i).Locality.toString()+","+it.get(i).Landmark.toString()+","+it.get(i).City.toString()+","+it.get(i).State.toString()+","+it.get(i).Pincode.toString()
//                }
                else {

                }
            }
        })

        viewProductModel.getCustomerAddress(AppUtils2.customerid.toInt())
    }


    private fun getproductlist() {

        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mAdapter = OverviewDetailAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.cartlist.observe(this, Observer {

            progressDialog.dismiss()

            AppUtils2.leaderlist = it as ArrayList<CartlistResponseData>
//             datalist= it as ArrayList<CartlistResponseData>
//            Toast.makeText(this,AppUtils2.leaderlist.size.toString(),Toast.LENGTH_LONG).show()
            mAdapter.setCartList(it, this, viewProductModel)

        })

        viewProductModel.getProductCartByUserId(AppUtils2.customerid.toInt())
    }

    fun getSummarydata() {

        progressDialog.show()

        viewProductModel.getsummarydata.observe(this, Observer {


            AppUtils2.productamount=it.FinalAmount.toString()
            AppUtils2.actualvalue=it.TotalAmount.toString()
            AppUtils2.totaldiscount=it.TotalDiscount.toString()
            binding.txtfinaltext.text="\u20B9" + it.FinalAmount.toString()
            binding.txttotoalvalue.text = "\u20B9" + it.TotalAmount.toString()
            binding.txtdiscount.text = "-"+"\u20B9" + it.TotalDiscount.toString()
            binding.txttoalamount.text = "\u20B9" + it.FinalAmount.toString()
            if (it.DeliveryCharges!=null&&it.DeliveryCharges==0){
                binding.tvDeliveryCharge.text="Free"
            }else{
                binding.tvDeliveryCharge.text=it.DeliveryCharges.toString()
            }
            progressDialog.dismiss()

        })

        viewProductModel.getCartSummary(AppUtils2.customerid.toInt(), AppUtils2.pincode, "")

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
                                    val mGeocoder = Geocoder(this@OverviewProductDetailsActivity, Locale.getDefault())
                                    if (mGeocoder != null) {
                                        var postalcode: MutableList<Address>? = mGeocoder.getFromLocation(lastlat!!.toDouble(), lastlongg!!.toDouble(), 5)
                                        if (postalcode != null && postalcode.size > 0) {
                                            for (i in 0 until postalcode.size){
                                                AppUtils2.pincode=postalcode.get(i).postalCode.toString()
                                                SharedPreferenceUtil.setData(this@OverviewProductDetailsActivity, "pincode",postalcode.get(i).postalCode.toString())
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
                                    this@OverviewProductDetailsActivity,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this@OverviewProductDetailsActivity,
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