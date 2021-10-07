package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.SellAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.ActivityMainBinding
import com.rns.farmerexpress.databinding.ActivitySellCatItemBinding
import com.rns.farmerexpress.model.Categories
import com.rns.farmerexpress.model.HomeModel
import com.rns.farmerexpress.model.SellModel
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class SellCatItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellCatItemBinding
    lateinit var rv : RecyclerView
    val list = ArrayList<Categories>()
    lateinit var adapter: SellAdapter
    lateinit var layoutManager: GridLayoutManager
    lateinit var progressBar: ProgressBar
    lateinit var tvToolbarH: TextView
    var id = ""
    var heading = ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellCatItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rv = binding.rvSellCatItem
        val back = binding.ivBack
        progressBar = binding.pbSubCat
        tvToolbarH = binding.tvToolbarH
        id = intent.getStringExtra("id").toString()
        heading = intent.getStringExtra("catName").toString()
        tvToolbarH.text = "कौनसा $heading बेचना चाहते हैं ?"

        progressBar.visibility = View.VISIBLE
        rv.visibility = View.GONE
        back.setOnClickListener{
            finish()
        }

        showItemCatData()

         adapter = SellAdapter(this,list)
        rv.layoutManager = GridLayoutManager(this,2)
        rv.adapter = adapter
    }

    private fun showItemCatData()  {
            val session = PreferenceConnector.readString(this,
                PreferenceConnector.profilestatus,"")
            val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
            val call: Call<SellModel> = service.getSubCatData(session,"select",id)
            try {
                call.enqueue(object : retrofit2.Callback<SellModel>{
                    override fun onResponse(call: Call<SellModel>, response: Response<SellModel>) {
                        val responseBody = response.body()!!

                        for (data in responseBody.category){
                            list.add(Categories(SellAdapter.VIEW_TYPE_TWO,data.id,data.catImage,data.catName,data.field,data.category))
                            heading = data.catName
                        }
//                    Log.d("onCatRes", "onResponse: ${responseBody.category}")
                        adapter = SellAdapter(this@SellCatItemActivity,list)
                        rv.layoutManager = GridLayoutManager(this@SellCatItemActivity,2)
                        rv.adapter = adapter
                        progressBar.visibility = View.GONE
                        rv.visibility = View.VISIBLE
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