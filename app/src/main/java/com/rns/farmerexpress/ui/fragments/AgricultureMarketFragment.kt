package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.AgriCatAdapter
import com.rns.farmerexpress.adapter.AgriCatListAdapter
import com.rns.farmerexpress.databinding.FragmentAgricultureMarketBinding
import com.rns.farmerexpress.databinding.FragmentOtherCatBinding
import com.rns.farmerexpress.model.AgriCatListModel
import com.rns.farmerexpress.model.AgriCatModel


class AgricultureMarketFragment : Fragment() {
    private var _binding: FragmentAgricultureMarketBinding? = null
    private val binding get() = _binding!!
    lateinit var rvAgriCat : RecyclerView
    lateinit var rvAgriCatList : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAgricultureMarketBinding.inflate(inflater, container, false)
        val root: View = binding.root
        rvAgriCat = binding.rvAgriCat
        rvAgriCatList = binding.rvAgriCatItem
        addAgriCatData()
        addAgriCatListData()

        return root
    }

    @SuppressLint("WrongConstant")
    private fun addAgriCatListData() {
        val list  = ArrayList<AgriCatListModel>()
        list.add(AgriCatListModel("बीज",R.drawable.tractorimg,"25000","दीनदयाल बाजार दुकान नंबर 120 "))
        list.add(AgriCatListModel("उर्वरक",R.drawable.ttt,"1000","महावीर नगर बाजारी मंडी "))
        list.add(AgriCatListModel("कीटनाशक",R.drawable.tt,"12000","रघुराम राजन श्याम नगर खसरा नंबर 540 "))
        list.add(AgriCatListModel("खरपतवार नाशक",R.drawable.timg,"36000","तेजा बाजार 189 दुकान "))

        val adapter = AgriCatListAdapter(requireActivity(),list)
        rvAgriCatList.layoutManager = LinearLayoutManager(requireContext(),LinearLayout.VERTICAL,false)
        rvAgriCatList.adapter = adapter
    }

    @SuppressLint("WrongConstant")
    private fun addAgriCatData() {
        val list = ArrayList<AgriCatModel>()
        list.add(AgriCatModel("सभी प्रोडक्ट"))
        list.add(AgriCatModel("बीज "))
        list.add(AgriCatModel("उर्वरक"))
        list.add(AgriCatModel("कीटनाशक"))
        list.add(AgriCatModel("खरपतवार नाशक"))

        val adapter  = AgriCatAdapter(requireActivity(),list)
        rvAgriCat.layoutManager = LinearLayoutManager(requireContext(),LinearLayout.HORIZONTAL,false)
        rvAgriCat.adapter = adapter
    }

    companion object {

    }
}