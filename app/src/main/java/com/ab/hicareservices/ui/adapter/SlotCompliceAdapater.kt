package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.slotcomplaincemodel.Data
import com.ab.hicareservices.databinding.LayoutComplainceAdapterBinding
import com.ab.hicareservices.databinding.LayoutOrdersAdapterBinding
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso

class SlotCompliceAdapater : RecyclerView.Adapter<SlotCompliceAdapater.MainViewHolder>() {

    var complincelist = mutableListOf<Data>()
    private var onOrderClickedHandler: OnOrderClickedHandler? = null
    lateinit var requireActivity:FragmentActivity

    @SuppressLint("NotifyDataSetChanged")
    fun serComplainceList(complincelist: List<Data>, requireActivity: FragmentActivity) {
        if (complincelist != null) {
            this.complincelist = complincelist.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutComplainceAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val complincelist = complincelist[position]
        holder.binding.tvNoOfTechnician.text="NoOfAvailableTechnician:- "+complincelist.NoOfAvailableTechnician.toString()
        holder.binding.txtnameorder.text="ScheduledDate:- "+complincelist.ScheduledDateText
        holder.binding.txtFillingSlotStatus.text="AvailableCompliance:- "+complincelist.AvailableCompliance.toString()
        holder.binding.txtappointmentdate.text=AppUtils2.formatDateTime4(complincelist.ScheduledDate.toString())
        holder.binding.txtrupees.visibility= View.INVISIBLE


//        if(complincelist.status__c.equals("Expired")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#D50000"))
//
//        }else if(complincelist.status__c.equals("Short Close")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#FB8C00"))
//
//        }else if(complincelist.status__c.equals("Cancelled")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#ff9e9e9e"))
//
//        }else if(complincelist.status__c.equals("Active")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#2bb77a"))
//
//        }else if (complincelist.status__c.equals("Rejected")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#FFAB00"))
//
//        }else{
//
//        }
//

//        holder.binding.btnPayNow.setOnClickListener {
//            onOrderClickedHandler?.onOrderPaynowClicked(position,
//                orders.order_Number__c!!,
//                orders.account_Name__r?.customer_id__c!!
//                , orders.service_Plan_Name__c!!,orders.order_Value_with_Tax__c!!)
//        }


//        Picasso.get().load(orders.service_Plan_Image_Url).into(holder.binding.imgespest)
//        holder.itemView.setOnClickListener {
//            onOrderClickedHandler?.onOrderItemClicked(position, orders.order_Number__c.toString(), orders.service_Type.toString(),orders.service_Plan_Image_Url.toString())
//        }
    }


    override fun getItemCount(): Int {
        return complincelist.size
    }
    fun setOnOrderItemClicked(l: OnOrderClickedHandler) {
        onOrderClickedHandler = l
    }

    class MainViewHolder(val binding: LayoutComplainceAdapterBinding) : RecyclerView.ViewHolder(binding.root)
}

