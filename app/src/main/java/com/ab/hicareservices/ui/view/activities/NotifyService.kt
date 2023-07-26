package com.ab.hicareservices.ui.view.activities

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ab.hicareservices.data.SharedPreferenceUtil

class NotifyService : Service() {

    var NOTIFICATION_CHANNEL_ID = SharedPreferenceUtil.getData(this, "notificationid", "").toString()
    private val default_notification_channel_id = "default"
    fun NotifyService() {}

    override fun onBind(intent: Intent): IBinder? {

        val notificationIntent = Intent(applicationContext, MyOrderActivity::class.java)
        notificationIntent.putExtra("fromNotification", true)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, default_notification_channel_id)
        mBuilder.setContentTitle("My Notification")
        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setContentText("Notification Listener Service Example")
        mBuilder.setSmallIcon(R.drawable.sym_def_app_icon)
        mBuilder.setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("TAG","Notiffyy: "+NOTIFICATION_CHANNEL_ID)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            assert(mNotificationManager != null)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val default_notification_channel_id = "default"
    }
}