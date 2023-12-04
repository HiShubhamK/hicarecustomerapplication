package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.HomeProduct
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.databinding.ActivityPaymentBinding
import com.ab.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.razorpay.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class PaymentActivity : AppCompatActivity(), PaymentResultWithDataListener {

    private lateinit var binding: ActivityPaymentBinding
    var cartlist = mutableListOf<CartlistResponseData>()
    lateinit var datalist: ArrayList<CartlistResponseData>
    lateinit var homeproduct: ArrayList<HomeProduct>

    var data1 = HashMap<String, Any>()

    private val viewProductModel: ProductViewModel by viewModels()
    var payment = ""
    var order_no = ""
    lateinit var options: JSONObject
    var accountId = ""
    var service = ""
    var serviceType = ""
    var orderValueWithTax = ""
    var orderValueWithTaxAfterDiscount = ""
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    var stdvalues = ""
    var product = false
    var shippingdata = ""
    var billingdata = ""
    var flat = ""
    var street = ""
    var landmark = ""
    var locality = ""
    var builingname = ""
    var pincode = ""
    var totaldiscount = ""
    var actualvalue = ""
    var razorpayorderid=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

        shippingdata = SharedPreferenceUtil.getData(this, "Shippingdata", "").toString()

        billingdata = SharedPreferenceUtil.getData(this, "Billingdata", "").toString()

        homeproduct = ArrayList()

        val intent = intent

        datalist = ArrayList()

        datalist = AppUtils2.leaderlist

        for (i in 0 until datalist.size) {
            homeproduct.add(
                HomeProduct(datalist.get(i).ProductId,
                    datalist.get(i).ProductName,
                    datalist.get(i).ProductCode,
                    datalist.get(i).ProductDisplayName,
                    datalist.get(i).ProductThumbnail,
                    datalist.get(i).DiscountType,
                    datalist.get(i).Discount?.toDouble(),
                    datalist.get(i).PricePerQuantity?.toDouble(),
                    datalist.get(i).DiscountedPrice?.toDouble(),
                    0.0,
                    datalist.get(i).ProductWeight?.toDouble(),
                    datalist.get(i).Quantity,
                    0.0f)
            )
        }

        order_no = intent.getStringExtra("ORDER_NO").toString()
        accountId = intent.getStringExtra("ACCOUNT_NO").toString()
        service = intent.getStringExtra("SERVICETYPE_NO").toString()
        serviceType = intent.getStringExtra("SERVICE_TYPE").toString()
        payment = intent.getDoubleExtra("PAYMENT", Double.MIN_VALUE).toDouble().toString()
        stdvalues = intent.getDoubleExtra("Standard_Value__c", Double.MIN_VALUE).toDouble().toString()

        product = intent.getBooleanExtra("Product", false)

        Checkout.sdkCheckIntegration(this)

        if(product==true){

            getSummarydata()
//
//            Handler(Looper.getMainLooper()).postDelayed({

                viewProductModel.razorpayOrderIdResponse.observe(this, Observer {

                    if (it.IsSuccess == true) {
                        razorpayorderid = it.Data.toString()
                        AppUtils2.razorpayorderid = it.Data.toString()
                    } else {

                    }

                })

                viewProductModel.CreateRazorpayOrderId(AppUtils2.productamount.toDouble(), 12342)
//            },1000)
        }else{

            viewProductModel.razorpayOrderIdResponse.observe(this, Observer {

                if (it.IsSuccess == true) {
                    razorpayorderid = it.Data.toString()
                    AppUtils2.razorpayorderid = it.Data.toString()
                } else {

                }

            })

            viewProductModel.CreateRazorpayOrderId(payment.toDouble(),12342)
        }


        if (product == true) {

//            getproductlist()

            getAddressforbilling()

            val notesproduct = prepareNotesProducts()
            options = prepareOptionforProduct(
                notesproduct,
                AppUtils2.productamount
            )

            Log.d("OptionTag",options.toString())


            try {
                Checkout.preload(applicationContext)
                val activity:PaymentActivity = this
                val co = Checkout()
//                co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
                co.setKeyID("rzp_live_2QlgSaiHhGkoo8")

                co.open(this, options)
            }
            catch (e: Exception){
                Toast.makeText(this,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }

        } else {

            val notes = prepareNotes(
                accountId,
                order_no,
                serviceType,
                payment,
                service,
                stdvalues
            )
            options = prepareOption(
                notes,
                serviceType,
                payment
            )

            try {
                val co = Checkout()
//                co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
                co.setKeyID("rzp_live_2QlgSaiHhGkoo8")
                co.open(this, options)
            } catch (e: Exception) {
                Log.d("TAG", "$e")
            }
        }
    }

    fun getSummarydata() {

        viewProductModel.getsummarydata.observe(this, Observer {

//            AppUtils2.productamount = it.FinalAmount!!.toDouble().toString()
            totaldiscount = it.TotalDiscount?.toDouble().toString()
            actualvalue = it.TotalAmount?.toDouble().toString()

        })
        viewProductModel.getCartSummary(AppUtils2.customerid.toInt(), AppUtils2.pincode, "")
    }

    private fun getAddressforbilling() {
        viewProductModel.getaddressbydetailid.observe(this, Observer {

            AppUtils2.flat = it.FlatNo.toString()
            AppUtils2.builingname = it.BuildingName.toString()
            AppUtils2.street = it.Street.toString()
            AppUtils2.locality = it.Locality.toString()
            AppUtils2.landmark = it.Landmark.toString()
            AppUtils2.pincodelast = it.Pincode.toString()
            AppUtils2.city=it.City.toString()
            AppUtils2.state=it.State.toString()
        })
        viewProductModel.getAddressDetailbyId(billingdata?.toInt())
    }

    private fun prepareOptionforProduct(
        notesproduct: JSONObject,
        productamount: String
    ): JSONObject {
        val options = JSONObject()
        options.put("name", "HiCare Services")
        options.put("description", "Product | Customer Mobile App")
        options.put("order_id", AppUtils2.razorpayorderid )
        options.put("image", "https://hicare.in/pub/media/wysiwyg/home/Hyginenew1.png")
        options.put("theme.color", "#2BB77A")
        options.put("currency", "INR")
        options.put("amount", "${AppUtils2.productamount.toDouble().roundToInt()}00")
        options.put("notes", notesproduct)
        val prefill = JSONObject()

//
//        prefill.put("email","akshay.tabib@hicare.in")
//        prefill.put("contact","7738753827")

        prefill.put("email",AppUtils2.customeremail)
        prefill.put("contact",AppUtils2.mobileno)

        options.put("prefill",prefill)
//        options.put("prefill.contact", AppUtils2.mobileno)
//        options.put("prefill.email", AppUtils2.email)
        return options
    }


    override fun onResume() {
        super.onResume()

    }

    private fun prepareNotes(
        accountId: String,
        orderNo: String,
        serviceType: String,
        orderValue: String,
        service: String,
        stdvalues: String
    ): JSONObject {

        val notes = JSONObject()
        notes.put("Account_Id", accountId)
        notes.put("InvoiceNo", "")
        notes.put("OrderNo", orderNo)
        notes.put("Service", service)
        notes.put("OrderValue", stdvalues)
        notes.put("OrderValueAfterDiscount", orderValue)
        notes.put("ETDiscount", 5)
        notes.put("ETPaidAmount", orderValue)
        notes.put("Param1", "")
        notes.put("Param2", "")
        notes.put("Param3", "")
        notes.put("IsEditOrder", true)
        notes.put("IsGeneralLink", false)
        notes.put("OrdersAmountMapping", "")
        notes.put("Service_Type", serviceType)
        return notes
    }

    private fun prepareOption(notes: JSONObject, description: String, amount: String): JSONObject {
        val options = JSONObject()
        options.put("name", "HiCare Services")
        options.put("description", description)
        options.put("image", "https://hicare.in/pub/media/wysiwyg/home/Hyginenew1.png")
        options.put("theme.color", "#2BB77A")
        options.put("currency", "INR")
        options.put("amount", "${amount.toDouble().roundToInt()}00")
        options.put("notes", notes)
        options.put("order_id", AppUtils2.razorpayorderid)
        val prefill = JSONObject()
        prefill.put("email",AppUtils2.customeremail)
        prefill.put("contact",AppUtils2.mobileno)

        options.put("prefill",prefill)

        return options
    }


    override fun onPaymentSuccess(s: String?, response: PaymentData?) {


        Log.d("Paymenttag",response!!.paymentId+" "+response.signature+" "+response.signature)

        if (product == true) {

            try {
                viewProductModel.paymentsuceess.observe(this, Observer {
                    if(it.IsSuccess==true){
                        binding.imgOffer.visibility = View.VISIBLE
                        binding.txtpayment.visibility = View.VISIBLE
                        binding.imgOffererror.visibility = View.GONE
                        SharedPreferenceUtil.setData(this, "Shippingdata", "")
                        SharedPreferenceUtil.setData(this, "Billingdata", "")
                        Toast.makeText(this, "Payment Successfully Done", Toast.LENGTH_LONG).show()
                        SharedPreferenceUtil.setData(this@PaymentActivity, "Paymentback","true")
                        getClearchache()
                        val intent=Intent(this@PaymentActivity,HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        binding.imgOffer.visibility = View.GONE
                        binding.imgOffererror.visibility = View.VISIBLE
                        binding.txtpayment.visibility = View.VISIBLE
                        binding.txtpayment.text = "Payment Failed"

                    }
                })

                var data = HashMap<String, Any>()

                data["HomeProduct"] = homeproduct
                data["AddressId"] = shippingdata.toInt()
                data["BillToAddressId"] = billingdata.toInt()
                data["Pincode"] = AppUtils2.pincode
                data["CartAmount"] = AppUtils2.actualvalue.toDouble()
                data["PayableAmount"] = AppUtils2.productamount.toDouble()
                data["DiscountAmount"] = AppUtils2.totaldiscount.toDouble()+AppUtils2.voucherdiscount.toDouble()
                data["DelieveryCharges"] = 0.0
                data["InstallationCharges"] = 0.0
                data["VoucherCode"] = AppUtils2.vouchercode
                data["SFDC_OrderNo"] = ""
                data["PaymentId"] = response.paymentId.toString()
                data["PayMethod"] = ""
                data["PayStatus"] = ""
                data["PayAmount"] = 0.0
                data["Booking_Source"] = ""
                data["Referred_By_Technician"] = ""
                data["Order_Source"] = ""
                data["Payment_LinkId"] = ""
                data["Razorpay_Payment_Id"] = response.paymentId.toString()
                data["User_Id"] = AppUtils2.customerid.toInt()




                viewProductModel.postSaveSalesOrder(data)

            }catch (e:Exception){
                e.printStackTrace()
            }

        } else {
//
//           Toast.makeText(this@PaymentActivity,response!!.paymentId+" "+response.orderId,Toast.LENGTH_LONG).show()
//
            orderDetailsViewModel.savePaymentResponse.observe(this, Observer {
                if (it.isSuccess == true) {
                    binding.imgOffer.visibility = View.VISIBLE
                    binding.txtpayment.visibility = View.VISIBLE
                    binding.imgOffererror.visibility = View.GONE
//                    SharedPreferenceUtil.setData(this@PaymentActivity, "Paymentback","true")
                    getClearchache()

                    Toast.makeText(this, "Payment Successfully Done", Toast.LENGTH_LONG).show()
                    val intent=Intent(this@PaymentActivity,HomeActivity::class.java)
                    startActivity(intent)
                } else {

                    binding.imgOffer.visibility = View.GONE
                    binding.imgOffererror.visibility = View.VISIBLE
                    binding.txtpayment.visibility = View.VISIBLE
                    binding.txtpayment.text = "Payment Failed"

                }
            })

            data1["razorpay_payment_id"] = response?.paymentId.toString()
            data1["razorpay_order_id"] = AppUtils2.razorpayorderid
            data1["razorpay_signature"] = response?.signature.toString()

            orderDetailsViewModel.saveAppPaymentDetails(data1)

        }


    }

    private fun getClearchache() {
        viewProductModel.getClearCache(AppUtils2.mobileno)
    }

    override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {
        try {
            binding.imgOffer.visibility = View.GONE
            binding.imgOffererror.visibility = View.VISIBLE
            binding.txtpayment.visibility = View.VISIBLE
            binding.txtpayment.text = "Payment Failed"
            Handler(Looper.getMainLooper()).postDelayed({
                onBackPressed()
            }, 500)

        } catch (e: Exception) {

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val data1 = Intent()
        data1.putExtra("title", AppUtils2.paymentsucess)
        finish()
    }


    private fun prepareNotesProducts(): JSONObject {
        val notes = JSONObject()
        notes.put("Name", AppUtils2.cutomername)
        notes.put("Contact", AppUtils2.customermobile)
        notes.put("Email", AppUtils2.customeremail)
        notes.put("InvoiceNo", "Product")
        notes.put("ActualAmount", AppUtils2.productamount)
        notes.put("UserId", AppUtils2.customerid)
        notes.put("AddressId", shippingdata)
        notes.put("FlatNo", AppUtils2.flat)
        notes.put("BuildingName", AppUtils2.builingname)
        notes.put("Street", AppUtils2.street)
        notes.put("Landmark", AppUtils2.landmark)
        notes.put("Pincode", AppUtils2.pincode)
        notes.put("City", AppUtils2.city)
        notes.put("Locality", AppUtils2.locality)
        return notes
    }

}