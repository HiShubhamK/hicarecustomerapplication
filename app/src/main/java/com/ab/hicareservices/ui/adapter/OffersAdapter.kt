package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.ab.hicareservices.R
import com.ab.hicareservices.ui.handler.OffersInterface
import com.ab.hicareservices.ui.viewmodel.OfferViewModel


class OffersAdapter(private val imageList: ArrayList<OfferViewModel>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<OffersAdapter.ImageViewHolder>() {
    private var offersInterface: OffersInterface? = null

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
        holder.itemView.setOnClickListener{
            offersInterface!!.onItemClick(position)

        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
    fun setOnOfferClick(l: OffersInterface) {
        offersInterface = l
    }

}