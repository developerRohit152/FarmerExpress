package com.rns.farmerexpress.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.CattleCatModel
import com.rns.farmerexpress.model.HomeModel
import com.rns.farmerexpress.model.SellModel

class CattleCatAdapter(private val activity: Activity, var list: ArrayList<CattleCatModel>)  : RecyclerView.Adapter<CattleCatAdapter.MyViewHolder>(){



    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImage : ImageView = itemView.findViewById(R.id.civImage)
        val userName : TextView = itemView.findViewById(R.id.tvName)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_cattle_cat,parent,false))
//




    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        holder.userImage.setImageResource(list[position].catImage)
        holder.userName.text = list.get(position).catName




    }

    override fun getItemCount(): Int = list.size

}


