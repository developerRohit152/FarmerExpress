package com.rns.farmerexpress.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rns.farmerexpress.ui.fragments.*

class ViewPagerAdapter(fragment : Fragment ) : FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment  {

        return when (position) {
            0 -> TractorFragment()
            1 -> CattleFragment()
            2 -> SeedFragment()
            3 -> AEquipmentFragment()
            4 -> CropFragment()
            5 -> AgricultureMarketFragment()
            6 -> OtherCatFragment()
            else -> TractorFragment()
        }
     }




}


