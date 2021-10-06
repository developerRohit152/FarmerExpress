package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.MandiAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.model.Comment
import com.rns.farmerexpress.model.MandiListModal
import com.rns.farmerexpress.model.Records
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response

@SuppressLint("StaticFieldLeak")
lateinit var recyclerView: RecyclerView
lateinit var layoutManager: LinearLayoutManager
lateinit var list :ArrayList<Records>

class MandiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandi)
        recyclerView = findViewById(R.id.rvMandi)
        list = ArrayList()
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        getMandiData()
    }

    private fun getMandiData(){
        val service: ApiInterface = APIClient.getMandi()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<MandiListModal> = service.mandiData("Rajasthan")
        try {
            call.enqueue(object : retrofit2.Callback<MandiListModal>{
                override fun onResponse(
                    call: Call<MandiListModal>,
                    response: Response<MandiListModal>
                ) {
                    val responseBody = response.body()!!
                    for (data in responseBody.records){
                        list.add(Records(data.state,data.district,data.market,data.commodity,data.variety,data.arrival_date,data.min_price,data.max_price,data.modal_price))
                    }
                    val adapter = MandiAdapter(this@MandiActivity, list)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = layoutManager
                    Log.d("OnResMandi", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<MandiListModal>, t: Throwable) {
                    Log.d("OnResMandiFail", "onResponse: ${t.message}")
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}