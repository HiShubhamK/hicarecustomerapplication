package com.ab.hicareservices.ui.view.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.product.ProductListResponseData
import com.ab.hicareservices.databinding.ActivityOverviewProductDetailsBinding
import com.ab.hicareservices.databinding.ActivityProductBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = ""
    var pincode: String? = ""
    private lateinit var mAdapter: ProductAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)


        MyLocationListener(this)

        AppUtils2.customerid =
            SharedPreferenceUtil.getData(this@ProductActivity, "customerid", "").toString()
        AppUtils2.pincode =
            SharedPreferenceUtil.getData(this@ProductActivity, "pincode", "").toString()

        progressDialog = ProgressDialog(this@ProductActivity, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        viewProductModel.productcount.observe(this@ProductActivity, Observer {
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
        })

        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())

        binding.getpincodetext.setText(AppUtils2.pincode)

        if (AppUtils2.pincode.equals("")) {
            Toast.makeText(this@ProductActivity, "please enter correct pincode", Toast.LENGTH_LONG).show()
        } else {
//            Handler(Looper.getMainLooper()).postDelayed({
//                progressDialog.show()
            getProductslist(AppUtils2.pincode!!)
//            },2000)
        }

        binding.imgsearch.setOnClickListener {
            if (binding.getpincodetext.text.equals("") || binding.getpincodetext.text.length != 6) {
                Toast.makeText(this@ProductActivity, "Please enter your pincode", Toast.LENGTH_LONG).show()
            } else if(binding.getpincodetext.text.toString().trim().length<6){
                Toast.makeText(this@ProductActivity, "Invalid pincode", Toast.LENGTH_LONG).show()
            }else{
                AppUtils2.pincode = binding.getpincodetext.text.trim().toString()
                SharedPreferenceUtil.setData(
                    this@ProductActivity,
                    "pincode",
                    binding.getpincodetext.text.toString()
                )
                getProductslist2(binding.getpincodetext.text.trim().toString())
            }

        }

        binding.cartmenu.setOnClickListener {
            val intent = Intent(this@ProductActivity, AddToCartActivity::class.java)
            startActivity(intent)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun getProductslist(pincode: String) {
//        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
//        progressDialog.setCancelable(false)
        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this@ProductActivity, LinearLayoutManager.VERTICAL, false)
        mAdapter = ProductAdapter()

        binding.recycleviewproduct.adapter = mAdapter


//        Handler(Looper.getMainLooper()).postDelayed({

        viewProductModel.productlist.observe(this@ProductActivity, Observer {

            if (it != null) {

                binding.recycleviewproduct.visibility = View.VISIBLE
                binding.textnotfound.visibility = View.GONE
                mAdapter.setProductList(it, this@ProductActivity, viewProductModel)
                progressDialog.dismiss()

            } else {
                binding.recycleviewproduct.visibility = View.GONE
                binding.textnotfound.visibility = View.VISIBLE
                progressDialog.dismiss()

            }
        })

        viewProductModel.responseMessage.observe(this@ProductActivity, Observer {
            progressDialog.dismiss()
            binding.recycleviewproduct.visibility = View.GONE
            Toast.makeText(this@ProductActivity, "Invalid Pincode", Toast.LENGTH_LONG).show()
        })
        viewProductModel.getProductlist(pincode)

//        },3000)



        mAdapter.setOnOrderItemClicked(object : OnProductClickedHandler {
            override fun onProductClickedHandler(position: Int, productid: Int) {

                progressDialog.show()

//                Handler(Looper.getMainLooper()).postDelayed({

                viewProductModel.addtocart.observe(this@ProductActivity, Observer {
                    if (it.IsSuccess == true) {
                        getproductcount()
                    } else {
                        Toast.makeText(
                            this@ProductActivity,
                            "Something went wrong! Unable to add product into cart",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })


                viewProductModel.getAddProductInCart(1, productid, AppUtils2.customerid.toInt())

                viewProductModel.productcount.observe(this@ProductActivity, Observer {
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
                viewProductModel.CreateEventNotificationResponse.observe(this@ProductActivity, Observer {
                    if (it.IsSuccess == true) {
                        Toast.makeText(
                            this@ProductActivity,
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
    }

    private fun getProductslist2(pincode: String) {
//        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
//        progressDialog.setCancelable(false)
        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this@ProductActivity, LinearLayoutManager.VERTICAL, false)
        mAdapter = ProductAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        Handler(Looper.getMainLooper()).postDelayed({

            viewProductModel.productlist.observe(this@ProductActivity, Observer {

                if (it != null) {
//                Log.e("TAG","DataUi:"+it)
                    progressDialog.dismiss()
                    mAdapter.setProductList(it, this@ProductActivity, viewProductModel)
                    binding.recycleviewproduct.visibility = View.VISIBLE
                    binding.textnotfound.visibility = View.GONE

                } else {
                    binding.recycleviewproduct.visibility = View.GONE
                    binding.textnotfound.visibility = View.VISIBLE
                    progressDialog.dismiss()

                }
            })

            viewProductModel.responseMessage.observe(this@ProductActivity,Observer {
                progressDialog.dismiss()
                binding.recycleviewproduct.visibility = View.GONE
                Toast.makeText(this@ProductActivity, "Invalid Pincode", Toast.LENGTH_LONG).show()
            })
            viewProductModel.getProductlist(pincode)

        },1000)



        mAdapter.setOnOrderItemClicked(object : OnProductClickedHandler {
            override fun onProductClickedHandler(position: Int, productid: Int) {

                progressDialog.show()

//                Handler(Looper.getMainLooper()).postDelayed({

                viewProductModel.addtocart.observe(this@ProductActivity, Observer {
                    if (it.IsSuccess == true) {
                        getproductcount()
                    } else {
                        Toast.makeText(
                            this@ProductActivity,
                            "Something went wrong! Unable to add product into cart",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })


                viewProductModel.getAddProductInCart(1, productid, AppUtils2.customerid.toInt())

                viewProductModel.productcount.observe(this@ProductActivity, Observer {
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
                viewProductModel.CreateEventNotificationResponse.observe(this@ProductActivity, Observer {
                    if (it.IsSuccess == true) {
                        Toast.makeText(
                            this@ProductActivity,
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
    }


    private fun getproductcount() {

        progressDialog.show()

        viewProductModel.productcount.observe(this, Observer {
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
    }

    override fun onResume() {
        super.onResume()
        viewProductModel.productcount.observe(this, Observer {
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