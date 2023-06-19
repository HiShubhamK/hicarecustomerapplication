package com.ab.hicareservices.ui.view.fragments

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentComplaintDetailBinding
import com.ab.hicareservices.ui.adapter.ComplaintAttachmentAdapter
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import org.json.JSONObject

class ComplaintDetailsFragment : Fragment() {

    private val TAG = "ComplaintDetailsFragment"
    lateinit var binding: FragmentComplaintDetailBinding
    lateinit var progressDialog: ProgressDialog

    private var complaintdate = ""
    private var complaintnum = ""
    var orderNo = ""
    var category = ""
    var complainttype = ""
    var serviceplan = ""
    var subject = ""
    var description = ""
    var status = ""
    var casenum = ""
    var complaintid = ""
    private val COMPLANTDATE = "COMPLANTDATE"
    private val COMPLAINTNO = "COMPLAINTNO"
    private val COMPLAINTID = "COMPLAINTID"
    private val ORDER_NO = "ORDER_NO"
    private val CATEGORY = "CATEGORY"
    private val COMPLAINT_TYPE = "COMPLAINT_TYPE"
    private val SERVICE_PLAN = "SERVICE_PLAN"
    private val SUBJECT = "SUBJECT"
    private val DESC = "DESC"
    private val STATUS = "STATUS"
    private val CASENUM = "CASENUM"
    private val ATTACHMENTS = "ATTACHMENTS"
    private lateinit var imageListnew: ArrayList<String>

    private lateinit var mAdapter: ComplaintAttachmentAdapter

    private val viewModel: ComplaintsViewModel by viewModels()

    lateinit var options: JSONObject
    private val viewModels: OtpViewModel by viewModels()
    var service_url_image: String = ""

