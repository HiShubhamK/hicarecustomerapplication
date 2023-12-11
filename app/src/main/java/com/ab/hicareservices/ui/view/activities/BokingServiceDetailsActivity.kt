package com.ab.hicareservices.ui.view.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.data.model.FaqList
import com.ab.hicareservices.data.model.servicesmodule.BhklistResponseData
import com.ab.hicareservices.data.model.servicesmodule.GetServicePlanResponseData
import com.ab.hicareservices.databinding.ActivityBokingServiceDetailsBinding
import com.ab.hicareservices.ui.adapter.BookingServiceListDetailsAdapter
import com.ab.hicareservices.ui.adapter.BookingServicePlanListAdapter
import com.ab.hicareservices.ui.handler.OnBookingViewDetials
import com.ab.hicareservices.ui.handler.OnServiceclicklistner
import com.ab.hicareservices.ui.viewmodel.ServiceBooking
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.SharedPreferencesManager
import com.ab.hicareservices.utils.UserData
import com.squareup.picasso.Picasso

class BokingServiceDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBokingServiceDetailsBinding
    var serviceName: String? = null
    var serviceCode: String? = null
    var serviceThumbnail: String? = null
    var shortDescription: String? = null
    var stailDescription: String? = null
    lateinit var spinnerlist: ArrayList<String>
    var selectedLocation: String? = null
    private lateinit var mAdapter: BookingServicePlanListAdapter
    private val viewProductModel: ServiceBooking by viewModels()
    private lateinit var nAdapter: BookingServiceListDetailsAdapter
    lateinit var bhklistResponseData: List<BhklistResponseData>
    var descrition = ""
    var shortdescrition = ""
    var id = 0
    var thumbnail = ""
    var servicecode = ""
    var servicename = ""
    var ServiceId = ""
    lateinit var progressDialog: ProgressDialog
    lateinit var faqList: List<FaqList>
    var checkserchbutton = false
    var checkbottomsheet = false

    private var lastClickTime: Long = 0
    private val clickTimeThreshold = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_boking_service_details)

        binding = ActivityBokingServiceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        MyLocationListener(this)

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        AppUtils2.pincode =
            SharedPreferenceUtil.getData(this@BokingServiceDetailsActivity, "pincode", "")
                .toString()

        if (AppUtils2.pincode.equals("")) {
            binding.getpincodetext.setText("400080")
        } else {
            binding.getpincodetext.setText(AppUtils2.pincode)
        }

        val intent = intent
        serviceName = intent.getStringExtra("ServiceName").toString()
        serviceCode = intent.getStringExtra("ServiceCode").toString()
        serviceThumbnail = intent.getStringExtra("ServiceThumbnail").toString()
        shortDescription = intent.getStringExtra("ShortDescription").toString()
        stailDescription = intent.getStringExtra("DetailDescription").toString()
        ServiceId = intent.getStringExtra("ServiceId").toString()

        faqList = ArrayList()


        val userData = UserData()

        SharedPreferenceUtil.setData(this, "ServiceCode", serviceCode.toString())


        userData.ServiceCode = serviceCode.toString()
        userData.ServiceType = "Pest"
        val sharedPreferencesManager = SharedPreferencesManager(this).saveUserData(userData)


        bhklistResponseData = ArrayList<BhklistResponseData>()

        binding.txtservicename.setText(serviceName.toString())
        Picasso.get().load(serviceThumbnail).into(binding.imgbanner)

        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }

        binding.recycleviewplans.layoutManager = LinearLayoutManager(
            this@BokingServiceDetailsActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        mAdapter = BookingServicePlanListAdapter()
        binding.recycleviewplans.adapter = mAdapter

        getplanlist(servicecode)

        viewProductModel.errorMessage.observe(this@BokingServiceDetailsActivity, Observer {
//            viewProductModel.getPlanAndPriceByPincodeAndServiceCode(
//                "400079",
//                AppUtils2.servicecode
//            )
        })


//        viewProductModel.getServicePincodeDetailResponse.observe(this@BokingServiceDetailsActivity,
//            Observer {
//
//                if (it.IsSuccess == true) {
//
//
//                    if (it.Data != null) {
//                        progressDialog.show()
//                        Handler(Looper.getMainLooper()).postDelayed({
//
//                            viewProductModel.servicePlanResponseData.observe(
//                                this@BokingServiceDetailsActivity,
//                                Observer {
//                                    if (it.isNotEmpty()) {
//                                        binding.recycleviewplans.visibility = View.VISIBLE
//
//                                        mAdapter.setServiceList(it, this)
//                                        checkserchbutton = false
//                                    } else {
//                                        binding.recycleviewplans.visibility = View.GONE
//                                        Toast.makeText(
//                                            this@BokingServiceDetailsActivity,
//                                            "Invalid pincode. Plan are not available ",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//                                        checkserchbutton = false
//                                    }
//                                    progressDialog.dismiss()
//
//                                })
//                            viewProductModel.getPlanAndPriceByPincodeAndServiceCode(
//                                AppUtils2.pincode,
//                                AppUtils2.servicecode
//                            )
//                            checkserchbutton = false
//                        }, 300)
//
//                    }
//                } else {
//                    Toast.makeText(
//                        this@BokingServiceDetailsActivity,
//                        it.ResponseMessage.toString(),
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            })
//
//
//        viewProductModel.GetServicePincodeDetail(AppUtils2.pincode, serviceCode.toString(), "Pest")


