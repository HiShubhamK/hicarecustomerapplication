package com.ab.hicareservices.ui.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.ViewPager2
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.dashboard.*
import com.ab.hicareservices.databinding.FragmentHomeBinding
import com.ab.hicareservices.location.MyLocationListener
import com.ab.hicareservices.ui.adapter.*
import com.ab.hicareservices.ui.handler.offerinterface
import com.ab.hicareservices.ui.handler.onResceduleInterface
import com.ab.hicareservices.ui.view.StickyMessageView
import com.ab.hicareservices.ui.view.activities.InAppWebViewActivity
import com.ab.hicareservices.ui.view.activities.OrderDetailActivity
import com.ab.hicareservices.ui.view.activities.PaymentActivity
import com.ab.hicareservices.ui.view.activities.SlotComplinceActivity
import com.ab.hicareservices.ui.viewmodel.DashboardViewModel
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.ui.viewmodel.PaymentCardViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.LocationPermissionManager
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdapter: DashboardMenuAdapter
    private lateinit var mMenuadapter: DashboardMenuAdapterNew
    private lateinit var msocialMediaAdapter: SocialMediaAdapter
    private lateinit var mvideoAdapter: VideoAdapter
    private lateinit var mHomeAdapter: HomeServiceAdapter
    private lateinit var mpayentdashboardadapter: PaymentDashboardAdapter
    private lateinit var mOfferAdapter: OffersAdapter
    private lateinit var mOfferTermsAdapter: OffersTermsAdapter
    lateinit var courseList: List<MenuData>
    lateinit var paymentcardlist: List<PaymentCardViewModel>
    val TAG = HomeFragment::class.java.simpleName
    private lateinit var handler: Handler
    private lateinit var handler2: Handler
    private lateinit var handler3: Handler
    private lateinit var imageList: ArrayList<BannerData>
    private lateinit var offerlist: List<OfferData>
    private lateinit var adapter: ImageAdapter
    private lateinit var madapterbrand: BrandAdapter
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageLists: List<Int>
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private val viewModels: OtpViewModel by viewModels()
    lateinit var progressDialog: ProgressDialog
    private lateinit var codOrders: ArrayList<String>
    private lateinit var menudata: ArrayList<MenuData>
    private lateinit var menudatanew: ArrayList<MenuData>
    var client: FusedLocationProviderClient? = null
    private var lat: String? = ""
    private var longg: String? = ""
    private var lastlat: String? = ""
    private var lastlongg: String? = ""
    private lateinit var stickyMessageView: StickyMessageView

    val REQUEST_CODE_PERMISSIONS = 101


    private var launcher=  registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "OK")
        } else {
            Log.d(TAG, "CANCEL")
//            requireContext().toast("Please Accept Location enable for use this App.")
        }
    }

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

        AppUtils2.mobileno = SharedPreferenceUtil.getData(requireActivity(), "mobileNo", "-1").toString()

        MyLocationListener(requireActivity())


        if (!LocationPermissionManager.checkLocationPermission(requireActivity())) {
            // Request the permission
            LocationPermissionManager.requestLocationPermission(requireActivity())
        } else {
            getCurrentLocations()
            // Permission already granted, proceed with your location-related tasks
            // ...
        }

        client = LocationServices
            .getFusedLocationProviderClient(
                requireActivity()
            )

//        if (ContextCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//            && ContextCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.POST_NOTIFICATIONS
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            // When permission is granted
//            // Call method
//            getCurrentLocations()
//
//        } else {
////            Toast.makeText(requireActivity(),"Not Ok",Toast.LENGTH_LONG).show()
//
//            // When permission is not granted
//            // Call method
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.POST_NOTIFICATIONS
//                    ),
//                    100
//                )
//            }
//        }

        progressDialog = ProgressDialog(requireActivity(), com.ab.hicareservices.R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val drawable = ProgressBar(requireActivity()).indeterminateDrawable.mutate()
            drawable.setColorFilter(
                ContextCompat.getColor(
                    requireActivity(),
                    com.ab.hicareservices.R.color.colorAccent
                ),
                PorterDuff.Mode.SRC_IN
            )
        }

        init(progressDialog)
        binding.idViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })
        binding.idViewPager4.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler2.removeCallbacks(runnable)
                handler2.postDelayed(runnable, 5000)
            }
        })


        getServiceData()


//        setHomeBanner()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // Check if the permission request is related to location
        if (requestCode == LocationPermissionManager.LOCATION_PERMISSION_REQUEST_CODE) {
            // Check if the permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your location-related tasks
                // ...
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable location features)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable, 5000)
        handler2.postDelayed(runnable2, 5000)
