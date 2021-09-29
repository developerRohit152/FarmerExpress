package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.RechargeAdapter.*
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.Plan
import com.rns.farmerexpress.model.RechargeModel
import com.rns.farmerexpress.ui.activities.ProceedPayActivity
import kotlinx.android.synthetic.main.recharge_design.view.*

class RechargeAdapter(val activity : Activity,val list:ArrayList<Plan>) : RecyclerView.Adapter<MyViewHolder>() {
    class MyViewHolder(itemView : View):RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recharge_design,parent,false));
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tvType.text = list[position].Type
        holder.itemView.tvOfferDetails.text = list[position].desc + "\n\nवेधता : ${list[position].validity}"
        holder.itemView.tvRupay.text = "₹ "+ list[position].rs
        holder.itemView.tvPlanSec.setOnClickListener {
            val i = Intent(activity, ProceedPayActivity::class.java)
            i.putExtra("type", holder.itemView.tvType.text.toString())
            i.putExtra("offer", holder.itemView.tvOfferDetails.text.toString())
            i.putExtra("rupay", list[position].rs)
            activity.startActivity(i)
        }
    }

    override fun getItemCount(): Int = list.size

}