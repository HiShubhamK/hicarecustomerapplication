package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.databinding.ActivityProductSummaryDetailBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.RelatedProductAdapter
import com.ab.hicareservices.ui.adapter.ServiceRequestAdapter
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.adapter.WeeksAdapter
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.handler.OnRelatedProductClick
import com.ab.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.kofigyan.stateprogressbar.StateProgressBar
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class ProductSummaryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductSummaryDetailBinding
    private val viewModel: ProductViewModel by viewModels()
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    private lateinit var mAdapter: ServiceRequestAdapter
    lateinit var mWeeksAdapter: WeeksAdapter
    lateinit var mSlotAdapter: SlotsAdapter
    private val taskId = ""
    private var date = ""
    var ProductDisplayName = ""
    var orderNo = ""
    var OrderDate = ""
    var OrderId = ""
    private val ORDER_NO = "ORDER_NO"
    private val SERVICE_TYPE = "SERVICE_TYPE"
    private val SERVICE_TYPE_IMG = "SERVICE_TYPE_IMG"
    lateinit var options: JSONObject
    private val viewModels: OtpViewModel by viewModels()
    var OrderStatus: String = ""
    var accountId = ""
    var service = ""
    var orderValueWithTax = 00.00

    //    var discount = ""
    var orderValueWithTaxAfterDiscount = ""
    var Discount: String = ""
    var OrderValuePostDiscount: String = ""
    var Tax: String = ""
    var ShippingCharge = ""

    //remain
    var InstallationCharge = ""
    var ProductThumbnail = ""
    var Quantity = ""
    var PaymentId = ""
    var PaymentStatus = ""
    var PaymentMethod = ""
    var Address = ""
    var OrderValue = ""
    var productid = ""
    var customerid = ""
    var pincode = ""
    var descriptionData = arrayOf("Booked", "Packed", "Dispatched", "Delivered")
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""

    private lateinit var relatedProductAdapter: RelatedProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        binding = ActivityProductSummaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        MyLocationListener(this)

        var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { activityResult ->
                val result = activityResult.resultCode
                val data = activityResult.data
            })

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)


        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }
        binding.cartmenu.setOnClickListener {
            val intent = Intent(this, AddToCartActivity::class.java)
            startActivity(intent)
        }
//
//

        val intent = intent
        ProductDisplayName = intent.getStringExtra("ProductDisplayName").toString()
        OrderId = intent.getStringExtra("OrderId").toString()
        orderNo = intent.getStringExtra("orderNo").toString()
        OrderDate = intent.getStringExtra("OrderDate").toString()
        OrderStatus = intent.getStringExtra("OrderStatus").toString()
        Discount = intent.getStringExtra("Discount").toString()
        OrderValuePostDiscount = intent.getStringExtra("OrderValuePostDiscount").toString()
        Tax = intent.getStringExtra("Tax").toString()
        ShippingCharge = intent.getStringExtra("ShippingCharge").toString()
        InstallationCharge = intent.getStringExtra("InstallationCharge").toString()
        ProductThumbnail = intent.getStringExtra("ProductThumbnail").toString()
        Quantity = intent.getStringExtra("Quantity").toString()
        PaymentId = intent.getStringExtra("PaymentId").toString()
        PaymentStatus = intent.getStringExtra("PaymentStatus").toString()
        PaymentMethod = intent.getStringExtra("PaymentMethod").toString()
        Address = intent.getStringExtra("Address").toString()
        OrderValue = intent.getStringExtra("OrderValue").toString()
        productid = intent.getStringExtra("ProductId").toString()
        customerid = intent.getStringExtra("CustomerId").toString()
        pincode = intent.getStringExtra("Pincode").toString()


        if (ProductThumbnail != null) {
            Picasso.get().load(ProductThumbnail).into(binding.imgType)
        }
        getServiceDetails()

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

        if (orderNo != null) {
            binding.bottomheadertext.visibility = View.VISIBLE
            binding.bottomheadertext.text = orderNo
        } else {
            binding.bottomheadertext.visibility = View.GONE
        }

        getSummarydata()


