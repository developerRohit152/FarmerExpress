package com.rns.farmerexpress.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.*

class AgriCatListAdapter(private val activity: Activity, var list: ArrayList<AgriCatListModel>)  : RecyclerView.Adapter<AgriCatListAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val catName : TextView = itemView.findViewById(R.id.tvName)
        val catImage : ImageView = itemView.findViewById(R.id.ivNewsImage)
        val catPrice : TextView = itemView.findViewById(R.id.tvPrice)
        val catSeller : TextView = itemView.findViewById(R.id.tvSeller)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_agri_cat_list,parent,false))

    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        holder.catName.text = list.get(position).catListName
        holder.catPrice.text = "₹ "+list.get(position).catListPrice
        holder.catSeller.text = "सेलर: "+list.get(position).catListSeller
        holder.catImage.setImageResource(list[position].catListImage)
   }

    override fun getItemCount(): Int = list.size
}


