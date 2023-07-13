package com.ab.hicareservices.ui.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.ActivityAddToCartBinding
import com.ab.hicareservices.databinding.ActivitySupportBinding
import com.ab.hicareservices.ui.view.fragments.HomeFragment

class SupportActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupportBinding
    private val requestCall = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        binding = ActivitySupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgLogo.setOnClickListener {
            onBackPressed()
            val intent=Intent(this@SupportActivity,HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        binding.textemail.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.type = "message/rfc822"
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("info@hicare.in"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "My subject")
            startActivity(Intent.createChooser(intent, "Email via..."))
        }

        binding.getcall.setOnClickListener {
            makePhoneCall()
        }
    }

    private fun makePhoneCall() {
        var number: String = "8828333888"
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val dial = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent=Intent(this@SupportActivity,HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}
