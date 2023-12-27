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
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.databinding.LayoutOrderSummeryBinding
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso


class OrderSummeryAdapter() : RecyclerView.Adapter<OrderSummeryAdapter.MainViewHolder>(){

    var productlist = mutableListOf<OrderSummeryData>()
    lateinit var requireActivity:FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    private var onProductClickedHandler: OnProductClickedHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutOrderSummeryBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val productlists=productlist[position]

//        if(productlists.IsFeedbackSubmitted==true) {
            holder.binding.txtratingvalues.text=productlists.Quantity.toString()
            holder.binding.statusTv.text=productlists.OrderStatus.toString()
            holder.binding.statusTv.setTextColor(Color.parseColor("#2bb77a"))
//
            if(productlists.OrderStatus.equals("Cancelled")){
                holder.binding.statusTv.setTextColor(Color.parseColor("#ff9e9e9e"))
            }
//            if (productlists.OrderStatus.toString()=="Booked"){
//
//            }

            holder.binding.tvOrderdate.text=AppUtils2.formatDateTime4(productlists.OrderDate.toString())
            Picasso.get().load(productlists.ProductThumbnail).into(holder.binding.imgthumbnail)
            holder.binding.txtname.text = productlists.ProductName
            holder.binding.ratingbar.rating = productlists.Selectedrating!!.toFloat()
            val drawable: Drawable = holder.binding.ratingbar.progressDrawable
//            drawable.setColorFilter(Color.parseColor("#FFEA00"), PorterDuff.Mode.SRC_ATOP)
            drawable.setColorFilter(Color.parseColor("#fec348"), PorterDuff.Mode.SRC_ATOP)

            if (productlists.Discount!!.toInt()!=0) {
                holder.binding.txtdealodday.text ="Saved " +"\u20B9" + productlists.ItemDiscount.toString()
                holder.binding.txtprice.text = productlists.OrderValuePostDiscount.toString()
                holder.binding.txtpriceline.text = ""+"\u20B9" + productlists.OrderValue.toString()
                holder.binding.txtpriceline.paintFlags=holder.binding.txtpriceline.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.binding.txtprice.text = "\u20B9" + productlists.OrderValue.toString()
                holder.binding.txtpriceline.visibility= View.GONE
            }

            holder.binding.btnaddtocart.setOnClickListener {
                onProductClickedHandler?.onProductView(position,productlists)
//                viewProductModel.getAddProductInCart(1,productlists.ProductId!!.toInt(),20)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


//            holder.itemView.setOnClickListener {
//                val intent= Intent(requireActivity,ProductDetailActivity::class.java)
//                intent.putExtra("productid",productlists.ProductId.toString())
//                requireActivity.startActivity(intent)
//            }

//        }else{
//
//        }
    }

    override fun getItemCount(): Int {
        return productlist.size
    }

    fun setProductList(
        productlist: List<OrderSummeryData>?,
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

    class MainViewHolder(val binding: LayoutOrderSummeryBinding) :
        RecyclerView.ViewHolder(binding.root)

}