package com.hc.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hc.hicareservices.data.model.complaints.ComplaintsData
import com.hc.hicareservices.databinding.LayoutComplaintsAdapterBinding

class ComplaintsAdapter() : RecyclerView.Adapter<ComplaintsAdapter.MainViewHolder>() {

    var complaints = mutableListOf<ComplaintsData>()
    fun setComplaintsList(orders: List<ComplaintsData>?) {
        if (orders != null) {
            this.complaints = orders.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutComplaintsAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val complaints = complaints[position]
        holder.binding.txtstatus.text=complaints.status
    }

    override fun getItemCount(): Int {
        return complaints.size
    }

    class MainViewHolder(val binding: LayoutComplaintsAdapterBinding) : RecyclerView.ViewHolder(binding.root)
}