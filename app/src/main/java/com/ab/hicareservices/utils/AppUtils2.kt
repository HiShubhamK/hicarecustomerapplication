package com.ab.hicareservices.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.ab.hicareservices.data.model.dashboard.SocialMediadata
import com.ab.hicareservices.data.model.getslots.TimeSlot
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.data.model.productcomplaint.productdetails.ComplaintAttachment
import com.ab.hicareservices.ui.view.activities.LoginActivity
import com.razorpay.Checkout
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object AppUtils2 {

    var TOKEN = ""
    var NotificationChannelid = ""
    var mobileno=""
    var order_number=""
    var ServiceDate=""
    var paymentsucess=""
    var leaderlist= ArrayList<CartlistResponseData>()
    var timeslotslist=  ArrayList<TimeSlot>()
    var cartcounts=""
    var customerid=""
    var cutomername=""
    var customermobile=""
    var customeremail=""
    var productamount=""
    var actualvalue=""
    var totaldiscount=""
    var pincode=""
    var email=""
    var razorpayorderid=""
    var flat = ""
    var street = ""
    var landmark = ""
    var locality = ""
    var builingname = ""
    var pincodelast = ""
    var city=""
    var state=""
    var changebuttonstatus:Boolean=false
    var postalcode=""
    var shippingdata=""
    var voucherdiscount=""
    var vouchercode=""
    private lateinit var imageListnew: ArrayList<ComplaintAttachment>
    var socialmedia = ArrayList<SocialMediadata>()
    var servicetype=ArrayList<String>()
    lateinit var datalist: MutableList<OrdersData>
    lateinit var Spinnerlist: ArrayList<String>
    var getsummarydata = ArrayList<OrderSummeryData>()
    lateinit var Spinnerlistproduct: ArrayList<String>
    lateinit var SpinnerlistSelect: ArrayList<String>

    var fromdasboardmenu:Boolean=false
    var Activityname=""
    var otp=""
    var checkotptruefalse=false

    @JvmStatic
    fun startPayment(activity: Activity) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val co = Checkout()
        co.setKeyID("rzp_test_sgH3fCu3wJ3T82")

        try {
            val options = JSONObject()
            options.put("name", "HiCare Services")
            options.put("description", "CMS Service Renewal")
            options.put("image","https://hicare.in/pub/media/wysiwyg/home/Hyginenew1.png")
            options.put("theme.color", "#2bb77a")
            options.put("currency", "INR")
            options.put("amount", "40000")
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    fun formatDateTime(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val simpleDateFormatOut = SimpleDateFormat("yyyy-MM-dd")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun formatDateTime2(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val simpleDateFormatOut = SimpleDateFormat("dd.LLL.yyyy")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun formatDateTime3(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val simpleDateFormatOut = SimpleDateFormat("E.LLLL.yyyy")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun formatDateTime4(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val simpleDateFormatOut = SimpleDateFormat("dd-MM-yyyy")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun formatDateTime5(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val simpleDateFormatOut = SimpleDateFormat("dd/MM/yyyy")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun formatDateTimeApi(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val simpleDateFormatOut = SimpleDateFormat("yyyy-MM-dd")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun getCurrentTimeStamp(): String? {
        var s = ""
        try {
            val dateFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
            dateFormatter.isLenient = false
            val today = Date()
            s = dateFormatter.format(today)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return s
    }


    fun isNetworkAvailable(loginActivity: Context): Boolean {
        val connectivityManager = loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }

}