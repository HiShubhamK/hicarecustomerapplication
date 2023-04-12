package com.hc.hicareservices.ui.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hc.hicareservices.ui.handler.OnListItemClickHandler
import android.app.Activity
import android.view.ViewGroup
import android.view.LayoutInflater
import com.hc.hicareservices.R
import com.hc.hicareservices.data.model.slots.TimeSlot
import com.hc.hicareservices.databinding.SlotsAdapterBinding

class SlotsAdapter(activity: FragmentActivity) : RecyclerView.Adapter<SlotsAdapter.ViewHolder>() {
    private val onItemClickHandler: OnListItemClickHandler? = null

    //    private List<SlotResponse> items = null;
    private val context: Activity
    private var lastSelectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SlotsAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        String slots = items.get(position).getStartTime() + " to " + items.get(position).getFinishTime();
//        holder.mSlotsAdapterBinding.txtSlots.setText(slots);
        if (position == lastSelectedPosition) {
            holder.binding.radioSlots.isChecked = true
            holder.binding.relSlot.setBackgroundResource(R.drawable.bg_slot)
        } else {
            holder.binding.radioSlots.isChecked = true
            holder.binding.relSlot.setBackgroundResource(R.drawable.bg_greyslot)
        }
        holder.binding.radioSlots.isChecked = position == lastSelectedPosition

        holder.binding.relSlot.setOnClickListener {
            lastSelectedPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
        holder.binding.radioSlots.setOnClickListener {
            lastSelectedPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    fun setData(data: List<TimeSlot?>?) {
//        items.clear();
//        for (int index = 0; index < data.size(); index++) {
//            SlotsViewModel slotsViewModel = new SlotsViewModel();
//            slotsViewModel.clone(data.get(index));
//            items.add(slotsViewModel);
//        }
    }

    fun addData(data: List<TimeSlot?>?) {
//        for (int index = 0; index < data.size(); index++) {
//            SlotsViewModel slotsViewModel = new SlotsViewModel();
//            slotsViewModel.clone(data.get(index));
//            items.add(slotsViewModel);
//        }
    }

    class ViewHolder(val binding: SlotsAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems() {}
    }

    init {
//        if (items == null) {
//            items = new ArrayList<>();
//        }
        context = activity
    }
}