package com.ab.hicareservices.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.ab.hicareservices.data.model.dashboard.SocialMediadata
import com.ab.hicareservices.data.model.getslots.TimeSlot
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.data.model.productcomplaint.productdetails.ComplaintAttachment
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.ui.view.activities.LoginActivity
import com.razorpay.Checkout
import org.json.JSONObject
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
    var Latt=""
    var Longg=""
    var billingAddress=""
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
    var versionname=""
    var ISChecklocationpermission=false
    var IsCheckbutton=false
    var servicecode =""
    lateinit var getServicePlanResponseData: ArrayList<GetServicePlanResponseData>
    var bookingserviceprice=""
    var bookingdiscount=""
    var bookingdiscountedprice=""
    var planid=""
    var bookingserviceaddress=""
    var ServicePlanName=""
    var spcode=""
    var AppointmentStart=""
    var AppointmentEnd=""
    var addresscode=""
    var cutomerid=""
    var serviceemail=""
    var bhk=""
    var Appointmentdataforcheckout=" "
    var checkerrormessage=false
    var checkloging=false
    var vouchercodedata=""
    var finalamounts=""
    var voucherdiscounts=""

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


    fun requestcode(dateTime: String): String{
        var codestatus=""
        if(dateTime.equals("200")){
          codestatus="200"
        }else if(dateTime.equals("401")){
            codestatus="401"
        }else if(dateTime.equals("500")){
            codestatus="500"
        }else{
            codestatus=""
        }
        return codestatus
    }
    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())       //yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        sdf.timeZone = TimeZone.getTimeZone("UTC") // Set timezone to UTC for Zulu time
        return sdf.format(Date())
    }

    fun getCurrentDateTimeplusone(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Get current date
        val currentDate = Calendar.getInstance()

        // Add one day to the current date
        currentDate.add(Calendar.DAY_OF_MONTH, 1)

        return sdf.format(currentDate.time)
    }
    fun getCurrentDateTimeminusone(dateTime: Date): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())  // Adjust format as needed

        // Validate input format
//        try {
//            sdf.parse(dateTime)  // Check if parsing is successful
//        } catch (e: ParseException) {
//            throw IllegalArgumentException("Invalid datetime format: $dateTime")
//        }

        // Parse input datetime
//        val parsedDate = sdf.parse(dateTime)

        // Create calendar object and subtract one day
        val calendar = Calendar.getInstance().apply {
            time = dateTime
            add(Calendar.DAY_OF_MONTH, -1)
        }

        // Format and return the previous datetime
        return sdf.format(calendar.time)
    }
//    fun getCurrentDateTimeminusone(dateTime: String): String {
//        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//
//        // Get current date
//        val currentDate = Calendar.getInstance()
//
//        // Add one day to the current date
//        currentDate.add(Calendar.DAY_OF_MONTH, -1)
//
//        return sdf.format(dateTime)
//    }

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }


    fun formatDatetime(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }

    fun formatDatetimeone(inputDate: String): String {
        val sdf24 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val sdf12 = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        val date = sdf24.parse(inputDate)
        return sdf12.format(date)

    }

    fun convertTo12HourFormat(time24: String): String {
        val sdf24 = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val sdf12 = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        val date = sdf24.parse(time24)
        return sdf12.format(date)
    }


    fun convertTimeFormat(originalTime: String): String {
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val outputFormat = SimpleDateFormat("hh:mm:ss a", Locale.US)

        // Parse the original time
        val date = inputFormat.parse(originalTime)

        // Format the parsed date into the desired format
        return outputFormat.format(date)
    }

    fun dateformatterfroslot(inputDate:String):String{
        val parsedDate = LocalDate.parse(inputDate)
        val outputDate = parsedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        return outputDate
    }

}