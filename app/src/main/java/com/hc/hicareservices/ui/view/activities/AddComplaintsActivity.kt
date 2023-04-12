package com.hc.hicareservices.ui.view.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hc.hicareservices.R
import com.hc.hicareservices.data.repository.MainRepository
import com.hc.hicareservices.databinding.ActivityAddComplaintsBinding
import com.hc.hicareservices.ui.viewmodel.CComplaintViewModel

class AddComplaintsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddComplaintsBinding
    var selectedCType = ""
    var selectedCSubType = ""
    var serviceType = ""
    var getServiceType = ""
    private val complaintViewModel: CComplaintViewModel by viewModels()
    lateinit var typeHash: HashMap<String, List<String>?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddComplaintsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val orderNo = intent.getStringExtra("orderNo").toString()
        getServiceType = intent.getStringExtra("serviceType").toString()

        binding.orderNoEt.setText(orderNo)

        //complaintViewModel = ViewModelProvider(this, CComplaintViewModelFactory(MainRepository(api))).get(CComplaintViewModel::class.java)

        typeHash = HashMap()
        binding.backIv.setOnClickListener {
            finish()
        }


        //serviceType = "pest"
        getFromServiceType()
        //getComplaintReason(serviceType)

        binding.complaintSpnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedType = binding.complaintSpnType.selectedItem.toString()
                if (selectedType != "Complaint Type"){
                    selectedCType = selectedType
                    if (!serviceType.equals("pest", true)) {
                        getSubTypeFromType()
                    }
                }
                //Log.d("TAG", selectedType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.subSpnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedType = binding.subSpnType.selectedItem.toString()
                if (selectedType != "None" || selectedType.equals("Complaint Sub Type", true)){
                    selectedCSubType = selectedType
                }
                //Log.d("TAG", selectedType)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.saveBtn.setOnClickListener {
            val orderNo = binding.orderNoEt.text.toString().trim()
            val serviceNo = binding.serviceNoEt.text.toString().trim()
            val complaintTitle = binding.complaintTitleEt.text.toString().trim()
            val complaintDescr = binding.complaintDescrEt.text.toString().trim()
            if (serviceType.equals("pest", true)){
                if (orderNo != "" && complaintTitle != "" && complaintDescr != "" && selectedCType != ""){
                    addComplaint(orderNo, serviceNo, selectedCType,
                        selectedCSubType, complaintTitle, complaintDescr, serviceType)
                }else{
                    Toast.makeText(this, "Please fill data properly.", Toast.LENGTH_SHORT).show()
                }
            }else{
                if (orderNo != "" && complaintTitle != "" && complaintDescr != ""
                    && selectedCType != "" && selectedCSubType != ""){
                    addComplaint(orderNo, serviceNo, selectedCType,
                        selectedCSubType, complaintTitle, complaintDescr, serviceType)
                }else{
                    Toast.makeText(this, "Please fill data properly", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getSubTypeFromType(){
        val subtype = ArrayList<String>()
        subtype.clear()
        subtype.add("Complaint Sub Type")
        typeHash[selectedCType]?.forEach {
            subtype.add(it)
        }
        val subtypeAdapter = object :
            ArrayAdapter<String>(this, R.layout.spinner_layout_new, subtype) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
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
        subtypeAdapter.setDropDownViewResource(R.layout.spinner_popup)
        binding.subSpnType.adapter = subtypeAdapter


    }

    private fun getFromServiceType(){
        val serviceTypeArray = arrayListOf("Select Service Type", "Pest", "HC")

        val serviceTypeAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, serviceTypeArray){
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0){
                    tv.setTextColor(Color.GRAY)
                }else{
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }

        serviceTypeAdapter.setDropDownViewResource(R.layout.spinner_popup)
        binding.serviceTypeSpn.adapter = serviceTypeAdapter

        binding.serviceTypeSpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedType = binding.serviceTypeSpn.selectedItem.toString()
                if (selectedType != "Select Service Type"){
                    //selectedCType = selectedType
                    serviceType = selectedType
                    Log.d("TAG", "Called Select Service Type")
                    binding.progressBar.visibility = View.VISIBLE
                    getComplaintReason(selectedType)
                    binding.complaintSpnType.isEnabled = true
                }
                //Log.d("TAG", selectedType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        serviceTypeArray.forEach {
            if (it == getServiceType){
                binding.serviceTypeSpn.setSelection(serviceTypeArray.indexOf(it))
                serviceType = it
                return@forEach
            }
        }
        binding.serviceTypeSpn.isEnabled = false
    }

    private fun getComplaintReason(serviceType: String){
        val type = ArrayList<String>()
        val subtype = ArrayList<String>()
        complaintViewModel.complaintReasons.observe(this) {
            if (it?.isSuccess == true) {
                type.clear()
                subtype.clear()
                binding.progressBar.visibility = View.GONE
                type.add("Complaint Type")
                if (serviceType.equals("pest", true)) {
                    subtype.add("None")
                } else {
                    subtype.add("Complaint Sub Type")
                }
                it.data?.forEach { complaintType ->
                    type.add(complaintType.name.toString())
                    if (!serviceType.equals("pest", true)) {
                        typeHash[complaintType.name.toString()] = complaintType.subType
                    }
                }
                Log.d("TAG", typeHash.toString())
                initArrayAdapter(serviceType, type, subtype)
            } else {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        complaintViewModel.getComplaintReasons(serviceType)
    }

    private fun addComplaint(orderNo: String, serviceNo: String,
                             complaintType: String, complaintSubType: String,
                             complaintTitle: String, complaintDescr: String, serviceType: String){
        val hashMap = HashMap<String, String>()
        hashMap["OrderNo"] = orderNo
        hashMap["ServiceNo"] = serviceNo
        hashMap["ComplaintType"] = complaintType
        hashMap["ComplaintSubType"] = complaintSubType
        hashMap["ComplaintTitle"] = complaintTitle
        hashMap["ComplaintDescription"] = complaintDescr
        hashMap["ServiceType"] = serviceType
        hashMap["Source"] = "mobileApp"
        hashMap["SubSource"] = "mobileApp"

        complaintViewModel.createComplaint(hashMap)
        complaintViewModel.createComplaintResponse.observe(this, {
            if (it.isSuccess == true){
                Toast.makeText(this, "Complaint Created", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initArrayAdapter(serviceType: String, type: List<String>, subType: List<String>){

        val typeAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, type){
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0){
                    tv.setTextColor(Color.GRAY)
                }else{
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }

        val subtypeAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, subType){
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0){
                    tv.setTextColor(Color.GRAY)
                }else{
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }

        typeAdapter.setDropDownViewResource(R.layout.spinner_popup)
        subtypeAdapter.setDropDownViewResource(R.layout.spinner_popup)
        binding.complaintSpnType.adapter = typeAdapter
        binding.subSpnType.adapter = subtypeAdapter
        binding.subSpnType.isEnabled = !serviceType.equals("pest", true)
    }
}