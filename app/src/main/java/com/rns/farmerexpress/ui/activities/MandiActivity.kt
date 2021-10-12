package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.MandiAdapter
import com.rns.farmerexpress.adapter.NewsAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.model.MandiListModal
import com.rns.farmerexpress.model.NewsModel
import com.rns.farmerexpress.model.Records
import kotlinx.android.synthetic.main.fragment_all_news.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response






class MandiActivity : AppCompatActivity() {
    @SuppressLint("StaticFieldLeak")
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    var list :ArrayList<Records> = ArrayList()
    var notLoading = true
    @SuppressLint("StaticFieldLeak")
    lateinit var adapter : MandiAdapter
    @SuppressLint("StaticFieldLeak")
    lateinit var pbMandi : ProgressBar
    @SuppressLint("StaticFieldLeak")
    lateinit var ivBack : ImageView
    var flagGetAll = true
    var snackbar: Snackbar? = null
    @SuppressLint("StaticFieldLeak")
    lateinit var clParent : View
    var total = 0
    var offset = 0
    var limit = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mandi)
        recyclerView = findViewById(R.id.rvMandi)
        pbMandi = findViewById(R.id.pbMandi)
        ivBack = findViewById(R.id.ivBack)
        clParent = findViewById(android.R.id.content)

        if (flagGetAll) {
            pbMandi.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            getMandiData()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("ShowToast")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
               if (total > offset + limit){
                    addDataOnScroll(offset+limit)
                }else {
//                        Toast.makeText(requireContext(), "End of page", Toast.LENGTH_SHORT).show()
                    snackbar =
                        Snackbar.make(
                            clParent,
                            "पेज समाप्त हुआ",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction("ठीक हैं ") { snackbar?.dismiss() }
                    snackbar!!.setActionTextColor(Color.WHITE)
                    val sbView = snackbar!!.view
                    snackbar!!.setBackgroundTint(Color.rgb(239, 127, 62))
                    val textView =
                        sbView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.WHITE)
                    snackbar!!.show()
                }

            }
        })


        adapter = MandiAdapter(this, list)
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        ivBack.setOnClickListener {
            finish()
        }
    }


    private fun addDataOnScroll(offset:Int) {
        if (notLoading && layoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
            list.add(Records(MandiAdapter.VIEW_TYPE_TWO, "", "", "", "","","","","","",""))
            adapter.notifyItemInserted(list.size - 1)
            notLoading = false
//            val i = count++
//        Toast.makeText(requireContext(), "count $i", Toast.LENGTH_SHORT).show()
            val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
            val call: retrofit2.Call<MandiListModal> = service.mandiData(offset.toString(), "Rajasthan")
            try {
                call.enqueue(object : Callback<MandiListModal> {
                    @SuppressLint("WrongConstant", "ShowToast", "ResourceType")
                    override fun onResponse(
                        call: Call<MandiListModal>,
                        response: Response<MandiListModal>
                    ) {
                        list.removeAt(list.size - 1)
                        adapter.notifyItemRemoved(list.size)
                        val responseBody = response.body()
                        if (responseBody != null) {
                            for (data in responseBody.records) {
                                list.add(
                                    Records(
                                        MandiAdapter.VIEW_TYPE_ONE,
                                        data.state,
                                        data.district,
                                        data.market,
                                        data.commodity,data.variety,data.arrival_date,data.min_price
                                       ,data.max_price,data.modal_price
                                    ,data.image
                                    )
                                )
                            }
//                            adapter = NewsAdapter(requireActivity(), lists)
//                            layoutManager =
//                                LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
//                            view?.recyclerView?.layoutManager = layoutManager
//                            view?.recyclerView?.adapter = adapter
//                                adapter.notifyDataSetChanged()
                            recyclerView.scrollToPosition(list.size - 12)
                            limit += 10
                            notLoading = true
                        }
                    }

                    override fun onFailure(call: Call<MandiListModal>, t: Throwable) {
                        Log.d("onFailRes", "onFailure: ${t.message}")
                    }
                })

            } catch (e: java.lang.Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun getMandiData(){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<MandiListModal> = service.mandiData("0","Rajasthan")
        try {
            call.enqueue(object : retrofit2.Callback<MandiListModal>{
                override fun onResponse(
                    call: Call<MandiListModal>,
                    response: Response<MandiListModal>
                ) {
                    val responseBody = response.body()!!
                    total = responseBody.total
                    for (data in responseBody.records){
                        list.add(Records(MandiAdapter.VIEW_TYPE_ONE,data.state,data.district,data.market,data.commodity,data.variety,data.arrival_date,data.min_price,data.max_price,data.modal_price,data.image))
                    }
                    adapter = MandiAdapter(this@MandiActivity, list)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = layoutManager
                    pbMandi.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    flagGetAll = false

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