//        viewProductModel.servicePlanResponseData.observe(
//            this@BokingServiceDetailsActivity,
//            Observer {
//                if (it.isNotEmpty()) {
//                    progressDialog.dismiss()
//                    mAdapter.setServiceList(it, this)
//                } else {
//                    calldefaultplans()
//                }
//            })
//
//        viewProductModel.getPlanAndPriceByPincodeAndServiceCode(
//            AppUtils2.pincode,
//            AppUtils2.servicecode
//        )
//
//
        if (checkserchbutton == false) {


            binding.imgsearch.setOnClickListener {

                checkserchbutton = true

                SharedPreferenceUtil.setData(
                    this,
                    "pincode",
                    binding.getpincodetext.text.toString()
                )
                AppUtils2.pincode = binding.getpincodetext.text.toString()

                if (binding.getpincodetext.text.length == 0) {
                    Toast.makeText(
                        this@BokingServiceDetailsActivity,
                        "Please enter pincode",
                        Toast.LENGTH_LONG
                    ).show()

                } else if (binding.getpincodetext.text.length != 6) {
                    Toast.makeText(
                        this@BokingServiceDetailsActivity,
                        "Invalid pincode.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                    progressDialog.show()
                    binding.recycleviewplans.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({

                        viewProductModel.servicePlanResponseData.observe(
                            this@BokingServiceDetailsActivity,
                            Observer {
                                if (it.isNotEmpty()) {
                                    mAdapter.setServiceList(it, this)
                                } else {
                                    binding.recycleviewplans.visibility = View.GONE
//                                    Toast.makeText(
//                                        this@BokingServiceDetailsActivity,
//                                        "Invalid pincode. Plan are not available ",
//                                        Toast.LENGTH_LONG
//                                    ).show()
                                    checkserchbutton = false
                                }
                                progressDialog.dismiss()
                            })
                        viewProductModel.getPlanAndPriceByPincodeAndServiceCode(
                            "400080",
                            AppUtils2.servicecode
                        )
                    }, 300)


                }
                val imm =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                // Check if there's a focused view before hiding the keyboard
                if (this is Activity && this.currentFocus != null) {
                    imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
                }
            }


        }

        mAdapter.setonViewdatail(object : OnBookingViewDetials {

            override fun onViewDetails(
                position: Int,
                planid: Int,
                servicePlanDescription: String?,
                price: Int?,
                discountedPrice: Int?
            ) {
                progressDialog.show()

                viewProductModel.bookingServiceDetailResponseData.observe(
                    this@BokingServiceDetailsActivity,
                    Observer {
                        if (it.Id != 0) {
                            progressDialog.dismiss()
                            descrition = it.DetailDescription.toString()
                            shortdescrition = it.ShortDescription.toString()
                            id = it.Id!!
                            thumbnail = it.ServiceThumbnail.toString()
                            servicecode = it.ServiceCode.toString()
                            servicename = it.ServiceName.toString()
                            if (it.FaqList != null) {
                                faqList = it.FaqList
                            } else {
                                faqList = ArrayList()
                            }
                        } else {

                        }
                    })
                viewProductModel.getActiveServiceDetailById(ServiceId.toInt())

                Handler(Looper.getMainLooper()).postDelayed({
                    val bottomSheetFragment = CustomBottomSheetFragment.newInstance(
                        id,
                        servicename,
                        servicecode,
                        thumbnail,
                        shortdescrition,
                        descrition,
                        servicePlanDescription,
                        price,
                        discountedPrice,
                        faqList as ArrayList<FaqList>
                    )
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }, 200)
            }

            override fun onClickAddButton(
                position: Int,
                planid: Int?,
                pincodeid: String,
                noOfBHK: String?,
                getServicePlanResponseData: ArrayList<GetServicePlanResponseData>,
                price: Int?,
                discountedAmount: Int?,
                discountedPrice: Int?,
                servicePlanName: String?
            ) {

                progressDialog.show()

                viewProductModel.getServicePincodeDetailResponse.observe(this@BokingServiceDetailsActivity,
                    Observer {

                        if (it.IsSuccess == true) {
                            if (it.Data != null) {

                                AppUtils2.getServicePlanResponseData = ArrayList()

                                AppUtils2.getServicePlanResponseData.clear()

                                AppUtils2.getServicePlanResponseData.addAll(
                                    getServicePlanResponseData
                                )

                                viewProductModel.activebhklist.observe(
                                    this@BokingServiceDetailsActivity,
                                    Observer {
                                        if (it.isNotEmpty()) {
                                            progressDialog.dismiss()
                                            bhklistResponseData = it

                                        } else {

                                        }
                                    })
                                viewProductModel.getActiveBHKList()
                                checkbottomsheet = true
                            } else {
                                Toast.makeText(
                                    this@BokingServiceDetailsActivity,
                                    "This pincode is not available",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {

                        }
                    })


                viewProductModel.GetServicePincodeDetail(
                    AppUtils2.pincode,
                    serviceCode.toString(),
                    "Pest"
                )


                if (checkbottomsheet == true) {
                    checkbottomsheet = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        val bottomSheetFragments = CustomBottomSheetAddBhkFragment.newInstance(
                            bhklistResponseData as ArrayList<BhklistResponseData>,
                            planid,
                            pincodeid,
                            getServicePlanResponseData as ArrayList<GetServicePlanResponseData>,
                            price,
                            discountedPrice,
                            servicePlanName
                        )
                        bottomSheetFragments.show(supportFragmentManager, bottomSheetFragments.tag)
                    }, 200)
                } else {

                }


//
//                AppUtils2.getServicePlanResponseData = ArrayList()
//
//                AppUtils2.getServicePlanResponseData.clear()
//
//                AppUtils2.getServicePlanResponseData.addAll(getServicePlanResponseData)
//
//                viewProductModel.activebhklist.observe(this@BokingServiceDetailsActivity, Observer {
//                    if (it.isNotEmpty()) {
//                        progressDialog.dismiss()
//                        bhklistResponseData = it
//
//                    } else {
//
//                    }
//                })
//                viewProductModel.getActiveBHKList()
//
//                Handler(Looper.getMainLooper()).postDelayed({
//                    val bottomSheetFragments = CustomBottomSheetAddBhkFragment.newInstance(
//                        bhklistResponseData as ArrayList<BhklistResponseData>,
//                        planid,
//                        pincodeid,
//                        getServicePlanResponseData as ArrayList<GetServicePlanResponseData>,
//                        price,
//                        discountedPrice,
//                        servicePlanName
//                    )
//                    bottomSheetFragments.show(supportFragmentManager, bottomSheetFragments.tag)
//                }, 200)
            }


        })

        binding.recMenu.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        nAdapter = BookingServiceListDetailsAdapter()
        binding.recMenu.adapter = nAdapter

        viewProductModel.serviceresponssedata.observe(this, Observer {


        })
        viewProductModel.getActiveServiceList()



        viewProductModel.bookingServiceDetailResponseData.observe(
            this@BokingServiceDetailsActivity,
            Observer {
                if (it.Id != 0) {
                    nAdapter.setServiceLists(it.OtherServiceList, this)
                    progressDialog.dismiss()
                } else {

                }
            })
        viewProductModel.getActiveServiceDetailById(ServiceId.toInt())

        binding.txtshortdes.text = shortDescription.toString()
        binding.txtlongdes.text = stailDescription.toString()

        spinnerlist = ArrayList()
        spinnerlist.add("Select flat area")
        nAdapter.getservicelistdata(object : OnServiceclicklistner {
            override fun onClickListnerdata(
                position: Int,
                ids: Int?,
                serviceNames: String?,
                serviceCodes: String?,
                serviceThumbnails: String?,
                shortDescriptions: String?,
                detailDescriptions: String?
            ) {
                super.onClickListnerdata(
                    position,
                    ids,
                    serviceNames,
                    serviceCodes,
                    serviceThumbnails,
                    shortDescriptions,
                    detailDescriptions
                )

                Toast.makeText(
                    this@BokingServiceDetailsActivity,
                    serviceCodes.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                binding.txtservicename.text = serviceNames.toString()
                Picasso.get().load(serviceThumbnails).into(binding.imgbanner)

                serviceName = serviceNames.toString()
                serviceCode = serviceCodes.toString()
                serviceThumbnail = serviceThumbnails
                shortDescription = shortDescriptions
                stailDescription = detailDescriptions
                ServiceId = ids.toString()

                getplanlist(serviceCodes.toString()!!)


            }
        })
    }

    fun getplanlist(serviceCode: String) {
        viewProductModel.getServicePincodeDetailResponse.observe(this@BokingServiceDetailsActivity,
            Observer {

                if (it.IsSuccess == true) {


                    if (it.Data != null) {
                        progressDialog.show()
                        Handler(Looper.getMainLooper()).postDelayed({

                            viewProductModel.servicePlanResponseData.observe(
                                this@BokingServiceDetailsActivity,
                                Observer {
                                    if (it.isNotEmpty()) {
                                        binding.recycleviewplans.visibility = View.VISIBLE

                                        mAdapter.setServiceList(it, this)
                                        checkserchbutton = false
                                    } else {

                                        viewProductModel.servicePlanResponseData.observe(
                                            this@BokingServiceDetailsActivity,
                                            Observer {
                                                if (it.isNotEmpty()) {
                                                    binding.recycleviewplans.visibility =
                                                        View.VISIBLE

                                                    mAdapter.setServiceList(it, this)
                                                    checkserchbutton = false
                                                } else {
                                                    binding.recycleviewplans.visibility = View.GONE
                                                    val currentTime = System.currentTimeMillis()
                                                    if (currentTime - lastClickTime > clickTimeThreshold) {
                                                        Toast.makeText(
                                                            this@BokingServiceDetailsActivity,
                                                            "Invalid pincode. Plan are not available ",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                        lastClickTime = currentTime
                                                    }
                                                    checkserchbutton = false
                                                }
                                                progressDialog.dismiss()

                                            })
                                        viewProductModel.getPlanAndPriceByPincodeAndServiceCode(
                                            AppUtils2.pincode,
                                            AppUtils2.servicecode
                                        )

                                        binding.recycleviewplans.visibility = View.GONE


                                        val currentTime = System.currentTimeMillis()
                                        if (currentTime - lastClickTime > clickTimeThreshold) {
                                            Toast.makeText(
                                                this@BokingServiceDetailsActivity,
                                                "Invalid pincode. Plan are not available ",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            lastClickTime = currentTime
                                        }
                                        checkserchbutton = false
                                    }
                                    progressDialog.dismiss()

                                })
                            viewProductModel.getPlanAndPriceByPincodeAndServiceCode(
                                "400080",
                                AppUtils2.servicecode
                            )
                            checkserchbutton = false
                        }, 300)

                    }
                } else {
                    Toast.makeText(
                        this@BokingServiceDetailsActivity,
                        it.ResponseMessage.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        viewProductModel.GetServicePincodeDetail(AppUtils2.pincode, AppUtils2.servicecode, "Pest")

    }

    override fun onBackPressed() {
        val intent = Intent(this@BokingServiceDetailsActivity, PestServicesActivity::class.java)
        startActivity(intent)
    }
}
