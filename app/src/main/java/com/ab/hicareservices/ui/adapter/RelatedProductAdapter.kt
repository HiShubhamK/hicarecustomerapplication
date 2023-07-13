package com.ab.hicareservices.ui.adapter

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.product.RelatedProducts
import com.ab.hicareservices.databinding.LayoutRelatedProductBinding
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.handler.OnRelatedProductClick
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso


class RelatedProductAdapter(
    activity: FragmentActivity,
    viewProductModel: ProductViewModel
) : RecyclerView.Adapter<RelatedProductAdapter.MainViewHolder>() {
    var productDetails = mutableListOf<RelatedProducts>()
    lateinit var productDetailActivity: FragmentActivity
    private var onRelatedProductClick: OnRelatedProductClick? = null
    private var onProductClickedHandler: OnProductClickedHandler? = null
    val viewmodel=viewProductModel
    val activityy=activity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutRelatedProductBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            holder.setIsRecyclable(false)
            val productlistdata = productDetails[position]
//            Picasso.get().load(productlistdata.ProductThumbnail).into(holder.binding.imgBanner)
            Glide.with(activityy).load(productlistdata.ProductThumbnail).into(holder.binding.imgBanner)
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

                onRelatedProductClick?.setonaddclicklistener(position,productlistdata.RelatedProductId!!.toInt(),1)



                //                viewmodel.getAddProductInCart(
//                    1,
//                    productlistdata.ProductId!!.toInt(),
//                    AppUtils2.customerid.toInt()
//                )
//                viewmodel.addtocart.observe(activityy,{
//                    if (it.IsSuccess==true){
//
//                        Toast.makeText(activityy,"Product Added To Cart",Toast.LENGTH_SHORT).show()
//
//                    }else{
//                        Toast.makeText(activityy,"Unable to add product, please try again later",Toast.LENGTH_SHORT).show()
//
//
//                    }

//                })
//                onProductClickedHandler!!.onProductClickedHandler(position,productlistdata.ProductId!!.toInt())
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
    fun setOnOrderItemClicked(l: OnRelatedProductClick) {
        onRelatedProductClick = l
    }

    class MainViewHolder(val binding: LayoutRelatedProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}