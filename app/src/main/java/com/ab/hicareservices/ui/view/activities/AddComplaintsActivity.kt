package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.ab.hicareservices.BuildConfig
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityAddComplaintsBinding
import com.ab.hicareservices.ui.view.fragments.OrderDetailsFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragment
import com.ab.hicareservices.ui.viewmodel.CComplaintViewModel
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

class AddComplaintsActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddComplaintsBinding
    var selectedCType = ""
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

    var REQUEST_CODE = 1234
    private lateinit var imageuri1: Uri
    private lateinit var imageuri2: Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddComplaintsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        orderNo = intent.getStringExtra("orderNo").toString()
        getServiceType = intent.getStringExtra("serviceType").toString()

        service_url_image = intent.getStringExtra("service_url_image").toString()
        captureby = intent.getStringExtra("captureby").toString()
//        service_url_image = intent.getStringExtra(SERVICE_TYPE_IMG).toString()
        arraylistImages = ArrayList()
        val extras = getIntent().extras
        if(orderNo!="") {
            binding.bottomheadertext.visibility=View.VISIBLE
            binding.bottomheadertext.text = orderNo
        }else{
            binding.bottomheadertext.visibility=View.GONE
            binding.bottomheadertext.text = orderNo

        }


//        binding.orderNoEt.text = orderNo

        //complaintViewModel = ViewModelProvider(this, CComplaintViewModelFactory(MainRepository(api))).get(CComplaintViewModel::class.java)

        typeHash = HashMap()
        binding.imgLogo.setOnClickListener {
            finish()
        }


        //serviceType = "pest"
