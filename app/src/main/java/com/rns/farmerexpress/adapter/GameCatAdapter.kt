package com.rns.farmerexpress.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.CattleCatModel
import com.rns.farmerexpress.model.GameCat
import com.rns.farmerexpress.model.HomeModel
import com.rns.farmerexpress.model.SellModel
import com.rns.farmerexpress.ui.activities.PlayGameActivity
import kotlinx.android.synthetic.main.recycler_newa_item.view.*

class GameCatAdapter(private val activity: Activity, var list: ArrayList<GameCat>)  : RecyclerView.Adapter<GameCatAdapter.MyViewHolder>(){



    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImage : ImageView = itemView.findViewById(R.id.ivImage)
        val userName : TextView = itemView.findViewById(R.id.tvHeading)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_newa_item,parent,false))

    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        holder.userImage.setImageResource(list[position].image)
        holder.userName.text = list[position].name
        holder.itemView.llNews.setOnClickListener{
            val  i = Intent(activity,PlayGameActivity::class.java)
            i.putExtra("catname",list[position].name)
            activity.startActivity(i)
        }

    }

    override fun getItemCount(): Int = list.size

}


