package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.dashboard.UpcomingService
import com.ab.hicareservices.data.model.service.ServiceData
import com.ab.hicareservices.databinding.LayoutPaymentDashboardAdapterBinding
import com.ab.hicareservices.databinding.ServiceRequestAdapterBinding
import com.ab.hicareservices.ui.handler.OnServiceRequestClickHandler
import com.ab.hicareservices.ui.handler.onResceduleInterface
import com.ab.hicareservices.utils.AppUtils2

class UpcomingServicesAdapter : RecyclerView.Adapter<UpcomingServicesAdapter.MainViewHolder>() {

    var services = mutableListOf<UpcomingService>()
    private var mOnServiceRequestClickHandler: OnServiceRequestClickHandler? = null
    private var onResceduleInterface: onResceduleInterface? = null

    fun setUpcomingService(movies: List<UpcomingService>?) {
        if (movies != null) {
            this.services = movies.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutPaymentDashboardAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val service = services[position]
            if(service.AppointmentTime.equals(" - ")){

                holder.binding.tvPlanDatetime.visibility=View.GONE
                holder.binding.tvappointmenttime.visibility=View.GONE
                holder.binding.tvScheduletime.visibility=View.GONE     //SR_Date__c
            }else{
                holder.binding.tvappointmenttime.visibility=View.VISIBLE
                holder.binding.tvScheduletime.visibility=View.VISIBLE
                holder.binding.tvPlanDatetime.text=": "+service.AppointmentTime
            }

            holder.binding.tvOrderNumber.text = ": "+service.OrderNumber_c
            holder.binding.btnetas.visibility=View.GONE
            holder.binding.ServiceName.text = service.ServicePlan_c
            holder.binding.tvServicestep.text = service.ServiceStep_c

            if(service.ServiceSequenceNumber_c==0) {
                holder.binding.tvseuenceno.text = ": " + service.Tag_c
            }else {
                holder.binding.tvseuenceno.text = ": " + service.ServiceSequenceNumber_c
            }
//
//            holder.binding.tvseuenceno.text=": " +service.ServiceSequenceNumber_c.toString()

            holder.binding.tvPayNow.text = "Reschedule"
            if (!service.AppointmentDateTime.equals(null)||!service.AppointmentDateTime.equals("")){
                holder.binding.tvPlanDate.text =": " +service.SRDate_c
            }else{
                holder.binding.tvPlanDate.text =": " +AppUtils2.formatDateTime4(service.AppointmentStartDateTime.toString())

//                holder.binding.tvPlanDate.text =": " +service.SRDate_c
            }
            if (service.HRAppointmentStartTimeAMPM_c.equals(null)||service.HRAppointmentFinishTimeAMPM_c.equals(null)){
                holder.binding.tvScheduletime.text = " - "
            }else {
                holder.binding.tvScheduletime.text = service.HRAppointmentStartTimeAMPM_c.toString() +" to "+service.HRAppointmentFinishTimeAMPM_c

            }


        }catch (e:Exception){
            e.printStackTrace()
        }

//        holder.binding.txtSequence.text = service.sequence_No__c.replace(".0","")
//        holder.binding.txtserviceno.text= service.SequenceNo_c.toString().replace(".0","")
//        holder.binding.txtStep.text = AppUtils2.formatDateTime2(service.AppointmentStartDateTime_c.toString()).substring(0,2)
//        holder.binding.txtdateinchar.text = AppUtils2.formatDateTime2(service.AppointmentStartDateTime_c.toString()).substring(3)
//        if (service.EnableRescheduleOption==true){
//            holder.binding.lnrRecheduleButton.visibility = View.VISIBLE
//        }else{
//            holder.binding.lnrRecheduleButton.visibility = View.GONE
//        }
        holder.binding.btnPayNows.setOnClickListener {
            onResceduleInterface!!.onRecheduleClick(position, services as ArrayList<UpcomingService>)
        }

//        holder.binding.btnView.setOnClickListener {
//            mOnServiceRequestClickHandler?.onViewServiceClicked(position)
//        }
    }

    override fun getItemCount(): Int {
        return services.size
    }

    fun setRescudullClick(l: onResceduleInterface) {
        onResceduleInterface = l
    }

    class MainViewHolder(val binding: LayoutPaymentDashboardAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
