package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.OtherServiceList
import com.ab.hicareservices.databinding.BookingServicelistLayoutBinding
import com.ab.hicareservices.ui.handler.OnServiceclicklistner
import com.ab.hicareservices.ui.view.activities.BokingServiceDetailsActivity
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso

class BookingServiceListDetailsAdapter: RecyclerView.Adapter<BookingServiceListDetailsAdapter.MainViewHolder>() {

    var service = mutableListOf<OtherServiceList>()
    lateinit var requireActivity: FragmentActivity
    private var onServiceclicklistner: OnServiceclicklistner? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = BookingServicelistLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val service = service[position]
        holder.binding.tvOrderName.text = service.ServiceName
        Picasso.get().load(service.ServiceThumbnail).into(holder.binding.imgLogo)

        holder.itemView.setOnClickListener {

            AppUtils2.servicecode=service.ServiceCode.toString()

            onServiceclicklistner?.onClickListnerdata(position,service.Id,service.ServiceName,service.ServiceCode,service.ServiceThumbnail,service.ShortDescription,service.DetailDescription)

//            val intent= Intent(requireActivity, BokingServiceDetailsActivity::class.java)
//            intent.putExtra("ServiceId", service.Id.toString())
//            intent.putExtra("ServiceName",service.ServiceName.toString())
//            intent.putExtra("ServiceCode",service.ServiceCode.toString())
//            intent.putExtra("ServiceThumbnail",service.ServiceThumbnail.toString())
//            intent.putExtra("ShortDescription",service.ShortDescription.toString())
//            intent.putExtra("DetailDescription",service.DetailDescription.toString())
//            requireActivity.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return service.size
    }

    fun setServiceLists(
        movies: ArrayList<OtherServiceList>,
        pestServicesActivity: Context
    ) {
        if (movies != null) {
            this.service = movies.toMutableList()
            this.requireActivity = pestServicesActivity as FragmentActivity
        }
        notifyDataSetChanged()
    }


    fun getservicelistdata(l: OnServiceclicklistner) {
        onServiceclicklistner=l
    }

    class MainViewHolder(val binding: BookingServicelistLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}