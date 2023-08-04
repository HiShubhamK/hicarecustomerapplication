package com.ab.hicareservices.ui.view.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.product.ProductListResponseData
import com.ab.hicareservices.databinding.FragmentOrderSummeryBinding
import com.ab.hicareservices.databinding.FragmentProductBinding
import com.ab.hicareservices.ui.adapter.OrderSummeryAdapter
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2

class OrderSummeryFragment : Fragment() {

    private lateinit var binding: FragmentOrderSummeryBinding
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = ""
    var pincode: String? = ""
    private lateinit var mAdapter: OrderSummeryAdapter
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrderSummeryBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_product, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppUtils2.customerid = SharedPreferenceUtil.getData(requireActivity(), "customerid", "").toString()
//        pincode = SharedPreferenceUtil.getData(requireActivity(), "pincode", "").toString()
////
//
        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        getProductsSummerylist()

    }

    private fun getProductsSummerylist() {

        progressDialog.show()
        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = OrderSummeryAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.responseMessage.observe(requireActivity(), Observer {
            Toast.makeText(requireActivity(),it.toString(),Toast.LENGTH_LONG).show()
        })

        viewProductModel.getordersummeryList.observe(requireActivity(), Observer {


            mAdapter.setProductList(it, requireActivity(), viewProductModel)

        })

        viewProductModel.getOrderSummeryList(AppUtils2.customerid.toInt())

        mAdapter.setOnOrderItemClicked(object : OnProductClickedHandler {
            override fun onProductClickedHandler(position: Int, productid: Int) {

            }

            override fun onProductView(position: Int, ordersummerydata: OrderSummeryData) {
                try {
                    if (ordersummerydata != null) {
                        val intent =
                            Intent(requireActivity(), ProductSummaryDetailActivity::class.java)
                        intent.putExtra("OrderId", ordersummerydata.Id.toString())
                        intent.putExtra("ProductId", ordersummerydata.ProductId.toString())
                        intent.putExtra("CustomerId", ordersummerydata.CustomerId.toString())
                        intent.putExtra("Pincode", ordersummerydata.Pincode.toString())
                        intent.putExtra("ProductDisplayName", ordersummerydata.ProductDisplayName.toString())
                        intent.putExtra("orderNo", ordersummerydata.OrderNumber.toString())
                        intent.putExtra("OrderDate", ordersummerydata.OrderDate.toString())
                        intent.putExtra("OrderStatus", ordersummerydata.OrderStatus.toString())
                        intent.putExtra("OrderValue", ordersummerydata.OrderValue.toString())
                        intent.putExtra("Discount", ordersummerydata.Discount.toString())
                        intent.putExtra(
                            "OrderValuePostDiscount",
                            ordersummerydata.OrderValuePostDiscount.toString()
                        )
                        intent.putExtra("Tax", ordersummerydata.Tax.toString())
                        intent.putExtra(
                            "ShippingCharge",
                            ordersummerydata.ShippingCharge.toString()
                        )
                        intent.putExtra(
                            "InstallationCharge",
                            ordersummerydata.InstallationCharge.toString()
                        )
                        intent.putExtra(
                            "ProductThumbnail",
                            ordersummerydata.ProductThumbnail.toString()
                        )
                        intent.putExtra("Quantity", ordersummerydata.Quantity.toString())
                        intent.putExtra("PaymentId", ordersummerydata.PaymentId.toString())
                        intent.putExtra("PaymentStatus", ordersummerydata.PaymentStatus.toString())
                        intent.putExtra("PaymentMethod", ordersummerydata.PaymentMethod.toString())
                        intent.putExtra("PaymentMethod", ordersummerydata.PaymentMethod.toString())
                        intent.putExtra(
                            "Address",
                            ordersummerydata.FlatNo.toString() + ", " + ordersummerydata.BuildingName.toString() + ", " + ordersummerydata.Locality + ", " + ordersummerydata.Landmark + ", " + ordersummerydata.Street + ", " + ordersummerydata.City + "-" + ordersummerydata.Pincode
                        )

                        startActivity(intent)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onNotifyMeclick(position: Int, productid: ProductListResponseData) {
            }
        })

        progressDialog.dismiss()

    }

    override fun onResume() {
        super.onResume()
//        viewProductModel.productcount.observe(requireActivity(), Observer {
//            if (it.IsSuccess==true){
//
//                if(it.Data==0){
//                    binding.cartmenu.visibility=View.GONE
//                }else{
//                    binding.cartmenu.visibility=View.VISIBLE
//                    AppUtils2.cartcounts=it.Data.toString()
//                    binding.appCompatImageViewd.text=it.Data.toString()
//                }
//            }else{
//                binding.cartmenu.visibility=View.GONE
//            }
//        })
//
////        viewProductModel.getProductCountInCar(customerid!!.toInt())
//        viewProductModel.getProductCountInCar(20)

    }


    companion object {
        @JvmStatic
        fun newInstance() =
            OrderSummeryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}