package com.ab.hicareservices.ui.view.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.ab.hicareservices.R
import com.ab.hicareservices.data.SharedPreferenceUtil
import com.ab.hicareservices.databinding.FragmentAccountBinding
import com.ab.hicareservices.ui.view.activities.ComplaintsActivity
import com.ab.hicareservices.ui.view.activities.HelpActivity
import com.ab.hicareservices.ui.view.activities.LoginActivity
import com.ab.hicareservices.ui.view.activities.ReferralActivity

class AccountFragment : Fragment() {

    lateinit var binding: FragmentAccountBinding
    var mobileno=""
    var first_name=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        first_name = SharedPreferenceUtil.getData(requireContext(), "FirstName", "").toString()
        mobileno = SharedPreferenceUtil.getData(requireContext(), "mobileNo", "").toString()

        binding.txtusernames.text=first_name.toString()
        binding.txtUserdetailes.text=mobileno.toString()

        binding.edtAccount.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment.newInstance())
                .addToBackStack("AccountFragment").commit()
        }

        binding.constraintorderid.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, OrdersFragment.newInstance())
                .addToBackStack("AccountFragment").commit()
        }

        binding.constraintcomplaints.setOnClickListener {
            val intent=Intent(requireActivity(),ComplaintsActivity::class.java)
            startActivity(intent)
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.container, ComplaintFragment.newInstance())
//                .addToBackStack("AccountFragment").commit()
        }

        binding.constraintreferBtn.setOnClickListener {
            val intent = Intent(requireContext(), ReferralActivity::class.java)
            startActivity(intent)
        }

        binding.constrainRateus.setOnClickListener {
//            showRatusdialog()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ab.hicareservices")))

        }

        binding.help.setOnClickListener {
            val intent = Intent(requireContext(), HelpActivity::class.java)
            startActivity(intent)
        }

        binding.contraintbutton.setOnClickListener{
            val mobileNumber="9324747360"
            val message=""
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("http://api.whatsapp.com/send?phone=+91$mobileNumber&text=$message")
            startActivity(intent)
        }

        binding.terms.setOnClickListener {
            val intent = Intent(requireContext(), TermsAndConditionFragment::class.java)
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

    private fun signOut() {
        SharedPreferenceUtil.setData(requireContext(), "mobileNo", "-1")
        SharedPreferenceUtil.setData(requireContext(), "bToken", "")
        SharedPreferenceUtil.setData(requireContext(), "IsLogin", false)
        SharedPreferenceUtil.setData(requireContext(), "pincode","")
        SharedPreferenceUtil.setData(requireContext(), "customerid","")
        SharedPreferenceUtil.setData(requireContext(), "FirstName","")
        SharedPreferenceUtil.setData(requireContext(), "MobileNo","")
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    private fun showRatusdialog() {

        val dialogView = Dialog(requireActivity()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            setCanceledOnTouchOutside(false)
            setContentView(R.layout.layout_dialog)
        }

        val okBtn = dialogView.findViewById(R.id.okBtn) as AppCompatButton
        val cancelBtn = dialogView.findViewById(R.id.btnCancel) as AppCompatButton

        okBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Thank You!", Toast.LENGTH_SHORT).show()
            dialogView.dismiss()

        }
        cancelBtn.setOnClickListener {
            dialogView.dismiss()
        }
        dialogView.show()
    }

}