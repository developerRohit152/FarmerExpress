package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.ViewPagerAdapter
import com.rns.farmerexpress.databinding.FragmentDashboardBinding
import com.rns.farmerexpress.ui.activities.BuyActivity


class
BuyFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var ivCompany: ImageView
    private lateinit var ivUser: ImageView
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewPager = binding.pager
        tabLayout = binding.tabLayout
//        ivCompany = binding.ivCompany
//        ivUser = binding.ivUser

//        ivUser.setOnClickListener {
//            startActivity(Intent(activity,BuyActivity::class.java))
//        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout  = binding.tabLayout
        pagerAdapter = ViewPagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            when(position){
                0 -> {tab.text = "कंपनी से खरीदे"
                     tab.setIcon(R.drawable.buyicon)

                }
                1 -> {
                    tab.text = "ग्राहक से खरीदे"
                    tab.setIcon(R.drawable.profile)
                }
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}