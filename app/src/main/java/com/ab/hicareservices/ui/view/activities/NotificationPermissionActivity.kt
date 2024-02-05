package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil

class NotificationPermissionActivity : AppCompatActivity() {
    private val notificationPermissionRequestCode = 1001
    lateinit var progressDialog: ProgressDialog
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_permission)

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)

        val button = findViewById<Button>(R.id.notification)
        val txtnotnow = findViewById<LinearLayout>(R.id.txtnotnow)

        txtnotnow.setOnClickListener {
            progressDialog.show()
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@NotificationPermissionActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
                progressDialog.dismiss()
            },1500)
        }

        button.setOnClickListener {
            progressDialog.show()
            SharedPreferenceUtil.setData(this@NotificationPermissionActivity, "Notificationpermission", true)
            val islogin = SharedPreferenceUtil.getData(
                this@NotificationPermissionActivity,
                "Notificationpermission",
                false
            )

//            Toast.makeText(this,islogin.toString(),Toast.LENGTH_SHORT).show()
            // Check if the permission is already granted
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is already granted, proceed with your logic
                onNotificationPermissionGranted()
            } else {
                // Request the notification permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        notificationPermissionRequestCode
                    )
                }
            }
        }
    }

    // Method to handle logic when notification permission is granted
//    private fun onNotificationPermissionGranted() {
//        if (isNotificationPermissionGranted()) {
//            // Permission granted, proceed with your logic
//            SharedPreferenceUtil.setData(this@NotificationPermissionActivity, "Notificationpermission", true)
//        } else {
//            // Permission denied, you can handle this case accordingly
//            SharedPreferenceUtil.setData(this@NotificationPermissionActivity, "Notificationpermission", false)
//            navigateToHomeActivity()
//        }
//        navigateToHomeActivity()
//    }

    private fun onNotificationPermissionGranted() {
        if (isNotificationPermissionGranted()) {
            // Permission granted, proceed with your logic
//            Toast.makeText(this,"Allow",Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                SharedPreferenceUtil.setData(
                    this@NotificationPermissionActivity,
                    "Notificationpermission",
                    true
                )
                navigateToHomeActivity()
            },1000)
        } else if(!isNotificationPermissionGranted()){
            // Permission denied, you can handle this case accordingly
//            Toast.makeText(this,"denied",Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                SharedPreferenceUtil.setData(
                    this@NotificationPermissionActivity,
                    "Notificationpermission",
                    true
                )
                navigateToHomeActivity()
            },1000)
        }
    }

    // Method to navigate to HomeActivity
    private fun navigateToHomeActivity() {
        progressDialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            val intent = Intent(this@NotificationPermissionActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        },1500)
    }

    private fun isNotificationPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return notificationManager.areNotificationsEnabled()
        } else {
            // For versions prior to Android O, notification permission is granted by default
            return true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == notificationPermissionRequestCode) {
            progressDialog.show()
            // Check if the permission is granted after the user responds to the permission request
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                progressDialog.dismiss()
                onNotificationPermissionGranted()
            } else {
                // Permission denied, handle this case accordingly
                Handler(Looper.getMainLooper()).postDelayed({
                    progressDialog.dismiss()
                    SharedPreferenceUtil.setData(
                        this@NotificationPermissionActivity,
                        "Notificationpermission",
                        false
                    )
                    val intent =
                        Intent(this@NotificationPermissionActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                },1000)
            // You might want to show a message to the user or take other actions
            }
        }
    }
}

//class NotificationPermissionActivity : AppCompatActivity() {
//    private val notificationPermissionRequestCode = 1001
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_notification_permission)
//
//        val button = findViewById<Button>(R.id.notification)
//
//
//        button.setOnClickListener {
//
//            if ( ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.POST_NOTIFICATIONS
//                )
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                // When permission is granted
//                // Call method
////            getCurrentLocations()
//
//            } else {
//
//                // When permission is not granted
//                // Call method
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(
//                        arrayOf(
//                            Manifest.permission.POST_NOTIFICATIONS
//                        ),
//                        1001
//                    )
//                }
//            }
//
//
//        }
//
//
//    }
//
//
//    private fun isNotificationPermissionGranted(): Boolean {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            return notificationManager.areNotificationsEnabled()
//        } else {
//            // For versions prior to Android O, notification permission is granted by default
//            return true
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == notificationPermissionRequestCode) {
//            if (isNotificationPermissionGranted()) {
//                SharedPreferenceUtil.setData(this@NotificationPermissionActivity, "Notificationpermission", true)
//
//            // Permission granted, proceed with your logic
//            } else {
//                SharedPreferenceUtil.setData(this@NotificationPermissionActivity, "Notificationpermission", true)
//
//                // Permission denied, you can handle this case accordingly
//            }
//            val intent = Intent(this@NotificationPermissionActivity, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//}