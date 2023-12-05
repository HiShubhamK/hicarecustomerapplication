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
import com.ab.hicareservices.databinding.LayoutServiceAddressListBinding
import com.ab.hicareservices.ui.handler.onAddressClickedHandler
import com.ab.hicareservices.ui.view.activities.AddressActivity
import com.ab.hicareservices.ui.view.activities.ServicesAddresslistActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.SharedPreferencesManager
import com.ab.hicareservices.utils.UserData


class ServiceAddressAdapter : RecyclerView.Adapter<ServiceAddressAdapter.MainViewHolder>() {

    var addressData = mutableListOf<ExistingCustomerAddressData>()
    lateinit var requireActivity:FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    var selectedPosition = -1
    var shipping=""
    private var onAddressClickedHandler: onAddressClickedHandler? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = LayoutServiceAddressListBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cutomeraddressdata=addressData[position]


            holder.binding.radiobuttons.setChecked(position == selectedPosition)

            if (cutomeraddressdata.IsDeafultAddress == true) {
                holder.binding.addresscard.setBackgroundColor(Color.parseColor("#F6F6F6"))
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
                AppUtils2.addresscode=cutomeraddressdata.Id.toString()
                AppUtils2.cutomerid=cutomeraddressdata.UserId.toString()

                SharedPreferenceUtil.setData(requireActivity, "Fname", "")
                SharedPreferenceUtil.setData(requireActivity, "Lname", "")
                SharedPreferenceUtil.setData(requireActivity, "Email", "")
                SharedPreferenceUtil.setData(requireActivity, "Pincode", "")
                SharedPreferenceUtil.setData(requireActivity, "MobileNo", "")
                SharedPreferenceUtil.setData(requireActivity, "AltMobileNo", "")
                SharedPreferenceUtil.setData(requireActivity, "FlatNo", "")
                SharedPreferenceUtil.setData(requireActivity, "Landmark", "")
                SharedPreferenceUtil.setData(requireActivity, "Locality", "")
                SharedPreferenceUtil.setData(requireActivity, "State", "")
                SharedPreferenceUtil.setData(requireActivity, "Street", "")
                SharedPreferenceUtil.setData(requireActivity, "City", "")
                SharedPreferenceUtil.setData(requireActivity, "BuildingName", "")
                SharedPreferenceUtil.setData(requireActivity, "Lat", "")
                SharedPreferenceUtil.setData(requireActivity, "Long", "")
                SharedPreferenceUtil.setData(requireActivity, "Address_Id", "")


                SharedPreferenceUtil.setData(requireActivity, "Fname", cutomeraddressdata.CustomerName.toString())
                SharedPreferenceUtil.setData(requireActivity, "Lname", cutomeraddressdata.LastName.toString())
                SharedPreferenceUtil.setData(requireActivity, "Email", cutomeraddressdata.EmailId.toString())
                SharedPreferenceUtil.setData(requireActivity, "Pincode", cutomeraddressdata.Pincode.toString())
                SharedPreferenceUtil.setData(requireActivity, "MobileNo", cutomeraddressdata.MobileNo.toString())
                SharedPreferenceUtil.setData(requireActivity, "AltMobileNo", cutomeraddressdata.AltMobileNo.toString())
                SharedPreferenceUtil.setData(requireActivity, "FlatNo", cutomeraddressdata.FlatNo.toString())
                SharedPreferenceUtil.setData(requireActivity, "Landmark", cutomeraddressdata.Landmark.toString())
                SharedPreferenceUtil.setData(requireActivity, "Locality", cutomeraddressdata.Locality.toString())
                SharedPreferenceUtil.setData(requireActivity, "State", cutomeraddressdata.State.toString())
                SharedPreferenceUtil.setData(requireActivity, "Street", cutomeraddressdata.Street.toString())
                SharedPreferenceUtil.setData(requireActivity, "City", cutomeraddressdata.City.toString())
                SharedPreferenceUtil.setData(requireActivity, "BuildingName", cutomeraddressdata.BuildingName.toString())
                SharedPreferenceUtil.setData(requireActivity, "Lat", cutomeraddressdata.Lat.toString())
                SharedPreferenceUtil.setData(requireActivity, "Long", cutomeraddressdata.Long.toString())
                SharedPreferenceUtil.setData(requireActivity, "Address_Id", cutomeraddressdata.Id!!.toInt())



                val userData = UserData()
                userData.Fname=cutomeraddressdata.CustomerName.toString()
                userData.LastName="."
                userData.Email=cutomeraddressdata.EmailId.toString()
                userData.Pincode=cutomeraddressdata.Pincode.toString()
                userData.MobileNo=cutomeraddressdata.MobileNo.toString()
                userData.AltMobileNo=cutomeraddressdata.AltMobileNo.toString()
                userData.FlatNo=cutomeraddressdata.FlatNo.toString()
                userData.Landmark=cutomeraddressdata.Landmark.toString()
                userData.Locality=cutomeraddressdata.Locality.toString()
                userData.State=cutomeraddressdata.State.toString()
                userData.Street=cutomeraddressdata.Street.toString()
                userData.City=cutomeraddressdata.City.toString()
                userData.BuildingName=cutomeraddressdata.BuildingName.toString()
                userData.Lat=cutomeraddressdata.Lat.toString()
                userData.Long=cutomeraddressdata.Long.toString()
                userData.Address_Id= cutomeraddressdata.Id!!.toInt()
                SharedPreferencesManager(requireActivity).saveUserData(userData)

                AppUtils2.bookingserviceaddress=cutomeraddressdata.FlatNo + "," + cutomeraddressdata.BuildingName+","+
                                                cutomeraddressdata.Street + "," + cutomeraddressdata.Locality + "," +
                                                cutomeraddressdata.Landmark + "," + cutomeraddressdata.City + "," +
                                                cutomeraddressdata.State + "-" + cutomeraddressdata.Pincode


                onAddressClickedHandler!!.setItemClickLister(
                    position,
                    cutomeraddressdata.Id,
                    true,
                    cutomeraddressdata.Pincode.toString()
                )
            }

            holder.binding.txtAddressHead1.text = cutomeraddressdata.FlatNo + "," + cutomeraddressdata.BuildingName+", "+cutomeraddressdata.Street + "," + cutomeraddressdata.Locality + "," +
                    cutomeraddressdata.Landmark + "," + cutomeraddressdata.City + "," +
                    cutomeraddressdata.State + "-" + cutomeraddressdata.Pincode


            holder.binding.txtUsername.text = cutomeraddressdata.CustomerName

        if (cutomeraddressdata.MobileNo!!.isNotEmpty()){
            holder.binding.txtphone.text = cutomeraddressdata.MobileNo
            holder.binding.txtphone.visibility=View.VISIBLE


        }else{
            holder.binding.txtphone.visibility=View.GONE
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
        this.requireActivity=addressActivity
        notifyDataSetChanged()
    }

    fun setOnAddressItemClicked(l: onAddressClickedHandler) {
        onAddressClickedHandler = l
    }

    class MainViewHolder(val binding: LayoutServiceAddressListBinding) : RecyclerView.ViewHolder(binding.root)

}