package com.rns.farmerexpress.ui.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.EditextAdapter
import com.rns.farmerexpress.model.EdittextModel
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

import android.content.Intent

import android.content.DialogInterface
import android.database.Cursor
import android.provider.MediaStore
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.widget.NestedScrollView
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.SellItemModel
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

import java.io.InputStream


class SubCatItemFiledActivity : AppCompatActivity() {
    private var typeList = ArrayList<String>()
    private var lengthList = ArrayList<String>()
    private var placeholderList = ArrayList<String>()
    private var nameList = ArrayList<String>()
    private var defList = ArrayList<String>()
    private var imageList = ArrayList<String>()
    private var sImage = ""
    val v = ArrayList<String>()
    private var subCatID : String = ""
    private var list = ArrayList<EdittextModel>()
    private lateinit var adapter: EditextAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var ivFront : ImageView
    private lateinit var ivBack : ImageView
    private lateinit var ivLeft : ImageView
    private lateinit var ivRight : ImageView
    private lateinit var btnSubmit: Button
    private lateinit var pbSubCat: ProgressBar
    private lateinit var nsSubCat: NestedScrollView
    private var flagFront = false
    private var flagBack = false
    private var flagRight = false
    private var flagLeft = false
    val lists = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_cat_item_fileds)
        recyclerView = findViewById(R.id.rvEdittext)
        ivFront = findViewById(R.id.ivFront)
        ivBack = findViewById(R.id.ivBack)
        ivLeft = findViewById(R.id.ivLeft)
        ivRight = findViewById(R.id.ivRight)
        btnSubmit = findViewById(R.id.btnSubmit)
        pbSubCat = findViewById(R.id.pbSubCat)
        nsSubCat = findViewById(R.id.nsSubCat)
        typeList = intent.getStringArrayListExtra("typeList") as ArrayList<String>
        lengthList = intent.getStringArrayListExtra("lengthList") as ArrayList<String>
        placeholderList = intent.getStringArrayListExtra("placeholderList") as ArrayList<String>
        nameList = intent.getStringArrayListExtra("nameList") as ArrayList<String>
        defList = intent.getStringArrayListExtra("defList") as ArrayList<String>
        subCatID = intent.getStringExtra("subcatid").toString()
        list.clear()
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        try {
            for (i in 0..typeList.size) {
                if (typeList[i] == "input")
                list.add(EdittextModel(EditextAdapter.VIEW_TYPE_EDITTEXT,typeList[i],lengthList[i],placeholderList[i],nameList[i],"",lists))
                else
                    list.add(EdittextModel(EditextAdapter.VIEW_TYPE_SPINNER,typeList[i],lengthList[i],placeholderList[i],nameList[i],defList[i],lists))
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
        adapter = EditextAdapter(this,list)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        val contact = PreferenceConnector.readString(this,PreferenceConnector.USER_PHONE,"")
        val latitude = PreferenceConnector.readString(this,PreferenceConnector.LATITUDE,"")
        val longitude = PreferenceConnector.readString(this,PreferenceConnector.LONGITUDE,"")
        btnSubmit.setOnClickListener {
            try {
                for (i in 0..list.size){
                    v.add(list[i].editTextValList[i])
}
            }catch (e : Exception){
                e.printStackTrace()
            }
            if (v.isEmpty()){
                Toast.makeText(this,"टेक्स्ट फील्ड को रिक्त ना छोड़े",Toast.LENGTH_LONG).show()
            }else{
                pbSubCat.visibility = View.VISIBLE
                nsSubCat.visibility = View.GONE
               postSellData(subCatID,v.toString(),imageList,contact,latitude,longitude)
            }

        }
        ivFront.setOnClickListener {
            if(checkAndRequestPermissions(this)){
                flagFront = true
                flagBack = false
                flagRight = false
                flagLeft = false
                chooseImage(this)
            }
        }
        ivBack.setOnClickListener {
            if(checkAndRequestPermissions(this)){
                flagFront = false
                flagBack = true
                flagRight = false
                flagLeft = false
                chooseImage(this)
            }
        }
        ivLeft.setOnClickListener {
            if(checkAndRequestPermissions(this)){
                flagFront = false
                flagBack = false
                flagRight = false
                flagLeft = true
                chooseImage(this)
            }
        }
        ivRight.setOnClickListener {
            if(checkAndRequestPermissions(this)){
                flagFront = false
                flagBack = false
                flagRight = true
                flagLeft = false
                chooseImage(this)
            }
        }



    }


    private fun postSellData(subCatIds : String,desc : String,image : ArrayList<String>,contact : String,latitude:String,longitude:String){
        val ja : JSONArray = JSONArray(image)
            Log.d("TAG", "postSellData: $ja")

        val session  = PreferenceConnector.readString(this,PreferenceConnector.profilestatus,"")
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<SellItemModel> = service.postSellData(session, "user",subCatIds,desc,ja.toString(),contact,latitude,longitude)
        try {
            call.enqueue(object : Callback<SellItemModel>{
                override fun onResponse(
                    call: Call<SellItemModel>,
                    response: Response<SellItemModel>
                ) {
                    val responseBody = response.body()
                    Log.d("onSellItemRes", "onResponse: $responseBody")
                    pbSubCat.visibility = View.GONE
                    nsSubCat.visibility = View.VISIBLE
                    Toast.makeText(this@SubCatItemFiledActivity,responseBody.toString(),Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<SellItemModel>, t: Throwable) {
                    Log.d("onSellItemResFail", "onFailure: ${t.message}")
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }


    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> if (ContextCompat.checkSelfPermission(
                    this@SubCatItemFiledActivity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "पोस्ट की फोटो के लिए कमेरे की पर्मिशन की जरूरत हैं", Toast.LENGTH_SHORT
                )
                    .show()
            } else if (ContextCompat.checkSelfPermission(
                    this@SubCatItemFiledActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    applicationContext,
                    "पोस्ट की फोटो के लिए स्टोरेज की पर्मिशन की जरूरत हैं",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                chooseImage(this@SubCatItemFiledActivity)
            }
        }
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "केमरे से फोटो ले",
            "गेलेरी से फोटो ले",
            "बाहर निकले"
        ) // create a menuOption Array
        // create a dialog for showing the optionsMenu
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        // set the items in builder
        builder.setItems(optionsMenu,
            DialogInterface.OnClickListener { dialogInterface, i ->
                if (optionsMenu[i] == "केमरे से फोटो ले") {
                    // Open the camera and get the photo
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                } else if (optionsMenu[i] == "गेलेरी से फोटो ले") {
                    // choose from  external storage
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                } else if (optionsMenu[i] == "बाहर निकले") {
                    dialogInterface.dismiss()
                }
            })
        builder.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                        val selectedImage = data.extras!!["data"] as Bitmap
                        val filePath: Uri? = data.data
                    if (flagFront) {
                        ivFront.setImageBitmap(selectedImage)
//                        val imageUri: Uri = data.data!!
//                        val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
//                        val selectedImagec = BitmapFactory.decodeStream(imageStream)
                        val encodedImage: String = encodeImage(selectedImage)
                        imageList.add(encodedImage)
                    }else if (flagBack){
                        ivBack.setImageBitmap(selectedImage)
                        val encodedImage: String = encodeImage(selectedImage)
                        imageList.add(encodedImage)
                    }else if (flagLeft){
                        ivLeft.setImageBitmap(selectedImage)
                        val encodedImage: String = encodeImage(selectedImage)
                        imageList.add(encodedImage)
                    }else if (flagRight){
                        ivRight.setImageBitmap(selectedImage)
                        val encodedImage: String = encodeImage(selectedImage)
                        imageList.add(encodedImage)
                    }



                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage: Uri? = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor: Cursor? =
                            contentResolver.query(selectedImage, filePathColumn, null, null, null)
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath: String = cursor.getString(columnIndex)
                            if (flagFront) {
                                ivFront.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                                convertBase64(selectedImage)
//                                imageList.add(picturePath)
                            }else if (flagBack){
                                ivBack.setImageBitmap(BitmapFactory.decodeFile(picturePath))
//                                imageList.add(picturePath)
                                convertBase64(selectedImage)
                            }else if (flagLeft){
                                ivLeft.setImageBitmap(BitmapFactory.decodeFile(picturePath))
//                                imageList.add(picturePath)
                                convertBase64(selectedImage)
                            }else if (flagRight){
                                ivRight.setImageBitmap(BitmapFactory.decodeFile(picturePath))
//                                imageList.add(picturePath)
                                convertBase64(selectedImage)
                            }else{}
                            cursor.close()
                        }
                    }
                }
            }
        }
    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
    private fun convertBase64(filePath: Uri?) {
        try {
            val bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, filePath)
            val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byte: ByteArray = byteArrayOutputStream.toByteArray()
            sImage = Base64.encodeToString(byte, Base64.DEFAULT)
//                      byteToImage(sImage)
            imageList.add(sImage)
        } catch (e: Exception) {

        }
    }

    companion object{
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    }
}