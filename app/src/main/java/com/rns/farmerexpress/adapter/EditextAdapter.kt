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
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray

import com.rns.farmerexpress.R
import com.rns.farmerexpress.model.AgriCatModel
import com.rns.farmerexpress.model.EdittextModel
import java.lang.Exception

class EditextAdapter(private val activity: Activity, var list: ArrayList<EdittextModel>)  :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    companion object{
        val VIEW_TYPE_EDITTEXT = 1
        val VIEW_TYPE_SPINNER = 2
        val VIEW_TYPE_NULL = 3
    }
    var editTextArrayList = ArrayList<String>()
    var editTextSpinnerList = ArrayList<String>()

    private inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val edittext : EditText = itemView.findViewById(R.id.etSub)
        fun bind(position: Int){
            edittext.hint = list[position].placeholder
            if (list[position].type == "text") {
                edittext.inputType = InputType.TYPE_CLASS_TEXT
            }
            else{
                edittext.inputType = InputType.TYPE_CLASS_TEXT
            }
                edittext.filters = arrayOf(InputFilter.LengthFilter(Integer.parseInt(list[position].length)))
            edittext.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    Log.d("TAG", "beforeTextChanged: ")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    Log.d("TAG", "afterTextChanged: ")
                    editTextArrayList.add(position, edittext.text.toString())

                    list[position].editTextValList.add(position,list[position].name +" : "+edittext.text.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                    Log.d("TAG", "afterTextChanged: ")

                }

            })

        }

    }
    private inner class Spinners(itemView: View):RecyclerView.ViewHolder(itemView){
        val sp : Spinner = itemView.findViewById(R.id.spBuyAdd)
        fun bind(position: Int){
            editTextSpinnerList.add(0,list[position].placeholder)
            editTextSpinnerList.add(1,"static data")
            val string : ArrayList<String> = list[position].default.split("|") as ArrayList<String>
            val adapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
                activity, android.R.layout.simple_spinner_dropdown_item,
                editTextSpinnerList as List<Any?>
            )
            sp.adapter = adapter

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_EDITTEXT) {
            val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
            return MyViewHolder(
                layoutInflater.inflate(
                    R.layout.recycler_edittext_item,
                    parent,
                    false
                )
            )
        }else{
            return Spinners(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_buy_item_input_design, parent, false)
            )
        }

    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (list[position].viewType == VIEW_TYPE_EDITTEXT){
            (holder as MyViewHolder).bind(position)
        }else{
            (holder as Spinners).bind(position)
        }



   }

    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}




