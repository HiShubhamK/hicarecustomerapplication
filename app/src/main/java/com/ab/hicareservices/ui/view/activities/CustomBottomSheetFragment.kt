package com.ab.hicareservices.ui.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.FaqList
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.ui.adapter.BookingFaqAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomBottomSheetFragment() : BottomSheetDialogFragment() {

    var data=""
    var servicename=""
    var servicecode=""
    var thumbnail=""
    var shortdescrition=""
    var descrition=""
    var fulldescritpition=""
    var price=""
    var discountedPrice=""
    private lateinit var mAdapter: BookingFaqAdapter

    companion object {
        private const val ARG_DATA = "data_key"
        private const val ARG_SERVICENAME = "data_SERVICENAME"
        private const val ARG_SERVICECODE = "data_SERVICECODE"
        private const val ARG_THUMBNAIL = "data_THUMBNAIL"
        private const val ARG_SHORTDESCRIPTION = "data_SHORTDESCRIPTION"
        private const val ARG_DESCRIPTION = "data_DESCRIPTION"
        private const val ARG_FUL_DESCRIPTION ="data_full_description"
        private const val ARG_PRICE ="data_PRICE"
        private const val ARG_DISCOUNTEDPRICE ="data_DISCOUNTEDPRICE"
        private const val ARG_FAQLIST ="data_FAQLIST"


        fun newInstance(
            data: Int?,
            servicename: String?,
            servicecode: String?,
            thumbnail: String?,
            shortdescrition: String?,
            descrition: String?,
            servicePlanDescription: String?,
            price: Int?,
            discountedPrice: Int?,
            faqList: ArrayList<FaqList>
        ): CustomBottomSheetFragment {
            val fragment = CustomBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_DATA, data.toString())
            args.putString(ARG_SERVICENAME, servicename.toString())
            args.putString(ARG_SERVICECODE, servicecode.toString())
            args.putString(ARG_THUMBNAIL, thumbnail.toString())
            args.putString(ARG_SHORTDESCRIPTION, shortdescrition.toString())
            args.putString(ARG_DESCRIPTION, descrition.toString())
            args.putString(ARG_FUL_DESCRIPTION,servicePlanDescription.toString())
            args.putString(ARG_PRICE,price.toString())
            args.putString(ARG_DISCOUNTEDPRICE,discountedPrice.toString())
            args.putParcelableArrayList(ARG_FAQLIST,ArrayList(faqList))
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.custom_bottom_sheet, container, false)

        data = arguments?.getString(ARG_DATA).toString()
        servicename = arguments?.getString(ARG_SERVICENAME).toString()
        servicecode = arguments?.getString(ARG_SERVICECODE).toString()
        thumbnail = arguments?.getString(ARG_THUMBNAIL).toString()
        shortdescrition = arguments?.getString(ARG_SHORTDESCRIPTION).toString()
        descrition = arguments?.getString(ARG_DESCRIPTION).toString()
        fulldescritpition=arguments?.getString(ARG_FUL_DESCRIPTION).toString()
        price=arguments?.getString(ARG_PRICE).toString()
        discountedPrice=arguments?.getString(ARG_DISCOUNTEDPRICE).toString()
        val serviceList = arguments?.getParcelableArrayList<FaqList>(ARG_FAQLIST)

        val textView = view.findViewById<AppCompatTextView>(R.id.servicename)
        val textViewdescription = view.findViewById<AppCompatTextView>(R.id.servicedescription)
        val thumbnails = view.findViewById<AppCompatImageView>(R.id.servicethumbnail)
        val shortdecriptions = view.findViewById<AppCompatTextView>(R.id.serviceshortdescription)
        val imgclose = view.findViewById<LinearLayout>(R.id.imgclose)
        val recFAQ=view.findViewById<RecyclerView>(R.id.recFAQ)
        var lnrFAQ=view.findViewById<LinearLayout>(R.id.lnrFAQ)

        textView.text = servicename
        textViewdescription.text=descrition
        shortdecriptions.text=fulldescritpition
        Glide.with(activity!!).load(thumbnail).into(thumbnails)

         if(serviceList!!.isNotEmpty() || serviceList!=null){
             lnrFAQ.visibility=View.VISIBLE
             Toast.makeText(activity,serviceList.toString(),Toast.LENGTH_SHORT).show()
         }else{
             lnrFAQ.visibility=View.GONE
         }

//        recFAQ.layoutManager = GridLayoutManager(activity, 3)
        recFAQ.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingFaqAdapter()
        recFAQ.adapter = this.mAdapter

        if (serviceList != null) {
            mAdapter.setFaq(serviceList, activity!!)
        }


        imgclose.setOnClickListener {
            dismiss()
        }

        return view

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        // Set maximum height and width
//        val displayMetrics = DisplayMetrics()
//        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
//
//        val maxHeight = (displayMetrics.heightPixels * 0.8).toInt() // Adjust the percentage as needed
//        val maxWidth = (displayMetrics.widthPixels * 0.9).toInt() // Adjust the percentage as needed
//
//        dialog?.window?.setLayout(
//            if (maxWidth > 0) maxWidth else ViewGroup.LayoutParams.MATCH_PARENT,
//            if (maxHeight > 0) maxHeight else ViewGroup.LayoutParams.WRAP_CONTENT
//        )
    }

}
