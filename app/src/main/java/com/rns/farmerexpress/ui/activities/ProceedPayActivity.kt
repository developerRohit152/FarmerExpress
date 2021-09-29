package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.rns.farmerexpress.R
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import kotlinx.android.synthetic.main.activity_proceed_pay.*
import kotlinx.android.synthetic.main.fragment_recharge.*
import org.json.JSONObject

class ProceedPayActivity : AppCompatActivity() , PaymentResultListener {
    var amount : Int = 1000

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proceed_pay)
        tvType.text = "रिचार्ज टाइप : ${intent.getStringExtra("type")}"
        tvOfferDetails.text = "रिचार्ज डिटेल्स : ${intent.getStringExtra("offer")}"
        amount = intent.getStringExtra("rupay")?.trim().toString().toInt() * 100
        tvRupay.text = "₹ ${intent.getStringExtra("rupay")}"
        tvMob.text = "फोन नंबर : ${PreferenceConnector.readString(this,PreferenceConnector.MOBILE,"")}"
        tvState.text = "राज्य : ${PreferenceConnector.readString(this,PreferenceConnector.stateName,"")}"
        tvOp.text = "ऑपरेटर : ${PreferenceConnector.readString(this,PreferenceConnector.opName,"")}"

        Checkout.preload(applicationContext)

        tvProceed.setOnClickListener {
            startPayment()
        }
    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this

        val co = Checkout()
        co.setKeyID("rzp_test_YfWkpKaZdEHNMT")

        try {
            val options = JSONObject()
            options.put("name", PreferenceConnector.readString(this,PreferenceConnector.profileName,""))
            options.put("description","Dummy Des")
            //You can omit the image option to fetch the image from dashboard
            options.put("image","https://www.pinclipart.com/picdir/big/221-2217551_computer-user-clip-art.png")
            options.put("theme.color", "#2EC02B")
            options.put("currency","INR")
            options.put("send_sms_hash",true)
//            options.put("order_id", "order_DBJOWzybf0sJbb")
            options.put("amount","$amount")//pass amount in currency subunits

            val retryObj =  JSONObject()
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","rohit.kumar@example.com")
            prefill.put("contact",PreferenceConnector.readString(this,PreferenceConnector.MOBILE,""))

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,p0,Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.d("OnFail", "onPaymentError: $p1")
    }

    override fun onDestroy() {
        super.onDestroy()
        Checkout.clearUserData(this)
    }
}