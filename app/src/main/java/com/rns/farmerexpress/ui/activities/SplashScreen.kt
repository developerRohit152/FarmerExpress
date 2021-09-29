package com.rns.farmerexpress.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.rns.farmerexpress.R
import com.rns.farmerexpress.commonUtility.PreferenceConnector

class SplashScreen : AppCompatActivity() {
     lateinit var mHandler: Handler
     lateinit var mRunnable: Runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        mRunnable = Runnable {
            if(PreferenceConnector.readString(this,PreferenceConnector.loginstatus,"").equals("true")) {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                finish()

            }
        }

        mHandler = Handler()
        mHandler.postDelayed(mRunnable, 1000)

    }

    override fun onDestroy() {
        super.onDestroy()
        if (mHandler != null && mRunnable != null) mHandler.removeCallbacks(mRunnable)
    }

}