package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityAddressBinding
import com.ab.hicareservices.ui.adapter.AddressAdapter
import com.ab.hicareservices.ui.viewmodel.HomeActivityViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import java.util.*

class AddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddressBinding
    private val viewProductModel: ProductViewModel by viewModels()
    private lateinit var mAdapter: AddressAdapter
    lateinit var datalist: ArrayList<String>
    private val viewModels: HomeActivityViewModel by viewModels()
    var shippingdata: String? = ""
    var billingdata: String? = ""
    lateinit var progressDialog: ProgressDialog
    var checkboxcheck: Boolean = false
    var pincodeshipping = ""
    var pincode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progressDialog =
            ProgressDialog(this, com.ab.hicareservices.R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        datalist = ArrayList()
        datalist.add("Select Type")

        pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()


        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        shippingdata = SharedPreferenceUtil.getData(this, "Shippingdata", "").toString()
        billingdata = SharedPreferenceUtil.getData(this, "Billingdata", "").toString()

        if (checkboxcheck == false) {
            checkboxcheck == true
            binding.checkbox.isChecked == false
            binding.checkbox.isClickable == true
        } else {
            checkboxcheck == false
            binding.checkbox.isChecked == true
            binding.checkbox.isClickable == false
        }

        if (!pincode.isNullOrEmpty()) {
            binding.tvPincode.text = "Deliver to pincode " + pincode

        } else {
            binding.tvPincode.visibility = View.GONE
        }

        binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
//            if (b == true) {

                val intent = Intent(this, ProductAddAddressActivity::class.java)
                intent.putExtra("AddressActivity", "AddressActivity")
                intent.putExtra("Shipping", "Billing")
                startActivity(intent)

//                showAddNewAddressdialog("false", binding.checkbox, AppUtils2.pincode)
//            } else {
//
//            }
        }

        binding.imgLogo.setOnClickListener {
            onBackPressed()
//            val intent= Intent(this@AddressActivity,AddToCartActivity::class.java)
//            startActivity(intent)
//            finish()
        }



        if (shippingdata.equals("")) {
            binding.txtshipping.text = ""
            getAddressListdata()
        } else {
            getAddressListdata2()
//            getAddressforshipping()
        }

        if (!billingdata.equals("")) {
            getAddressforbilling()
        } else {
            binding.txtbilling.text = ""
        }


        binding.txtchangeaddress.setOnClickListener {
            val intent = Intent(this, AddresslistActivity::class.java)
            intent.putExtra("shippingaddress", "true")
            startActivity(intent)
        }


        binding.btnnext.setOnClickListener {
            if (binding.txtshipping.text.equals("")) {
                Toast.makeText(this, "Please Add Shipping Address", Toast.LENGTH_SHORT).show()
            } else {
                if (pincodeshipping.equals(AppUtils2.pincode)) {
                    if (billingdata.equals("")) {
                        billingdata = shippingdata
                        SharedPreferenceUtil.setData(this, "Billingdata", billingdata)

                        val intent = Intent(this, OverviewProductDetailsActivity::class.java)
                        intent.putExtra("Billdata", billingdata)
                        intent.putExtra("Shipdata", shippingdata)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, OverviewProductDetailsActivity::class.java)
                        intent.putExtra("Billdata", billingdata)
                        intent.putExtra("Shipdata", shippingdata)
                        startActivity(intent)
                    }
                } else {
                    if (billingdata.equals("")) {
                        billingdata = shippingdata
                        SharedPreferenceUtil.setData(this, "Billingdata", billingdata)

                        val intent = Intent(this, OverviewProductDetailsActivity::class.java)
                        intent.putExtra("Billdata", billingdata)
                        intent.putExtra("Shipdata", shippingdata)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, OverviewProductDetailsActivity::class.java)
                        intent.putExtra("Billdata", billingdata)
                        intent.putExtra("Shipdata", shippingdata)
                        startActivity(intent)
                    }
                }
            }
        }

        binding.lnraddress.setOnClickListener {

            val intent = Intent(this, ProductAddAddressActivity::class.java)
            intent.putExtra("AddressActivity", "AddressActivity")
            intent.putExtra("Shipping", "Shipping")
            startActivity(intent)

//            showAddNewAddressdialog("true", binding.checkbox, AppUtils2.pincode)
        }

    }

    private fun getAddressforbilling() {
        progressDialog.show()
        viewProductModel.getaddressbydetailid.observe(this, Observer {
            progressDialog.dismiss()
            binding.txtbilling.visibility = View.VISIBLE
            binding.txtbilling.text =
                it.FlatNo.toString() + "," + it.BuildingName.toString() + "," + it.Street.toString() + "," +
                        it.Locality.toString() + "," + it.Landmark.toString() + "," + it.Pincode.toString()
        })
        viewProductModel.getAddressDetailbyId(billingdata!!.toInt())
    }


    private fun getAddressforshipping() {
        progressDialog.show()
        viewProductModel.getaddressbydetailid.observe(this, Observer {
            progressDialog.dismiss()

            binding.txtshipping.visibility = View.VISIBLE
            binding.txtshipping.text =
                it.FlatNo.toString() + "," + it.BuildingName.toString() + "," + it.Street.toString() + "," +
                        it.Locality.toString() + "," + it.Landmark.toString() + "," + it.Pincode.toString()
            pincodeshipping = it.Pincode.toString()
        })
        viewProductModel.getAddressDetailbyId(shippingdata!!.toInt())
    }


    private fun getAddressListdata() {

        progressDialog.show()

        viewProductModel.cutomeraddress.observe(this, Observer {

            progressDialog.dismiss()

            for (i in 0 until it.size) {

                var data = it.get(i).Id.toString()
                if (data.equals(shippingdata)) {
                    binding.txtshipping.text =
                        it.get(i).FlatNo.toString() + "," + it.get(i).BuildingName.toString() + "," + it.get(
                            i
                        ).Street.toString() + "," +
                                it.get(i).Locality.toString() + "," + it.get(i).Landmark.toString() + "," + it.get(
                            i
                        ).City.toString() + "," + it.get(i).State.toString() + "," + it.get(i).Pincode.toString()
                    pincodeshipping = it.get(i).Pincode.toString()
                    SharedPreferenceUtil.setData(this@AddressActivity, "pincode", "")
                    SharedPreferenceUtil.setData(this@AddressActivity, "pincode", pincodeshipping)
                    binding.tvPincode.text = "Deliver to pincode " + pincodeshipping
                    break
                } else if (it.get(i).IsDefault == true) {
                    binding.txtshipping.text =
                        it.get(i).FlatNo.toString() + "," + it.get(i).BuildingName.toString() + "," + it.get(
                            i
                        ).Street.toString() + "," +
                                it.get(i).Locality.toString() + "," + it.get(i).Landmark.toString() + "," + it.get(
                            i
                        ).City.toString() + "," + it.get(i).State.toString() + "," + it.get(i).Pincode.toString()
                    pincodeshipping = it.get(i).Pincode.toString()
                    SharedPreferenceUtil.setData(this, "Shippingdata", it.get(i).Id.toString())
                    shippingdata = it.get(i).Id.toString()
                    SharedPreferenceUtil.setData(this@AddressActivity, "pincode", "")
                    SharedPreferenceUtil.setData(this@AddressActivity, "pincode", pincodeshipping)
                    binding.tvPincode.text = "Deliver to pincode " + pincodeshipping
                    break
                } else {
                    binding.txtshipping.text =
                        it.get(i).FlatNo.toString() + "," + it.get(i).BuildingName.toString() + "," + it.get(
                            i
                        ).Street.toString() + "," +
                                it.get(i).Locality.toString() + "," + it.get(i).Landmark.toString() + "," + it.get(
                            i
                        ).City.toString() + "," + it.get(i).State.toString() + "," + it.get(i).Pincode.toString()
                    pincodeshipping = it.get(i).Pincode.toString()
                    SharedPreferenceUtil.setData(this, "Shippingdata", it.get(i).Id.toString())
                    shippingdata = it.get(i).Id.toString()
                    SharedPreferenceUtil.setData(this@AddressActivity, "pincode", "")
                    SharedPreferenceUtil.setData(this@AddressActivity, "pincode", pincodeshipping)
                    binding.tvPincode.text = "Deliver to pincode " + pincodeshipping
                    break
                }
            }
        })

        viewProductModel.getCustomerAddress(AppUtils2.customerid.toInt())
    }

    private fun getAddressListdata2() {

        progressDialog.show()

        viewProductModel.cutomeraddress.observe(this, Observer {

            progressDialog.dismiss()

            for (i in 0 until it.size) {

                var data = it.get(i).Id.toString()
                if (data.equals(shippingdata)) {
                    binding.txtshipping.text =
                        it.get(i).FlatNo.toString() + "," + it.get(i).BuildingName.toString() + "," + it.get(
                            i
                        ).Street.toString() + "," +
                                it.get(i).Locality.toString() + "," + it.get(i).Landmark.toString() + "," + it.get(
                            i
                        ).City.toString() + "," + it.get(i).State.toString() + "," + it.get(i).Pincode.toString()
                    pincodeshipping = it.get(i).Pincode.toString()
                    break
                }
            }
        })

        viewProductModel.getCustomerAddress(AppUtils2.customerid.toInt())
    }


    private fun showAddNewAddressdialog(
        b: String,
        appCompatCheckBox: AppCompatCheckBox,
        pincode: String
    ) {
        var selectedLocation = ""
        var dateTime = ""
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.layout_addnew_addreess, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()

        if (b.equals("true")) {

            var courses = arrayOf<String?>("Select Address Type", "Home", "Office", "Others")

            val etname = promptsView.findViewById<View>(R.id.etname) as EditText
            val edtmobileno = promptsView.findViewById<View>(R.id.edtmobileno) as EditText
            val etemps = promptsView.findViewById<View>(R.id.etemps) as TextView
            val etflatno = promptsView.findViewById<View>(R.id.etflatno) as EditText
            val etbuildname = promptsView.findViewById<View>(R.id.etbuildname) as EditText
            val etstreet = promptsView.findViewById<View>(R.id.etstreet) as EditText
            val etlocality = promptsView.findViewById<View>(R.id.etlocality) as TextView
            val etlandmark = promptsView.findViewById<View>(R.id.etlandmark) as EditText
            val etpincode = promptsView.findViewById<View>(R.id.etpincodes) as EditText
            val etstate = promptsView.findViewById<View>(R.id.etstate) as EditText
            val etcity = promptsView.findViewById<View>(R.id.etcitites) as EditText

            val imgcancels = promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView
            val spinner = promptsView.findViewById<View>(R.id.spinner) as AppCompatSpinner
            val saveBtn = promptsView.findViewById<View>(R.id.saveBtn) as Button


            alertDialog.setCancelable(false)

            imgcancels.setOnClickListener {
                alertDialog.cancel()
                binding.checkbox.isChecked == false
                appCompatCheckBox.isChecked = false
            }
            AppUtils2.mobileno = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()

            val arrayAdapter =
                object : ArrayAdapter<String>(this, R.layout.spinner_popup, courses) {
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
                    if (selectedLocation != "Select Address Type") {

                    } else {

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            etpincode.setText(pincode)

            etpincode.isEnabled = false
            etpincode.isClickable = false

            saveBtn.setOnClickListener {
                if (selectedLocation.toString().trim().equals("Select Address Type") &&
                    etname.text.toString().trim().equals("") && edtmobileno.text.toString().trim()
                        .equals("") &&
                    etemps.text.toString().trim().equals("") && selectedLocation.toString().trim()
                        .equals("Select Type") &&
                    etflatno.text.toString().trim().equals("") && etbuildname.text.toString().trim()
                        .equals("") &&
                    etstreet.text.toString().trim().equals("") && etlocality.text.toString().trim()
                        .equals("") &&
                    etlandmark.text.toString().trim().equals("") &&
                    etcity.text.toString().trim().equals("") &&
                    etstate.text.toString().trim().equals("")
                ) {

                    Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()

                } else if (selectedLocation.toString().trim().equals("Select Address Type")) {
                    Toast.makeText(this, "Please select Address Type", Toast.LENGTH_SHORT).show()
                } else if (etname.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show()
                } else if (edtmobileno.text.toString().trim()
                        .equals("") && !edtmobileno.text.toString().equals("0000000000")
                ) {
                    Toast.makeText(this, "Enter mobile number", Toast.LENGTH_LONG).show()
                } else if (edtmobileno.text.toString().trim().length < 10) {
                    Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG).show()
                } else if (edtmobileno.text.toString().trim().equals("0000000000")) {
                    Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG).show()
                } else if (etemps.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter email address", Toast.LENGTH_LONG).show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(etemps.text.toString()).matches()) {
                    Toast.makeText(this, "Enter correct email address", Toast.LENGTH_LONG).show()
                } else if (selectedLocation.toString().trim().equals("Select Type")) {
                    Toast.makeText(this, "Please select service type", Toast.LENGTH_SHORT).show()
                } else if (etflatno.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter flat number", Toast.LENGTH_LONG).show()
                } else if (etbuildname.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter Building name", Toast.LENGTH_LONG).show()
                } else if (etstreet.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter street name", Toast.LENGTH_LONG).show()
                } else if (etlocality.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter Locality", Toast.LENGTH_LONG).show()
                } else if (etlandmark.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter Landmark", Toast.LENGTH_LONG).show()
                } else if (etcity.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter city", Toast.LENGTH_LONG).show()
                } else if (etstate.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter state", Toast.LENGTH_LONG).show()
                } else if (etpincode.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter pincode", Toast.LENGTH_LONG).show()
                } else if (etpincode.text.toString().trim().length < 6) {
                    Toast.makeText(this, "Incorrect pincode", Toast.LENGTH_LONG).show()
                } else {
                    progressDialog.show()
                    var data = HashMap<String, Any>()
                    data["Id"] = 0
                    data["OrderId"] = 0
                    data["Customer_Id"] = AppUtils2.customerid.toInt()
                    data["Contact_Person_Name"] = etname.text.toString()
                    data["Contact_Person_Mobile"] = edtmobileno.text.toString()
                    data["Contact_Person_Email"] = etemps.text.toString()
                    data["Flat_No"] = etflatno.text.toString()
                    data["Building_Name"] = etbuildname.text.toString()
                    data["Street"] = etstreet.text.toString()
                    data["Locality"] = etlocality.text.toString()
                    data["Landmark"] = etlandmark.text.toString()
                    data["City"] = etcity.text.toString()
                    data["State"] = etstate.text.toString()
                    data["Pincode"] = etpincode.text.toString()
                    data["Address_Lat"] = ""
                    data["Address_Long"] = ""
                    data["AddressType"] = selectedLocation
                    data["GST_No"] = ""
                    data["IsDefault"] = false

                    viewProductModel.getsaveaddressresponse.observe(this, Observer {
                        progressDialog.dismiss()
                        if (it.IsSuccess == true) {
                            var newAddressid = it.Data.toString()
                            shippingdata = newAddressid
                            SharedPreferenceUtil.setData(this, "Shippingdata", newAddressid)
                            getAddressListdata2()
                            alertDialog.dismiss()
                            binding.checkbox.isChecked == false
                            appCompatCheckBox.isChecked = false

                            Toast.makeText(
                                this,
                                "Shipping address added successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(this, "Something went to wrong.", Toast.LENGTH_LONG)
                                .show()
                        }
                    })

                    viewProductModel.postSaveAddress(data)


                }
            }
        } else if (b.equals("false")) {

            var courses = arrayOf<String?>(
                "Home", "Office",
                "Others"
            )

            val etname = promptsView.findViewById<View>(R.id.etname) as EditText
            val edtmobileno = promptsView.findViewById<View>(R.id.edtmobileno) as EditText
            val etemps = promptsView.findViewById<View>(R.id.etemps) as TextView
            val etflatno = promptsView.findViewById<View>(R.id.etflatno) as EditText
            val etbuildname = promptsView.findViewById<View>(R.id.etbuildname) as EditText
            val etstreet = promptsView.findViewById<View>(R.id.etstreet) as EditText
            val etlocality = promptsView.findViewById<View>(R.id.etlocality) as TextView
            val etlandmark = promptsView.findViewById<View>(R.id.etlandmark) as EditText
            val etpincode = promptsView.findViewById<View>(R.id.etpincodes) as EditText
            val txttitle = promptsView.findViewById<View>(R.id.texttitle) as TextView
            val lnraddresstypes =
                promptsView.findViewById<View>(R.id.lnraddresstypes) as LinearLayout
            val etstate = promptsView.findViewById<View>(R.id.etstate) as EditText
            val etcity = promptsView.findViewById<View>(R.id.etcitites) as EditText

            val imgcancels = promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView
            val spinner = promptsView.findViewById<View>(R.id.spinner) as AppCompatSpinner
            val saveBtn = promptsView.findViewById<View>(R.id.saveBtn) as Button

            txttitle.text = "Billing Address"

            alertDialog.setCancelable(false)


            lnraddresstypes.visibility = View.GONE

            imgcancels.setOnClickListener {
                alertDialog.cancel()
                checkboxcheck == false
                appCompatCheckBox.isChecked = false
            }
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

            saveBtn.setOnClickListener {
                if (etname.text.toString().trim().equals("") && edtmobileno.text.toString().trim()
                        .equals("") &&
                    etemps.text.toString().trim().equals("") && selectedLocation.toString().trim()
                        .equals("Select Type") &&
                    etflatno.text.toString().trim().equals("") && etbuildname.text.toString().trim()
                        .equals("") &&
                    etstreet.text.toString().trim().equals("") && etlocality.text.toString().trim()
                        .equals("") &&
                    etlandmark.text.toString().trim().equals("") && etpincode.text.toString().trim()
                        .equals("") &&
                    etcity.text.toString().trim().equals("") && etstate.text.toString().trim()
                        .equals("")
                ) {
                    Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_LONG).show()
                } else if (etname.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show()
                } else if (edtmobileno.text.toString().trim()
                        .equals("") && !edtmobileno.text.toString().equals("0000000000")
                ) {
                    Toast.makeText(this, "Enter mobile number", Toast.LENGTH_LONG).show()
                } else if (edtmobileno.text.toString().trim().length < 10) {
                    Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG).show()
                } else if (edtmobileno.text.toString().trim().equals("0000000000")) {
                    Toast.makeText(this, "Enter correct mobile number", Toast.LENGTH_LONG).show()
                } else if (etemps.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter email address", Toast.LENGTH_LONG).show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(etemps.text.toString()).matches()) {
                    Toast.makeText(this, "Enter correct email address", Toast.LENGTH_LONG).show()
                } else if (selectedLocation.toString().trim().equals("Select Type")) {
                    Toast.makeText(this, "Please select service type", Toast.LENGTH_SHORT).show()
                } else if (etflatno.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter flat number", Toast.LENGTH_LONG).show()
                } else if (etbuildname.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter Building name", Toast.LENGTH_LONG).show()
                } else if (etstreet.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter street name", Toast.LENGTH_LONG).show()
                } else if (etlocality.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter Locality", Toast.LENGTH_LONG).show()
                } else if (etlandmark.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter landmark", Toast.LENGTH_LONG).show()
                } else if (etcity.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter city", Toast.LENGTH_LONG).show()
                } else if (etstate.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter state", Toast.LENGTH_LONG).show()
                } else if (etpincode.text.toString().trim().equals("")) {
                    Toast.makeText(this, "Enter pincode", Toast.LENGTH_LONG).show()
                } else if (etpincode.text.toString().trim().length < 6) {
                    Toast.makeText(this, "Incorrect pincode", Toast.LENGTH_LONG).show()
                } else {
                    progressDialog.show()
                    var data = HashMap<String, Any>()
                    data["Id"] = 0
                    data["OrderId"] = 0
                    data["Customer_Id"] = AppUtils2.customerid.toInt()
                    data["Contact_Person_Name"] = etname.text.toString()
                    data["Contact_Person_Mobile"] = edtmobileno.text.toString()
                    data["Contact_Person_Email"] = etemps.text.toString()
                    data["Flat_No"] = etflatno.text.toString()
                    data["Building_Name"] = etbuildname.text.toString()
                    data["Street"] = etstreet.text.toString()
                    data["Locality"] = etlocality.text.toString()
                    data["Landmark"] = etlandmark.text.toString()
                    data["City"] = etcity.text.toString()
                    data["State"] = etstate.text.toString()
                    data["Pincode"] = etpincode.text.toString()
                    data["Address_Lat"] = ""
                    data["Address_Long"] = ""
                    data["AddressType"] = selectedLocation
                    data["GST_No"] = ""
                    data["IsDefault"] = false

                    viewProductModel.getsaveaddressresponse.observe(this, Observer {
                        progressDialog.dismiss()
                        if (it.IsSuccess == true) {
                            var newaddessid = it.Data.toString()
                            billingdata = it.Data.toString()
                            SharedPreferenceUtil.setData(this, "Billingdata", newaddessid)
//                            getAddressforshipping()
                            getAddressforbilling()

                            Toast.makeText(
                                this,
                                "Billing address added successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.checkbox.isChecked == false
                            appCompatCheckBox.isChecked = false
                            alertDialog.dismiss()

                        } else {
                        }
                    })

                    viewProductModel.postSaveAddress(data)

                }
            }
        }
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SharedPreferenceUtil.setData(this, "Billingdata", "")
        SharedPreferenceUtil.setData(this, "Shippingdata", "")
        val intent = Intent(this@AddressActivity, AddToCartActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }
}
