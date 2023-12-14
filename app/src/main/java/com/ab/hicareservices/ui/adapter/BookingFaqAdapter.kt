package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.FaqList
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.databinding.LayoutFaqBinding
import java.util.ArrayList

class BookingFaqAdapter: RecyclerView.Adapter<BookingFaqAdapter.MainViewHolder>() {
    var productDetails = mutableListOf<FaqList>()
    lateinit var productDetailActivity: FragmentActivity


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutFaqBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val productlistdata = productDetails[position]

        holder.binding.tvQuetion.text=productlistdata.FAQTitle
        holder.binding.tvDesc.text=productlistdata.FAQDetail
        holder.binding.tvShowAnswer.setOnClickListener{


            val animation = AnimationUtils.loadAnimation(productDetailActivity, R.anim.slide_up)
            val animation2 = AnimationUtils.loadAnimation(productDetailActivity, R.anim.slide_down)


            if (holder.binding.crdDetail.isVisible){

                holder.binding.crdDetail.startAnimation(animation2)


//                holder.binding.crdDetail.animate().apply {
//                    scaleX(1.5f)
//                    scaleY(1.5f)
//                    duration = 500 // Adjust the duration as needed
//                    start()
//                }

                holder.binding.tvShowAnswer.text="+"
                holder.binding.crdDetail.visibility= View.GONE

            }else{
//                holder.binding.crdDetail.animate().apply {
//                    scaleX(1.0f)
//                    scaleY(1.0f)
//                    duration = 500
//                    start()
//                }
                holder.binding.crdDetail.startAnimation(animation2)
                holder.binding.tvShowAnswer.text="-"
                holder.binding.crdDetail.visibility= View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return productDetails.size
    }
    fun setFaq(
        productDetails: ArrayList<FaqList>?,
        productDetailActivity: FragmentActivity
    ) {
        this.productDetails = productDetails!!
        this.productDetailActivity = productDetailActivity
        notifyDataSetChanged()
    }

    class MainViewHolder(val binding: LayoutFaqBinding) :
        RecyclerView.ViewHolder(binding.root)
}
