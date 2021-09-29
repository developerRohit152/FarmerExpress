package com.rns.farmerexpress.adapter
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rns.farmerexpress.ui.fragments.*

@Suppress("DEPRECATION")
internal class ViewPagerNewsAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int
) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AllNewsFragment()
            1 -> CrimeNewsFragment()
            2 -> PoliticsFragment()
            3 -> EntertainmentFragment()
            4-> SportsFragment()
            5-> HoroscopeFragment()
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}