//        handler3.postDelayed(runnable3, 5000)
    }

    private val runnable = Runnable {
//        binding.idViewPager2.currentItem = binding.idViewPager2.currentItem + 1
        binding.idViewPager.setCurrentItem(binding.idViewPager.currentItem + 1, true)

    }
    private val runnable2 = Runnable {
        binding.idViewPager4.setCurrentItem(binding.idViewPager4.currentItem + 1, true)

//        binding.recOffers.s(binding.recOffers.currentItem + 1, true);

//        binding.recOffers.currentItem = binding.recOffers.currentItem + 1
    }
//    private val runnable3 = Runnable {
//        binding.idViewPager2.setCurrentItem(binding.idViewPager2.currentItem + 1, true)
//
////        binding.recOffers.currentItem = binding.recOffers.currentItem + 1
//    }

    private fun init(progressDialog: ProgressDialog) {
//        progressDialog.show()
        binding.shimmer.visibility=View.VISIBLE
        binding.shimmer.isShimmerStarted
        binding.nestedscroll.visibility = View.GONE
        AppUtils2.TOKEN = SharedPreferenceUtil.getData(requireContext(), "bToken", "").toString()
        AppUtils2.mobileno = SharedPreferenceUtil.getData(activity!!, "mobileNo", "-1").toString()
//        viewModels.validateAccount(AppUtils2.mobileno)
        handler = Handler(Looper.myLooper()!!)
        handler2 = Handler(Looper.myLooper()!!)
        handler3 = Handler(Looper.myLooper()!!)
        imageList = ArrayList()
        offerlist = ArrayList()
        courseList = ArrayList<MenuData>()
        menudatanew = ArrayList<MenuData>()
        menudata = ArrayList<MenuData>()
        codOrders = ArrayList()
        binding.crdpest.visibility = View.GONE

//        mOfferAdapter = OffersAdapter(offerlist as ArrayList<OfferData>, binding.recOffers)
//        madapterbrand = BrandAdapter(binding.idViewPager2, requireActivity())
//        mOfferAdapter = OffersAdapter(offerlist as ArrayList<OfferData>,binding.recOffers)

        dashboardViewModel.dashboardmain.observe(requireActivity(), Observer {
            Log.d(TAG, "onDashboardData: $it orders fragment")
            try {
                adapter.serBanner(it!!.BannerData)
//                it.MenuData.forEach{
//                    if(it.Title.equals("Products")){
//                        menudatanew.add(it)
//                    }else if (it.Title.equals("Commercial/B2B")){
//                        menudatanew.add(it)
//                    }else{
//                        menudata.add(it)
//                    }
//                }
                mAdapter.setServiceList(it.MenuData)
//                mMenuadapter.setServiceList(menudatanew)

                AppUtils2.socialmedia=it.SocialMediadata
                msocialMediaAdapter.setSocialMedialist(it.SocialMediadata)
                if(it.UpcomingService!=null || it.CODOrders!=null || it.TodaysService!=null) {
                    mpayentdashboardadapter.setPaymentData(
                        it.UpcomingService,
                        it.CODOrders,
                        it.TodaysService
                    )
                }
                mvideoAdapter.setvideo(it.VideoData)
                mOfferAdapter.serBanner(it.OfferData)
                madapterbrand.serBrand(it.BrandData)

//                progressDialog.dismiss()
                binding.shimmer.stopShimmer()
                binding.shimmer.visibility=View.GONE

                binding.nestedscroll.visibility = View.VISIBLE

            } catch (e: Exception) {
                e.printStackTrace()
            }

//            binding.idViewPager.adapter = adapter
        })
        adapter = ImageAdapter(binding.idViewPager, requireActivity())
        mOfferAdapter = OffersAdapter(binding.idViewPager4, requireActivity())

//        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/6e3f5c3d-abdb-4158-b49a-d3e88d763851.jpg")
//        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/cb8b73d2-da3c-4ce6-a172-ae774063d915.jpg")
//        imageList.add("https://s3.ap-south-1.amazonaws.com/hicare-others/6796f0c8-0b67-48e2-884c-047f8991f7ce.jpg")

        binding.idViewPager.adapter = adapter
        binding.idViewPager4.adapter = mOfferAdapter
        mOfferAdapter.setOnOfferClick(object : offerinterface {
            @SuppressLint("MissingInflatedId")
            override fun onOfferClick(position: Int, offers: ArrayList<OfferData>) {
                val modelBottomSheet =
                    LayoutInflater.from(requireContext())
                        .inflate(
                            com.ab.hicareservices.R.layout.layout_offer_detail_bottomsheet,
                            null
                        )
                val dialog = BottomSheetDialog(requireContext())

                val textapp: TextView =
                    modelBottomSheet.findViewById(com.ab.hicareservices.R.id.tvCoupen)
                val tvOfferTitle: TextView =
                    modelBottomSheet.findViewById(com.ab.hicareservices.R.id.tvOfferTitle)
                val tvCopy: Button =
                    modelBottomSheet.findViewById(com.ab.hicareservices.R.id.tvCopy)

                val btnRedeem: Button =
                    modelBottomSheet.findViewById(com.ab.hicareservices.R.id.btnRedeem)
//                mOfferTermsAdapter.serBanner(offers[position].TermsnConditions)

                mOfferTermsAdapter=OffersTermsAdapter(requireActivity(),offers[position].TermsnConditions)

                val recTnc: RecyclerView =
                    modelBottomSheet.findViewById(com.ab.hicareservices.R.id.recTnc)
                recTnc.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                recTnc.adapter=mOfferTermsAdapter
                textapp.text = offers[position].VoucherCode
                tvOfferTitle.text = offers[position].OfferTitle
                if (offers[position].IsCopyEnabled == true) {
                    tvCopy.visibility = View.VISIBLE
                } else {
                    tvCopy.visibility = View.GONE

                }
                if (offers[position].IsRedeemEnabled==true){
                    btnRedeem.visibility=View.VISIBLE
                }else{
                    btnRedeem.visibility=View.GONE
                }

                btnRedeem.setOnClickListener {
                    if (offers[position].IsExternalAppBrowserLink == true) {
                        requireActivity()!!.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(offers[position].PageLink)
                            )
                        )

                    } else if (offers[position].IsInAppBrowserLink == true) {
                        requireActivity()!!.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(offers[position].PageLink)
                            )
                        )

                    } else if (offers[position].IsAppLink == true) {
                        requireActivity()!!.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(offers[position].PageLink)
                            )
                        )

                    }
                }
                tvCopy.setOnClickListener {
                    val clipboard = ContextCompat.getSystemService(
                        requireContext(),
                        ClipboardManager::class.java
                    )
                    clipboard?.setPrimaryClip(ClipData.newPlainText("", textapp.text))
                    Toast.makeText(requireContext(), "Copied!", Toast.LENGTH_SHORT).show()


                }

                dialog.setContentView(modelBottomSheet)
                dialog.show()
            }

        })
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


        binding.idViewPager4.offscreenPageLimit = 3
        binding.idViewPager4.clipToPadding = false
        binding.idViewPager4.clipChildren = false
        binding.idViewPager4.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.recMenu.layoutManager =
            GridLayoutManager(context, 3)
