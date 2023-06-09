package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.dashboard.BannerData
import com.ab.hicareservices.data.model.dashboard.OfferData
import com.ab.hicareservices.ui.handler.OffersInterface
import com.ab.hicareservices.ui.handler.offerinterface


class OffersAdapter(private val viewpa: ViewPager2, private val viewPager2: FragmentActivity) :
    RecyclerView.Adapter<OffersAdapter.ImageViewHolder>() {
    private var offersInterface: offerinterface? = null

    var bannerLis = mutableListOf<OfferData>()

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgOffer: LottieAnimationView = itemView.findViewById(R.id.imgOffer)
        val tvOffers: TextView = itemView.findViewById(R.id.tvOffers)
    }
    fun serBanner(bannerListt: ArrayList<OfferData>){
        this.bannerLis=bannerListt
        notifyDataSetChanged()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.offer_adapter, parent, false)
        return ImageViewHolder(view)
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        try {
//        Picasso.get().load(imageList[position].courseImg).into(holder.imgOffer)
//        holder.imgOffer.setAnimation(bannerLis[position].courseImg)

            holder.imgOffer.repeatCount = LottieDrawable.INFINITE
            holder.imgOffer.playAnimation()

//        Glide.with(this).load(imageList[position].courseImg)).into(holder.imgOffer)

            holder.tvOffers.text = bannerLis[position].OfferTitle

            if (position == bannerLis.size - 1) {
                viewpa.post(runnable)
            }
            holder.itemView.setOnClickListener {
                offersInterface!!.onOfferClick(position, bannerLis as ArrayList<OfferData>)

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return bannerLis.size
    }

    private val runnable = Runnable {
        bannerLis.addAll(bannerLis)
        notifyDataSetChanged()
    }
    fun setOnOfferClick(l: offerinterface) {
        offersInterface = l
    }

}