package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.ui.handler.SpinnerItemSelectedListener
import com.squareup.picasso.Picasso

class CustomSpinnerProductAdapter(private val context: Context,private val items: MutableList<OrderSummeryData>) : BaseAdapter() {

    var itemSelectedListener: SpinnerItemSelectedListener? = null

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)

        val servicename = view.findViewById<TextView>(R.id.servicename)
        val serviceorders = view.findViewById<TextView>(R.id.serviceorders)
        val serviceaddress = view.findViewById<TextView>(R.id.serviceaddress)
        val serviceimageview = view.findViewById<ImageView>(R.id.serviceimageview)
        val cardviewselection= view.findViewById<CardView>(R.id.cardviewselection)
        serviceaddress.visibility=View.GONE
        servicename.text=items[position].ProductName.toString()
        serviceorders.text=items[position].OrderNumber.toString()
        Picasso.get().load(items[position].ProductThumbnail).into(serviceimageview)



        serviceimageview.setOnClickListener {
            itemSelectedListener?.onItemSelected(position,items[position].ProductDisplayName.toString(),
                items[position].OrderDate.toString(),
                items[position].ProductId.toString(),
                items[position].OrderNumber.toString(),
                items[position].Id.toString(),
                items[position].OrderDate.toString(),
                items[position].OrderValuePostDiscount.toString(),
                items[position].OrderStatus.toString())
        }

        return view
    }

    fun setOnOrderItemClicked(l: SpinnerItemSelectedListener) {
        itemSelectedListener = l
    }

}