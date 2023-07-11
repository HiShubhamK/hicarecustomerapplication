package com.ab.hicareservices.ui.adapter

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.databinding.LayoutCartlistBinding
import com.ab.hicareservices.ui.handler.onCartClickedHandler
import com.ab.hicareservices.ui.view.activities.AddToCartActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso

class CartAdapter : RecyclerView.Adapter<CartAdapter.MainViewHolder>() {

    var productlist = mutableListOf<CartlistResponseData>()
    lateinit var requireActivity: FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    lateinit var progressDialog: ProgressDialog
    var changebuttonstatus: Boolean = false
    private var onCartClickedHandler: onCartClickedHandler? = null
//    lateinit var progressDialogs: ProgressDialog

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

//        if(changebuttonstatus==false){
//            holder.binding.imgadd.isClickable=true
//            holder.binding.imgremove.isClickable=true
//            holder.binding.imgdelete.isClickable=true
//        }else{
//            holder.binding.imgadd.isClickable=true
//            holder.binding.imgremove.isClickable=true
//            holder.binding.imgdelete.isClickable=true
//        }

        progressDialog = ProgressDialog(requireActivity, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        val productlists = productlist[position]

        holder.binding.textcount.text= productlists.Quantity!!.toInt().toString()
        counts = productlists.Quantity!!.toInt()
        holder.binding.txtratingvalues.text = productlists.ProductRating.toString()
        Picasso.get().load(productlists.ProductThumbnail).into(holder.binding.imgthumbnail)
        holder.binding.txtname.text = productlists.ProductName
        holder.binding.ratingbar.rating = productlists.ProductRating!!.toFloat()
        val drawable: Drawable = holder.binding.ratingbar.getProgressDrawable()
        drawable.setColorFilter(Color.parseColor("#fec348"), PorterDuff.Mode.SRC_ATOP)
        if (productlists.Discount != 0) {
            holder.binding.txtdealodday.text =
                "Saved " + "\u20B9" + productlists.Discount.toString()
            holder.binding.txtprice.text = productlists.DiscountedPricePerQuantity.toString()
            holder.binding.txtpriceline.text = "\u20B9" + productlists.PricePerQuantity.toString()
            holder.binding.txtpriceline.paintFlags =
                holder.binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.txtprice.text = "\u20B9" + productlists.PricePerQuantity.toString()
            holder.binding.txtpriceline.visibility = View.GONE
        }
        holder.binding.imgdelete.setOnClickListener {

            progressDialog.dismiss()

            onCartClickedHandler!!.setondeleteclicklistener(
                position,
                productlists.CartId,
                productlists.UserId
            )

            progressDialog.dismiss()
            notifyDataSetChanged()
        }

        holder.binding.imgadd.setOnClickListener {
//            progressDialog.show()
            counts = counts + 1
            if (counts > 1) {
                holder.binding.imgremove.isEnabled = true
                onCartClickedHandler!!.setonaddclicklistener(
                    position,
                    productlists.ProductId!!.toInt(),
                    1, holder.binding.imgadd
                )
            } else if (counts <= productlists.MaximumBuyQuantity!!.toInt()) {
                holder.binding.imgadd.isClickable = false
            }
            holder.binding.textcount.text = counts.toString()
//                viewProductModel.getAddProductInCart(counts, productlists.ProductId!!.toInt(), 20)

        }

        holder.binding.imgremove.setOnClickListener {
            counts = counts - 1
            if (counts == 1) {
                holder.binding.textcount.text = "1"
                holder.binding.imgremove.isEnabled = false
                onCartClickedHandler!!.setonaddclicklistener(
                    position,
                    productlists.ProductId!!.toInt(),
                    -1, holder.binding.imgremove
                )
            } else if (counts < 1) {
                holder.binding.textcount.text = "1"
                holder.binding.imgremove.isEnabled = false
            } else {
                holder.binding.textcount.text = counts.toString()
                onCartClickedHandler!!.setonaddclicklistener(
                    position,
                    productlists.ProductId!!.toInt(),
                    -1, holder.binding.imgremove
                )
            }
            if (counts <= productlists.MaximumBuyQuantity!!.toInt()) {
                holder.binding.imgadd.isClickable = true
            }
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
        viewProductModel: ProductViewModel,
        progressDialog: ProgressDialog,
        changebuttonstatus: Boolean
    ) {
        this.requireActivity = addToCartActivity
        this.viewProductModel = viewProductModel
//        this.progressDialog = progressDialog
        this.changebuttonstatus = changebuttonstatus
        if (productlist != null) {
            this.productlist = productlist.toMutableList()
            notifyDataSetChanged()
        }
    }
}