//        binding.recMenu2.layoutManager =
//            GridLayoutManager(context, 2)

        binding.recSocialMedia.layoutManager =
            GridLayoutManager(context, 3)
        binding.recOffers.layoutManager =
            GridLayoutManager(context, 3)
        mAdapter = DashboardMenuAdapter(requireActivity())
//        mMenuadapter = DashboardMenuAdapterNew(requireActivity())
        binding.recVideo.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        msocialMediaAdapter = SocialMediaAdapter(requireActivity())

        binding.recPayments.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mpayentdashboardadapter = PaymentDashboardAdapter()
        binding.recPayments.adapter = mpayentdashboardadapter
        binding.recPayments.addItemDecoration(CirclePagerIndicatorDecoration())
//        binding.recPayments.smoothSnapToPosition()
        binding.recPayments.layoutManager!!.smoothScrollToPosition(
            binding.recPayments,
            RecyclerView.State(),
            binding.recPayments.adapter!!.itemCount
        )
        mpayentdashboardadapter.setRescudullClick(object : onResceduleInterface {
            override fun onRecheduleClick(position: Int, upcomingdata: ArrayList<UpcomingService>) {
                val intent = Intent(requireActivity(), SlotComplinceActivity::class.java)
                intent.putExtra("ServiceCenter_Id", upcomingdata[position].HRRegion_r!!.Id)
                if (upcomingdata[position].AppointmentDate != null) {
                    intent.putExtra("SlotDate", upcomingdata[position].AppointmentDate)
                } else {
                    intent.putExtra("SlotDate", upcomingdata[position].SRPlanDate)
                }
                intent.putExtra("TaskId", upcomingdata[position].Id)
                intent.putExtra("SkillId", upcomingdata[position].TaskSkill_c)
                intent.putExtra("Lat", upcomingdata[position].GoogleLat_c)
                intent.putExtra("Long", upcomingdata[position].GoogleLong_c)
                intent.putExtra("ServiceType", "Pest")
                intent.putExtra("Pincode", upcomingdata[position].HRZipPostalCode_c)
                intent.putExtra("SPCode", upcomingdata[position].OrderSPCode)
                intent.putExtra("ServiceUnit", upcomingdata[position].OrderServiceArea_r!!.Unit_c)
                intent.putExtra("Unit",upcomingdata[position].Unit)

                startActivity(intent)
            }

            override fun onPaymentClick(position: Int, order: ArrayList<CODOrders>) {
                val intent = Intent(requireActivity(), PaymentActivity::class.java)
                intent.putExtra("ORDER_NO", order[position].OrderNumber_c)
                intent.putExtra("ACCOUNT_NO", order[position].CustomerId_c)
                intent.putExtra("SERVICETYPE_NO", order[position].ServicePlanName_c)
                intent.putExtra("PAYMENT", order[position].OrderValueWithTax_c!!.toDouble())
                intent.putExtra("SERVICE_TYPE", order[position].ServiceType)
                intent.putExtra("Standard_Value__c", order[position].StandardValue_c)
                intent.putExtra("Product", false)

                startActivity(intent)
            }

            override fun onPaymentitemsClick(position: Int, order: ArrayList<CODOrders>) {
                val intent = Intent(requireActivity(), OrderDetailActivity::class.java)
                intent.putExtra("orderNo", order[position].OrderNumber_c)
                intent.putExtra("serviceType", order[position].ServiceType)
                intent.putExtra("service_url_image", order[position].ServicePlanImageUrl)
                intent.putExtra(
                    "locationLatitudeS",
                    order[position].AccountName_r!!.Location_Latitude_s
                )
                intent.putExtra(
                    "locationLongitudeS",
                    order[position].AccountName_r!!.Location_Longitude_s
                )
                intent.putExtra("ServiceCenterId", order[position]!!.HRShippingRegion_r!!.Id)

                startActivity(intent)
            }

            override fun onToadaysClick(
                position: Int,
                offers: String?,
                customerotpC: String?,
                name: String?,
                technicianmobileC: String?,
                toString: String
            ) {
                showLeadDialog(offers,customerotpC,name,technicianmobileC,toString)
            }

        })

        mvideoAdapter = VideoAdapter(requireActivity())
        binding.recMenu.adapter = mAdapter
