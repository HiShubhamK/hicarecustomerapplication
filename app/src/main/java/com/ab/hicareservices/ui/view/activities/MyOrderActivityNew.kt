package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityMyOrderNewBinding
import com.ab.hicareservices.databinding.FragmentOrdersNewBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.adapter.ViewPagerAdapter
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragmentNew
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel
import com.ab.hicareservices.utils.AppUtils2
import org.json.JSONObject


class MyOrderActivityNew : AppCompatActivity() {

    lateinit var binding: ActivityMyOrderNewBinding
    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mAdapter: OrdersAdapter
    private lateinit var nAdapter: OrderMenuAdapter
    private var mobile = ""
    private var ordertype = ""
    lateinit var homeActivity: HomeActivity
    lateinit var orderactivityforadapter: FragmentActivity
    lateinit var options: JSONObject
    lateinit var progressDialog: ProgressDialog


    var activityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { activityResult ->
            val result = activityResult.resultCode
            val data = activityResult.data
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)
        binding = ActivityMyOrderNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mobile = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        AppUtils2.fromdasboardmenu=true
        binding.imgLogo.setOnClickListener {
            onBackPressed()
        }
        setupViewPager()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setupViewPager() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, OrdersFragmentNew.newInstance())
            .addToBackStack("OrdersFragmentNew").commit()
//        binding.tabLayout.setOnClickListener {
//            binding.tabLayout.tavt
//        }

    }

}
