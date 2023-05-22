package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import com.ab.hicareservices.data.model.weeks.WeekModel
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.ui.handler.OnListItemClickHandler
import com.ab.hicareservices.ui.handler.OnRescheduleClickHandler
import android.view.ViewGroup
import android.view.LayoutInflater
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.RescheduleAdapterBinding
import org.joda.time.DateTime
import java.util.ArrayList

class WeeksAdapter(weekList: ArrayList<WeekModel>?, today: DateTime?, txtMonth: TextView) :
    RecyclerView.Adapter<WeeksAdapter.ViewHolder>() {
    private var onItemClickHandler: OnListItemClickHandler? = null
    private var onRescheduleClickHandler: OnRescheduleClickHandler? = null
    private var items: ArrayList<WeekModel> = ArrayList()
    private var dateTime: DateTime? = null
    private val txtMonth: TextView
    private var month = ""
    private var year = ""
    private var lastSelectedPosition = 0
    init {
        if (weekList != null) {
            items.addAll(weekList)
        }
        dateTime = today
        this.txtMonth = txtMonth
    }
    fun setOnItemClickHandler(onRescheduleClickHandler: OnRescheduleClickHandler?) {
        this.onRescheduleClickHandler = onRescheduleClickHandler
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mRescheduleAdapterBinding =
            RescheduleAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(mRescheduleAdapterBinding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtDays.text = items[position].getDays()
        holder.binding.txtDate.text = items[position].getDate()

//        holder.mRescheduleAdapterBinding.txtDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onRescheduleClickHandler.onDateSelected(position);
//                holder.mRescheduleAdapterBinding.txtDate.setBackgroundResource(R.drawable.circle);
//                holder.mRescheduleAdapterBinding.txtDate.setTextColor(Color.WHITE);
//                month = items.get(position).getDateTime().toString("MMMM");
//                year = items.get(position).getDateTime().toString("yy");
//                txtMonth.setText(month + " " + year);
//            }
//        });
        if (lastSelectedPosition == position) {
            onRescheduleClickHandler!!.onDateSelected(position)
            holder.binding.txtDate.setBackgroundResource(R.drawable.circle)
            holder.binding.txtDate.setTextColor(Color.WHITE)
            month = items[position].getDateTime().toString("MMMM")
            year = items[position].getDateTime().toString("yyyy")
            txtMonth.text = "$month $year"
        } else {
            holder.binding.txtDate.setBackgroundResource(R.drawable.grey_circle)
            holder.binding.txtDate.setTextColor(Color.BLACK)
        }

        holder.binding.txtDate.setOnClickListener {
            lastSelectedPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnItemClickHandler(onItemClickHandler: OnListItemClickHandler?) {
        this.onItemClickHandler = onItemClickHandler
    }

    fun setOnRescheduleClickHandler(onRescheduleClickHandler: OnRescheduleClickHandler?) {
        this.onRescheduleClickHandler = onRescheduleClickHandler
    }

    //    public void setData(List<Complaints> data) {
    //        items.clear();
    //        for (int index = 0; index < data.size(); index++) {
    //            ComplaintHistoryViewModel complaintViewModel = new ComplaintHistoryViewModel();
    //            complaintViewModel.clone(data.get(index));
    //            items.add(complaintViewModel);
    //        }
    //    }
    //
    //    public void addData(List<Complaints> data) {
    //        for (int index = 0; index < data.size(); index++) {
    //            ComplaintHistoryViewModel complaintViewModel = new ComplaintHistoryViewModel();
    //            complaintViewModel.clone(data.get(index));
    //            items.add(complaintViewModel);
    //        }
    //    }
    class ViewHolder(val binding: RescheduleAdapterBinding) : RecyclerView.ViewHolder(binding.root)

}