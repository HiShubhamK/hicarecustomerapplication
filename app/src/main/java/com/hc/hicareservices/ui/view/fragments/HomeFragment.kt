package com.hc.hicareservices.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.hc.hicareservices.R
import com.hc.hicareservices.databinding.FragmentHomeBinding
import com.hc.hicareservices.ui.adapter.DashboardMenuAdapter
import com.hc.hicareservices.ui.adapter.HomeServiceAdapter
import com.hc.hicareservices.ui.adapter.PaymentDashboardAdapter
import com.hc.hicareservices.ui.viewmodel.GridViewModal
import com.hc.hicareservices.ui.viewmodel.PaymentCardViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdapter: DashboardMenuAdapter
    private lateinit var mHomeAdapter: HomeServiceAdapter
    private lateinit var mpayentdashboardadapter: PaymentDashboardAdapter
    lateinit var courseList: List<GridViewModal>
    lateinit var paymentcardlist: List<PaymentCardViewModel>
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


//        binding.recMenu.setOnClickListener {
//            AppUtils2.startPayment(requireActivity())
//        }
        setHomeBanner()
        getServiceData()
    }


    private fun setHomeBanner() {
        try {
            val slideModels: MutableList<SlideModel> = ArrayList()
            slideModels.add(SlideModel("https://s3.ap-south-1.amazonaws.com/hicare-others/6e3f5c3d-abdb-4158-b49a-d3e88d763851.jpg", ScaleTypes.FIT))
            slideModels.add(SlideModel("https://s3.ap-south-1.amazonaws.com/hicare-others/cb8b73d2-da3c-4ce6-a172-ae774063d915.jpg", ScaleTypes.FIT))
            slideModels.add(SlideModel("https://s3.ap-south-1.amazonaws.com/hicare-others/6796f0c8-0b67-48e2-884c-047f8991f7ce.jpg", ScaleTypes.FIT))
            binding.slider.setImageList(slideModels, ScaleTypes.FIT)
//            binding.slider.setScrollIndicators();

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getServiceData() {
        courseList = ArrayList<GridViewModal>()
        paymentcardlist = ArrayList<PaymentCardViewModel>()

        // on below line we are adding data to
        // our course list with image and course name.
        courseList = courseList + GridViewModal("My Orders", R.drawable.myorders)
        courseList = courseList + GridViewModal("Ask Expert", R.drawable.experts)
        courseList = courseList + GridViewModal("Renewal", R.drawable.orderrenew)
        courseList = courseList + GridViewModal("Wallet", R.drawable.digitalwallet)
        courseList = courseList + GridViewModal("Feedback", R.drawable.feedback)
        courseList = courseList + GridViewModal("Refer & Earn", R.drawable.referandearn)
        courseList = courseList + GridViewModal("Offers", R.drawable.offer)
//        courseList = courseList + GridViewModal("Redeem Now!", R.drawable.redeem)
        courseList = courseList + GridViewModal("Customer Support", R.drawable.customersuppoer)
//        courseList = courseList + GridViewModal("Raise Complaint", R.drawable.complain)
        courseList = courseList + GridViewModal("Upcoming Services", R.drawable.comingsoon)

        paymentcardlist=paymentcardlist+PaymentCardViewModel("Termite 1 year","Kindly click renew button to renew your order",R.drawable.hicarelogo,"Renew",false)
        paymentcardlist=paymentcardlist+PaymentCardViewModel("Termite 2 year","Kindly click view button to view your order details",R.drawable.hicarelogo,"View",false)
        paymentcardlist=paymentcardlist+PaymentCardViewModel("AutoMos","Kindly click paynow button to pay your order payment",R.drawable.hicarelogo,"Pay Now",false)


//        val courseAdapter = GridRVAdapter(courseList = courseList, LayoutInflater.from(getActivity()))

//        binding.recMenu.adapter=courseAdapter
//        binding.recMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
//            // inside on click method we are simply displaying
//            // a toast message with course name.
//            Toast.makeText(
//                context, courseList[position].courseName + " selected",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
        binding.recMenu.layoutManager =
            GridLayoutManager(context,4)
        mAdapter = DashboardMenuAdapter(courseList)

        binding.recPayments.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mpayentdashboardadapter = PaymentDashboardAdapter(paymentcardlist)


//        binding.recMenu.adapter = mAdapter
        binding.recMenu.adapter = mAdapter
        binding.recPayments.adapter = mpayentdashboardadapter
    }


}