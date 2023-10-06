package com.ab.hicareservices.utils

// ConnectivityReceiver.kt
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager

class ConnectivityReceiver(private val listener: ConnectivityChangeListener) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            val isConnected = isNetworkConnected(context)
            listener.onConnectivityChanged(isConnected)
        }
    }

    private fun isNetworkConnected(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}

