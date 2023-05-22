package com.ab.hicareservices.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ab.hicareservices.R
import com.ab.hicareservices.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imgLogo.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, AccountFragment.newInstance()).commit();
        }

        binding.txtedits.setOnClickListener {
            binding.txtedits.visibility = View.GONE
            binding.lnrEditPersonalDetails.visibility = View.VISIBLE
            binding.lnrpersonalDetails.visibility = View.GONE
            binding.txtsaves.visibility = View.VISIBLE
        }

        binding.txtsaves.setOnClickListener {
            binding.lnrEditPersonalDetails.visibility = View.VISIBLE
            binding.lnrpersonalDetails.visibility = View.GONE
            binding.txtsaves.visibility = View.GONE
            binding.txtedits.visibility = View.VISIBLE
        }

    }

}