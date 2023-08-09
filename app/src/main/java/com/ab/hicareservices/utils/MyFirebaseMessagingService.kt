package com.ab.hicareservices.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.ui.view.activities.HomeActivity
import com.ab.hicareservices.ui.view.activities.ProductDetailActivity
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso


class MyFirebaseMessagingService : FirebaseMessagingService() {

    var IsSticky: Boolean = false

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object {
        var CHANNEL_ID = "my_notification_channel"
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notifydata = remoteMessage.data
//        val imageUrll = remoteMessage.data["imageUrl"]


//        var imageUrll = remoteMessage.notification?.imageUrl.toString()
        var imageUrll =
            "https://s3.ap-south-1.amazonaws.com/hicare-others/8b88c42f-f538-4f1e-9d82-1c085035d9f1.png"
//        val json = JSONObject(notifydata.toString())
        var ActivityName = notifydata["ActivityName"].toString()
//        var ActivityName =
//            "Product|21|3778|400605|8976399055|https://s3.ap-south-1.amazonaws.com/hicare-others/8b88c42f-f538-4f1e-9d82-1c085035d9f1.png"
        IsSticky = notifydata.get("IsSticky").toBoolean()
        var IsProduct = notifydata.get("IsProduct").toBoolean()
        var IsService = notifydata.get("IsService").toBoolean()
        var IsHidden = notifydata.get("IsHidden").toBoolean()
        Log.e(
            "Notification:-",
            "ActivityName: " + ActivityName.toString() + "IsSticky: " + IsSticky + " IsProduct: " + IsProduct + " IsService: " + IsService + " imageUrll: " + imageUrll
        )
        if (IsSticky == true) {
            remoteMessage.notification?.let {
//                FloatNotify(it.title ?: "", it.body ?: "",it.channelId)

                showNotification(
                    it.title ?: "",
                    it.body ?: "",
                    it.channelId,
                    imageUrll,
                    ActivityName,
                    IsSticky,
                    IsProduct
                )
            }
        } else {
            remoteMessage.notification?.let {
                showNotification(
                    it.title ?: "",
                    it.body ?: "",
                    it.channelId,
                    imageUrll,
                    ActivityName,
                    IsSticky,
                    IsProduct
                )
//                FloatNotify(it.title ?: "", it.body ?: "",it.channelId)
            }
        }
        // Handle the received notification message here.


    }


