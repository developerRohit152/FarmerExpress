package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.ViewPagerAdapter
import com.rns.farmerexpress.databinding.FragmentDashboardBinding


class BuyFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewPager = binding.pager
        tabLayout  = binding.tabLayout



        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            when(position){
                0 -> {tab.text = "ट्रैक्टर"
                     tab.setIcon(R.drawable.ttt)
                }
                1 -> {
                    tab.text = "पशु"
//                    tab.setIcon(R.drawable.tt)

                }
                2 -> {
                    tab.text = "बीज"
//                    tab.setIcon(R.drawable.tt)

                }   3 -> {
                    tab.text = "कृषि यंत्र "
//                    tab.setIcon(R.drawable.tt)

                } 4  -> {
                    tab.text = "फसल"
//                    tab.setIcon(R.drawable.tt)
                }  5  -> {
                    tab.text = "कृषि बाजार  "
//                    tab.setIcon(R.drawable.tt)
                } 6  -> {
                    tab.text = "अन्य "
//                    tab.setIcon(R.drawable.tt)
                }
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}