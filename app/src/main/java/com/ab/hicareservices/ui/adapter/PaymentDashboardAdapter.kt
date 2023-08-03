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


            holder.binding.btnetas.visibility=View.GONE
            holder.binding.btnPayNows.visibility=View.VISIBLE


            val recipe=upcomingservicelist[position]
            holder.binding.ServiceName.text = recipe.ServicePlan_c
            holder.binding.tvServicestep.text=recipe.ServiceStep_c
            holder.binding.tvOrderNumber.text=":  "+recipe.OrderNumber_c


            if(recipe.AppointmentTime.equals(" - ")){
                holder.binding.tvappointmenttime.visibility=View.GONE
                holder.binding.tvScheduletime.visibility=View.GONE
                holder.binding.tvPlanDatetime.visibility=View.GONE
            }else{
                holder.binding.tvappointmenttime.visibility=View.VISIBLE
                holder.binding.tvScheduletime.visibility=View.VISIBLE
                holder.binding.tvPlanDatetime.visibility=View.VISIBLE
                holder.binding.tvPlanDatetime.text=":  "+recipe.AppointmentTime
            }

//            if (!recipe.AppointmentDateTime.equals(null)||!recipe.AppointmentDateTime.equals("")){
//                holder.binding.tvPlanDate.text =": " +recipe.AppointmentStartDate
//            }else{
//                holder.binding.tvPlanDate.text =": " +recipe.SRPlanDate
//            }
//

            if (!recipe.AppointmentDateTime.equals(null)||!recipe.AppointmentDateTime.equals("")){
                holder.binding.tvappointmentdate.text="Appointment"
                holder.binding.tvPlanDate.text =":  "+ recipe.AppointmentStartDate

            }else if(recipe.SRDate_c!=null){
                holder.binding.tvPlanDate.text =":  "+ AppUtils2.formatDateTime4(recipe.SRPlanDate.toString())
            }else{
//                holder.binding.lnrtime.visibility=View.GONE
//                holder.binding.lnrdate.visibility=View.GONE
            }

            holder.binding.tvPayNows.visibility=View.VISIBLE
            holder.binding.lnrETA.visibility=View.GONE

            holder.binding.imgAvatar.visibility = View.GONE

            holder.binding.tvPayNows.text = "Reschedule"
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

            holder.binding.btnetas.visibility=View.GONE
            holder.binding.btnPayNows.visibility=View.VISIBLE

            val recipe = codOrders[position]
            holder.binding.tvappointmenttime.visibility=View.VISIBLE
            holder.binding.tvScheduletime.visibility=View.GONE
            holder.binding.tvPlanDatetime.visibility=View.VISIBLE
            holder.binding.tvappointmentdate.visibility=View.GONE
            holder.binding.tvPlanDate.visibility=View.GONE

            holder.binding.tvappointmenttime.text="Amount to be paid"

            holder.binding.tvPlanDatetime.text=":"+"₹  "+recipe.OrderValueWithTax_c
            holder.binding.ServiceName.text = recipe.ServicePlanName_c
//            holder.binding.serviceDesc.text = recipe.ServicePlan_c
            holder.binding.tvPayNows.visibility=View.VISIBLE
            holder.binding.tvPayNows.text="Pay Now"
//            holder.binding.lnrorderno.visibility=View.VISIBLE
            holder.binding.tvOrderNumber.text=": "+recipe.OrderNumber_c
//            holder.binding.lnrtime.visibility=View.GONE
//            if (recipe.AppointmentStartDateTime_c!=null){
//                holder.binding.tvappointmentdate.text="Appointment Date"
//                holder.binding.tvPlanDate.text =": "+ AppUtils2.formatDateTime4(recipe.AppointmentEndDateTime_c.toString())
//            }else{
//                holder.binding.tvPlanDate.text =": "+ AppUtils2.formatDateTime4(recipe.CreatedDate.toString())
//
//            }
            holder.binding.imgAvatar.visibility = View.GONE

