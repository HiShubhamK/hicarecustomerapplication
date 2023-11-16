package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.servicesmodule.ServiceListResponseData
import com.ab.hicareservices.databinding.DashboardMenuAdapterBinding
import com.ab.hicareservices.ui.view.activities.BokingServiceDetailsActivity
import com.ab.hicareservices.ui.view.activities.PestServicesActivity
import com.squareup.picasso.Picasso

class BookingServiceListAdapter : RecyclerView.Adapter<BookingServiceListAdapter.MainViewHolder>() {

    var service = mutableListOf<ServiceListResponseData>()
    lateinit var requireActivity: FragmentActivity

    fun setServiceList(
        movies: List<ServiceListResponseData>?,
        pestServicesActivity: PestServicesActivity
    ) {
        if (movies != null) {
            this.service = movies.toMutableList()
            this.requireActivity = pestServicesActivity
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DashboardMenuAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val service = service[position]
        holder.binding.tvOrderName.text = service.ServiceName
        Picasso.get().load(service.ServiceThumbnail).into(holder.binding.imgLogo2)

        holder.itemView.setOnClickListener {
            val intent= Intent(requireActivity, BokingServiceDetailsActivity::class.java)
            intent.putExtra("ServiceName",service.ServiceName.toString())
            intent.putExtra("ServiceCode",service.ServiceCode.toString())
            intent.putExtra("ServiceThumbnail",service.ServiceThumbnail.toString())
            intent.putExtra("ShortDescription",service.ShortDescription.toString())
            intent.putExtra("DetailDescription",service.DetailDescription.toString())
            requireActivity.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return service.size
    }

    class MainViewHolder(val binding: DashboardMenuAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}
