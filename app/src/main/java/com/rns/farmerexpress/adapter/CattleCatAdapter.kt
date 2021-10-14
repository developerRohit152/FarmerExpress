package com.rns.farmerexpress.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.Categories
import com.rns.farmerexpress.model.CattleCatModel
import com.rns.farmerexpress.model.HomeModel
import com.rns.farmerexpress.model.SellModel
import com.rns.farmerexpress.ui.activities.SubBuyActivity
import com.squareup.picasso.Picasso

class CattleCatAdapter(private val activity: Activity, var list: ArrayList<Categories>)  : RecyclerView.Adapter<CattleCatAdapter.MyViewHolder>(){

    companion object {
        const val VIEW_TYPE_ONE = 1

    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImage : ImageView = itemView.findViewById(R.id.civImage)
        val userName : TextView = itemView.findViewById(R.id.tvName)
        val cvParent : CardView = itemView.findViewById(R.id.cvParent)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_cattle_cat,parent,false))
//




    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        Picasso.get().load("http://${list[position].catImage}").placeholder(R.drawable.imageplaceholder).error(R.drawable.error).into(holder.userImage)
        holder.userName.text = list[position].catName
        holder.cvParent.setOnClickListener {
            val i = Intent(activity, SubBuyActivity::class.java)
            i.putExtra("catid",(list[position].id).toString())
            activity.startActivity(i)
        }




    }

    override fun getItemCount(): Int = list.size

}


