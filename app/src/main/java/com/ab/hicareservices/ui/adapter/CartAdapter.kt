package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.product.ProductListResponseData
import com.ab.hicareservices.databinding.LayoutCartlistBinding
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.view.activities.AddToCartActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class CartAdapter : RecyclerView.Adapter<CartAdapter.MainViewHolder>() {

    var productlist = mutableListOf<ProductListResponseData>()
    lateinit var requireActivity: FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    private var onProductClickedHandler: OnProductClickedHandler? = null


    class MainViewHolder(val binding: LayoutCartlistBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutCartlistBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val count=0
        var counts=1

        val productlists=productlist[position]

        if(productlists.IsStockAvailable==true) {

            Picasso.get().load(productlists.ProductThumbnail).into(holder.binding.imgthumbnail)
            holder.binding.txtname.text = productlists.ProductName
            holder.binding.ratingbar.rating = productlists.ProductRating!!.toFloat()
            val drawable: Drawable = holder.binding.ratingbar.getProgressDrawable()
            drawable.setColorFilter(Color.parseColor("#FFEA00"), PorterDuff.Mode.SRC_ATOP)
            if (productlists.Discount!=0) {
                holder.binding.txtdealodday.text ="Save " +"\u20B9" + productlists.Discount.toString()
                holder.binding.txtprice.text = productlists.DiscountedPrice.toString()
                holder.binding.txtpriceline.text = "M.R.P : "+"\u20B9" + productlists.PricePerQuantity.toString()
                holder.binding.txtpriceline.paintFlags=holder.binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.binding.txtprice.text = "\u20B9" + productlists.PricePerQuantity.toString()
                holder.binding.txtpriceline.visibility= View.GONE
            }

        }else{

        }


        holder.binding.imgadd.setOnClickListener {
                counts=counts+1
            holder.binding.textcount.text=counts.toString()
        }

        holder.binding.imgremove.setOnClickListener {
            counts=counts-1
            holder.binding.textcount.text=counts.toString()
            if(counts==1){
                holder.binding.imgremove.visibility=View.GONE
                holder.binding.imgdelete.visibility=View.VISIBLE
            }else{
                holder.binding.imgdelete.visibility=View.GONE
            }
        }

        holder.binding.imgdelete.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return productlist.size
    }

    fun setCartList(
        productlist: List<ProductListResponseData>?,
        addToCartActivity: AddToCartActivity,
        viewProductModel: ProductViewModel
    ) {
        this.requireActivity = addToCartActivity
        this.viewProductModel = viewProductModel
        if (productlist != null) {
            this.productlist = productlist.toMutableList()
            notifyDataSetChanged()
        }
    }
}