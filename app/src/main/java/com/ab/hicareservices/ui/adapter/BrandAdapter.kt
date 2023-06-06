package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.dashboard.BannerData
import com.ab.hicareservices.data.model.dashboard.BrandData
import com.squareup.picasso.Picasso


class BrandAdapter(private val viewPager: ViewPager2, private val requireActivity: FragmentActivity) :
    RecyclerView.Adapter<BrandAdapter.ImageViewHolder>() {
    var brand = mutableListOf<BrandData>()
//   lateinit var requireActivity:FragmentActivity


    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)


    }
    fun serBrand(branddata: ArrayList<BrandData>){
        this.brand=branddata
        notifyDataSetChanged()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.brand_image_container, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        Picasso.get().load(brand[position].ImageUrl).into(holder.imageView)
        if (position == brand.size-1){
            viewPager.post(runnablenew)
        }
        holder.itemView.setOnClickListener{
//            if(bannerLis[position].IsExternalAppBrowserLink==true){
//
//            requireActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bannerLis[position].PageLink)))
//            }else if (bannerLis[position].IsAppLink==true){
//
//            }else if (bannerLis[position].IsInAppBrowserLink==true){
//
//            }else{
//
//            }
        }
        holder.tvTitle.text=brand[position].Title

    }

    override fun getItemCount(): Int {
        return brand.size
    }

    private val runnablenew = Runnable {
        brand.addAll(brand)
        notifyDataSetChanged()
    }
}