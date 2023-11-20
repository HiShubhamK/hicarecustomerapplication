package com.ab.hicareservices.ui.view.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ab.hicareservices.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_bottom_sheet, container, false)
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