package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.slotcomplaincemodel.Data
import com.ab.hicareservices.databinding.LayoutComplainceAdapterBinding
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.view.fragments.SlotComplinceFragment
import com.ab.hicareservices.utils.AppUtils2

class SlotCompliceAdapater : RecyclerView.Adapter<SlotCompliceAdapater.MainViewHolder>() {

    var complincelist = mutableListOf<Data>()
    private var onOrderClickedHandler: OnOrderClickedHandler? = null
    lateinit var requireActivity: FragmentActivity

    @SuppressLint("NotifyDataSetChanged")
    fun serComplainceList(complincelist: List<Data>, requireActivity: FragmentActivity) {
        if (complincelist != null) {
            this.complincelist = complincelist.toMutableList()
            this.requireActivity = requireActivity
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutComplainceAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val complincelist = complincelist[position]
        holder.binding.tvScheduleDate.text =
            AppUtils2.formatDateTime4(complincelist.ScheduledDate.toString())

        holder.binding.tvAvailablity.text = complincelist.Title
        holder.binding.tvAvailablity.paintFlags = Paint.UNDERLINE_TEXT_FLAG


        if (complincelist.Title.equals("N/A")) {
//            holder.binding.crdMain.setBackgroundColor(Color.parseColor("#D61A3C"))
            holder.binding.tvAvailablity.setTextColor(Color.parseColor("#D61A3C"))
//
        } else if (complincelist.Title.equals("Filling Fast")) {
//
//            holder.binding.crdMain.setBackgroundColor(Color.parseColor("#ED944D"));

            holder.binding.tvAvailablity.setTextColor(Color.parseColor("#ED944D"))
//
        } else if (complincelist.Title.equals("Available")) {
            holder.binding.tvAvailablity.setTextColor(Color.parseColor("#48A14D"))
//            holder.binding.crdMain.setBackgroundColor(Color.parseColor("#48A14D"));

        }
        holder.binding.crdMain.isEnabled = complincelist.IsEnabled!!
        holder.binding.crdMain.setOnClickListener {

//            requireActivity.supportFragmentManager.beginTransaction()
//                .replace(
//                    R.id.container,
//                    SlotComplinceFragment.newInstance(
//                        ServiceCenterId,
//                        getCurrentDate(),
//                        service.ParentTaskId.toString(),
//                        service.Parent_Task_Skill_Id.toString(),
//                        locationLatitudeS,
//                        locationLongitudeS,
//                        serviceType
//                    )
//                ).addToBackStack("SlotComplinceFragment").commit()
//


        }
        //        else if(complincelist.status__c.equals("Short Close")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#FB8C00"))
//
//        }else if(complincelist.status__c.equals("Cancelled")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#ff9e9e9e"))
//
//        }else if(complincelist.status__c.equals("Active")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#2bb77a"))
//
//        }else if (complincelist.status__c.equals("Rejected")){
//
//            holder.binding.txtnamestatus.setTextColor(Color.parseColor("#FFAB00"))
//
//        }else{
//
//        }
//

//        holder.binding.btnPayNow.setOnClickListener {
//            onOrderClickedHandler?.onOrderPaynowClicked(position,
//                orders.order_Number__c!!,
//                orders.account_Name__r?.customer_id__c!!
//                , orders.service_Plan_Name__c!!,orders.order_Value_with_Tax__c!!)
//        }


//        Picasso.get().load(orders.service_Plan_Image_Url).into(holder.binding.imgespest)
//        holder.itemView.setOnClickListener {
//            onOrderClickedHandler?.onOrderItemClicked(position, orders.order_Number__c.toString(), orders.service_Type.toString(),orders.service_Plan_Image_Url.toString())
//        }
    }


    override fun getItemCount(): Int {
        return complincelist.size
    }

    fun setOnOrderItemClicked(l: OnOrderClickedHandler) {
        onOrderClickedHandler = l
    }

    class MainViewHolder(val binding: LayoutComplainceAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)
}

