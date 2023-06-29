package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityOverviewProductDetailsBinding
import com.ab.hicareservices.ui.adapter.AddressAdapter
import com.ab.hicareservices.ui.adapter.OverviewDetailAdapter
import com.ab.hicareservices.ui.handler.onAddressClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2

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

        AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()
        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()

        val intent = intent
        billdata = intent.getStringExtra("Billdata").toString()
        shipdaata = intent.getStringExtra("Shipdata").toString()

        getproductlist()
        getSummarydata()
        getAddressList()

        binding.txtplcaeorder.setOnClickListener{
            val intent= Intent(this,PaymentActivity::class.java)
            intent.putExtra("Product",true)
            startActivity(intent)
        }


    }

    private fun getAddressListbilladdress(){
        viewProductModel.cutomeraddress.observe(this, Observer {

            for (i in 0 until it.size){
                var data=it.get(i).Id.toString()
                if(data.equals(shipdaata)){
                    binding.txtshipping.text=it.get(i).FlatNo.toString()+","+it.get(i).BuildingName.toString()+","+it.get(i).Street.toString()+","+
                            it.get(i).Locality.toString()+","+it.get(i).Landmark.toString()+","+it.get(i).City.toString()+","+it.get(i).State.toString()+","+it.get(i).Pincode.toString()
                }else if(data.equals(billdata)){
                    binding.txtshipping.text=it.get(i).FlatNo.toString()+","+it.get(i).BuildingName.toString()+","+it.get(i).Street.toString()+","+
                            it.get(i).Locality.toString()+","+it.get(i).Landmark.toString()+","+it.get(i).City.toString()+","+it.get(i).State.toString()+","+it.get(i).Pincode.toString()
                }else{

                }
            }
        })

        viewProductModel.getCustomerAddress(AppUtils2.customerid.toInt())
    }


    private fun getAddressList() {

        viewProductModel.cutomeraddress.observe(this, Observer {

            for (i in 0 until it.size){
                var data=it.get(i).Id.toString()
                if(data.equals(shipdaata)){
                    AppUtils2.cutomername=it.get(i).ContactPersonName.toString()
                    AppUtils2.customermobile=it.get(i).ContactPersonMobile.toString()
                    AppUtils2.customeremail=it.get(i).ContactPersonEmail.toString()
                    binding.txtshipping.text=it.get(i).FlatNo.toString()+","+it.get(i).BuildingName.toString()+","+it.get(i).Street.toString()+","+
                            it.get(i).Locality.toString()+","+it.get(i).Landmark.toString()+","+it.get(i).City.toString()+","+it.get(i).State.toString()+","+it.get(i).Pincode.toString()
                }else if(data.equals(billdata)){
                    binding.txtbilling.text=it.get(i).FlatNo.toString()+","+it.get(i).BuildingName.toString()+","+it.get(i).Street.toString()+","+
                            it.get(i).Locality.toString()+","+it.get(i).Landmark.toString()+","+it.get(i).City.toString()+","+it.get(i).State.toString()+","+it.get(i).Pincode.toString()
                }else{

                }
            }
        })

        viewProductModel.getCustomerAddress(AppUtils2.customerid.toInt())
    }



    private fun getproductlist() {

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mAdapter = OverviewDetailAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.cartlist.observe(this, Observer {

            mAdapter.setCartList(it, this, viewProductModel)

        })

        viewProductModel.getProductCartByUserId(AppUtils2.customerid.toInt())
    }

    fun getSummarydata() {

        viewProductModel.getsummarydata.observe(this, Observer {

            AppUtils2.productamount=it.FinalAmount.toString()
            binding.txtfinaltext.text="\u20B9" + it.FinalAmount.toString()

        })


        viewProductModel.getCartSummary(AppUtils2.customerid.toInt(),AppUtils2.pincode, "")

    }

    override fun onResume() {
        super.onResume()
        getSummarydata()
    }

}