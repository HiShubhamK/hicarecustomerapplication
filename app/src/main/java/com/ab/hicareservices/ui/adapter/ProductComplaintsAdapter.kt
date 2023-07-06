package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.productcomplaint.ProductComplaintData
import com.ab.hicareservices.databinding.LayoutComplaintsAdaptersBinding
import com.ab.hicareservices.ui.view.activities.ProductComplaintDetailsActivity
import com.ab.hicareservices.utils.AppUtils2


class ProductComplaintsAdapter(requireActivity: FragmentActivity) : RecyclerView.Adapter<ProductComplaintsAdapter.MainViewHolder>() {

    var complaints = mutableListOf<ProductComplaintData>()
    var imageList = ArrayList<String>()
    val requireActivity=requireActivity
//    private lateinit var requireActivity: FragmentActivity

    fun setComplaintsList(
        complaintdata: List<ProductComplaintData>,
        imageList: ArrayList<String>,
        requireActivity: FragmentActivity
    ) {
        if (complaintdata != null) {
            this.complaints = complaintdata.toMutableList()
            this.imageList = imageList
//            this.requireActivity = requireActivity
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = LayoutComplaintsAdaptersBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        try {
            val complaints = complaints[position]
            holder.binding.txthello.text = complaints.ProductDisplayName
            holder.binding.txtStatus.text = complaints.ComplaintStatus
            holder.binding.tvCaseNumber.text =
                AppUtils2.formatDateTime4(complaints.CreatedOn.toString())


            holder.binding.txtCreationDate.text =
                "Case Number:- " + complaints.OrderNumber.toString()
            if (complaints.ComplaintStatus.equals("Active")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#D50000"))

            } else if (complaints.ComplaintStatus.equals("Pending")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#FB8C00"))

            } else if (complaints.ComplaintStatus.equals("Resolved") || complaints.ComplaintStatus.equals("Closed")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#2bb77a"))

            } else {

            }
//            imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/cb8b73d2-da3c-4ce6-a172-ae774063d915.jpg")
            holder.itemView.setOnClickListener {

                try {
                    val intent = Intent(requireActivity, ProductComplaintDetailsActivity::class.java)
                    intent.putExtra(
                        "Dateformat",
                        AppUtils2.formatDateTime4(complaints.CreatedOn.toString())
                    )
                    intent.putExtra("ComplaintNo", complaints.OrderId.toString())
                    intent.putExtra("OrderNO", complaints.OrderNumber.toString())
                    intent.putExtra("Pest", "Pest")
                    intent.putExtra("Cdescription", complaints.ComplaintDescription.toString())
                    intent.putExtra("ServicePlan", complaints.ProductName.toString())
                    intent.putExtra("Subject", complaints.ComplaintSubject.toString())
                    intent.putExtra("Description", complaints.ComplaintDescription.toString())
                    intent.putExtra("status", holder.binding.txtStatus.text.toString())
                    intent.putExtra("CaseNo", complaints.OrderValuePostDiscount.toString())
                    intent.putStringArrayListExtra("Imagelist", complaints.AttachmentList)
                    intent.putExtra("Complaintid", complaints.ProductId.toString())
                    requireActivity.startActivity(intent)
                }catch (e:Exception){
                    e.printStackTrace()
                }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun getItemCount(): Int {
        return complaints.size
    }

    class MainViewHolder(val binding: LayoutComplaintsAdaptersBinding) :
        RecyclerView.ViewHolder(binding.root)
}