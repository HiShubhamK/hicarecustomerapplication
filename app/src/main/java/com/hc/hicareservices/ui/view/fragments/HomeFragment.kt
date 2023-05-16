package com.hc.hicareservices.ui.view.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.hc.hicareservices.R
import com.hc.hicareservices.databinding.FragmentHomeBinding
import com.hc.hicareservices.ui.adapter.*
import com.hc.hicareservices.utils.AppUtils2
import kotlin.math.abs


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdapter: ServiceAdapter
    private lateinit var mHomeAdapter: HomeServiceAdapter
    private lateinit var mFanAdapter: FanbookAdapter
//    lateinit var viewPager: ViewPager
    private lateinit var handler : Handler
    private lateinit var imageList:ArrayList<Int>
    private lateinit var adapter: ImageAdapter
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageLists: List<Int>
    val TAG = HomeFragment::class.java.simpleName
    private var dotscount :Int=0

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

        init()
//        setUpTransformer()



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


    private fun setUpTransformer() {

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        binding.idViewPager.setPageTransformer(transformer)

    }

    private fun init() {

        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add(R.drawable.bird)
        imageList.add(R.drawable.cleaning)
        imageList.add(R.drawable.bird)

        adapter = ImageAdapter(imageList, binding.idViewPager)


        binding.idViewPager.adapter = adapter
//
//        binding.dotsIndicator.attachTo(binding.idViewPager)

        dotscount = adapter.itemCount

       var dots = arrayOfNulls<ImageView>(dotscount)

        for (i in 0 until dotscount) {
            dots[i] = ImageView(requireContext())
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.nonactive_dot
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.sliderdots.addView(dots[i], params)
        }

        dots[0]!!.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                com.hc.hicareservices.R.drawable.active_dot
            )
        )


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


//    private fun setHomeBanner() {
//
//        imageList = ArrayList<Int>()
//        imageList = imageList + R.drawable.bird
//        imageList = imageList + R.drawable.cleaning
//        imageList = imageList + R.drawable.bird
//        imageList = imageList + R.drawable.cleaning
//
//        viewPagerAdapter = ViewPagerAdapter(requireContext(), imageList)
//
//        binding.idViewPager.adapter=viewPagerAdapter
//
////        try {
////            val slideModels: MutableList<SlideModel> = ArrayList()
////            slideModels.add(SlideModel("https://image.shutterstock.com/image-vector/pest-control-house-hygiene-disinfection-600w-1422131480.jpg", ScaleTypes.FIT))
////            slideModels.add(SlideModel("https://image.shutterstock.com/image-photo/lablebutton-termite-protection-600w-772644724.jpg", ScaleTypes.FIT))
////            slideModels.add(SlideModel("https://image.shutterstock.com/image-vector/pest-control-services-banner-people-600w-1758538019.jpg", ScaleTypes.FIT))
////            binding.slider.setImageList(slideModels, ScaleTypes.FIT)
////        } catch (e: Exception) {
////            e.printStackTrace()
////        }
//    }

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
