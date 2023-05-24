package com.ab.hicareservices.ui.view.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ab.hicareservices.BuildConfig
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityReferralBinding
import com.ab.hicareservices.ui.viewmodel.ReferralViewModel


class ReferralActivity : AppCompatActivity() {

    lateinit var binding: ActivityReferralBinding
    private val referralViewModel: ReferralViewModel by viewModels()
    var referralCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferralBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //referralViewModel = ViewModelProvider(this, ReferralViewModelFactory(MainRepository(api))).get(ReferralViewModel::class.java)

        getReferralCode("9967994682")

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.shareBtn.setOnClickListener {
            if (referralCode != "") {
                shareLink()
            }else{
                Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.whatsappBtn.setOnClickListener {
            if (isAppInstalled("com.whatsapp")){
                shareOnWhatsapp()
            }else{
                shareLink()
                Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareLink(){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HiCare Services")
        var shareMessage = "\nI availed services from HiCare and I am happy to recommend them." +
                "\nYou can use referral code $referralCode to avail 15% discount for any pest control," +
                "bird netting or professional cleaning needs.\n" +
                "The code will be valid for 3 months on their website https://www.hicare.in and on 8828333888\n\n"
        shareMessage =
            "${shareMessage}Download HiCare app from the google play store\n" +
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    private fun shareOnWhatsapp(){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.setPackage("com.whatsapp")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HiCare Services")
        var shareMessage = "\nI availed services from HiCare and I am happy to recommend them." +
                "\nYou can use referral code $referralCode to avail 15% discount for any pest control," +
                "bird netting or professional cleaning needs.\n" +
                "The code will be valid for 3 months on their website https://www.hicare.in and on 8828333888\n\n"
        shareMessage =
            "${shareMessage}Download the app from google play store\n" +
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    private fun getReferralCode(mobileNo: String){
        binding.progressBar.visibility = View.VISIBLE
        referralViewModel.getReferralCode(mobileNo)
        referralViewModel.referralResponse.observe(this, {
            if (it != null) {
                if (it.isSuccess == true) {
                    binding.progressBar.visibility = View.GONE
                    binding.referralCodeTv.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.referralCodeTv.text = it.data.toString()
                    binding.txtTitle.text = ""
                    binding.txtDescription.text = ""
                    binding.tvShareText.text = ""
                    referralCode = it.data.toString()
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.referralCodeTv.setTextColor(ContextCompat.getColor(this, R.color.red))
                    binding.referralCodeTv.text = "Error"
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.progressBar.visibility = View.GONE
                binding.referralCodeTv.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.referralCodeTv.text = "Null"
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isAppInstalled(packageName: String): Boolean {
        val pm = packageManager
        return try {
                pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
    }
}