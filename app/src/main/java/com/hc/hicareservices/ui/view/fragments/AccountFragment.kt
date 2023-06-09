package com.hc.hicareservices.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hc.hicareservices.R
import com.hc.hicareservices.data.SharedPreferenceUtil
import com.hc.hicareservices.databinding.FragmentAccountBinding
import com.hc.hicareservices.ui.view.activities.ComplaintsActivity
import com.hc.hicareservices.ui.view.activities.HelpActivity
import com.hc.hicareservices.ui.view.activities.LoginActivity
import com.hc.hicareservices.ui.view.activities.ReferralActivity

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.complaintsLayout.setOnClickListener {
            val intent = Intent(requireContext(), ComplaintsActivity::class.java)
            startActivity(intent)
        }
        binding.referBtn.setOnClickListener {
            val intent = Intent(requireContext(), ReferralActivity::class.java)
            startActivity(intent)
        }
        binding.help.setOnClickListener {
            val intent= Intent(requireContext(), HelpActivity::class.java)
            startActivity(intent)
        }
        binding.signOut.setOnClickListener {
            signOut()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AccountFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun signOut(){
        SharedPreferenceUtil.setData(requireContext(), "mobileNo", "-1")
        SharedPreferenceUtil.setData(requireContext(), "bToken", "")
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}