//        binding.help.setOnClickListener {
//            val intent = Intent(this, AddComplaintsActivity::class.java)
//            intent.putExtra("orderNo", orderNo)
//            intent.putExtra("serviceType", serviceType)
//            intent.putExtra("OrderStatus", OrderStatus)
//
//            startActivity(intent)
//        }

        try {
            progressDialog.show()
            binding.yourStateProgressBarId.setStateDescriptionData(descriptionData)
//            when (descriptionData.equals(OrderStatus)) {
//                "" -> binding.yourStateProgressBarId.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
//                2 -> binding.yourStateProgressBarId.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
//                3 -> binding.yourStateProgressBarId.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
//                4 -> binding.yourStateProgressBarId.setAllStatesCompleted(true)
//            }

            if (descriptionData.contains(OrderStatus)) {
                if (OrderStatus == "Booked") {
                    binding.yourStateProgressBarId.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
                } else if (OrderStatus == "Packed") {
                    binding.yourStateProgressBarId.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)

                } else if (OrderStatus == "Dispatched" || OrderStatus.equals("Shipped")) {
                    binding.yourStateProgressBarId.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)

                } else if (OrderStatus == "Delivered") {
                    binding.yourStateProgressBarId.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)

                } else {
                    binding.btnNeedhelp.visibility=View.GONE
                    binding.yourStateProgressBarId.visibility = View.GONE
                    binding.txtStatusTitle.visibility = View.GONE
                    binding.statusTv.visibility = View.VISIBLE
                    binding.viewline.visibility = View.GONE
                }
            } else {
                binding.btnNeedhelp.visibility=View.GONE

                binding.yourStateProgressBarId.visibility = View.GONE
                binding.txtStatusTitle.visibility = View.GONE
                binding.statusTv.visibility = View.VISIBLE
                binding.viewline.visibility = View.GONE


            }
            binding.recRelatedProduct.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            relatedProductAdapter = RelatedProductAdapter(this, viewModel)


            binding.recRelatedProduct.adapter = relatedProductAdapter
