package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.ActivityOrderDetailBinding
import com.ab.hicareservices.databinding.ActivityProductSummaryDetailBinding
import com.ab.hicareservices.ui.adapter.ServiceRequestAdapter
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.adapter.WeeksAdapter
import com.ab.hicareservices.ui.handler.OnServiceRequestClickHandler
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceViewModel
import com.ab.hicareservices.utils.AppUtils2
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        binding = ActivityProductSummaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
//       
//

        val intent = intent
        ProductDisplayName = intent.getStringExtra("ProductDisplayName").toString()
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


//        binding.help.setOnClickListener {
//            val intent = Intent(this, AddComplaintsActivity::class.java)
//            intent.putExtra("orderNo", orderNo)
//            intent.putExtra("serviceType", serviceType)
//            intent.putExtra("OrderStatus", OrderStatus)
//
//            startActivity(intent)
//        }

        try {

            binding.payNowBtn.setOnClickListener {
                try {


//                    val intent = Intent(this, PaymentActivity::class.java)
//                    intent.putExtra("ORDER_NO", orderNo)
//                    intent.putExtra("ACCOUNT_NO", accountId)
//                    intent.putExtra("SERVICETYPE_NO", service)
//                    intent.putExtra("SERVICE_TYPE",serviceType)
//                    intent.putExtra("PAYMENT", orderValueWithTax)
//                    intent.putExtra("Standard_Value__c",ShippingCharge)
//                    activityResultLauncher.launch(intent)

//                    val co = Checkout()
//                    co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
//                    co.open(requireActivity(), options)
                } catch (e: Exception) {
                    Log.d("TAG", "$e")
                }
            }
        } catch (e: Exception) {

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
            progressDialog.dismiss()
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
            binding.orderNoTv.text = ":" + orderNo
            binding.dateTv.text = ":" +AppUtils2.formatDateTime4(OrderDate)
            binding.tvTax.text = "" + Tax+"%"
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
                if (Discount != null) "₹ ${Math.round(Discount.toDouble())}" else "₹ 0"
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

            if (OrderStatus.toString()=="Booked"){
                binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))
                binding.statusTv.text = OrderStatus

            }else {
                binding.statusTv.setTextColor(Color.parseColor("#FB8C00"))
                binding.statusTv.text = OrderStatus

            }

        }catch (e:Exception){
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

}

