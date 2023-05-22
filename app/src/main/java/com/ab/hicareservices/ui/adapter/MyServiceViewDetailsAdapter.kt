package com.ab.hicareservices.ui.adapter

import com.ab.hicareservices.data.model.service.ServiceTaskData
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.ab.hicareservices.databinding.ViewServiceDetailAdapterBinding

class MyServiceViewDetailsAdapter(tasksList: List<ServiceTaskData>?) : RecyclerView.Adapter<MyServiceViewDetailsAdapter.ViewHolder>() {
    private var items: List<ServiceTaskData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mServiceAdapterBinding = ViewServiceDetailAdapterBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(mServiceAdapterBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.mViewServiceDetailAdapterBinding.txtDateTime.setText(items.get(position).getAppointmentDateTime());
//        holder.mViewServiceDetailAdapterBinding.txtOrder.setText(items.get(position).getOrderNumber__c());
//        holder.mViewServiceDetailAdapterBinding.txtReason.setText(items.get(position).getIncomplete_reason__c());
//        holder.mViewServiceDetailAdapterBinding.txtSequence.setText(String.valueOf(items.get(position).getService_Sequence_Number__c()));
//        holder.mViewServiceDetailAdapterBinding.txtStatus.setText(items.get(position).getTechnician_Status());
    }

    override fun getItemCount(): Int {
        return 3
    }

    class ViewHolder(private val binding: ViewServiceDetailAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    init {
        items = tasksList
    }
}