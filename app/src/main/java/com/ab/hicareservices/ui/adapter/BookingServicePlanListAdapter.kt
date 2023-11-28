package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.databinding.BookingPlanServicesAdapterBinding
import com.ab.hicareservices.ui.handler.OnBookingViewDetials

class BookingServicePlanListAdapter :
    RecyclerView.Adapter<BookingServicePlanListAdapter.MainViewHolder>() {

    var plandata = mutableListOf<GetServicePlanResponseData>()
    lateinit var requireActivity: FragmentActivity
    private var onBookingViewDetials: OnBookingViewDetials? = null
    lateinit var getServicePlanResponseData: ArrayList<GetServicePlanResponseData>

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
        holder.binding.planname.text = plan.ServicePlanName
        holder.binding.servicepriceplan.text = "Start at" + " " + "\u20B9" + plan.Price.toString()
        holder.binding.txtdescription.text = plan.ServicePlanDescription
        holder.itemView.setOnClickListener {
            onBookingViewDetials?.onViewDetails(position, plan.Id!!, plan.ServicePlanDescription,plan.Price,plan.DiscountedPrice)
        }

//        holder.itemView.setOnClickListener {
//            val intent= Intent(requireActivity,BookingServiceDetailsActivity::class.java)
//            requireActivity.startActivity(intent)
//        }

        holder.binding.btnaddtocart.setOnClickListener {

            getServicePlanResponseData = ArrayList()

            getServicePlanResponseData.clear()

            getServicePlanResponseData.add(
                GetServicePlanResponseData(
                    plan.Id,
                    plan.PincodeId,
                    plan.Pincode,
                    plan.BHKId,
                    plan.NoOfBHK,
                    plan.PlanId,
                    plan.ServicePlanName,
                    plan.ServicePlanDescription,
                    plan.SPCode,
                    plan.Price,
                    plan.DiscountedAmount,
                    plan.DiscountedPrice,
                    plan.DiscountType,
                    plan.Discount,
                    plan.DiscountStartDate,
                    plan.DiscountEndDate,
                    plan.Ratings,
                    plan.IsActive,
                    plan.CreatedBy,
                    plan.CreatedOn,
                    plan.Serviceid,
                    plan.ModifiedBy,
                    plan.ModifiedOn

            )
            )

            onBookingViewDetials?.onClickAddButton(
                position,
                plan.Id,
                plan.PincodeId.toString(),
                plan.NoOfBHK,
                getServicePlanResponseData,
                plan.Price,
                plan.DiscountedAmount,
                plan.DiscountedPrice
                )

        }
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

    fun setonViewdatail(l: OnBookingViewDetials) {
        onBookingViewDetials = l
    }

    class MainViewHolder(val binding: BookingPlanServicesAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}