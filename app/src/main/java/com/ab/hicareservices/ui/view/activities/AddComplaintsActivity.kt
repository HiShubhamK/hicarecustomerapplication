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
import com.ab.hicareservices.BuildConfig
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityAddComplaintsBinding
import com.ab.hicareservices.ui.adapter.CustomSpinnerAdapter
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddComplaintsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddComplaintsBinding
    var selectedCType = "Complaint Type"
    var selectedCSubType = ""
    var serviceType = ""
    var getServiceType = ""
    var servicetypeee = ""
    var orderNo = ""
    var captureby = ""
    var service_url_image = ""
    private val complaintViewModel: CComplaintViewModel by viewModels()
    private val uploadAttachmentViewModel: UploadAttachmentViewModel by viewModels()
    lateinit var typeHash: HashMap<String, List<String>?>
    lateinit var arraylistImages: ArrayList<String>
    val CAMERA_REQUEST = 1
    val REQUEST_GALLERY_PHOTO = 2
    private var mPhotoFile: File? = null
    private var bitmap: Bitmap? = null
    private var selectedImagePath = ""
    private var mobile = ""


    //camera init
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewFinder: PreviewView

    var REQUEST_CODE = 1234
    private lateinit var imageuri1: Uri
    private lateinit var imageuri2: Uri
    private lateinit var imageuri3: Uri
    lateinit var progressDialog: ProgressDialog
    var checkformactivity = false
    var selectspinner = ""
    private lateinit var adapter: CustomSpinnerAdapter
    private val viewModels: OrdersViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddComplaintsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        viewFinder = findViewById<PreviewView>(R.id.viewFinder)

        mobile =
            SharedPreferenceUtil.getData(this@AddComplaintsActivity, "mobileNo", "-1").toString()

        val intent = intent
        orderNo = intent.getStringExtra("orderNo").toString()
        getServiceType = intent.getStringExtra("serviceType").toString()
        checkformactivity = intent.getBooleanExtra("complaint", false)
        service_url_image = intent.getStringExtra("service_url_image").toString()
