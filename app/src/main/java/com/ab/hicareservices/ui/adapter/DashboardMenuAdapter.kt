package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.databinding.DashboardMenuAdapterBinding
import com.ab.hicareservices.ui.viewmodel.GridViewModal
import com.squareup.picasso.Picasso

class DashboardMenuAdapter(courseList: List<GridViewModal>) : RecyclerView.Adapter<DashboardMenuAdapter.MainViewHolder>() {

    var service = courseList
//    private var mOnServiceRequestClickHandler: OnServiceRequestClickHandler? = null

//    fun setServiceList(movies: List<GridViewModal>?) {
//        if (movies != null) {
//            this.service = movies.toMutableList()
//        }
//        notifyDataSetChanged()
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DashboardMenuAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val service = service[position]
        holder.binding.tvOrderName.text = service.courseName
        Picasso.get().load(service.courseImg).into( holder.binding.imgLogo);

//        holder.binding.txtSequence.text = service.sequence_No__c.replace(".0","")
//        holder.binding.imgLogo.text= service.sequence_No__c.replace(".0","")
//        holder.binding.txtStep.text = AppUtils2.formatDateTime2(service.appointment_Start_Date_Time__c).substring(0,2)
//        holder.binding.txtdateinchar.text = AppUtils2.formatDateTime2(service.appointment_Start_Date_Time__c).substring(3)
//        if (service.enable_Reschedule_Option){
//            holder.binding.btnReschedule.visibility = View.VISIBLE
//        }else{
//            holder.binding.btnReschedule.visibility = View.GONE
//        }
//        holder.binding.btnReschedule.setOnClickListener {
//            mOnServiceRequestClickHandler?.onRescheduleServiceClicked(position)
//        }
//
//        holder.binding.btnView.setOnClickListener {
//            mOnServiceRequestClickHandler?.onViewServiceClicked(position)
//        }
    }

    override fun getItemCount(): Int {
        return service.size
    }

//    fun setOnServiceItemClicked(l: OnServiceRequestClickHandler) {
//        mOnServiceRequestClickHandler = l
//    }

    class MainViewHolder(val binding: DashboardMenuAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}