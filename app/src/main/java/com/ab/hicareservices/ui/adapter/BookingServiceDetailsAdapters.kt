package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.BookingServiceDetailsActivity
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.databinding.CustomBottomSheetAddBhk2AdapterBinding
import com.ab.hicareservices.ui.handler.OnBookingFlatPerPrice

class BookingServiceDetailsAdapters : RecyclerView.Adapter<BookingServiceDetailsAdapters.MainViewHolder>() {

    var service = mutableListOf<BhklistResponseData>()
    lateinit var requireActivity: BookingServiceDetailsActivity
    private var onBookingViewDetials: OnBookingFlatPerPrice? = null
//
//    fun setServiceList(
//        movies: List<ServiceListResponseData>,
//        bookingServiceDetailsActivity: BookingServiceDetailsActivity,
//
//        ) {
//        if (movies != null) {
//            this.service = movies as MutableList<BhklistResponseData>
//            this.requireActivity = bookingServiceDetailsActivity
//        }
//        notifyDataSetChanged()
//    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = CustomBottomSheetAddBhk2AdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
//        holder.binding.txtaddbhk.text=service[position].NoOfBHK.toString()
//        holder.binding.txtaddbhk.setOnClickListener {
//            onBookingViewDetials!!.onClickonFlat(position,service[position].NoOfBHK)
//        }
    }

    override fun getItemCount(): Int {
        return service.size
    }

//    fun setonViewdatail(l: OnBookingFlatPerPrice) {
//        onBookingViewDetials=l
//    }

    fun setServiceList(it: List<BhklistResponseData>, bookingServiceDetailsActivity: BookingServiceDetailsActivity) {
        this.service= it.toMutableList()
        this.requireActivity = bookingServiceDetailsActivity
        notifyDataSetChanged()
    }

    class MainViewHolder(val binding: CustomBottomSheetAddBhk2AdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}