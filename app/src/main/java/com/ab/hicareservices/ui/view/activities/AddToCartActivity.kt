package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityAddToCartBinding
import com.ab.hicareservices.databinding.ActivityProductDetailBinding
import com.ab.hicareservices.ui.adapter.CartAdapter
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel

class AddToCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddToCartBinding
    private lateinit var mAdapter: CartAdapter
    private val viewProductModel: ProductViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)
        binding=ActivityAddToCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = CartAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.productlist.observe(this, Observer {


            mAdapter.setCartList(it, this,viewProductModel)

        })


        viewProductModel.getProductlist("400601")

    }
}