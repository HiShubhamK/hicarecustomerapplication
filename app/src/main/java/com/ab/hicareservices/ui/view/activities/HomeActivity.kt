package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.product.CustomerAddressData
import com.ab.hicareservices.databinding.ActivityMainBinding
import com.ab.hicareservices.ui.handler.PaymentListener
import com.ab.hicareservices.ui.view.fragments.AccountFragment
import com.ab.hicareservices.ui.view.fragments.HomeFragment
import com.ab.hicareservices.ui.view.fragments.OrderDetailsFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.viewmodel.HomeActivityViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), PaymentResultWithDataListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var geocoder: Geocoder
    lateinit var address: List<Address>
    var lat = 0.0
    var lng = 0.0
    var paymentListener: PaymentListener? = null
    var titles: String? = null
    private val viewModel: OtpViewModel by viewModels()
    var token: String? = null
    lateinit var datalist: ArrayList<String>
    private val requestCall = 1
    private val viewModels: HomeActivityViewModel by viewModels()
    private val viewProductModel:ProductViewModel by viewModels()
    var customerid:Int=0
    var  pincode:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        datalist = ArrayList()
        datalist.add("Select Type")

        AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
//        viewModel.validateAccount(AppUtils2.mobileno)


        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatus()
        }, 3000)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->

            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            token = task.result

            Log.e("Token", token.toString())

            var clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", token)
            clipboardManager.setPrimaryClip(clipData)

        })

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getNotificationtoken(token.toString())
        }, 1500)

        binding.addFab.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragment.newInstance()).commit();

//        binding.bottomheadertext.text=AppUtils2.order_number

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    binding.addFab.visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance())
                        .addToBackStack("HomeFragment").commit()
                    true

                }
                R.id.nav_bookings -> {
                    binding.addFab.visibility = View.GONE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProductFragment.newInstance())
                        .addToBackStack("HomeFragment").commit()
                    true
                }
                R.id.nav_account -> {
                    binding.addFab.visibility = View.GONE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, AccountFragment.newInstance())
                        .addToBackStack("AccountFragment").commit()
                    true
                }
                R.id.nav_cart -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance()).addToBackStack("Tag")
                        .commit()

                    true
                }
                R.id.nav_orders -> {
                    binding.addFab.visibility = View.GONE
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, OrdersFragment.newInstance())
                        .addToBackStack("OrdersFragment").commit()
                    true
                }
                else -> false
            }
        }
        binding.bottomNavigation.selectedItemId = R.id.nav_home

        binding.addFab.setOnClickListener {
            showLeadDialog()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            getLeadMethod()
        }, 1500)


