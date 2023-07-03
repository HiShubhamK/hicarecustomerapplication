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
import com.ab.hicareservices.data.model.product.CartlistResponseData
import com.ab.hicareservices.databinding.ActivityOverviewProductDetailsBinding
import com.ab.hicareservices.ui.adapter.AddressAdapter
import com.ab.hicareservices.ui.adapter.OverviewDetailAdapter
import com.ab.hicareservices.ui.handler.onAddressClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2

class OverviewProductDetailsActivity : AppCompatActivity() {

    var billdata = ""
    var shipdaata = ""
    private lateinit var binding: ActivityOverviewProductDetailsBinding
    private val viewProductModel: ProductViewModel by viewModels()
    private lateinit var mAdapter: OverviewDetailAdapter
    var shippingdata: String? = ""
    var billingdata: String? = ""
    lateinit var datalist: ArrayList<CartlistResponseData>
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_product_details)
        binding = ActivityOverviewProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog =
            ProgressDialog(this, com.ab.hicareservices.R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        shippingdata = SharedPreferenceUtil.getData(this, "Shippingdata", "").toString()
        billingdata = SharedPreferenceUtil.getData(this, "Billingdata", "").toString()
        AppUtils2.pincode = SharedPreferenceUtil.getData(this, "pincode", "").toString()
        AppUtils2.customerid = SharedPreferenceUtil.getData(this, "customerid", "").toString()

        val intent = intent
        billdata = intent.getStringExtra("Billdata").toString()
        shipdaata = intent.getStringExtra("Shipdata").toString()

        getproductlist()
        getSummarydata()
        getAddressList()
        getAddressforbilling()
        if (!AppUtils2.pincode.isNullOrEmpty()){
            binding.tvPincode.text="Deliver to pincode "+AppUtils2.pincode
        }else {
            binding.tvPincode.visibility=View.GONE
        }

        binding.txtplcaeorder.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("Product", true)
            startActivity(intent)
        }
    }

    private fun getAddressforbilling() {
        progressDialog.show()
        viewProductModel.getaddressbydetailid.observe(this, Observer {
            progressDialog.dismiss()
            binding.txtbilling.visibility = View.VISIBLE
            binding.txtbilling.text =
                it.FlatNo.toString() + "," + it.BuildingName.toString() + "," + it.Street.toString() + "," +
                        it.Locality.toString() + "," + it.Landmark.toString() + "," + it.Pincode.toString()
        })
        viewProductModel.getAddressDetailbyId(billdata!!.toInt())
    }


    private fun getAddressList() {

        progressDialog.show()

        viewProductModel.cutomeraddress.observe(this, Observer {

            progressDialog.dismiss()

            for (i in 0 until it.size) {
                var data = it.get(i).Id.toString()
                if (data.equals(shipdaata)) {
                    AppUtils2.cutomername = it.get(i).ContactPersonName.toString()
                    AppUtils2.customermobile = it.get(i).ContactPersonMobile.toString()
                    AppUtils2.customeremail = it.get(i).ContactPersonEmail.toString()
                    binding.txtshipping.text =
                        it.get(i).FlatNo.toString() + "," + it.get(i).BuildingName.toString() + "," + it.get(
                            i
                        ).Street.toString() + "," +
                                it.get(i).Locality.toString() + "," + it.get(i).Landmark.toString() + "," + it.get(
                            i
                        ).City.toString() + "," + it.get(i).State.toString() + "," + it.get(i).Pincode.toString()
                }
//                else if(data.equals(billdata)){
//                    binding.txtbilling.text=it.get(i).FlatNo.toString()+","+it.get(i).BuildingName.toString()+","+it.get(i).Street.toString()+","+
//                            it.get(i).Locality.toString()+","+it.get(i).Landmark.toString()+","+it.get(i).City.toString()+","+it.get(i).State.toString()+","+it.get(i).Pincode.toString()
//                }
                else {

                }
            }
        })

        viewProductModel.getCustomerAddress(AppUtils2.customerid.toInt())
    }


    private fun getproductlist() {

        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mAdapter = OverviewDetailAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.cartlist.observe(this, Observer {

            progressDialog.dismiss()

            AppUtils2.leaderlist = it as ArrayList<CartlistResponseData>
//             datalist= it as ArrayList<CartlistResponseData>
//            Toast.makeText(this,AppUtils2.leaderlist.size.toString(),Toast.LENGTH_LONG).show()
            mAdapter.setCartList(it, this, viewProductModel)

        })

        viewProductModel.getProductCartByUserId(AppUtils2.customerid.toInt())
    }

    fun getSummarydata() {

        progressDialog.show()

        viewProductModel.getsummarydata.observe(this, Observer {


            AppUtils2.productamount=it.FinalAmount.toString()
            AppUtils2.actualvalue=it.TotalAmount.toString()
            AppUtils2.totaldiscount=it.TotalDiscount.toString()
            binding.txtfinaltext.text="\u20B9" + it.FinalAmount.toString()
            binding.txttotoalvalue.text = "\u20B9" + it.TotalAmount.toString()
            binding.txtdiscount.text = "\u20B9" + it.TotalDiscount.toString()
            binding.txttoalamount.text = "\u20B9" + it.FinalAmount.toString()
            if (it.DeliveryCharges!=null&&it.DeliveryCharges==0){
                binding.tvDeliveryCharge.text="Free Delivery"
            }else{
                binding.tvDeliveryCharge.text=it.DeliveryCharges.toString()
            }
            progressDialog.dismiss()

        })

        viewProductModel.getCartSummary(AppUtils2.customerid.toInt(), AppUtils2.pincode, "")

    }

    override fun onResume() {
        super.onResume()
        getSummarydata()
    }

}