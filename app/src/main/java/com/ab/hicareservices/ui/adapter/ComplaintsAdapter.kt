package com.ab.hicareservices.ui.adapter

import android.R
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.databinding.LayoutComplaintsAdaptersBinding
import com.ab.hicareservices.ui.view.fragments.ComplaintDetailsFragment
import com.ab.hicareservices.utils.AppUtils2


class ComplaintsAdapter() : RecyclerView.Adapter<ComplaintsAdapter.MainViewHolder>() {

    var complaints = mutableListOf<ComplaintsData>()
    fun setComplaintsList(complaintdata: List<ComplaintsData>?) {
        if (complaintdata != null) {
            this.complaints = complaintdata.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutComplaintsAdaptersBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val complaints = complaints[position]
        holder.binding.txthello.text = complaints.ServicePlan_c
        holder.binding.txtStatus.text = complaints.Status
        holder.binding.tvCaseNumber.text =
            AppUtils2.formatDateTime4(complaints.CreatedDate.toString())
        holder.binding.txtCreationDate.text = "Case Number:- "+complaints.CaseNumber.toString()
        if(complaints.Status.equals("Open")){

            holder.binding.txtStatus.setTextColor(Color.parseColor("#D50000"))

        }else if(complaints.Status.equals("Pending")){

            holder.binding.txtStatus.setTextColor(Color.parseColor("#FB8C00"))

        }else if(complaints.Status.equals("Resolved")||complaints.Status.equals("Closed")){

            holder.binding.txtStatus.setTextColor(Color.parseColor("#2bb77a"))

        }else{

        }
        holder.itemView.setOnClickListener{
            val appCompatActivity = it.context as AppCompatActivity
            appCompatActivity.supportFragmentManager.
            beginTransaction()
                .replace(com.ab.hicareservices.R.id.container, ComplaintDetailsFragment.newInstance(AppUtils2.formatDateTime4(complaints.CreatedDate.toString()),complaints.ComplaintNo_c.toString(),complaints.OrderNo_c.toString(),complaints.ComplainCategory.toString(),complaints.SubType.toString(),complaints.ServicePlan_c.toString(),complaints.Subject.toString(),complaints.Description.toString(),holder.binding.txtStatus.text.toString(),complaints.CaseNumber.toString()))
                .addToBackStack(null)
                .commit()
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(com.ab.hicareservices.R.id.container, ComplaintDetailsFragment.newInstance()).addToBackStack("OrderDetailsFragment").commit()
        }
      
    }

    override fun getItemCount(): Int {
        return complaints.size
    }

    class MainViewHolder(val binding: LayoutComplaintsAdaptersBinding) :
        RecyclerView.ViewHolder(binding.root)
}