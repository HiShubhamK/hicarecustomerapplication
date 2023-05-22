package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.ServiceRequestAdapterBinding
import com.ab.hicareservices.ui.handler.OnServiceRequestClickHandler
import com.ab.hicareservices.utils.AppUtils2

class ServiceRequestAdapter : RecyclerView.Adapter<ServiceRequestAdapter.MainViewHolder>() {

    var service = mutableListOf<ServiceData>()
    private var mOnServiceRequestClickHandler: OnServiceRequestClickHandler? = null

    fun setServiceList(movies: List<ServiceData>?) {
        if (movies != null) {
            this.service = movies.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ServiceRequestAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val service = service[position]
        holder.binding.txtStatus.text = service.status__c
//        holder.binding.txtSequence.text = service.sequence_No__c.replace(".0","")
        holder.binding.txtserviceno.text= service.sequence_No__c.replace(".0","")
        holder.binding.txtStep.text = AppUtils2.formatDateTime2(service.appointment_Start_Date_Time__c).substring(0,2)
        holder.binding.txtdateinchar.text = AppUtils2.formatDateTime2(service.appointment_Start_Date_Time__c).substring(3)
        if (service.enable_Reschedule_Option){
            holder.binding.btnReschedule.visibility = View.VISIBLE
        }else{
            holder.binding.btnReschedule.visibility = View.GONE
        }
        holder.binding.btnReschedule.setOnClickListener {
            mOnServiceRequestClickHandler?.onRescheduleServiceClicked(position)
        }

        holder.binding.btnView.setOnClickListener {
            mOnServiceRequestClickHandler?.onViewServiceClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return service.size
    }

    fun setOnServiceItemClicked(l: OnServiceRequestClickHandler) {
        mOnServiceRequestClickHandler = l
    }

    class MainViewHolder(val binding: ServiceRequestAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
