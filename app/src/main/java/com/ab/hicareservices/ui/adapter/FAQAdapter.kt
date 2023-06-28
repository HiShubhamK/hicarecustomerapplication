package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.product.ProductFAQ
import com.ab.hicareservices.data.model.product.ProductGallery
import com.ab.hicareservices.data.model.product.RelatedProducts
import com.ab.hicareservices.databinding.LayoutBannerItemBinding
import com.ab.hicareservices.databinding.LayoutFaqBinding
import com.ab.hicareservices.databinding.LayoutRelatedProductBinding
import com.ab.hicareservices.ui.view.activities.ProductDetailActivity
import com.squareup.picasso.Picasso


class FAQAdapter() : RecyclerView.Adapter<FAQAdapter.MainViewHolder>() {
    var productDetails = mutableListOf<ProductFAQ>()
    lateinit var productDetailActivity: FragmentActivity




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutFaqBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val productlistdata = productDetails[position]

        holder.binding.tvQuetion.text=productlistdata.FAQTitle
        holder.binding.tvDesc.text=productlistdata.FAQDetail
        holder.binding.tvShowAnswer.setOnClickListener{
            if (holder.binding.crdDetail.isVisible){
                holder.binding.tvShowAnswer.text="+"
                holder.binding.crdDetail.visibility= View.GONE
            }else{
                holder.binding.tvShowAnswer.text="-"
                holder.binding.crdDetail.visibility= View.VISIBLE

            }

        }
    }


    override fun getItemCount(): Int {
        return productDetails.size
    }
    fun setFaq(
        productDetails: ArrayList<ProductFAQ>,
        productDetailActivity: ProductDetailActivity
    ) {
        this.productDetails = productDetails
        this.productDetailActivity = productDetailActivity
        notifyDataSetChanged()
    }


    class MainViewHolder(val binding: LayoutFaqBinding) :
        RecyclerView.ViewHolder(binding.root)
}