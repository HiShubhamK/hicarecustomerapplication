package com.ab.hicareservices.ui.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.dashboard.BannerData
import com.ab.hicareservices.data.model.dashboard.OfferData
import com.ab.hicareservices.data.model.product.ProductTestimonialList
import com.ab.hicareservices.data.model.product.RelatedProducts
import com.ab.hicareservices.ui.handler.OffersInterface
import com.ab.hicareservices.ui.handler.offerinterface
import com.ab.hicareservices.utils.AppUtils2


class ProductDetailCustomerReviewAdapter(private val viewpa: ViewPager2, private val activity: FragmentActivity) :
    RecyclerView.Adapter<ProductDetailCustomerReviewAdapter.ImageViewHolder>() {
    private var offersInterface: offerinterface? = null

    var ProductTestimonialList = mutableListOf<ProductTestimonialList>()

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtratingvalues: TextView = itemView.findViewById(R.id.txtratingvalues)
        val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvCustomercity: TextView = itemView.findViewById(R.id.tvCustomercity)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val ratingbar: androidx.appcompat.widget.AppCompatRatingBar = itemView.findViewById(R.id.ratingbar)
    }
    fun setProductReview(ProductTestimonialListtt: ArrayList<ProductTestimonialList>){
        this.ProductTestimonialList=ProductTestimonialListtt
        notifyDataSetChanged()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product_customer_review_adapter, parent, false)
        return ImageViewHolder(view)
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        try {
//        Picasso.get().load(imageList[position].courseImg).into(holder.imgOffer)
//        holder.imgOffer.setAnimation(ProductTestimonialList[position].courseImg)


//        Glide.with(this).load(imageList[position].courseImg)).into(holder.imgOffer)

            holder.txtratingvalues.text = ProductTestimonialList[position].Rating?.toFloat().toString()
            holder.tvCustomerName.text = ProductTestimonialList[position].TempCustName.toString()
            holder.tvCustomercity.text = " ("+ProductTestimonialList[position].TempCustCity.toString()+")"
            holder.tvDate.text =AppUtils2.formatDateTime4(ProductTestimonialList[position].CreatedOn.toString())
            holder.tvTitle.text = ProductTestimonialList[position].Title.toString()
            holder.tvDesc.text = ProductTestimonialList[position].Description.toString()
            holder.ratingbar.rating = ProductTestimonialList[position].Rating!!.toFloat()
            val drawable: Drawable = holder.ratingbar.progressDrawable
            drawable.setColorFilter(Color.parseColor("#FFEA00"), PorterDuff.Mode.SRC_ATOP)
            if (position == ProductTestimonialList.size - 1) {
                viewpa.post(runnable)
            }
          
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return ProductTestimonialList.size
    }

    private val runnable = Runnable {
        ProductTestimonialList.addAll(ProductTestimonialList)
        notifyDataSetChanged()
    }
    fun setOnOfferClick(l: offerinterface) {
        offersInterface = l
    }

}