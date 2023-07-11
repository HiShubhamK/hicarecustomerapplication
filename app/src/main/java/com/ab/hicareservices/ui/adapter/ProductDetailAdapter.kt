package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.product.ProductGallery
import com.ab.hicareservices.databinding.LayoutBannerItemBinding
import com.ab.hicareservices.ui.view.activities.ProductDetailActivity
import com.squareup.picasso.Picasso


class ProductDetailAdapter() : RecyclerView.Adapter<ProductDetailAdapter.MainViewHolder>() {
    var productDetails = mutableListOf<ProductGallery>()
    lateinit var productDetailActivity: FragmentActivity




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutBannerItemBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val productlistdata = productDetails[position]
        Picasso.get().load(productlistdata.GalleryImage).into(holder.binding.imgBanner)

        holder.binding.imgBanner.setOnClickListener{
            if (productlistdata.VideoUrl!!.isNotEmpty()){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(productlistdata.VideoUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                productDetailActivity!!.startActivity(intent)
            }
        }

    }


    override fun getItemCount(): Int {
        return productDetails.size
    }
    fun setPrductdetail(
        productDetails: ArrayList<ProductGallery>,
        productDetailActivity: ProductDetailActivity
    ) {
        this.productDetails = productDetails
        this.productDetailActivity = productDetailActivity
        notifyDataSetChanged()
    }

    class MainViewHolder(val binding: LayoutBannerItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}