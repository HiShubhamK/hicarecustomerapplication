package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.ui.handler.offerinterface


class OffersTermsAdapter(private val viewPager2: FragmentActivity, termsnConditions: List<String>?) :
    RecyclerView.Adapter<OffersTermsAdapter.ImageViewHolder>() {
    private var offersInterface: offerinterface? = null

var data=termsnConditions


    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imgOffer: LottieAnimationView = itemView.findViewById(R.id.imgOffer)
        val tvTerms: TextView = itemView.findViewById(R.id.tvTerms)
    }
//    fun serBanner(bannerListt: List<String>?){
//        this.bannerLis= bannerListt as List<String>
//        notifyDataSetChanged()
//
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.offer_terms_adapter, parent, false)
        return ImageViewHolder(view)
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        try {

//        Picasso.get().load(imageList[position].courseImg).into(holder.imgOffer)
//        holder.imgOffer.setAnimation(bannerLis[position].courseImg)
//
//            holder.imgOffer.repeatCount = LottieDrawable.INFINITE
//            holder.imgOffer.playAnimation()

//        Glide.with(this).load(imageList[position].courseImg)).into(holder.imgOffer)

            holder.tvTerms.text = data?.get(position).toString()

//            if (position == bannerLis.size - 1) {
//                viewpa.post(runnable)
//            }
//            holder.itemView.setOnClickListener {
//                offersInterface!!.onOfferClick(position, bannerLis as ArrayList<OfferData>)
//
//            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

//    private val runnable = Runnable {
//        bannerLis.addAll(bannerLis)
//        notifyDataSetChanged()
//    }
    fun setOnOfferClick(l: offerinterface) {
        offersInterface = l
    }

}