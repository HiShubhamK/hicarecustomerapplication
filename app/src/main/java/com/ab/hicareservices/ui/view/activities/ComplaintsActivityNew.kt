package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
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
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.ab.hicareservices.BuildConfig
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.databinding.ActivityAddComplaintsBinding
import com.ab.hicareservices.databinding.FragmentOrdersNewBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.adapter.ProductViewPagerAdapter
import com.ab.hicareservices.ui.view.fragments.ProductComplaintsFragment
import com.ab.hicareservices.ui.viewmodel.CComplaintViewModel
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.ui.viewmodel.UploadAttachmentViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class ComplaintsActivityNew : AppCompatActivity() {

    private val TAG = "ComplaintsActivityNew"
    lateinit var binding: FragmentOrdersNewBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    private var ordertype = ""
    lateinit var homeActivity: HomeActivity
    lateinit var orderactivityforadapter: FragmentActivity
    lateinit var options: JSONObject
    lateinit var progressDialog: ProgressDialog
    lateinit var datalist: MutableList<OrdersData>
    lateinit var Spinnerlist: ArrayList<String>
     var getsummarydata= ArrayList<OrderSummeryData>()


    private val viewProductModel: ProductViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOrdersNewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        binding.title.text = "My Complaints"
        binding.imgLogo.visibility = View.VISIBLE
        binding.imgLogo.setOnClickListener {
            finish()
        }
        datalist = ArrayList()
        Spinnerlist = ArrayList()

        mobile =
            SharedPreferenceUtil.getData(this@ComplaintsActivityNew, "mobileNo", "-1").toString()


        apicall()

        apicallforproduct()

        setupViewPager()

//        for (i in 0 until datalist.size) {
//            Spinnerlist.add(datalist.get(i).ServicePlanName_c.toString())
//        }


        binding.help.setOnClickListener {
            showLeadDialog()
        }

    }

    private fun apicallforproduct() {

        viewProductModel.responseMessage.observe(this@ComplaintsActivityNew, androidx.lifecycle.Observer {
            Toast.makeText(this@ComplaintsActivityNew,it.toString(),Toast.LENGTH_LONG).show()
        })

        viewProductModel.getordersummeryList.observe(this@ComplaintsActivityNew, androidx.lifecycle.Observer{

            getsummarydata.addAll(it)

        })

        viewProductModel.getOrderSummeryList(AppUtils2.customerid.toInt())

    }

    private fun apicall() {

        viewModel.ordersList.observe(this@ComplaintsActivityNew, androidx.lifecycle.Observer {
            if (it != null) {
                datalist.addAll(it)
                progressDialog.dismiss()
                for (i in 0 until it.size) {
//                    datalist.addAll(it)
                    Spinnerlist.add(it.get(i).ServicePlanName_c.toString())
                    Toast.makeText(
                        this@ComplaintsActivityNew,
                        Spinnerlist.size.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                }

                Toast.makeText(
                    this@ComplaintsActivityNew,
                    "Datalist" + datalist.size.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        viewModel.responseMessage.observe(
            this@ComplaintsActivityNew,
            androidx.lifecycle.Observer {
                progressDialog.dismiss()
            })

        viewModel.errorMessage.observe(this@ComplaintsActivityNew, androidx.lifecycle.Observer {
            if (it != null) {
            }
        })

        viewModel.getCustomerOrdersByMobileNo(mobile, progressDialog)

    }

    private fun setupViewPager() {

        val orderfragment = ProductComplaintsFragment()
        val extraList = ProductComplaintsFragment()
        val adapter = ProductViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(orderfragment, "Services")
        adapter.addFragment(extraList, "Products")

        val viewPager = binding.vpFragments
        viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.vpFragments)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
//        getFromServiceType()
    }

    override fun onBackPressed() {
        finish()

    }


    private fun showLeadDialog() {
        var selectedLocation = ""
        var dateTime = ""
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.layout_lead_spinner, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        val spinner = promptsView.findViewById<View>(R.id.spinner_lead) as AppCompatSpinner
        val btnSubmit = promptsView.findViewById<View>(R.id.btnlead) as Button
        val imgcancels = promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView

        alertDialog.setCancelable(false)

        imgcancels.setOnClickListener { alertDialog.cancel() }

        val arrayAdapter = object : ArrayAdapter<String>(
            this@ComplaintsActivityNew,
            R.layout.spinner_layout_new,
            Spinnerlist
        ) {
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

            var orderNo = ""
            var serviceType = ""
            var service_url_image = ""

            for (i in 0 until datalist.size) {
                if (datalist.get(i).ServicePlanName_c.equals(selectedLocation)) {
                    orderNo = datalist.get(i).OrderNumber_c.toString()
                    serviceType = datalist.get(i).ServiceType.toString()
                    service_url_image = datalist.get(i).ServicePlanImageUrl.toString()
                    break
                }
            }

            val intent = Intent(this, AddComplaintsActivity::class.java)
            intent.putExtra("orderNo", orderNo)
            intent.putExtra("serviceType", serviceType)
            intent.putExtra("service_url_image", service_url_image)
            startActivity(intent)

            alertDialog.dismiss()

        }
        alertDialog.show()
    }


}