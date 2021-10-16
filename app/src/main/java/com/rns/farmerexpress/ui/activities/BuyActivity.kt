package com.rns.farmerexpress.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.SellAdapter
import com.rns.farmerexpress.adapter.ViewPagerAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.Categories
import com.rns.farmerexpress.model.SellModel
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class BuyActivity : AppCompatActivity() {
//    not use
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var rvBuy: RecyclerView
    val list = ArrayList<Categories>()
    lateinit var adapter: SellAdapter
    lateinit var layoutManager: GridLayoutManager
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
//        viewPager = findViewById(R.id.pager)
        rvBuy = findViewById(R.id.rvBuy)
        progressBar = findViewById(R.id.pbBuy)
        progressBar.visibility = View.VISIBLE
        rvBuy.visibility = View.GONE

//        addCatData()

        adapter = SellAdapter(this,list)
        rvBuy.layoutManager = GridLayoutManager(this,2)
        rvBuy.adapter = adapter

    }
//
//    private fun addCatData() {
//        val session = PreferenceConnector.readString(this,
//            PreferenceConnector.profilestatus,"")
//        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
//        val call: Call<SellModel> = service.getCatData(session,"select")
//        try {
//            call.enqueue(object : retrofit2.Callback<SellModel>{
//                override fun onResponse(call: Call<SellModel>, response: Response<SellModel>) {
//                    val responseBody = response.body()!!
//                    for (data in responseBody.category){
//                        list.add(Categories(SellAdapter.VIEW_TYPE_THREE,data.id,data.catImage,data.catName,"",-1))
//                    }
//
////                    Log.d("onCatRes", "onResponse: ${responseBody.category}")
//                    adapter = SellAdapter(this@BuyActivity,list)
//                    rvBuy.layoutManager = GridLayoutManager(this@BuyActivity,2)
//                    rvBuy.adapter = adapter
//                    progressBar.visibility = View.GONE
//                    rvBuy.visibility = View.VISIBLE
//                }
//                override fun onFailure(call: Call<SellModel>, t: Throwable) {
//                    Log.d("onCatResFail", "onResponse: ${t.message}")
//                }
//
//            })
//        }catch (e : Exception){
//            e.printStackTrace()
//        }
//    }
    }