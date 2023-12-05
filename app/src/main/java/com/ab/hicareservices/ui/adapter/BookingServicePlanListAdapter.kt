package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.databinding.BookingPlanServicesAdapterBinding
import com.ab.hicareservices.ui.handler.OnBookingViewDetials
import com.ab.hicareservices.utils.AppUtils2

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
        AppUtils2.ServicePlanName=plan.ServicePlanName.toString()
        holder.binding.pricewisebhk.text = "\u20B9" + plan.Price.toString()
        holder.binding.servicepriceplan.text = "\u20B9" + plan.DiscountedPrice.toString()
        holder.binding.pricewisebhk.paintFlags = holder.binding.pricewisebhk.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        holder.binding.txtdescription.text = plan.ServicePlanDescription
        holder.itemView.setOnClickListener {
            onBookingViewDetials?.onViewDetails(position, plan.Id!!, plan.ServicePlanDescription,plan.Price,plan.DiscountedPrice)
        }

        holder.binding.btnaddtocart.setOnClickListener {

            AppUtils2.planid=plan.PlanId.toString()
            AppUtils2.spcode=plan.SPCode.toString()

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
                    plan.ServiceInstructions,
                    plan.ServiceLogo,
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
                plan.PlanId,
                plan.PincodeId.toString(),
                plan.NoOfBHK,
                getServicePlanResponseData,
                plan.Price,
                plan.DiscountedAmount,
                plan.DiscountedPrice,
                plan.ServicePlanName
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