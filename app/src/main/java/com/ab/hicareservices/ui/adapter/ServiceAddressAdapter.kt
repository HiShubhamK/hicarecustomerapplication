package com.ab.hicareservices.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.product.CustomerAddressData
import com.ab.hicareservices.data.model.servicesmodule.ExistingCustomerAddressData
import com.ab.hicareservices.databinding.LayoutAddressBinding
import com.ab.hicareservices.ui.handler.onAddressClickedHandler
import com.ab.hicareservices.ui.view.activities.AddressActivity
import com.ab.hicareservices.ui.view.activities.ServicesAddresslistActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2


class ServiceAddressAdapter : RecyclerView.Adapter<ServiceAddressAdapter.MainViewHolder>() {

    var addressData = mutableListOf<ExistingCustomerAddressData>()
    lateinit var requireActivity:FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    var selectedPosition = -1
    var shipping:String=""

    private var onAddressClickedHandler: onAddressClickedHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = LayoutAddressBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cutomeraddressdata=addressData[position]

        if(shipping.equals("true")) {

            holder.binding.radiobuttons.setChecked(position == selectedPosition)

            if (cutomeraddressdata.IsDefault == true) {
                holder.binding.addresscard.setCardBackgroundColor(Color.parseColor("#F6F6F6"))
            }

            holder.binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
                selectedPosition = holder.adapterPosition
                onAddressClickedHandler!!.setonaddclicklistener(position, cutomeraddressdata.Id,true)
            }

            holder.binding.radiobuttons.setOnCheckedChangeListener { compoundButton, b ->

                if (b) {

                    holder.binding.checkbox.visibility = View.VISIBLE
                    selectedPosition = holder.adapterPosition
                    onAddressClickedHandler!!.setradiobuttonclicklistern(position)

                }
            }

            holder.itemView.setOnClickListener {
                onAddressClickedHandler!!.setItemClickLister(
                    position,
                    cutomeraddressdata.Id,
                    true,
                    cutomeraddressdata.Pincode.toString()
                )
            }

            holder.binding.txtusername.text = cutomeraddressdata.Id.toString()
            holder.binding.txtusername.text =
                cutomeraddressdata.FlatNo + "," + cutomeraddressdata.BuildingName + "," +
                        cutomeraddressdata.Street + "," + cutomeraddressdata.Locality + "," +
                        cutomeraddressdata.Landmark + "," + cutomeraddressdata.City + "," +
                        cutomeraddressdata.State + "," + cutomeraddressdata.Pincode
            holder.binding.txtphoneno.text = "Phone No:" + cutomeraddressdata.MobileNo
        }else if(shipping.equals("false")){
            holder.binding.radiobuttons.setChecked(position == selectedPosition)

            holder.binding.radiobuttons.setOnCheckedChangeListener { compoundButton, b ->

                if (b) {

                    selectedPosition = holder.adapterPosition
                    onAddressClickedHandler!!.setradiobuttonclicklistern(position)

                }
            }

            holder.itemView.setOnClickListener {
                AppUtils2.pincode=cutomeraddressdata.Pincode.toString()
                SharedPreferenceUtil.setData(
                    requireActivity,
                    "pincode",
                    cutomeraddressdata.Pincode.toString()
                )
                val intent= Intent(requireActivity, AddressActivity::class.java)
                requireActivity.startActivity(intent)
                requireActivity.finish()
                onAddressClickedHandler!!.setItemClickLister(position, cutomeraddressdata.Id,false,cutomeraddressdata.Pincode.toString())
            }

            holder.binding.txtusername.text = cutomeraddressdata.Id.toString()
            holder.binding.txtusername.text =
                cutomeraddressdata.FlatNo + "," + cutomeraddressdata.BuildingName + "," +
                        cutomeraddressdata.Street + "," + cutomeraddressdata.Locality + "," +
                        cutomeraddressdata.Landmark + "," + cutomeraddressdata.City + "," +
                        cutomeraddressdata.State + "," + cutomeraddressdata.Pincode
            holder.binding.txtphoneno.text = "Phone No:" + cutomeraddressdata.MobileNo
        }
    }

    override fun getItemId(position: Int): Long {
        // pass position
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        // pass position
        return position
    }

    override fun getItemCount(): Int {
        return addressData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAddressList(
        productlist: List<ExistingCustomerAddressData>,
        addressActivity: ServicesAddresslistActivity,
        viewProductModel: ServiceBooking,
        shipping: String
    ) {
        this.shipping=shipping
        this.addressData= productlist!!.toMutableList()
        notifyDataSetChanged()
    }

    fun setOnAddressItemClicked(l: onAddressClickedHandler) {
        onAddressClickedHandler = l
    }

    class MainViewHolder(val binding: LayoutAddressBinding) : RecyclerView.ViewHolder(binding.root)

}