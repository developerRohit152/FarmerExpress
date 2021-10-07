package com.rns.farmerexpress.ui.fragments.news

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.NewsAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.model.NewsModel
import kotlinx.android.synthetic.main.fragment_all_news.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

//
class PoliticsFragment : Fragment() {
    var lists: ArrayList<NewsModel> = ArrayList()
    lateinit var adapter: NewsAdapter
    lateinit var layoutManager: LinearLayoutManager
    var notLoading = true
    var count = 1
    var snackbar: Snackbar? = null
    var flagGetAll = true
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_politics, container, false)
        if (flagGetAll) {
            view?.indeterminateBar?.visibility = View.VISIBLE
            getPoliticsNews()
        }
        count = 2
        view?.recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (notLoading && layoutManager.findLastCompletelyVisibleItemPosition() == lists.size - 1) {
                    addDataOnScroll()
                }
            }
        })
        adapter = NewsAdapter(requireActivity(), lists)
        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        view?.recyclerView?.layoutManager = layoutManager
        view?.recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()
        return view
    }



    private fun addDataOnScroll() {
        lists.add(NewsModel(NewsAdapter.VIEW_TYPE_TWO, "", "", "", ""))
        adapter.notifyItemInserted(lists.size - 1)
        notLoading = false
        val i = count++
//        Toast.makeText(requireContext(), "count $i", Toast.LENGTH_SHORT).show()
        val service: ApiInterface = APIClient.getNewsClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<List<NewsModel>> = service.getCatNews(7,10, i)
        try {
            call.enqueue(object : Callback<List<NewsModel>> {
                @SuppressLint("WrongConstant", "ShowToast", "ResourceType")
                override fun onResponse(
                    call: Call<List<NewsModel>>,
                    response: Response<List<NewsModel>>
                ) {
                    lists.removeAt(lists.size - 1)
                    adapter.notifyItemRemoved(lists.size)
                    val responseBody = response.body()
                    if (responseBody!!.isNotEmpty()) {
                        for (data in responseBody) {
                            lists.add(
                                NewsModel(
                                    NewsAdapter.VIEW_TYPE_ONE,
                                    data.post_id,
                                    data.category,
                                    data.title,
                                    data.name
                                )
                            )
                        }

                        view?.recyclerView?.scrollToPosition(lists.size - 12)
                        notLoading = true
                    } else {
                        snackbar =
                            Snackbar.make(requireView(), "पेज समाप्त हुआ", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ठीक हैं ") { snackbar?.dismiss() }
                        snackbar!!.setActionTextColor(Color.WHITE);
                        val sbView = snackbar!!.view
                        snackbar!!.setBackgroundTint(Color.rgb(239,127,62))
                        val textView =
                            sbView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                        textView.setTextColor(Color.WHITE)
                        snackbar!!.show()


                    }
                }

                override fun onFailure(call: Call<List<NewsModel>>, t: Throwable) {
                    Log.d("onFailRes", "onFailure: ${t.message}")
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    private fun getPoliticsNews() {
        val service: ApiInterface = APIClient.getNewsClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<List<NewsModel>> = service.getCatNews(7,10, 1)
        try {
            call.enqueue(object : Callback<List<NewsModel>> {
                @SuppressLint("WrongConstant")
                override fun onResponse(
                    call: Call<List<NewsModel>>,
                    response: Response<List<NewsModel>>
                ) {

                    val responseBody = response.body()!!
                    Log.d("resNews", "onResponse: $responseBody")
                    for (newsData in responseBody) {
                        lists.add(
                            NewsModel(
                                NewsAdapter.VIEW_TYPE_ONE,
                                newsData.post_id,
                                newsData.category,
                                newsData.title,
                                newsData.name
                            )
                        )
                    }
                    adapter = NewsAdapter(requireActivity(), lists)
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
                    view?.recyclerView?.layoutManager = layoutManager
                    view?.recyclerView?.adapter = adapter
                    adapter.notifyDataSetChanged()
                    view?.indeterminateBar?.visibility = View.INVISIBLE
                    flagGetAll = false
                }

                override fun onFailure(call: Call<List<NewsModel>>, t: Throwable) {
                    Log.d("resFailureNews", "onFailure: ${t.message}")
                    view?.indeterminateBar?.visibility = View.INVISIBLE

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    companion object {

    }
}