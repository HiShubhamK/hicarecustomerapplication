package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.databinding.LayoutComplaintsAdaptersBinding

class ComplaintsAdapter: RecyclerView.Adapter<ComplaintsAdapter.MainViewHolder>() {

    var complaints = mutableListOf<ComplaintsData>()

    @SuppressLint("NotifyDataSetChanged")
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
        try {
            val complaints = complaints[position]
            if(complaints.subject!=null){
                holder.binding.txthello.text=complaints.subject

            }else {
                holder.binding.txthello.text="NA"

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return complaints.size
    }

    class MainViewHolder(val binding: LayoutComplaintsAdaptersBinding) : RecyclerView.ViewHolder(binding.root)
}