package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityBookInspectionBinding
import com.ab.hicareservices.ui.viewmodel.HomeActivityViewModel
import com.ab.hicareservices.utils.AppUtils2
import java.util.HashMap


class BookInspectionActivity : AppCompatActivity() {
    private lateinit var binding:ActivityBookInspectionBinding
    lateinit var datalist: ArrayList<String>
    private val viewModels: HomeActivityViewModel by viewModels()
    var selectedLocation:String?=null
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_inspection)
        binding=ActivityBookInspectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        datalist= ArrayList()
        datalist.add("Select Type")
        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        viewModels.spinnerList.observe(this, Observer{
            datalist.addAll(it)
        })

        viewModels.getleaderspinner("pest")

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
        binding.spinner.adapter = arrayAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedLocation = binding.spinner.selectedItem.toString()
                if (selectedLocation != "Select Type") {

                } else {
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

        binding.saveBtn.setOnClickListener {

            if(binding.etname.text.toString().trim().equals("") && binding.edtmobileno.text.toString().trim().equals("") &&
                binding.etemps.text.toString().trim().equals("")  && selectedLocation.toString().trim().equals("Select Type") &&
                binding.etflatno.text.toString().trim().equals("") && binding.etbuildname.text.toString().trim().equals("") &&
                binding.etstreet.text.toString().trim().equals("") && binding.etlocality.text.toString().trim().equals("") &&
                binding.etlandmark.text.toString().trim().equals("") && binding.etpincode.text.toString().trim().equals(""))
//                && binding.etcity.text.toString().trim().equals("") && binding.etstate.text.toString().trim().equals(""))
            {

                Toast.makeText(this,"All fields are mandatory",Toast.LENGTH_LONG).show()

            }
            else if(binding.etname.text.toString().trim().equals("")){
                Toast.makeText(this,"Enter name",Toast.LENGTH_LONG).show()
            }else if(binding.edtmobileno.text.toString().trim().equals("") && !binding.edtmobileno.text.toString().equals("0000000000")){
                Toast.makeText(this,"Enter mobile number",Toast.LENGTH_LONG).show()
            }else if(binding.edtmobileno.text.toString().trim().length<10){
                Toast.makeText(this,"Enter correct mobile number",Toast.LENGTH_LONG).show()
            }else if(binding.edtmobileno.text.toString().trim().equals("0000000000")){
                Toast.makeText(this,"Enter correct mobile number",Toast.LENGTH_LONG).show()
            } else if(binding.etemps.text.toString().trim().equals("") ){
                Toast.makeText(this,"Enter email address",Toast.LENGTH_LONG).show()
            }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etemps.text.toString()).matches()){
                Toast.makeText(this,"Enter correct email address",Toast.LENGTH_LONG).show()
            } else if(selectedLocation.toString().trim().equals("Select Type")){
                Toast.makeText(this,"Please select service type", Toast.LENGTH_SHORT).show()
            }else if(binding.etflatno.text.toString().trim().equals("")){
                Toast.makeText(this,"Enter flat number",Toast.LENGTH_LONG).show()
            }else if(binding.etbuildname.text.toString().trim().equals("")){
                Toast.makeText(this,"Enter building name",Toast.LENGTH_LONG).show()
            }else if(binding.etstreet.text.toString().trim().equals("")){
                Toast.makeText(this,"Enter street name",Toast.LENGTH_LONG).show()
            }else if(binding.etlocality.text.toString().trim().equals("")){
                Toast.makeText(this,"Enter locality",Toast.LENGTH_LONG).show()
            }else if(binding.etlandmark.text.toString().trim().equals("")){
                Toast.makeText(this,"Enter landmark",Toast.LENGTH_LONG).show()
            }
//            else if( binding.etcity.text.toString().trim().equals("")){
//                Toast.makeText(this,"Enter city",Toast.LENGTH_LONG).show()
//            }else if(binding.etstate.text.toString().trim().equals("")) {
//                Toast.makeText(this,"Enter state",Toast.LENGTH_LONG).show()
//            }
            else if(binding.etpincodes.text.toString().trim().equals("")){
                Toast.makeText(this,"Enter pincode",Toast.LENGTH_LONG).show()
            } else if(binding.etpincodes.text.toString().trim().length<6){
                Toast.makeText(this,"Invalid pincode",Toast.LENGTH_LONG).show()
            }else{

                progressDialog.show()

                Handler(Looper.getMainLooper()).postDelayed({

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
                    data["FirstName"] = binding.etname.text.toString()
                    data["LastName"] = "."
                    data["Mobile"] = binding.edtmobileno.text.toString()
                    data["AltMobile"] = ""
                    data["Email"] = binding.etemps.text.toString()
                    data["Company"] = ""
                    data["EmployeeCount"] = 0
                    data["Service"] = selectedLocation.toString()
                    data["ServiceCategory"] = ""
                    data["ServiceSubCategory"] = ""
                    data["FlatNo"] = binding.etflatno.text.toString()
                    data["Building"] = binding.etbuildname.text.toString()
                    data["Street"] = binding.etstreet.text.toString()
                    data["Locality"] = binding.etlocality.text.toString()
                    data["Landmark"] = binding.etlandmark.text.toString()
                    data["City"] = ""
                    data["State"] = ""
                    data["Pincode"] = binding.etpincode.text.toString()
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

                    viewModels.responseMessgae.observe(this, Observer {
                        progressDialog.dismiss()
                        Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                    })

                    viewModels.leadResponse.observe(this, Observer {
                        if (it.IsSuccess == true) {

                            val intent=Intent(this,HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                            progressDialog.dismiss()

                            Toast.makeText(this, "Inspection appointment booked", Toast.LENGTH_LONG)
                                .show()
                            AppUtils2.eventCall(this,"Inspection appointment booked: "+data)

                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Something went to wrong.", Toast.LENGTH_LONG)
                                .show()
                        }

                    })

                    progressDialog.dismiss()

                    viewModels.postleaderdata(data)
                },1000)

            }
        }
    }
}