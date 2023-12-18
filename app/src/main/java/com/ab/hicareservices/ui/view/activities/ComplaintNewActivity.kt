package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityComplaintNewBinding
import com.ab.hicareservices.databinding.ActivityComplaintsBinding
import com.ab.hicareservices.ui.adapter.ProductViewPagerAdapter
import com.ab.hicareservices.ui.adapter.SocialMediaAdapter
import com.ab.hicareservices.ui.view.fragments.ProductComplaintsFragment
import com.ab.hicareservices.ui.viewmodel.HomeActivityViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ComplaintNewActivity : AppCompatActivity() {

    lateinit var binding: ActivityComplaintNewBinding
    private val requestCall = 1
    private lateinit var msocialMediaAdapter: SocialMediaAdapter
    private val viewModels: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_new)
        binding = ActivityComplaintNewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recSocialMedia.layoutManager =
            GridLayoutManager(this, 3)

        msocialMediaAdapter = SocialMediaAdapter(this)

        binding.recSocialMedia.adapter = msocialMediaAdapter

        msocialMediaAdapter.setSocialMedialist(AppUtils2.socialmedia)

        binding.addFab.setOnClickListener{
            showLeadDialog()
        }


        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

        binding.textemail.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "message/rfc822"
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@hicare.in"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "My subject")
            startActivity(Intent.createChooser(intent, "Email via..."))
        }

        binding.getcall.setOnClickListener {
            makePhoneCall()
        }
    }

    private fun makePhoneCall() {
        var number: String = "8828333888"
        if (number.trim { it <= ' ' }.isNotEmpty()) {
//
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$number")
            startActivity(intent)
//
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onBackPressed() {
        super.onBackPressed()
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
            object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, AppUtils2.servicetype) {
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

                viewModels.leadResponse.observe(this, androidx.lifecycle.Observer {
                    if (it.IsSuccess == true) {
                        DesignToast.makeText(this,"Request submitted successfully", Toast.LENGTH_SHORT, DesignToast.TYPE_SUCCESS).show();

//                        Toast.makeText(this, "Request submitted successfully", Toast.LENGTH_LONG).show()
                        alertDialog.cancel()
                    } else {
                        alertDialog.cancel()
                        DesignToast.makeText(this,"Something went to wrong", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                        Toast.makeText(this, "Something went to wrong", Toast.LENGTH_LONG).show()
                    }
                })

                viewModels.postleaderdata(data)

            }

        }


        alertDialog.show()
    }


}
