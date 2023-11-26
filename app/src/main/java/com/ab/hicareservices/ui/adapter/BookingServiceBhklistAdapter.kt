package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.databinding.CustomBottomSheetAddBhk2AdapterBinding
import com.ab.hicareservices.ui.handler.OnBookingFlatPerPrice

class BookingServiceBhklistAdapter : RecyclerView.Adapter<BookingServiceBhklistAdapter.MainViewHolder>() {

    var service = mutableListOf<BhklistResponseData>()
    lateinit var requireActivity: FragmentActivity
    private var onBookingViewDetials: OnBookingFlatPerPrice? = null

    fun setServiceList(
        movies: List<BhklistResponseData>,
        pestServicesActivity: Context
    ) {
        if (movies != null) {
            this.service = movies.toMutableList()
            this.requireActivity = pestServicesActivity as FragmentActivity
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = CustomBottomSheetAddBhk2AdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.txtaddbhk.text=service[position].NoOfBHK.toString()
        holder.binding.txtaddbhk.setOnClickListener {
               onBookingViewDetials!!.onClickonFlat(position,service[position].NoOfBHK)
        }
    }

    override fun getItemCount(): Int {
        return service.size
    }

    fun setonViewdatail(l: OnBookingFlatPerPrice) {
        onBookingViewDetials=l
    }

    class MainViewHolder(val binding: CustomBottomSheetAddBhk2AdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}
