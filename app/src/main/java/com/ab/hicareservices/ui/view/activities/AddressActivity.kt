package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.model.product.CustomerAddressData
import com.ab.hicareservices.databinding.ActivityAddressBinding
import com.ab.hicareservices.ui.adapter.AddressAdapter
import com.ab.hicareservices.ui.viewmodel.ProductViewModel

class AddressActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAddressBinding
    private val viewProductModel: ProductViewModel  by viewModels()
    private lateinit var mAdapter: AddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        binding= ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getAddressList()

    }

    private fun getAddressList() {
        binding.recycleviewaddress.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = AddressAdapter()

        binding.recycleviewaddress.adapter = mAdapter

        viewProductModel.cutomeraddress.observe(this, Observer {

            mAdapter.setAddressList(it, this,viewProductModel)

        })

        viewProductModel.getCustomerAddress(1)
    }
}