    companion object {
        @JvmStatic
        fun newInstance(
            complaintdate1: String,
            complaintnum1: String,
            orderno1: String,
            category1: String,
            complainttype1: String,
            serviceplan1: String,
            subject1: String,
            description1: String,
            status1: String,
            casenum: String,
            imageList: ArrayList<String>,
            complaintid: String
        ) =
            ComplaintDetailsFragment().apply {
                arguments = Bundle().apply {
                    this.putString(COMPLANTDATE, complaintdate1)
                    this.putString(COMPLAINTNO, complaintnum1)
                    this.putString(ORDER_NO, orderno1)
                    this.putString(CATEGORY, category1)
                    this.putString(COMPLAINT_TYPE, complainttype1)
                    this.putString(SERVICE_PLAN, serviceplan1)
                    this.putString(SUBJECT, subject1)
                    this.putString(DESC, description1)
                    this.putString(STATUS, status1)
                    this.putString(CASENUM, casenum)
                    this.putString(COMPLAINTID, complaintid)
                    this.putStringArrayList("attachmentlist", imageList)

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageListnew = ArrayList()
        arguments?.let {
            complaintdate = it.getString(COMPLANTDATE).toString()
            complaintnum = it.getString(COMPLAINTNO).toString()
            orderNo = it.getString(ORDER_NO).toString()
            category = it.getString(CATEGORY).toString()
            complainttype = it.getString(COMPLAINT_TYPE).toString()
            serviceplan = it.getString(SERVICE_PLAN).toString()
            subject = it.getString(SUBJECT).toString()
            description = it.getString(DESC).toString()
            status = it.getString(STATUS).toString()
            casenum = it.getString(CASENUM).toString()
            imageListnew = it.getStringArrayList("attachmentlist") as ArrayList<String>
            complaintid = it.getString(COMPLAINTID).toString()


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComplaintDetailBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(requireContext()).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Log.d("TAG", "orderno $orderNo and $serviceplan")

//        Picasso.get().load(service_url_image).into(binding.imgType)
//
//        binding.imgLogo.setOnClickListener{
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.container, OrdersFragment.newInstance()).commit();
//        }
        binding.title.text = "Complaint Details"
        binding.txtStatus.text = status
        binding.txtComplaintDate.text = AppUtils2.formatDateTime4(complaintdate)
        binding.txtcomplaintno.text = complaintnum
        binding.txtorderno.text = orderNo
        binding.txtCategory.text = category
        binding.txtcomplainttype.text = complainttype
        binding.txtServicePlan.text = serviceplan
        binding.txtSubject.text = subject
        binding.txtDescription.text = description
        if (status == "Open") {

            binding.txtStatus.setTextColor(Color.parseColor("#D50000"))

        } else if (status == "Pending") {

            binding.txtStatus.setTextColor(Color.parseColor("#FB8C00"))

        } else if (status == "Resolved" || status == "Closed") {

            binding.txtStatus.setTextColor(Color.parseColor("#2bb77a"))

        } else {

        }
        binding.imgLogo.setOnClickListener {
            requireActivity().finish()
        }

//        binding.txtSeeAttachment.setOnClickListener{
////            if(!imageListnew.isEmpty()) {
////                requireActivity().supportFragmentManager.beginTransaction()
////                    .replace(R.id.container, ComplaintAttachmentFragment.newInstance(complaintid))
////                    .addToBackStack("AccountFragment").commit();
////            }else{
////                Toast.makeText(requireActivity(),"No attachment available",Toast.LENGTH_LONG).show()
////            }
//        }
        AppUtils2.mobileno =
            SharedPreferenceUtil.getData(requireActivity(), "mobileNo", "-1").toString()
        progressDialog.show()
        getcomplaintAttachment()

//        if(orderNo!=null){
//            binding.bottomheadertext.visibility=View.VISIBLE
//            binding.bottomheadertext.text=orderNo
//        }else{
//            binding.bottomheadertext.visibility=View.GONE
//        }


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

//        binding.help.setOnClickListener {
//            val intent = Intent(requireContext(), AddComplaintsActivity::class.java)
//            intent.putExtra("orderNo", orderNo)
//            intent.putExtra("serviceType", serviceType)
//            startActivity(intent)
//        }
//        binding.payNowBtn.setOnClickListener {
//            try {
//                val co = Checkout()
//                co.setKeyID("rzp_test_sgH3fCu3wJ3T82")
//                co.open(requireActivity(), options)
//            }catch (e: Exception){
//                Log.d("TAG", "$e")
//            }
//        }
//        (activity as HomeActivity).setOnPaymentListener(object : PaymentListener{
//            override fun onPaymentSuccess(s: String?, response: PaymentData?) {
//                progressDialog.setMessage("Saving Payment details...")
//                progressDialog.show()
//                orderDetailsViewModel.savePaymentResponse.observe(requireActivity()){
//                    progressDialog.dismiss()
//                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
//                }
//                val data = HashMap<String, Any>()
//                data["razorpay_payment_id"] = response?.paymentId.toString()
//                data["razorpay_order_id"] = response?.orderId.toString()
//                data["razorpay_signature"] = response?.signature.toString()
//                Log.d("TAG", "$data")
//                orderDetailsViewModel.saveAppPaymentDetails(data)
//            }
//
//            override fun onPaymentError(p0: Int, p1: String?, response: PaymentData?) {
//                Log.d("TAG", "Error")
//            }
//        })
    }

    private fun getServiceDetails(orderNo: String, serviceType: String) {
//        orderDetailsViewModel.orderDetailsData.observe(requireActivity()) {
//            if (it != null) {
//                val data = it[0]
//                val accountId = data.account_Name__r?.customer_id__c
//                val service = data.service_Plan_Name__c
//                val orderValueWithTax = data.order_Value_with_Tax__c
//                val discount = orderValueWithTax.toString().toDouble() * 0.05
//                val orderValueWithTaxAfterDiscount = data.order_Value_with_Tax__c.toString().toDouble() - discount
//                binding.orderNameTv.text = data.service_Plan_Name__c
//                binding.orderNoTv.text = orderNo
//                binding.txtaddress.text= data.account_Name__r?.accountAddress ?: "N/A"
////                binding.orderDateTv.text  = data.createdDateText
////                binding.statusTv.text = data.status__c
//                binding.apartmentSizeTv.text = "Selected Apartment Size - ${data.unit1__c}"
//                binding.quantityTv.text = "QTY: ${data.quantity__c}"
////                binding.paymentStatusTv.text = if (data.enable_Payment_Link == false) "Paid" else "Unpaid"
//                binding.totalTv.text = "₹ ${data.standard_Value__c}"
//                binding.priceTv.text = "₹ ${Math.round(data.standard_Value__c!!.toDouble())}"
//                binding.discountTv.text = if (data.orderDiscountValue != null) "₹ ${Math.round(data.orderDiscountValue.toDouble())}" else "₹ 0"
//                binding.totalAmountTv.text = "₹ ${Math.round(data.order_Value_with_Tax__c!!.toDouble())}"
////                binding.completionDateTv.text = data.end_Date__c ?: "N/A"
//                binding.contactDetailsTv.text = "${data.account_Name__r?.name} | ${data.account_Name__r?.mobile__c}"
//                binding.addressTv.text = data.account_Name__r?.accountAddress ?: "N/A"
//                binding.textaddress.text=  data.account_Name__r?.accountAddress ?: "N/A"
//                binding.textserviceperiod.text=data.service_Period
//                binding.textdatestart.text=AppUtils2.formatDateTime4(data.start_Date__c.toString())
//                binding.textdateend.text=AppUtils2.formatDateTime4(data.end_Date__c.toString())
//                val notes = prepareNotes(accountId, orderNo, service, serviceType, orderValueWithTax?.toDouble(), orderValueWithTaxAfterDiscount)
//                options = prepareOption(notes, data.service_Plan_Name__c.toString(), orderValueWithTaxAfterDiscount.toString())
//                if (data.enable_Payment_Link == true){
//                    binding.payNowBtn.visibility = View.VISIBLE
//                }else{
//                    binding.payNowBtn.visibility = View.GONE
//                }
//
//                binding.statusTv.text = data.status__c
//
////
////                if(data.status__c.equals("Active")){
////                    binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))
////                }else {
////                    binding.statusTv.text = data.status__c
////                    binding.statusTv.setTextColor(Color.parseColor("#B71C1C"))
////                }
////
//
//                if(data.status__c.equals("Expired")){
//
//                    binding.statusTv.setTextColor(Color.parseColor("#D50000"))
//
//                }else if(data.status__c.equals("Short Close")){
//
//                    binding.statusTv.setTextColor(Color.parseColor("#FB8C00"))
//
//                }else if(data.status__c.equals("Cancelled")){
//
//                    binding.statusTv.setTextColor(Color.parseColor("#ff9e9e9e"))
//
//                }else if(data.status__c.equals("Active")){
//
//                    binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))
//
//                }else if (data.status__c.equals("Rejected")){
//
//                    binding.statusTv.setTextColor(Color.parseColor("#FFAB00"))
//
//                }else{
//
//                }
//
//                if (data.enable_Payment_Link == false){
//                    binding.paymentStatusTv.text="Paid"
//                }else{
//                    binding.paymentStatusTv.text="Unpaid"
//                }
//            }
//        }
//        orderDetailsViewModel.getOrderDetailsByOrderNo(orderNo, serviceType)
    }

    private fun getcomplaintAttachment() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        mAdapter = ComplaintAttachmentAdapter()

//        viewModel.attachments.observe(requireActivity(),{
//
//           if (it!=null){
//               imageList.addAll(it)
//           }
//        })
        viewModel.attachments.observe(requireActivity(), Observer {
//            Log.d(TAG, "onViewCreated: $it")
//            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext,"FAiles",Toast.LENGTH_SHORT).show()
            if (it != null) {
                mAdapter.setAttachment(it as ArrayList<String>)
                progressDialog.dismiss()
                binding.recyclerView.visibility=View.VISIBLE
                binding.tvNodata.visibility=View.GONE
            }else{
                progressDialog.dismiss()
                binding.recyclerView.visibility=View.GONE
                binding.tvNodata.visibility=View.VISIBLE
            }

//        Log.e("TAG", "Attachments: $imageList")
//            Toast.makeText(requireContext(),"attacchmnt"+imageList,Toast.LENGTH_SHORT).show()

        })
        viewModel.errorMessage.observe(requireActivity(), Observer {
//            Log.d(TAG, "onViewCreated: $it")
//            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
//            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
//            binding.recyclerView.visibility=View.GONE
//            binding.tvNodata.visibility=View.VISIBLE
//            binding.tvNodata.text = it

//        Log.e("TAG", "Attachments: $imageList")
//            Toast.makeText(requireContext(),"attacchmnt"+imageList,Toast.LENGTH_SHORT).show()

        })



        binding.recyclerView.adapter = mAdapter

//        viewModel.getAllComplaints("9967994682")

//        binding.progressBar.visibility= View.GONE

        viewModel.getComlaintAttachment(complaintid)
//        viewModel.getAllComplaints(SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString())
    }


}