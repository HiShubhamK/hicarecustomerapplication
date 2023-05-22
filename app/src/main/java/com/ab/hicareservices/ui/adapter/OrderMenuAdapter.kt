package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.databinding.LayoutOrderMenuListBinding

class OrderMenuAdapter : RecyclerView.Adapter<OrderMenuAdapter.MainViewHolder>() {

    var orders = mutableListOf<OrdersData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding=LayoutOrderMenuListBinding.inflate(inflater,parent,false)
        return MainViewHolder(binding)
//
//        val binding = LayoutOrderMenuListBinding.inflate(inflater, parent, false)
//        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val orders=orders[position]
        holder.binding.ordersmenu.text=orders.status__c
    }


    override fun getItemCount(): Int {
       return orders.size
    }

    fun setOrdersList(orders: List<OrdersData>?) {

        if (orders != null) {
            this.orders = orders.toMutableList()
        }
        notifyDataSetChanged()
    }

    class MainViewHolder(val binding: LayoutOrderMenuListBinding) : RecyclerView.ViewHolder(binding.root)



}