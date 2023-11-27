package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.databinding.CustomBottomSheetAddBhk2AdapterBinding
import com.ab.hicareservices.ui.handler.OnBookingFlatPerPrice


class BookingServiceBhklistAdapter : RecyclerView.Adapter<BookingServiceBhklistAdapter.MainViewHolder>() {

    var service = mutableListOf<BhklistResponseData>()
    lateinit var requireActivity: FragmentActivity
    private var onBookingViewDetials: OnBookingFlatPerPrice? = null
    var selectedPosition = -1
    var lastSelectedPosition = -1
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION


    fun setServiceList(
        movies: List<BhklistResponseData>,
        context: Context
    ) {
        if (movies != null) {
            this.service = movies.toMutableList()
            this.requireActivity = context as FragmentActivity
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = CustomBottomSheetAddBhk2AdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.txtaddbhk.text=service[position].NoOfBHK
//        holder.binding.txtaddbhk.setOnClickListener {
//            if (position == selectedItemPosition) {
//                holder.binding.txtaddbhk.setTextColor(Color.RED)
//            } else {
//                holder.binding.txtaddbhk.setTextColor(Color.BLACK)
//            }
//               onBookingViewDetials!!.onClickonFlat(position,service[position].NoOfBHK)
//            notifyDataSetChanged()
//        }
//
//        holder.itemView.setOnClickListener {
//            if (position == selectedPosition) {
//                holder.binding.txtaddbhk.setTextColor(Color.RED)
//                onBookingViewDetials!!.onClickonFlat(position,service[position].NoOfBHK,selectedPosition)
//                notifyDataSetChanged()
//            } else {
//                holder.binding.txtaddbhk.setTextColor(Color.RED)
//                onBookingViewDetials!!.onClickonFlat(position,service[position].NoOfBHK,selectedPosition)
//                notifyDataSetChanged()
//            }
//        }

        holder.binding.root.setOnClickListener { v ->
            lastSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)
        }
        if (selectedPosition === holder.adapterPosition) {
            holder.binding.txtaddbhk.setTextColor(ContextCompat.getColor(requireActivity, R.color.colorAccent))
            onBookingViewDetials!!.onClickonFlat(position,service[position].NoOfBHK,selectedPosition)
        } else {
            holder.binding.txtaddbhk.setTextColor(Color.GRAY)
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
