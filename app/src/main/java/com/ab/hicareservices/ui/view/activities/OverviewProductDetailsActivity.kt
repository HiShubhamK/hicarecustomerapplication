package com.ab.hicareservices.ui.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityOverviewProductDetailsBinding
import com.ab.hicareservices.ui.adapter.AddressAdapter
import com.ab.hicareservices.ui.adapter.OverviewDetailAdapter
import com.ab.hicareservices.ui.handler.onAddressClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel

class OverviewProductDetailsActivity : AppCompatActivity() {

    var billdata=""
    var shipdaata=""
    private lateinit var binding: ActivityOverviewProductDetailsBinding
    private val viewProductModel: ProductViewModel by viewModels()
    private lateinit var mAdapter: OverviewDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_product_details)
        binding=ActivityOverviewProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        billdata = intent.getStringExtra("Billdata").toString()
        shipdaata = intent.getStringExtra("Shipdata").toString()


        getproductlist()
        getSummarydata()
        getAddressList()

        Toast.makeText(this,billdata+"  "+ shipdaata,Toast.LENGTH_LONG).show()

    }

    private fun getAddressList() {

        viewProductModel.cutomeraddress.observe(this, Observer {

            for (i in 0 until it.size){
                var data=it.get(i).Id
                if(data!!.equals(billdata)){
                    Toast.makeText(this,"billingdata "+it.get(i).Id.toString(),Toast.LENGTH_LONG).show()
                }else if(data!!.equals(shipdaata)){
                    Toast.makeText(this,"shippingdata "+data.toString(),Toast.LENGTH_LONG).show()
                }else{

                }
            }
        })

        viewProductModel.getCustomerAddress(20)
    }



    private fun getproductlist() {

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mAdapter = OverviewDetailAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.cartlist.observe(this, Observer {

            mAdapter.setCartList(it, this, viewProductModel)

        })

//        viewProductModel.getProductCartByUserId(customerid!!.toInt())
        viewProductModel.getProductCartByUserId(20)
    }

    fun getSummarydata() {

        viewProductModel.getsummarydata.observe(this, Observer {

            binding.txtfinaltext.text="\u20B9" + it.FinalAmount.toString()

        })

//        viewProductModel.getCartSummary(customerid!!.toInt(),"400078", "")

        viewProductModel.getCartSummary(20,"400078", "")

    }

    override fun onResume() {
        super.onResume()
        getSummarydata()
    }

}