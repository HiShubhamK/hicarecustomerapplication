package com.hc.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hc.hicareservices.data.model.orders.OrdersData
import com.hc.hicareservices.databinding.LayoutOrdersAdapterBinding
import com.hc.hicareservices.ui.handler.OnOrderClickedHandler
import com.hc.hicareservices.utils.AppUtils2

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
        holder.binding.txtorderid.text=" #"+orders.order_Number__c
        holder.binding.txtservicetype.text=""+orders.service_Plan_Name__c
        holder.binding.txtdate.text=" "+AppUtils2.formatDateTime3(orders.start_Date__c!!)
        holder.binding.enddate.text=AppUtils2.formatDateTime3(orders.end_Date__c!!)
        holder.binding.txtstatus.text=orders.status__c
        holder.binding.txtamounts.text = "₹ ${orders.order_Value_with_Tax__c}"
        holder.itemView.setOnClickListener {
            onOrderClickedHandler?.onOrderItemClicked(position, orders.order_Number__c.toString(), orders.service_Type.toString())
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