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
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.databinding.FragmentProductBinding
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = ""
    var pincode: String? = ""
    private lateinit var mAdapter: ProductAdapter
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
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AppUtils2.customerid =
            SharedPreferenceUtil.getData(requireActivity(), "customerid", "").toString()
        AppUtils2.pincode =
            SharedPreferenceUtil.getData(requireActivity(), "pincode", "").toString()

        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        viewProductModel.productcount.observe(requireActivity(), Observer {
            progressDialog.show()
            if (it.IsSuccess == true) {

                progressDialog.dismiss()
                if (it.Data == 0) {
                    binding.cartmenu.visibility = View.GONE
                } else {
                    binding.cartmenu.visibility = View.VISIBLE
                    binding.appCompatImageViewd.text = it.Data.toString()
                }
            } else {
                progressDialog.dismiss()
                binding.cartmenu.visibility = View.GONE
            }
        })

        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())

        binding.getpincodetext.setText(AppUtils2.pincode)

        if (AppUtils2.pincode.equals("")) {
            Toast.makeText(requireActivity(),"please enter correct pincode",Toast.LENGTH_LONG).show()
        } else {
            getProductslist(AppUtils2.pincode!!)
        }

        binding.imgsearch.setOnClickListener{
            if(binding.getpincodetext.text.equals("")){
                Toast.makeText(requireActivity(),"please enter correct pincode",Toast.LENGTH_LONG).show()
            } else{
                SharedPreferenceUtil.setData(requireActivity(), "pincode",binding.getpincodetext.text.toString())
                getProductslist(binding.getpincodetext.text.toString())
            }

        }

        binding.cartmenu.setOnClickListener {
            val intent = Intent(requireActivity(), AddToCartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getProductslist(pincode: String) {

        progressDialog.show()

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = ProductAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.productlist.observe(requireActivity(), Observer {
            progressDialog.dismiss()

            if (it!=null){
                binding.recycleviewproduct.visibility=View.VISIBLE
                binding.textnotfound.visibility=View.GONE
                mAdapter.setProductList(it, requireActivity(), viewProductModel)
            }else {
                binding.recycleviewproduct.visibility=View.GONE
                binding.textnotfound.visibility=View.VISIBLE
            }


        })

        viewProductModel.errorMessage.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
        })

        viewProductModel.getProductlist(AppUtils2.pincode)

        mAdapter.setOnOrderItemClicked(object : OnProductClickedHandler {
            override fun onProductClickedHandler(position: Int, productid: Int) {

                progressDialog.show()

                viewProductModel.getAddProductInCart(1, productid, AppUtils2.customerid.toInt())

                viewProductModel.productcount.observe(requireActivity(), Observer {
                    progressDialog.dismiss()
                    if (it.IsSuccess == true) {
                        binding.cartmenu.visibility = View.VISIBLE
                        binding.appCompatImageViewd.text = it.Data.toString()
                    } else {
                        binding.cartmenu.visibility = View.GONE
                    }
                })


                viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())

            }

            override fun onProductView(position: Int, productid: OrderSummeryData) {

            }
        })


    }

    override fun onResume() {
        super.onResume()
        viewProductModel.productcount.observe(requireActivity(), Observer {
            if (it.IsSuccess == true) {

                if (it.Data == 0) {
                    binding.cartmenu.visibility = View.GONE
                } else {
                    binding.cartmenu.visibility = View.VISIBLE
                    AppUtils2.cartcounts = it.Data.toString()
                    binding.appCompatImageViewd.text = it.Data.toString()
                }
            } else {
                binding.cartmenu.visibility = View.GONE
            }
        })

        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())
//        viewProductModel.getProductCountInCar(20)

    }

    private fun showalertDailogbox() {
        var selectedLocation = ""
        var dateTime = ""
        val li = LayoutInflater.from(requireActivity())
        val promptsView = li.inflate(R.layout.pincodelayout, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        val edtpincode = promptsView.findViewById<View>(R.id.edtpincode) as EditText
        val button = promptsView.findViewById<View>(R.id.btnpincode) as Button
        val cancel=promptsView.findViewById<View>(R.id.imgbtncancel)  as ImageView
        alertDialog.show()
        alertDialog.setCancelable(false)

        cancel.setOnClickListener {
            alertDialog.dismiss()
        }

        button.setOnClickListener {

            if (edtpincode.text.trim().toString().equals("")) {
                Toast.makeText(requireActivity(), "Please enter pincode", Toast.LENGTH_LONG).show()
            } else if (edtpincode.text.trim().toString().equals("000000")) {
                Toast.makeText(requireActivity(), "Please enter correct pincode", Toast.LENGTH_LONG)
                    .show()
            } else {
                var pincode = edtpincode.text.trim().toString()
                SharedPreferenceUtil.setData(requireActivity(), "pincode", pincode)
                alertDialog.dismiss()
                getProductslist(pincode)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProductFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}