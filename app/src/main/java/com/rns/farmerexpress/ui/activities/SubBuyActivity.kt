package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.BuyItemAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.SellModel
import com.rns.farmerexpress.model.SubBuyData
import com.rns.farmerexpress.model.SubBuyModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SubBuyActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var adapter : BuyItemAdapter
    var list = ArrayList<SubBuyData>()
    lateinit var layoutManager: LinearLayoutManager
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_buy)
        recyclerView = findViewById(R.id.rvSubBuy)
        progressBar = findViewById(R.id.pbSubBuy)
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        val session = PreferenceConnector.readString(this,
            PreferenceConnector.profilestatus,"")
        val catid : String = intent.getStringExtra("catid") as String
        val latitude = PreferenceConnector.readString(this,PreferenceConnector.LATITUDE,"")
        val longitude = PreferenceConnector.readString(this,PreferenceConnector.LONGITUDE,"")
        getSubBuyData(session,latitude,longitude,catid)

        adapter = BuyItemAdapter(this,list)
        layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    private fun getSubBuyData(session : String,latitude:String,longitude : String,catid:String){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<SubBuyModel> = service.getBuyData(session,"user","1","10",latitude,longitude,"1000",catid,"")
        try {
            call.enqueue(object : Callback<SubBuyModel>{
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<SubBuyModel>, response: Response<SubBuyModel>) {
                    val responseBody = response.body()!!
                    for (data in responseBody.store){
                        list.add(SubBuyData(data.store_id,data.user_name,data.user_image,data.place,data.description,data.store_images,data.contact,data.distance,data.likes,data.isLiked
                                                ,data.date))
                    }
                    adapter = BuyItemAdapter(this@SubBuyActivity,list)
                    layoutManager = LinearLayoutManager(this@SubBuyActivity,LinearLayout.VERTICAL,false)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Log.d("onResSubBuyData", "onResponse: ${responseBody.store}")
                }

                override fun onFailure(call: Call<SubBuyModel>, t: Throwable) {
                    Log.d("onResSubBuyDataFail", "onResponse: ${t.message}")
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@SubBuyActivity,"डेटा लोड नहीं हुआ , कुछ देर बाद प्रयास करे",Toast.LENGTH_LONG).show()
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}