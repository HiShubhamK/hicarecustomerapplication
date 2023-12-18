package com.ab.hicareservices.ui.view.activities

import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import com.ab.hicareservices.utils.AppUtils2
import com.ab.hicareservices.utils.DesignToast


class ReferralActivity : AppCompatActivity() {

    lateinit var binding: ActivityReferralBinding
    private val referralViewModel: ReferralViewModel by viewModels()
    var referralCode = ""
    var facebooktxt = ""
    var twittertxt = ""
    var whatsapptxt = ""
    lateinit var progressDialog: ProgressDialog

    var othertxt = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReferralBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressDialog = ProgressDialog(this, R.style.TransparentProgressDialog)
        progressDialog.setCancelable(false)
        //referralViewModel = ViewModelProvider(this, ReferralViewModelFactory(MainRepository(api))).get(ReferralViewModel::class.java)

        getReferralCode(AppUtils2.mobileno)

        binding.imgLogo.setOnClickListener {
            finish()
        }

        binding.shareBtn.setOnClickListener {
            if (referralCode != "") {
                shareLink()
            } else {
                DesignToast.makeText(this,"Please wait...", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.referralCodeTv.setOnClickListener {
            if (binding.referralCodeTv.text != "") {
                copyToClipboard(binding.referralCodeTv.text)
            }
        }

        binding.whatsappBtn.setOnClickListener {
            if (isAppInstalled("com.whatsapp")) {
                shareOnWhatsapp()
            } else {
                shareLink()
                DesignToast.makeText(this,"Whatsapp not installed", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                Toast.makeText(this, "Whatsapp not installed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnTwitter.setOnClickListener {
            if (isAppInstalled("com.whatsapp")) {
                shareOnTwitter()
            } else {
                shareLink()
                DesignToast.makeText(this,"Twitter not installed", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();


//                Toast.makeText(this, "Twitter not installed", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnFacebook.setOnClickListener {
            if (isAppInstalled("com.whatsapp")) {
                shareOnFacebook()
            } else {
                shareLink()
                DesignToast.makeText(this,"Facebook not installed", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

//                Toast.makeText(this, "Facebook not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HiCare Services")
        var shareMessage = othertxt
        shareMessage =
            "${shareMessage}Download HiCare app from the google play store\n" +
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    private fun shareOnWhatsapp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.setPackage("com.whatsapp")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HiCare Services")
        var shareMessage = whatsapptxt
        shareMessage =
            "${shareMessage}Download the app from google play store\n" +
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    private fun shareOnTwitter() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.setPackage("com.twitter.android")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HiCare Services")
        var shareMessage = twittertxt
        shareMessage =
            "${shareMessage}Download the app from google play store\n" +
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    private fun shareOnFacebook() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.setPackage("com.facebook.katana")
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HiCare Services")
        var shareMessage = facebooktxt
        shareMessage =
            "${shareMessage}Download the app from google play store\n" +
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    private fun getReferralCode(mobileNo: String) {
        progressDialog.show()
        referralViewModel.getReferralCode(mobileNo)
        referralViewModel.referralResponse.observe(this, {
            if (it != null) {
                if (it.IsSuccess == true) {
                    binding.progressBar.visibility = View.GONE
                    binding.referralCodeTv.setTextColor(ContextCompat.getColor(this, R.color.black))
                    binding.referralCodeTv.text = it.Data!!.ReferralCode
                    binding.txtTitle.text = it.Data!!.PrimaryMessage
                    binding.txtDescription.text = it.Data!!.SecondaryMessage
                    binding.tvShareText.text = it!!.Data!!.ShareText.toString()
                    facebooktxt = it!!.Data!!.FBShareText.toString()
                    whatsapptxt = it!!.Data!!.WAShareText.toString()
                    twittertxt = it!!.Data!!.TwitterShareText.toString()
                    othertxt = it!!.Data!!.OtherShareText.toString()
                    referralCode = it.Data!!.ReferralCode.toString()
                    progressDialog.dismiss()

                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.referralCodeTv.setTextColor(ContextCompat.getColor(this, R.color.red))
                    binding.referralCodeTv.text = "Error"
                    binding.txtTitle.text = ""
                    binding.txtDescription.text = ""
                    binding.tvShareText.text = ""
//                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    DesignToast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();

                    progressDialog.dismiss()

                }
            } else {
                binding.progressBar.visibility = View.GONE
                binding.referralCodeTv.setTextColor(ContextCompat.getColor(this, R.color.red))
                binding.referralCodeTv.text = "Null"
                binding.txtTitle.text = ""
                binding.txtDescription.text = ""
                binding.tvShareText.text = ""
                DesignToast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT, DesignToast.TYPE_ERROR).show();


//                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
            progressDialog.dismiss()
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

    fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        clipboard?.setPrimaryClip(ClipData.newPlainText("", text))
//        Toast.makeText(applicationContext, "Copied!", Toast.LENGTH_SHORT).show();
        DesignToast.makeText(this,"Copied!", Toast.LENGTH_SHORT, DesignToast.TYPE_SUCCESS).show();

    }
}