package com.rns.farmerexpress.adapter

import android.app.Activity
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.AgriCatModel
import com.rns.farmerexpress.model.EdittextModel

class EditextAdapter(private val activity: Activity, var list: ArrayList<EdittextModel>)  : RecyclerView.Adapter<EditextAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val edittext : EditText = itemView.findViewById(R.id.etSub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_edittext_item,parent,false))

    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {
        holder.edittext.hint = list[position].placeholder
        if (list[position].type == "text") {
            holder.edittext.inputType = InputType.TYPE_CLASS_TEXT
        }else if (list[position].type == "number"){
            holder.edittext.inputType = InputType.TYPE_CLASS_NUMBER
        }
        holder.edittext.filters = arrayOf(InputFilter.LengthFilter(Integer.parseInt(list[position].length)))
   }

    override fun getItemCount(): Int = list.size
}


