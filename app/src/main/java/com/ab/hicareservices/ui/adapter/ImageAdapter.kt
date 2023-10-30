package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.dashboard.BannerData
import com.ab.hicareservices.ui.view.activities.BookInspectionActivity
import com.ab.hicareservices.ui.view.activities.ComplaintsActivityNew
import com.ab.hicareservices.ui.view.activities.InAppWebViewActivity
import com.ab.hicareservices.ui.view.activities.MyOrderActivityNew
import com.ab.hicareservices.ui.view.activities.ProductDetailActivity
import com.squareup.picasso.Picasso


class ImageAdapter(
    private val viewPager2: ViewPager2,
    private val requireActivity: FragmentActivity,
    pincode: String
) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    var bannerLis = mutableListOf<BannerData>()
    var pincode = pincode
//   lateinit var requireActivity:FragmentActivity


    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)


    }
    fun serBanner(bannerListt: ArrayList<BannerData>){
        this.bannerLis=bannerListt
        notifyDataSetChanged()


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_container, parent, false)

        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        try {

            Picasso.get().load(bannerLis[position].ImageUrl).into(holder.imageView)
            if (position == bannerLis.size - 1) {
                viewPager2.post(runnable)
            }
            holder.itemView.setOnClickListener {
                if (bannerLis[position].IsExternalAppBrowserLink == true) {

                    requireActivity.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(bannerLis[position].PageLink)
                        )
                    )
                } else if (bannerLis[position].IsAppLink == true) {

                   var pagelink = bannerLis[position].PageLink.toString()

                    var pagelinkss=""
                    var pagelinkdigit=""

                    if(pagelink.startsWith("Product")) {

                        var delimiter = "|"

                        val parts = pagelink.split(delimiter)

                        pagelinkss = parts[0].toString()
                        pagelinkdigit=parts[1].toString()
                    }

                    if(bannerLis[position].PageLink.equals("BookInspectionActivity")){
                        val intent = Intent(requireActivity, BookInspectionActivity::class.java)
                        requireActivity!!.startActivity(intent)

                    }else if(bannerLis[position].PageLink.equals("ComplaintsActivityNew")){

                        val intent = Intent(requireActivity, ComplaintsActivityNew::class.java)
                        requireActivity!!.startActivity(intent)


                    }else if(pagelinkss.equals("ProductDetailActivity")){

                        val intent = Intent(requireActivity, ProductDetailActivity::class.java)
                        intent.putExtra("productid",pagelinkdigit)
                        intent.putExtra("pincode", pincode)
                        requireActivity!!.startActivity(intent)


                    }else if(bannerLis[position].PageLink.equals("MyOrderActivityNew")){

                        val intent = Intent(requireActivity, MyOrderActivityNew::class.java)
                        requireActivity!!.startActivity(intent)


                    }

                } else if (bannerLis[position].IsInAppBrowserLink == true) {
                    val intent = Intent(requireActivity, InAppWebViewActivity::class.java)
                    intent.putExtra("PageLink", bannerLis[position].PageLink)
                    requireActivity!!.startActivity(intent)

                } else {

                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return bannerLis.size
    }

    private val runnable = Runnable {
        bannerLis.addAll(bannerLis)
        notifyDataSetChanged()
    }
}