package com.rns.farmerexpress.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rns.farmerexpress.ui.fragments.*

class ViewPagerAdapter(activity: FragmentActivity  ) : FragmentStateAdapter(activity){

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment  {

        return when (position) {
            0 -> CompanyFragment()
            1 -> CompanyFragment()
            2 -> UserFragment()
            else -> CompanyFragment()
        }
     }




}


