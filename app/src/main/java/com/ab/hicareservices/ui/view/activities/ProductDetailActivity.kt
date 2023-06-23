package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityProductDetailBinding
import com.ab.hicareservices.ui.adapter.ProductDetailAdapter
import com.ab.hicareservices.ui.viewmodel.ProductViewModel

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProductDetailBinding
    var productid:String?=null
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid:String?=null
    var pincode:String?=null
    private lateinit var mAdapter:ProductDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        binding= ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent= intent
        productid = intent.getStringExtra("productid").toString()

        customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()



        getlist()

    }

    private fun getlist() {

        binding.recyleproductdetails.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = ProductDetailAdapter()

        binding.recyleproductdetails.adapter = mAdapter

        viewProductModel.producDetailsResponse.observe(this, Observer {
            Toast.makeText(this,it.IsSuccess.toString(),Toast.LENGTH_LONG).show()
            mAdapter.setPrductdetail(it.Data!!.ProductGallery,this)
        })

        viewProductModel.getProductDetails(productid!!.toInt(), "400601", customerid!!.toInt())

    }
}