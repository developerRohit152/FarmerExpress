package com.rns.farmerexpress.ui.activities

import `in`.aabhasjindal.otptextview.OtpTextView
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.ActivityOtpactivityBinding
import com.rns.farmerexpress.model.OTPVeri
import com.rns.farmerexpress.model.SessionRes
import com.rns.farmerexpress.model.dataSession
import com.rns.farmerexpress.ui.fragments.ProfileEditFragment
import kotlinx.android.synthetic.main.activity_otpactivity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    lateinit var btnVerify: Button
    lateinit var otp: OtpTextView
    lateinit var tvWarning: TextView
    lateinit var tvVeriNumber: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeCompnonets()
        val phone = intent.getStringExtra("phone")
        tvVeriNumber.text = "Verifying $phone"
        btnVerify.setOnClickListener {
            if (otp.otp!!.length < 4) {
                tvWarning.visibility = View.VISIBLE
                tvWarning.text = "अपना सही 4 अंकों का OTP नंबर लिखे"
                otp.showError()
            } else {
            startOTPVerification(otp.otp?.trim().toString(),phone.toString())
        }
            }
}

    private fun startOTPVerification(otp : String,phonen : String) {
        pbOTP.visibility = View.VISIBLE
        val service : ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call : Call<OTPVeri> = service.otpVeriy(otp,phonen)
        try {
            call.enqueue(object : Callback<OTPVeri>{
                override fun onResponse(call: Call<OTPVeri>, response: Response<OTPVeri>) {
                    val responseBody = response.body()!!
                    Log.d("res", "res "+response.message())
                    val otpRes : OTPVeri = OTPVeri(responseBody.status,responseBody.session,responseBody.message)
                    Log.d("otpred", "onResponse: "+otpRes)
                    val  session = responseBody.session
                   if (otpRes.status.equals("002")){
                       Toast.makeText(this@OTPActivity,"आपका OTP नंबर गलत हैं ",Toast.LENGTH_SHORT).show()
                    }else{
                       pbOTP.visibility = View.GONE
                        val i  = Intent(this@OTPActivity,ProfileActivity::class.java)
                       i.putExtra("session",session)
                       val bundle = Bundle()
                       bundle.putString("sessions", session)
                       sessionsc = session.toString()
                       ProfileEditFragment().arguments = bundle
                       PreferenceConnector.writeString(this@OTPActivity,PreferenceConnector.profilestatus,session)
                       PreferenceConnector.writeString(this@OTPActivity,PreferenceConnector.USER_PHONE,phonen)
                       startActivity(i)
                       finishAffinity()
                    }
                }

                override fun onFailure(call: Call<OTPVeri>, t: Throwable) {
                    Log.d("otpred", "onResponse: "+t.message)

                }
            })
        }catch (e : Exception){
            e.printStackTrace()
        }


    }

companion object{
    var sessionsc : String? = null
}


    private fun initializeCompnonets() {
    btnVerify = binding.btnVerify
    otp = binding.otpView
    tvWarning = binding.tvWarning
        tvVeriNumber = binding.tvVeriNumber
}
}