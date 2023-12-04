package com.ab.hicareservices.ui.view.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityPestServicesBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.BookingServiceListAdapter
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.SharedPreferencesManager
import com.ab.hicareservices.utils.UserData

class PestServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPestServicesBinding
    private lateinit var mAdapter: BookingServiceListAdapter
    private val viewProductModel: ServiceBooking by viewModels()

    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_pest_services)
        binding = ActivityPestServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyLocationListener(this)


        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)


        AppUtils2.pincode =
            SharedPreferenceUtil.getData(this@PestServicesActivity, "pincode", "").toString()

        if (AppUtils2.pincode.equals("")) {
            binding.getpincodetext.setText("400080")
        } else {
            binding.getpincodetext.setText(AppUtils2.pincode)
        }

        binding.imgLogo.setOnClickListener {
            onBackPressed()

        }


//        binding.recMenu.layoutManager = GridLayoutManager(this@PestServicesActivity, 3)
        binding.recMenu.layoutManager =
            LinearLayoutManager(this@PestServicesActivity, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServiceListAdapter()
        binding.recMenu.adapter = mAdapter

        progressDialog.show()

        viewProductModel.serviceresponssedata.observe(this@PestServicesActivity, Observer {
            if (it.isNotEmpty()) {
                progressDialog.dismiss()

                mAdapter.setServiceList(it, this)

// Set other properties...


            } else {

            }
        })
        viewProductModel.getActiveServiceList()

        binding.imgsearch.setOnClickListener {

            SharedPreferenceUtil.setData(this, "pincode", binding.getpincodetext.text.toString())
            AppUtils2.pincode = binding.getpincodetext.text.toString()

            progressDialog.show()

            Handler(Looper.getMainLooper()).postDelayed({

                viewProductModel.serviceresponssedata.observe(this@PestServicesActivity, Observer {
                    if (it.isNotEmpty()) {
                        progressDialog.dismiss()
                        mAdapter.setServiceList(it, this)
                    } else {

                    }
                })
                viewProductModel.getActiveServiceList()

            }, 300)

            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            // Check if there's a focused view before hiding the keyboard
            if (this is Activity && this.currentFocus != null) {
                imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
            }


        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}