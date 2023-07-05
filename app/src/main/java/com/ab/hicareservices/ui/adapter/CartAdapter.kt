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
    lateinit var progressDialogs: ProgressDialog

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

        progressDialogs = ProgressDialog(requireActivity, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        val productlists = productlist[position]

        if (AppUtils2.changebuttonstatus == false) {
            holder.binding.imgadd.isClickable = true
            holder.binding.imgremove.isClickable = true
            holder.binding.imgdelete.isClickable=true
        } else {
            holder.binding.imgadd.isClickable = false
            holder.binding.imgremove.isClickable = false
            holder.binding.imgdelete.isClickable=false
        }

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

        if (productlists.Quantity == 1) {
            holder.binding.imgremove.visibility = View.GONE
            holder.binding.imgdelete.visibility = View.VISIBLE
        } else if (productlists.Quantity!!.toInt() > 1) {
            holder.binding.imgremove.visibility = View.VISIBLE
            holder.binding.imgdelete.visibility = View.GONE
            holder.binding.textcount.text = productlists.Quantity!!.toString()
            holder.binding.textcount.text = productlists.Quantity.toString()
            counts = productlists.Quantity!!.toInt()
        }

        holder.binding.imgadd.setOnClickListener {
            if (counts == 1){
                holder.binding.imgremove.visibility = View.VISIBLE
                holder.binding.imgdelete.visibility = View.GONE
            }

            progressDialogs.show()
            holder.binding.imgadd.isClickable = false
            holder.binding.imgadd.isEnabled = false
            holder.binding.imgremove.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                if (counts > 1) {
                    holder.binding.imgremove.visibility = View.VISIBLE
                    counts = counts + 1
                    holder.binding.imgadd.isClickable = false
                    holder.binding.imgadd.isEnabled = false
                    holder.binding.textcount.text = counts.toString()
                    AppUtils2.changebuttonstatus = true
                    onCartClickedHandler!!.setonaddclicklistener(
                        position,
                        productlists.ProductId!!.toInt(),
                        1,
                        holder.binding.imgadd
                    )
                    holder.binding.textcount.text = counts.toString()
                    holder.binding.imgdelete.visibility = View.GONE
                    progressDialogs.dismiss()
                }else if (counts == 1) {
                    holder.binding.imgremove.visibility = View.VISIBLE
                    counts = counts + 1
                    holder.binding.imgadd.isClickable = false
                    holder.binding.imgadd.isEnabled = false
                    holder.binding.textcount.text = counts.toString()
                    AppUtils2.changebuttonstatus = true
                    onCartClickedHandler!!.setonaddclicklistener(
                        position,
                        productlists.ProductId!!.toInt(),
                        1,
                        holder.binding.imgadd
                    )
                    holder.binding.textcount.text = counts.toString()
                    holder.binding.imgdelete.visibility = View.GONE
                    progressDialogs.dismiss()
                } else if (counts <= productlists.MaximumBuyQuantity!!.toInt()) {
                    holder.binding.imgadd.isClickable = false
                    progressDialogs.dismiss()
                } else {
                    holder.binding.imgadd.isClickable = false
                    progressDialogs.dismiss()
                }

            }, 1000)


        }

        holder.binding.imgremove.setOnClickListener {
            progressDialogs.show()
            holder.binding.imgremove.isClickable = false
            holder.binding.imgremove.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                counts = counts - 1
                if (counts == 1) {
                    holder.binding.imgdelete.visibility = View.VISIBLE
                    holder.binding.imgremove.visibility = View.GONE
                    holder.binding.textcount.text = counts.toString()
                    AppUtils2.changebuttonstatus = true
                    onCartClickedHandler!!.setonaddclicklistener(
                        position,
                        productlists.ProductId!!.toInt(),
                        -1, holder.binding.imgremove
                    )
                    progressDialogs.dismiss()
                } else if (counts > 1) {
//                    counts = counts - 1
//                holder.binding.imgremove.isClickable = false
//                holder.binding.imgremove.isEnabled = false
                    holder.binding.textcount.text = counts.toString()
                    onCartClickedHandler!!.setonaddclicklistener(
                        position,
                        productlists.ProductId!!.toInt(),
                        -1,
                        holder.binding.imgremove,
                    )

                    AppUtils2.changebuttonstatus = true
                    progressDialogs.dismiss()
                } else {
                    progressDialogs.dismiss()
                }
                if (counts <= productlists.MaximumBuyQuantity!!.toInt()) {
                    holder.binding.imgadd.isClickable = false
                }
            }, 1000)
        }

        holder.binding.imgdelete.setOnClickListener {
            progressDialogs.dismiss()

            Handler(Looper.getMainLooper()).postDelayed({

                onCartClickedHandler!!.setondeleteclicklistener(
                    position,
                    productlists.CartId,
                    productlists.UserId
                )
                progressDialogs.dismiss()

            },1500)
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
        viewProductModel: ProductViewModel,
        progressDialog: ProgressDialog,
        changebuttonstatus: Boolean
    ) {
        this.requireActivity = addToCartActivity
        this.viewProductModel = viewProductModel
        this.progressDialog = progressDialog
        this.changebuttonstatus = changebuttonstatus
        if (productlist != null) {
            this.productlist = productlist.toMutableList()
            notifyDataSetChanged()
        }
    }
}