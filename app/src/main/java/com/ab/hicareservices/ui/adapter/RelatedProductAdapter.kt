package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.product.ProductGallery
import com.ab.hicareservices.data.model.product.RelatedProducts
import com.ab.hicareservices.databinding.LayoutBannerItemBinding
import com.ab.hicareservices.databinding.LayoutRelatedProductBinding
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.handler.OnRelatedProductClick
import com.ab.hicareservices.ui.view.activities.ProductDetailActivity
import com.squareup.picasso.Picasso


class RelatedProductAdapter() : RecyclerView.Adapter<RelatedProductAdapter.MainViewHolder>() {
    var productDetails = mutableListOf<RelatedProducts>()
    lateinit var productDetailActivity: FragmentActivity
    private var onRelatedProductClick: OnRelatedProductClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutRelatedProductBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val productlistdata = productDetails[position]
            Picasso.get().load(productlistdata.ProductThumbnail).into(holder.binding.imgBanner)
            holder.binding.txtratingvalues.text = productlistdata.ProductRating.toString()
            holder.binding.ratingbar.rating = productlistdata.ProductRating!!.toFloat()
            val drawable: Drawable = holder.binding.ratingbar.progressDrawable
            drawable.setColorFilter(Color.parseColor("#fec348"), PorterDuff.Mode.SRC_ATOP)
            holder.binding.tvRelatedProductname.text = productlistdata.RelatedProductName
            holder.binding.tvDisccount.text =
                "Save " + "\u20B9" + productlistdata!!.Discount.toString()
            holder.binding.txtpriceline.text = "\u20B9" + productlistdata!!.PricePerQuantity
            holder.binding.txtprice.text = "\u20B9" + productlistdata!!.DiscountedPrice
            holder.binding.txtpriceline.paintFlags =
                holder.binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.imgddToCart.setOnClickListener{
                onRelatedProductClick!!.onRelatedProdAddtoCart(position,productlistdata.ProductId!!.toInt())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

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
    fun setOnRelatedProductClick(l: OnRelatedProductClick) {
        onRelatedProductClick = l
    }

    class MainViewHolder(val binding: LayoutRelatedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}