//        binding.recMenu2.adapter = mMenuadapter
        binding.recSocialMedia.adapter = msocialMediaAdapter
        binding.recVideo.adapter = mvideoAdapter
        binding.crdpest.visibility = View.VISIBLE
        madapterbrand = BrandAdapter(binding.recOffers, requireActivity())
        binding.recOffers.adapter = madapterbrand
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        val snapHelper: SnapHelper = PagerSnapHelper()
//        binding.recOffers.layoutManager = layoutManager
//        snapHelper.attachToRecyclerView(binding.recOffers)
//        binding.recOffers.scrollToPosition(madapterbrand.itemCount -1);
//        (binding.recOffers.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(madapterbrand.itemCount, 0)


//        binding.dotsIndicator.attachTo(binding.idViewPager)

        dashboardViewModel.GetDashboard(AppUtils2.mobileno)


    }


    private fun getServiceData() {
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


//        binding.recOffers.layoutManager =
//            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

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

//        binding.recMenu.adapter = mAdapter


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
            mPaint.color = Color.parseColor("#2bb77a")

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

    fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        clipboard?.setPrimaryClip(ClipData.newPlainText("", text))
        Toast.makeText(applicationContext, "Copied!", Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocations() {
        try {
            val locationManager: LocationManager =
                requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
            // Check condition
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )
            ) {
                try {
                    client!!.lastLocation.addOnCompleteListener(
                        object : OnCompleteListener<Location?> {

                            override fun onComplete(
                                task: Task<Location?>
                            ) {


                                // Initialize location
                                val location: Location? = task.result
                                // Check condition
                                if (location != null) {
                                    // When location result is not
                                    // null set latitude
//                            Toasty.success(
//                                this@Checkin_Out_Home,
//                                "Lat: " + location.getLatitude() + "long: " + location.getLongitude()
//                            )
                                    lat = location.latitude.toString()
                                    longg = location.longitude.toString()

//                            tvLatitude.setText(java.lang.String.valueOf(location.getLatitude()))
//                            // set longitude
//                            tvLongitude.setText(java.lang.String.valueOf(location.getLongitude()))
                                } else {
                                    // When location result is null
                                    // initialize location request
                                    val locationRequest =
                                        LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setInterval(10000).setFastestInterval(1000)
                                            .setNumUpdates(1)

                                    // Initialize location call back
                                    val locationCallback: LocationCallback =
                                        object : LocationCallback() {
                                            fun voidonLocationResult(
                                                locationResult: LocationResult
                                            ) {
                                                // Initialize
                                                // location
                                                val location1: Location = locationResult.lastLocation
                                                // Set latitude
//                                    Toasty.success(
//                                        this@Checkin_Out_Home,
//                                        "Lat: " + location1.getLatitude() + "long: " + location1.getLongitude()
//                                    )
                                                lastlat = location1.latitude.toString()
                                                lastlongg = location1.longitude.toString()
                                                val mGeocoder =
                                                    Geocoder(requireActivity(), Locale.getDefault())
                                                if (mGeocoder != null) {
                                                    var postalcode: MutableList<Address>? =
                                                        mGeocoder.getFromLocation(
                                                            lastlat!!.toDouble(),
                                                            lastlongg!!.toDouble(),
                                                            5
                                                        )
                                                    if (postalcode != null && postalcode.size > 0) {
                                                        for (i in 0 until postalcode.size) {
                                                            AppUtils2.pincode =
                                                                postalcode.get(i).postalCode.toString()
                                                            SharedPreferenceUtil.setData(
                                                                requireActivity(),
                                                                "pincode",
                                                                postalcode.get(i).postalCode.toString()
                                                            )
                                                            break
                                                        }
                                                    }
                                                }
//                                    tvLatitude.setText(java.lang.String.valueOf(location1.getLatitude()))
//                                    // Set longitude
//                                    tvLongitude.setText(java.lang.String.valueOf(location1.getLongitude()))
                                            }
                                        }

                                    // Request location updates
                                    if (ActivityCompat.checkSelfPermission(
                                            requireActivity(),
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                            requireActivity(),
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return
                                    }
                                    Looper.myLooper()?.let {
                                        client!!.requestLocationUpdates(
                                            locationRequest,
                                            locationCallback,
                                            it
                                        )
                                    }
                                }
                            }
                        })
                }catch (e:Exception){
                    e.printStackTrace()
                }
                // When location service is enabled
                // Get last location

            } else {
                enableLoc()

//                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun enableLoc() {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000
        locationRequest.fastestInterval = 5 * 1000
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(requireActivity()).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(
                    ApiException::class.java
                )
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(exception.status.resolution!!!!).build()
                        launcher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                    }
                }
//                when (exception.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                         // Location settings are not satisfied. But could be fixed by showing the
//                        // user a dialog.
//                        try {
//                            // Cast to a resolvable exception.
//                            val resolvable = exception as ResolvableApiException
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            resolvable.startResolutionForResult(
//                                requireActivity(),
//                                REQUEST_CODE_PERMISSIONS
//                            )
//                        } catch (e: SendIntentException) {
//                            // Ignore the error.
//                        } catch (e: ClassCastException) {
//                            // Ignore, should be an impossible error.
//                        }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
//                }
            }
        }
    }


    private fun showLeadDialog(
        offers: String?,
        customerotpC: String?,
        name: String?,
        technicianmobileC: String?,
        toString: String
    ) {
        val li = LayoutInflater.from(requireActivity())
        val promptsView = li.inflate(R.layout.layout_today_dialog, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setView(promptsView)
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        val txtTechName = promptsView.findViewById<View>(R.id.txtTechName) as TextView
        val txttxtmobile = promptsView.findViewById<View>(R.id.txttxtmobile) as TextView
        val edtpincode = promptsView.findViewById<View>(R.id.txtonsiteotp) as TextView
        val email = promptsView.findViewById<View>(R.id.txtcompltionotp) as TextView
        val imgbtncancel=promptsView.findViewById<View>(R.id.imgbtncancel) as ImageView
        val btnlead=promptsView.findViewById<View>(R.id.btnlead) as Button

        txtTechName.text=": "+offers
        txttxtmobile.text=": "+customerotpC
        edtpincode.text=": "+name
        email.text=": "+technicianmobileC

        alertDialog.setCancelable(false)

        var ids="http://run.hicare.in/web/MapTrack/TrackResource?code="+toString

        btnlead.setOnClickListener {
            val intent = Intent(requireActivity(), InAppWebViewActivity::class.java)
            intent.putExtra("PageLink", ids)
            startActivity(intent)
            alertDialog.dismiss()
        }

        imgbtncancel.setOnClickListener { alertDialog.cancel() }

        alertDialog.show()
    }



}

