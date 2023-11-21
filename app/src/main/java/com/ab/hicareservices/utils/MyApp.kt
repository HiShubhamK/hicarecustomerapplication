package com.ab.hicareservices.utils

// MyApp.kt
import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner

open class MyApp : Application() {

    private var isInBackground = true

    override fun onCreate() {
        super.onCreate()
        try {
            instance=MyApp()
            ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isAppInBackground(): Boolean {
        return isInBackground
    }

    fun updateAppBackgroundState(isBackground: Boolean) {
        isInBackground = isBackground
    }

    companion object {

        private lateinit var instance: MyApp
        fun getAppContext(): MyApp {
            return instance
        }
    }
}
