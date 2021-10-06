package com.rns.farmerexpress.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.NewsModel
import com.rns.farmerexpress.ui.activities.NewsDescActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_desc.view.*
import kotlinx.android.synthetic.main.loading.view.*
import kotlinx.android.synthetic.main.recycler_newa_item.view.*

class NewsAdapter(private val activity: Activity, val list: ArrayList<NewsModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    class MyNewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {

        }
    }
    class showLoading(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.recycler_newa_item, parent, false)
//        return MyNewsViewHolder(view)

        if (viewType == NewsAdapter.VIEW_TYPE_ONE) {
            return MyNewsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_newa_item, parent, false)
            )
        }
        return showLoading(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading, parent, false)
        )


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].viewType == VIEW_TYPE_ONE) {
            Picasso.get().load("http://khabarexpo.in/admin/image.php?id=${list[position].post_id}")
                .into(holder.itemView.ivImage)
            holder.itemView.tvCat.text = list[position].category
            holder.itemView.tvHeading.text = list[position].title
            holder.itemView.tvAuthor.text = list[position].name
            holder.itemView.llNews.setOnClickListener {
                val postID = list[position].post_id
                val i = Intent(activity, NewsDescActivity::class.java)
                i.putExtra("id", postID)
                activity.startActivity(i)
            }
            } else {

            }
        }

        override fun getItemCount(): Int = list.size
        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

    }