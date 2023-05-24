package com.ab.hicareservices.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.databinding.LayoutComplaintsAdaptersBinding
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
        holder.binding.txthello.text = complaints.Description
        holder.binding.txtStatus.text = complaints.Status
        holder.binding.tvCaseNumber.text =
            AppUtils2.formatDateTime4(complaints.CreatedDate.toString())
        holder.binding.txtCreationDate.text = complaints.CaseNumber.toString()
        if(complaints.Status.equals("Open")){

            holder.binding.txtStatus.setTextColor(Color.parseColor("#D50000"))

        }else if(complaints.Status.equals("Pending")){

            holder.binding.txtStatus.setTextColor(Color.parseColor("#FB8C00"))

        }else if(complaints.Status.equals("Resolved")||complaints.Status.equals("Closed")){

            holder.binding.txtStatus.setTextColor(Color.parseColor("#2bb77a"))

        }else{

        }
    }

    override fun getItemCount(): Int {
        return complaints.size
    }

    class MainViewHolder(val binding: LayoutComplaintsAdaptersBinding) :
        RecyclerView.ViewHolder(binding.root)
}