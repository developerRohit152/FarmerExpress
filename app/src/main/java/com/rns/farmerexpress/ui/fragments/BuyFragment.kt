package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
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
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewPager = binding.pager
        tabLayout = binding.tabLayout
//        ivCompany = binding.ivCompany
//        ivUser = binding.ivUser

//        ivUser.setOnClickListener {
//            startActivity(Intent(activity,BuyActivity::class.java))
//        }
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDark)
                tab?.icon?.setColorFilter(tabColor,PorterDuff.Mode.SRC_IN)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabColor = ContextCompat.getColor(requireContext(),R.color.black)
                tab?.icon?.setColorFilter(tabColor,PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                TODO("Not yet implemented")
            }

        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout  = binding.tabLayout
        pagerAdapter = ViewPagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter
        viewPager.isUserInputEnabled = false
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            when(position){
                0 -> {tab.text = "सभी"
                     tab.setIcon(R.drawable.all)

                } 1 -> {tab.text = "कंपनी से खरीदे"
                     tab.setIcon(R.drawable.buyicon)

                }
                2 -> {
                    tab.text = "ग्राहक से खरीदे"
                    tab.setIcon(R.drawable.ic_user)
                }
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}