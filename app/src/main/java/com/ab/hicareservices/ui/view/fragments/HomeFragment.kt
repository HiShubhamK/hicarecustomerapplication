package com.ab.hicareservices.ui.view.fragments

import android.R
import android.content.Intent
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.ViewPager2
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.dashboard.BannerData
import com.ab.hicareservices.data.model.dashboard.MenuData
import com.ab.hicareservices.databinding.FragmentHomeBinding
import com.ab.hicareservices.ui.adapter.*
import com.ab.hicareservices.ui.handler.onSlotclick
import com.ab.hicareservices.ui.viewmodel.*
import com.ab.hicareservices.utils.AppUtils2
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdapter: DashboardMenuAdapter
    private lateinit var mHomeAdapter: HomeServiceAdapter
    private lateinit var mpayentdashboardadapter: PaymentDashboardAdapter
//    private lateinit var mOfferAdapter: OffersAdapter
    lateinit var courseList: List<MenuData>
    lateinit var paymentcardlist: List<PaymentCardViewModel>
    val TAG = HomeFragment::class.java.simpleName
    private lateinit var handler: Handler
    private lateinit var handler2: Handler
    private lateinit var imageList: ArrayList<BannerData>
    private lateinit var offerlist: List<OfferViewModel>
    private lateinit var adapter: ImageAdapter
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageLists: List<Int>
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val viewModels: OtpViewModel by viewModels()



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
            binding.horizontalScrollView.scrollTo(0, 0)
        }


//        binding.recMenu.setOnClickListener {
//            AppUtils2.startPayment(requireActivity())
//        }
        init()
        binding.idViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })
//        setHomeBanner()
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable, 5000)
        handler2.postDelayed(runnable2, 5000)
    }

    private val runnable = Runnable {
//        binding.idViewPager.currentItem = binding.idViewPager.currentItem + 1
        binding.idViewPager.setCurrentItem(binding.idViewPager.currentItem + 1, true);

    }
    private val runnable2 = Runnable {
        binding.recOffers.setCurrentItem(binding.recOffers.currentItem + 1, true);

//        binding.recOffers.currentItem = binding.recOffers.currentItem + 1
    }

    private fun init() {
        AppUtils2.TOKEN = SharedPreferenceUtil.getData(requireContext(), "bToken", "").toString()
        AppUtils2.mobileno = SharedPreferenceUtil.getData(activity!!, "mobileNo", "-1").toString()
        viewModels.validateAccount(AppUtils2.mobileno)
        handler = Handler(Looper.myLooper()!!)
        handler2 = Handler(Looper.myLooper()!!)
        imageList = ArrayList()
        offerlist = ArrayList()
        courseList = ArrayList<MenuData>()


        dashboardViewModel.dashboardmain.observe(requireActivity(), Observer {
            Log.d(TAG, "onViewCreated: $it orders fragment")
            adapter.serBanner(it.BannerData)
            mAdapter.setServiceList(it.MenuData)
//            mOfferAdapter.setServiceList(it.OfferData)



//            binding.idViewPager.adapter = adapter
        })
        adapter = ImageAdapter(binding.idViewPager,requireActivity())

//        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/6e3f5c3d-abdb-4158-b49a-d3e88d763851.jpg")
//        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/cb8b73d2-da3c-4ce6-a172-ae774063d915.jpg")
//        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/6796f0c8-0b67-48e2-884c-047f8991f7ce.jpg")

//        adapter = ImageAdapter(imageList,binding.idViewPager)


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


        binding.recMenu.layoutManager =
            GridLayoutManager(context, 3)
        mAdapter = DashboardMenuAdapter(requireActivity())
        binding.recMenu.adapter = mAdapter

//        binding.dotsIndicator.attachTo(binding.idViewPager)

        Handler(Looper.getMainLooper()).postDelayed({
            dashboardViewModel.GetDashboard(AppUtils2.mobileno)
        }, 500)

    }


    private fun getServiceData(courseList2: ArrayList<MenuData>) {
        paymentcardlist = ArrayList<PaymentCardViewModel>()

        // on below line we are adding data to
        // our course list with image and course name.
//        courseList = courseList + GridViewModal("My Orders", R.drawable.myorders)
//        courseList = courseList + GridViewModal("Renewal", R.drawable.orderrenew)
//        courseList = courseList + GridViewModal("Wallet", R.drawable.digitalwallet)
//        courseList = courseList + GridViewModal("Offers", R.drawable.offer)
//        courseList = courseList + GridViewModal("Feedback", R.drawable.feedback)
//        courseList = courseList + GridViewModal("Ask Expert", R.drawable.experts)
//        courseList = courseList + GridViewModal("Refer & Earn", R.drawable.referandearn)
////        courseList = courseList + GridViewModal("Redeem Now!", R.drawable.redeem)
//        courseList = courseList + GridViewModal("Support", R.drawable.customersuppoer)
//        courseList = courseList + GridViewModal("Raise Complaint", R.drawable.complain)
//        courseList = courseList + GridViewModal("Upcoming Services", R.drawable.comingsoon)

        paymentcardlist = paymentcardlist + PaymentCardViewModel(
            "Termite 1 year",
            "Kindly click Renewal button to renew your service",
            com.ab.hicareservices.R.drawable.hicarelogo,
            "Renewal",
            false
        )
        paymentcardlist = paymentcardlist + PaymentCardViewModel(
            "Termite 2 year",
            "Kindly click Reschedule button to Reschedule your service",
            com.ab.hicareservices.R.drawable.hicarelogo,
            "Reschedule",
            false
        )
        paymentcardlist = paymentcardlist + PaymentCardViewModel(
            "AutoMos",
            "Kindly click paynow button to pay your order payment",
            com.ab.hicareservices.R.drawable.hicarelogo,
            "Pay Now",
            false
        )


//        offerlist = (offerlist + OfferViewModel(
//            "Upcoming Services",
//            R.raw.comingsoon
//        )) as ArrayList<OfferViewModel>
//        offerlist =
//            (offerlist + OfferViewModel("Offers", R.raw.animoffers)) as ArrayList<OfferViewModel>
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



        binding.recPayments.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mpayentdashboardadapter = PaymentDashboardAdapter(paymentcardlist)

//        binding.recOffers.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

//        mOfferAdapter = OffersAdapter(offerlist as ArrayList<OfferViewModel>,binding.recOffers)
//        binding.recOffers.adapter = mOfferAdapter
//        binding.recOffers.offscreenPageLimit = 2
//        binding.recOffers.clipToPadding = false
//        binding.recOffers.clipChildren = false
//        binding.recOffers.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//        binding.recOffers.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                handler2.removeCallbacks(runnable2)
//                handler2.postDelayed(runnable2, 5000)
//            }
//        })
//        mOfferAdapter.setOnOfferClick(object :onSlotclick{
//            override fun onSlotItemclicked(
//                position: Int,
//                Pincode: String,
//                Service_Code: String,
//                Service_Date: String,
//                Service_Subscription: String?,
//                Unit: String?,
//                Lat: String,
//                Long: String,
//                ServiceType: String,
//                scheduledate: String
//            ) {
//                TODO("Not yet implemented")
//            }
//
//        })
//        binding.recMenu.adapter = mAdapter
        binding.recPayments.adapter = mpayentdashboardadapter
        binding.recPayments.addItemDecoration(CirclePagerIndicatorDecoration())
//        binding.recPayments.smoothSnapToPosition()
        binding.recPayments.layoutManager!!.smoothScrollToPosition(
            binding.recPayments,
            RecyclerView.State(),
            binding.recPayments.adapter!!.itemCount
        )
//        mOfferAdapter.setOnOfferClick {
////            val modelBottomSheet =
////                LayoutInflater.from(requireContext())
////                    .inflate(com.ab.hicareservices.R.layout.layout_offer_detail_bottomsheet, null)
////            val dialog = BottomSheetDialog(requireContext())
////
////           val tvCoupen :TextView = modelBottomSheet.findViewById(com.ab.hicareservices.R.id.tvCoupen) as TextView
////            tvCoupen
////            dialog.setContentView(modelBottomSheet)
////            dialog.show()
//        }

        binding.lnrTwitter.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/hicare_services")
                )
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://twitter.com/hicare_services")
                    )
                )
            }
        }
        binding.lnrFacebook.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/HiCare?ref=hl")
                )
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/HiCare?ref=hl")
                    )
                )
            }
        }
        binding.lnrInsta.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/hicareservicespvtltd/?igshid=fkw5lb8qbj5a")
                )
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/hicareservicespvtltd/?igshid=fkw5lb8qbj5a")
                    )
                )
            }
        }

    }

    class CirclePagerIndicatorDecoration : ItemDecoration() {
        private val colorActive = 0x727272
        private val colorInactive = 0xF44336

        /**
         * Height of the space the indicator takes up at the bottom of the view.
         */
        private val mIndicatorHeight = (DP * 16).toInt()

        /**
         * Indicator stroke width.
         */
        private val mIndicatorStrokeWidth = DP * 2

        /**
         * Indicator width.
         */
        private val mIndicatorItemLength = DP * 16

        /**
         * Padding between indicators.
         */
        private val mIndicatorItemPadding = DP * 4

        /**
         * Some more natural animation interpolation
         */
        private val mInterpolator: AccelerateDecelerateInterpolator =
            AccelerateDecelerateInterpolator()
        private val mPaint: Paint = Paint()
        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDrawOver(c, parent, state)
            val itemCount = parent.adapter!!.itemCount

            // center horizontally, calculate width and subtract half from center
            val totalLength = mIndicatorItemLength * itemCount
            val paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding
            val indicatorTotalWidth = totalLength + paddingBetweenItems
            val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

            // center vertically in the allotted space
            val indicatorPosY = parent.height - mIndicatorHeight / 2f
            drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)


            // find active page (which should be highlighted)
            val layoutManager = parent.layoutManager as LinearLayoutManager?
            val activePosition = layoutManager!!.findFirstVisibleItemPosition()
            if (activePosition == RecyclerView.NO_POSITION) {
                return
            }

            // find offset of active page (if the user is scrolling)
            val activeChild = layoutManager.findViewByPosition(activePosition)
            val left = activeChild!!.left
            val width = activeChild.width

            // on swipe the active item will be positioned from [-width, 0]
            // interpolate offset for smooth animation
            val progress: Float = mInterpolator.getInterpolation(left * -1 / width.toFloat())
            drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
        }
