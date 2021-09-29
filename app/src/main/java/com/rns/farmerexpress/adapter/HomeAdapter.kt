package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Presentation
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.JsonObject

import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.GetPostData
import com.rns.farmerexpress.model.LikeCommentShareModel
import com.rns.farmerexpress.model.PostDatas
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_home_bg_design.view.*
import kotlinx.android.synthetic.main.recycler_home_design.view.*
import kotlinx.android.synthetic.main.recycler_home_poll_design.view.*
import kotlinx.android.synthetic.main.recycler_home_poll_design.view.cvUserImagec
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import com.rns.farmerexpress.ui.activities.CommentActivity
import androidx.core.content.ContextCompat.startActivity
import com.rns.farmerexpress.model.PollSelectedModel


class HomeAdapter(private val activity: Activity, var list: ArrayList<PostDatas>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_POST = 1
        const val VIEW_TYPE_POLL = 2
        const val VIEW_TYPE_BG = 3
        const val VIEW_TYPE_LOADING = 4
    }

    private inner class postViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.cvUserImage)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val location: TextView = itemView.findViewById(R.id.tvLocation)
        val contentc: TextView = itemView.findViewById(R.id.tvContent)
        val timec: TextView = itemView.findViewById(R.id.tvTime)
        val images: ImageSlider = itemView.findViewById(R.id.image_slider)
        val likeCount: TextView = itemView.findViewById(R.id.tvLike)
        val commentCount: TextView = itemView.findViewById(R.id.tvComment)
        val shareCount: TextView = itemView.findViewById(R.id.tvShare)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val postDatas = list[position]
