package com.ab.hicareservices.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicareservices.data.model.product.CustomerAddressData
import com.ab.hicareservices.databinding.LayoutAddressBinding
import com.ab.hicareservices.ui.view.activities.AddressActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel


class AddressAdapter : RecyclerView.Adapter<AddressAdapter.MainViewHolder>() {

    var productlist = mutableListOf<CustomerAddressData>()
    lateinit var requireActivity:FragmentActivity
    lateinit var viewProductModel: ProductViewModel
    var selectedPosition = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = LayoutAddressBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cutomeraddressdata=productlist[position]
        holder.binding.radiobuttons.setChecked(position == selectedPosition)

        holder.binding.radiobuttons.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                selectedPosition=holder.adapterPosition
                notifyDataSetChanged()
            }
        }

        holder.binding.txtusername.text=cutomeraddressdata.Id.toString()
        holder.binding.txtusername.text=cutomeraddressdata.FlatNo+","+cutomeraddressdata.BuildingName+","+
                cutomeraddressdata.Street+","+cutomeraddressdata.FlatNo+","+cutomeraddressdata.Locality+","+
                cutomeraddressdata.Landmark+","+cutomeraddressdata.City+","+
                cutomeraddressdata.State+","+cutomeraddressdata.Pincode
        holder.binding.txtphoneno.text=cutomeraddressdata.ContactPersonMobile

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
        return productlist.size
    }

    fun setAddressList(productlist: List<CustomerAddressData>?, addressActivity: AddressActivity, viewProductModel: ProductViewModel) {
        this.productlist= productlist!!.toMutableList()
        notifyDataSetChanged()
    }


    class MainViewHolder(val binding: LayoutAddressBinding) : RecyclerView.ViewHolder(binding.root)

}