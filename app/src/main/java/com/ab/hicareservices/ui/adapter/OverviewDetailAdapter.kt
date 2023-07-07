package com.ab.hicareservices.ui.adapter

import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.databinding.LayoutOverviewdetailsBinding
import com.ab.hicareservices.ui.handler.onCartClickedHandler
import com.ab.hicareservices.ui.view.activities.OverviewProductDetailsActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class OverviewDetailAdapter : RecyclerView.Adapter<OverviewDetailAdapter.MainViewHolder>() {

    var productlist = mutableListOf<CartlistResponseData>()
    lateinit var requireActivity: FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    private var onCartClickedHandler: onCartClickedHandler? = null

    class MainViewHolder(val binding: LayoutOverviewdetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutOverviewdetailsBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val productlists = productlist[position]

        holder.binding.txtratingvalues.text = productlists.ProductRating.toString()
        Picasso.get().load(productlists.ProductThumbnail).into(holder.binding.imgthumbnail)
        holder.binding.txtname.text = productlists.ProductName
        holder.binding.ratingbar.rating = productlists.ProductRating!!.toFloat()
        holder.binding.txtpricelineqty.text=productlists.Quantity.toString()
        val drawable: Drawable = holder.binding.ratingbar.getProgressDrawable()
        drawable.setColorFilter(Color.parseColor("#fec348"), PorterDuff.Mode.SRC_ATOP)
        if (productlists.Discount != 0) {
            holder.binding.txtdealodday.text = "Saved " + "\u20B9" + productlists.Discount.toString()
            holder.binding.txtprice.text = productlists.DiscountedPricePerQuantity.toString()
            holder.binding.txtpriceline.visibility=View.GONE
            holder.binding.txtpriceline.text = "\u20B9" + productlists.PricePerQuantity.toString()
            holder.binding.txtpriceline.paintFlags =
                holder.binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.txtprice.text = "\u20B9" + productlists.PricePerQuantity.toString()
            holder.binding.txtpriceline.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return productlist.size
    }

    fun setOnOrderItemClicked(l: onCartClickedHandler) {
        onCartClickedHandler = l
    }

    fun setCartList(
        productlist: List<CartlistResponseData>,
        addToCartActivity: OverviewProductDetailsActivity,
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