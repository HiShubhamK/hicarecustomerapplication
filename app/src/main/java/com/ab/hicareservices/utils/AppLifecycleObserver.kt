package com.ab.hicareservices.utils

// AppLifecycleObserver.kt
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ab.hicareservices.ui.view.activities.SplashActivity

class AppLifecycleObserver(private val context: Context) : LifecycleObserver {



    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        MyApp.getAppContext().updateAppBackgroundState(false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        // App goes to the background
        MyApp.getAppContext().updateAppBackgroundState(true)

        // Launch the splash screen when the app goes to the background
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}
