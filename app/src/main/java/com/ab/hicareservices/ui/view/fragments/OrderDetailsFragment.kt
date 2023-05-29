package com.ab.hicareservices.ui.view.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.weeks.WeekModel
import com.ab.hicareservices.ui.adapter.ServiceRequestAdapter
import com.ab.hicareservices.ui.adapter.SlotsAdapter
import com.ab.hicareservices.ui.adapter.WeeksAdapter
import com.ab.hicareservices.ui.handler.OnRescheduleClickHandler
import com.ab.hicareservices.ui.handler.OnServiceRequestClickHandler
import com.ab.hicareservices.ui.handler.PaymentListener
import com.ab.hicareservices.ui.view.activities.AddComplaintsActivity
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceViewModel
import com.ab.hicareservices.utils.AppUtils
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.databinding.FragmentOrderDetailsBinding
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.squareup.picasso.Picasso
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.json.JSONObject
import kotlin.math.roundToInt

class OrderDetailsFragment : Fragment() {

    private val TAG = "OrderDetailsFragment"
    lateinit var binding: FragmentOrderDetailsBinding
    private val viewModel: ServiceViewModel by viewModels()
    private val orderDetailsViewModel: OrderDetailsViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    private lateinit var mAdapter: ServiceRequestAdapter
    lateinit var mWeeksAdapter: WeeksAdapter
    lateinit var mSlotAdapter: SlotsAdapter
    private val taskId = ""
    private var date = ""
    var orderNo = ""
    var serviceType = ""
    private val ORDER_NO = "ORDER_NO"
    private val SERVICE_TYPE = "SERVICE_TYPE"
    private val SERVICE_TYPE_IMG = "SERVICE_TYPE_IMG"

    lateinit var options: JSONObject
    private val viewModels: OtpViewModel by viewModels()
    var service_url_image:String=""

    companion object {
        @JvmStatic
        fun newInstance(orderNo: String, serviceType: String, service_url_image: String) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    this.putString(ORDER_NO, orderNo)
                    this.putString(SERVICE_TYPE, serviceType)
                    this.putString(SERVICE_TYPE_IMG, service_url_image)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderNo = it.getString(ORDER_NO).toString()
            serviceType = it.getString(SERVICE_TYPE).toString()
            service_url_image=it.getString(SERVICE_TYPE_IMG).toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ServiceVMFactory(MainRepository(api))).get(ServiceViewModel::class.java)
        /*orderDetailsViewModel = ViewModelProvider(requireActivity(),
            OrderDetailsVMFactory(MainRepository(api)))[OrderDetailsViewModel::class.java]*/
        progressDialog = ProgressDialog(requireContext()).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "orderno $orderNo and $serviceType")

        Picasso.get().load(service_url_image).into(binding.imgType)

        binding.imgLogo.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, OrdersFragment.newInstance()).commit();
        }

        binding.help.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, OrdersFragment.newInstance()).commit();
        }


        AppUtils2.mobileno=SharedPreferenceUtil.getData(activity!!, "mobileNo", "-1").toString()
        viewModels.validateAccount(AppUtils2.mobileno)
        getServiceDetails(orderNo, serviceType)

        if(orderNo!=null){
            binding.bottomheadertext.visibility=View.VISIBLE
            binding.bottomheadertext.text=orderNo
        }else{
            binding.bottomheadertext.visibility=View.GONE
        }

        Handler(Looper.getMainLooper()).postDelayed({
            getServiceList()
        }, 500)

