package com.rns.farmerexpress.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rns.farmerexpress.ui.fragments.*

class ViewPagerInfoAdapter(fragment : Fragment ) : FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment  {

        return when (position) {
            0 -> CompanyFragment()
            1 -> UserFragment()
            2 -> SeedFragment()
            3 -> AEquipmentFragment()
            4 -> CropFragment()
            else -> CompanyFragment()
        }
    }




}

