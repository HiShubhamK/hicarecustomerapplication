package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.slots.TimeSlot
import com.ab.hicareservices.databinding.SlotsAdapterBinding
import com.ab.hicareservices.ui.handler.OnListItemClickHandler
import com.ab.hicareservices.ui.handler.onSlotSelection

class BookingSlotsAdapter(activity: FragmentActivity, slotData: ArrayList<com.ab.hicareservices.data.model.getslots.TimeSlot>, TaskId: String) : RecyclerView.Adapter<BookingSlotsAdapter.ViewHolder>() {
    private val onItemClickHandler: OnListItemClickHandler? = null


    val getSlotresponse = slotData
    private var onSlotSelection: onSlotSelection? = null

    //        private List<SlotResponse> items = null;
    private val context: Activity
    private var lastSelectedPosition = -1
    val taskid=TaskId
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SlotsAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val slotlist=getSlotresponse

        holder.binding.txtSlots.text = slotlist[position].StartTime+" to "+slotlist[position].FinishTime
        holder.binding.radioSlots.isChecked = position == lastSelectedPosition
        ;
//        if (holder.binding.radioSlots.isChecked) {
//            holder.binding.radioSlots.setBackgroundResource(R.drawable.selected_radio_button_bg)
//        }else{
//
//        }

//        if (holder.binding.radioSlots.isChecked){
//            onSlotSelection?.onSlotBookSelect(position, taskid,slotlist[position].Start.toString(),slotlist[position].StartTime.toString(),slotlist[position].FinishTime.toString(),"", "Pest" ,slotlist[position].Finish.toString())
//        }


        if (position == lastSelectedPosition) {
            holder.binding.radioSlots.isChecked = true
            holder.binding.radioSlots.setBackgroundResource(R.drawable.selected_radio_button_bg)
        } else {
            holder.binding.radioSlots.isChecked=false
            holder.binding.radioSlots.setBackgroundResource(R.drawable.unselected_radio_button_bg)

        }
//        holder.binding.radioSlots.isChecked = position == lastSelectedPosition

        holder.binding.relSlot.setOnClickListener {
            lastSelectedPosition = holder.adapterPosition
            notifyDataSetChanged()

            onSlotSelection?.onSlotBookSelect(holder.adapterPosition, taskid,slotlist[position].Start.toString(),slotlist[position].StartTime.toString(),slotlist[position].FinishTime.toString(),"", "Pest",slotlist[position].Finish.toString() )

        }
        holder.binding.radioSlots.setOnClickListener {
            lastSelectedPosition = holder.adapterPosition
            notifyDataSetChanged()
            onSlotSelection?.onSlotBookSelect(holder.adapterPosition, taskid,slotlist[position].Start.toString(),slotlist[position].StartTime.toString(),slotlist[position].FinishTime.toString(),"Mobile App", "Pest",slotlist[position].Finish.toString() )

        }
    }

    override fun getItemCount(): Int {
        return getSlotresponse.size
    }
    fun setOnSlotSelection(l: onSlotSelection) {
        onSlotSelection = l
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