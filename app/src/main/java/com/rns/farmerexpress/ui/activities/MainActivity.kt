package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.icu.lang.UCharacter.IndicPositionalCategory.BOTTOM
import android.icu.lang.UCharacter.IndicPositionalCategory.BOTTOM_AND_LEFT
import android.media.Image
import android.os.Bundle
import android.system.Os.close
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.core.view.marginBottom
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.navigation.NavigationView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.ActivityMainBinding
import de.hdodenhof.circleimageview.CircleImageView


import android.widget.Toast

import androidx.annotation.NonNull
import androidx.core.view.children
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.util.CollectionUtils.setOf
import com.google.android.gms.tasks.OnCompleteListener
import com.rns.farmerexpress.ui.fragments.ProfileEditFragment
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfigurationd: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var uImage: ImageView
    private lateinit var uName: TextView

    @SuppressLint("RestrictedApi", "WrongConstant", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.appBarMain.toolbar)
        setSupportActionBar(binding.appBarMain.toolbar.toolbar)
//        getSupportActionBar()!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val navView: BottomNavigationView = binding.navbView
        val navViewd: NavigationView = binding.navView
        drawerLayout = binding.drawerLayout
        val headerView : View = navViewd.getHeaderView(0)
        uImage = headerView.findViewById(R.id.imageView)
        uName = headerView.findViewById(R.id.nav_name)
        uImage.setImageResource(R.drawable.profile)
        uName.text = PreferenceConnector.readString(this,PreferenceConnector.profileName,"")
        byteToImage(PreferenceConnector.readString(this,PreferenceConnector.profilePic,""))



        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        navViewd.setNavigationItemSelectedListener { item ->
//
//        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val navControllerd = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val bNMV: BottomNavigationMenuView = navView.getChildAt(0) as BottomNavigationMenuView
        val v: View = bNMV.getChildAt(2)
        val itemView: BottomNavigationItemView = v as BottomNavigationItemView
//        itemView.setIconSize(60)

        navViewd.menu.getItem(7).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
            startActivity(Intent(this,NewsActivity::class.java))
            true
        })
        navViewd.menu.getItem(7).setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
            startActivity(Intent(this,GamesActivity::class.java))
            true
        })
//        itemView.textAlignment  = BOTTOM
//        itemView.setPadding(0,0,0,20)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_buy,
                R.id.navigation_sell,
                R.id.navigation_question,
                R.id.navigation_info
            )
        )
        appBarConfigurationd = AppBarConfiguration(
            setOf(
                R.id.navigation_question,
                R.id.navigation_sell,
                R.id.navigation_home,

                ), drawerLayout
        )


//        setupActionBarWithNavController(navControllerd, appBarConfigurationd)
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navViewd.setupWithNavController(navControllerd)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfigurationd) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            R.id.action_settings -> {
                var i = Intent(this, LoginActivity::class.java)
                PreferenceConnector.writeString(this, PreferenceConnector.loginstatus, "false")
                startActivity(i)
                finish()
//                mGoogleSignInClient.signOut()
//                    .addOnCompleteListener(this, OnCompleteListener<Void?> {
//                    })
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun byteToImage(imageUrl: String) {
        try {
            val byte: ByteArray = Base64.decode(imageUrl, Base64.DEFAULT)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
            uImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}