//        binding.imgadd.setOnClickListener {
//            binding.recycleView.visibility=View.VISIBLE
//            binding.imgminus.visibility=View.VISIBLE
//            binding.imgadd.visibility=View.GONE
//        }
//
//
//        binding.imgminus.setOnClickListener {
//            binding.recycleView.visibility=View.GONE
//            binding.imgadd.visibility=View.VISIBLE
//            binding.imgminus.visibility=View.GONE
//
//        }

        binding.help.setOnClickListener {
            val intent = Intent(requireContext(), AddComplaintsActivity::class.java)
            intent.putExtra("orderNo", orderNo)
            intent.putExtra("serviceType", serviceType)
            intent.putExtra("service_url_image", service_url_image)
            startActivity(intent)
        }
        binding.payNowBtn.setOnClickListener {
            try {
                val co = Checkout()
                co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
                co.open(requireActivity(), options)
            }catch (e: Exception){
                Log.d("TAG", "$e")
            }
        }
        (activity as HomeActivity).setOnPaymentListener(object : PaymentListener{
            override fun onPaymentSuccess(s: String?, response: PaymentData?) {
                progressDialog.setMessage("Saving Payment details...")
                progressDialog.show()
                orderDetailsViewModel.savePaymentResponse.observe(requireActivity()){
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
                val data = HashMap<String, Any>()
                data["razorpay_payment_id"] = response?.paymentId.toString()
                data["razorpay_order_id"] = response?.orderId.toString()
                data["razorpay_signature"] = response?.signature.toString()
                Log.d("TAG", "$data")
                orderDetailsViewModel.saveAppPaymentDetails(data)
            }

            override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {
                Log.d("TAG", "Error")
            }
        })
    }

    private fun getServiceDetails(orderNo: String, serviceType: String){
        orderDetailsViewModel.orderDetailsData.observe(requireActivity()) {
            if (it != null) {
                val data = it[0]
                val accountId = data.account_Name__r?.customer_id__c
                val service = data.service_Plan_Name__c
                val orderValueWithTax = data.order_Value_with_Tax__c
                val discount = orderValueWithTax.toString().toDouble() * 0.05
                val orderValueWithTaxAfterDiscount = data.order_Value_with_Tax__c.toString().toDouble() - discount
                binding.orderNameTv.text = data.service_Plan_Name__c
                binding.orderNoTv.text = orderNo
                binding.txtaddress.text= data.account_Name__r?.accountAddress ?: "N/A"
//                binding.orderDateTv.text  = data.createdDateText
//                binding.statusTv.text = data.status__c
                binding.apartmentSizeTv.text = "Selected Apartment Size - ${data.unit1__c}"
                binding.quantityTv.text = "QTY: ${data.quantity__c}"
//                binding.paymentStatusTv.text = if (data.enable_Payment_Link == false) "Paid" else "Unpaid"
                binding.totalTv.text = "₹ ${data.standard_Value__c}"
                binding.priceTv.text = "₹ ${Math.round(data.standard_Value__c!!.toDouble())}"
                binding.discountTv.text = if (data.orderDiscountValue != null) "₹ ${Math.round(data.orderDiscountValue.toDouble())}" else "₹ 0"
                binding.totalAmountTv.text = "₹ ${Math.round(data.order_Value_with_Tax__c!!.toDouble())}"
//                binding.completionDateTv.text = data.end_Date__c ?: "N/A"
                binding.contactDetailsTv.text = "${data.account_Name__r?.name} | ${data.account_Name__r?.mobile__c}"
                binding.addressTv.text = data.account_Name__r?.accountAddress ?: "N/A"
                binding.textaddress.text=  data.account_Name__r?.accountAddress ?: "N/A"
                binding.textserviceperiod.text=data.service_Period
                binding.textdatestart.text=AppUtils2.formatDateTime4(data.start_Date__c.toString())
                binding.textdateend.text=AppUtils2.formatDateTime4(data.end_Date__c.toString())
                val notes = prepareNotes(accountId, orderNo, service, serviceType, orderValueWithTax?.toDouble(), orderValueWithTaxAfterDiscount)
                options = prepareOption(notes, data.service_Plan_Name__c.toString(), orderValueWithTaxAfterDiscount.toString())
                if (data.enable_Payment_Link == true){
                    binding.payNowBtn.visibility = View.VISIBLE
                }else{
                    binding.payNowBtn.visibility = View.GONE
                }

                binding.statusTv.text = data.status__c

//
//                if(data.status__c.equals("Active")){
//                    binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))
//                }else {
//                    binding.statusTv.text = data.status__c
//                    binding.statusTv.setTextColor(Color.parseColor("#B71C1C"))
//                }
//

                if(data.status__c.equals("Expired")){

                    binding.statusTv.setTextColor(Color.parseColor("#D50000"))

                }else if(data.status__c.equals("Short Close")){

                    binding.statusTv.setTextColor(Color.parseColor("#FB8C00"))

                }else if(data.status__c.equals("Cancelled")){

                    binding.statusTv.setTextColor(Color.parseColor("#ff9e9e9e"))

                }else if(data.status__c.equals("Active")){

                    binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))

                }else if (data.status__c.equals("Rejected")){

                    binding.statusTv.setTextColor(Color.parseColor("#FFAB00"))

                }else{

                }

                if (data.enable_Payment_Link == false){
                    binding.paymentStatusTv.text="Paid"
                }else{
                    binding.paymentStatusTv.text="Unpaid"
                }
            }
        }
        orderDetailsViewModel.getOrderDetailsByOrderNo(orderNo, serviceType)
    }

    private fun getServiceList() {
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
        mAdapter = ServiceRequestAdapter()

        binding.recycleView.adapter = mAdapter

        viewModel.serviceList.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            Log.d(TAG, "onViewCreated: $it")
            mAdapter.setServiceList(it)
        })
        mAdapter.setOnServiceItemClicked(object : OnServiceRequestClickHandler {

            override fun onViewServiceClicked(position: Int) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MyServiceDetailsFragment.newInstance()).addToBackStack("OrderDetailsFragment").commit()
            }

            override fun onRescheduleServiceClicked(position: Int) {
                showRescheduleDialog()
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onViewCreated: $it")
            binding.recycleView.visibility=View.GONE
            binding.txterrormessage.visibility=View.VISIBLE
        })
        viewModel.getServiceRequest(orderNo, serviceType)
    }

    private fun showDetailsDialog(){
    }

    private fun showRescheduleDialog() {
        val li = LayoutInflater.from(activity)
        val promptsView = li.inflate(R.layout.reschedule_layout, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        val weekList: ArrayList<WeekModel> = ArrayList<WeekModel>()
        val today: DateTime = DateTime().withTimeAtStartOfDay()
        val txtMonth = promptsView.findViewById<View>(R.id.txtMonth) as TextView
        val txtSlots = promptsView.findViewById<View>(R.id.txtNoSlots) as TextView
        val btnSubmit = promptsView.findViewById<View>(R.id.btnSubmit) as Button
        val recycleWeeks: RecyclerView =
            promptsView.findViewById<View>(R.id.recycleView) as RecyclerView
        val recycleSlots: RecyclerView =
            promptsView.findViewById<View>(R.id.recycleSlots) as RecyclerView
        recycleWeeks.setHasFixedSize(true)
        var layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycleWeeks.layoutManager = layoutManager
        recycleSlots.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(activity)
        recycleSlots.layoutManager = layoutManager
        btnSubmit.visibility = View.GONE
        for (i in 2..8) {
            val weekModel = WeekModel()
            weekModel.setDateTime(today.plusDays(i).withTimeAtStartOfDay())
            weekModel.setDate(today.plusDays(i).dayOfMonth.toString())
            weekModel.setDays(AppUtils.getDays(today.plusDays(i).dayOfWeek))
            weekList.add(weekModel)
        }
        mWeeksAdapter = WeeksAdapter(weekList, today, txtMonth)
        recycleWeeks.adapter = mWeeksAdapter
        mWeeksAdapter.setOnRescheduleClickHandler(object : OnRescheduleClickHandler {
            override fun onDateSelected(position: Int) {
                weekList[position].getDateTime()
                val strDateTime: String = weekList[position].getDateTime().toString()
                val dateTime: DateTime = DateTime.parse(strDateTime)
                val fmt: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                date = fmt.print(dateTime)
//                getAvailableSlots(btnSubmit, txtSlots)
            }

            override fun onSlotSelected(position: Int) {
            }

        })
        mSlotAdapter = SlotsAdapter(requireActivity())
        recycleSlots.adapter = mSlotAdapter
        alertDialog.show()
    }

    private fun prepareOption(notes: JSONObject, description: String, amount: String): JSONObject{
        val options = JSONObject()
        options.put("name", "HiCare Services")
        options.put("description", description)
        options.put("image","https://hicare.in/pub/media/wysiwyg/home/Hyginenew1.png")
        options.put("theme.color", "#2BB77A")
        options.put("currency", "INR")
        options.put("amount", "${amount.toDouble().roundToInt()}00")
        options.put("notes", notes)
        return options
    }
    private fun prepareNotes(
        accountId: String?,
        orderNo: String?,
        service: String?,
        serviceType: String?,
        orderValue: Double?,
        orderValueAfterDiscount: Double?
    ): JSONObject{
        val notes = JSONObject()
        notes.put("Account_Id", accountId)
        notes.put("InvoiceNo", "")
        notes.put("OrderNo", orderNo)
        notes.put("Service", service)
        notes.put("OrderValue", orderValue)
        notes.put("OrderValueAfterDiscount", orderValueAfterDiscount)
        notes.put("ETDiscount", 5)
        notes.put("ETPaidAmount", orderValueAfterDiscount)
        notes.put("Param1", "")
        notes.put("Param2", "")
        notes.put("Param3", "")
        notes.put("IsEditOrder", true)
        notes.put("IsGeneralLink", false)
        notes.put("OrdersAmountMapping", "")
        notes.put("Service_Type", serviceType)
        return notes
    }
}