package com.ab.hicareservices.utils

import android.app.Activity
import android.widget.Toast
import com.razorpay.Checkout
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object AppUtils2 {

    var TOKEN = ""
    var mobileno=""
    var order_number=""

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
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val simpleDateFormatOut = SimpleDateFormat("dd-MM-yyyy")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun formatDateTime2(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val simpleDateFormatOut = SimpleDateFormat("dd.LLLL.yyyy")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun formatDateTime3(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val simpleDateFormatOut = SimpleDateFormat("dd-LLL-yyyy")
        val parsedDate = simpleDateFormat.parse(dateTime)
        return simpleDateFormatOut.format(parsedDate)
    }

    fun formatDateTime4(dateTime: String): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val simpleDateFormatOut = SimpleDateFormat("dd-MM-yyyy")
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

}