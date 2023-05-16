package com.hc.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hc.hicareservices.databinding.LayoutFanbookAdapterBinding
import com.hc.hicareservices.databinding.LayoutPaymentDashboardAdapterBinding
import com.hc.hicareservices.ui.viewmodel.PaymentCardViewModel
import com.squareup.picasso.Picasso

class PaymentDashboardAdapter(paymentcardlist: List<PaymentCardViewModel>) : RecyclerView.Adapter<PaymentDashboardAdapter.MainViewHolder>() {

    var list = paymentcardlist


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutPaymentDashboardAdapterBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val recipe = list[position]
        holder.binding.ServiceName.text = recipe.title
        holder.binding.serviceDesc.text = recipe.description
        holder.binding.tvPayNow.text = recipe.ButtonTitle
        Picasso.get().load(recipe.courseImg).into( holder.binding.imgAvatar)
        if (recipe.isButtonCancel){
            holder.binding.imgClose.visibility=View.VISIBLE
        }else {
            holder.binding.imgClose.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MainViewHolder(val binding: LayoutPaymentDashboardAdapterBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}
