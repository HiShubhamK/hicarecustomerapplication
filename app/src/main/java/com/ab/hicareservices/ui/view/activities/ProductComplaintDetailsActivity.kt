package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.productcomplaint.productdetails.ComplaintAttachment
import com.ab.hicareservices.databinding.ActivityProductComplaintDetailBinding
import com.ab.hicareservices.ui.adapter.ComplaintAttachmentAdapter
import com.ab.hicareservices.ui.adapter.ProductComplaintAttachmentAdapter
import com.ab.hicareservices.ui.view.fragments.HomeFragment
import com.ab.hicareservices.ui.viewmodel.ComplaintsViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2
import org.json.JSONObject

class ProductComplaintDetailsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProductComplaintDetailBinding
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
    var complaintidd = ""
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
    private lateinit var imageListnew: ArrayList<ComplaintAttachment>

    private lateinit var mAdapter: ProductComplaintAttachmentAdapter

    private val viewModel: ComplaintsViewModel by viewModels()

    lateinit var options: JSONObject
    private val viewModels: OtpViewModel by viewModels()
    var service_url_image: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_complaint_detail)
        try {
            binding=ActivityProductComplaintDetailBinding.inflate(layoutInflater)
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

            try {
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
//                imageListnew = intent.getStringArrayListExtra("Imagelist") as ArrayList<String>
                complaintid = intent.getStringExtra("Complaintid").toString()
                complaintidd = intent.getStringExtra("complaintid").toString()
            }catch (e:Exception){
                e.printStackTrace()
            }



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
            getcomplaintAttachment()

        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    private fun getcomplaintAttachment() {
//        progressDialog.show()
        imageListnew= ArrayList()
        viewModel.getproductcomplaintlist.observe(this, Observer {
//            Log.d(TAG, "onViewCreated: $it")
//            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
//            Toast.makeText(applicationContext,"FAiles",Toast.LENGTH_SHORT).show()
            if (it!=null){
                mAdapter.setAttachment(it.ComplaintAttachment as ArrayList<ComplaintAttachment>)
            }
            progressDialog.dismiss()

//        Log.e("TAG", "Attachments: $imageList")
//            Toast.makeText(requireContext(),"attacchmnt"+imageList,Toast.LENGTH_SHORT).show()

        })
        binding.recyclerView.layoutManager =LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = ProductComplaintAttachmentAdapter()
        if (imageListnew!=null) {
            if (imageListnew.isNotEmpty()) {
                mAdapter.setAttachment(imageListnew)
                binding.lnrAttachments.visibility = View.VISIBLE
                binding.tvNodata.visibility = View.GONE
                progressDialog.dismiss()

            } else {
                binding.lnrAttachments.visibility = View.GONE
                binding.tvNodata.visibility = View.VISIBLE
            }

        }else{
            progressDialog.dismiss()
            binding.lnrAttachments.visibility = View.GONE
            binding.tvNodata.visibility = View.VISIBLE
        }


        viewModel.GetComplaintDetailId(complaintidd.toInt())



//        viewModel.attachments.observe(requireActivity(),{
//
//           if (it!=null){
//               imageList.addAll(it)
//           }
//        })

//        viewModel.errorMessage.observe(this, Observer {
////            Log.d(TAG, "onViewCreated: $it")
////            Toast.makeText(applicationContext,viewModel.complaintList.toString(),Toast.LENGTH_SHORT).show()
////            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
//            progressDialog.dismiss()
////            binding.recyclerView.visibility=View.GONE
////            binding.tvNodata.visibility=View.VISIBLE
////            binding.tvNodata.text = it
//
////        Log.e("TAG", "Attachments: $imageList")
////            Toast.makeText(requireContext(),"attacchmnt"+imageList,Toast.LENGTH_SHORT).show()
//
//        })
//


        binding.recyclerView.adapter = mAdapter
//        binding.recyclerView.addItemDecoration(CirclePagerIndicatorDecoration())


//        viewModel.getAllComplaints("9967994682")

//        binding.progressBar.visibility= View.GONE

//        viewModel.getComlaintAttachment(complaintid)
//        viewModel.getAllComplaints(SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString())
    }
    class CirclePagerIndicatorDecoration : RecyclerView.ItemDecoration() {
        private val colorActive = 0x727272
        private val colorInactive = 0xF44336

        /**
         * Height of the space the indicator takes up at the bottom of the view.
         */
        private val mIndicatorHeight = (DP * 16).toInt()

        /**
         * Indicator stroke width.
         */
        private val mIndicatorStrokeWidth = DP * 2

        /**
         * Indicator width.
         */
        private val mIndicatorItemLength = DP * 16

        /**
         * Padding between indicators.
         */
        private val mIndicatorItemPadding = DP * 4

        /**
         * Some more natural animation interpolation
         */
        private val mInterpolator: AccelerateDecelerateInterpolator =
            AccelerateDecelerateInterpolator()
        private val mPaint: Paint = Paint()
        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
            val itemCount = parent.adapter!!.itemCount

            // center horizontally, calculate width and subtract half from center
            val totalLength = mIndicatorItemLength * itemCount
            val paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding
            val indicatorTotalWidth = totalLength + paddingBetweenItems
            val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

            // center vertically in the allotted space
            val indicatorPosY = parent.height - mIndicatorHeight / 2f
            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)


            // find active page (which should be highlighted)
            val layoutManager = parent.layoutManager as LinearLayoutManager?
            val activePosition = layoutManager!!.findFirstVisibleItemPosition()
            if (activePosition == RecyclerView.NO_POSITION) {
                return
            }

            // find offset of active page (if the user is scrolling)
            val activeChild = layoutManager.findViewByPosition(activePosition)
            val left = activeChild!!.left
            val width = activeChild.width

            // on swipe the active item will be positioned from [-width, 0]
            // interpolate offset for smooth animation
            val progress: Float = mInterpolator.getInterpolation(left * -1 / width.toFloat())
            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
        }
