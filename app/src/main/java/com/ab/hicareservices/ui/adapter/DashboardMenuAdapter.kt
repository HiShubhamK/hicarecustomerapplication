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
import com.ab.hicareservices.databinding.DashboardMenuAdapterBinding
import com.ab.hicareservices.ui.view.fragments.ComplaintFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.viewmodel.GridViewModal
import com.squareup.picasso.Picasso

class DashboardMenuAdapter(private val  fragmentActivity: FragmentActivity?) : RecyclerView.Adapter<DashboardMenuAdapter.MainViewHolder>() {

    var service = mutableListOf<MenuData>()
//    private var mOnServiceRequestClickHandler: OnServiceRequestClickHandler? = null

    fun setServiceList(movies: List<MenuData>?) {
        if (movies != null) {
            this.service = movies.toMutableList()
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DashboardMenuAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val service = service[position]
            holder.binding.tvOrderName.text = service.Title
            Picasso.get().load(service.ImageUrl).into( holder.binding.imgLogo)
            holder.itemView.setOnClickListener{
                if(service.IsExternalAppBrowserLink==true){

                    fragmentActivity!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(service.PageLink)))
                }else if (service.IsAppLink==true){
                    if (service.Title.equals("My Orders")){
                        fragmentActivity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.container, OrdersFragment.newInstance()).addToBackStack("AccountFragment").commit();

                    }else if (service.Title.equals("Complaints")){
                        fragmentActivity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.container, ComplaintFragment.newInstance()).addToBackStack("AccountFragment").commit();
                    }else if (service.Title.equals("Renewals")){


                    }else if (service.Title.equals("Book Inspection")){


                    }else if (service.Title.equals("Support")){

                    }

                }else if (service.IsInAppBrowserLink==true){

                }else{

                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return service.size
    }


    class MainViewHolder(val binding: DashboardMenuAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
