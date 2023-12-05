package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.databinding.BookingCheckoutAdapterLayoutBinding
import com.ab.hicareservices.ui.handler.OnBookingViewDetials
import com.squareup.picasso.Picasso
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
        holder.binding.servicepriceplan.text = "" + "" + "\u20B9"+ plan.Price.toString()
        holder.binding.txtdescription.text = Html.fromHtml(plan.ServiceInstructions)
        Picasso.get().load(plan.ServiceLogo).into(holder.binding.imglogo)

        holder.binding.imgaddinstructoin.setOnClickListener {
                    holder.binding.imgaddinstructoin.text="-"
                    holder.binding.edtinstruction.visibility=View.VISIBLE
            SharedPreferenceUtil.setData(requireActivity,"Instructions",holder.binding.edtinstruction.text.toString())
        }

        holder.binding.imgremoveinstructoin.setOnClickListener {
            holder.binding.imgaddinstructoin.text="+"
            holder.binding.edtinstruction.visibility=View.GONE
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

    class MainViewHolder(val binding: BookingCheckoutAdapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}