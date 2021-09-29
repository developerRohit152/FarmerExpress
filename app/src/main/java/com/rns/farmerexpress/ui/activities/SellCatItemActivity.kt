package com.rns.farmerexpress.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.SellAdapter
import com.rns.farmerexpress.databinding.ActivityMainBinding
import com.rns.farmerexpress.databinding.ActivitySellCatItemBinding
import com.rns.farmerexpress.model.HomeModel
import com.rns.farmerexpress.model.SellModel

class SellCatItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellCatItemBinding
    lateinit var rv : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellCatItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rv = binding.rvSellCatItem
        val back = binding.ivBack
        back.setOnClickListener{
            finish()
        }

        showItemCatData()
    }

    private fun showItemCatData() {
        val list = ArrayList<SellModel>()
        list.add(SellModel(SellAdapter.VIEW_TYPE_TWO,R.drawable.tt,"गाय"))
        list.add(SellModel(SellAdapter.VIEW_TYPE_TWO,R.drawable.ttt ,"भेस"))
        list.add(SellModel(SellAdapter.VIEW_TYPE_TWO,R.drawable.timg,"बेल"))
        list.add(SellModel(SellAdapter.VIEW_TYPE_TWO,R.drawable.tractorimg,"अन्य"))

        val adapter = SellAdapter(this,list)
        rv.layoutManager = GridLayoutManager(this,2)
        rv.adapter = adapter
    }
}