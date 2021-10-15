package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider

import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.model.*
import com.rns.farmerexpress.ui.activities.SubBuyActivity
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

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
    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        Picasso.get().load("http://${list[position].catImage}").placeholder(R.drawable.imageplaceholder).error(R.drawable.error).into(holder.userImage)
        holder.userName.text = list[position].catName
        holder.cvParent.setOnClickListener {
//            val i = Intent(activity, SubBuyActivity::class.java)
//            i.putExtra("catid",(list[position].id).toString())
//            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int = list.size


}


