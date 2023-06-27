package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityAddressBinding
import com.ab.hicareservices.ui.adapter.AddressAdapter
import com.ab.hicareservices.ui.handler.onAddressClickedHandler
import com.ab.hicareservices.ui.viewmodel.HomeActivityViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import java.util.*

class AddressActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddressBinding
    private val viewProductModel: ProductViewModel  by viewModels()
    private lateinit var mAdapter: AddressAdapter
    lateinit var datalist: ArrayList<String>
    private val viewModels: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        binding= ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datalist = ArrayList()
        datalist.add("Select Type")
//
//        Handler(Looper.getMainLooper()).postDelayed({
//            getLeadMethod()
//        }, 1500)

        getAddressList()

        binding.lnraddress.setOnClickListener {
            showAddNewAddressdialog(true, 0)
        }

    }

    private fun getAddressList() {
        binding.recycleviewaddress.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = AddressAdapter()

        binding.recycleviewaddress.adapter = mAdapter

        viewProductModel.cutomeraddress.observe(this, Observer {

            mAdapter.setAddressList(it, this,viewProductModel)
            mAdapter.notifyDataSetChanged()

        })

        viewProductModel.getCustomerAddress(20)

        mAdapter.setOnAddressItemClicked(object : onAddressClickedHandler{
            override fun setonaddclicklistener(position: Int, id: Int?) {
                showAddNewAddressdialog(false,id)
            }

            override fun setonaddclicklisteners(position: Int) {
                mAdapter.notifyDataSetChanged()
            }
        })

    }

    private fun getLeadMethod() {
        try {
            viewModels.spinnerList.observe(this, Observer {
                datalist.addAll(it)
            })

            viewModels.getleaderspinner("pest")
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun showAddNewAddressdialog(b: Boolean, id: Int?) {
        var selectedLocation = ""
        var dateTime = ""
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.layout_addnew_addreess, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()



        if(b==true){

            var courses = arrayOf<String?>("Home", "Office",
                "Others")

            val etname = promptsView.findViewById<View>(R.id.etname) as EditText
            val edtmobileno = promptsView.findViewById<View>(R.id.edtmobileno) as EditText
            val etemps = promptsView.findViewById<View>(R.id.etemps) as TextView
            val etflatno = promptsView.findViewById<View>(R.id.etflatno) as EditText
            val etbuildname = promptsView.findViewById<View>(R.id.etbuildname) as EditText
            val etstreet = promptsView.findViewById<View>(R.id.etstreet) as EditText
            val etlocality = promptsView.findViewById<View>(R.id.etlocality) as TextView
            val etlandmark = promptsView.findViewById<View>(R.id.etlandmark) as EditText
            val etpincode = promptsView.findViewById<View>(R.id.etpincodes) as EditText


            val imgcancels = promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView
            val spinner = promptsView.findViewById<View>(R.id.spinner) as AppCompatSpinner
            val saveBtn = promptsView.findViewById<View>(R.id.saveBtn) as Button


            alertDialog.setCancelable(false)

            imgcancels.setOnClickListener { alertDialog.cancel() }
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
                if(etname.text.toString().trim().equals("") && edtmobileno.text.toString().trim().equals("") &&
                    etemps.text.toString().trim().equals("")  && selectedLocation.toString().trim().equals("Select Type") &&
                    etflatno.text.toString().trim().equals("") && etbuildname.text.toString().trim().equals("") &&
                    etstreet.text.toString().trim().equals("") && etlocality.text.toString().trim().equals("") &&
                    etlandmark.text.toString().trim().equals("") && etpincode.text.toString().trim().equals("")) {

                    Toast.makeText(this,"All fields are mandatory",Toast.LENGTH_LONG).show()

                }
                else if(etname.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter name",Toast.LENGTH_LONG).show()
                }else if(edtmobileno.text.toString().trim().equals("") && !edtmobileno.text.toString().equals("0000000000")){
                    Toast.makeText(this,"Enter mobile number",Toast.LENGTH_LONG).show()
                }else if(edtmobileno.text.toString().trim().length<10){
                    Toast.makeText(this,"Enter correct mobile number",Toast.LENGTH_LONG).show()
                }else if(edtmobileno.text.toString().trim().equals("0000000000")){
                    Toast.makeText(this,"Enter correct mobile number",Toast.LENGTH_LONG).show()
                } else if(etemps.text.toString().trim().equals("") ){
                    Toast.makeText(this,"Enter email adress",Toast.LENGTH_LONG).show()
                }else if(!Patterns.EMAIL_ADDRESS.matcher(etemps.text.toString()).matches()){
                    Toast.makeText(this,"Enter correct email adress",Toast.LENGTH_LONG).show()
                } else if(selectedLocation.toString().trim().equals("Select Type")){
                    Toast.makeText(this,"Please select service type", Toast.LENGTH_SHORT).show()
                }else if(etflatno.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter flat number",Toast.LENGTH_LONG).show()
                }else if(etbuildname.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter Building name",Toast.LENGTH_LONG).show()
                }else if(etstreet.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter street name",Toast.LENGTH_LONG).show()
                }else if(etlocality.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter locatity",Toast.LENGTH_LONG).show()
                }else if(etlandmark.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter landkmark",Toast.LENGTH_LONG).show()
                }else if(etpincode.text.toString().trim().length<6){
                    Toast.makeText(this,"Enter  pincode",Toast.LENGTH_LONG).show()
                } else if(etpincode.text.toString().trim().length<6){
                    Toast.makeText(this,"Enter correct pincode",Toast.LENGTH_LONG).show()
                }else{
                    var data = HashMap<String, Any>()
                    data["Id"] =0
                    data["OrderId"] =0
                    data["Customer_Id"] =20
                    data["Contact_Person_Name"] = etname.text.toString()
                    data["Contact_Person_Mobile"] =edtmobileno.text.toString()
                    data["Contact_Person_Email"] =etemps.text.toString()
                    data["Flat_No"] =etflatno.text.toString()
                    data["Building_Name"] =etbuildname.text.toString()
                    data["Street"] =etstreet.text.toString()
                    data["Locality"] =etlocality.text.toString()
                    data["Landmark"] =etlandmark.text.toString()
                    data["City"] =""
                    data["State"] =""
                    data["Pincode"] =etpincode.text.toString()
                    data["Address_Lat"] =""
                    data["Address_Long"] =""
                    data["AddressType"] = selectedLocation
                    data["GST_No"] =""
                    data["IsDefault"] =false

                    viewProductModel.getsaveaddressresponse.observe(this, Observer {
                        if(it.IsSuccess==true){
                            val intent=intent
                            finish()
                            startActivity(intent)
                            alertDialog.dismiss()
                            Toast.makeText(this,"Shipping address added successfully",Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this,"Something went to wrong.",Toast.LENGTH_LONG).show()
                        }
                    })

                    viewProductModel.postSaveAddress(data)


                }
            }
        }else{

            var courses = arrayOf<String?>("Home", "Office",
                "Others")

            val etname = promptsView.findViewById<View>(R.id.etname) as EditText
            val edtmobileno = promptsView.findViewById<View>(R.id.edtmobileno) as EditText
            val etemps = promptsView.findViewById<View>(R.id.etemps) as TextView
            val etflatno = promptsView.findViewById<View>(R.id.etflatno) as EditText
            val etbuildname = promptsView.findViewById<View>(R.id.etbuildname) as EditText
            val etstreet = promptsView.findViewById<View>(R.id.etstreet) as EditText
            val etlocality = promptsView.findViewById<View>(R.id.etlocality) as TextView
            val etlandmark = promptsView.findViewById<View>(R.id.etlandmark) as EditText
            val etpincode = promptsView.findViewById<View>(R.id.etpincodes) as EditText
            val txttitle=promptsView.findViewById<View>(R.id.texttitle) as TextView

            val imgcancels = promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView
            val spinner = promptsView.findViewById<View>(R.id.spinner) as AppCompatSpinner
            val saveBtn = promptsView.findViewById<View>(R.id.saveBtn) as Button

            txttitle.text="Billing Address"

            alertDialog.setCancelable(false)

            imgcancels.setOnClickListener { alertDialog.cancel() }
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
                if(etname.text.toString().trim().equals("") && edtmobileno.text.toString().trim().equals("") &&
                    etemps.text.toString().trim().equals("")  && selectedLocation.toString().trim().equals("Select Type") &&
                    etflatno.text.toString().trim().equals("") && etbuildname.text.toString().trim().equals("") &&
                    etstreet.text.toString().trim().equals("") && etlocality.text.toString().trim().equals("") &&
                    etlandmark.text.toString().trim().equals("") && etpincode.text.toString().trim().equals("")) {

                    Toast.makeText(this,"All fields are mandatory",Toast.LENGTH_LONG).show()

                }
                else if(etname.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter name",Toast.LENGTH_LONG).show()
                }else if(edtmobileno.text.toString().trim().equals("") && !edtmobileno.text.toString().equals("0000000000")){
                    Toast.makeText(this,"Enter mobile number",Toast.LENGTH_LONG).show()
                }else if(edtmobileno.text.toString().trim().length<10){
                    Toast.makeText(this,"Enter correct mobile number",Toast.LENGTH_LONG).show()
                }else if(edtmobileno.text.toString().trim().equals("0000000000")){
                    Toast.makeText(this,"Enter correct mobile number",Toast.LENGTH_LONG).show()
                } else if(etemps.text.toString().trim().equals("") ){
                    Toast.makeText(this,"Enter email adress",Toast.LENGTH_LONG).show()
                }else if(!Patterns.EMAIL_ADDRESS.matcher(etemps.text.toString()).matches()){
                    Toast.makeText(this,"Enter correct email adress",Toast.LENGTH_LONG).show()
                } else if(selectedLocation.toString().trim().equals("Select Type")){
                    Toast.makeText(this,"Please select service type", Toast.LENGTH_SHORT).show()
                }else if(etflatno.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter flat number",Toast.LENGTH_LONG).show()
                }else if(etbuildname.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter Building name",Toast.LENGTH_LONG).show()
                }else if(etstreet.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter street name",Toast.LENGTH_LONG).show()
                }else if(etlocality.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter locatity",Toast.LENGTH_LONG).show()
                }else if(etlandmark.text.toString().trim().equals("")){
                    Toast.makeText(this,"Enter landkmark",Toast.LENGTH_LONG).show()
                }else if(etpincode.text.toString().trim().length<6){
                    Toast.makeText(this,"Enter  pincode",Toast.LENGTH_LONG).show()
                } else if(etpincode.text.toString().trim().length<6){
                    Toast.makeText(this,"Enter correct pincode",Toast.LENGTH_LONG).show()
                }else{
                    var data = HashMap<String, Any>()
                    data["Id"] =0
                    data["OrderId"] =0
                    data["Customer_Id"] =20
                    data["Contact_Person_Name"] = etname.text.toString()
                    data["Contact_Person_Mobile"] =edtmobileno.text.toString()
                    data["Contact_Person_Email"] =etemps.text.toString()
                    data["Flat_No"] =etflatno.text.toString()
                    data["Building_Name"] =etbuildname.text.toString()
                    data["Street"] =etstreet.text.toString()
                    data["Locality"] =etlocality.text.toString()
                    data["Landmark"] =etlandmark.text.toString()
                    data["City"] =""
                    data["State"] =""
                    data["Pincode"] =etpincode.text.toString()
                    data["Address_Lat"] =""
                    data["Address_Long"] =""
                    data["AddressType"] = selectedLocation
                    data["GST_No"] =""
                    data["IsDefault"] =false

                    viewProductModel.getsaveaddressresponse.observe(this, Observer {
                        if(it.IsSuccess==true){

                            val billdata= SharedPreferenceUtil.setData(this@AddressActivity, "BillingAddress",it.Data.toString())

                            val intent= Intent(this@AddressActivity,OverviewProductDetailsActivity::class.java)
                            intent.putExtra("Billdata",billdata.toString())
                            intent.putExtra("Shipdata",id.toString())
                            startActivity(intent)
                            alertDialog.dismiss()
//                            Toast.makeText(this,"Shipping address added successfully",Toast.LENGTH_LONG).show()
                        }else{
//                            Toast.makeText(this,"Something went to wrong.",Toast.LENGTH_LONG).show()
                        }
                    })

                    viewProductModel.postSaveAddress(data)

                }
            }
        }
        alertDialog.show()

    }
}