//        binding.recRelatedProduct.addItemDecoration(CirclePagerIndicatorDecoration())
            binding.recRelatedProduct.layoutManager!!.smoothScrollToPosition(
                binding.recRelatedProduct,
                RecyclerView.State(),
                binding.recRelatedProduct.adapter!!.itemCount
            )
            viewModel.producDetailsResponse.observe(this, Observer {
                if (it.RelatedProducts != null) {
                    binding.recRelatedProduct.visibility = View.VISIBLE
                    relatedProductAdapter.setRelatedProduct(it.RelatedProducts, this)
                    progressDialog.dismiss()
                } else {
                    binding.recRelatedProduct.visibility = View.GONE

                }


            })


            viewModel.getProductDetails(productid!!.toInt(), pincode, customerid!!.toInt())

        } catch (e: Exception) {

        }


        relatedProductAdapter.setOnOrderItemClicked(object : OnRelatedProductClick {
            override fun setonaddclicklistener(position: Int, productid: Int, i: Int) {

                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.addtocart.observe(this@ProductSummaryDetailActivity, Observer {
                        progressDialog.dismiss()
                        if(it.IsSuccess==true){
                            DesignToast.makeText(
                                this@ProductSummaryDetailActivity,
                                "Product Added to Cart",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_SUCCESS
                            ).show()
//                            Toast.makeText(this@ProductSummaryDetailActivity,"Product Added to Cart", Toast.LENGTH_LONG).show()
                            getSummarydata()
                        }else{
                            progressDialog.dismiss()
                            DesignToast.makeText(
                                this@ProductSummaryDetailActivity,
                                "Something went to wrong",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
//                            Toast.makeText(this@ProductSummaryDetailActivity,"Something went to wrong", Toast.LENGTH_LONG).show()
                        }

                    })

                    viewModel.getAddProductInCart(i, productid, AppUtils2.customerid.toInt())
                }, 1000)


            }


        })
        binding.btnNeedhelp.setOnClickListener {
            val intent = Intent(this, AddProductComplaintsActivity::class.java)
            try {
                intent.putExtra("complaintactivity",false)
                intent.putExtra("ProductId", productid)
                intent.putExtra("orderNo", orderNo)
                intent.putExtra("displayname", ProductDisplayName)
                intent.putExtra("Created_On", OrderDate)
                intent.putExtra("Complaint_Status",OrderStatus)
                intent.putExtra("OrderId",OrderId)
                intent.putExtra("OrderValuePostDiscount",OrderValuePostDiscount)
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }


    }


    private fun getSummarydata() {
        try{
            viewModel.productcount.observe(this, Observer {
                if (it.IsSuccess == true) {

                    if (it.Data == 0) {
                        binding.cartmenu.visibility = View.INVISIBLE
                    } else {
                        binding.cartmenu.visibility = View.VISIBLE
                        AppUtils2.cartcounts = it.Data.toString()
                        binding.appCompatImageViewd.text = it.Data.toString()
                    }
                } else {
                    binding.cartmenu.visibility = View.INVISIBLE
                }
            })

            viewModel.getProductCountInCar(AppUtils2.customerid.toInt())

        }catch (e:Exception){
            e.printStackTrace()
        }


    }
    private fun getServiceLists(progressDialog: ProgressDialog) {

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        mAdapter = ServiceRequestAdapter()

        binding.recycleView.adapter = mAdapter


    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        return sdf.format(Date())
    }

    private fun getServiceDetails() {
        try {
//        orderDetailsViewModel.orderDetailsData.observe(this) {
//            if (it != null) {
//                val data = it[0]
//                accountId = data.account_Name__r?.customer_id__c.toString()
//                service = data.service_Plan_Name__c.toString()
//                orderValueWithTax = data.order_Value_with_Tax__c.toString().toDouble()
//                discount = (orderValueWithTax.toString().toDouble() * 0.05).toString()
////                 orderValueWithTaxAfterDiscount =
////                     (data.order_Value_with_Tax__c.toString().toDouble() - discount)..toString()
            binding.orderNameTv.text = ProductDisplayName
            binding.orderNoTv.text = ": " + orderNo
            binding.dateTv.text = ": " + AppUtils2.formatDateTime4(OrderDate)
            binding.tvTax.text = "" + Tax + "%"
            binding.tvInstallCharge.text = "₹ " + InstallationCharge
            binding.tvShippingCharges.text = "₹ " + ShippingCharge

            if (PaymentStatus != null) {
                binding.paymentStatusTv.text = PaymentStatus
            } else {
                binding.paymentStatusTv.text = "Unpaid"
            }
            binding.textaddress.text = Address
            binding.tvPaymentId.text = PaymentId
            binding.tvPaymentmethod.text = PaymentMethod

//
//        binding.apartmentSizeTv.text = "Selected Apartment Size - ${data.unit1__c}"
            binding.quantityTv.text = "QTY: ${Quantity}"
//                binding.paymentStatusTv.text = if (data.enable_Payment_Link == false) "Paid" else "Unpaid"
            binding.totalTv.text = "₹ ${OrderValuePostDiscount}"
            binding.priceTv.text = "₹ ${Math.round(OrderValue.toDouble())}"
            binding.discountTv.text =
                if (Discount != null) "- ₹ ${Math.round(Discount.toDouble())}" else "- ₹ 0"
            binding.totalAmountTv.text =
                "₹ ${Math.round(OrderValuePostDiscount!!.toDouble())}"
//                binding.completionDateTv.text = data.end_Date__c ?: "N/A"
//        binding.contactDetailsTv.text =
//            "${data.account_Name__r?.name} | ${data.account_Name__r?.mobile__c}"
            binding.addressTv.text = Address
//        binding.textserviceperiod.text = data.service_Period
//        binding.textdatestart.text =
//            AppUtils2.formatDateTime4(data.start_Date__c.toString())
//        binding.textdateend.text = AppUtils2.formatDateTime4(data.end_Date__c.toString())
//                val notes = prepareNotes(
//                    accountId,
//                    orderNo,
//                    service,
//                    serviceType,
//                    orderValueWithTax?.toDouble(),
//                    orderValueWithTaxAfterDiscount
//                )
//                options = prepareOption(
//                    notes,
//                    data.service_Plan_Name__c.toString(),
//                    orderValueWithTaxAfterDiscount.toString()
//                )
//        if (data.enable_Payment_Link == true) {
//            binding.payNowBtn.visibility = View.VISIBLE
//        } else {
//            binding.payNowBtn.visibility = View.GONE
//        }

            if (OrderStatus.toString() == "Booked") {
                binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))
                binding.statusTv.text = OrderStatus

            } else {
                binding.statusTv.setTextColor(Color.parseColor("#FB8C00"))
                binding.statusTv.text = OrderStatus

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

//
//                if(data.status__c.equals("Active")){
//                    binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))
//                }else {
//                    binding.statusTv.text = data.status__c
//                    binding.statusTv.setTextColor(Color.parseColor("#B71C1C"))
//                }
//


//            }
//        }
//        orderDetailsViewModel.getOrderDetailsByOrderNo(orderNo, serviceType)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        getSummarydata()
    }

}

