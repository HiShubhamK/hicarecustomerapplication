package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.databinding.BookingPlanServicesAdapterBinding
import com.ab.hicareservices.databinding.BookingServiceAdapterBinding
import com.ab.hicareservices.ui.handler.OnBookingViewDetials
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.view.activities.BokingServiceDetailsActivity
import com.ab.hicareservices.ui.view.activities.CustomBottomSheetFragment

class BookingServicePlanListAdapter: RecyclerView.Adapter<BookingServicePlanListAdapter.MainViewHolder>() {

    var plandata = mutableListOf<GetServicePlanResponseData>()
    lateinit var requireActivity: FragmentActivity
    private var onBookingViewDetials: OnBookingViewDetials? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = BookingPlanServicesAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun getItemCount(): Int {
       return plandata.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val plan = plandata[position]
        holder.binding.planname.text=plan.ServicePlanName
        holder.binding.servicepriceplan.text="Start at"+" "+"\u20B9"+plan.Price.toString()
        holder.binding.txtdescription.text=plan.ServicePlanDescription
        holder.itemView.setOnClickListener {
            onBookingViewDetials?.onViewDetails(position, plan.Id!!)
        }
    }

    fun setServiceList(
        it: List<GetServicePlanResponseData>?,
        bokingServiceDetailsActivity: BokingServiceDetailsActivity
    ) {
        if (it != null) {
            this.plandata = it.toMutableList()
            this.requireActivity = bokingServiceDetailsActivity
        }
        notifyDataSetChanged()
    }

    fun setonViewdatail(l: OnBookingViewDetials) {
        onBookingViewDetials=l
    }

    class MainViewHolder(val binding: BookingPlanServicesAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}