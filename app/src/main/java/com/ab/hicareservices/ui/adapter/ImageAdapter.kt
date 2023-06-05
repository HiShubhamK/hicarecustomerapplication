package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.dashboard.BannerData
import com.squareup.picasso.Picasso


class ImageAdapter(private val viewPager2: ViewPager2,private val requireActivity: FragmentActivity) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    var bannerLis = mutableListOf<BannerData>()
//   lateinit var requireActivity:FragmentActivity


    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)


    }
    fun serBanner(bannerListt: ArrayList<BannerData>){
        this.bannerLis=bannerListt
        notifyDataSetChanged()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_container, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        Picasso.get().load(bannerLis[position].ImageUrl).into(holder.imageView)
        if (position == bannerLis.size-1){
            viewPager2.post(runnable)
        }
        holder.itemView.setOnClickListener{
            if(bannerLis[position].IsExternalAppBrowserLink==true){

            requireActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(bannerLis[position].PageLink)))
            }else if (bannerLis[position].IsAppLink==true){

            }else if (bannerLis[position].IsInAppBrowserLink==true){

            }else{

            }
        }

    }

    override fun getItemCount(): Int {
        return bannerLis.size
    }

    private val runnable = Runnable {
        bannerLis.addAll(bannerLis)
        notifyDataSetChanged()
    }
}