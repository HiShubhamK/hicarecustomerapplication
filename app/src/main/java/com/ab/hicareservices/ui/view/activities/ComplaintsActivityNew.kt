package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import com.ab.hicareservices.BuildConfig
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityAddComplaintsBinding
import com.ab.hicareservices.databinding.FragmentOrdersNewBinding
import com.ab.hicareservices.ui.adapter.OrderMenuAdapter
import com.ab.hicareservices.ui.adapter.OrdersAdapter
import com.ab.hicareservices.ui.adapter.ProductViewPagerAdapter
import com.ab.hicareservices.ui.view.fragments.ProductComplaintsFragment
import com.ab.hicareservices.ui.viewmodel.CComplaintViewModel
import com.ab.hicareservices.ui.viewmodel.OrdersViewModel
import com.ab.hicareservices.ui.viewmodel.UploadAttachmentViewModel
import com.ab.hicareservices.utils.AppUtils2
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ComplaintsActivityNew : AppCompatActivity() {

    private val TAG = "ComplaintsActivityNew"
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
        binding = FragmentOrdersNewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        binding.title.text="My Complaints"
        binding.imgLogo.visibility=View.VISIBLE
        binding.imgLogo.setOnClickListener{
            finish()
        }
        setupViewPager()
    }





    private fun setupViewPager() {

        val orderfragment = ProductComplaintsFragment()
        val extraList = ProductComplaintsFragment()
        val adapter = ProductViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(orderfragment, "Services")
        adapter.addFragment(extraList, "Products")

        val viewPager = binding.vpFragments
        viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.vpFragments)
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onResume() {
        super.onResume()
//        getFromServiceType()


    }


    override fun onBackPressed() {
        finish()


    }


}