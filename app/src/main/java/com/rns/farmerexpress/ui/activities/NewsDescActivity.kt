package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.text.HtmlCompat
import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.model.NewsDataModel
import com.rns.farmerexpress.model.NewsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_desc.*
import kotlinx.android.synthetic.main.recycler_newa_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.math.log

class NewsDescActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_desc)
        indeterminateBar.visibility = View.VISIBLE
        val id: String = intent.getStringExtra("id").toString()
        getNewsData(id)
    }

    private fun getNewsData(id: String) {

        val  service = APIClient.getNewsClient()?.create(ApiInterface::class.java)
        val call : retrofit2.Call<List<NewsDataModel>> = service!!.getNewsData(id)
        try {
            call.enqueue(object : Callback<List<NewsDataModel>> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<List<NewsDataModel>>,
                    response: Response<List<NewsDataModel>>
                ) {
                    val responseBody = response.body()!!
                    Log.d("onRes", "onResponse: $responseBody")
                    for (newsData in responseBody)
                    {
                        tvHead.text = newsData.title
                        tvContent.text = "${newsData.name}\n${newsData.date}"
//                        tvText.text = newsData.post
                        tvText.text = "${newsData.city}: ${android.text.Html.fromHtml(newsData.post, HtmlCompat.FROM_HTML_MODE_LEGACY)}"
                        Picasso.get()
                            .load("http://khabarexpo.in/admin/image.php?id=${newsData.post_id}")
                            .into(ivNewsImage)
                        indeterminateBar.visibility = View.INVISIBLE

                    }

                }


                override fun onFailure(call: Call<List<NewsDataModel>>, t: Throwable) {
                    Log.d("onFail", "onFailure: ${t.message}")
                    indeterminateBar.visibility = View.INVISIBLE

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}