//            holder.binding.tvappointmenttime.visibility=View.GONE
//            holder.binding.tvScheduletime.visibility=View.GONE
//            holder.binding.tvPlanDatetime.visibility=View.GONE

//        Picasso.get().load(recipe.courseImg).into( holder.binding.imgAvatar)
//        if (recipe.isButtonCancel){
//            holder.binding.imgClose.visibility=View.VISIBLE
//        }else {
//            holder.binding.imgClose.visibility = View.GONE
//        }

            holder.binding.tvPayNows.setOnClickListener {
                onResceduleInterface!!.onPaymentClick(position,codOrders as ArrayList<CODOrders>)
            }

//            holder.itemView.setOnClickListener {
//                onResceduleInterface!!.onPaymentitemsClick(position, codOrders as ArrayList<CODOrders>)
//            }



        }else{
            val recipe = todaysservices[position]
            holder.binding.btnetas.visibility=View.VISIBLE
            holder.binding.btnPayNows.visibility=View.GONE
            holder.binding.ServiceName.text = recipe.ServicePlan_c
            holder.binding.tvServicestep.text=": "+recipe.ServiceStep_c
            holder.binding.tvOrderNumber.text=": "+recipe.OrderNumber_c

//            holder.binding.tveta.text=recipe.HRAssignmentStartTimeAMPM_c+"-"+recipe.HRAssignmentFinishTimeAMPM_c

            if(recipe.AppointmentTime.equals(" - ")){
                holder.binding.tvappointmenttime.visibility=View.GONE
                holder.binding.tvScheduletime.visibility=View.GONE
                holder.binding.tvPlanDatetime.visibility=View.GONE
            }else{
                holder.binding.tvappointmenttime.visibility=View.VISIBLE
                holder.binding.tvScheduletime.visibility=View.VISIBLE
                holder.binding.tvPlanDatetime.visibility=View.VISIBLE
                holder.binding.tvPlanDatetime.text=": "+recipe.AppointmentTime
            }

            holder.itemView.setOnClickListener {
                onResceduleInterface!!.onToadaysClick(position,recipe.Onsiteotp,recipe.CustomerOTP_c,
                    recipe.HRAssignedResource_r?.Name, recipe.HRAssignedResource_r?.TechnicianMobile_c,recipe.Id.toString())

            }


            if (recipe.AppointmentDate!=null){
                holder.binding.tvappointmentdate.text="Appointment"
                holder.binding.tvPlanDate.text =":  "+AppUtils2.formatDateTime4(recipe.AppointmentDate.toString())
            }else if(recipe.SRDate_c!=null){
                holder.binding.tvPlanDate.text =":  "+AppUtils2.formatDateTime4(recipe.SRDate_c.toString())
            }else{
//                holder.binding.lnrtime.visibility=View.GONE
//                holder.binding.lnrdate.visibility=View.GONE
            }

            if(recipe.HRAssignmentStartTimeAMPM_c!!.isNotEmpty() && recipe.HRAssignmentFinishTimeAMPM_c!!.isNotEmpty() ){
//                holder.binding.lnrtime.visibility=View.GONE
                holder.binding.lnrETA.visibility=View.VISIBLE
                holder.binding.txtetas.text=recipe.HRAssignmentStartTimeAMPM_c+"-"+recipe.HRAssignmentFinishTimeAMPM_c

            }else{
                holder.binding.lnrETA.visibility=View.GONE
//                holder.binding.lnrtime.visibility=View.VISIBLE
                holder.binding.tvScheduletime.text=recipe.Appointmentstarttime+"-"+recipe.AppointmentFinishtime
            }

            holder.binding.tvPayNows.visibility=View.GONE
            holder.binding.lnrETA.visibility=View.VISIBLE
            holder.binding.imgAvatar.visibility = View.GONE

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
