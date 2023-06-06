package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.splash)
        binding.splashimg.startAnimation(animation)


        Handler(Looper.getMainLooper()).postDelayed({
            val islogin=SharedPreferenceUtil.getData(this, "IsLogin", false)
            if(islogin==true){
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                finish()
            }else{
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }, 3000)

    }
}