//        captureby = intent.getStringExtra("captureby").toString()

        if (checkformactivity == true) {
            binding.servicetypes.visibility = View.VISIBLE
        } else {
            binding.servicetypes.visibility = View.GONE
        }


        try {
            adapter = CustomSpinnerAdapter(this, AppUtils2.datalist)
            binding.spinnerLead.adapter = adapter

        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.spinnerLead.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                try {
                    orderNo = AppUtils2.datalist[position].OrderNumber_c.toString()
                    serviceType = AppUtils2.datalist[position].ServiceType.toString()
                    getServiceType = AppUtils2.datalist[position].ServiceType.toString()
                    service_url_image = AppUtils2.datalist[position].ServicePlanImageUrl.toString()
                    binding.bottomheadertext.text =
                        AppUtils2.datalist[position].OrderNumber_c.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


//        val arrayAdapter = object : ArrayAdapter<String>(
//            this@AddComplaintsActivity,
//            R.layout.spinner_layout_new,
//            AppUtils2.Spinnerlist
//        ) {
//            override fun isEnabled(position: Int): Boolean {
//                return position != 0
//            }
//
//            override fun getDropDownView(
//                position: Int,
//                convertView: View?,
//                parent: ViewGroup
//            ): View {
//                val view = super.getDropDownView(position, convertView, parent)
//                val tv = view as TextView
//                if (position == 0) {
//                    tv.setTextColor(Color.GRAY)
//                } else {
//                    tv.setTextColor(Color.BLACK)
//                }
//                return view
//            }
//        }
//        arrayAdapter.setDropDownViewResource(R.layout.spinner_popup)
//        binding.spinnerLead.adapter = arrayAdapter
//
//        binding.spinnerLead.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                selectspinner = binding.spinnerLead.selectedItem.toString()
//                Toast.makeText(this@AddComplaintsActivity,AppUtils2.datalist.size.toString(),Toast.LENGTH_SHORT).show()
//                    for (i in 0 until AppUtils2.datalist.size) {
//                        if (AppUtils2.datalist.get(i).ServicePlanName_c.equals(selectspinner)) {
//                            orderNo = AppUtils2.datalist.get(i).OrderNumber_c.toString()
//                            serviceType = AppUtils2.datalist.get(i).ServiceType.toString()
//                            getServiceType=AppUtils2.datalist.get(i).ServiceType.toString()
//                            service_url_image = AppUtils2.datalist.get(i).ServicePlanImageUrl.toString()
//                            binding.bottomheadertext.text=AppUtils2.datalist.get(i).OrderNumber_c.toString()
//                            break
//                        }
//                    }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }


        viewModels.ordersList.observe(this@AddComplaintsActivity, androidx.lifecycle.Observer {
            if (it != null) {
                try {
                    AppUtils2.datalist = ArrayList()
                    AppUtils2.datalist.clear()
                    AppUtils2.datalist.addAll(it)
                    AppUtils2.Spinnerlist = ArrayList()
                    AppUtils2.Spinnerlist.clear()
                    AppUtils2.Spinnerlist.add("Select Service")
                    progressDialog.dismiss()
                    if (AppUtils2.datalist != null) {
                        for (i in 0 until AppUtils2.datalist.size) {
                            if (AppUtils2.datalist.get(i).ServicePlanName_c.equals(selectspinner)) {
                                orderNo = AppUtils2.datalist.get(i).OrderNumber_c.toString()
                                serviceType = AppUtils2.datalist.get(i).ServiceType.toString()
                                getServiceType = AppUtils2.datalist.get(i).ServiceType.toString()
                                service_url_image =
                                    AppUtils2.datalist.get(i).ServicePlanImageUrl.toString()
                                binding.bottomheadertext.text =
                                    AppUtils2.datalist.get(i).OrderNumber_c.toString()
                                break
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

        viewModels.responseMessage.observe(
            this@AddComplaintsActivity,
            androidx.lifecycle.Observer {
                progressDialog.dismiss()
            })

        viewModels.errorMessage.observe(this@AddComplaintsActivity, androidx.lifecycle.Observer {
            if (it != null) {
            }
        })

        val mobile =
            SharedPreferenceUtil.getData(this@AddComplaintsActivity, "mobileNo", "-1").toString()
        viewModels.getCustomerOrdersByMobileNo(mobile, "Active", progressDialog,this@AddComplaintsActivity)


        val extrass = getIntent().extras
        arraylistImages = ArrayList()
        arraylistImages.add(0, "")
        arraylistImages.add(1, "")
        arraylistImages.add(2, "")
        arraylistImages.add(3, "")
        arraylistImages.add(4, "")
        val extras = getIntent().extras
        if (orderNo != "") {
            binding.bottomheadertext.visibility = View.VISIBLE
            binding.bottomheadertext.text = orderNo
        } else {
            binding.bottomheadertext.visibility = View.GONE
        }

        val img1 = SharedPreferenceUtil.getData(this, "Image1", "").toString()


        val img2 = SharedPreferenceUtil.getData(this, "Image2", "").toString()

        typeHash = HashMap()
        binding.imgLogo.setOnClickListener {
            finish()

            SharedPreferenceUtil.setData(this, "Image1", "").toString()
            SharedPreferenceUtil.setData(this, "Image2", "").toString()
        }


        binding.complaintSpnType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selectedType = binding.complaintSpnType.selectedItem.toString()
                    if (selectedType != "Complaint Type") {
                        selectedCType = selectedType
                        if (!serviceType.equals("pest", true)) {
                            getSubTypeFromType()
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        binding.subSpnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedType = binding.subSpnType.selectedItem.toString()
                if (selectedType != "None" || selectedType.equals("Complaint Sub Type", true)) {
                    selectedCSubType = selectedType
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.saveBtn.setOnClickListener {
            val serviceNo = binding.serviceNoEt.text.toString().trim()
            val complaintTitle = binding.complaintTitleEt.text.toString().trim()
            val complaintDescr = binding.complaintDescrEt.text.toString().trim()
            if (serviceType.equals("pest", true)) {
                if (orderNo != "" && complaintTitle != "" && complaintDescr != "" && selectedCType != "Complaint Type") { //&& selectedCType != ""&& selectedCType != "Complaint Type"
                    addComplaint(
                        orderNo, serviceNo, selectedCType,
                        selectedCSubType, complaintTitle, complaintDescr, serviceType
                    )
                } else {
                    Toast.makeText(this, "Please fill data properly.", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (orderNo != "" && complaintTitle != "" && complaintDescr != ""&& selectedCType != "Complaint Type") {    //&& selectedCType != ""&& selectedCType != "Complaint Type"
                    addComplaint(
                        orderNo, serviceNo, selectedCType,
                        selectedCSubType, complaintTitle, complaintDescr, serviceType
                    )
                } else {
//                    Toast.makeText(this, orderNo, Toast.LENGTH_LONG).show()
                    Toast.makeText(this, "Please fill data properly", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.lnrUpload.setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            intent.putExtra("orderNo", orderNo)
//            intent.putExtra("getServiceType", getServiceType)
//            intent.putExtra("captureby", "Image1")
//            startActivity(intent)
//            finish()
            captureby = "img1"
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    AddComplaintsActivity.REQUIRED_PERMISSIONS,
                    AddComplaintsActivity.REQUEST_CODE_PERMISSIONS
                )
            }
        }
        binding.lnrUpload2.setOnClickListener {
            captureby = "img2"
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    AddComplaintsActivity.REQUIRED_PERMISSIONS,
                    AddComplaintsActivity.REQUEST_CODE_PERMISSIONS
                )
            }
//            val intent = Intent(this, CameraActivity::class.java)
//            intent.putExtra("orderNo", orderNo)
//            intent.putExtra("getServiceType", getServiceType)
//            intent.putExtra("captureby", "Image2")
//            startActivity(intent)
//            finish()
        }
        binding.lnrUpload3.setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            intent.putExtra("orderNo", orderNo)
//            intent.putExtra("getServiceType", getServiceType)
//            intent.putExtra("captureby", "Image3")
//            startActivity(intent)
//            finish()
            captureby = "img3"
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    AddComplaintsActivity.REQUIRED_PERMISSIONS,
                    AddComplaintsActivity.REQUEST_CODE_PERMISSIONS
                )
            }
        }
        binding.lnrUpload4.setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            intent.putExtra("orderNo", orderNo)
//            intent.putExtra("getServiceType", getServiceType)
//            intent.putExtra("captureby", "Image3")
//            startActivity(intent)
//            finish()
            captureby = "img4"
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    AddComplaintsActivity.REQUIRED_PERMISSIONS,
                    AddComplaintsActivity.REQUEST_CODE_PERMISSIONS
                )
            }
        }
        binding.lnrUpload5.setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            intent.putExtra("orderNo", orderNo)
//            intent.putExtra("getServiceType", getServiceType)
//            intent.putExtra("captureby", "Image3")
//            startActivity(intent)
//            finish()
            captureby = "img5"
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    AddComplaintsActivity.REQUIRED_PERMISSIONS,
                    AddComplaintsActivity.REQUEST_CODE_PERMISSIONS
                )
            }
        }


        // set on click listener for the button of capture photo
        // it calls a method which is implemented below
        findViewById<Button>(R.id.camera_capture_button).setOnClickListener {

            binding.scrollView2.visibility = View.VISIBLE
            binding.layoutCamera.visibility = View.GONE
            takePhoto(captureby)
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun getSubTypeFromType() {
        val subtype = ArrayList<String>()
        subtype.clear()
        subtype.add("Complaint Sub Type")
        typeHash[selectedCType]?.forEach {
            subtype.add(it)
        }
        val subtypeAdapter = object :
            ArrayAdapter<String>(this, R.layout.spinner_layout_new, subtype) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        subtypeAdapter.setDropDownViewResource(R.layout.spinner_popup)
        binding.subSpnType.adapter = subtypeAdapter


    }

    private fun getFromServiceType() {
        val serviceTypeArray = arrayListOf("Select Service Type", "Pest", "HC")

        val serviceTypeAdapter =
            object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, serviceTypeArray) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }

        serviceTypeAdapter.setDropDownViewResource(R.layout.spinner_popup)
        binding.serviceTypeSpn.adapter = serviceTypeAdapter

        binding.serviceTypeSpn.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selectedType = binding.serviceTypeSpn.selectedItem.toString()
                    if (selectedType != "Select Service Type") {
                        //selectedCType = selectedType
                        serviceType = selectedType
                        Log.d("TAG", "Called Select Service Type")
                        binding.progressBar.visibility = View.VISIBLE
                        binding.complaintSpnType.isEnabled = true
                    }
                    //Log.d("TAG", selectedType)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

        serviceTypeArray.forEach {
            if (it == getServiceType) {
                binding.serviceTypeSpn.setSelection(serviceTypeArray.indexOf(it))
                serviceType = it
                return@forEach
            }
        }
        binding.serviceTypeSpn.isEnabled = false
    }

    private fun setCaptureImage(data: Uri, captureby: String) {
        try {
            val imageStream: InputStream? = this.contentResolver.openInputStream(data)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            val baos = ByteArrayOutputStream()
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val b = baos.toByteArray()
            val encodedString: String = Base64.encodeToString(b, Base64.DEFAULT)
//            binding.progressBar.visibility = View.VISIBLE

            Log.e("TAG", "Bse64Str: " + encodedString)
//            if (captureby.equals("Image1")) {

            val hashMap = HashMap<String, Any>()


            hashMap["FileName"] = "complaint1"
            hashMap["FileExtension"] = ".jpeg"
            hashMap["FileData"] = encodedString


            uploadAttachmentViewModel.attachmenturl.observe(this, {
                progressDialog.show()

                binding.scrollView2.visibility = View.VISIBLE
                binding.layoutCamera.visibility = View.GONE
                try {
                    if (it != "") {


//                    binding.lnrImage.visibility = View.VISIBLE
//                    binding.lnrUpload.visibi lity = View.GONE
//                    Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
//                    SharedPreferenceUtil.setData(this, captureby, it)
//                    arraylistImages.add(it)
//                    if (it != null || it != "") {
//                        Picasso.get().load(it).into(binding.imgUploadedCheque)
//                    }
//                    if (arraylistImages.isNotEmpty()) {

                        if (captureby == "img2") {
//


                            if (arraylistImages.isNotEmpty() && arraylistImages[1] != "") {
                                binding.lnrUpload2.visibility = View.GONE
                                binding.lnrImage2.visibility = View.VISIBLE
                                Picasso.get().load(arraylistImages[1].toString())
                                    .into(binding.imgUploadedCheque2)
                            } else {
                                binding.lnrUpload2.visibility = View.GONE
                                binding.lnrImage2.visibility = View.VISIBLE
                                arraylistImages.add(1, it)
                                Picasso.get().load(arraylistImages[1].toString())
                                    .into(binding.imgUploadedCheque2)

                            }
                            progressDialog.dismiss()

                        } else if (captureby == "img1") {
//
                            if (arraylistImages.isNotEmpty() && arraylistImages[0] != "") {
                                binding.lnrUpload.visibility = View.GONE
                                binding.lnrImage.visibility = View.VISIBLE
                                Picasso.get().load(arraylistImages[0].toString())
                                    .into(binding.imgUploadedCheque2)

                            } else {
                                if (it != null && !it.equals("")) {
                                    binding.lnrUpload.visibility = View.GONE
                                    binding.lnrImage.visibility = View.VISIBLE
                                    arraylistImages.add(0, it)
                                    Picasso.get().load(arraylistImages[0].toString())
                                        .into(binding.imgUploadedCheque)

                                }
                            }
                            progressDialog.dismiss()

                        } else if (captureby == "img3") {
                            if (arraylistImages.isNotEmpty() && arraylistImages[2] != "") {
                                binding.lnrUpload3.visibility = View.GONE
                                binding.lnrImage3.visibility = View.VISIBLE
                                Picasso.get().load(arraylistImages[2].toString())
                                    .into(binding.imgUploadedCheque3)
                            } else {
                                if (it != null && !it.equals("")) {
                                    binding.lnrUpload3.visibility = View.GONE
                                    binding.lnrImage3.visibility = View.VISIBLE
                                    arraylistImages.add(2, it)
                                    Picasso.get().load(arraylistImages[2].toString())
                                        .into(binding.imgUploadedCheque3)

                                }

                            }
                            progressDialog.dismiss()


                        } else if (captureby == "img4") {
                            if (arraylistImages.isNotEmpty() && arraylistImages[3] != "") {
                                binding.lnrUpload4.visibility = View.GONE
                                binding.lnrImage4.visibility = View.VISIBLE
                                Picasso.get().load(arraylistImages[3].toString())
                                    .into(binding.imgUploadedCheque4)
                            } else {
                                if (it != null && !it.equals("")) {
                                    binding.lnrUpload4.visibility = View.GONE
                                    binding.lnrImage4.visibility = View.VISIBLE
                                    arraylistImages.add(3, it)
                                    Picasso.get().load(arraylistImages[3].toString())
                                        .into(binding.imgUploadedCheque4)

                                }

                            }

                            progressDialog.dismiss()

                        } else if (captureby == "img5") {
                            if (arraylistImages.isNotEmpty() && arraylistImages[4] != "") {
                                binding.lnrUpload5.visibility = View.GONE
                                binding.lnrImage5.visibility = View.VISIBLE
                                Picasso.get().load(arraylistImages[4].toString())
                                    .into(binding.imgUploadedCheque5)
                            } else {
                                if (it != null && !it.equals("")) {
                                    binding.lnrUpload5.visibility = View.GONE
                                    binding.lnrImage5.visibility = View.VISIBLE
                                    arraylistImages.add(4, it)

                                    Picasso.get().load(arraylistImages[4].toString())
                                        .into(binding.imgUploadedCheque5)

                                }
                            }

                            progressDialog.dismiss()

                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }


//                }

            })


            uploadAttachmentViewModel.UploadAttachment(hashMap)
            try {
                binding.imageCancel.setOnClickListener {
                    if (arraylistImages[0] != "") {
                        arraylistImages.removeAt(0)
                        binding.lnrImage.visibility = View.GONE
                        binding.lnrUpload.visibility = View.VISIBLE
                    }
                }
                binding.imageCancel2.setOnClickListener {
                    if (arraylistImages[1] != "") {

                        arraylistImages.removeAt(1)
                        binding.lnrImage2.visibility = View.GONE
                        binding.lnrUpload2.visibility = View.VISIBLE
                    }
                }
                binding.imageCancel3.setOnClickListener {
                    if (arraylistImages[2] != "") {

                        arraylistImages.removeAt(2)
                        binding.lnrImage3.visibility = View.GONE
                        binding.lnrUpload3.visibility = View.VISIBLE
                    }
                }
                binding.imageCancel4.setOnClickListener {
                    if (arraylistImages[3] != "") {
                        arraylistImages.removeAt(3)
                        binding.lnrImage4.visibility = View.GONE
                        binding.lnrUpload4.visibility = View.VISIBLE
                    }
                }
                binding.imageCancel5.setOnClickListener {
                    if (arraylistImages[4] != "") {
                        arraylistImages.removeAt(4)
                        binding.lnrImage5.visibility = View.GONE
                        binding.lnrUpload5.visibility = View.VISIBLE
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

//            uploadattachment(encodedString, captureby)

//            } else if (captureby == "Image2") {
//                uploadattachment(encodedString, captureby)
//
//            } else if (captureby == "Image3") {
//                uploadattachment(encodedString, captureby)
//
//            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun getComplaintReason(serviceType: String) {
        val type = ArrayList<String>()
        val subtype = ArrayList<String>()

        complaintViewModel.responseMessage.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        })

        complaintViewModel.responseMessage.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        })

        complaintViewModel.complaintReasons.observe(this) {
            if (it?.isSuccess == true) {
                type.clear()
                subtype.clear()
                binding.progressBar.visibility = View.GONE
                type.add("Complaint Type")
                if (serviceType.equals("pest", true)) {
                    subtype.add("None")
                } else {
                    subtype.add("Complaint Sub Type")
                }
                it.data?.forEach { complaintType ->
                    type.add(complaintType.name.toString())
                    if (!serviceType.equals("pest", true)) {
                        typeHash[complaintType.name.toString()] = complaintType.subType
                    }
                }
                Log.d("TAG", typeHash.toString())
                initArrayAdapter(serviceType, type, subtype)
            } else {
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        complaintViewModel.getComplaintReasonses(serviceType,this)
    }

    private fun addComplaint(
        orderNo: String, serviceNo: String,
        complaintType: String, complaintSubType: String,
        complaintTitle: String, complaintDescr: String, serviceType: String
    ) {
        progressDialog.show()

        arraylistImages.removeAll(listOf("", null))
        val hashMap = HashMap<String, Any>()
        hashMap["OrderNo"] = orderNo
        hashMap["ServiceNo"] = "1"
        hashMap["ComplaintType"] = complaintType
        hashMap["ComplaintSubType"] = complaintSubType
        hashMap["ComplaintTitle"] = complaintTitle
        hashMap["ComplaintDescription"] = complaintDescr
        hashMap["ServiceType"] = getServiceType
        hashMap["Source"] = "mobileApp"
        hashMap["SubSource"] = "mobileApp"
        hashMap["ComplaintAttachment"] = arraylistImages


        complaintViewModel.createComplaint(hashMap)
        complaintViewModel.createComplaintResponse.observe(this, {
            if (it.isSuccess == true) {
                finish()
                progressDialog.dismiss()
                Toast.makeText(this, "Complaint Raised Successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()

        })
    }

    private fun uploadattachment(
        imagebase64: String,
        captureby: String
    ) {

//        Toast.makeText(this,captureby,Toast.LENGTH_SHORT).show()

        if (captureby.equals("Image1")) {
            val hashMap = HashMap<String, Any>()



            hashMap["FileName"] = "complaint1"
            hashMap["FileExtension"] = ".jpeg"
            hashMap["FileData"] = imagebase64


            uploadAttachmentViewModel.UploadAttachment(hashMap)


        } else if (captureby.equals("Image2")) {
            val hashMap = HashMap<String, Any>()
            hashMap["FileName"] = "complaint2"
            hashMap["FileExtension"] = ".jpeg"
            hashMap["FileData"] = imagebase64


            uploadAttachmentViewModel.UploadAttachment(hashMap)
            uploadAttachmentViewModel.attachmenturl.observe(this, {
                if (it != "") {
                    binding.lnrImage2.visibility = View.VISIBLE
                    binding.lnrUpload2.visibility = View.GONE
//                    arraylistImages.add(it)
//                    Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                    SharedPreferenceUtil.setData(this, captureby, it)
//                    if (it != null || it != "") {
//                        Picasso.get().load(it).into(binding.imgUploadedCheque2)
//                    }
//                    Log.e("TAG","imageArray2:- "+arraylistImages.toString())

                    binding.progressBar.visibility = View.GONE

                }

            })

            binding.imageCancel2.setOnClickListener {
//                arraylistImages.removeAt(1)
                binding.lnrImage2.visibility = View.GONE
                binding.lnrUpload2.visibility = View.VISIBLE
            }
        } else if (captureby == "Image3") {
            val hashMap = HashMap<String, Any>()
            hashMap["FileName"] = "complaint2"
            hashMap["FileExtension"] = ".jpeg"
            hashMap["FileData"] = imagebase64


            uploadAttachmentViewModel.UploadAttachment(hashMap)
            uploadAttachmentViewModel.attachmenturl.observe(this, {
                if (it != "") {
                    binding.lnrImage3.visibility = View.VISIBLE
                    binding.lnrUpload3.visibility = View.GONE
//                    arraylistImages.add(it)
//                    Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                    SharedPreferenceUtil.setData(this, captureby, it)
//                    if (it != null || it != "") {
//                        Picasso.get().load(it).into(binding.imgUploadedCheque2)
//                    }
//                    Log.e("TAG","imageArray2:- "+arraylistImages.toString())

                    binding.progressBar.visibility = View.GONE

                }

            })

            binding.imageCancel3.setOnClickListener {
//                arraylistImages.removeAt(1)
                binding.lnrImage3.visibility = View.GONE
                binding.lnrUpload3.visibility = View.VISIBLE
            }
        }


    }

    private fun initArrayAdapter(serviceType: String, type: List<String>, subType: List<String>) {

        val typeAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, type) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }

        val subtypeAdapter =
            object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, subType) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val tv = view as TextView
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY)
                    } else {
                        tv.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }

        typeAdapter.setDropDownViewResource(R.layout.spinner_popup)
        subtypeAdapter.setDropDownViewResource(R.layout.spinner_popup)
        binding.complaintSpnType.adapter = typeAdapter
        binding.subSpnType.adapter = subtypeAdapter
        binding.subSpnType.isEnabled = !serviceType.equals("pest", true)
    }

    private fun requestStoragePermission(isCamera: Boolean) {
        try {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent()
                            } else {
                                dispatchGalleryIntent()
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                })
                .withErrorListener {
                    Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT).show()
                }
                .onSameThread()
                .check()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dispatchTakePictureIntent() {
        try {
            val intent = Intent(this, CameraActivity::class.java)
            if (intent.resolveActivity(this.packageManager) != null) {
                // Create the File where the photo should go
                val photoFile = createImageFile()
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile
                )
                mPhotoFile = photoFile
//                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK)
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                val TimeStamp = AppUtils2.getCurrentTimeStamp()
                startActivityForResult(intent, CAMERA_REQUEST)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
//    private fun startCamera() {
//        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//        cameraProviderFuture.addListener(Runnable {
//
//            // Used to bind the lifecycle of cameras to the lifecycle owner
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//            // Preview
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
//                }
//
//            imageCapture = ImageCapture.Builder().build()
//
//            // Select back camera as a default
//            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//            try {
//                // Unbind use cases before rebinding
//                cameraProvider.unbindAll()
//
//                // Bind use cases to camera
//                cameraProvider.bindToLifecycle(
//                    this, cameraSelector, preview, imageCapture
//                )
//
//            } catch (exc: Exception) {
//                Log.e("TAG", "Use case binding failed", exc)
//            }
//
//        }, ContextCompat.getMainExecutor(this))
//    }

    private fun dispatchGalleryIntent() {
        try {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    private fun showSettingsDialog() {
        try {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Need Permissions")
            builder.setMessage(
                "This app needs permission to use this feature. You can grant them in app settings."
            )
            builder.setPositiveButton("GOTO SETTINGS") { dialog: DialogInterface, which: Int ->
                dialog.cancel()
                openSettings()
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
            builder.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun openSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = contentResolver.query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(column_index!!)
        } finally {
            cursor?.close()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Log.d("TAG", "Cam Request")
//            selectedImagePath = mPhotoFile?.path ?: ""
//            if (selectedImagePath != null || selectedImagePath != "") {
//                val bit = BitmapDrawable(resources, selectedImagePath).bitmap
//                Log.d("TAG", bit.toString())
//                val i = (bit.height * (1024.0 / bit.width)).toInt()
//                bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true)
//            }
//            val baos = ByteArrayOutputStream()
//            bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
//            val b = baos.toByteArray()
//            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
//            Log.d("TAG-Image", encodedImage)
//            uploadOnsiteImage(encodedImage)
//        } else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == Activity.RESULT_OK) {
//            val selectedImage = data?.data
//            mPhotoFile = File(getRealPathFromUri(selectedImage).toString())
//            selectedImagePath = mPhotoFile!!.path
//            if (selectedImagePath != null) {
//                val bit = BitmapDrawable(this.resources, selectedImagePath).bitmap
//                val i = (bit.height * (1024.0 / bit.width)).toInt()
//                bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true)
//            }
//            val baos = ByteArrayOutputStream()
//            bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
//            val b = baos.toByteArray()
//            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
//            uploadOnsiteImage(encodedImage)
//        }
//    }

    private fun uploadOnsiteImage(base64: String) {
        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun takePhoto(captureby: String) {
        // Get a stable reference of the
        // modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        // Set up image capture listener,
        // which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    // set the saved uri to the image view
                    findViewById<ImageView>(R.id.iv_capture).visibility = View.VISIBLE
//                    findViewById<ImageView>(R.id.iv_capture).setImageURI(savedUri)

                    progressDialog.show()

                    Log.e("TAG", "UriData:- " + savedUri.toString())

                    goBackToPreview(savedUri, captureby)


//                    val msg = "Photo capture succeeded: $savedUri"
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
//                    Log.d(TAG, msg)
                }
            })


    }

    private fun goBackToPreview(savedUri: Uri, capturebyy: String) {
        Log.e("TAG", "UriData:- " + savedUri.toString())

//        if (captureby == "Image2") {
//            val intent = Intent(this, AddComplaintsActivity::class.java)
//            intent.putExtra("imageUri2",savedUri.toString())
//            intent.putExtra("orderNo",orderNo)
//            intent.putExtra("serviceType",getServiceType)
//            intent.putExtra("captureby","Image2")
//            startActivity(intent)
//            finish()


        setCaptureImage(savedUri, capturebyy)


//        } else if (captureby == "Image1") {
//            val intent = Intent(this, AddComplaintsActivity::class.java)
//            intent.putExtra("imageUri",savedUri.toString())
//            intent.putExtra("orderNo",orderNo)
//            intent.putExtra("serviceType",getServiceType)
//            intent.putExtra("captureby","Image1")
//            startActivity(intent)
//            finish()


//            setCaptureImage(savedUri, captureby)

//            binding.lnrUpload.visibility = View.GONE
//            binding.lnrImage.visibility = View.VISIBLE
//            Picasso.get().load(it.toString()).into(binding.imgUploadedCheque)
//            arraylistImages.add(it)
//        } else if (captureby == "Image3") {
//
//            setCaptureImage(savedUri, captureby)
//
//
//
//
//        }


    }

    private fun startCamera() {
        binding.scrollView2.visibility = View.GONE
        binding.layoutCamera.visibility = View.VISIBLE
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // creates a folder inside internal storage
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    // checks the camera permission
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            // If all permissions granted , then start Camera
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                // If permissions are not granted,
                // present a toast to notify the user that
                // the permissions were not granted.
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "CameraXGFG"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 20
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }


    override fun onResume() {
        super.onResume()
//        getFromServiceType()
        getComplaintReason("Pest")


    }


    override fun onBackPressed() {
        if (binding.layoutCamera.isVisible == true) {
            binding.layoutCamera.visibility = View.GONE
            binding.scrollView2.visibility = View.VISIBLE

        } else {
            super.onBackPressed()
            SharedPreferenceUtil.setData(this, "Image1", "").toString()
            SharedPreferenceUtil.setData(this, "Image2", "").toString()
            SharedPreferenceUtil.setData(this, "Image3", "").toString()
//        this.supportFragmentManager.beginTransaction()
//            .replace(R.id.container, OrdersFragment.newInstance()).addToBackStack("AccountFragment").commit();
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {


        }
    }
}