package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.FragmentCattleBinding
import com.rns.farmerexpress.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rns.farmerexpress.adapter.*
import com.squareup.picasso.Picasso
import javax.annotation.Nullable


class UserFragment : Fragment(){



    private var _binding: FragmentCattleBinding? = null
    lateinit var rvCattlePost: RecyclerView
    lateinit var tabs: TabLayout
    var list = ArrayList<Categories>()
    lateinit var viewPager: ViewPager2
    lateinit var viewPagerSubAdapter: ViewPagerSubAdapter

    val lists = ArrayList<String>()
    lateinit var adapter: CattleCatAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var progressBar: ProgressBar

    private val binding get() = _binding!!


    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCattleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewPager = binding.pager
        tabs = binding.tabLayout
//        lists.add("rr")
//        lists.add("po")
//        lists.add("io")
//        addCatData()
        viewPagerSubAdapter = ViewPagerSubAdapter(requireActivity(),list.size)
        viewPager.adapter = viewPagerSubAdapter

//        init()

        addCatData()

        return root
    }


    private fun init(){
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = list[position].catName
        }.attach()
    }
    private fun addCatData() {
        val session = PreferenceConnector.readString(activity,
            PreferenceConnector.profilestatus,"")
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<SellModel> = service.getCatData(session,"select")
        try {
            call.enqueue(object : retrofit2.Callback<SellModel>{
                override fun onResponse(call: Call<SellModel>, response: Response<SellModel>) {
                    val responseBody = response.body()!!
                    for (data in responseBody.category){
                        list.add(Categories(CattleCatAdapter.VIEW_TYPE_ONE,data.id,data.catImage,data.catName,"",-1))
                    }
                    viewPagerSubAdapter = ViewPagerSubAdapter(requireActivity(),list.size)
                    viewPager.adapter = viewPagerSubAdapter
                    init()
//                    Log.d("onCatRes", "onResponse: ${responseBody.category}")
//                    adapter = CattleCatAdapter(requireActivity(),list)
//                    progressBar.visibility = View.GONE
//                    recyclerView.visibility = View.VISIBLE
                }
                override fun onFailure(call: Call<SellModel>, t: Throwable) {
                    Log.d("onCatResFail", "onResponse: ${t.message}")
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

}