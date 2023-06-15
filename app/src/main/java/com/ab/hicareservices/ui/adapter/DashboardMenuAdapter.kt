package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.dashboard.MenuData
import com.ab.hicareservices.databinding.DashboardMenuAdapterBinding
import com.ab.hicareservices.ui.view.activities.BookInspectionActivity
import com.ab.hicareservices.ui.view.activities.ComplaintsActivity
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.view.fragments.SupportFragments
import com.squareup.picasso.Picasso

class DashboardMenuAdapter(private val  fragmentActivity: FragmentActivity?) : RecyclerView.Adapter<DashboardMenuAdapter.MainViewHolder>() {

    var service = mutableListOf<MenuData>()

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
            if (service.IsIcon==true){
                holder.binding.imgLogo2.visibility=View.VISIBLE
                holder.binding.imgLogo.visibility=View.GONE
                Picasso.get().load(service.ImageUrl).into( holder.binding.imgLogo2)

            }else{
                holder.binding.imgLogo2.visibility=View.GONE
                holder.binding.imgLogo.visibility=View.VISIBLE
                Picasso.get().load(service.ImageUrl).into( holder.binding.imgLogo)

            }
            holder.itemView.setOnClickListener{
                if(service.IsExternalAppBrowserLink==true){
                    fragmentActivity!!.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(service.PageLink)))
                }else if (service.IsAppLink==true){
                    if (service.Title.equals("My Orders")){
                        fragmentActivity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.container, OrdersFragment.newInstance()).addToBackStack("HomeFragment").commit()

                    }else if (service.Title.equals("Complaints")){
                        val intent=Intent(fragmentActivity,ComplaintsActivity::class.java)
                        fragmentActivity!!.startActivity(intent)
//                        fragmentActivity!!.supportFragmentManager.beginTransaction()
//                            .replace(R.id.container, ComplaintFragment.newInstance()).addToBackStack("HomeFragment").commit()
                    }else if (service.Title.equals("Renewals")){


                    }else if (service.Title.equals("Book an Inspection")){

                        val intent=Intent(fragmentActivity,BookInspectionActivity::class.java)
                        fragmentActivity!!.startActivity(intent)

//                        fragmentActivity!!.supportFragmentManager.beginTransaction()
//                            .replace(R.id.container, SupportFragments.newInstance()).addToBackStack("AccountFragment").commit()

                    }else if (service.Title.equals("Support")){
                        fragmentActivity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.container, SupportFragments.newInstance()).addToBackStack("AccountFragment").commit()
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
