package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.databinding.LayoutComplaintsAdaptersBinding

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
        holder.binding.txthello.text=complaints.Subject
    }


    override fun getItemCount(): Int {
        return complaints.size
    }

    class MainViewHolder(val binding: LayoutComplaintsAdaptersBinding) : RecyclerView.ViewHolder(binding.root)
}