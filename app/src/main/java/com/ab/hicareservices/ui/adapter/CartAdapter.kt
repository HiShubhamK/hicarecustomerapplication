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
import com.ab.hicareservices.databinding.LayoutCartlistBinding
import com.ab.hicareservices.ui.handler.onCartClickedHandler
import com.ab.hicareservices.ui.view.activities.AddToCartActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class CartAdapter : RecyclerView.Adapter<CartAdapter.MainViewHolder>() {

    var productlist = mutableListOf<CartlistResponseData>()
    lateinit var requireActivity: FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    private var onCartClickedHandler: onCartClickedHandler? = null

    class MainViewHolder(val binding: LayoutCartlistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutCartlistBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val count = 0
        var counts = 1

        val productlists = productlist[position]

        holder.binding.txtratingvalues.text = productlists.ProductRating.toString()
        Picasso.get().load(productlists.ProductThumbnail).into(holder.binding.imgthumbnail)
        holder.binding.txtname.text = productlists.ProductName
        holder.binding.ratingbar.rating = productlists.ProductRating!!.toFloat()
        val drawable: Drawable = holder.binding.ratingbar.getProgressDrawable()
        drawable.setColorFilter(Color.parseColor("#fec348"), PorterDuff.Mode.SRC_ATOP)
        if (productlists.Discount != 0) {
            holder.binding.txtdealodday.text = "Save " + "\u20B9" + productlists.Discount.toString()
            holder.binding.txtprice.text = productlists.DiscountedPricePerQuantity.toString()
            holder.binding.txtpriceline.text = "M.R.P : " + "\u20B9" + productlists.PricePerQuantity.toString()
            holder.binding.txtpriceline.paintFlags =
                holder.binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.txtprice.text = "\u20B9" + productlists.PricePerQuantity.toString()
            holder.binding.txtpriceline.visibility = View.GONE
        }

        if(productlists.Quantity==0){
            holder.binding.textcount.text = "1"
            counts=1
        }else{
            holder.binding.textcount.text = productlists.Quantity.toString()
            counts=productlists.Quantity!!.toInt()
        }


        if (counts == 1) {
            holder.binding.imgremove.visibility = View.VISIBLE
        } else if (counts > 1) {
            holder.binding.imgremove.visibility = View.VISIBLE
        }

        holder.binding.imgadd.setOnClickListener {
            holder.binding.imgremove.isClickable = true
            counts = counts + 1
            if (counts > 1) {
                holder.binding.imgremove.visibility = View.VISIBLE
                holder.binding.imgadd.isClickable = true
                onCartClickedHandler!!.setonaddclicklistener(
                    position,
                    productlists.ProductId!!.toInt(),
                    1
                )
            } else if (counts <= productlists.MinimumBuyQuantity!!.toInt()) {
                holder.binding.imgadd.isClickable = false
            }
            holder.binding.textcount.text = counts.toString()

        }

        holder.binding.imgremove.setOnClickListener {
            counts = counts - 1
            if(counts==1) {
                holder.binding.textcount.text = counts.toString()
                holder.binding.imgremove.isClickable = false
                onCartClickedHandler!!.setonaddclicklistener(
                    position,
                    productlists.ProductId!!.toInt(),
                    -1
                )
            }else{
                holder.binding.textcount.text = counts.toString()
                onCartClickedHandler!!.setonaddclicklistener(
                    position,
                    productlists.ProductId!!.toInt(),
                    -1
                )
            }
            if (counts <= productlists.MaximumBuyQuantity!!.toInt()) {
                holder.binding.imgadd.isClickable = true
            }


        }

        holder.binding.imgdelete.setOnClickListener {
//            if(count==1){
//                holder.binding.textcount.text=count.toString()
//            }
//            holder.binding.textcount.text="1"
            onCartClickedHandler!!.setondeleteclicklistener(
                position,
                productlists.CartId,
                productlists.UserId
            )

//            viewProductModel.getDeleteProductCart(productlists.CartId!!.toInt(),productlists.UserId!!.toInt())
            notifyDataSetChanged()
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