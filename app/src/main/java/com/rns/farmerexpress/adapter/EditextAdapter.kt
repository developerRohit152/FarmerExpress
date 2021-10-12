package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.AgriCatModel
import com.rns.farmerexpress.model.EdittextModel
import java.lang.Exception

class EditextAdapter(private val activity: Activity, var list: ArrayList<EdittextModel>)  : RecyclerView.Adapter<EditextAdapter.MyViewHolder>(){
    var editTextArrayList = ArrayList<String>()
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val edittext : EditText = itemView.findViewById(R.id.etSub)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context);
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_edittext_item,parent,false))

    }

    override fun onBindViewHolder(holder:MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.edittext.hint = list[position].placeholder
        if (list[position].type == "text") {
            holder.edittext.inputType = InputType.TYPE_CLASS_TEXT
        }else if (list[position].type == "number"){
            holder.edittext.inputType = InputType.TYPE_CLASS_NUMBER
        }
        holder.edittext.filters = arrayOf(InputFilter.LengthFilter(Integer.parseInt(list[position].length)))
        val  c : Int = Integer.parseInt(list[position].length)

//        Toast.makeText(activity,holder.edittext.text,Toast.LENGTH_LONG).show()
        holder.edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("TAG", "beforeTextChanged: ")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("TAG", "afterTextChanged: ")
                    editTextArrayList.add(position,holder.edittext.text.toString())

                        list[position].editTextValList.add(position,holder.edittext.text.toString())
                Toast.makeText(activity,editTextArrayList.toString(),Toast.LENGTH_LONG).show()

            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("TAG", "afterTextChanged: ")

            }

        })



   }

    override fun getItemCount(): Int = list.size
}




