package com.hc.hicareservices.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.cunoraz.gifview.library.GifView
import com.hc.hicareservices.R
import com.hc.hicareservices.ui.viewmodel.OfferViewModel
import com.squareup.picasso.Picasso


class OffersAdapter(private val imageList: ArrayList<OfferViewModel>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<OffersAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgOffer: LottieAnimationView = itemView.findViewById(R.id.imgOffer)
        val tvOffers: TextView = itemView.findViewById(R.id.tvOffers)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.offer_adapter, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        Picasso.get().load(imageList[position].courseImg).into(holder.imgOffer)
        holder.imgOffer.setAnimation(imageList[position].courseImg)

        holder.imgOffer.repeatCount = LottieDrawable.INFINITE
        holder.imgOffer.playAnimation()

//        Glide.with(this).load(imageList[position].courseImg)).into(holder.imgOffer)

        holder.tvOffers.text=imageList[position].courseName

        if (position == imageList.size-1){
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}