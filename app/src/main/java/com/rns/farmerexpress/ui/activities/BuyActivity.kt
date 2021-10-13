package com.rns.farmerexpress.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.ViewPagerAdapter

class BuyActivity : AppCompatActivity() {
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
        viewPager = findViewById(R.id.pager)
        tabLayout  = findViewById(R.id.tabLayout)
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

    }