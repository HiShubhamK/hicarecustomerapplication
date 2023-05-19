package com.hc.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hc.hicareservices.data.model.orders.OrdersData
import com.hc.hicareservices.databinding.LayoutOrdersAdapterBinding
import com.hc.hicareservices.ui.handler.OnOrderClickedHandler
import com.hc.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.MainViewHolder>() {

    var orders = mutableListOf<OrdersData>()
    private var onOrderClickedHandler: OnOrderClickedHandler? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setOrdersList(orders: List<OrdersData>?) {
        if (orders != null) {
            this.orders = orders.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutOrdersAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val orders = orders[position]
//        holder.binding.txtorderid.text=""+orders.order_Number__c
//        holder.binding.txtservicetype.text=""+orders.service_Plan_Name__c
//        holder.binding.txtdate.text=" "+AppUtils2.formatDateTime4(orders.start_Date__c!!)
//        holder.binding.enddate.text=AppUtils2.formatDateTime4(orders.end_Date__c!!)
//        holder.binding.txtstatus.text=orders.status__c
//        holder.binding.txtamounts.text = "₹ ${orders.order_Value_with_Tax__c}"

        holder.binding.txtname.text=orders.service_Plan_Name__c
        holder.binding.txtnameorder.text=orders.order_Number__c
        holder.binding.txtnamestatus.text=orders.status__c
        holder.binding.txtappointmentdate.text=AppUtils2.formatDateTime4(orders.appointmentEndDateTime__c.toString())
        holder.binding.txtrupees.text = "₹ ${orders.order_Value_with_Tax__c}"

        if(orders.status__c.equals("Expired")){

            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#D50000"))

        }else if(orders.status__c.equals("Short Close")){

            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#FB8C00"))

        }else if(orders.status__c.equals("Cancelled")){

            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#ff9e9e9e"))

        }else if(orders.status__c.equals("Active")){

            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#2bb77a"))

        }else if (orders.status__c.equals("Rejected")){

            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#FFAB00"))

        }else{



        }



        Picasso.get().load(orders.service_Plan_Image_Url).into(holder.binding.imgespest)
        holder.itemView.setOnClickListener {
            onOrderClickedHandler?.onOrderItemClicked(position, orders.order_Number__c.toString(), orders.service_Type.toString(),orders.service_Plan_Image_Url.toString())
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }
    fun setOnOrderItemClicked(l: OnOrderClickedHandler) {
        onOrderClickedHandler = l
    }

    class MainViewHolder(val binding: LayoutOrdersAdapterBinding) : RecyclerView.ViewHolder(binding.root)
}