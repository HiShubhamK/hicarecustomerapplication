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
import com.ab.hicareservices.data.model.dashboard.MenuData
import com.ab.hicareservices.data.model.dashboard.OfferData
import com.ab.hicareservices.ui.viewmodel.OfferViewModel
import com.squareup.picasso.Picasso


class OffersAdapter(private var offers: List<OfferData>, private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<OffersAdapter.ImageViewHolder>() {
//    private var offersInterface: OffersInterface? = null

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgOffer: LottieAnimationView = itemView.findViewById(R.id.imgOffer)
        val tvOffers: TextView = itemView.findViewById(R.id.tvOffers)
    }
    fun setServiceList(OfferData: List<OfferData>?) {
        if (OfferData != null) {
            this.offers = OfferData.toMutableList() as ArrayList<OfferData>
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.offer_adapter, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        try {
//            Picasso.get().load(imageList[position].courseImg).into(holder.imgOffer)
//            holder.imgOffer.setAnimation(offers[position].OfferTitle)


//            holder.imgOffer.repeatCount = LottieDrawable.INFINITE
//            holder.imgOffer.playAnimation()

//        Glide.with(this).load(imageList[position].courseImg)).into(holder.imgOffer)

            holder.tvOffers.text=offers[position].OfferTitle

            if (position == offers.size-1){
                viewPager2.post(runnable)
            }
            holder.itemView.setOnClickListener{
//                offersInterface!!.onItemClick(position,offers)

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
//
    }

    override fun getItemCount(): Int {
        return offers.size
    }

    private val runnable = Runnable {
        offers=offers
        notifyDataSetChanged()
    }
//    fun setOnOfferClick(l: OffersInterface) {
//        offersInterface = l
//    }

}