package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.GameCatAdapter
import com.rns.farmerexpress.adapter.NewsAdapter
import com.rns.farmerexpress.model.GameCat
import com.rns.farmerexpress.model.NewsModel
import kotlinx.android.synthetic.main.activity_games.*
import kotlinx.android.synthetic.main.fragment_sports.*

class GamesActivity : AppCompatActivity() {
    lateinit var lists: ArrayList<GameCat>
    lateinit var adapter: GameCatAdapter
    lateinit var layoutManager: LinearLayoutManager

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)
        ivBack.setOnClickListener {
            finish()
        }

        lists = ArrayList()
        lists.add(GameCat(R.drawable.flogo,"खेती संबंधित सवाल "))
        lists.add(GameCat(R.drawable.tt,"बीज  संबंधित सवाल "))
        lists.add(GameCat(R.drawable.tractorimg,"पशु  संबंधित सवाल "))
        lists.add(GameCat(R.drawable.timg,"वाहन  संबंधित सवाल "))
        lists.add(GameCat(R.drawable.ttt,"खेती उपकरण संबंधित सवाल "))

        adapter = GameCatAdapter(this,lists)
        layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        rvGameCatList.layoutManager = layoutManager

        rvGameCatList.adapter = adapter


    }
}