//            message.text = recyclerViewModel.textData
            Picasso.get().load("https://" + postDatas.user_image).placeholder(R.drawable.profile)
                .error(R.drawable.profile).into(userImage)
                        Picasso.get().load("https://" + postDatas.user_image).placeholder(R.drawable.profile)
                .error(R.drawable.profile).into(itemView.cvUserImagec)
            val imageList = ArrayList<SlideModel>()
            val tagList = ArrayList<String>()
            try {
                val imgobj = JSONArray(postDatas.post_images)
                val imgArr = imgobj.getJSONObject(0)
                val fimg = imgArr.optString("image")
                imageList.add(SlideModel("https://" + fimg, ScaleTypes.CENTER_CROP))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val tagObj = JSONArray(postDatas.post_tags)
                for (i in 0..5) {
                    val tagArr = tagObj.getJSONObject(i)
                    val tags = tagArr.optString("tag")
                    tagList.add(tags)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            userName.text = postDatas.user_name
            location.text = list[position].location
            contentc.text = list[position].discription + "\n" + tagList.toString()
            timec.text = list[position].date
            likeCount.text = "${list[position].likes}" + " लाइक"
            commentCount.text = list[position].comments + " कमेंट्स "
            shareCount.text = list[position].shares + " शेयर"
            images.setImageList(imageList)
            var likeCounts : Int = list[position].likes
            var isLike = postDatas.isLiked
            if (postDatas.isLiked == 1){
                itemView.ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
            }else{
                itemView.ivLike.setImageResource(R.drawable.ic_like_24)
            }
           itemView.llLike.setOnClickListener {
               if (isLike == 1){
                   itemView.ivLike.setImageResource(R.drawable.ic_like_24)
                   isLike = 0
                   likeCounts--
                   likes(postDatas.post_id,"like")
               }else{
                   itemView.ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
                   isLike = 1
                   likeCounts++
                   likes(postDatas.post_id,"like")
               }
               likeCount.text = "$likeCounts लाइक"
           }
            itemView.etCommentI.setOnClickListener {
               startCommentActivity(postDatas.post_id)
            }
            itemView.llComment.setOnClickListener {
              startCommentActivity(postDatas.post_id)
            }
            var share = true
            var shareCounts : Int = postDatas.shares.toInt()
            itemView.llShare.setOnClickListener {
               sharePost(postDatas.discription,postDatas.post_id)
                likes(postDatas.post_id,"share")
                if(share) {
                    shareCounts++
                    shareCount.text = "$shareCounts शेयर "
                    share = false
                }
            }

        }
    }

    private inner class pollViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.cvUserImage)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val location: TextView = itemView.findViewById(R.id.tvLocation)
        val contentc: TextView = itemView.findViewById(R.id.tvContent)
        val timec: TextView = itemView.findViewById(R.id.tvTime)
        val likeCount: TextView = itemView.findViewById(R.id.tvLikeP)
        val commentCount: TextView = itemView.findViewById(R.id.tvComment)
        val shareCount: TextView = itemView.findViewById(R.id.tvShare)
        val tvOption1: TextView = itemView.findViewById(R.id.tvOption1)
        val tvOption3: TextView = itemView.findViewById(R.id.tvOption3)
        val tvOption4: TextView = itemView.findViewById(R.id.tvOption4)
        val tvOption5: TextView = itemView.findViewById(R.id.tvOption5)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val tagList = ArrayList<String>()
            val pollList = ArrayList<String>()

            val postDatas = list[position]
            Picasso.get().load("https://" + postDatas.user_image).placeholder(R.drawable.profile)
                .error(R.drawable.profile).into(userImage)
            Picasso.get().load("https://" + postDatas.user_image).placeholder(R.drawable.profile)
                .error(R.drawable.profile).into(itemView.cvUserImagec)
            try {
                val tagObj = JSONArray(postDatas.post_tags)
                for (i in 0..5) {
                    val tagArr = tagObj.getJSONObject(i)
                    val tags = tagArr.optString("tag")
                    tagList.add(tags)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                val pollObj = JSONArray(postDatas.poll)
                for (i in 0..5) {
                    val pollArr = pollObj.getJSONObject(i)
                    val polls = pollArr.optString("poll")
//                    polls[4].toString()
                    pollList.add(polls)
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
            try {
                tvOption1.text = pollList[0]
                itemView.tvOption2.text = pollList[1]
//                tvOption3.text = pollList[2]
//                tvOption4.text = pollList[3]
                if (pollList.size>=3){
                    itemView.cParent3.visibility = View.VISIBLE
                    tvOption3.text = pollList[2]
                }else{
                    itemView.cParent3.visibility = View.GONE
                }
                if (pollList.size>=4){
                    itemView.cParent4.visibility = View.VISIBLE
                    tvOption4.text = pollList[3]
                }else{
                    itemView.cParent4.visibility = View.GONE
                }
                if (pollList.size>=5){
                    itemView.cParent5.visibility = View.VISIBLE
                    tvOption5.text = pollList[4]
                }else{
                    itemView.cParent5.visibility = View.GONE
                }

            }catch (e:Exception){
                e.printStackTrace()
            }
            userName.text = postDatas.user_name
            location.text = list[position].location
            contentc.text = list[position].discription +"\n${tagList}"
            timec.text = list[position].date
            likeCount.text = "${list[position].likes}" + " लाइक"
            commentCount.text = list[position].comments + " कमेंट्स "
            shareCount.text = list[position].shares + " शेयर"
            var likeCounts : Int = list[position].likes
            var isLike = postDatas.isLiked
            if (postDatas.isLiked == 1){
                itemView.ivLikeP.setImageResource(R.drawable.ic_round_thumb_up_24)
            }else{
                itemView.ivLikeP.setImageResource(R.drawable.ic_like_24)
            }

            itemView.llLikeP.setOnClickListener {
                if (isLike == 1){
                    itemView.ivLikeP.setImageResource(R.drawable.ic_like_24)
                    isLike = 0
                    likeCounts--
                    likes(postDatas.post_id,"like")
                }else{
                    itemView.ivLikeP.setImageResource(R.drawable.ic_round_thumb_up_24)
                    isLike = 1
                    likeCounts++
                    likes(postDatas.post_id,"like")
                }
                likeCount.text = "$likeCounts लाइक"
            }
            itemView.etCommentP.setOnClickListener {
               startCommentActivity(postDatas.post_id)
            }
            itemView.llCommentP.setOnClickListener {
               startCommentActivity(postDatas.post_id)
            }
            var share = true
            itemView.llShareP.setOnClickListener {
                sharePost(postDatas.discription,postDatas.post_id)
                 var shareCounts : Int = postDatas.shares.toInt()
                likes(postDatas.post_id,"share")
                if(share) {
                    shareCounts++
                    shareCount.text = "$shareCounts शेयर "
                    share = false
                }
            }
            itemView.cParent.setOnClickListener {
                selectPoll()
            }


        }
    }
    private inner class BGViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val bg : TextView = itemView.findViewById(R.id.tvBG)
        val userImage: ImageView = itemView.findViewById(R.id.cvUserImage)
        val userName: TextView = itemView.findViewById(R.id.tvUserName)
        val location: TextView = itemView.findViewById(R.id.tvLocation)
        val contentc: TextView = itemView.findViewById(R.id.tvContent)
        val timec: TextView = itemView.findViewById(R.id.tvTime)
        val likeCount: TextView = itemView.findViewById(R.id.tvLike)
        val commentCount: TextView = itemView.findViewById(R.id.tvComment)
        val shareCount: TextView = itemView.findViewById(R.id.tvShare)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val postDatas = list[position]
            Picasso.get().load("https://" + postDatas.user_image).placeholder(R.drawable.profile)
                .error(R.drawable.profile).into(userImage)
            Picasso.get().load("https://" + postDatas.user_image).placeholder(R.drawable.profile)
                .error(R.drawable.profile).into(itemView.cvUserImagec)
            val tagList = ArrayList<String>()

            try {
                val imgobj = JSONArray(postDatas.post_tags)
                for (i in 0..5) {
                    val imgArr = imgobj.getJSONObject(i)
                    val fimg = imgArr.optString("tag")
                    tagList.add(fimg)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            userName.text = postDatas.user_name
            location.text = list[position].location
            timec.text = list[position].date
            contentc.text = tagList.toString()
            likeCount.text = "${list[position].likes}" + " लाइक"
            commentCount.text = list[position].comments + " कमेंट्स "
            shareCount.text = list[position].shares + " शेयर"
            var likeCounts : Int = list[position].likes
            var isLike = postDatas.isLiked
            bg.text = postDatas.discription
            if (postDatas.color == "FFFFFFFF") {
                bg.setTextColor(Color.BLACK)
            }else{
               bg.setTextColor(Color.WHITE)
            }
                bg.setBackgroundColor(Color.parseColor("#" + postDatas.color))

            if (postDatas.isLiked == 1){
                itemView.ivLikeB.setImageResource(R.drawable.ic_round_thumb_up_24)
            }else{
                itemView.ivLikeB.setImageResource(R.drawable.ic_like_24)
            }

            itemView.llLikeB.setOnClickListener {
                if (isLike == 1){
                    itemView.ivLikeB.setImageResource(R.drawable.ic_like_24)
                    isLike = 0
                    likeCounts--
                    likes(postDatas.post_id,"like")
                }else{
                    itemView.ivLikeB.setImageResource(R.drawable.ic_round_thumb_up_24)
                    isLike = 1
                    likeCounts++
                    likes(postDatas.post_id,"like")
                }
                likeCount.text = "$likeCounts लाइक"
            }
            itemView.etComment.setOnClickListener {
              startCommentActivity(postDatas.post_id)
            }
            itemView.llCommentB.setOnClickListener {
              startCommentActivity(postDatas.post_id)
            }
            var share = true
            itemView.llShareB.setOnClickListener {
                sharePost(postDatas.discription,postDatas.post_id)
                var shareCounts : Int = postDatas.shares.toInt()
                likes(postDatas.post_id,"share")
                if(share) {
                    shareCounts++
                    shareCount.text = "$shareCounts शेयर "
                    share = false
                }
            }
        }
    }

    private inner class showLoading(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_POST) {
            return postViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_home_design, parent, false)
            )
        } else if (viewType == VIEW_TYPE_POLL){
            return pollViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_home_poll_design, parent, false)
            )
        }else if (viewType == VIEW_TYPE_BG){
            return BGViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_bg_design,parent,false))
        }else{
             return NewsAdapter.showLoading(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading, parent, false)
            )
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (list[position].viewType === VIEW_TYPE_POST) {
            (holder as postViewHolder).bind(position)
        } else if (list[position].viewType == VIEW_TYPE_POLL) {
            (holder as pollViewHolder).bind(position)
        }else if (list[position].viewType == VIEW_TYPE_BG){
            (holder as BGViewHolder).bind(position)
        }else{

        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private fun likes(postID : Int,type:String){
        val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<LikeCommentShareModel> = service.likeShare(session, postID,type)
        try {
            call.enqueue(object : Callback<LikeCommentShareModel>{
                override fun onResponse(
                    call: Call<LikeCommentShareModel>,
                    response: Response<LikeCommentShareModel>
                ) {
                    response.body()!!
                    Log.d("onResLike", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<LikeCommentShareModel>, t: Throwable) {
                    Log.d("onResFailLike", "onResponse: ${t.message}")
                }
            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    private fun startCommentActivity(postID: Int){
        val i = Intent(activity,CommentActivity::class.java)
        i.putExtra("postId",postID.toString())
        activity.startActivity(i)
    }
    private fun sharePost(disc : String,postID: Int){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${disc}\nCheck the post on India's fastest growing app Farmer Express\nhttp://www.farmerexpress.rnsitsolutions.com/api/${postID}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        activity.startActivity(shareIntent)
    }

    private fun selectPoll(){
        val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<PollSelectedModel> = service.pollSelect(session, "277","j34jj")
        try {
            call.enqueue(object : Callback<PollSelectedModel> {
                override fun onResponse(
                    call: Call<PollSelectedModel>,
                    response: Response<PollSelectedModel>
                ) {
                    response.body()!!
                    Log.d("onPollSelectedRes", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<PollSelectedModel>, t: Throwable) {
                    Log.d("onPollSelectedResFail", "onResponse: ${t.message}")
                }
            })
        }catch (e : Exception){

        }

    }

    private fun showKeyboard() {
        val inputMethodManager =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}


