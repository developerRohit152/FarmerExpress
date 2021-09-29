package com.rns.farmerexpress.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.ViewPagerAdapter
import com.rns.farmerexpress.adapter.ViewPagerNewsAdapter
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val viewPager : ViewPager = findViewById(R.id.pager)
        tabLayout.addTab(tabLayout.newTab().setText("न्यूज"))
        tabLayout.addTab(tabLayout.newTab().setText("क्राइम"))
        tabLayout.addTab(tabLayout.newTab().setText("राजनीति"))
        tabLayout.addTab(tabLayout.newTab().setText("मनोरंजन"))
        tabLayout.addTab(tabLayout.newTab().setText("खेल "))
        tabLayout.addTab(tabLayout.newTab().setText("राशिफल"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = ViewPagerNewsAdapter(this, supportFragmentManager,
            tabLayout.tabCount)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

}