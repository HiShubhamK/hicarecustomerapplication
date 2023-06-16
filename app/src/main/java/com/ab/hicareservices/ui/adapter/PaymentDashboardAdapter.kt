package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.dashboard.CODOrders
import com.ab.hicareservices.data.model.dashboard.UpcomingService
import com.ab.hicareservices.databinding.LayoutPaymentDashboardAdapterBinding
import com.ab.hicareservices.ui.handler.onResceduleInterface
import com.ab.hicareservices.utils.AppUtils2

class PaymentDashboardAdapter() : RecyclerView.Adapter<PaymentDashboardAdapter.MainViewHolder>() {


    var upcomingservicelist = mutableListOf<UpcomingService>()
    var codOrders= mutableListOf<CODOrders>()
    var todaysservices = mutableListOf<UpcomingService>()

    private var onResceduleInterface: onResceduleInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutPaymentDashboardAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }
    fun setPaymentData(
        upcomingservicelist1: ArrayList<UpcomingService>,
        codOrders: ArrayList<CODOrders>,
        todaysServices: ArrayList<UpcomingService>
    ){
        this.upcomingservicelist=upcomingservicelist1
        this.codOrders=codOrders
        this.todaysservices=todaysServices
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {


        if (upcomingservicelist.isNotEmpty()){

            val recipe = upcomingservicelist[position]
//            holder.binding.ServiceName.text = recipe.ServiceStep_c
            holder.binding.ServiceName.text=recipe.ServicePlan_c
//            holder.binding.serviceDesc.text = recipe.ServicePlan_c
            if (recipe.AppointmentDate!=null){
                holder.binding.tvPlanDate.text = AppUtils2.formatDateTime4(recipe.AppointmentDate.toString())
            }else{
                holder.binding.tvPlanDate.text = AppUtils2.formatDateTime4(recipe.SRDate_c.toString())

            }
            holder.binding.tvPayNow.text = "Reschedule"
            holder.binding.imgAvatar.visibility = View.GONE
//        Picasso.get().load(recipe.courseImg).into( holder.binding.imgAvatar)
//        if (recipe.isButtonCancel){
//            holder.binding.imgClose.visibility=View.VISIBLE
//        }else {
//            holder.binding.imgClose.visibility = View.GONE
//        }
            holder.itemView.setOnClickListener {
                onResceduleInterface!!.onRecheduleClick(position, upcomingservicelist as ArrayList<UpcomingService>)

            }


        }else if (codOrders.isNotEmpty()){


            val recipe = upcomingservicelist[position]
            holder.binding.ServiceName.text = recipe.ServiceStep_c
//            holder.binding.serviceDesc.text = recipe.ServicePlan_c
            holder.binding.tvPayNow.text="Pay Now"

            if (recipe.AppointmentDate!=null){
                holder.binding.tvPlanDate.text = AppUtils2.formatDateTime4(recipe.AppointmentDate.toString())
            }else{
                holder.binding.tvPlanDate.text = AppUtils2.formatDateTime4(recipe.SRDate_c.toString())

            }
            holder.binding.imgAvatar.visibility = View.GONE
//        Picasso.get().load(recipe.courseImg).into( holder.binding.imgAvatar)
//        if (recipe.isButtonCancel){
//            holder.binding.imgClose.visibility=View.VISIBLE
//        }else {
//            holder.binding.imgClose.visibility = View.GONE
//        }

            holder.binding.tvPayNow.setOnClickListener {
                onResceduleInterface!!.onPaymentClick(position,codOrders as ArrayList<CODOrders>)
            }

            holder.itemView.setOnClickListener {
                onResceduleInterface!!.onPaymentitemsClick(position, codOrders as ArrayList<CODOrders>)
            }



        }else{
            val recipe = upcomingservicelist[position]
            holder.binding.ServiceName.text = recipe.ServiceStep_c
//            holder.binding.serviceDesc.text = recipe.ServicePlan_c
            if (recipe.AppointmentDate!=null){
                holder.binding.tvPlanDate.text = AppUtils2.formatDateTime4(recipe.AppointmentDate.toString())
            }else{
                holder.binding.tvPlanDate.text = AppUtils2.formatDateTime4(recipe.SRDate_c.toString())

            }
            holder.binding.tvPayNow.visibility=View.GONE
            holder.binding.lnrETA.visibility=View.VISIBLE

            holder.binding.imgAvatar.visibility = View.GONE
//        Picasso.get().load(recipe.courseImg).into( holder.binding.imgAvatar)
//        if (recipe.isButtonCancel){
//            holder.binding.imgClose.visibility=View.VISIBLE
//        }else {
//            holder.binding.imgClose.visibility = View.GONE
//        }
//            holder.itemView.setOnClickListener {
//                onResceduleInterface!!.onRecheduleClick(position, todaysservices as ArrayList<UpcomingService>)
//
//            }


        }




    }

    override fun getItemCount(): Int {
        if (upcomingservicelist.isNotEmpty()){
            return upcomingservicelist.size
        }else if (codOrders.isNotEmpty()){
            return codOrders.size
        }else{
            return todaysservices.size
        }
    }


    fun setRescudullClick(l: onResceduleInterface) {
        onResceduleInterface = l
    }

    class MainViewHolder(val binding: LayoutPaymentDashboardAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
