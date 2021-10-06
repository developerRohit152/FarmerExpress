package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.*
import kotlinx.android.synthetic.main.recycler_mandi_item.view.*

class MandiAdapter(private val activity: Activity, var list: ArrayList<Records>)  : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }
    class showLoading(itemView: View): RecyclerView.ViewHolder(itemView){

    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return MyViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_mandi_item, parent, false)
            )
        }
        return NewsAdapter.showLoading(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading, parent, false)
        )

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        if (list[position].viewType == VIEW_TYPE_ONE) {

            val data = list[position]
            holder.itemView.tvDist.text = data.district + ", " + data.state
            holder.itemView.tvMandiName.text = "मंडी " + data.market
            holder.itemView.tvVariety.text = "क़िस्म " + data.variety
            holder.itemView.tvCrop.text = "फसल  " + data.commodity
            holder.itemView.tvDate.text = "दिनाक " + data.arrival_date
            holder.itemView.tvMinPrice.text = "न्यूनतम ₹" + data.min_price
            holder.itemView.tvMaxPrice.text = "अधिकतम ₹" + data.max_price
            holder.itemView.tvModal.text = "मॉडल ₹" + data.modal_price
        }else{

        }
   }


    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}


