package com.ab.hicareservices.utils

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner


class BaseApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        try {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
        isActivityVisible = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        // App in foreground
        isActivityVisible = true
    }

    companion object {
        var isActivityVisible = false
    }
}
