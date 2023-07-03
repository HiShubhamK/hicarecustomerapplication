package com.ab.hicareservices.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ab.hicareservices.ui.view.activities.OrderSummeryFragment
import com.ab.hicareservices.ui.view.fragments.OrdersFragment


class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    var mFragments: ArrayList<Fragment> = ArrayList()
    var mFragmentsTitle: ArrayList<String> = ArrayList()
    fun addFragment(f: Fragment?, s: String?) {
        if (f != null) {
            mFragments.add(f)
        }
        if (s != null) {
            mFragmentsTitle.add(s)
        }
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OrdersFragment()
            1 -> OrderSummeryFragment()
            else -> OrdersFragment()
        }
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentsTitle[position]
    }
}