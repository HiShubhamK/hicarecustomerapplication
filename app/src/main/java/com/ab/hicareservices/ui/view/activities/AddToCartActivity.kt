package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityAddToCartBinding
import com.ab.hicareservices.ui.adapter.CartAdapter
import com.ab.hicareservices.ui.handler.onCartClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2

class AddToCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddToCartBinding
    private lateinit var mAdapter: CartAdapter
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = null
    var pincode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)
        binding = ActivityAddToCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()

        getproductlist()
        getSummarydata()

    }

    private fun getproductlist() {

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = CartAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.cartlist.observe(this, Observer {

            mAdapter.setCartList(it, this, viewProductModel)

        })

        mAdapter.setOnOrderItemClicked(object : onCartClickedHandler {
            override fun setondeleteclicklistener(position: Int, cartId: Int?, userId: Int?) {

                viewProductModel.getDeleteProductCart.observe(this@AddToCartActivity, Observer {
                    if(it.IsSuccess==true){
                        AppUtils2.cartcounts=""
                        val intent=intent
                        finish()
                        startActivity(intent)
                    }
                })

                viewProductModel.getDeleteProductCart(cartId!!.toInt(), 20)
//
//                getproductlist()
//                getSummarydata()
            }

        })

        viewProductModel.getProductCartByUserId(customerid!!.toInt())

    }

    fun getSummarydata() {

        viewProductModel.getsummarydata.observe(this, Observer {

            binding.txttotoalvalue.text ="\u20B9" + it.TotalAmount.toString()
            binding.txtdiscount.text ="\u20B9" + it.TotalDiscount.toString()
            binding.txttoalamount.text ="\u20B9" + it.FinalAmount.toString()

        })

        viewProductModel.getCartSummary(customerid!!.toInt(),"400078", "")

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

}