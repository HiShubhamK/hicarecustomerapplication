package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityComplaintDetailsBinding
import com.ab.hicareservices.ui.adapter.ComplaintAttachmentAdapter
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import org.json.JSONObject

class ComplaintDetailsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityComplaintDetailsBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_details)
        binding=ActivityComplaintDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog= ProgressDialog(this,R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        val intent=intent
//
//        intent.putExtra("Dateformat", AppUtils2.formatDateTime4(complaints.CreatedDate.toString()))
//        intent.putExtra("ComplaintNo",complaints.ComplaintNo_c.toString())
//        intent.putExtra("OrderNO",complaints.OrderNo_c.toString())
//        intent.putExtra("Pest","Pest")
//        intent.putExtra("Cdescription",complaints.ComplaintDescription_c.toString())
//        intent.putExtra("ServicePlan", complaints.ServicePlan_c.toString())
//        intent.putExtra("Subject",complaints.Subject.toString())
//        intent.putExtra("Description",complaints.Description.toString())
//        intent.putExtra("status",holder.binding.txtStatus.text.toString())
//        intent.putExtra("CaseNo",complaints.CaseNumber.toString())
//        intent.putStringArrayListExtra("Imagelist",imageList)
//        intent.putExtra("Complaintid",complaints.Id.toString())

        complaintdate = intent.getStringExtra("Dateformat").toString()
        complaintnum = intent.getStringExtra("ComplaintNo").toString()
        orderNo = intent.getStringExtra("OrderNO").toString()
        category = intent.getStringExtra("Pest").toString()
        complainttype = intent.getStringExtra("Cdescription").toString()
        serviceplan = intent.getStringExtra("ServicePlan").toString()
        subject = intent.getStringExtra("Subject").toString()
        description = intent.getStringExtra("Description").toString()
        status = intent.getStringExtra("status").toString()
        casenum = intent.getStringExtra("CaseNo").toString()
        imageListnew = intent.getStringArrayListExtra("Imagelist") as ArrayList<String>
        complaintid = intent.getStringExtra("Complaintid").toString()


        binding.title.text = "Complaint Detail"
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
            onBackPressed()
        }

        AppUtils2.mobileno =
            SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        progressDialog.show()
        getcomplaintAttachment()


    }

    private fun getcomplaintAttachment() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = ComplaintAttachmentAdapter()

//        viewModel.attachments.observe(requireActivity(),{
//
//           if (it!=null){
//               imageList.addAll(it)
//           }
//        })
        viewModel.attachments.observe(this, Observer {
//            Log.d(TAG, "onViewCreated: $it")
//            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext,"FAiles",Toast.LENGTH_SHORT).show()
            if (it != null) {
                mAdapter.setAttachment(it as ArrayList<String>)
                progressDialog.dismiss()
                binding.recyclerView.visibility= View.VISIBLE
                binding.tvNodata.visibility= View.GONE
            }else{
                progressDialog.dismiss()
                binding.recyclerView.visibility= View.GONE
                binding.tvNodata.visibility= View.VISIBLE
            }

//        Log.e("TAG", "Attachments: $imageList")
//            Toast.makeText(requireContext(),"attacchmnt"+imageList,Toast.LENGTH_SHORT).show()

        })
        viewModel.errorMessage.observe(this, Observer {
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