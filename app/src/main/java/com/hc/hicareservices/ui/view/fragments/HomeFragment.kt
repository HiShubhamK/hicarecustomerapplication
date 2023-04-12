package com.hc.hicareservices.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.hc.hicareservices.ui.adapter.FanbookAdapter
import com.hc.hicareservices.ui.adapter.HomeServiceAdapter
import com.hc.hicareservices.ui.adapter.ServiceAdapter
import com.hc.hicareservices.databinding.FragmentHomeBinding
import com.hc.hicareservices.utils.AppUtils2
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdapter: ServiceAdapter
    private lateinit var mHomeAdapter: HomeServiceAdapter
    private lateinit var mFanAdapter: FanbookAdapter

    val TAG = HomeFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        //19.1019646,72.9342336
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.horizontalScrollView.post {
            binding.horizontalScrollView.scrollTo(0,0)
        }
        binding.pestControl.setOnClickListener {
            AppUtils2.startPayment(requireActivity())
        }
        setHomeBanner()
        getServiceData()
    }


    private fun setHomeBanner() {
        try {
            val slideModels: MutableList<SlideModel> = ArrayList()
            slideModels.add(SlideModel("https://image.shutterstock.com/image-vector/pest-control-house-hygiene-disinfection-600w-1422131480.jpg", ScaleTypes.FIT))
            slideModels.add(SlideModel("https://image.shutterstock.com/image-photo/lablebutton-termite-protection-600w-772644724.jpg", ScaleTypes.FIT))
            slideModels.add(SlideModel("https://image.shutterstock.com/image-vector/pest-control-services-banner-people-600w-1758538019.jpg", ScaleTypes.FIT))
            binding.slider.setImageList(slideModels, ScaleTypes.FIT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getServiceData() {
        binding.recyclerPest.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = ServiceAdapter()

        binding.recyclerHome.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mFanAdapter = FanbookAdapter()


        binding.recyclerPest.adapter = mAdapter
        binding.recyclerHome.adapter = mFanAdapter
    }
}