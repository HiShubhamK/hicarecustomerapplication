package com.ab.hicareservices.ui.view.fragments

import android.R.id
import android.R.id.tabs
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentOrdersNewBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.adapter.ViewPagerAdapter
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.view.activities.OrderSummeryFragment
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel
import org.json.JSONObject


class OrdersFragmentNew() : Fragment() {
    private val TAG = "OrdersFragmentNew"
    lateinit var binding: FragmentOrdersNewBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    private var ordertype = ""
    lateinit var homeActivity: HomeActivity
    lateinit var orderactivityforadapter: FragmentActivity
    lateinit var options: JSONObject
    lateinit var progressDialog: ProgressDialog




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersNewBinding.inflate(inflater, container, false)
        //viewModel = ViewModelProvider(requireActivity(), ViewModelFactory(MainRepository(api))).get(OrdersViewModel::class.java)
        mobile = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "-1").toString()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrdersFragmentNew().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        requireActivity().onBackPressedDispatcher.addCallback(requireActivity()) {
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(
//                    R.id.container, HomeFragment.newInstance()
//                ).addToBackStack("OrdersFragment").commit()
//        }

        //setting height and width of progressBar

        progressDialog = ProgressDialog(requireActivity(), R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        setupViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private fun setupViewPager() {

        val orderfragment = OrdersFragment()
        val extraList = OrderSummeryFragment()
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(orderfragment, "Services")
        adapter.addFragment(extraList, "Order Summary")

        val viewPager = binding.vpFragments
        viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.vpFragments)
    }




}
