package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.servicesmodule.ServiceListResponseData
import com.ab.hicareservices.databinding.BookingServiceAdapterBinding
import com.ab.hicareservices.ui.view.activities.BokingServiceDetailsActivity
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.SharedPreferencesManager
import com.ab.hicareservices.utils.UserData
import com.squareup.picasso.Picasso

class BookingServiceListAdapter : RecyclerView.Adapter<BookingServiceListAdapter.MainViewHolder>() {

    var service = mutableListOf<ServiceListResponseData>()
    lateinit var requireActivity: FragmentActivity

    fun setServiceList(
        movies: List<ServiceListResponseData>?,
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

        val binding = BookingServiceAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val service = service[position]
        holder.binding.tvOrderName.text = service.ServiceName
        Picasso.get().load(service.ServiceThumbnail).into(holder.binding.imgLogo)

        holder.binding.txtshortdes.text = service.ShortDescription.toString()
        holder.binding.txtlongdes.text = service.DetailDescription.toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(requireActivity, BokingServiceDetailsActivity::class.java)
            AppUtils2.servicecode = service.ServiceCode.toString()
            SharedPreferenceUtil.setData(requireActivity,"ServiceId", service.Id.toString())
            intent.putExtra("ServiceId", service.Id.toString())
            intent.putExtra("ServiceName", service.ServiceName.toString())
            intent.putExtra("ServiceCode", service.ServiceCode.toString())
            intent.putExtra("ServiceThumbnail", service.ServiceThumbnail.toString())
            intent.putExtra("ShortDescription", service.ShortDescription.toString())
            intent.putExtra("DetailDescription", service.DetailDescription.toString())

            requireActivity.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return service.size
    }

    class MainViewHolder(val binding: BookingServiceAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}
