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
import com.ab.hicareservices.utils.DesignToast
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import okhttp3.internal.notify
import java.util.*

class AddToCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddToCartBinding
    private lateinit var mAdapter: CartAdapter
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = null
    var pincode: String? = null
    lateinit var progressDialog: ProgressDialog
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)
        binding = ActivityAddToCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        MyLocationListener(this)

        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "400080").toString()

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        getproductlist()
        getSummarydata()
        pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()
        if (!pincode.isNullOrEmpty()) {
            binding.tvPincode.text = "Deliver to pincode " + pincode

        } else {
            binding.tvPincode.visibility = View.GONE
        }
        binding.txtplcaeorder.setOnClickListener {
            SharedPreferenceUtil.setData(this, "Billingdata", "")
            val intent = Intent(this, AddressActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getproductlist() {

        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = CartAdapter()

        binding.recycleviewproduct.adapter = mAdapter


        viewProductModel.responseMessage.observe(this, Observer {
            binding.upcomingservices.visibility = View.VISIBLE
            binding.scrollview.visibility = View.GONE
            binding.recycleviewproduct.visibility = View.GONE
            binding.cardviewprice.visibility = View.GONE
            binding.lnrbuttoncart.visibility = View.GONE
        })

        viewProductModel.cartlist.observe(this, Observer {

            if (it != null) {

                binding.upcomingservices.visibility = View.GONE
                binding.recycleviewproduct.visibility = View.VISIBLE
                binding.cardviewprice.visibility = View.VISIBLE
                binding.lnrbuttoncart.visibility = View.VISIBLE
                mAdapter.setCartList(
                    it,
                    this,
                    viewProductModel,
                    progressDialog,
                    AppUtils2.changebuttonstatus
                )
                progressDialog.dismiss()
            } else {
                progressDialog.dismiss()
                binding.upcomingservices.visibility = View.VISIBLE
                binding.scrollview.visibility = View.GONE
                binding.recycleviewproduct.visibility = View.GONE
                binding.cardviewprice.visibility = View.GONE
                binding.lnrbuttoncart.visibility = View.GONE
            }
        })

        mAdapter.setOnOrderItemClicked(object : onCartClickedHandler {
            override fun setondeleteclicklistener(position: Int, cartId: Int?, userId: Int?) {

                progressDialog.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    viewProductModel.getDeleteProductCart.observe(this@AddToCartActivity, Observer {
                        if (it.IsSuccess == true) {
                            progressDialog.dismiss()
                            getSummarydata()
                            AppUtils2.cartcounts = ""
                            AppUtils2.changebuttonstatus = false

                            Handler(Looper.getMainLooper()).postDelayed({
                                val mIntent = intent
                                finish()
                                startActivity(mIntent)
                                progressDialog.dismiss()

                            }, 2000)

                        }
                        progressDialog.dismiss()

                    })
                    viewProductModel.getDeleteProductCart(
                        cartId!!.toInt(),
                        AppUtils2.customerid.toInt()
                    )

                }, 1000)

            }

            override fun setonaddclicklistener(
                position: Int,
                productid: Int,
                i: Int,
                imgadd: ImageView
            ) {


                progressDialog.show()

                Handler(Looper.getMainLooper()).postDelayed({
                    viewProductModel.addtocart.observe(this@AddToCartActivity, Observer {
                        progressDialog.dismiss()
                        if (it.IsSuccess == true) {
                            imgadd.isClickable = true
                            imgadd.isEnabled = true
                            progressDialog.dismiss()
                            getSummarydata()
                            AppUtils2.changebuttonstatus = false
                        } else {
//                        progressDialog.dismiss()
                            DesignToast.makeText(
                                this@AddToCartActivity,
                                "Something went to wrong",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        }
                    })
                    viewProductModel.getAddProductInCart(i, productid, AppUtils2.customerid.toInt())
                }, 1000)
            }

        })

//        viewProductModel.getProductCartByUserId(customerid!!.toInt())
        viewProductModel.getProductCartByUserId(AppUtils2.customerid.toInt(), AppUtils2.pincode)
    }

    fun getSummarydata() {

        progressDialog.show()

//        Handler(Looper.getMainLooper()).postDelayed({

        viewProductModel.getsummarydata.observe(this, Observer {

            if (it.TotalAmount!!.toDouble().toInt() != 0) {
                progressDialog.dismiss()
                binding.upcomingservices.visibility = View.GONE
                binding.cardviewprice.visibility = View.VISIBLE
                binding.lnrbuttoncart.visibility = View.VISIBLE
                binding.txttotoalvalue.text = "\u20B9" + it.TotalAmount!!.toDouble().toString()
                binding.txtdiscount.text = "-" + "\u20B9" + it.TotalDiscount!!.toDouble().toString()
                binding.txttoalamount.text = "\u20B9" + it.FinalAmount!!.toDouble().toString()
                binding.txtfinaltext.text = "\u20B9" + it.FinalAmount!!.toDouble().toString()

            } else if (it.TotalAmount!!.toDouble().toInt() == 0) {
                binding.upcomingservices.visibility = View.VISIBLE
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
        finish()
    }

    override fun onResume() {
        super.onResume()
        getSummarydata()
    }
}