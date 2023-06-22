package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.databinding.LayoutOrdersAdapterBinding
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.MainViewHolder>() {

    var orders = mutableListOf<OrdersData>()
    private var onOrderClickedHandler: OnOrderClickedHandler? = null
    lateinit var requireActivity: FragmentActivity

    @SuppressLint("NotifyDataSetChanged")
    fun setOrdersList(orders: List<OrdersData>?, requireActivity: FragmentActivity) {
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

//    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        val orders = orders[position]
//        holder.binding.txtorderid.text=""+orders.order_Number__c
//        holder.binding.txtservicetype.text=""+orders.service_Plan_Name__c
//        holder.binding.txtdate.text=" "+AppUtils2.formatDateTime4(orders.start_Date__c!!)
//        holder.binding.enddate.text=AppUtils2.formatDateTime4(orders.end_Date__c!!)
//        holder.binding.txtstatus.text=orders.status__c
//        holder.binding.txtamounts.text = "₹ ${orders.order_Value_with_Tax__c}"
//        holder.itemView.setOnClickListener {
//            onOrderClickedHandler?.onOrderItemClicked(position, orders.order_Number__c.toString(), orders.service_Type.toString(),orders.service_Plan_Image_Url.toString())
//        }
//    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val orders = orders[position]
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

            if(orders.enable_Payment_Link==true) {
                holder.binding.addview.visibility=View.VISIBLE
                holder.binding.btnPayNow.visibility=View.VISIBLE
            }else{
                holder.binding.addview.visibility=View.GONE
                holder.binding.btnPayNow.visibility=View.GONE
            }

            holder.binding.btnPayNow.setOnClickListener {
                onOrderClickedHandler?.onOrderPaynowClicked(
                    position,
                    orders.order_Number__c!!,
                    orders.account_Name__r?.customer_id__c!!,
                    orders.service_Plan_Name__c!!,
                    orders.order_Value_with_Tax__c!!,
                    orders.service_Type!!
                )
            }

            Picasso.get().load(orders.service_Plan_Image_Url).into(holder.binding.imgespest)
            holder.itemView.setOnClickListener {
//                try {
                    onOrderClickedHandler?.onOrderItemClicked(position, orders.order_Number__c.toString(), orders.service_Type.toString(),orders.service_Plan_Image_Url.toString(),orders.account_Name__r!!.location__Latitude__s,orders.account_Name__r!!.location__Longitude__s,orders.hR_Shipping_Region__r!!.id.toString())
//                }catch (e:Exception){
//                    e.printStackTrace()
//                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }


    override fun getItemCount(): Int {
        return orders.size
    }
    fun setOnOrderItemClicked(l: OnOrderClickedHandler) {
        onOrderClickedHandler = l
    }

    class MainViewHolder(val binding: LayoutOrdersAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)
}

