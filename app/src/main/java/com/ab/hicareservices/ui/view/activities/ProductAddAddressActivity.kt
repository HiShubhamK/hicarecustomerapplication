package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.product.ProductListResponseData
import com.ab.hicareservices.databinding.ActivityAddresslistBinding
import com.ab.hicareservices.databinding.ActivityProductAddAddressBinding
import com.ab.hicareservices.databinding.ActivityProductBinding
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast

class ProductAddAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductAddAddressBinding
    var selectedLocation = ""
    private val viewProductModel: ProductViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    var activityname = ""
    var checkshippingbilling = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_add_address)
        binding = ActivityProductAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog =
            ProgressDialog(this, com.ab.hicareservices.R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        activityname = intent.getStringExtra("AddressActivity").toString()
        checkshippingbilling = intent.getStringExtra("Shipping").toString()

        binding.title.setText("Add A New " + checkshippingbilling + " Address")

        binding.imgLogo.setOnClickListener {
            onBackPressed()
//            if (activityname.equals("AddressActivity")) {
//                val intent = Intent(this@ProductAddAddressActivity, AddressActivity::class.java)
//                startActivity(intent)
//                finish()
//            } else {
//                val intent = Intent(this@ProductAddAddressActivity, AddresslistActivity::class.java)
//                intent.putExtra("shippingaddress", "true")
//                startActivity(intent)
//                finish()
//            }
        }

        if (activityname.equals("AddressActivity")) {

            if (checkshippingbilling.equals("Shipping")) {

                var courses = arrayOf<String?>("Select Address Type", "Home", "Office", "Others")

//                binding.etpincodes.setText(AppUtils2.pincode)

                binding.etpincodes.isEnabled = true
                binding.etpincodes.isClickable = true

                AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

                val arrayAdapter =
                    object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, courses) {
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

                binding.spinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            selectedLocation = binding.spinner.selectedItem.toString()
                            if (selectedLocation != "Select Type") {

                            } else {
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }

                binding.saveBtn.setOnClickListener {
                    if (selectedLocation.toString().trim().equals("Select Address Type") &&
                        binding.etname.text.toString().trim()
                            .equals("") && binding.edtmobileno.text.toString().trim().equals("") &&
                        binding.etemps.text.toString().trim()
                            .equals("") && selectedLocation.toString()
                            .trim().equals("Select Type") &&
                        binding.etflatno.text.toString().trim()
                            .equals("") && binding.etbuildname.text.toString().trim().equals("") &&
                        binding.etstreet.text.toString().trim()
                            .equals("") && binding.etlocality.text.toString().trim().equals("") &&
                        binding.etlandmark.text.toString().trim().equals("") &&
                        binding.etcitites.text.toString().trim()
                            .equals("") && binding.etstate.text.toString().trim().equals("")
                    ) {

//                        Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "All fields are mandatory",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (selectedLocation.toString().trim().equals("Select Address Type")) {
//                        Toast.makeText(this, "Please select Address Type", Toast.LENGTH_SHORT).show()
                        DesignToast.makeText(
                            this,
                            "Please select Address Type",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etname.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.edtmobileno.text.toString().trim()
                            .equals("") && !binding.edtmobileno.text.toString().equals("0000000000")
                    ) {
//                        Toast.makeText(this, "Enter mobile number", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter mobile number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.edtmobileno.text.toString().trim().length < 10) {
//                        Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG)
//                            .show()

                        DesignToast.makeText(
                            this,
                            "Enter correct mobile number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.edtmobileno.text.toString().trim().equals("0000000000")) {
//                        Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG)
//                            .show()

                        DesignToast.makeText(
                            this,
                            "Enter correct mobile number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etemps.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter email address", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter email address",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etemps.text.toString())
                            .matches()
                    ) {
//                        Toast.makeText(this, "Enter correct email address", Toast.LENGTH_LONG)
//                            .show()

                        DesignToast.makeText(
                            this,
                            "Enter correct email address",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (selectedLocation.toString().trim().equals("Select Type")) {
//                        Toast.makeText(this, "Please select service type", Toast.LENGTH_SHORT)
//                            .show()


                        DesignToast.makeText(
                            this,
                            "Please select service type",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etflatno.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter flat number", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter flat number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etbuildname.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter building name", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter building name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etstreet.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter street name", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter street name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etlocality.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter locality", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter locality",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etlandmark.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter landmark", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter landmark",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etcitites.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter city", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter city",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etstate.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter state", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter state",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etpincodes.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter pincode", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter pincode",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etpincodes.text.toString().trim().length < 6) {
//                        Toast.makeText(this, "Enter correct pincode", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter correct pincode",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else {

                        progressDialog.show()

                        viewProductModel.productlist.observe(
                            this@ProductAddAddressActivity,
                            Observer {

                                if (it != null) {
                                    if (it.isNotEmpty()) {

                                        progressDialog.show()
                                        var data = HashMap<String, Any>()
                                        data["Id"] = 0
                                        data["OrderId"] = 0
                                        data["Customer_Id"] = AppUtils2.customerid.toInt()
                                        data["Contact_Person_Name"] = binding.etname.text.toString()
                                        data["Contact_Person_Mobile"] =
                                            binding.edtmobileno.text.toString()
                                        data["Contact_Person_Email"] =
                                            binding.etemps.text.toString()
                                        data["Flat_No"] = binding.etflatno.text.toString()
                                        data["Building_Name"] = binding.etbuildname.text.toString()
                                        data["Street"] = binding.etstreet.text.toString()
                                        data["Locality"] = binding.etlocality.text.toString()
                                        data["Landmark"] = binding.etlandmark.text.toString()
                                        data["City"] = binding.etcitites.text.toString()
                                        data["State"] = binding.etstate.text.toString()
                                        data["Pincode"] = binding.etpincodes.text.toString()
                                        data["Address_Lat"] = ""
                                        data["Address_Long"] = ""
                                        data["AddressType"] = selectedLocation
                                        data["GST_No"] = ""
                                        data["IsDefault"] = false

                                        viewProductModel.getsaveaddressresponse.observe(
                                            this,
                                            Observer {
                                                if (it.IsSuccess == true) {
                                                    SharedPreferenceUtil.setData(
                                                        this,
                                                        "Shippingdata",
                                                        it.Data.toString()
                                                    )
//                                Toast.makeText(
//                                    this,
//                                    "Shipping address added successfully",
//                                    Toast.LENGTH_LONG
//                                ).show()

                                                    SharedPreferenceUtil.setData(this@ProductAddAddressActivity, "pincode", binding.etpincodes.text.toString())

                                                    DesignToast.makeText(
                                                        this,
                                                        "Shipping address added successfully",
                                                        Toast.LENGTH_SHORT,
                                                        DesignToast.TYPE_SUCCESS
                                                    ).show()

                                                    val intent = Intent(
                                                        this@ProductAddAddressActivity,
                                                        AddressActivity::class.java
                                                    )
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                    startActivity(intent)
                                                    finish()

                                                } else {
                                                    Toast.makeText(
                                                        this,
                                                        "Something went to wrong.",
                                                        Toast.LENGTH_LONG
                                                    )
                                                        .show()
                                                }
                                                progressDialog.dismiss()

                                            })

                                        viewProductModel.postSaveAddress(data)

                                    } else {
                                        progressDialog.dismiss()
                                    }
                                } else {
                                    progressDialog.dismiss()

                                }
                            })
                        viewProductModel.responseMessage.observe(
                            this@ProductAddAddressActivity,
                            Observer {
                                progressDialog.dismiss()
                                if (AppUtils2.checkerrormessage == true) {
                                    AppUtils2.checkerrormessage = false
                                    DesignToast.makeText(
                                        this@ProductAddAddressActivity,
                                        "Pincode not serviceable",
                                        Toast.LENGTH_SHORT,
                                        DesignToast.TYPE_ERROR
                                    ).show()
                                }

                            })
                        viewProductModel.getProductlist(binding.etpincodes.text.toString())


//
//                        progressDialog.show()
//                        var data = HashMap<String, Any>()
//                        data["Id"] = 0
//                        data["OrderId"] = 0
//                        data["Customer_Id"] = AppUtils2.customerid.toInt()
//                        data["Contact_Person_Name"] = binding.etname.text.toString()
//                        data["Contact_Person_Mobile"] = binding.edtmobileno.text.toString()
//                        data["Contact_Person_Email"] = binding.etemps.text.toString()
//                        data["Flat_No"] = binding.etflatno.text.toString()
//                        data["Building_Name"] = binding.etbuildname.text.toString()
//                        data["Street"] = binding.etstreet.text.toString()
//                        data["Locality"] = binding.etlocality.text.toString()
//                        data["Landmark"] = binding.etlandmark.text.toString()
//                        data["City"] = binding.etcitites.text.toString()
//                        data["State"] = binding.etstate.text.toString()
//                        data["Pincode"] = binding.etpincodes.text.toString()
//                        data["Address_Lat"] = ""
//                        data["Address_Long"] = ""
//                        data["AddressType"] = selectedLocation
//                        data["GST_No"] = ""
//                        data["IsDefault"] = false
//
//                        viewProductModel.getsaveaddressresponse.observe(this, Observer {
//                            progressDialog.dismiss()
//                            if (it.IsSuccess == true) {
//                                SharedPreferenceUtil.setData(
//                                    this,
//                                    "Shippingdata",
//                                    it.Data.toString()
//                                )
////                                Toast.makeText(
////                                    this,
////                                    "Shipping address added successfully",
////                                    Toast.LENGTH_LONG
////                                ).show()
//
//                                DesignToast.makeText(
//                                    this,
//                                    "Shipping address added successfully",
//                                    Toast.LENGTH_SHORT,
//                                    DesignToast.TYPE_SUCCESS
//                                ).show()
//
//                                val intent = Intent(this@ProductAddAddressActivity, AddressActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                startActivity(intent)
//                                finish()
//
//                            } else {
//                                Toast.makeText(this, "Something went to wrong.", Toast.LENGTH_LONG)
//                                    .show()
//                            }
//                        })
//
//                        viewProductModel.postSaveAddress(data)
                    }
                }

            } else {

                binding.lnraddresstypes.visibility = View.GONE

                var courses = arrayOf<String?>("Select Address Type", "Home", "Office", "Others")

                binding.etpincodes.setText("")

                binding.etpincodes.isEnabled = true
                binding.etpincodes.isClickable = true

                AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

                val arrayAdapter =
                    object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, courses) {
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

                binding.spinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            selectedLocation = binding.spinner.selectedItem.toString()
                            if (selectedLocation != "Select Type") {

                            } else {
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }

                binding.saveBtn.setOnClickListener {
                    if (selectedLocation.toString().trim().equals("Select Address Type") &&
                        binding.etname.text.toString().trim()
                            .equals("") && binding.edtmobileno.text.toString().trim().equals("") &&
                        binding.etemps.text.toString().trim()
                            .equals("") && selectedLocation.toString()
                            .trim().equals("Select Type") &&
                        binding.etflatno.text.toString().trim()
                            .equals("") && binding.etbuildname.text.toString().trim().equals("") &&
                        binding.etstreet.text.toString().trim()
                            .equals("") && binding.etlocality.text.toString().trim().equals("") &&
                        binding.etlandmark.text.toString().trim().equals("") &&
                        binding.etcitites.text.toString().trim()
                            .equals("") && binding.etstate.text.toString().trim().equals("")
                    ) {

//                        Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "All fields are mandatory",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (selectedLocation.toString().trim().equals("Select Address Type")) {
//                        Toast.makeText(this, "Please select Address Type", Toast.LENGTH_SHORT).show()

                        DesignToast.makeText(
                            this,
                            "Please select Address Type",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etname.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.edtmobileno.text.toString().trim()
                            .equals("") && !binding.edtmobileno.text.toString().equals("0000000000")
                    ) {
//                        Toast.makeText(this, "Enter mobile number", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter mobile number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.edtmobileno.text.toString().trim().length < 10) {
                        Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG)
                            .show()
                    } else if (binding.edtmobileno.text.toString().trim().equals("0000000000")) {
//                        Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter correct mobile number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etemps.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter email address", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter email address",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etemps.text.toString())
                            .matches()
                    ) {
//                        Toast.makeText(this, "Enter correct email address", Toast.LENGTH_LONG)
//                            .show()

                        DesignToast.makeText(
                            this,
                            "Enter correct email address",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (selectedLocation.toString().trim().equals("Select Type")) {
//                        Toast.makeText(this, "Please select service type", Toast.LENGTH_SHORT)
//                            .show()

                        DesignToast.makeText(
                            this,
                            "Please select service type",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etflatno.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter flat number", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter flat number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etbuildname.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter building name", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter building name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etstreet.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter street name", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter street name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etlocality.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter locality", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter locality",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etlandmark.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter landmark", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter landmark",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etcitites.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter city", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter city",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etstate.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter state", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter state",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etpincodes.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter pincode", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter pincode",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else if (binding.etpincodes.text.toString().trim().length < 6) {
//                        Toast.makeText(this, "Enter correct pincode", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "Enter correct pincode",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()
                    } else {
                        progressDialog.show()
                        var data = HashMap<String, Any>()
                        data["Id"] = 0
                        data["OrderId"] = 0
                        data["Customer_Id"] = AppUtils2.customerid.toInt()
                        data["Contact_Person_Name"] = binding.etname.text.toString()
                        data["Contact_Person_Mobile"] = binding.edtmobileno.text.toString()
                        data["Contact_Person_Email"] = binding.etemps.text.toString()
                        data["Flat_No"] = binding.etflatno.text.toString()
                        data["Building_Name"] = binding.etbuildname.text.toString()
                        data["Street"] = binding.etstreet.text.toString()
                        data["Locality"] = binding.etlocality.text.toString()
                        data["Landmark"] = binding.etlandmark.text.toString()
                        data["City"] = binding.etcitites.text.toString()
                        data["State"] = binding.etstate.text.toString()
                        data["Pincode"] = binding.etpincodes.text.toString()
                        data["Address_Lat"] = ""
                        data["Address_Long"] = ""
                        data["AddressType"] = selectedLocation
                        data["GST_No"] = ""
                        data["IsDefault"] = false

                        viewProductModel.getsaveaddressresponse.observe(this, Observer {
                            progressDialog.dismiss()
                            if (it.IsSuccess == true) {
                                SharedPreferenceUtil.setData(
                                    this,
                                    "Billingdata",
                                    it.Data.toString()
                                )
//                                Toast.makeText(
//                                    this,
//                                    "Billing address added successfully",
//                                    Toast.LENGTH_LONG
//                                ).show()


//                                SharedPreferenceUtil.setData(this@ProductAddAddressActivity, "pincode", binding.etpincodes.text.toString())

                                DesignToast.makeText(
                                    this,
                                    "Billing address added successfully",
                                    Toast.LENGTH_SHORT,
                                    DesignToast.TYPE_SUCCESS
                                ).show()

                                val intent = Intent(
                                    this@ProductAddAddressActivity,
                                    AddressActivity::class.java
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(this, "Something went to wrong.", Toast.LENGTH_LONG)
                                    .show()
                            }
                        })

                        viewProductModel.postSaveAddress(data)

                    }
                }
            }
        } else
            if (activityname.equals("AddresslistActivity")) {

                if (checkshippingbilling.equals("Shipping")) {

                    var courses =
                        arrayOf<String?>("Select Address Type", "Home", "Office", "Others")

//                binding.etpincodes.setText(AppUtils2.pincode)

                    binding.etpincodes.isEnabled = true
                    binding.etpincodes.isClickable = true

                    AppUtils2.mobileno =
                        SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

                    val arrayAdapter =
                        object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, courses) {
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

                    binding.spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                selectedLocation = binding.spinner.selectedItem.toString()
                                if (selectedLocation != "Select Type") {

                                } else {
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }
                        }

                    binding.saveBtn.setOnClickListener {
                        if (selectedLocation.toString().trim().equals("Select Address Type") &&
                            binding.etname.text.toString().trim()
                                .equals("") && binding.edtmobileno.text.toString().trim()
                                .equals("") &&
                            binding.etemps.text.toString().trim()
                                .equals("") && selectedLocation.toString()
                                .trim().equals("Select Type") &&
                            binding.etflatno.text.toString().trim()
                                .equals("") && binding.etbuildname.text.toString().trim()
                                .equals("") &&
                            binding.etstreet.text.toString().trim()
                                .equals("") && binding.etlocality.text.toString().trim()
                                .equals("") &&
                            binding.etlandmark.text.toString().trim().equals("") &&
                            binding.etcitites.text.toString().trim()
                                .equals("") && binding.etstate.text.toString().trim().equals("")
                        ) {

//                        Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "All fields are mandatory",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()

                        } else if (selectedLocation.toString().trim()
                                .equals("Select Address Type")
                        ) {
//                        Toast.makeText(this, "Please select Address Type", Toast.LENGTH_SHORT).show()
                            DesignToast.makeText(
                                this,
                                "Please select Address Type",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()

                        } else if (binding.etname.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show()
                            DesignToast.makeText(
                                this,
                                "Enter name",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.edtmobileno.text.toString().trim()
                                .equals("") && !binding.edtmobileno.text.toString()
                                .equals("0000000000")
                        ) {
//                        Toast.makeText(this, "Enter mobile number", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter mobile number",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.edtmobileno.text.toString().trim().length < 10) {
//                        Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG)
//                            .show()

                            DesignToast.makeText(
                                this,
                                "Enter correct mobile number",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.edtmobileno.text.toString().trim()
                                .equals("0000000000")
                        ) {
//                        Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG)
//                            .show()

                            DesignToast.makeText(
                                this,
                                "Enter correct mobile number",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()

                        } else if (binding.etemps.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter email address", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter email address",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etemps.text.toString())
                                .matches()
                        ) {
//                        Toast.makeText(this, "Enter correct email address", Toast.LENGTH_LONG)
//                            .show()

                            DesignToast.makeText(
                                this,
                                "Enter correct email address",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (selectedLocation.toString().trim().equals("Select Type")) {
//                        Toast.makeText(this, "Please select service type", Toast.LENGTH_SHORT)
//                            .show()


                            DesignToast.makeText(
                                this,
                                "Please select service type",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etflatno.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter flat number", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter flat number",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etbuildname.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter building name", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter building name",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etstreet.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter street name", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter street name",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etlocality.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter locality", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter locality",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etlandmark.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter landmark", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter landmark",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etcitites.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter city", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter city",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etstate.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter state", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter state",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etpincodes.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter pincode", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter pincode",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etpincodes.text.toString().trim().length < 6) {
//                        Toast.makeText(this, "Enter correct pincode", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter correct pincode",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else {


                            viewProductModel.CreateEventNotificationResponse.observe(
                                this@ProductAddAddressActivity,
                                Observer {
                                    if (it.IsSuccess == true) {
                                        progressDialog.dismiss()

//                Toast.makeText(
//                    this@ProductActivity,
//                    "Thank You! For Notifying Us",
//                    Toast.LENGTH_SHORT
//                ).show()
                                        DesignToast.makeText(
                                            this,
                                            "ThankYou For Notifying Us!",
                                            Toast.LENGTH_SHORT,
                                            DesignToast.TYPE_SUCCESS
                                        ).show();

                                    }
                                })

                            viewProductModel.productlist.observe(
                                this@ProductAddAddressActivity,
                                Observer {

                                    if (it != null) {
                                        if (it.isNotEmpty()) {

                                            progressDialog.show()
                                            var data = HashMap<String, Any>()
                                            data["Id"] = 0
                                            data["OrderId"] = 0
                                            data["Customer_Id"] = AppUtils2.customerid.toInt()
                                            data["Contact_Person_Name"] =
                                                binding.etname.text.toString()
                                            data["Contact_Person_Mobile"] =
                                                binding.edtmobileno.text.toString()
                                            data["Contact_Person_Email"] =
                                                binding.etemps.text.toString()
                                            data["Flat_No"] = binding.etflatno.text.toString()
                                            data["Building_Name"] =
                                                binding.etbuildname.text.toString()
                                            data["Street"] = binding.etstreet.text.toString()
                                            data["Locality"] = binding.etlocality.text.toString()
                                            data["Landmark"] = binding.etlandmark.text.toString()
                                            data["City"] = binding.etcitites.text.toString()
                                            data["State"] = binding.etstate.text.toString()
                                            data["Pincode"] = binding.etpincodes.text.toString()
                                            data["Address_Lat"] = ""
                                            data["Address_Long"] = ""
                                            data["AddressType"] = selectedLocation
                                            data["GST_No"] = ""
                                            data["IsDefault"] = false

                                            viewProductModel.getsaveaddressresponse.observe(
                                                this,
                                                Observer {
                                                    if (it.IsSuccess == true) {
                                                        SharedPreferenceUtil.setData(
                                                            this,
                                                            "Shippingdata",
                                                            it.Data.toString()
                                                        )
//                                Toast.makeText(
//                                    this,
//                                    "Shipping address added successfully",
//                                    Toast.LENGTH_LONG
//                                ).show()

                                                        SharedPreferenceUtil.setData(this@ProductAddAddressActivity, "pincode", binding.etpincodes.text.toString())

                                                        DesignToast.makeText(
                                                            this,
                                                            "Shipping address added successfully",
                                                            Toast.LENGTH_SHORT,
                                                            DesignToast.TYPE_SUCCESS
                                                        ).show()

                                                        val intent = Intent(
                                                            this@ProductAddAddressActivity,
                                                            AddressActivity::class.java
                                                        )
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                        startActivity(intent)
                                                        finish()
                                                        progressDialog.dismiss()


                                                    } else {
                                                        Toast.makeText(
                                                            this,
                                                            "Something went to wrong.",
                                                            Toast.LENGTH_LONG
                                                        )
                                                            .show()
                                                    }
                                                })

                                            viewProductModel.postSaveAddress(data)

                                        } else {
                                            progressDialog.dismiss()
                                        }
                                    } else {
                                        progressDialog.dismiss()

                                    }
                                })
                            viewProductModel.responseMessage.observe(
                                this@ProductAddAddressActivity,
                                Observer {
                                    progressDialog.dismiss()
                                    if (AppUtils2.checkerrormessage == true) {
                                        AppUtils2.checkerrormessage = false
                                        DesignToast.makeText(
                                            this@ProductAddAddressActivity,
                                            "Pincode not serviceable",
                                            Toast.LENGTH_SHORT,
                                            DesignToast.TYPE_ERROR
                                        ).show()
                                    }

                                })
                            viewProductModel.getProductlist(binding.etpincodes.text.toString())


//
//                        progressDialog.show()
//                        var data = HashMap<String, Any>()
//                        data["Id"] = 0
//                        data["OrderId"] = 0
//                        data["Customer_Id"] = AppUtils2.customerid.toInt()
//                        data["Contact_Person_Name"] = binding.etname.text.toString()
//                        data["Contact_Person_Mobile"] = binding.edtmobileno.text.toString()
//                        data["Contact_Person_Email"] = binding.etemps.text.toString()
//                        data["Flat_No"] = binding.etflatno.text.toString()
//                        data["Building_Name"] = binding.etbuildname.text.toString()
//                        data["Street"] = binding.etstreet.text.toString()
//                        data["Locality"] = binding.etlocality.text.toString()
//                        data["Landmark"] = binding.etlandmark.text.toString()
//                        data["City"] = binding.etcitites.text.toString()
//                        data["State"] = binding.etstate.text.toString()
//                        data["Pincode"] = binding.etpincodes.text.toString()
//                        data["Address_Lat"] = ""
//                        data["Address_Long"] = ""
//                        data["AddressType"] = selectedLocation
//                        data["GST_No"] = ""
//                        data["IsDefault"] = false
//
//                        viewProductModel.getsaveaddressresponse.observe(this, Observer {
//                            progressDialog.dismiss()
//                            if (it.IsSuccess == true) {
//                                SharedPreferenceUtil.setData(
//                                    this,
//                                    "Shippingdata",
//                                    it.Data.toString()
//                                )
////                                Toast.makeText(
////                                    this,
////                                    "Shipping address added successfully",
////                                    Toast.LENGTH_LONG
////                                ).show()
//
//                                DesignToast.makeText(
//                                    this,
//                                    "Shipping address added successfully",
//                                    Toast.LENGTH_SHORT,
//                                    DesignToast.TYPE_SUCCESS
//                                ).show()
//
//                                val intent = Intent(this@ProductAddAddressActivity, AddressActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                startActivity(intent)
//                                finish()
//
//                            } else {
//                                Toast.makeText(this, "Something went to wrong.", Toast.LENGTH_LONG)
//                                    .show()
//                            }
//                        })
//
//                        viewProductModel.postSaveAddress(data)
                        }
                    }

                } else {

                    binding.lnraddresstypes.visibility = View.GONE

                    var courses =
                        arrayOf<String?>("Select Address Type", "Home", "Office", "Others")

                    binding.etpincodes.setText("")

                    binding.etpincodes.isEnabled = true
                    binding.etpincodes.isClickable = true

                    AppUtils2.mobileno =
                        SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

                    val arrayAdapter =
                        object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, courses) {
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

                    binding.spinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                selectedLocation = binding.spinner.selectedItem.toString()
                                if (selectedLocation != "Select Type") {

                                } else {
                                }
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                            }
                        }

                    binding.saveBtn.setOnClickListener {
                        if (selectedLocation.toString().trim().equals("Select Address Type") &&
                            binding.etname.text.toString().trim()
                                .equals("") && binding.edtmobileno.text.toString().trim()
                                .equals("") &&
                            binding.etemps.text.toString().trim()
                                .equals("") && selectedLocation.toString()
                                .trim().equals("Select Type") &&
                            binding.etflatno.text.toString().trim()
                                .equals("") && binding.etbuildname.text.toString().trim()
                                .equals("") &&
                            binding.etstreet.text.toString().trim()
                                .equals("") && binding.etlocality.text.toString().trim()
                                .equals("") &&
                            binding.etlandmark.text.toString().trim().equals("") &&
                            binding.etcitites.text.toString().trim()
                                .equals("") && binding.etstate.text.toString().trim().equals("")
                        ) {

//                        Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "All fields are mandatory",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()

                        } else if (selectedLocation.toString().trim()
                                .equals("Select Address Type")
                        ) {
//                        Toast.makeText(this, "Please select Address Type", Toast.LENGTH_SHORT).show()

                            DesignToast.makeText(
                                this,
                                "Please select Address Type",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etname.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter name",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.edtmobileno.text.toString().trim()
                                .equals("") && !binding.edtmobileno.text.toString()
                                .equals("0000000000")
                        ) {
//                        Toast.makeText(this, "Enter mobile number", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter mobile number",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.edtmobileno.text.toString().trim().length < 10) {
                            Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG)
                                .show()
                        } else if (binding.edtmobileno.text.toString().trim()
                                .equals("0000000000")
                        ) {
//                        Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter correct mobile number",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etemps.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter email address", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter email address",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etemps.text.toString())
                                .matches()
                        ) {
//                        Toast.makeText(this, "Enter correct email address", Toast.LENGTH_LONG)
//                            .show()

                            DesignToast.makeText(
                                this,
                                "Enter correct email address",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (selectedLocation.toString().trim().equals("Select Type")) {
//                        Toast.makeText(this, "Please select service type", Toast.LENGTH_SHORT)
//                            .show()

                            DesignToast.makeText(
                                this,
                                "Please select service type",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etflatno.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter flat number", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter flat number",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etbuildname.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter building name", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter building name",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etstreet.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter street name", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter street name",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etlocality.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter locality", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter locality",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etlandmark.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter landmark", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter landmark",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etcitites.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter city", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter city",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etstate.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter state", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter state",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etpincodes.text.toString().trim().equals("")) {
//                        Toast.makeText(this, "Enter pincode", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter pincode",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else if (binding.etpincodes.text.toString().trim().length < 6) {
//                        Toast.makeText(this, "Enter correct pincode", Toast.LENGTH_LONG).show()

                            DesignToast.makeText(
                                this,
                                "Enter correct pincode",
                                Toast.LENGTH_SHORT,
                                DesignToast.TYPE_ERROR
                            ).show()
                        } else {
                            progressDialog.show()
                            var data = HashMap<String, Any>()
                            data["Id"] = 0
                            data["OrderId"] = 0
                            data["Customer_Id"] = AppUtils2.customerid.toInt()
                            data["Contact_Person_Name"] = binding.etname.text.toString()
                            data["Contact_Person_Mobile"] = binding.edtmobileno.text.toString()
                            data["Contact_Person_Email"] = binding.etemps.text.toString()
                            data["Flat_No"] = binding.etflatno.text.toString()
                            data["Building_Name"] = binding.etbuildname.text.toString()
                            data["Street"] = binding.etstreet.text.toString()
                            data["Locality"] = binding.etlocality.text.toString()
                            data["Landmark"] = binding.etlandmark.text.toString()
                            data["City"] = binding.etcitites.text.toString()
                            data["State"] = binding.etstate.text.toString()
                            data["Pincode"] = binding.etpincodes.text.toString()
                            data["Address_Lat"] = ""
                            data["Address_Long"] = ""
                            data["AddressType"] = selectedLocation
                            data["GST_No"] = ""
                            data["IsDefault"] = false

                            viewProductModel.getsaveaddressresponse.observe(this, Observer {
                                if (it.IsSuccess == true) {
                                    SharedPreferenceUtil.setData(
                                        this,
                                        "Billingdata",
                                        it.Data.toString()
                                    )
//                                Toast.makeText(
//                                    this,
//                                    "Billing address added successfully",
//                                    Toast.LENGTH_LONG
//                                ).show()

                                    DesignToast.makeText(
                                        this,
                                        "Billing address added successfully",
                                        Toast.LENGTH_SHORT,
                                        DesignToast.TYPE_SUCCESS
                                    ).show()

                                    val intent = Intent(
                                        this@ProductAddAddressActivity,
                                        AddressActivity::class.java
                                    )
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(intent)
                                    finish()


                                    progressDialog.dismiss()


                                } else {
                                    Toast.makeText(
                                        this,
                                        "Something went to wrong.",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            })

                            viewProductModel.postSaveAddress(data)

                        }
                    }
                }
            } else {

                var courses = arrayOf<String?>("Select Address Type", "Home", "Office", "Others")

                binding.etpincodes.setText(AppUtils2.pincode)

                binding.etpincodes.isEnabled = false
                binding.etpincodes.isClickable = false

                AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

                val arrayAdapter =
                    object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, courses) {
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

                binding.spinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            selectedLocation = binding.spinner.selectedItem.toString()
                            if (selectedLocation != "Select Type") {

                            } else {
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                        }
                    }

                binding.saveBtn.setOnClickListener {
                    if (selectedLocation.toString().trim().equals("Select Address Type") &&
                        binding.etname.text.toString().trim()
                            .equals("") && binding.edtmobileno.text.toString().trim().equals("") &&
                        binding.etemps.text.toString().trim()
                            .equals("") && selectedLocation.toString()
                            .trim().equals("Select Type") &&
                        binding.etflatno.text.toString().trim()
                            .equals("") && binding.etbuildname.text.toString().trim().equals("") &&
                        binding.etstreet.text.toString().trim()
                            .equals("") && binding.etlocality.text.toString().trim().equals("") &&
                        binding.etlandmark.text.toString().trim().equals("") &&
                        binding.etcitites.text.toString().trim()
                            .equals("") && binding.etstate.text.toString().trim().equals("")
                    ) {

//                    Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()

                        DesignToast.makeText(
                            this,
                            "All fields are mandatory",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()


                    } else if (selectedLocation.toString().trim().equals("Select Address Type")) {
//                    Toast.makeText(this, "Please select Address Type", Toast.LENGTH_SHORT).show()
                        DesignToast.makeText(
                            this,
                            "Please select Address Type",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etname.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.edtmobileno.text.toString().trim()
                            .equals("") && !binding.edtmobileno.text.toString().equals("0000000000")
                    ) {
//                    Toast.makeText(this, "Enter mobile number", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter mobile number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.edtmobileno.text.toString().trim().length < 10) {
//                    Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter correct mobile number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.edtmobileno.text.toString().trim().equals("0000000000")) {
//                    Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter correct mobile number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etemps.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter email address", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter email address",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etemps.text.toString())
                            .matches()
                    ) {
//                    Toast.makeText(this, "Enter correct email address", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter correct email address",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (selectedLocation.toString().trim().equals("Select Type")) {
//                    Toast.makeText(this, "Please select service type", Toast.LENGTH_SHORT).show()
                        DesignToast.makeText(
                            this,
                            "Please select service type",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etflatno.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter flat number", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter flat number",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etbuildname.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter building name", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter building name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etstreet.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter street name", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter street name",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etlocality.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter locality", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter locality",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etlandmark.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter landmark", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter landmark",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etcitites.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter city", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter city",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etstate.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter state", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter state",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etpincodes.text.toString().trim().equals("")) {
//                    Toast.makeText(this, "Enter pincode", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter pincode",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else if (binding.etpincodes.text.toString().trim().length < 6) {
//                    Toast.makeText(this, "Enter correct pincode", Toast.LENGTH_LONG).show()
                        DesignToast.makeText(
                            this,
                            "Enter correct pincode",
                            Toast.LENGTH_SHORT,
                            DesignToast.TYPE_ERROR
                        ).show()

                    } else {
                        progressDialog.show()
                        var data = HashMap<String, Any>()
                        data["Id"] = 0
                        data["OrderId"] = 0
                        data["Customer_Id"] = AppUtils2.customerid.toInt()
                        data["Contact_Person_Name"] = binding.etname.text.toString()
                        data["Contact_Person_Mobile"] = binding.edtmobileno.text.toString()
                        data["Contact_Person_Email"] = binding.etemps.text.toString()
                        data["Flat_No"] = binding.etflatno.text.toString()
                        data["Building_Name"] = binding.etbuildname.text.toString()
                        data["Street"] = binding.etstreet.text.toString()
                        data["Locality"] = binding.etlocality.text.toString()
                        data["Landmark"] = binding.etlandmark.text.toString()
                        data["City"] = binding.etcitites.text.toString()
                        data["State"] = binding.etstate.text.toString()
                        data["Pincode"] = binding.etpincodes.text.toString()
                        data["Address_Lat"] = ""
                        data["Address_Long"] = ""
                        data["AddressType"] = selectedLocation
                        data["GST_No"] = ""
                        data["IsDefault"] = false

                        viewProductModel.getsaveaddressresponse.observe(this, Observer {
                            if (it.IsSuccess == true) {
//                            Toast.makeText(
//                                this,
//                                "Shipping address added successfully",
//                                Toast.LENGTH_LONG
//                            ).show()


                                DesignToast.makeText(
                                    this,
                                    "Shipping address added successfully",
                                    Toast.LENGTH_SHORT,
                                    DesignToast.TYPE_SUCCESS
                                ).show()

                                val intent = Intent(
                                    this@ProductAddAddressActivity,
                                    AddresslistActivity::class.java
                                )
                                intent.putExtra("shippingaddress", "true")
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Something went to wrong.", Toast.LENGTH_LONG)
                                    .show()
                            }
                            progressDialog.dismiss()

                        })

                        viewProductModel.postSaveAddress(data)

                    }
                }
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}