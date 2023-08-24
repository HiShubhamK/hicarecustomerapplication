package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.FragmentActivity
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.databinding.ActivityAddComplaintsBinding
import com.ab.hicareservices.databinding.ActivitymycomplaintBinding
import com.ab.hicareservices.databinding.FragmentOrdersNewBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.adapter.ProductViewPagerAdapter
import com.ab.hicareservices.ui.view.fragments.ProductComplaintsFragment
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import org.json.JSONObject
import kotlin.collections.ArrayList

class ComplaintsActivityNew : AppCompatActivity() {

    private val TAG = "ComplaintsActivityNew"
    lateinit var binding: ActivitymycomplaintBinding
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
    var getsummarydata = ArrayList<OrderSummeryData>()
    lateinit var Spinnerlistproduct: ArrayList<String>
    lateinit var SpinnerlistSelect: ArrayList<String>
    private val viewProductModel: ProductViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitymycomplaintBinding.inflate(layoutInflater)
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
        Spinnerlistproduct = ArrayList()
        getsummarydata = ArrayList()
        SpinnerlistSelect = ArrayList()
        SpinnerlistSelect.add("Select Complaint")
        SpinnerlistSelect.add("Services")
        SpinnerlistSelect.add("Products")
        mobile = SharedPreferenceUtil.getData(this@ComplaintsActivityNew, "mobileNo", "-1").toString()


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

        viewProductModel.responseMessage.observe(
            this@ComplaintsActivityNew,
            androidx.lifecycle.Observer {
            })

        viewProductModel.getordersummeryList.observe(
            this@ComplaintsActivityNew,
            androidx.lifecycle.Observer {
                AppUtils2.Spinnerlistproduct= ArrayList()
                AppUtils2.getsummarydata=ArrayList()
                AppUtils2.getsummarydata.addAll(it)
                AppUtils2.Spinnerlistproduct.add("Select product")
                getsummarydata.addAll(it)
                for (i in 0 until it.size) {
                    Spinnerlistproduct.add(it.get(i).ProductDisplayName.toString())
                    AppUtils2.Spinnerlistproduct.add(it.get(i).ProductDisplayName.toString())
                }

            })

        viewProductModel.getOrderSummeryList(AppUtils2.customerid.toInt())

    }

    private fun apicall() {

        viewModel.ordersList.observe(this@ComplaintsActivityNew, androidx.lifecycle.Observer {
            if (it != null) {
                AppUtils2.datalist=ArrayList()
                AppUtils2.datalist.addAll(it)
                datalist.addAll(it)
                AppUtils2.Spinnerlist= ArrayList()
                AppUtils2.Spinnerlist.add("Select Service")
                progressDialog.dismiss()
                for (i in 0 until it.size) {
//                    datalist.addAll(it)
                    Spinnerlist.add(it.get(i).ServicePlanName_c.toString())
                    AppUtils2.Spinnerlist.add(it.get(i).ServicePlanName_c.toString())


                }


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
        var selectproduct = ""
        var selectcomplaints = ""
        var dateTime = ""
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.layout_lead_spinner, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        val spinner = promptsView.findViewById<View>(R.id.spinner_lead) as AppCompatSpinner
        val spinner2 = promptsView.findViewById<View>(R.id.spinner_lead2) as AppCompatSpinner
        val spinner3 = promptsView.findViewById<View>(R.id.spinner_lead3) as AppCompatSpinner
        val btnSubmit = promptsView.findViewById<View>(R.id.btnlead) as Button
        val imgcancels = promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView
        val servicetypes = promptsView.findViewById<View>(R.id.servicetypes) as LinearLayout
        val producttypes = promptsView.findViewById<View>(R.id.producttypes) as LinearLayout


        alertDialog.setCancelable(false)

        imgcancels.setOnClickListener { alertDialog.cancel() }


        val arrayAdapter3 = object : ArrayAdapter<String>(
            this@ComplaintsActivityNew,
            R.layout.spinner_layout_new,
            SpinnerlistSelect
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
        arrayAdapter3.setDropDownViewResource(R.layout.spinner_popup)
        spinner3.adapter = arrayAdapter3

        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectcomplaints = spinner.selectedItem.toString()
                if (selectcomplaints != "Select Complaint") {

                } else if (selectcomplaints.equals("Services")) {
                    servicetypes.visibility = View.VISIBLE

                    producttypes.visibility = View.GONE
                    servicetypes.visibility = View.VISIBLE
                } else if (selectcomplaints.equals("Products")) {
                    servicetypes.visibility = View.GONE
                    producttypes.visibility = View.VISIBLE

                } else {

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


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
                    selectproduct=""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        val arrayAdapter2 = object : ArrayAdapter<String>(
            this@ComplaintsActivityNew,
            R.layout.spinner_layout_new,
            Spinnerlistproduct
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
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_popup)
        spinner2.adapter = arrayAdapter2

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectproduct = spinner.selectedItem.toString()
                if (selectproduct != "Select Type") {

                } else {
                    selectedLocation=""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        btnSubmit.setOnClickListener {

            var orderNo = ""
            var serviceType = ""
            var service_url_image = ""

            if(selectedLocation!="") {

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
            }else{

            }

        }
        alertDialog.show()
    }


}