package com.rns.farmerexpress.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.rns.farmerexpress.R
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.Categories
import com.rns.farmerexpress.model.SellModel
import com.rns.farmerexpress.ui.activities.SellCatItemActivity
import com.rns.farmerexpress.ui.activities.SubCatItemFiledActivity
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.lang.Exception

class SellAdapter(private val activity: Activity, var list: ArrayList<Categories>)  : RecyclerView.Adapter<SellAdapter.MyViewHolder>(){

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
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_sell_item,parent,false))

    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        if (list[position].viewType == VIEW_TYPE_ONE) {
            Picasso.get().load("http://${list[position].catImage}").placeholder(R.drawable.imageplaceholder).error(R.drawable.error).into(holder.userImage)
            holder.userName.text = list[position].catName
            holder.cvParent.setOnClickListener {
                val i = Intent(activity,SellCatItemActivity::class.java)
                val v = list[position].id
                i.putExtra("id",v.toString())
                i.putExtra("catName",list[position].catName)
                activity.startActivity(i)
            }
        } else {
            Picasso.get().load("http://${list[position].catImage}").placeholder(R.drawable.imageplaceholder).error(R.drawable.error).into(holder.userImage)
            holder.userName.text = list[position].catName
            val typeList = ArrayList<String>()
            val lengthList = ArrayList<String>()
            val placeholderList = ArrayList<String>()

            holder.cvParent.setOnClickListener {
                typeList.clear()
                lengthList.clear()
                placeholderList.clear()
                try {
                    val fieldObj = JSONArray(list[position].field)
                    for (i in 0..fieldObj.length()) {
                        val dataArr = fieldObj.getJSONObject(i)
                        val types = dataArr.optString("type")
                        val lengths = dataArr.optString("length")
                        val placeholders = dataArr.optString("placeholder")
                        typeList.add(types)
                        lengthList.add(lengths)
                        placeholderList.add(placeholders)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val i = Intent(activity,SubCatItemFiledActivity::class.java)
                i.putStringArrayListExtra("typeList",typeList)
                i.putStringArrayListExtra("lengthList",lengthList)
                i.putStringArrayListExtra("placeholderList",placeholderList)
                activity.startActivity(i)

            }
        }



    }

    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}


