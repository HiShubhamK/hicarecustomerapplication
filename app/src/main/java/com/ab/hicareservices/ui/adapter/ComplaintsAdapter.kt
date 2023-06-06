package com.ab.hicareservices.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.databinding.LayoutComplaintsAdaptersBinding
import com.ab.hicareservices.ui.view.fragments.ComplaintDetailsFragment
import com.ab.hicareservices.utils.AppUtils2


class ComplaintsAdapter() : RecyclerView.Adapter<ComplaintsAdapter.MainViewHolder>() {

    var complaints = mutableListOf<ComplaintsData>()
    var imageList = ArrayList<String>()
    private lateinit var  requireActivity: FragmentActivity

    fun setComplaintsList(
        complaintdata: List<ComplaintsData>?,
        imageList: ArrayList<String>,
        requireActivity: FragmentActivity
    ) {
        if (complaintdata != null) {
            this.complaints = complaintdata.toMutableList()
            this.imageList = imageList
            this.requireActivity = requireActivity
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutComplaintsAdaptersBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val complaints = complaints[position]
            holder.binding.txthello.text = complaints.ServicePlan_c
            holder.binding.txtStatus.text = complaints.Status
            holder.binding.tvCaseNumber.text =
                AppUtils2.formatDateTime4(complaints.CreatedDate.toString())


            holder.binding.txtCreationDate.text =
                "Case Number:- " + complaints.CaseNumber.toString()
            if (complaints.Status.equals("Open")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#D50000"))

            } else if (complaints.Status.equals("Pending")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#FB8C00"))

            } else if (complaints.Status.equals("Resolved") || complaints.Status.equals("Closed")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#2bb77a"))

            } else {

            }
//            imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/cb8b73d2-da3c-4ce6-a172-ae774063d915.jpg")
            holder.itemView.setOnClickListener {
//                if (imageList.isNotEmpty()) {
                    val appCompatActivity = it.context as AppCompatActivity
                    appCompatActivity.supportFragmentManager.beginTransaction()
                        .replace(
                            com.ab.hicareservices.R.id.container,
                            ComplaintDetailsFragment.newInstance(
                                AppUtils2.formatDateTime4(complaints.CreatedDate.toString()),
                                complaints.ComplaintNo_c.toString(),
                                complaints.OrderNo_c.toString(),
                                "Pest",
                                complaints.ComplaintDescription_c.toString(),
                                complaints.ServicePlan_c.toString(),
                                complaints.Subject.toString(),
                                complaints.Description.toString(),
                                holder.binding.txtStatus.text.toString(),
                                complaints.CaseNumber.toString(),
                                imageList
                            )
                        )
                        .addToBackStack(null)
                        .commit()
//                } else {
//                    Toast.makeText(requireActivity,"Sorry, attachments not available!", Toast.LENGTH_SHORT).show()
//
//                }
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(com.ab.hicareservices.R.id.container, ComplaintDetailsFragment.newInstance()).addToBackStack("OrderDetailsFragment").commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return complaints.size
    }

    class MainViewHolder(val binding: LayoutComplaintsAdaptersBinding) :
        RecyclerView.ViewHolder(binding.root)
}