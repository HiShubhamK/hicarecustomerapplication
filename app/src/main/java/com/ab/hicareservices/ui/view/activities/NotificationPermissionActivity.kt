package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil

class NotificationPermissionActivity : AppCompatActivity() {
    private val notificationPermissionRequestCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_permission)

        val button = findViewById<Button>(R.id.notification)

        button.setOnClickListener {
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
    private fun onNotificationPermissionGranted() {
        if (isNotificationPermissionGranted()) {
            // Permission granted, proceed with your logic
            SharedPreferenceUtil.setData(this@NotificationPermissionActivity, "Notificationpermission", true)
        } else {
            // Permission denied, you can handle this case accordingly
            SharedPreferenceUtil.setData(this@NotificationPermissionActivity, "Notificationpermission", false)
        }
        navigateToHomeActivity()
    }

    // Method to navigate to HomeActivity
    private fun navigateToHomeActivity() {
        val intent = Intent(this@NotificationPermissionActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
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
            // Check if the permission is granted after the user responds to the permission request
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onNotificationPermissionGranted()
            } else {
                // Permission denied, handle this case accordingly
                SharedPreferenceUtil.setData(this@NotificationPermissionActivity, "Notificationpermission", false)
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