package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.HomeAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_activiyt.view.*
import kotlinx.android.synthetic.main.activity_single_post.*
import kotlinx.android.synthetic.main.fragment_homes.*
import kotlinx.android.synthetic.main.recycler_home_bg_design.view.*
import kotlinx.android.synthetic.main.recycler_home_design.view.*
import kotlinx.android.synthetic.main.recycler_home_poll_design.view.*
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SinglePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_post)
        pbSinglePost.visibility = View.VISIBLE
        llSinglePost.visibility = View.GONE
        val session = PreferenceConnector.readString(this,PreferenceConnector.profilestatus,"")
        val action: String? = intent?.action
        val data: Uri? = intent?.data
//        http://www.farmerexpress.rnsitsolutions.com/api/401
        val postID = data.toString().substring(data.toString().lastIndexOf("/") + 1)
        if (data != null) {
         PreferenceConnector.writeString(this,PreferenceConnector.POSTIDSINGLE,postID)
        }
        getHomeData(session,PreferenceConnector.readString(this,PreferenceConnector.POSTIDSINGLE,""))
//        Toast.makeText(this,postID,Toast.LENGTH_LONG).show()

    }



    private fun getHomeData(session:String,postID: String){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<GetSinglePost> = service.getSinglePost(session,postID)
        try {
            call.enqueue(object : retrofit2.Callback<GetSinglePost>{
                @SuppressLint("WrongConstant", "SetTextI18n")
                override fun onResponse(call: Call<GetSinglePost>, response: Response<GetSinglePost>) {
                    response.body()!!
                    val responseBody = response.body()
                        Log.d("OnSinglePostRes", "onResponse: $responseBody")
                    for (data in responseBody!!.post){
                        if (data.type == "image") {

                            Picasso.get().load("https://" + data.user_image).placeholder(R.drawable.profile)
                                .error(R.drawable.profile).into(cvUserImage)
                            Picasso.get().load("https://" + data.user_image).placeholder(R.drawable.profile)
                                .error(R.drawable.profile).into(cvUserImagec)
                            val imageList = ArrayList<SlideModel>()
                            val tagList = ArrayList<String>()
                            try {
                                val imgobj = JSONArray(data.post_images)
                                val imgArr = imgobj.getJSONObject(0)
                                val fimg = imgArr.optString("image")
                                imageList.add(SlideModel("https://" + fimg, ScaleTypes.CENTER_CROP))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            try {
                                val tagObj = JSONArray(data.post_tags)
                                for (i in 0..5) {
                                    val tagArr = tagObj.getJSONObject(i)
                                    val tags = tagArr.optString("tag")
                                    tagList.add(tags)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            tvUserName.text = data.user_name
                            tvLocation.text = data.location
                            if(tagList.size == 0){
                                tvContent.text = data.discription
                            }else if (tagList.size == 1){
                                tvContent.text = data.discription + "\n"+tagList[0]

                            }else if (tagList.size == 2){
                                tvContent.text = data.discription + "\n"+tagList[0] +"," + tagList[1]

                            }else if (tagList.size == 3){
                                tvContent.text = data.discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]

                            }else if (tagList.size == 4){
                                tvContent.text = data.discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]+"," + tagList[3]

                            }else if (tagList.size == 5){
                                tvContent.text = data.discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]+"," + tagList[3]+"," + tagList[4]

                            }
                            tvTime.text = data.date
                            tvLike.text = "${data.likes}" + " लाइक"
                            tvComment.text = data.comments + " कमेंट्स "
                            tvShare.text = data.shares + " शेयर"
                            image_slider.setImageList(imageList)
                            var likeCounts : Int = data.likes
                            var isLike = data.isLiked
                            if (data.isLiked == 1){
                                ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
                            }else{
                                ivLike.setImageResource(R.drawable.ic_like_24)
                            }
                            llLike.setOnClickListener {
                                if (isLike == 1){
                                    ivLike.setImageResource(R.drawable.ic_like_24)
                                    isLike = 0
                                    likeCounts--
                                    likes(data.post_id,"like")
                                }else{
                                    ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
                                    isLike = 1
                                    likeCounts++
                                    likes(data.post_id,"like")
                                }
                                tvLike.text = "$likeCounts लाइक"
                            }
                            etCommentI.setOnClickListener {
                                startCommentActivity(data.post_id)
                            }
                            llComment.setOnClickListener {
                                startCommentActivity(data.post_id)
                            }
                            var share = true
                            var shareCounts : Int = data.shares.toInt()
                            llShare.setOnClickListener {
                                sharePost(data.discription,data.post_id)
                                likes(data.post_id,"share")
                                if(share) {
                                    shareCounts++
                                    tvShare.text = "$shareCounts शेयर "
                                    share = false
                                }
                            }

                            image_slider.visibility = View.VISIBLE
                            tvBG.visibility = View.GONE
                            llPoll.visibility = View.GONE
                            pbSinglePost.visibility = View.GONE
                            llSinglePost.visibility = View.VISIBLE
                        }
                        else if (data.type == "bg"){

                            Picasso.get().load("https://" + data.user_image).placeholder(R.drawable.profile)
                                .error(R.drawable.profile).into(cvUserImage)
                            Picasso.get().load("https://" + data.user_image).placeholder(R.drawable.profile)
                                .error(R.drawable.profile).into(cvUserImagec)
                            val tagList = ArrayList<String>()

                            try {
                                val imgobj = JSONArray(data.post_tags)
                                for (i in 0..5) {
                                    val imgArr = imgobj.getJSONObject(i)
                                    val fimg = imgArr.optString("tag")
                                    tagList.add(fimg)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            tvUserName.text = data.user_name
                            tvLocation.text = data.location
                            tvTime.text = data.date
                            if(tagList.size == 0){
                            }else if (tagList.size == 1){
                                tvContent.text = tagList[0]

                            }else if (tagList.size == 2){
                                tvContent.text = tagList[0] +", " + tagList[1]

                            }else if (tagList.size == 3){
                                tvContent.text = tagList[0] +", " + tagList[1]+", " + tagList[2]

                            }else if (tagList.size == 4){
                                tvContent.text = tagList[0] +", " + tagList[1]+", " + tagList[2]+", " + tagList[3]

                            }else if (tagList.size == 5){
                                tvContent.text = tagList[0] +", " + tagList[1]+", " + tagList[2]+", " + tagList[3]+", " + tagList[4]

                            }
                            tvLike.text = "${data.likes}" + " लाइक"
                            tvComment.text = data.comments + " कमेंट्स "
                            tvShare.text = data.shares + " शेयर"
                            var likeCounts : Int = data.likes
                            var isLike = data.isLiked
                            tvBG.text = data.discription
                            if (data.color == "FFFFFFFF") {
                                tvBG.setTextColor(Color.BLACK)
                            }else{
                                tvBG.setTextColor(Color.WHITE)
                            }
                            tvBG.setBackgroundColor(Color.parseColor("#" + data.color))

                            if (data.isLiked == 1){
                                ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
                            }else{
                                ivLike.setImageResource(R.drawable.ic_like_24)
                            }

                            llLike.setOnClickListener {
                                if (isLike == 1){
                                    ivLike.setImageResource(R.drawable.ic_like_24)
                                    isLike = 0
                                    likeCounts--
                                    likes(data.post_id,"like")
                                }else{
                                    ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
                                    isLike = 1
                                    likeCounts++
                                    likes(data.post_id,"like")
                                }
                                tvLike.text = "$likeCounts लाइक"
                            }
                            etCommentI.setOnClickListener {
                                startCommentActivity(data.post_id)
                            }
                            llComment.setOnClickListener {
                                startCommentActivity(data.post_id)
                            }
                            var share = true
                            llShare.setOnClickListener {
                                sharePost(data.discription,data.post_id)
                                var shareCounts : Int = data.shares.toInt()
                                likes(data.post_id,"share")
                                if(share) {
                                    shareCounts++
                                    tvShare.text = "$shareCounts शेयर "
                                    share = false
                                }
                            }
                            image_slider.visibility = View.GONE
                            tvBG.visibility = View.VISIBLE
                            llPoll.visibility = View.GONE
                            pbSinglePost.visibility = View.GONE
                            llSinglePost.visibility = View.VISIBLE
                        }else{
                            val tagList = ArrayList<String>()
                            val pollList = ArrayList<String>()
                            val pollListValue = ArrayList<Int>()
                            
                            Picasso.get().load("https://" + data.user_image).placeholder(R.drawable.profile)
                                .error(R.drawable.profile).into(cvUserImage)
                            Picasso.get().load("https://" + data.user_image).placeholder(R.drawable.profile)
                                .error(R.drawable.profile).into(cvUserImagec)
                            try {
                                val tagObj = JSONArray(data.post_tags)
                                for (i in 0..5) {
                                    val tagArr = tagObj.getJSONObject(i)
                                    val tags = tagArr.optString("tag")
                                    tagList.add(tags)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            try {
                                val pollObj = JSONArray(data.poll)
                                for (i in 0..5) {
                                    val pollArr = pollObj.getJSONObject(i)
                                    val polls = pollArr.optString("poll")
                                    val pollVal = pollObj.getJSONObject(i)
                                    val values = pollVal.optInt("value")
//                                    polls[4].toString()
                                    pollList.add(polls)
                                    pollListValue.add(values)
                                }
                            }catch (e : Exception){
                                e.printStackTrace()
                            }
                            try {
                                tvOption1.text = pollList[0]
                                tvOption2.text = pollList[1]
                                if (pollList.size>=3){
                                    cParent3.visibility = View.VISIBLE
                                    tvOption3.text = pollList[2]
                                }else{
                                    cParent3.visibility = View.GONE
                                }
                                if (pollList.size>=4){
                                    cParent4.visibility = View.VISIBLE
                                    tvOption4.text = pollList[3]
                                }else{
                                    cParent4.visibility = View.GONE
                                }
                                if (pollList.size>=5){
                                    cParent5.visibility = View.VISIBLE
                                    tvOption5.text = pollList[4]
                                }else{
                                    cParent5.visibility = View.GONE
                                }

                            }catch (e:Exception){
                                e.printStackTrace()
                            }
                            tvUserName.text = data.user_name
                            tvLocation.text = data.location
                            if(tagList.size == 0){
                                tvContent.text = data.discription
                            }else if (tagList.size == 1){
                                tvContent.text = data.discription + "\n"+tagList[0]

                            }else if (tagList.size == 2){
                                tvContent.text = data.discription + "\n"+tagList[0] +"," + tagList[1]

                            }else if (tagList.size == 3){
                                tvContent.text = data.discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]

                            }else if (tagList.size == 4){
                                tvContent.text = data.discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]+"," + tagList[3]

                            }else if (tagList.size == 5){
                                tvContent.text = data.discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]+"," + tagList[3]+"," + tagList[4]

                            }
                            tvTime.text = data.date
                            tvLike.text = "${data.likes}" + " लाइक"
                            tvComment.text = data.comments + " कमेंट्स "
                            tvShare.text = data.shares + " शेयर"
                            var likeCounts : Int = data.likes
                            var isLike = data.isLiked
                            if (data.isLiked == 1){
                                ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
                            }else{
                                ivLike.setImageResource(R.drawable.ic_like_24)
                            }

                            llLike.setOnClickListener {
                                if (isLike == 1){
                                    ivLike.setImageResource(R.drawable.ic_like_24)
                                    isLike = 0
                                    likeCounts--
                                    likes(data.post_id,"like")
                                }else{
                                    ivLike.setImageResource(R.drawable.ic_round_thumb_up_24)
                                    isLike = 1
                                    likeCounts++
                                    likes(data.post_id,"like")
                                }
                                tvLike.text = "$likeCounts लाइक"
                            }
                            etCommentI.setOnClickListener {
                                startCommentActivity(data.post_id)
                            }
                            llComment.setOnClickListener {
                                startCommentActivity(data.post_id)
                            }
                            var share = true
                            llShare.setOnClickListener {
                                sharePost(data.discription,data.post_id)
                                var shareCounts : Int = data.shares.toInt()
                                likes(data.post_id,"share")
                                if(share) {
                                    shareCounts++
                                    tvShare.text = "$shareCounts शेयर "
                                    share = false
                                }
                            }
                            hideSeekbar(seekbar1,seekbar2,seekbar3,seekbar4,seekbar5)
                            hideTvPercent(tvPercent1,tvPercent2,tvPercent3,tvPercent4,tvPercent5)
                            showClParent(cParent,cParent2,cParent3,cParent4,cParent5)

                            tvOption1.setOnClickListener {
                                changeSeekBarColor(seekbar2,seekbar3,seekbar4,seekbar5)
                                selectPoll(data.post_id.toString(),pollList[0],tvPercent1,tvPercent2,tvPercent3,tvPercent4,tvPercent5,
                                    seekbar1,seekbar2,seekbar3,seekbar4,seekbar5,cParent,cParent2,cParent3,
                                    cParent4,cParent5)
                            }
                            tvOption2.setOnClickListener {
                                changeSeekBarColor(seekbar1,seekbar2,seekbar4,seekbar5)
                                selectPoll(data.post_id.toString(),pollList[1],tvPercent1,tvPercent2,tvPercent3,tvPercent4,tvPercent5,
                                    seekbar1,seekbar2,seekbar3,seekbar4,seekbar5,cParent,cParent2,cParent3,
                                    cParent4,cParent5)
                            }
                            tvOption3.setOnClickListener {
                                changeSeekBarColor(seekbar1,seekbar2,seekbar4,seekbar5)
                                selectPoll(data.post_id.toString(),pollList[2],tvPercent1,tvPercent2,tvPercent3,tvPercent4,tvPercent5,
                                    seekbar1,seekbar2,seekbar3,seekbar4,seekbar5,cParent,cParent2,cParent3,
                                    cParent4,cParent5)
                            }
                            tvOption4.setOnClickListener {
                                changeSeekBarColor(seekbar1,seekbar2,seekbar3,seekbar5)
                                selectPoll(data.post_id.toString(),pollList[3],tvPercent1,tvPercent2,tvPercent3,tvPercent4,tvPercent5,
                                    seekbar1,seekbar2,seekbar3,seekbar4,seekbar5,cParent,cParent2,cParent3,
                                    cParent4,cParent5)
                            }
                            tvOption5.setOnClickListener {
                                changeSeekBarColor(seekbar1,seekbar2,seekbar3,seekbar4)
                                selectPoll(data.post_id.toString(),pollList[4],tvPercent1,tvPercent2,tvPercent3,tvPercent4,tvPercent5,
                                    seekbar1,seekbar2,seekbar3,seekbar4,seekbar5,cParent,cParent2,cParent3,
                                    cParent4,cParent5)
                            }

                            if (data.isPollSelected == "1"){
                                try {
                                    showSeekbar(seekbar1,seekbar2,seekbar3,seekbar4,seekbar5)
                                    hideClParent(cParent,cParent2,cParent3,cParent4,cParent5)
                                    showTvPercent(tvPercent1,tvPercent2,tvPercent3,tvPercent4,tvPercent5)
                                    tvPercent1?.text = pollListValue[0].toString()+"%"
                                    seekbar1?.progress = pollListValue[0]
                                    seekbar1?.max = 100
                                    tvPercent2?.text = pollListValue[1].toString()+"%"
                                    seekbar2?.progress = pollListValue[1]
                                    seekbar2?.max = 100
                                    if (pollListValue.size>=3){
                                        tvPercent3?.text = pollListValue[2].toString()+"%"
                                        seekbar3?.progress = pollListValue[2]
                                        seekbar3?.max = 100
                                    }
                                    if (pollListValue.size>=4){
                                        tvPercent4?.text = pollListValue[3].toString()+"%"
                                        seekbar4?.progress = pollListValue[3]
                                        seekbar4?.max = 100
                                    }
                                    if (pollListValue.size>=5){
                                        tvPercent5?.text = pollListValue[4].toString()+"%"
                                        seekbar5?.progress = pollListValue[4]
                                        seekbar5?.max = 100
                                    }

                                    if(data.selectedPoll == pollList[0]){
                                        changeSeekBarColor(seekbar2,seekbar3,seekbar4,seekbar5)
                                    }else if(data.selectedPoll == pollList[1]){
                                        changeSeekBarColor(seekbar1,seekbar3,seekbar4,seekbar5)
                                    }else if(data.selectedPoll == pollList[2]){
                                        changeSeekBarColor(seekbar1,seekbar2,seekbar4,seekbar5)
                                    }else if(data.selectedPoll == pollList[3]){
                                        changeSeekBarColor(seekbar1,seekbar2,seekbar3,seekbar5)
                                    }else if(data.selectedPoll == pollList[4]){
                                        changeSeekBarColor(seekbar1,seekbar2,seekbar3,seekbar4)
                                    }

                                }catch (e : Exception){

                                }
                            }
                            image_slider.visibility = View.GONE
                            tvBG.visibility = View.GONE
                            llPoll.visibility = View.VISIBLE
                            pbSinglePost.visibility = View.GONE
                            llSinglePost.visibility = View.VISIBLE
                            
                        }
                    }

                }
                override fun onFailure(call: Call<GetSinglePost>, t: Throwable) {
                    Log.d("OnSinglePostResFail", "onResponse: ${t.message}")
                }

            })
        }catch (e: Exception){
            e.printStackTrace()
        }
    }





    @SuppressLint("ResourceAsColor")
    private fun changeSeekBarColor(seekbar2: SeekBar?, seekbar3: SeekBar?, seekbar4: SeekBar?, seekbar5: SeekBar?) {
        seekbar2?.getProgressDrawable()?.setColorFilter(R.color.pollColor, PorterDuff.Mode.DST_IN)
        seekbar3?.getProgressDrawable()?.setColorFilter(R.color.pollColor, PorterDuff.Mode.DST_IN)
        seekbar4?.getProgressDrawable()?.setColorFilter(R.color.pollColor, PorterDuff.Mode.DST_IN)
        seekbar5?.getProgressDrawable()?.setColorFilter(R.color.pollColor, PorterDuff.Mode.DST_IN)
    }
    private fun hideSeekbar(seekbar1: SeekBar?, seekbar2: SeekBar?, seekbar3: SeekBar?, seekbar4: SeekBar?, seekbar5: SeekBar?) {
        seekbar1?.visibility = View.INVISIBLE
        seekbar2?.visibility = View.INVISIBLE
        seekbar3?.visibility = View.INVISIBLE
        seekbar4?.visibility = View.INVISIBLE
        seekbar5?.visibility = View.INVISIBLE
    }
    private fun showSeekbar(seekbar1: SeekBar?, seekbar2: SeekBar?, seekbar3: SeekBar?, seekbar4: SeekBar?, seekbar5: SeekBar?) {
        seekbar1?.visibility = View.VISIBLE
        seekbar2?.visibility = View.VISIBLE
        seekbar3?.visibility = View.VISIBLE
        seekbar4?.visibility = View.VISIBLE
        seekbar5?.visibility = View.VISIBLE
    }
    private fun hideTvPercent(tvPercent1: TextView?, tvPercent2: TextView?, tvPercent3: TextView?, tvPercent4: TextView?, tvPercent5: TextView?) {
        tvPercent1?.visibility = View.INVISIBLE
        tvPercent2?.visibility = View.INVISIBLE
        tvPercent3?.visibility = View.INVISIBLE
        tvPercent4?.visibility = View.INVISIBLE
        tvPercent5?.visibility = View.INVISIBLE
    }
    private fun showTvPercent(tvPercent1: TextView?, tvPercent2: TextView?, tvPercent3: TextView?, tvPercent4: TextView?, tvPercent5: TextView?) {
        tvPercent1?.visibility = View.VISIBLE
        tvPercent2?.visibility = View.VISIBLE
        tvPercent3?.visibility = View.VISIBLE
        tvPercent4?.visibility = View.VISIBLE
        tvPercent5?.visibility = View.VISIBLE
    }
    private fun hideClParent(clParent: ConstraintLayout?, clParent2: ConstraintLayout?, clParent3: ConstraintLayout?, clParent4: ConstraintLayout?, clParent5: ConstraintLayout?) {
        clParent?.background = null
        clParent2?.background = null
        clParent3?.background = null
        clParent4?.background = null
        clParent5?.background = null

    }
    private fun showClParent(clParent: ConstraintLayout?, clParent2: ConstraintLayout?, clParent3: ConstraintLayout?, clParent4: ConstraintLayout?, clParent5: ConstraintLayout?) {
        clParent?.setBackgroundResource(R.drawable.rzp_border)
        clParent2?.setBackgroundResource(R.drawable.rzp_border)
        clParent3?.setBackgroundResource(R.drawable.rzp_border)
        clParent4?.setBackgroundResource(R.drawable.rzp_border)
        clParent5?.setBackgroundResource(R.drawable.rzp_border)
    }



    private fun selectPoll(postID: String,pollDes:String,tvOptions1:TextView?,tvOptions2:TextView?,tvOptions3:TextView?,tvOptions4:TextView?,tvOptions5:TextView?,seekbar1: SeekBar?,
                           seekbar2: SeekBar?,seekbar3: SeekBar?,seekbar4: SeekBar?,seekbar5: SeekBar?,clParent:ConstraintLayout?,clParent2:ConstraintLayout?,
                           clParent3:ConstraintLayout?,clParent4:ConstraintLayout?,clParent5:ConstraintLayout?){
        hideClParent(clParent,clParent2,clParent3,clParent4,clParent5)
        val session = PreferenceConnector.readString(this,PreferenceConnector.profilestatus,"")
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<PollSelectedModel> = service.pollSelect(session, postID,pollDes)
        try {
            call.enqueue(object : Callback<PollSelectedModel> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<PollSelectedModel>,
                    response: Response<PollSelectedModel>
                ) {
                    response.body()!!
                    val pollListValue = ArrayList<Polls>()
                    if(response.body()!!.poll.isNotEmpty()) {
                        for (data in response.body()!!.poll) {
                            pollListValue.add(Polls(data.poll, data.value))
                        }
                    }

                    try {
                        showSeekbar(seekbar1,seekbar2,seekbar3,seekbar4,seekbar5)
                        showTvPercent(tvOptions1,tvOptions2,tvOptions3,tvOptions4,tvOptions5)
                        tvOptions1?.text = pollListValue[0].value.toString()+"%"
                        seekbar1?.progress = pollListValue[0].value
                        seekbar1?.max = 100
                        tvOptions2?.text = pollListValue[1].value.toString()+"%"
                        seekbar2?.progress = pollListValue[1].value
                        seekbar2?.max = 100
                        if (pollListValue.size>=3){
                            tvOptions3?.text = pollListValue[2].value.toString()+"%"
                            seekbar3?.progress = pollListValue[2].value
                            seekbar3?.max = 100
                        }
                        if (pollListValue.size>=4){
                            tvOptions4?.text = pollListValue[3].value.toString()+"%"
                            seekbar4?.progress = pollListValue[3].value
                            seekbar4?.max = 100
                        }
                        if (pollListValue.size>=5){
                            tvOptions5?.text = pollListValue[4].value.toString()+"%"
                            seekbar5?.progress = pollListValue[4].value
                            seekbar5?.max = 100
                        }

                    }catch (e : Exception){

                    }
                    Log.d("onPollSelectedRes", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<PollSelectedModel>, t: Throwable) {
                    Log.d("onPollSelectedResFail", "onResponse: ${t.message}")
                }
            })
        }catch (e : Exception){

        }

    }

    private fun likes(postID : Int,type:String){
        val session = PreferenceConnector.readString(this,PreferenceConnector.profilestatus,"")
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
        val i = Intent(this,CommentActivity::class.java)
        i.putExtra("postId",postID.toString())
        PreferenceConnector.writeString(this,PreferenceConnector.ONBACKSINGLEMAIN,"True")
        startActivity(i)
    }
    private fun sharePost(disc : String,postID: Int){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${disc}\nCheck the post on India's fastest growing app Farmer Express\nhttp://www.farmerexpress.rnsitsolutions.com/api/${postID}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}