//        viewProductModel.customerlogininfo.observe(this, Observer {
//            Toast.makeText(this,it.Data!!.Id.toString(),Toast.LENGTH_LONG).toString()
//            customerid= it.Data!!.Id!!
//        })

        viewProductModel.getCustomerid("7208408308")

        viewProductModel.getProductlist("400601")

        Handler(Looper.getMainLooper()).postDelayed({
            getCustomerAddress()
        }, 1500)

    }

    private fun getCustomerAddress() {
        viewProductModel.cutomeraddress.observe(this, Observer {
        })

        viewProductModel.getCustomerAddress(customerid)

    }

    private fun getLeadMethod() {
        viewModels.spinnerList.observe(this, Observer {
            datalist.addAll(it)
        })

        viewModels.getleaderspinner("pest")
    }


    private fun showLeadDialog() {
        var selectedLocation = ""
        var dateTime = ""
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.layout_lead, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
//        val today: DateTime = DateTime().withTimeAtStartOfDay()
        val edtname = promptsView.findViewById<View>(R.id.edtname) as EditText
        val edtmobile = promptsView.findViewById<View>(R.id.edtmobile) as EditText
        val edtpincode = promptsView.findViewById<View>(R.id.edtpincode) as EditText
        val spinner = promptsView.findViewById<View>(R.id.spinner_lead) as AppCompatSpinner
        val lnrcall = promptsView.findViewById<View>(R.id.getcall) as LinearLayoutCompat
        val btnSubmit = promptsView.findViewById<View>(R.id.btnlead) as Button
        val email = promptsView.findViewById<View>(R.id.textemail) as TextView
        val imgcancels = promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView

        alertDialog.setCancelable(false)

        imgcancels.setOnClickListener { alertDialog.cancel() }

        email.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "message/rfc822"
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("me@somewhere.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "My subject")

            startActivity(Intent.createChooser(intent, "Email via..."))
        }

        AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss aaa z")
        dateTime = simpleDateFormat.format(calendar.time).toString()


        lnrcall.setOnClickListener {
            makePhoneCall()
        }

        val arrayAdapter =
            object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, datalist) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }
        arrayAdapter.setDropDownViewResource(R.layout.spinner_popup)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedLocation = spinner.selectedItem.toString()
                if (selectedLocation != "Select Type") {

                } else {
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnSubmit.setOnClickListener {

            if (edtname.text.toString().trim().equals("")) {
                edtname.setError("Enter name")
            } else if (edtmobile.text.toString().trim().equals("")) {
                edtmobile.setError("Enter mobile number")
            } else if (edtmobile.text.toString().trim().length < 10) {
                edtmobile.setError("Enter correct mobile number")
            } else if (edtmobile.text.toString().trim().equals("0000000000")) {
                edtmobile.setError("Enter correct mobile number")
            } else if (edtpincode.text.toString().trim().equals("")) {
                edtpincode.setError("Enter pincode")
            } else if (edtpincode.text.toString().length < 6) {
                edtpincode.setError("Enter correct pincode")
            } else if (edtpincode.text.toString().trim().equals("000000")) {
                edtpincode.setError("Enter correct pincode")
            } else if (selectedLocation.toString().trim().equals("Select Type")) {
                Toast.makeText(this, "Please select Service type", Toast.LENGTH_SHORT).show()
            } else {
                var data = HashMap<String, Any>()
                data["LMSId"] = ""
                data["SFDCId"] = ""
                data["CallCenterId"] = ""
                data["ServiceType"] = "pest"
                data["Batch_Name"] = ""
                data["Original_Batch_Name"] = ""
                data["Created_On"] = ""
                data["LeadType"] = ""
                data["Salutation"] = ""
                data["FirstName"] = edtname.text.toString()
                data["LastName"] = "."
                data["Mobile"] = edtmobile.text.toString()
                data["AltMobile"] = ""
                data["Email"] = ""
                data["Company"] = ""
                data["EmployeeCount"] = 0
                data["Service"] = selectedLocation.toString()
                data["ServiceCategory"] = ""
                data["ServiceSubCategory"] = ""
                data["FlatNo"] = ""
                data["Building"] = ""
                data["Street"] = ""
                data["Locality"] = ""
                data["Landmark"] = ""
                data["City"] = ""
                data["State"] = ""
                data["Pincode"] = edtpincode.text.toString()
                data["Lat"] = ""
                data["Long"] = ""
                data["Priority"] = 0
                data["Agency"] = ""
                data["Utm_Campaign"] = "Mobile app"
                data["Utm_Source"] = "Mobile app"
                data["Utm_Sub_Source"] = "Mobile app"
                data["BHK"] = ""
                data["Status"] = ""
                data["Service_Value"] = ""
                data["PaymentMode"] = ""
                data["Lead_Source"] = "Mobile app"
                data["Lead_Sub_Source"] = "Mobile app"
                data["Remark"] = ""
                data["Gclid"] = ""
                data["Utm_Medium"] = "Mobile app"
                data["Utm_Content"] = "Mobile app"
                data["Utm_Term"] = "Mobile app"
                data["Campaign_Url"] = ""

                viewModels.leadResponse.observe(this, Observer {
                    if (it.IsSuccess == true) {
                        alertDialog.cancel()
                    } else {
                        alertDialog.cancel()
                        Toast.makeText(this, "Something went to wrong", Toast.LENGTH_LONG).show()
                    }
                })
                viewModels.postleaderdata(data)
            }

        }


        alertDialog.show()
    }

    private fun makePhoneCall() {
        var number: String = "8976399055"
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val dial = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }

    //
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun checkUserStatus() {
        val mobileNo = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        if (mobileNo == "-1") {
            val i = Intent(this, LoginActivity::class.java)
            //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }
    }

    override fun onPaymentSuccess(p0: String?, response: PaymentData?) {
        if (response != null) {
            Toast.makeText(this, "${response.paymentId}", Toast.LENGTH_SHORT).show()
            paymentListener?.onPaymentSuccess(p0, response)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        paymentListener?.onPaymentError(p0, p1, response)
    }

    fun setOnPaymentListener(paymentListener: PaymentListener?) {
        this.paymentListener = paymentListener
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (binding.bottomNavigation.getSelectedItemId() === R.id.nav_home) {
            finishAffinity()
        } else {
            binding.bottomNavigation.selectedItemId = R.id.nav_home
        }
    }
}