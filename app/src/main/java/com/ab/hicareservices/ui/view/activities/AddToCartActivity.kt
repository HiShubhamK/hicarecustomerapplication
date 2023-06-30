package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)
        binding = ActivityAddToCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()
        AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        getproductlist()
        getSummarydata()
        binding.txtplcaeorder.setOnClickListener{
            val intent= Intent(this,AddressActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getproductlist() {

        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = CartAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.cartlist.observe(this, Observer {

            if(it!=null) {
                mAdapter.setCartList(it, this, viewProductModel)
                progressDialog.dismiss()
            }else{
                progressDialog.dismiss()
                binding.cardviewprice.visibility= View.GONE
                binding.lnrbuttoncart.visibility=View.GONE
            }
        })

        mAdapter.setOnOrderItemClicked(object : onCartClickedHandler {
            override fun setondeleteclicklistener(position: Int, cartId: Int?, userId: Int?) {
                progressDialog.show()
                viewProductModel.getDeleteProductCart.observe(this@AddToCartActivity, Observer {
                    if(it.IsSuccess==true){
                        progressDialog.dismiss()
                        AppUtils2.cartcounts=""
                        val intent=intent
                        finish()
                        startActivity(intent)
                    }
                })
                viewProductModel.getDeleteProductCart(cartId!!.toInt(), AppUtils2.customerid.toInt())

            }

            override fun setonaddclicklistener(position: Int, productid: Int, i: Int) {

                progressDialog.show()

                viewProductModel.addtocart.observe(this@AddToCartActivity, Observer {
                    progressDialog.dismiss()
                    if(it.IsSuccess==true){
                        progressDialog.dismiss()
                        getSummarydata()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this@AddToCartActivity,"Something went to wromg",Toast.LENGTH_LONG).show()
                    }

                })

                viewProductModel.getAddProductInCart(i, productid, AppUtils2.customerid.toInt())
            }

        })

//        viewProductModel.getProductCartByUserId(customerid!!.toInt())
        viewProductModel.getProductCartByUserId(AppUtils2.customerid.toInt())
    }

    fun getSummarydata() {

        progressDialog.show()

        viewProductModel.getsummarydata.observe(this, Observer {

            progressDialog.dismiss()

            if(it.TotalAmount!=0) {

                binding.txttotoalvalue.text = "\u20B9" + it.TotalAmount.toString()
                binding.txtdiscount.text = "\u20B9" + it.TotalDiscount.toString()
                binding.txttoalamount.text = "\u20B9" + it.FinalAmount.toString()
                binding.txtfinaltext.text = "\u20B9" + it.FinalAmount.toString()

            }else{
                binding.cardviewprice.visibility=View.GONE
            }
        })

        viewProductModel.getCartSummary(AppUtils2.customerid.toInt(), AppUtils2.pincode, "")

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onResume() {
        super.onResume()
        getSummarydata()
    }


}