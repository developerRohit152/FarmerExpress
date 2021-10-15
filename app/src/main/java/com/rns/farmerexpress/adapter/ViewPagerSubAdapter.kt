package com.rns.farmerexpress.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rns.farmerexpress.model.Categories
import com.rns.farmerexpress.ui.fragments.*
import com.rns.farmerexpress.ui.fragments.CropFragment.Companion.ARG_OBJECT

class ViewPagerSubAdapter(activity: FragmentActivity, numOfTabs:Int ) : FragmentStateAdapter(activity){
    private var mNumOfTabs = numOfTabs

        override fun getItemCount(): Int = mNumOfTabs

        override fun createFragment(position: Int): Fragment {
            val list = ArrayList<Categories>()
            // Return a NEW fragment instance in createFragment(int)
            val fragment = CropFragment()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-
                try {
                putInt(ARG_OBJECT, list[position].id)

                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            return fragment
        }
    }




