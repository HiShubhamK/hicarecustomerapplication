package com.ab.hicareservices.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.complaints.ComplaintsData
import com.ab.hicareservices.data.model.complaints.Data
import com.ab.hicareservices.databinding.LayoutComplaintsAdaptersBinding
import com.ab.hicareservices.ui.view.activities.ComplaintDetailsActivity
import com.ab.hicareservices.ui.view.activities.ComplaintsActivity
import com.ab.hicareservices.ui.view.fragments.ComplaintDetailsFragment
import com.ab.hicareservices.utils.AppUtils2


class ComplaintsAdapter(requireActivity: FragmentActivity) : RecyclerView.Adapter<ComplaintsAdapter.MainViewHolder>() {

    var complaints = mutableListOf<ComplaintsData>()
    var imageList = ArrayList<String>()
    val requireActivity=requireActivity
//    private lateinit var requireActivity: ComplaintsActivity

    fun setComplaintsList(
        complaintdata: List<ComplaintsData>?,
        imageList: ArrayList<String>) {
        if (complaintdata != null) {
            complaints = complaintdata.toMutableList()
            this.imageList = imageList
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
            holder.binding.txthello.text = complaints.ServicePlan_c
            holder.binding.txtStatus.text = complaints.Status
            holder.binding.tvCaseNumber.text =
                AppUtils2.formatDateTime4(complaints.CreatedDate.toString())


            holder.binding.txtCreationDate.text =
                "Case Number:- " + complaints.CaseNumber.toString()
            if (complaints.Status.equals("Open")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#D50000"))

            } else if (complaints.Status.equals("Pending")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#FB8C00"))

            } else if (complaints.Status.equals("Resolved") || complaints.Status.equals("Closed")) {

                holder.binding.txtStatus.setTextColor(Color.parseColor("#2bb77a"))

            } else {

            }
//            imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/cb8b73d2-da3c-4ce6-a172-ae774063d915.jpg")
            holder.itemView.setOnClickListener {

                try {

                    val intent = Intent(requireActivity, ComplaintDetailsActivity::class.java)
                    intent.putExtra("Dateformat", complaints.CreatedDate.toString())
                    intent.putExtra("ComplaintNo", complaints.ComplaintNo_c.toString())
                    intent.putExtra("OrderNO", complaints.OrderNo_c.toString())
                    intent.putExtra("Pest", "Pest")
                    intent.putExtra("Cdescription", complaints.ComplaintDescription_c.toString())
                    intent.putExtra("ServicePlan", complaints.ServicePlan_c.toString())
                    intent.putExtra("Subject", complaints.Subject.toString())
                    intent.putExtra("Description", complaints.Description.toString())
                    intent.putExtra("status", holder.binding.txtStatus.text.toString())
                    intent.putExtra("CaseNo", complaints.CaseNumber.toString())
                    intent.putStringArrayListExtra("Imagelist", imageList)
                    intent.putExtra("Complaintid", complaints.Id.toString())
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