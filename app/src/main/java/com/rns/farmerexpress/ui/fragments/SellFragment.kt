package com.rns.farmerexpress.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.SellAdapter

import com.rns.farmerexpress.databinding.FragmentSellBinding
import com.rns.farmerexpress.model.SellModel

class SellFragment : Fragment() {

    private var _binding: FragmentSellBinding? = null
    lateinit var recyclerView: RecyclerView


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.rvSell

        addCatData()


        return root
    }

    private fun addCatData() {
        val list = ArrayList<SellModel>()
        list.add(SellModel(SellAdapter.VIEW_TYPE_ONE,R.drawable.tt,"ट्रैक्टर"))
        list.add(SellModel(SellAdapter.VIEW_TYPE_ONE,R.drawable.tt,"पशु"))
        list.add(SellModel(SellAdapter.VIEW_TYPE_ONE,R.drawable.tt,"बीज"))
        list.add(SellModel(SellAdapter.VIEW_TYPE_ONE,R.drawable.tt,"खेती के उपकरण"))

        val adapter = SellAdapter(requireActivity(),list)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}