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
import com.ab.hicareservices.databinding.LayoutProductlistBinding
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.view.activities.ProductDetailActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class ProductAdapter() : RecyclerView.Adapter<ProductAdapter.MainViewHolder>(){

    var productlist = mutableListOf<ProductListResponseData>()
    lateinit var requireActivity:FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    private var onProductClickedHandler: OnProductClickedHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutProductlistBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val productlists=productlist[position]

        if(productlists.IsStockAvailable==true) {
            holder.binding.txtratingvalues.text=productlists.ProductRating.toString()
            Picasso.get().load(productlists.ProductThumbnail).into(holder.binding.imgthumbnail)
            holder.binding.txtname.text = productlists.ProductName
            holder.binding.ratingbar.rating = productlists.ProductRating!!.toFloat()
            val drawable: Drawable = holder.binding.ratingbar.getProgressDrawable()
//            drawable.setColorFilter(Color.parseColor("#FFEA00"), PorterDuff.Mode.SRC_ATOP)
            drawable.setColorFilter(Color.parseColor("#fec348"), PorterDuff.Mode.SRC_ATOP)

            if (productlists.Discount!=0) {
                holder.binding.txtdealodday.text ="Save " +"\u20B9" + productlists.Discount.toString()
                holder.binding.txtprice.text = productlists.DiscountedPrice.toString()
                holder.binding.txtpriceline.visibility=View.GONE
                holder.binding.txtpriceline.text = "\u20B9" + productlists.PricePerQuantity.toString()
                holder.binding.txtpriceline.paintFlags=holder.binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.binding.txtprice.text = "\u20B9" + productlists.PricePerQuantity.toString()
                holder.binding.txtpriceline.visibility= View.GONE
            }

            holder.binding.btnaddtocart.setOnClickListener {
                onProductClickedHandler?.onProductClickedHandler(position,productlists.ProductId!!.toInt())
//                viewProductModel.getAddProductInCart(1,productlists.ProductId!!.toInt(),20)
            }

            holder.itemView.setOnClickListener {
                val intent= Intent(requireActivity,ProductDetailActivity::class.java)
                intent.putExtra("productid",productlists.ProductId.toString())
                requireActivity.startActivity(intent)
            }

        }else{

        }
    }

    override fun getItemCount(): Int {
        return productlist.size
    }

    fun setProductList(
        productlist: List<ProductListResponseData>?,
        requireActivity: FragmentActivity,
        viewProductModel: ProductViewModel
    ) {
        this.requireActivity=requireActivity
        this.viewProductModel=viewProductModel
        if (productlist != null) {
            this.productlist = productlist.toMutableList()
            notifyDataSetChanged()
        }
    }

    fun setOnOrderItemClicked(l: OnProductClickedHandler) {
        onProductClickedHandler = l
    }

    class MainViewHolder(val binding: LayoutProductlistBinding) :
        RecyclerView.ViewHolder(binding.root)

}