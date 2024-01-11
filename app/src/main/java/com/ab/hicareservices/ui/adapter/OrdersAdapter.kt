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
import kotlin.math.roundToInt

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
            holder.binding.txtname.text=orders.ServicePlanName_c
            holder.binding.txtnameorder.text=orders.OrderNumber_c
            holder.binding.txtnamestatus.text=orders.Status_c
//            var roundValue = orders.OrderValueWithTax_c?.toDouble().roundToInt()
            holder.binding.txtrupeess.text = "₹ ${orders.OrderValueWithTax_c?.toDouble()?.roundToInt()}"
            holder.binding.startdate.text=AppUtils2.formatDateTime4(orders.StartDate_c.toString())
            holder.binding.enddate.text=AppUtils2.formatDateTime4(orders.EndDate_c.toString())

//            if(orders.appointmentEndDateTime__c.equals("")){
//                holder.binding.txtappointmentdate.visibility=View.GONE
//                holder.binding.nextappointmentdate.visibility=View.GONE
//            }else{
//                holder.binding.txtappointmentdate.visibility=View.VISIBLE
//                holder.binding.nextappointmentdate.visibility=View.VISIBLE
//
//            }

            holder.binding.addressorder.text=orders.AccountName_r?.AccountAddress.toString()

            if( orders.NextServiceDate==null || orders.NextServiceDate.equals("") ){
                holder.binding.txtappointmentdate.visibility=View.GONE
                holder.binding.nextappointmentdate.visibility=View.GONE
            }else{
                holder.binding.txtappointmentdate.visibility=View.VISIBLE
                holder.binding.nextappointmentdate.visibility=View.VISIBLE
                holder.binding.txtappointmentdate.text=AppUtils2.formatDateTime4(orders.NextServiceDate.toString())
//                holder.binding.txtappointmentdate.text=orders.NextServiceDate.toString()
            }


            if(orders.Status_c.equals("Expired")){
                holder.binding.txtappointmentdate.visibility=View.GONE
                holder.binding.nextappointmentdate.visibility=View.GONE

                holder.binding.txtnamestatus.setTextColor(Color.parseColor("#D50000"))

            }else if(orders.Status_c.equals("Short Close")){

                holder.binding.txtnamestatus.setTextColor(Color.parseColor("#FB8C00"))

            }else if(orders.Status_c.equals("Cancelled")){

                holder.binding.txtnamestatus.setTextColor(Color.parseColor("#ff9e9e9e"))

            }else if(orders.Status_c.equals("Active")){

                holder.binding.txtnamestatus.setTextColor(Color.parseColor("#2bb77a"))

            }else if (orders.Status_c.equals("Rejected")){

            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#FFAB00"))

            }else{

            }

            if(orders.EnablePaymentLink==true) {
                holder.binding.addview.visibility=View.VISIBLE
                holder.binding.btnPayNow.visibility=View.VISIBLE
            }else{
                holder.binding.addview.visibility=View.GONE
                holder.binding.btnPayNow.visibility=View.GONE
            }

            holder.binding.btnPayNow.setOnClickListener {
                AppUtils2.Checkpayment=""
                AppUtils2.Checkpayment="OrderfragmentNew"
                onOrderClickedHandler?.onOrderPaynowClicked(
                    position,
                    orders.OrderNumber_c.toString(),
                    orders.AccountName_r?.CustomerId_c.toString(),
                    orders.ServicePlanName_c.toString(),
                    orders.OrderValueWithTax_c!!.toDouble(),
                    orders.ServiceType.toString(),
                    orders.StandardValue_c?.toDouble()
                )
            }

            Picasso.get().load(orders.ServicePlanImageUrl).into(holder.binding.imgespest)
            holder.itemView.setOnClickListener {
                try {
                    onOrderClickedHandler?.onOrderItemClicked(position, orders.OrderNumber_c.toString(), orders.ServiceType.toString(),orders.ServicePlanImageUrl.toString(),orders.AccountName_r?.Location_Latitude_s?.toDouble(),orders.AccountName_r?.Location_Longitude_s?.toDouble(),orders.HRShippingRegion_r?.Id.toString())
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            holder.binding.btnNotifyme.setOnClickListener {
                try {
                    onOrderClickedHandler?.onNotifyMeclick(position, orders.OrderNumber_c.toString(), orders.ServiceType.toString())
                }catch (e:Exception){
                    e.printStackTrace()
                }
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

