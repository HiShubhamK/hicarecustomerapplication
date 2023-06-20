package com.ab.hicareservices.ui.view.activities

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityUpcomingServicesBinding
import com.ab.hicareservices.databinding.FragmentOrdersBinding
import com.ab.hicareservices.databinding.FragmentProductBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.view.fragments.AccountFragment
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.utils.AppUtils2
import okio.ByteString.Companion.encode

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

        Toast.makeText(requireActivity(),"Customerid"+customerid +"  "+ "pincode"+ pincode,Toast.LENGTH_LONG).show()

        if(pincode!=null){
              getProductslist()
        }else{
            showalertDailogbox()
        }

    }

    private fun getProductslist() {

        binding.recycleviewproduct.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mAdapter = ProductAdapter()

        binding.recycleviewproduct.adapter = mAdapter


        viewProductModel.productlist.observe(requireActivity(), Observer {

            Toast.makeText(requireActivity(),it.toString(),Toast.LENGTH_LONG).show()

            mAdapter.setProductList(it, requireActivity())

        })

        viewProductModel.getProductlist("400601")

    }

    private fun showalertDailogbox() {
        var selectedLocation = ""
        var dateTime = ""
        val li = LayoutInflater.from(requireActivity())
        val promptsView = li.inflate(R.layout.pincodelayout, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
//        val edtname = promptsView.findViewById<View>(R.id.edtname) as EditText
//
//        alertDialog.setCancelable(false)

        alertDialog.show()
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