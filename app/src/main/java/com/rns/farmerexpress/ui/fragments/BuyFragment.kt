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
    private lateinit var ivCompany: ImageView
    private lateinit var ivUser: ImageView
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        ivCompany = binding.ivCompany
        ivUser = binding.ivUser

        ivUser.setOnClickListener {
            startActivity(Intent(activity,BuyActivity::class.java))
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}