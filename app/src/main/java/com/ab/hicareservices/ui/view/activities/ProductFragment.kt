package com.ab.hicareservices.ui.view.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.location.LocationListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.ab.hicareservices.location.MyLocationListener
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

        MyLocationListener(requireActivity())

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
            Toast.makeText(requireActivity(), "please enter correct pincode", Toast.LENGTH_LONG).show()
        } else {
            getProductslist(AppUtils2.pincode!!)
        }

        binding.imgsearch.setOnClickListener {
            if (binding.getpincodetext.text.equals("") || binding.getpincodetext.text.length != 6) {
                Toast.makeText(requireActivity(), "Please enter your pincode", Toast.LENGTH_LONG).show()
            } else if(binding.getpincodetext.text.toString().trim().length<6){
                Toast.makeText(requireActivity(), "Invalid pincode", Toast.LENGTH_LONG).show()
            }else{
                AppUtils2.pincode = binding.getpincodetext.text.trim().toString()
                SharedPreferenceUtil.setData(
                    requireActivity(),
                    "pincode",
                    binding.getpincodetext.text.toString()
                )
                getProductslist(binding.getpincodetext.text.trim().toString())
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

        Handler(Looper.getMainLooper()).postDelayed({

            viewProductModel.productlist.observe(requireActivity(), Observer {

                if (it != null) {

                    binding.recycleviewproduct.visibility = View.VISIBLE
                    binding.textnotfound.visibility = View.GONE
                    mAdapter.setProductList(it, requireActivity(), viewProductModel)
                    progressDialog.dismiss()

                } else {
                    binding.recycleviewproduct.visibility = View.GONE
                    binding.textnotfound.visibility = View.VISIBLE
                    progressDialog.dismiss()

                }

            })

            viewProductModel.errorMessage.observe(requireActivity(), Observer {
                progressDialog.dismiss()
                binding.recycleviewproduct.visibility = View.GONE
                Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_LONG).show()
            })

                viewProductModel.getProductlist(pincode)

            progressDialog.dismiss()

        }, 1500)

        mAdapter.setOnOrderItemClicked(object : OnProductClickedHandler {
            override fun onProductClickedHandler(position: Int, productid: Int) {

                progressDialog.show()

                Handler(Looper.getMainLooper()).postDelayed({

                    viewProductModel.addtocart.observe(requireActivity(), Observer {
                        if (it.IsSuccess == true) {
                            getproductcount()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Something went wrong! Unable to add product into cart",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })


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
                }, 1500)
            }

            override fun onProductView(position: Int, productid: OrderSummeryData) {

            }
        })
    }

    private fun getproductcount() {

        progressDialog.show()

        viewProductModel.productcount.observe(requireActivity(), Observer {
            progressDialog.dismiss()
            if (it.IsSuccess == true) {
                progressDialog.dismiss()
                binding.cartmenu.visibility = View.VISIBLE
                binding.appCompatImageViewd.text = it.Data.toString()
            } else {
                binding.cartmenu.visibility = View.GONE
            }
        })

        viewProductModel.getProductCountInCar(AppUtils2.customerid.toInt())
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


    companion object {
        @JvmStatic
        fun newInstance() =
            ProductFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}