package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.data.model.product.ProducDetailsData
import com.ab.hicareservices.data.model.product.ProductGallery
import com.ab.hicareservices.databinding.LayoutBannerItemBinding
import com.ab.hicareservices.databinding.LayoutComplaintsAdaptersBinding
import com.ab.hicareservices.ui.view.activities.ComplaintDetailsActivity
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso


class ProductDetailAdapter(private val requireActivity: FragmentActivity) :
    RecyclerView.Adapter<ProductDetailAdapter.MainViewHolder>() {
    var ProductDetails = mutableListOf<ProductGallery>()
//   lateinit var requireActivity:FragmentActivity

    @SuppressLint("NotifyDataSetChanged")
    fun setPrductdetail(branddata: ArrayList<ProductGallery>) {
        this.ProductDetails = branddata!!
        notifyDataSetChanged()


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutBannerItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            Picasso.get().load(ProductDetails[position].GalleryImage).into(holder.binding.imgBanner)




    }


    override fun getItemCount(): Int {
        return ProductDetails.size
    }

    class MainViewHolder(val binding: LayoutBannerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}