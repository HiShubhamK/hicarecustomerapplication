package com.ab.hicareservices.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentAccountBinding
import com.ab.hicareservices.ui.view.activities.ComplaintsActivity
import com.ab.hicareservices.ui.view.activities.HelpActivity
import com.ab.hicareservices.ui.view.activities.LoginActivity
import com.ab.hicareservices.ui.view.activities.ReferralActivity

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


        binding.edtAccount.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment.newInstance()).addToBackStack("AccountFragment").commit();
        }

        binding.constraintorderid.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, OrdersFragment.newInstance()).addToBackStack("AccountFragment").commit();
        }

        binding.constraintcomplaints.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ComplaintFragment.newInstance()).addToBackStack("AccountFragment").commit();

        }
        binding.constraintreferBtn.setOnClickListener {
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