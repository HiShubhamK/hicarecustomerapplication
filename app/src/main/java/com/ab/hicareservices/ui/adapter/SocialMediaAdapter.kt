package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.dashboard.BannerData
import com.ab.hicareservices.data.model.dashboard.MenuData
import com.ab.hicareservices.data.model.dashboard.SocialMediadata
import com.ab.hicareservices.databinding.DashboardMenuAdapterBinding
import com.ab.hicareservices.databinding.SocialmediaAdapterBinding
import com.ab.hicareservices.ui.view.fragments.ComplaintFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.viewmodel.GridViewModal
import com.squareup.picasso.Picasso

class SocialMediaAdapter(private val  fragmentActivity: FragmentActivity?) : RecyclerView.Adapter<SocialMediaAdapter.MainViewHolder>() {

    var socialmedialist = mutableListOf<SocialMediadata>()
//    private var mOnServiceRequestClickHandler: OnServiceRequestClickHandler? = null

    fun setSocialMedialist(socialmedialist: List<SocialMediadata>?) {
        if (socialmedialist != null) {
            this.socialmedialist = socialmedialist.toMutableList()
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = SocialmediaAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val socialmedialist = socialmedialist[position]
            holder.binding.tvOrderName.text = socialmedialist.Title
            Picasso.get().load(socialmedialist.ImageUrl).into( holder.binding.imgLogo)
            holder.itemView.setOnClickListener{

                    fragmentActivity!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(socialmedialist.PageUrl)))

            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return socialmedialist.size
    }


    class MainViewHolder(val binding: SocialmediaAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
