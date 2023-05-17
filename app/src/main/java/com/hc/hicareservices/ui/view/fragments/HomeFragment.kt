package com.hc.hicareservices.ui.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.hc.hicareservices.R
import com.hc.hicareservices.databinding.FragmentHomeBinding
import com.hc.hicareservices.ui.adapter.DashboardMenuAdapter
import com.hc.hicareservices.ui.adapter.HomeServiceAdapter
import com.hc.hicareservices.ui.adapter.ImageAdapter
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
    private lateinit var handler : Handler
    private lateinit var imageList:ArrayList<String>
    private lateinit var adapter: ImageAdapter
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageLists: List<Int>

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
        init()
        binding.idViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 2000)
            }
        })
//        setHomeBanner()
        getServiceData()
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable , 2000)
    }

    private val runnable = Runnable {
        binding.idViewPager.currentItem = binding.idViewPager.currentItem + 1
    }

    private fun init() {

        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/6e3f5c3d-abdb-4158-b49a-d3e88d763851.jpg")
        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/cb8b73d2-da3c-4ce6-a172-ae774063d915.jpg")
        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/6796f0c8-0b67-48e2-884c-047f8991f7ce.jpg")

        adapter = ImageAdapter(imageList, binding.idViewPager)


        binding.idViewPager.adapter = adapter
//
//        binding.dotsIndicator.attachTo(binding.idViewPager)



//        binding.idViewPager.add


//        binding.idViewPager.addOnPageChangeListener(object : OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                for (i in 0 until dotscount) {
//                    dots[i]!!.setImageDrawable(
//                        ContextCompat.getDrawable(
//                            requireContext(),
//                            R.drawable.nonactive_dot
//                        )
//                    )
//                }
//                dots[position]!!.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.active_dot
//                    )
//                )
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {}
//        })
//
//


        binding.idViewPager.offscreenPageLimit = 3
        binding.idViewPager.clipToPadding = false
        binding.idViewPager.clipChildren = false
        binding.idViewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//        binding.dotsIndicator.attachTo(binding.idViewPager)


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

        paymentcardlist=paymentcardlist+PaymentCardViewModel("Termite 1 year","Kindly click Renewal button to renew your service",R.drawable.hicarelogo,"Renewal",false)
        paymentcardlist=paymentcardlist+PaymentCardViewModel("Termite 2 year","Kindly click Reschedule button to Reschedule your service",R.drawable.hicarelogo,"Reschedule",false)
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