package com.ab.hicareservices.ui.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.orders.OrdersData
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso

class CustomSpinnerAdapter(private val context: Context, private val items: MutableList<OrdersData>) : BaseAdapter() {

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
        val createddate=view.findViewById<TextView>(R.id.createddate)
        createddate.text= AppUtils2.formatDateTime4(items[position].StartDate_c.toString())
        servicename.text=items[position].ServicePlanName_c.toString()
        serviceorders.text=items[position].OrderNumber_c.toString()
        serviceaddress.text=items[position].AccountName_r?.AccountAddress.toString()
        Picasso.get().load(items[position].ServicePlanImageUrl).into(serviceimageview)

        return view
    }
}
