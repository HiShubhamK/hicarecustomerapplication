package com.ab.hicareservices.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.data.model.product.ProductListResponseData
import com.ab.hicareservices.databinding.LayoutProductlistBinding
import com.squareup.picasso.Picasso

class ProductAdapter() : RecyclerView.Adapter<ProductAdapter.MainViewHolder>(){

    var productlist = mutableListOf<ProductListResponseData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutProductlistBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val productlists=productlist[position]

        if(productlists.IsStockAvailable==true) {

            Picasso.get().load(productlists.ProductThumbnail).into(holder.binding.imgthumbnail)
            holder.binding.txtname.text = productlists.ProductName
            holder.binding.ratingbar.rating = productlists.ProductRating!!.toFloat()



            if (productlists.Discount!=0) {
                holder.binding.txtdealodday.text = "\u20B9" + productlists.Discount.toString()
                holder.binding.txtprice.text = "\u20B9" + productlists.DiscountedPrice.toString()
                holder.binding.txtpriceline.text = "M.R.P : "+"\u20B9" + productlists.PricePerQuantity.toString()
                holder.binding.txtpriceline.paintFlags=holder.binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.binding.txtprice.text = "\u20B9" + productlists.PricePerQuantity.toString()
                holder.binding.txtpriceline.visibility= View.GONE
            }

        }else{

        }
    }

    override fun getItemCount(): Int {
        return productlist.size
    }

    fun setProductList(productlist: List<ProductListResponseData>?, requireActivity: FragmentActivity) {
        if (productlist != null) {
            this.productlist = productlist.toMutableList()
            notifyDataSetChanged()
        }
    }


    class MainViewHolder(val binding: LayoutProductlistBinding) :
        RecyclerView.ViewHolder(binding.root)

}