package com.rns.farmerexpress.ui.activities

import android.R.attr
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.databinding.ActivityLoginBinding
import com.rns.farmerexpress.model.LoginResponse
import com.rns.farmerexpress.model.SubDist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import android.R.attr.data
import android.app.PendingIntent.getActivity
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException





class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var etPhone: EditText
    private lateinit var btnLogin: Button
    lateinit var llGAuth: LinearLayout
    lateinit var mGoogleSignInClient: GoogleSignInClient
     var RC_SIGN_IN: Int = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeComponents()
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)

        btnLogin.setOnClickListener {
            if (etPhone.text.length < 10) {
                etPhone.error = "अपना सही 10 अंकों का फोन नंबर लिखे"
            } else {
                val i = Intent(this, OTPActivity::class.java)
                startLogin(etPhone.text.trim().toString())
                i.putExtra("phone", etPhone.text.trim().toString())
                startActivity(i)
            }
        }
        llGAuth.setOnClickListener {
//            startGAuthLogin()
        }


    }

    private fun startGAuthLogin() {
            val signInIntent :Intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val personName = acct.displayName
                val personGivenName = acct.givenName
                val personFamilyName = acct.familyName
                val personEmail = acct.email
                val personId = acct.id
                val personPhoto: Uri = acct.photoUrl
            }
            // Signed in successfully, show authenticated UI.
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }


    private fun startLogin(phone: String) {
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<LoginResponse> = service.userLogin(phone)
        try {
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val responseBody = response.body()!!

                    Log.d("res", "onResponse: " + response.message())

                    var loginRes: LoginResponse =
                        LoginResponse(responseBody.status, responseBody.message)
                    Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_LONG)
                        .show()
                    Log.d("output", "Output :  " + loginRes)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: " + t.message)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun initializeComponents() {
        etPhone = binding.etPhoneNumber
        btnLogin = binding.btnLogin
        llGAuth = binding.llGAuth
    }

}