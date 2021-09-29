package com.rns.farmerexpress.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.ViewPagerInfoAdapter


class QuestionFragment : Fragment() {
    private lateinit var pagerAdapter: ViewPagerInfoAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root : View =  inflater.inflate(R.layout.fragment_question, container, false)
        viewPager = root.findViewById(R.id.pager)
        tabLayout  = root.findViewById(R.id.tabLayout)


        return root;
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = ViewPagerInfoAdapter(this)
        viewPager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            when(position){
                0 -> {
                    tab.text = "पशु"
//                    tab.setIcon(R.drawable.tt)
                }
                1 -> {
                    tab.text = "मंडी "
//                    tab.setIcon(R.drawable.tt)
                }   2   -> {
                tab.text = "फसल"
//                    tab.setIcon(R.drawable.tt)
            }  3  -> {
                tab.text = "ज्ञान "
//                    tab.setIcon(R.drawable.tt)
            } 4  -> {
                tab.text = "अन्य "
//                    tab.setIcon(R.drawable.tt)
            }
            }
        }.attach()

    }

    companion object {

    }
}