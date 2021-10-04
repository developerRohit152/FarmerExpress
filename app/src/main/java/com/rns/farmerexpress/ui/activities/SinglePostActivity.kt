package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.HomeAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.GetPostData
import com.rns.farmerexpress.model.GetSinglePost
import com.rns.farmerexpress.model.PostDatas
import kotlinx.android.synthetic.main.fragment_homes.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class SinglePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_post)
        val session = PreferenceConnector.readString(this,PreferenceConnector.profilestatus,"")
        getHomeData(session)

    }



    private fun getHomeData(session:String){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<GetSinglePost> = service.getSinglePost(session,"282")
        try {
            call.enqueue(object : retrofit2.Callback<GetSinglePost>{
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<GetSinglePost>, response: Response<GetSinglePost>) {
                    response.body()!!
                    val responseBody = response.body()
                        Log.d("OnSinglePostRes", "onResponse: $responseBody")
                    for (data in responseBody!!.post){
                        if (data.type == "image") {

                        }else if (data.type == "bg"){
                        }else{

                        }
                    }

                }
                override fun onFailure(call: Call<GetSinglePost>, t: Throwable) {
                    Log.d("OnSinglePostResFail", "onResponse: ${t.message}")
                }

            })
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}