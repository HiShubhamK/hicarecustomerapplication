package com.ab.hicareservices.ui.view.activities

import android.app.AlertDialog
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
import com.ab.hicareservices.databinding.FragmentProductBinding
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.handler.OnOrderClickedHandler
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val viewProductModel: ProductViewModel by viewModels()
    var customerid: String? = null
    var pincode: String? = null
    private lateinit var mAdapter: ProductAdapter

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
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
//        return inflater.inflate(R.layout.fragment_product, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customerid = SharedPreferenceUtil.getData(requireActivity(), "customerid", "").toString()
        pincode = SharedPreferenceUtil.getData(requireActivity(), "pincode", "").toString()


        viewProductModel.productcount.observe(requireActivity(), Observer {
            if (it.IsSuccess==true){

                if(it.Data==0){
                    binding.cartmenu.visibility=View.GONE
                }else{
                    binding.cartmenu.visibility=View.VISIBLE
                    binding.appCompatImageViewd.text=it.Data.toString()
                }
            }else{
                binding.cartmenu.visibility=View.GONE
            }
        })

        viewProductModel.getProductCountInCar(customerid!!.toInt())

        if(pincode!=null){
              getProductslist(pincode!!)
        }else{
            showalertDailogbox()
        }

        binding.cartmenu.setOnClickListener{
            val intent= Intent(requireActivity(),AddToCartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getProductslist(pincode: String) {

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = ProductAdapter()

        binding.recycleviewproduct.adapter = mAdapter

        viewProductModel.productlist.observe(requireActivity(), Observer {

            mAdapter.setProductList(it, requireActivity(),viewProductModel)

        })

        mAdapter.setOnOrderItemClicked(object : OnProductClickedHandler{
            override fun onProductClickedHandler(position: Int, productid: Int) {

                viewProductModel.getAddProductInCart(1,productid,customerid!!.toInt())

                viewProductModel.productcount.observe(requireActivity(), Observer {
                    if (it.IsSuccess==true){
                        binding.cartmenu.visibility=View.VISIBLE
                        binding.appCompatImageViewd.text=it.Data.toString()
                    }else{
                        binding.cartmenu.visibility=View.GONE
                    }
                })

                viewProductModel.getProductCountInCar(customerid!!.toInt())

            }
        })

        viewProductModel.getProductlist("400078")
    }

    override fun onResume() {
        super.onResume()
        viewProductModel.productcount.observe(requireActivity(), Observer {
            if (it.IsSuccess==true){

                if(it.Data==0){
                    binding.cartmenu.visibility=View.GONE
                }else{
                    binding.cartmenu.visibility=View.VISIBLE
                    AppUtils2.cartcounts=it.Data.toString()
                    binding.appCompatImageViewd.text=it.Data.toString()
                }
            }else{
                binding.cartmenu.visibility=View.GONE
            }
        })

        viewProductModel.getProductCountInCar(customerid!!.toInt())
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
        val button=promptsView.findViewById<View>(R.id.btnpincode) as Button
        alertDialog.show()
        alertDialog.setCancelable(false)

        button.setOnClickListener {

            if(edtpincode.text.trim().toString().equals("")){
                Toast.makeText(requireActivity(),"Please enter pincode",Toast.LENGTH_LONG).show()
            }else if(edtpincode.text.trim().toString().equals("000000")){
                Toast.makeText(requireActivity(),"Please enter correct pincode",Toast.LENGTH_LONG).show()
            }else{
                var pincode=edtpincode.text.trim().toString()
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