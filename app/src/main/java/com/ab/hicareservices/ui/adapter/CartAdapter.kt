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
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.databinding.LayoutCartlistBinding
import com.ab.hicareservices.ui.handler.onCartClickedHandler
import com.ab.hicareservices.ui.view.activities.AddToCartActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import okhttp3.internal.notify

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

        progressDialog = ProgressDialog(requireActivity, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        val productlists = productlist[position]

        if (productlists.StockStatus != null) {
            if (productlists.StockStatus!! > 0) {
                holder.binding.crdbtnplusminus.visibility = View.VISIBLE
                holder.binding.outofstocks.visibility = View.GONE
            } else {
                holder.binding.crdbtnplusminus.visibility = View.GONE
                holder.binding.outofstocks.visibility = View.VISIBLE
            }
        } else {

        }

        holder.binding.textcount.text = productlists.Quantity!!.toInt().toString()
        counts = productlists.Quantity!!.toInt()
        holder.binding.txtratingvalues.text = productlists.ProductRating.toString()
//        Picasso.get().load(productlists.ProductThumbnail).into(holder.binding.imgthumbnail)
        Glide.with(requireActivity).load(productlists.ProductThumbnail)
            .into(holder.binding.imgthumbnail)
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

//            counts=1
            holder.binding.textcount.text = counts.toString()
            onCartClickedHandler!!.setondeleteclicklistener(
                position,
                productlists.CartId,
                productlists.UserId
            )

            progressDialog.dismiss()
            notifyItemChanged(position)
        }

        holder.binding.imgadd.setOnClickListener {
            progressDialog.show()
            progressDialog.setCancelable(false)
            counts = counts + 1
            if (counts > 1) {
//                Toast.makeText(requireActivity, "Product Added to Cart", Toast.LENGTH_SHORT).show()
                DesignToast.makeText(
                    requireActivity,
                    "Product Added to Cart",
                    Toast.LENGTH_SHORT,
                    DesignToast.TYPE_SUCCESS
                ).show()
                holder.binding.textcount.text = counts.toString()
                holder.binding.imgremove.isEnabled = true
                holder.binding.imgremove.isClickable = true
                onCartClickedHandler!!.setonaddclicklistener(
                    position,
                    productlists.ProductId!!.toInt(),
                    1, holder.binding.imgadd
                )
                progressDialog.dismiss()
            } else if (counts <= productlists.MaximumBuyQuantity!!.toInt()) {
                holder.binding.imgadd.isClickable = false
                progressDialog.dismiss()
            } else {
                counts = 1
                holder.binding.textcount.text = counts.toString()
            }

            progressDialog.dismiss()
//                viewProductModel.getAddProductInCart(counts, productlists.ProductId!!.toInt(), 20)

        }

        holder.binding.imgremove.setOnClickListener {
            progressDialog.show()
            progressDialog.setCancelable(false)
            if (counts == 1) {
                counts = counts - 1
                counts = 1
                holder.binding.textcount.text = "1"
                holder.binding.imgremove.isEnabled = false
//                onCartClickedHandler!!.setonaddclicklistener(
//                    position,
//                    productlists.ProductId!!.toInt(),
//                    -1, holder.binding.imgremove
//                )
                progressDialog.dismiss()
            } else if (counts < 1) {
                counts = 1
                holder.binding.textcount.text = "1"
                holder.binding.imgremove.isEnabled = false
                progressDialog.dismiss()
            } else if (counts > 1) {
//                Toast.makeText(requireActivity, "Product Removed from Cart", Toast.LENGTH_SHORT)
//                    .show()
                DesignToast.makeText(
                    requireActivity,
                    "Product Removed from Cart",
                    Toast.LENGTH_SHORT,
                    DesignToast.TYPE_ERROR
                ).show()
                counts = counts - 1
                holder.binding.textcount.text = counts.toString()
                onCartClickedHandler!!.setonaddclicklistener(
                    position,
                    productlists.ProductId!!.toInt(),
                    -1, holder.binding.imgremove
                )
                progressDialog.dismiss()
            } else {
                counts = 1
                holder.binding.imgremove.isEnabled = false
                holder.binding.imgremove.isClickable = false
                holder.binding.imgremove.isEnabled = true
                holder.binding.imgadd.isClickable = true
            }
            if (counts <= productlists.MaximumBuyQuantity!!.toInt()) {
                holder.binding.imgadd.isClickable = true
                progressDialog.dismiss()
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