//        fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
//            super.onDrawOver(c, parent, state!!)

//        }

        private fun drawInactiveIndicators(
            c: Canvas,
            indicatorStartX: Float,
            indicatorPosY: Float,
            itemCount: Int
        ) {
            mPaint.setColor(Color.GRAY)

            // width of item indicator including padding
            val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
            var start = indicatorStartX
            for (i in 0 until itemCount) {
                // draw the line for every item
                c.drawCircle(start + mIndicatorItemLength, indicatorPosY, itemWidth / 6, mPaint)
                //  c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
                start += itemWidth
            }
        }

        private fun drawHighlights(
            c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
            highlightPosition: Int, progress: Float, itemCount: Int
        ) {
            mPaint.color = Color.GREEN

            // width of item indicator including padding
            val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
            if (progress == 0f) {
                // no swipe, draw a normal indicator
                val highlightStart = indicatorStartX + itemWidth * highlightPosition
                /*   c.drawLine(highlightStart, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
        */c.drawCircle(highlightStart, indicatorPosY, itemWidth / 6, mPaint)
            } else {
                val highlightStart = indicatorStartX + itemWidth * highlightPosition
                // calculate partial highlight
                val partialLength = mIndicatorItemLength * progress
                c.drawCircle(
                    highlightStart + mIndicatorItemLength,
                    indicatorPosY,
                    itemWidth / 6,
                    mPaint
                )

                // draw the cut off highlight
                /* c.drawLine(highlightStart + partialLength, indicatorPosY,
                    highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
*/
                // draw the highlight overlapping to the next item as well
                /* if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth;
                */
                /*c.drawLine(highlightStart, indicatorPosY,
                        highlightStart + partialLength, indicatorPosY, mPaint);*/
                /*
                c.drawCircle(highlightStart ,indicatorPosY,itemWidth/4,mPaint);

            }*/
            }
        }


        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = mIndicatorHeight

        }

        companion object {
            private val DP: Float = Resources.getSystem().getDisplayMetrics().density
        }

        init {
            mPaint.setStrokeCap(Paint.Cap.ROUND)
            mPaint.setStrokeWidth(mIndicatorStrokeWidth)
            mPaint.setStyle(Paint.Style.FILL)
            mPaint.setAntiAlias(true)
        }
    }


}