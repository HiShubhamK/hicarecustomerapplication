package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.dashboard.MenuData
import com.ab.hicareservices.databinding.DashboardMenuAdapterBinding
import com.ab.hicareservices.ui.view.activities.*
import com.ab.hicareservices.ui.view.fragments.ComplaintFragmentNew
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.view.fragments.SupportFragments
import com.squareup.picasso.Picasso

class DashboardMenuAdapterNew(private val fragmentActivity: FragmentActivity?) :
    RecyclerView.Adapter<DashboardMenuAdapterNew.MainViewHolder>() {

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
            if (service.IsIcon == true) {
                holder.binding.imgLogo2.visibility = View.VISIBLE
                holder.binding.imgLogo.visibility = View.GONE
                Picasso.get().load(service.ImageUrl).into(holder.binding.imgLogo2)

            } else {
                holder.binding.imgLogo2.visibility = View.GONE
                holder.binding.imgLogo.visibility = View.VISIBLE
                Picasso.get().load(service.ImageUrl).into(holder.binding.imgLogo)

            }
            holder.itemView.setOnClickListener {
                if (service.IsExternalAppBrowserLink == true) {
                    fragmentActivity!!.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(service.PageLink)
                        )
                    )
                } else if (service.Title.equals("My Orders") && service.IsAppLink == true) {
                    val intent = Intent(fragmentActivity, MyOrderActivity::class.java)
                    fragmentActivity!!.startActivity(intent)


//                    fragmentActivity!!.supportFragmentManager.beginTransaction()
//                        .replace(R.id.container, OrdersFragment.newInstance(true))
//                        .addToBackStack("HomeFragment").commit()

                } else if (service.Title.equals("Complaints") && service.IsAppLink == true) {

                    val intent = Intent(fragmentActivity, ComplaintsActivityNew::class.java)
                    fragmentActivity!!.startActivity(intent)
//                    fragmentActivity!!.supportFragmentManager.beginTransaction()
//                        .replace(R.id.container, ComplaintFragmentNew.newInstance())
//                        .addToBackStack("HomeFragment").commit()
                } else if (service.Title.equals("Renewals") && service.IsAppLink == true) {


                } else if (service.Title.equals("Book an Inspection") && service.IsAppLink == true) {

                    val intent = Intent(fragmentActivity, BookInspectionActivity::class.java)
                    fragmentActivity!!.startActivity(intent)

//                        fragmentActivity!!.supportFragmentManager.beginTransaction()
//                            .replace(R.id.container, SupportFragments.newInstance()).addToBackStack("AccountFragment").commit()

                } else if (service.Title.equals("Upcoming Service") && service.IsAppLink == true) {

                    val intent = Intent(fragmentActivity, UpcomingServicesActivity::class.java)
                    fragmentActivity!!.startActivity(intent)

//                        fragmentActivity!!.supportFragmentManager.beginTransaction()
//                            .replace(R.id.container, SupportFragments.newInstance()).addToBackStack("AccountFragment").commit()

                } else if (service.Title.equals("Support") && service.IsAppLink == true) {
                    val intent = Intent(fragmentActivity, SupportActivity::class.java)
                    fragmentActivity!!.startActivity(intent)
                    fragmentActivity!!.finish()
//                    fragmentActivity!!.supportFragmentManager.beginTransaction()
//                        .replace(R.id.container, SupportFragments.newInstance())
//                        .addToBackStack("AccountFragment").commit()
                } else if (service.Title.equals("Products") && service.IsAppLink == true) {
                    fragmentActivity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, ProductFragment.newInstance())
                        .addToBackStack("AccountFragment").commit()
                } else if (service.IsInAppBrowserLink == true && service.PageLink != null || service.PageLink != "") {
                    val intent = Intent(fragmentActivity, InAppWebViewActivity::class.java)
                    intent.putExtra("PageLink", service.PageLink)
                    fragmentActivity!!.startActivity(intent)
                } else {
                    Toast.makeText(fragmentActivity, "Coming Soon!", Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return service.size
    }

    class MainViewHolder(val binding: DashboardMenuAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {}
}
