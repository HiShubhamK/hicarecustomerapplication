package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.product.ProductGallery
import com.ab.hicareservices.data.model.product.RelatedProducts
import com.ab.hicareservices.databinding.LayoutBannerItemBinding
import com.ab.hicareservices.databinding.LayoutRelatedProductBinding
import com.ab.hicareservices.ui.view.activities.ProductDetailActivity
import com.squareup.picasso.Picasso


class RelatedProductAdapter() : RecyclerView.Adapter<RelatedProductAdapter.MainViewHolder>() {
    var productDetails = mutableListOf<RelatedProducts>()
    lateinit var productDetailActivity: FragmentActivity




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutRelatedProductBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val productlistdata = productDetails[position]
        Picasso.get().load(productlistdata.ProductThumbnail).into(holder.binding.imgBanner)

        holder.binding.tvRelatedProductname.text=productlistdata.RelatedProductName
    }


    override fun getItemCount(): Int {
        return productDetails.size
    }
    fun setRelatedProduct(
        productDetails: ArrayList<RelatedProducts>,
        fragmentActivity: FragmentActivity
    ) {
        this.productDetails = productDetails
        this.productDetailActivity = fragmentActivity
        notifyDataSetChanged()
    }


    class MainViewHolder(val binding: LayoutRelatedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}