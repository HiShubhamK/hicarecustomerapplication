package com.ab.hicareservices.ui.view.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.databinding.ActivityBokingServiceDetailsBinding
import com.ab.hicareservices.databinding.ActivityBookInspectionBinding
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2

class BokingServiceDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBokingServiceDetailsBinding
    var serviceName: String? = null
    var serviceCode: String? = null
    var serviceThumbnail: String? = null
    var shortDescription: String? = null
    var stailDescription: String? = null
    lateinit var spinnerlist: ArrayList<String>
    var selectedLocation:String?=null
    private val viewProductModel: ServiceBooking by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_boking_service_details)

        binding=ActivityBokingServiceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        serviceName = intent.getStringExtra("ServiceName").toString()
        serviceCode = intent.getStringExtra("ServiceCode").toString()
        serviceThumbnail = intent.getStringExtra("ServiceThumbnail").toString()
        shortDescription = intent.getStringExtra("ShortDescription").toString()
        stailDescription = intent.getStringExtra("DetailDescription").toString()



        binding.txtviewdetails.setOnClickListener{


            val bottomSheetFragment = CustomBottomSheetFragment()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)





            }






















        binding.txtshortdes.text=shortDescription.toString()
        binding.txtlongdes.text=stailDescription.toString()

        spinnerlist= ArrayList()
        spinnerlist.add("Select flat area")

        binding.imgLogo.setOnClickListener{
            onBackPressed()
            finish()
        }

        viewProductModel.activebhklist.observe(this@BokingServiceDetailsActivity, Observer {
            if (it.isNotEmpty()) {
                for (i in 0 until it.size) {
                    spinnerlist.add(it.get(i).NoOfBHK.toString())
                }
                Toast.makeText(this,spinnerlist.size.toString(),Toast.LENGTH_SHORT).show()
            } else {

            }
        })

        viewProductModel.getActiveBHKList()

        val arrayAdapter =
            object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, spinnerlist) {
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
                if (selectedLocation != "Select flat area") {

                } else {
                    viewProductModel.activebhklist.observe(this@BokingServiceDetailsActivity, Observer {
                        if (it.isNotEmpty()) {
                            for (i in 0 until it.size) {
                                spinnerlist.add(it.get(i).NoOfBHK.toString())
                            }
                            Toast.makeText(this@BokingServiceDetailsActivity,spinnerlist.size.toString(),Toast.LENGTH_SHORT).show()
                        } else {

                        }
                    })

                    viewProductModel.getPlanAndPriceByBHKandPincode("400601",selectedLocation.toString(),serviceName.toString())

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }



    }
}