//        getFromServiceType()
        //getComplaintReason(serviceType)

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
                    //Log.d("TAG", selectedType)
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
                //Log.d("TAG", selectedType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.saveBtn.setOnClickListener {
            val orderNo = binding.bottomheadertext.text.toString().trim()
            val serviceNo = binding.serviceNoEt.text.toString().trim()
            val complaintTitle = binding.complaintTitleEt.text.toString().trim()
            val complaintDescr = binding.complaintDescrEt.text.toString().trim()
            if (serviceType.equals("pest", true)) {
                if (orderNo != "" && complaintTitle != "" && complaintDescr != "" && selectedCType != "") {
                    addComplaint(
                        orderNo, serviceNo, selectedCType,
                        selectedCSubType, complaintTitle, complaintDescr, serviceType
                    )
                } else {
                    Toast.makeText(this, "Please fill data properly.", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (orderNo != "" && complaintTitle != "" && complaintDescr != ""
                    && selectedCType != "" && selectedCSubType != ""
                ) {
                    addComplaint(
                        orderNo, serviceNo, selectedCType,
                        selectedCSubType, complaintTitle, complaintDescr, serviceType
                    )
                } else {
                    Toast.makeText(this, "Please fill data properly", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.lnrUpload.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("orderNo", orderNo)
            intent.putExtra("getServiceType", getServiceType)
            intent.putExtra("captureby", "1")
            startActivity(intent)
        }
        binding.lnrUpload2.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("orderNo", orderNo)
            intent.putExtra("getServiceType", getServiceType)
            intent.putExtra("captureby", "2")
            startActivity(intent)
        }

        try {
            imageuri1 = Uri.parse(extras!!.getString("imageUri"))
            if (imageuri1 != null) {
                setCaptureImage(imageuri1, captureby)
            }
//            if (arraylistImages[0] != "") {

            imageuri2 = Uri.parse(extras!!.getString("imageUri2"))
            if (imageuri2 != null) {
                setCaptureImage(imageuri2, captureby)
            }
//            }else{
////                binding.lnrUpload2.isEnabled=false
//
//            }

        } catch (e: Exception) {
            e.printStackTrace()
        }



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
            binding.progressBar.visibility = View.VISIBLE

            if (captureby == "1") {
                uploadattachment(encodedString, captureby)

            }else if (captureby=="2"){
                uploadattachment(encodedString, captureby)

            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun getComplaintReason(serviceType: String) {
        val type = ArrayList<String>()
        val subtype = ArrayList<String>()
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
        complaintViewModel.getComplaintReasons(serviceType)
    }

    private fun addComplaint(
        orderNo: String, serviceNo: String,
        complaintType: String, complaintSubType: String,
        complaintTitle: String, complaintDescr: String, serviceType: String
    ) {
        val hashMap = HashMap<String, Any>()
        hashMap["OrderNo"] = orderNo
        hashMap["ServiceNo"] = serviceNo
        hashMap["ComplaintType"] = complaintType
        hashMap["ComplaintSubType"] = complaintSubType
        hashMap["ComplaintTitle"] = complaintTitle
        hashMap["ComplaintDescription"] = complaintDescr
        hashMap["ServiceType"] = serviceType
        hashMap["Source"] = "mobileApp"
        hashMap["SubSource"] = "mobileApp"
        hashMap["attachments"] = arraylistImages

        complaintViewModel.createComplaint(hashMap)
        complaintViewModel.createComplaintResponse.observe(this, {
            if (it.isSuccess == true) {
                finish()
                Toast.makeText(this, "Complaint Created", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadattachment(
        imagebase64: String,
        captureby: String
    ) {
        if (captureby == "1") {
            val hashMap = HashMap<String, Any>()
            hashMap["FileName"] = "complaint1"
            hashMap["FileExtension"] = ".jpeg"
            hashMap["FileData"] = imagebase64


            uploadAttachmentViewModel.UploadAttachment(hashMap)
            uploadAttachmentViewModel.attachmenturl.observe(this, {
                if (it != "") {
                    binding.lnrImage.visibility = View.VISIBLE
                    binding.lnrUpload.visibility = View.GONE
                    arraylistImages.add(0, it)
                    if (it != null || it != "") {
                        Picasso.get().load(it).into(binding.imgUploadedCheque)
                    }
                    Log.e("TAG","imageArray:- "+arraylistImages.toString())

                    binding.progressBar.visibility = View.GONE

                }

            })

            binding.imageCancel.setOnClickListener {
                arraylistImages.removeAt(0)
                binding.lnrImage.visibility = View.GONE
                binding.lnrUpload.visibility = View.VISIBLE
            }
        } else if (captureby == "2") {
            val hashMap = HashMap<String, Any>()
            hashMap["FileName"] = "complaint1"
            hashMap["FileExtension"] = ".jpeg"
            hashMap["FileData"] = imagebase64


            uploadAttachmentViewModel.UploadAttachment(hashMap)
            uploadAttachmentViewModel.attachmenturl.observe(this, {
                if (it != "") {
                    binding.lnrImage2.visibility = View.VISIBLE
                    binding.lnrUpload2.visibility = View.GONE
                    arraylistImages.add(1, it)
                    if (it != null || it != "") {
                        Picasso.get().load(it).into(binding.imgUploadedCheque2)
                    }
                    Log.e("TAG","imageArray2:- "+arraylistImages.toString())

                    binding.progressBar.visibility = View.GONE

                }

            })

            binding.imageCancel2.setOnClickListener {
                arraylistImages.removeAt(1)
                binding.lnrImage2.visibility = View.GONE
                binding.lnrUpload2.visibility = View.VISIBLE
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
//            if (TmsUtils.mPermissions) {
//                TmsUtils.init(this, REQUEST_CODE)
//            } else {
//                TmsUtils.verifyPermissions(this, REQUEST_CODE)
//            }
        }
    }

    override fun onResume() {
        super.onResume()
//        getFromServiceType()
        getComplaintReason("Pest")


    }

    override fun onDestroy() {
        super.onDestroy()
    }


//    override fun onBackPressed() {
//        super.onBackPressed()
//        this.supportFragmentManager.beginTransaction()
//            .replace(R.id.container, OrdersFragment.newInstance()).addToBackStack("AccountFragment").commit();
//        finish()
//    }

}