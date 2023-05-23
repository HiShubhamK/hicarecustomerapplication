package com.ab.hicareservices.ui.view.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.ActivityLoginBinding
import com.ab.hicareservices.ui.viewmodel.OtpViewModel
import com.ab.hicareservices.utils.AppUtils2

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val viewModel: OtpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkUserStatus()

        //viewModel = ViewModelProvider(this, OtpViewModelFactory(MainRepository(api))).get(OtpViewModel::class.java)
//
//        slideLeft(binding.genieIv)
//
//        binding.notHaveAccTv.setOnClickListener {
//            val intent = Intent(this, RegistrationActivity::class.java)
//            startActivity(intent)
//        }

        binding.signInBtn.setOnClickListener {
            val mobileNo = binding.mobileNoEt.text.toString()
            if (mobileNo.length != 10){
                binding.mobileNoEt.setError("Invalid Phone Number")
                return@setOnClickListener
            }
            binding.signInBtn.isEnabled = false
            getOtp(mobileNo)
        }
    }

    override fun onResume() {
  //      binding.signInBtn.isEnabled = true
        super.onResume()
    }

    private fun getOtp(mobileNo: String){
        viewModel.getOtp(mobileNo)
        viewModel.otpResponse.observe(this, Observer {
            if (it.isSuccess == true) {
                val intent = Intent(this, OTPActivity::class.java)
                intent.putExtra("mobileNo", mobileNo)
                intent.putExtra("otp", it.data)
                startActivity(intent)
            }else{
//                binding.progressBar.visibility = View.GONE
//                binding.signInBtn.isEnabled = true
//                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
//    fun slideLeft(view: View){
//        view.animate()
//            .translationX(-20f)
//            .setDuration(1000)
//            .setInterpolator(LinearInterpolator())
//            .setListener(object : AnimatorListenerAdapter(){
//                override fun onAnimationEnd(animation: Animator?) {
//                    super.onAnimationEnd(animation)
//                    slideRight(view)
//                }
//            }).start()
//    }
//
//    fun slideRight(view: View){
//        view.animate()
//            .translationX(20f)
//            .setDuration(1000)
//            .setInterpolator(LinearInterpolator())
//            .setListener(object : AnimatorListenerAdapter(){
//                override fun onAnimationEnd(animation: Animator?) {
//                    super.onAnimationEnd(animation)
//                    slideLeft(view)
//                }
//            }).start()
//    }
//
    private fun checkUserStatus(){
        val mobileNo = SharedPreferenceUtil.getData(this, "mobileNo", "-1").toString()
        AppUtils2.TOKEN = SharedPreferenceUtil.getData(this, "bToken", "").toString()
        if (mobileNo != "-1"){
            val i = Intent(this, HomeActivity::class.java)
            //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        }
    }
}