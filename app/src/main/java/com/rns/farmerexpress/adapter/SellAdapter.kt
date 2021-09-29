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
import com.rns.farmerexpress.model.HomeModel
import com.rns.farmerexpress.model.SellModel
import com.rns.farmerexpress.ui.activities.SellCatItemActivity

class SellAdapter(private val activity: Activity, var list: ArrayList<SellModel>)  : RecyclerView.Adapter<SellAdapter.MyViewHolder>(){

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImage : ImageView = itemView.findViewById(R.id.ivCat)
        val userName : TextView = itemView.findViewById(R.id.tvCat)
        val cvParent : CardView = itemView.findViewById(R.id.cvParent)





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_sell_item,parent,false))

    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
//        holder.userImage.setImageResource(list[position].catImage)
//        holder.userName.text = list.get(position).catName
//        holder.cvParent.setOnClickListener {
////            var i  = Intent(this,SellCatItemActivity::class.java)
//            var i = Intent(activity,SellCatItemActivity::class.java)
//            activity.startActivity(i)
//        }

        if (list[position].viewType === VIEW_TYPE_ONE) {
            holder.userImage.setImageResource(list[position].catImage)
            holder.userName.text = list.get(position).catName
            holder.cvParent.setOnClickListener {
//            var i  = Intent(this,SellCatItemActivity::class.java)
                var i = Intent(activity,SellCatItemActivity::class.java)
                activity.startActivity(i)
            }
        } else {
            holder.userImage.setImageResource(list[position].catImage)
            holder.userName.text = list.get(position).catName
        }



    }

    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}


