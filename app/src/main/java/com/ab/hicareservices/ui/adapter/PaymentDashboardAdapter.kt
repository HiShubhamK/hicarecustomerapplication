package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.dashboard.OfferData
import com.ab.hicareservices.data.model.dashboard.UpcomingService
import com.ab.hicareservices.databinding.LayoutPaymentDashboardAdapterBinding
import com.ab.hicareservices.ui.handler.offerinterface
import com.ab.hicareservices.ui.handler.onResceduleInterface
import com.ab.hicareservices.ui.viewmodel.PaymentCardViewModel
import com.ab.hicareservices.utils.AppUtils
import com.ab.hicareservices.utils.AppUtils2

class PaymentDashboardAdapter() : RecyclerView.Adapter<PaymentDashboardAdapter.MainViewHolder>() {


    var upcomingservicelist = mutableListOf<UpcomingService>()
    private var onResceduleInterface: onResceduleInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutPaymentDashboardAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }
    fun setPaymentData(upcomingservicelist1: ArrayList<UpcomingService>){
        this.upcomingservicelist=upcomingservicelist1
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val recipe = upcomingservicelist[position]
        holder.binding.ServiceName.text = recipe.ServiceStep_c
        holder.binding.serviceDesc.text = recipe.ServicePlan_c
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
    }

    override fun getItemCount(): Int {
        return upcomingservicelist.size
    }


    fun setRescudullClick(l: onResceduleInterface) {
        onResceduleInterface = l
    }

    class MainViewHolder(val binding: LayoutPaymentDashboardAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
