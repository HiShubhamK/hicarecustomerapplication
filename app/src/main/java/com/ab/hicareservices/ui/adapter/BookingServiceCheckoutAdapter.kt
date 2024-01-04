package com.ab.hicareservices.ui.adapter

import android.content.Context
import android.os.Looper
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.databinding.BookingCheckoutAdapterLayoutBinding
import com.ab.hicareservices.ui.handler.Checkoutinstruciton
import com.ab.hicareservices.utils.AppUtils2
import com.squareup.picasso.Picasso

class BookingServiceCheckoutAdapter :
    RecyclerView.Adapter<BookingServiceCheckoutAdapter.MainViewHolder>() {

    var plandata = mutableListOf<GetServicePlanResponseData>()
    lateinit var requireActivity: FragmentActivity
    private var checkoutinstrucitons: Checkoutinstruciton? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = BookingCheckoutAdapterLayoutBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return plandata.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val plan = plandata[position]
        holder.binding.planname.text = plan.ServicePlanName
        holder.binding.servicepriceplan.text = "" + "" + "\u20B9" + plan.Price.toString()
        holder.binding.txtdescription.text = Html.fromHtml(plan.ServiceInstructions)
        Picasso.get().load(plan.ServiceLogo).into(holder.binding.imglogo)

        SharedPreferenceUtil.setData(
            requireActivity,
            "Instructions",
            holder.binding.edtinstruction.text.toString()
        )

        holder.binding.edtinstruction.setText(
            SharedPreferenceUtil.getData(
                requireActivity,
                "Instructions",
                ""
            ).toString()
        )

        val animation = AnimationUtils.loadAnimation(requireActivity, R.anim.slide_up)
        val animation2 = AnimationUtils.loadAnimation(requireActivity, R.anim.slide_up)

//        binding.splashimg.startAnimation(animation)


        holder.binding.imgaddinstructoin.setOnClickListener {

            Log.d("Buttonclick", "1")
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                holder.binding.imgaddinstructoin.visibility = View.GONE
                holder.binding.imgremoveinstructoin.visibility = View.VISIBLE
                holder.binding.txtdescription.visibility = View.VISIBLE
                SharedPreferenceUtil.setData(
                    requireActivity,
                    "Instructions",
                    holder.binding.edtinstruction.text.toString()
                )

            }, 200)

//            holder.binding.txtdescription.startAnimation(animation2)

        }



        holder.binding.edtinstruction.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for your case
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val newText = charSequence?.toString() ?: ""
                checkoutinstrucitons?.onTextChanged(newText)

                SharedPreferenceUtil.setData(
                    requireActivity,
                    "Instructions",
                    ""
                )

                SharedPreferenceUtil.setData(
                    requireActivity,
                    "Instructions",
                    newText
                )

            }

            override fun afterTextChanged(editable: Editable?) {
                // Not needed for your case
            }
        })

        holder.binding.imgremoveinstructoin.setOnClickListener {

//            holder.binding.txtdescription.startAnimation(animation)
            holder.binding.imgremoveinstructoin.postDelayed({
                holder.binding.imgremoveinstructoin.visibility = View.GONE
                holder.binding.imgaddinstructoin.visibility = View.VISIBLE
//                holder.binding.edtinstruction.visibility = View.GONE
                holder.binding.txtdescription.visibility = View.GONE
            }, 200)

        }


        holder.binding.imgaddinstructoinforextra.setOnClickListener {
//            holder.binding.edtinstruction.startAnimation(animation2)
            holder.binding.imgaddinstructoinforextra.postDelayed({

                holder.binding.imgaddinstructoinforextra.visibility = View.GONE
                holder.binding.imgremoveinstructoinextra.visibility = View.VISIBLE
                holder.binding.edtinstruction.visibility = View.VISIBLE
//                AppUtils2.checkinstruction=holder.binding.edtinstruction.text.toString()
//                SharedPreferenceUtil.setData(
//                    requireActivity,
//                    "Instructions",
//                    holder.binding.edtinstruction.text.toString()
//                )
            }, 200)

            AppUtils2.checkinstruction=holder.binding.edtinstruction.text.toString()
            SharedPreferenceUtil.setData(
                requireActivity,
                "Instructions",
                holder.binding.edtinstruction.text.toString()
            )
        }

        holder.binding.imgremoveinstructoinextra.setOnClickListener {
//            holder.binding.edtinstruction.startAnimation(animation)
            holder.binding.imgremoveinstructoin.postDelayed({
                holder.binding.imgremoveinstructoinextra.visibility = View.GONE
                holder.binding.imgaddinstructoinforextra.visibility = View.VISIBLE
                holder.binding.edtinstruction.visibility = View.GONE
            }, 200)

        }
    }

    fun setServiceList(
        it: List<GetServicePlanResponseData>?,
        bokingServiceDetailsActivity: Context
    ) {
        if (it != null) {
            this.plandata = it.toMutableList()
            this.requireActivity = bokingServiceDetailsActivity as FragmentActivity
        }
        notifyDataSetChanged()
    }

    fun setTextChange(checkoutinstruciton: Checkoutinstruciton) {
        checkoutinstrucitons=checkoutinstruciton
    }


    class MainViewHolder(val binding: BookingCheckoutAdapterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {}

}
