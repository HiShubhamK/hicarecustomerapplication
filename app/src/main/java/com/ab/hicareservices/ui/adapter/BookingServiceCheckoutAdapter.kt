package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.databinding.BookingCheckoutAdapterLayoutBinding
import com.ab.hicareservices.ui.handler.OnBookingViewDetials
import java.util.ArrayList

class BookingServiceCheckoutAdapter : RecyclerView.Adapter<BookingServiceCheckoutAdapter.MainViewHolder>() {

    var plandata = mutableListOf<GetServicePlanResponseData>()
    lateinit var requireActivity: FragmentActivity
    private var onBookingViewDetials: OnBookingViewDetials? = null
    lateinit var getServicePlanResponseData: ArrayList<GetServicePlanResponseData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = BookingCheckoutAdapterLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return plandata.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val plan = plandata[position]
        holder.binding.planname.text = plan.ServicePlanName
        holder.binding.servicepriceplan.text = "Start at" + " " + "\u20B9" + plan.Price.toString()
        holder.binding.txtdescription.text = plan.ServicePlanDescription
//        holder.itemView.setOnClickListener {
//            onBookingViewDetials?.onViewDetails(position, plan.Id!!)
//        }

    }

    fun setServiceList(
        it: List<GetServicePlanResponseData>?,
        bokingServiceDetailsActivity: Context
    ) {
        if (it != null) {
            this.plandata = it.toMutableList()
            this.requireActivity = bokingServiceDetailsActivity as FragmentActivity
        }
        notifyDataSetChanged()
    }

    class MainViewHolder(val binding: BookingCheckoutAdapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}