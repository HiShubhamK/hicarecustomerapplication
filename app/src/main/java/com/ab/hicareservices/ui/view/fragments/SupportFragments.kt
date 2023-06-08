package com.ab.hicareservices.ui.view.fragments

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentOrdersBinding
import com.ab.hicareservices.databinding.FragmentSupportFragmentsBinding


class SupportFragments : Fragment() {
    private val requestCall = 1
    lateinit var binding: FragmentSupportFragmentsBinding

    companion object {
        @JvmStatic
        fun newInstance() =
            SupportFragments().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportFragmentsBinding.inflate(inflater, container, false)
        return binding.root
        return inflater.inflate(R.layout.fragment_support_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgLogo.setOnClickListener {
            requireActivity()!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance()).addToBackStack("AccountFragment").commit()
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
        var number:String="8828333888"
        if (number.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), requestCall)
            } else {
                val dial = "tel:$number"
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        } else {
            Toast.makeText(requireActivity(), "Enter Phone Number", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}