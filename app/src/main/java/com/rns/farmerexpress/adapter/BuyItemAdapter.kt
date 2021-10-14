package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

import com.rns.farmerexpress.R
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.ui.activities.SellCatItemActivity
import com.rns.farmerexpress.ui.activities.SubBuyActivity
import com.rns.farmerexpress.ui.activities.SubCatItemFiledActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_home_design.view.*
import org.json.JSONArray
import java.lang.Exception
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.model.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat.startActivity





class BuyItemAdapter(private val activity: Activity, var list: ArrayList<SubBuyData>)  : RecyclerView.Adapter<BuyItemAdapter.MyViewHolder>(){

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userImage : ImageView = itemView.findViewById(R.id.cvUserImage)
        val userName : TextView = itemView.findViewById(R.id.tvUserName)
        val tvLocation : TextView = itemView.findViewById(R.id.tvLocation)
        val ivMenuOp : ImageView = itemView.findViewById(R.id.ivMenuOp)
        val tvContent : TextView = itemView.findViewById(R.id.tvContent)
        val tvMore : TextView = itemView.findViewById(R.id.tvMore)
        val tvDist : TextView = itemView.findViewById(R.id.tvDist)
        val tvTime : TextView = itemView.findViewById(R.id.tvTime)
        val image_slider : ImageSlider = itemView.findViewById(R.id.image_slider)
        val btnCall : Button = itemView.findViewById(R.id.btnCall)
        val llLike : LinearLayout = itemView.findViewById(R.id.llLike)
        val ivLike : ImageView = itemView.findViewById(R.id.ivLike)
        val tvLike : TextView = itemView.findViewById(R.id.tvLike)
        val llShare : LinearLayout = itemView.findViewById(R.id.llShare)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater : LayoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(layoutInflater.inflate(R.layout.recycler_buy_item_design,parent,false))

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder:MyViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val imageList = ArrayList<SlideModel>()
        Picasso.get().load("https://" + list[position].user_image).placeholder(R.drawable.profile).error(R.drawable.error).into(holder.userImage)
        holder.userName.text = list[position].user_name
        holder.tvLocation.text = list[position].place
//        holder.tvContent.text = list[position].description
        holder.tvTime.text = list[position].date
        try {
            val imgobj = JSONArray(list[position].store_images)
            for (i in 0..4) {
                val imgArr = imgobj.getJSONObject(i)
                val fimg = imgArr.optString("image")
                imageList.add(SlideModel("https://" + fimg, ScaleTypes.CENTER_CROP))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.image_slider.setImageList(imageList)
        val descList = ArrayList<String>()
        var desc = ""
        try {
            descList.add(list[position].description)
            val separator = ","
            desc = descList.joinToString(separator)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        holder.tvContent.text = desc

        holder.tvDist.text =  "दूरी ${list[position].distance.toString().substring(0,8)} किमी."
        var likeCounts : Int = list[position].likes
        var isLike = list[position].isLiked
        holder.tvLike.text = "${list[position].likes} लाइक"
        if (list[position].isLiked == 1){
            holder.ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
        }else{
            holder.ivLike.setImageResource(R.drawable.ic_like_24)
        }
        holder.llLike.setOnClickListener {
            if (isLike == 1){
                holder.ivLike.setImageResource(R.drawable.ic_like_24)
                isLike = 0
                likeCounts--
                likes(list[position].store_id.toString())
            }else{
                holder.ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
                isLike = 1
                likeCounts++
                likes(list[position].store_id.toString())
            }
            holder.tvLike.text = "$likeCounts लाइक"
        }

        holder.llShare.setOnClickListener {
            sharePost(list[position].description,list[position].store_id)
        }

        holder.ivMenuOp.setOnClickListener {
            val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
            val  builder  = AlertDialog.Builder(activity)
            builder.setTitle("Farmer Express")
                .setItems(R.array.list_option, DialogInterface.OnClickListener { dialog, which ->
                    when(which){
                        0 -> {  deletePost(session,list[position].store_id,position,list)

                        }
                        1-> {Toast.makeText(activity,"Hide",Toast.LENGTH_SHORT).show()}
                        2-> {Toast.makeText(activity,"Resport",Toast.LENGTH_SHORT).show()}
                        else -> {}
                    }
                })
            val alertDialog = builder.create()
            alertDialog.show()
        }

        holder.btnCall.setOnClickListener {
            val  builder  = AlertDialog.Builder(activity)
            builder.setTitle("Farmer Express")
                .setCancelable(false)
                .setMessage("क्या आप विक्रेता से बात करना चाहते हैं?")
                .setPositiveButton("हाँ",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        if (ContextCompat.checkSelfPermission(activity,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.CALL_PHONE),
                                101)

                        } else {

                            val intent =
                                Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list[position].contact))
                            activity.startActivity(intent)
                        }
                    }
                })
                .setNegativeButton("नहीं",object : DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                       dialog?.dismiss()
                    }

                })
            val alertDialog = builder.create()
            alertDialog.show()
        }

    }



    override fun getItemCount(): Int = list.size

    private fun sharePost(disc : String,postID: Int){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${disc}\nCheck the post on India's fastest growing app Farmer Express\nhttp://www.farmerexpress.online/api/${postID}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        activity.startActivity(shareIntent)
    }
    private fun likes(storeid : String){
        val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<LikeSubBuyModel> = service.likeSubBuy(session,storeid)
        try {
            call.enqueue(object  : Callback<LikeSubBuyModel>{
                override fun onResponse(
                    call: Call<LikeSubBuyModel>,
                    response: Response<LikeSubBuyModel>
                ) {
                    response.body()!!
                }

                override fun onFailure(call: Call<LikeSubBuyModel>, t: Throwable) {
                    Log.d("TAG", "onFailure:${t.message} ")
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun deletePost(session: String,postID: Int,position: Int,list: ArrayList<SubBuyData>){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<PostDelete> = service.deleteSubBuyPost(session, postID.toString(),"user")
        try {
            call.enqueue(object  : Callback<PostDelete>{
                override fun onResponse(call: Call<PostDelete>, response: Response<PostDelete>) {
                    val responseBody = response.body()!!
                    if (responseBody.status == "001"){
                        list.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position,list.size)
                        Toast.makeText(activity,"पोस्ट हटाए गई ",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(activity,"यह पोस्ट आप नहीं हटा सकते",Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<PostDelete>, t: Throwable) {
                    Log.d("OnResFailPostDelete", "onFailure: ${t.message}")
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}


