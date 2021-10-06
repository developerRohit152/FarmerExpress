package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.*
import kotlinx.android.synthetic.main.fragment_homes.view.*
import kotlinx.android.synthetic.main.recycler_mandi_item.view.*

class MandiAdapter(private val activity: Activity, var list: ArrayList<Records>)  : RecyclerView.Adapter<MandiAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_mandi_item,parent,false))

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        val data = list[position]
        holder.itemView.tvDist.text = data.district +", "+data.state
        holder.itemView.tvMandiName.text = "मंडी "+data.market
        holder.itemView.tvVariety.text = "क़िस्म "+data.variety
        holder.itemView.tvCrop.text = "फसल  "+data.commodity
        holder.itemView.tvDate.text = "दिनाक "+data.arrival_date
        holder.itemView.tvMinPrice.text = "न्यूनतम ₹"+data.min_price
        holder.itemView.tvMaxPrice.text = "अधिकतम ₹"+data.max_price
        holder.itemView.tvModal.text = "मॉडल ₹"+data.modal_price
   }

    override fun getItemCount(): Int = list.size
}


