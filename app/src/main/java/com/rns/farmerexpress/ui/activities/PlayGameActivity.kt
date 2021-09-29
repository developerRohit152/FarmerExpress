package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rns.farmerexpress.R
import kotlinx.android.synthetic.main.activity_play_game.*
import java.text.DecimalFormat
import java.text.NumberFormat


class PlayGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)
        ivBack.setOnClickListener {
            finish()
        }
        tvCatName.text = intent.getStringExtra("catname")

        object : CountDownTimer(50000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                val f: NumberFormat = DecimalFormat("00")
                val sec = millisUntilFinished / 1000 % 60
                tvCount.text = f.format(sec)
            }

            // When the task is over it will print 00:00:00 there
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                tvCount.text = "00"
                Toast.makeText(this@PlayGameActivity, "Finish", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}