//        fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
//            super.onDrawOver(c, parent, state!!)

//        }

        private fun drawInactiveIndicators(
            c: Canvas,
            indicatorStartX: Float,
            indicatorPosY: Float,
            itemCount: Int
        ) {
            mPaint.setColor(Color.GRAY)

            // width of item indicator including padding
            val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
            var start = indicatorStartX
            for (i in 0 until itemCount) {
                // draw the line for every item
                c.drawCircle(start + mIndicatorItemLength, indicatorPosY, itemWidth / 6, mPaint)
                //  c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
                start += itemWidth
            }
        }

        private fun drawHighlights(
            c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
            highlightPosition: Int, progress: Float, itemCount: Int
        ) {
            mPaint.color = Color.parseColor("#2bb77a")

            // width of item indicator including padding
            val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
            if (progress == 0f) {
                // no swipe, draw a normal indicator
                val highlightStart = indicatorStartX + itemWidth * highlightPosition
                /*   c.drawLine(highlightStart, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
        */c.drawCircle(highlightStart, indicatorPosY, itemWidth / 6, mPaint)
            } else {
                val highlightStart = indicatorStartX + itemWidth * highlightPosition
                // calculate partial highlight
                val partialLength = mIndicatorItemLength * progress
                c.drawCircle(
                    highlightStart + mIndicatorItemLength,
                    indicatorPosY,
                    itemWidth / 6,
                    mPaint
                )

                // draw the cut off highlight
                /* c.drawLine(highlightStart + partialLength, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
*/
                // draw the highlight overlapping to the next item as well
                /* if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth;
                */
                /*c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + partialLength, indicatorPosY, mPaint);*/
                /*
                c.drawCircle(highlightStart ,indicatorPosY,itemWidth/4,mPaint);

            }*/
            }
        }


        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = mIndicatorHeight

        }

        companion object {
            private val DP: Float = Resources.getSystem().getDisplayMetrics().density
        }

        init {
            mPaint.strokeCap = Paint.Cap.ROUND
            mPaint.strokeWidth = mIndicatorStrokeWidth
            mPaint.style = Paint.Style.FILL
            mPaint.isAntiAlias = true
        }
    }


}