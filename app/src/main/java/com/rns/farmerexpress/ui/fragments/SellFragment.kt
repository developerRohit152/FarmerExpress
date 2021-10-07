package com.rns.farmerexpress.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.inappmessaging.internal.ApiClient
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.SellAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector

import com.rns.farmerexpress.databinding.FragmentSellBinding
import com.rns.farmerexpress.model.Categories
import com.rns.farmerexpress.model.NewsModel
import com.rns.farmerexpress.model.SellModel
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class SellFragment : Fragment() {
    val list = ArrayList<Categories>()
    private var _binding: FragmentSellBinding? = null
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: SellAdapter
    lateinit var layoutManager: GridLayoutManager
    lateinit var progressBar: ProgressBar


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
        progressBar = binding.pbSell
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        addCatData()
        adapter = SellAdapter(requireActivity(),list)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        recyclerView.adapter = adapter

        return root
    }

    private fun addCatData() {
        val session = PreferenceConnector.readString(requireContext(),
            PreferenceConnector.profilestatus,"")
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<SellModel> = service.getCatData(session,"select")
        try {
            call.enqueue(object : retrofit2.Callback<SellModel>{
                override fun onResponse(call: Call<SellModel>, response: Response<SellModel>) {
                    val responseBody = response.body()!!
                    for (data in responseBody.category){
                        list.add(Categories(SellAdapter.VIEW_TYPE_ONE,data.id,data.catImage,data.catName,"",-1))
                    }

//                    Log.d("onCatRes", "onResponse: ${responseBody.category}")
                    adapter = SellAdapter(requireActivity(),list)
                    recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                    recyclerView.adapter = adapter
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
                override fun onFailure(call: Call<SellModel>, t: Throwable) {
                    Log.d("onCatResFail", "onResponse: ${t.message}")
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}