package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityProductDetailBinding
import com.ab.hicareservices.ui.viewmodel.ProductViewModel

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProductDetailBinding
    var productid:String?=null
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid:String?=null
    var pincode:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        binding= ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent= intent
        productid = intent.getStringExtra("productid").toString()

        customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()

//        viewProductModel.getProductDetails(productid!!.toInt(), "400601", 10)

        viewProductModel.producDetailsResponse.observe(this, Observer {
            Toast.makeText(this,it.Data!!.ProductGallery.size,Toast.LENGTH_LONG).show()
        })

        viewProductModel.getProductDetails(productid!!.toInt(), "400601", customerid!!.toInt())

    }
}