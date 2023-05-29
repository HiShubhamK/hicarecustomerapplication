package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.ui.handler.OffersInterface
import com.ab.hicareservices.ui.viewmodel.OfferViewModel
import com.squareup.picasso.Picasso


class ComplaintAttachmentAdapter() :
    RecyclerView.Adapter<ComplaintAttachmentAdapter.ImageViewHolder>() {
    private var offersInterface: OffersInterface? = null
    private lateinit var imageList:ArrayList<String>
    fun setAttachment(complaintdata: ArrayList<String>) {
        if (complaintdata != null) {
            this.imageList = complaintdata
        }
        notifyDataSetChanged()
    }
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivComplaintAttachment: ImageView = itemView.findViewById(R.id.ivComplaintAttachment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.complaint_attachment_adapter, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Picasso.get().load(imageList[position]).into(holder.ivComplaintAttachment)
//        holder.imgOffer.setAnimation(imageList[position].courseImg)



//        Glide.with(this).load(imageList[position].courseImg)).into(holder.imgOffer)

//        holder.tvOffers.text=imageList[position]

//        if (position == imageList.size-1){
//            viewPager2.post(runnable)
//        }
//        holder.itemView.setOnClickListener{
//            offersInterface!!.onItemClick(position)
//
//        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
//
//    private val runnable = Runnable {
//        imageList.addAll(imageList)
//        notifyDataSetChanged()
//    }
    fun setOnOfferClick(l: OffersInterface) {
        offersInterface = l
    }

}