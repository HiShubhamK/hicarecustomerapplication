package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityAddToCartBinding
import com.ab.hicareservices.ui.adapter.CartAdapter
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

        viewProductModel.cartlist.observe(this, Observer {


            mAdapter.setCartList(it, this,viewProductModel)

        })


        viewProductModel.getProductCartByUserId(20)

        viewProductModel.getCartSummary(20,"400601","")

    }
}