    private fun FloatNotify(title: String, message: String, channelId: String?) {
        val notificationBuilder = channelId?.let {
            NotificationCompat.Builder(this, it)
                .setSmallIcon(com.ab.hicareservices.R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Set the priority to HIGH for heads-up display
                .setAutoCancel(true)
        }

// Show the notification
        val notificationId = Math.random().toInt() // Unique ID for the notification
        val notificationManager = NotificationManagerCompat.from(this)
        notificationBuilder?.build()?.let { notificationManager.notify(notificationId, it) }
    }

    private fun showNotification(
        title: String,
        message: String,
        channelId: String?,
        imageUrl: String?,
        ActivityName: String,
        IsSticky: Boolean,
        IsProduct: Boolean
    ) {
        if (IsProduct == true) {

            val info = ActivityName
            var delimiter = "|"

            val parts = info.split(delimiter)
            Log.e("TAG", "Splitedactivityname: " + parts)
            val intenttype = parts[0].toString()
            val productId = parts[1].toString()
            val custid = parts[2].toString()
            val pincode = parts[3].toString()
            val mobile = parts[4].toString()
            val imageUrl2 = parts[5].toString()
            val imageUrlNew = imageUrl

            SharedPreferenceUtil.setData(
                this,
                "pincode",
                pincode
            )

            SharedPreferenceUtil.setData(
                this,
                "customerid",
                custid
            )


            CHANNEL_ID = channelId.toString()

            val intent = Intent(this, ProductDetailActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP


            intent.putExtra("productid", productId)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


            val notificationLayout =
                RemoteViews("com.ab.hicareservices", com.ab.hicareservices.R.layout.layout_notification)
            notificationLayout.setTextViewText(com.ab.hicareservices.R.id.notificationtitle, title)
            notificationLayout.setTextViewText(com.ab.hicareservices.R.id.notificationtext, message)
            if (imageUrlNew != null) {
                notificationLayout.setImageViewUri(
                    com.ab.hicareservices.R.id.image,
                    imageUrlNew?.toUri()
                )
//                val target = notificationLayout.setImageViewUri(R.id.image, ImageView::class.java)
//                Glide
//                    .with(applicationContext)
//                    .load(imageUrlNew?.toUri()) // the uri you got from Firebase
//                    .centerCrop()
//                    .into(com.ab.hicareservices.R.id.image);
//                val target = notificationLayout.findViewById(R.id.notificationImageView, ImageView::class.java)
//
//                Picasso.get().load(imageUrlNew?.toUri()).into(
//                    com.ab.hicareservices.R.id.image)

            }


            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(com.ab.hicareservices.R.drawable.ic_notification)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        com.ab.hicareservices.R.mipmap.ic_launcher
                    )
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCustomContentView(notificationLayout)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle()) // Required for custom views


            // Add the FLAG_NO_CLEAR flag to make the notification sticky
            if (IsSticky) {
                builder.setOngoing(true)
            } else {
                builder.setOngoing(false)
            }


            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            // Create the notification channel for Android Oreo and higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Custom Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)
            }
            builder.build().flags.and(Notification.FLAG_AUTO_CANCEL)


            val notificationId = Math.random().toInt() // Use a unique ID for each notification
            notificationManager.notify(notificationId, builder.build())
        } else {
            if (ActivityName == null || ActivityName == "") {

//            val info = ActivityName
//            var delimiter = "|"
//
//            val parts = info.split(delimiter)
//            Log.e("TAG", "Splitedactivityname: " + parts)
//            val intenttype = parts[0].toString()
//            val productId = parts[1].toString()
//            val custid = parts[2].toString()
//            val pincode = parts[3].toString()
//            val mobile = parts[4].toString()
//            val imageUrl2 = parts[5].toString()
                val imageUrlNew = imageUrl
//
//            SharedPreferenceUtil.setData(
//                this,
//                "pincode",
//                pincode
//            )
//
//            SharedPreferenceUtil.setData(
//                this,
//                "customerid",
//                custid
//            )


                CHANNEL_ID = channelId.toString()

                val intent = Intent(this, HomeActivity::class.java)
//            intent.putExtra("productid", productId)
                val pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


                val notificationLayout =
                    RemoteViews(packageName, com.ab.hicareservices.R.layout.layout_notification)
                notificationLayout.setTextViewText(
                    com.ab.hicareservices.R.id.notificationtitle,
                    title
                )
                notificationLayout.setTextViewText(
                    com.ab.hicareservices.R.id.notificationtext,
                    message
                )
                if (imageUrlNew != null) {
                    notificationLayout.setImageViewUri(
                        com.ab.hicareservices.R.id.image,
                        imageUrlNew?.toUri()
                    )
                }


                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(com.ab.hicareservices.R.drawable.ic_notification)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources,
                            com.ab.hicareservices.R.mipmap.ic_launcher
                        )
                    )
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setCustomContentView(notificationLayout)
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle()) // Required for custom views


                // Add the FLAG_NO_CLEAR flag to make the notification sticky
                if (IsSticky) {
                    builder.setOngoing(true)
                } else {
                    builder.setOngoing(false)
                }
                builder.build().flags.and(Notification.FLAG_AUTO_CANCEL)


                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


                // Create the notification channel for Android Oreo and higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        CHANNEL_ID,
                        "Custom Notifications",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    notificationManager.createNotificationChannel(channel)
                }


                val notificationId = Math.random().toInt() // Use a unique ID for each notification
                notificationManager.notify(notificationId, builder.build())
            }
        }

    }

}
//class MyFirebaseMessagingService : FirebaseMessagingService() {
//    var IsSticky:Boolean=false
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        Log.e("Notification", "done: " + remoteMessage.data.toString())
//
//        try {
//
//            if (remoteMessage.notification != null) {
//                if (!remoteMessage.notification.toString().equals("")) {
//                    showNotification(
//                        remoteMessage.notification?.title.toString(),
//                        remoteMessage.notification?.body.toString(),
//                        remoteMessage.notification?.channelId,
//                        remoteMessage.notification?.imageUrl,
//                        remoteMessage.notification?.tag,
//                        remoteMessage.data
//                    )
//                } else {
//                    showNotification(
//                        remoteMessage.notification?.title.toString(),
//                        remoteMessage.notification?.body.toString(),
//                        remoteMessage.notification?.channelId,
//                        remoteMessage.notification?.imageUrl,
//                        remoteMessage.notification?.tag,
//                        remoteMessage.data
//                    )
//                }
//
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }
//
//    // Method to get the custom Design for the display of
//    // notification.
//    private fun getCustomDesign(
//        title: String,
//        message: String,
//        imageUrl: Uri?
//    ): RemoteViews {
//        val remoteViews = RemoteViews("com.ab.hicareservices", R.layout.layout_notification)
//        remoteViews.setTextViewText(R.id.title, title)
//        remoteViews.setTextViewText(R.id.text, message)
//        remoteViews.setImageViewUri(R.id.image, imageUrl)
//        return remoteViews
//    }
//
//    // Method to display the notifications
//    fun showNotification(
//        title: String,
//        message: String,
//        channelId: String?,
//        imageUrl: Uri?,
//        tag: String?,
//        data: Map<String, String>
//    ) {
//        val intent = Intent(this, HomeActivity::class.java)
//        // Assign channel ID
//        val channel_id = channelId
//        val notifydata = data
////        val json = JSONObject(notifydata.toString())
//        var ActivityName = notifydata.get("ActivityName")
//        IsSticky = notifydata.get("IsSticky").toBoolean()
//        var IsProduct = notifydata.get("IsProduct").toBoolean()
//        var IsService = notifydata.get("IsService").toBoolean()
//        var IsHidden = notifydata.get("IsHidden").toBoolean()
//        Log.e(
//            "Notification:-",
//            "ActivityName: " + ActivityName.toString() + "IsSticky: " + IsSticky + " IsProduct: " + IsProduct + " IsService: " + IsService + " IsHidden: " + IsHidden
//        )
//
//        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
//        // the activities present in the activity stack,
//        // on the top of the Activity that is to be launched
//
//
//        if (IsSticky&&isAppInForeground(applicationContext)==false) {
//            intent.addFlags(Intent.FLAG_RECEIVER_NO_ABORT)
//        }else{
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        }
//        // Pass the intent to PendingIntent to start the
//        // next Activity
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_MUTABLE
//        )
//
//        // Create a Builder object using NotificationCompat
//        // class. This will allow control over all the flags
//
//        val remoteViews = RemoteViews("com.ab.hicareservices", R.layout.layout_notification)
//
//        var builder: NotificationCompat.Builder =
//            NotificationCompat.Builder(applicationContext, channel_id.toString())
////                .setSmallIcon(R.drawable.ic_automos)
////                .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_automos))
////                .setAutoCancel(false)
////                .setOnlyAlertOnce(true)
////                .setStyle(NotificationCompat.BigPictureStyle())
////                .setContentTitle(title)
////                .setContentText(message)
////                .setVibrate(longArrayOf(100, 1000, 2000, 340))
//                .setSmallIcon(R.drawable.ic_automos)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_automos))
//                .setColor(ContextCompat.getColor(applicationContext, R.color.red))
//                .setStyle(NotificationCompat.BigPictureStyle())
//                .setStyle(NotificationCompat.BigTextStyle().bigText(title))
//                .setStyle(
//                    NotificationCompat.BigTextStyle().bigText(message).setSummaryText("#HICARE")
//                )
//                .setShowWhen(true)
//                .setAutoCancel(true)
//                .setVibrate(longArrayOf(100, 1000, 2000, 340))
//                .setContentIntent(pendingIntent)
//                .setContent(remoteViews)
//
//
////        remoteViews.setTextViewText(R.id.notificationtitle, title)
////        remoteViews.setTextViewText(R.id.notificationtext, message)
////        remoteViews.setImageViewResource(R.id.image, R.drawable.hicarelogo)
//
//
////        builder = builder.setCustomContentView(
////            getCustomDesign(title, message, imageUrl)
////        )
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?        // Check if the Android Version is greater than Oreo
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationChannel = NotificationChannel(
//                channel_id,
//                "HicareServices",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationChannel.description = message
//            notificationChannel.enableVibration(true)
//            notificationChannel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000)
//            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//
//            notificationManager?.createNotificationChannel(notificationChannel)
//
//        }
//
//
//
//        notificationManager?.notify(1, builder.build())
//    }
//    private fun isAppInForeground(context: Context): Boolean {
//        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
//            val foregroundTaskInfo = am.getRunningTasks(1)[0]
//            val foregroundTaskPackageName = foregroundTaskInfo.topActivity!!.packageName
//            foregroundTaskPackageName.lowercase(Locale.getDefault()) == context.packageName.lowercase(
//                Locale.getDefault()
//            )
//        } else {
//            val appProcessInfo = RunningAppProcessInfo()
//            ActivityManager.getMyMemoryState(appProcessInfo)
//            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
//                return true
//            }
//            val km = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
//            // App is foreground, but screen is locked, so show notification
//            km.inKeyguardRestrictedInputMode()
//        }
//    }
//
//}
