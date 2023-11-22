package com.ab.hicareservices.ui.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.ab.hicareservices.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomBottomSheetFragment() : BottomSheetDialogFragment() {

    var data=""
    var servicename=""
    var servicecode=""
    var thumbnail=""
    var shortdescrition=""
    var descrition=""

    companion object {
        private const val ARG_DATA = "data_key"
        private const val ARG_SERVICENAME = "data_SERVICENAME"
        private const val ARG_SERVICECODE = "data_SERVICECODE"
        private const val ARG_THUMBNAIL = "data_THUMBNAIL"
        private const val ARG_SHORTDESCRIPTION = "data_SHORTDESCRIPTION"
        private const val ARG_DESCRIPTION = "data_DESCRIPTION"

        fun newInstance(
            data: Int?,
            servicename: String?,
            servicecode: String?,
            thumbnail: String?,
            shortdescrition: String?,
            descrition: String?
        ): CustomBottomSheetFragment {
            val fragment = CustomBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_DATA, data.toString())
            args.putString(ARG_SERVICENAME, servicename.toString())
            args.putString(ARG_SERVICECODE, servicecode.toString())
            args.putString(ARG_THUMBNAIL, thumbnail.toString())
            args.putString(ARG_SHORTDESCRIPTION, shortdescrition.toString())
            args.putString(ARG_DESCRIPTION, descrition.toString())
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

        val textView = view.findViewById<AppCompatTextView>(R.id.servicename)
        val textViewdescription = view.findViewById<AppCompatTextView>(R.id.servicedescription)
        val thumbnails = view.findViewById<AppCompatImageView>(R.id.servicethumbnail)
        val shortdecriptions = view.findViewById<AppCompatTextView>(R.id.serviceshortdescription)
        textView.text = servicename
        textViewdescription.text=descrition
        shortdecriptions.text=shortdescrition
        Glide.with(activity!!).load(thumbnail).into(thumbnails)

        return view

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
