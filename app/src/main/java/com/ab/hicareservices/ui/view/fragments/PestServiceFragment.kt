package com.ab.hicareservices.ui.view.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.ordersummery.OrderSummeryData
import com.ab.hicareservices.data.model.product.ProductListResponseData
import com.ab.hicareservices.databinding.ActivityPestServicesBinding
import com.ab.hicareservices.databinding.FragmentProductBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.BookingServiceListAdapter
import com.ab.hicareservices.ui.adapter.ProductAdapter
import com.ab.hicareservices.ui.handler.OnProductClickedHandler
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.viewmodel.ProductViewModel
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import java.text.SimpleDateFormat
import java.util.Date


class PestServiceFragment : Fragment() {
    private lateinit var binding: ActivityPestServicesBinding
    private lateinit var mAdapter: BookingServiceListAdapter
    private val viewProductModel: ServiceBooking by viewModels()

    lateinit var progressDialog: ProgressDialog

//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActivityPestServicesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyLocationListener(requireActivity())


        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        AppUtils2.pincode =
            SharedPreferenceUtil.getData(requireContext(), "pincode", "").toString()

        if (AppUtils2.pincode.equals("")) {
            binding.getpincodetext.setText("400080")
        } else {
            binding.getpincodetext.setText(AppUtils2.pincode)
        }

//        binding.imgLogo.setOnClickListener {
//            onBackPressed()
//        }

        binding.recMenu.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        mAdapter = BookingServiceListAdapter()
        binding.recMenu.adapter = mAdapter

        progressDialog.show()


        viewProductModel.errorMessage.observe(requireActivity(), Observer {

            viewProductModel.getActiveServiceList()

        })

        viewProductModel.serviceresponssedata.observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
                progressDialog.dismiss()
                mAdapter.setServiceList(it, requireActivity())
            } else {

            }
        })
        viewProductModel.getActiveServiceList()

        binding.imgsearch.setOnClickListener {

            SharedPreferenceUtil.setData(requireActivity(), "pincode", binding.getpincodetext.text.toString())
            AppUtils2.pincode = binding.getpincodetext.text.toString()

            if (binding.getpincodetext.text.length == 0) {
//                Toast.makeText(
//                    this@PestServicesActivity,
//                    "Please enter your pincode",
//                    Toast.LENGTH_LONG
//                ).show()


                DesignToast.makeText(requireActivity(), "Please enter your pincode", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();



            }else if(binding.getpincodetext.text.length != 6){
//                Toast.makeText(
//                    this@PestServicesActivity,
//                    "Please enter valid pincode",
//                    Toast.LENGTH_LONG
//                ).show()

                DesignToast.makeText(requireActivity(), "Please enter valid pincode", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();


            } else {

                progressDialog.show()

                Handler(Looper.getMainLooper()).postDelayed({

                    viewProductModel.errorMessage.observe(requireActivity(), Observer {

                        viewProductModel.getActiveServiceList()

                    })

                    viewProductModel.serviceresponssedata.observe(
                        requireActivity(),
                        Observer {
                            if (it.isNotEmpty()) {
                                progressDialog.dismiss()
                                mAdapter.setServiceList(it, requireActivity())
                            } else {

                            }
                            val imm =
                                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            // Check if there's a focused view before hiding the keyboard
                            if (this is Fragment && requireActivity().currentFocus != null) {
                                imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
                            }
                        })
                    viewProductModel.getActiveServiceList()

                }, 300)
            }

        }

    }





    override fun onResume() {
        super.onResume()


    }


    companion object {
        @JvmStatic
        fun newInstance() =
            PestServiceFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
//    fun onBackPressed() {
//        val intent = Intent(requireContext(), HomeActivity::class.java)
//        startActivity(intent)
//    }

}