package com.hc.hicareservices.ui.view.fragments

import android.app.ProgressDialog
import android.content.Intent
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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hc.hicareservices.R
import com.hc.hicareservices.data.SharedPreferenceUtil
import com.hc.hicareservices.data.model.weeks.WeekModel
import com.hc.hicareservices.databinding.FragmentOrderDetailsBinding
import com.hc.hicareservices.ui.adapter.ServiceRequestAdapter
import com.hc.hicareservices.ui.adapter.SlotsAdapter
import com.hc.hicareservices.ui.adapter.WeeksAdapter
import com.hc.hicareservices.ui.handler.OnRescheduleClickHandler
import com.hc.hicareservices.ui.handler.OnServiceRequestClickHandler
import com.hc.hicareservices.ui.handler.PaymentListener
import com.hc.hicareservices.ui.view.activities.AddComplaintsActivity
import com.hc.hicareservices.ui.view.activities.HomeActivity
import com.hc.hicareservices.ui.viewmodel.OrderDetailsViewModel
import com.hc.hicareservices.ui.viewmodel.OtpViewModel
import com.hc.hicareservices.ui.viewmodel.ServiceViewModel
import com.hc.hicareservices.utils.AppUtils
import com.hc.hicareservices.utils.AppUtils2
import com.razorpay.Checkout
import com.razorpay.PaymentData
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.json.JSONObject
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
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
    lateinit var options: JSONObject
    private val viewModels: OtpViewModel by viewModels()

    companion object {
        @JvmStatic
        fun newInstance(orderNo: String, serviceType: String) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                    this.putString(ORDER_NO, orderNo)
                    this.putString(SERVICE_TYPE, serviceType)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderNo = it.getString(ORDER_NO).toString()
            serviceType = it.getString(SERVICE_TYPE).toString()
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

        AppUtils2.mobileno=SharedPreferenceUtil.getData(activity!!, "mobileNo", "-1").toString()
        viewModels.validateAccount(AppUtils2.mobileno)
        getServiceDetails(orderNo, serviceType)

        Handler(Looper.getMainLooper()).postDelayed({
            getServiceList()
        }, 5000)

        binding.complaintLayout.setOnClickListener {
            val intent = Intent(requireContext(), AddComplaintsActivity::class.java)
            intent.putExtra("orderNo", orderNo)
            intent.putExtra("serviceType", serviceType)
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
                binding.orderDateTv.text = data.createdDateText
                binding.statusTv.text = data.status__c
                binding.apartmentSizeTv.text = "Selected Apartment Size - ${data.unit1__c}"
                binding.quantityTv.text = "QTY: ${data.quantity__c}"
                binding.paymentStatusTv.text = if (data.enable_Payment_Link == false) "Paid" else "Unpaid"
                binding.totalTv.text = "₹ ${data.standard_Value__c}"
                binding.priceTv.text = "₹ ${data.standard_Value__c}"
                binding.discountTv.text = if (data.orderDiscountValue != null) "₹ ${data.orderDiscountValue}" else "₹ 0"
                binding.totalAmountTv.text = "₹ ${data.order_Value_with_Tax__c}"
                binding.completionDateTv.text = data.end_Date__c ?: "N/A"
                binding.contactDetailsTv.text = "${data.account_Name__r?.name} | ${data.account_Name__r?.mobile__c}"
                binding.addressTv.text = data.account_Name__r?.accountAddress ?: "N/A"
                val notes = prepareNotes(accountId, orderNo, service, serviceType, orderValueWithTax?.toDouble(), orderValueWithTaxAfterDiscount)
                options = prepareOption(notes, data.service_Plan_Name__c.toString(), orderValueWithTaxAfterDiscount.toString())
                if (data.enable_Payment_Link == true){
                    binding.payNowBtn.visibility = View.VISIBLE
                }else{
                    binding.payNowBtn.visibility = View.VISIBLE
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
            Log.d(TAG, "onViewCreated: $it")
            mAdapter.setServiceList(it)
            binding.progressBar.visibility = View.GONE
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