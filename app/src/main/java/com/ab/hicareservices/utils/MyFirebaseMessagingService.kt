package com.ab.hicareservices.utils

import com.ab.hicareservices.R
import com.ab.hicareservices.ui.view.activities.HomeActivity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("Notification","done: "+remoteMessage.data.toString())

        try {

            if(remoteMessage.notification!=null){
                if (!remoteMessage.notification.toString().equals("")) {
                    showNotification(
                        remoteMessage.notification?.title.toString(),
                        remoteMessage.notification?.body.toString(),
                        remoteMessage.notification?.channelId,
                        remoteMessage.notification?.imageUrl,
                        remoteMessage.notification?.tag,
                        remoteMessage.data
                    )
                } else {
                    showNotification(
                        remoteMessage.notification?.title.toString(),
                        remoteMessage.notification?.body.toString(),
                        remoteMessage.notification?.channelId,
                        remoteMessage.notification?.imageUrl,
                        remoteMessage.notification?.tag,
                        remoteMessage.data
                    )
                }

            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    // Method to get the custom Design for the display of
    // notification.
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews {
        val remoteViews = RemoteViews("com.ab.hicareservices", R.layout.layout_notification)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.text, message)
        remoteViews.setImageViewResource(
            R.id.image,
            R.drawable.hicarelogo
        )
        return remoteViews
    }

    // Method to display the notifications
    fun showNotification(
        title: String,
        message: String,
        channelId: String?,
        imageUrl: Uri?,
        tag: String?,
        data: MutableMap<String, String>
    ) {
        val intent = Intent(this, HomeActivity::class.java)
        // Assign channel ID
        val channel_id = channelId
        val notifydata = data
        val json = JSONObject(notifydata.toString())
        val ActivityName = json.getString("ActivityName")
        val IsSticky = json.getBoolean("IsSticky")
        val IsProduct = json.getString("IsProduct")
        val IsService = json.getString("IsService")
        val IsHidden = json.getString("IsHidden")
        Log.e("Notification:-","ActivityName: "+ActivityName +"IsSticky: "+IsSticky+" IsProduct: "+IsProduct+" IsService: "+IsService+" IsHidden: "+IsHidden)

        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Pass the intent to PendingIntent to start the
        // next Activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_MUTABLE
        )

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags

        val remoteViews = RemoteViews("com.ab.hicareservices", R.layout.layout_notification)

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channel_id.toString())
                .setSmallIcon(R.drawable.reviewdialog_round_icon)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
                .setContentIntent(pendingIntent)
                .setContent(remoteViews)

        remoteViews.setTextViewText(R.id.notificationtitle, title)
        remoteViews.setTextViewText(R.id.notificationtext, message)
        remoteViews.setImageViewResource(R.id.image, R.drawable.hicarelogo)


        builder = builder.setCustomContentView(
            getCustomDesign(title, message)
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channel_id,
                "HicareServices",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        notificationManager!!.notify(0, builder.build())
    }
}