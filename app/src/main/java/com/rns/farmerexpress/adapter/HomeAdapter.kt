package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_home_bg_design.view.*
import kotlinx.android.synthetic.main.recycler_home_design.view.*
import kotlinx.android.synthetic.main.recycler_home_poll_design.view.*
import kotlinx.android.synthetic.main.recycler_home_poll_design.view.cvUserImagec
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import com.rns.farmerexpress.ui.activities.CommentActivity
import com.rns.farmerexpress.model.*


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
        val ivMenuOP: ImageView = itemView.findViewById(R.id.ivMenuOp)
        val llPost: LinearLayout = itemView.findViewById(R.id.llPost)
        val tvMore: TextView = itemView.findViewById(R.id.tvMore)

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
            if(tagList.size == 0){
            contentc.text = list[position].discription
            }else if (tagList.size == 1){
                contentc.text = list[position].discription + "\n"+tagList[0]

            }else if (tagList.size == 2){
                contentc.text = list[position].discription + "\n"+tagList[0] +"," + tagList[1]

            }else if (tagList.size == 3){
                contentc.text = list[position].discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]

            }else if (tagList.size == 4){
                contentc.text = list[position].discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]+"," + tagList[3]

            }else if (tagList.size == 5){
                contentc.text = list[position].discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]+"," + tagList[3]+"," + tagList[4]

            }
            var tvMoreFlag = true
            if (postDatas.discription.length > 600){
                contentc.maxLines = 6
                tvMore.visibility = View.VISIBLE
            }else{
                tvMore.visibility = View.GONE
            }
            tvMore.setOnClickListener {
                if (tvMoreFlag) {
                    contentc.maxLines = 80
                    tvMore.text = "कम करके पढे"
                    tvMoreFlag = false
                } else {
                    tvMore.text = "...और अधिक पढे"
                    tvMoreFlag = true
                    contentc.maxLines = 6
                }
            }
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

            ivMenuOP.setOnClickListener {
                val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
                val  builder  = AlertDialog.Builder(activity)
                builder.setTitle("Farmer Express")
                    .setItems(R.array.list_option, DialogInterface.OnClickListener { dialog, which ->
                       when(which){
                           0 -> {  deletePost(session,list[position].post_id,position,list)

                           }
                           1-> {Toast.makeText(activity,"Hide",Toast.LENGTH_SHORT).show()}
                           2-> {Toast.makeText(activity,"Resport",Toast.LENGTH_SHORT).show()}
                           else -> {}
                       }
                    })
                val alertDialog = builder.create()
                alertDialog.show()
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
        val ivMenuOp: ImageView = itemView.findViewById(R.id.ivMenuOp)
        val llPoll: LinearLayout = itemView.findViewById(R.id.llPoll)
        val tvMore: TextView = itemView.findViewById(R.id.tvMore)


//         tvOptions1 = itemView.findViewById(R.id.tvOption1)
//         tvOptions3 = itemView.findViewById(R.id.tvOption3)
//         tvOptions4 = itemView.findViewById(R.id.tvOption4)
//         tvOptions5 = itemView.findViewById(R.id.tvOption5)

        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables", "ResourceAsColor")
        fun bind(position: Int) {
            val tagList = ArrayList<String>()
            val pollList = ArrayList<String>()
            val pollListValue = ArrayList<Int>()

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
                    val pollVal = pollObj.getJSONObject(i)
                    val values = pollVal.optInt("value")
//                    polls[4].toString()
                    pollList.add(polls)
                    pollListValue.add(values)
                }
            }catch (e : Exception){
                e.printStackTrace()
            }
            try {
                itemView.tvOption1.text = pollList[0]
                itemView.tvOption2.text = pollList[1]
//                tvOption3.text = pollList[2]
//                tvOption4.text = pollList[3]
                if (pollList.size>=3){
                    itemView.cParent3.visibility = View.VISIBLE
                    itemView.tvOption3.text = pollList[2]
                }else{
                    itemView.cParent3.visibility = View.GONE
                }
                if (pollList.size>=4){
                    itemView.cParent4.visibility = View.VISIBLE
                    itemView.tvOption4.text = pollList[3]
                }else{
                    itemView.cParent4.visibility = View.GONE
                }
                if (pollList.size>=5){
                    itemView.cParent5.visibility = View.VISIBLE
                    itemView.tvOption5.text = pollList[4]
                }else{
                    itemView.cParent5.visibility = View.GONE
                }

            }catch (e:Exception){
                e.printStackTrace()
            }
            userName.text = postDatas.user_name
            location.text = list[position].location
            if(tagList.size == 0){
                contentc.text = list[position].discription
            }else if (tagList.size == 1){
                contentc.text = list[position].discription + "\n"+tagList[0]

            }else if (tagList.size == 2){
                contentc.text = list[position].discription + "\n"+tagList[0] +"," + tagList[1]

            }else if (tagList.size == 3){
                contentc.text = list[position].discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]

            }else if (tagList.size == 4){
                contentc.text = list[position].discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]+"," + tagList[3]

            }else if (tagList.size == 5){
                contentc.text = list[position].discription + "\n"+tagList[0] +"," + tagList[1]+"," + tagList[2]+"," + tagList[3]+"," + tagList[4]

            }

            var tvMoreFlag = true
            if (postDatas.discription.length > 600){
                contentc.maxLines = 6
                tvMore.visibility = View.VISIBLE
            }else{
                tvMore.visibility = View.GONE
            }
            tvMore.setOnClickListener {
                if (tvMoreFlag){
                    contentc.maxLines = 80
                    tvMore.text = "कम करके पढे"
                    tvMoreFlag = false
                }else{
                    tvMore.text = "...और अधिक पढे"
                    tvMoreFlag = true
                    contentc.maxLines = 6
                }
            }
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
            hideSeekbar(itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5)
            hideTvPercent(itemView.tvPercent1,itemView.tvPercent2,itemView.tvPercent3,itemView.tvPercent4,itemView.tvPercent5)
            showClParent(itemView.cParent,itemView.cParent2,itemView.cParent3,itemView.cParent4,itemView.cParent5)

            itemView.tvOption1.setOnClickListener {
                changeSeekBarColor(itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5)
                selectPoll(postDatas.post_id.toString(),pollList[0],itemView.tvPercent1,itemView.tvPercent2,itemView.tvPercent3,itemView.tvPercent4,itemView.tvPercent5,
                    itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5,itemView.cParent,itemView.cParent2,itemView.cParent3,
                    itemView.cParent4,itemView.cParent5)
            }
            itemView.tvOption2.setOnClickListener {
                changeSeekBarColor(itemView.seekbar1,itemView.seekbar2,itemView.seekbar4,itemView.seekbar5)
                selectPoll(postDatas.post_id.toString(),pollList[1],itemView.tvPercent1,itemView.tvPercent2,itemView.tvPercent3,itemView.tvPercent4,itemView.tvPercent5,
                    itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5,itemView.cParent,itemView.cParent2,itemView.cParent3,
                    itemView.cParent4,itemView.cParent5)
            }
            itemView.tvOption3.setOnClickListener {
                changeSeekBarColor(itemView.seekbar1,itemView.seekbar2,itemView.seekbar4,itemView.seekbar5)
                selectPoll(postDatas.post_id.toString(),pollList[2],itemView.tvPercent1,itemView.tvPercent2,itemView.tvPercent3,itemView.tvPercent4,itemView.tvPercent5,
                    itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5,itemView.cParent,itemView.cParent2,itemView.cParent3,
                    itemView.cParent4,itemView.cParent5)
            }
            itemView.tvOption4.setOnClickListener {
                changeSeekBarColor(itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar5)
                selectPoll(postDatas.post_id.toString(),pollList[3],itemView.tvPercent1,itemView.tvPercent2,itemView.tvPercent3,itemView.tvPercent4,itemView.tvPercent5,
                    itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5,itemView.cParent,itemView.cParent2,itemView.cParent3,
                    itemView.cParent4,itemView.cParent5)
            }
            itemView.tvOption5.setOnClickListener {
                changeSeekBarColor(itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4)
                selectPoll(postDatas.post_id.toString(),pollList[4],itemView.tvPercent1,itemView.tvPercent2,itemView.tvPercent3,itemView.tvPercent4,itemView.tvPercent5,
                    itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5,itemView.cParent,itemView.cParent2,itemView.cParent3,
                    itemView.cParent4,itemView.cParent5)
            }

            if (postDatas.isPollSelected == "1"){
                try {
                    showSeekbar(itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5)
                    hideClParent(itemView.cParent,itemView.cParent2,itemView.cParent3,itemView.cParent4,itemView.cParent5)
                    showTvPercent(itemView.tvPercent1,itemView.tvPercent2,itemView.tvPercent3,itemView.tvPercent4,itemView.tvPercent5)
                    itemView.tvPercent1?.text = pollListValue[0].toString()+"%"
                    itemView.seekbar1?.progress = pollListValue[0]
                    itemView.seekbar1?.max = 100
                    itemView.tvPercent2?.text = pollListValue[1].toString()+"%"
                    itemView.seekbar2?.progress = pollListValue[1]
                    itemView.seekbar2?.max = 100
                    if (pollListValue.size>=3){
                        itemView.tvPercent3?.text = pollListValue[2].toString()+"%"
                        itemView.seekbar3?.progress = pollListValue[2]
                        itemView.seekbar3?.max = 100
                    }
                    if (pollListValue.size>=4){
                       itemView.tvPercent4?.text = pollListValue[3].toString()+"%"
                        itemView.seekbar4?.progress = pollListValue[3]
                        itemView.seekbar4?.max = 100
                    }
                    if (pollListValue.size>=5){
                        itemView.tvPercent5?.text = pollListValue[4].toString()+"%"
                        itemView.seekbar5?.progress = pollListValue[4]
                        itemView.seekbar5?.max = 100
                    }

                    if(postDatas.selectedPoll == pollList[0]){
                        changeSeekBarColor(itemView.seekbar2,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5)
                    }else if(postDatas.selectedPoll == pollList[1]){
                        changeSeekBarColor(itemView.seekbar1,itemView.seekbar3,itemView.seekbar4,itemView.seekbar5)
                    }else if(postDatas.selectedPoll == pollList[2]){
                        changeSeekBarColor(itemView.seekbar1,itemView.seekbar2,itemView.seekbar4,itemView.seekbar5)
                    }else if(postDatas.selectedPoll == pollList[3]){
                        changeSeekBarColor(itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar5)
                    }else if(postDatas.selectedPoll == pollList[4]){
                        changeSeekBarColor(itemView.seekbar1,itemView.seekbar2,itemView.seekbar3,itemView.seekbar4)
                    }

                }catch (e : Exception){

                }
            }

//            itemView.seekbar1.setOnTouchListener(object : View.OnTouchListener{
//                @SuppressLint("ClickableViewAccessibility")
//                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                    selectPoll(postDatas.post_id.toString(),pollList[0])
//                    Toast.makeText(activity,pollList[0].toString(),Toast.LENGTH_SHORT).show()
//                    return true
//                }
//            })

            ivMenuOp.setOnClickListener {
                val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
                val  builder  = AlertDialog.Builder(activity)
                builder.setTitle("Farmer Express")
                    .setItems(R.array.list_option, DialogInterface.OnClickListener { dialog, which ->
                        when(which){
                            0 -> {
                                deletePost(session,list[position].post_id,position,list)

                            }
                            1-> {Toast.makeText(activity,"Hide",Toast.LENGTH_SHORT).show()}
                            2-> {Toast.makeText(activity,"Resport",Toast.LENGTH_SHORT).show()}
                            else -> {}
                        }
                    })
                val alertDialog = builder.create()
                alertDialog.show()
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
        val ivMenuOP: ImageView = itemView.findViewById(R.id.ivMenuOp)
        val tvMore: TextView = itemView.findViewById(R.id.tvMore)
        val llBg: LinearLayout = itemView.findViewById(R.id.llBg)

//        val pb: ProgressBar = itemView.findViewById(R.id.pbbg)
//        val cvParent: CardView = itemView.findViewById(R.id.cvParentbg)

        @SuppressLint("SetTextI18n", "ResourceAsColor")
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
            if(tagList.size == 0){
            }else if (tagList.size == 1){
                contentc.text = tagList[0]

            }else if (tagList.size == 2){
                contentc.text = tagList[0] +", " + tagList[1]

            }else if (tagList.size == 3){
                contentc.text = tagList[0] +", " + tagList[1]+", " + tagList[2]

            }else if (tagList.size == 4){
                contentc.text = tagList[0] +", " + tagList[1]+", " + tagList[2]+", " + tagList[3]

            }else if (tagList.size == 5){
                contentc.text = tagList[0] +", " + tagList[1]+", " + tagList[2]+", " + tagList[3]+", " + tagList[4]

            }

            likeCount.text = "${list[position].likes}" + " लाइक"
            commentCount.text = list[position].comments + " कमेंट्स "
            shareCount.text = list[position].shares + " शेयर"
            var likeCounts : Int = list[position].likes
            var isLike = postDatas.isLiked
            var tvMoreFlag = true
            bg.text = postDatas.discription
            if (postDatas.discription.length > 600){
                bg.maxLines = 6
                tvMore.visibility = View.VISIBLE
            }else{
                tvMore.visibility = View.GONE
            }
            tvMore.setOnClickListener {
                if (tvMoreFlag){
                    bg.maxLines = 80
                    tvMore.text = "कम करके पढे"
                    tvMoreFlag = false
                }else{
                    tvMore.text = "...और अधिक पढे"
                    tvMoreFlag = true
                    bg.maxLines = 6
                }
            }
            if (postDatas.color == "FFFFFFFF") {
                bg.setTextColor(Color.BLACK)
                tvMore.setTextColor(R.color.colorPrimaryDark)
            }else{
               bg.setTextColor(Color.WHITE)
                tvMore.setTextColor(Color.WHITE)

            }
                bg.setBackgroundColor(Color.parseColor("#" + postDatas.color))
                llBg.setBackgroundColor(Color.parseColor("#" + postDatas.color))

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

            ivMenuOP.setOnClickListener {
                val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
                val  builder  = AlertDialog.Builder(activity)
                builder.setTitle("Farmer Express")
                    .setItems(R.array.list_option, DialogInterface.OnClickListener { dialog, which ->
                        when(which){
                            0 -> {
                                deletePost(session,list[position].post_id,position,list)


                            }
                            1-> {Toast.makeText(activity,"Hide",Toast.LENGTH_SHORT).show()}
                            2-> {Toast.makeText(activity,"Resport",Toast.LENGTH_SHORT).show()}
                            else -> {}
                        }
                    })
                val alertDialog = builder.create()
                alertDialog.show()
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
        PreferenceConnector.writeString(activity,PreferenceConnector.ONBACKSINGLEMAIN,"False")
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

    private fun selectPoll(postID: String,pollDes:String,tvOptions1:TextView?,tvOptions2:TextView?,tvOptions3:TextView?,tvOptions4:TextView?,tvOptions5:TextView?,seekbar1: SeekBar?,
                           seekbar2: SeekBar?,seekbar3: SeekBar?,seekbar4: SeekBar?,seekbar5: SeekBar?,clParent:ConstraintLayout?,clParent2:ConstraintLayout?,
                           clParent3:ConstraintLayout?,clParent4:ConstraintLayout?,clParent5:ConstraintLayout?){
        hideClParent(clParent,clParent2,clParent3,clParent4,clParent5)
        val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
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



    @SuppressLint("ResourceAsColor")
    private fun changeSeekBarColor(seekbar2: SeekBar?, seekbar3: SeekBar?, seekbar4: SeekBar?, seekbar5: SeekBar?) {
        seekbar2?.getProgressDrawable()?.setColorFilter(R.color.pollColor, PorterDuff.Mode.DST_IN)
        seekbar3?.getProgressDrawable()?.setColorFilter(R.color.pollColor, PorterDuff.Mode.DST_IN)
        seekbar4?.getProgressDrawable()?.setColorFilter(R.color.pollColor, PorterDuff.Mode.DST_IN)
        seekbar5?.getProgressDrawable()?.setColorFilter(R.color.pollColor, PorterDuff.Mode.DST_IN)
    }
    private fun hideSeekbar(seekbar1: SeekBar?,seekbar2: SeekBar?,seekbar3: SeekBar?,seekbar4: SeekBar?,seekbar5: SeekBar?) {
        seekbar1?.visibility = View.INVISIBLE
        seekbar2?.visibility = View.INVISIBLE
        seekbar3?.visibility = View.INVISIBLE
        seekbar4?.visibility = View.INVISIBLE
        seekbar5?.visibility = View.INVISIBLE
    }
    private fun showSeekbar(seekbar1: SeekBar?,seekbar2: SeekBar?,seekbar3: SeekBar?,seekbar4: SeekBar?,seekbar5: SeekBar?) {
        seekbar1?.visibility = View.VISIBLE
        seekbar2?.visibility = View.VISIBLE
        seekbar3?.visibility = View.VISIBLE
        seekbar4?.visibility = View.VISIBLE
        seekbar5?.visibility = View.VISIBLE
    }
    private fun hideTvPercent(tvPercent1: TextView?,tvPercent2: TextView?,tvPercent3: TextView?,tvPercent4: TextView?,tvPercent5: TextView?) {
        tvPercent1?.visibility = View.INVISIBLE
        tvPercent2?.visibility = View.INVISIBLE
        tvPercent3?.visibility = View.INVISIBLE
        tvPercent4?.visibility = View.INVISIBLE
        tvPercent5?.visibility = View.INVISIBLE
    }
    private fun showTvPercent(tvPercent1: TextView?,tvPercent2: TextView?,tvPercent3: TextView?,tvPercent4: TextView?,tvPercent5: TextView?) {
        tvPercent1?.visibility = View.VISIBLE
        tvPercent2?.visibility = View.VISIBLE
        tvPercent3?.visibility = View.VISIBLE
        tvPercent4?.visibility = View.VISIBLE
        tvPercent5?.visibility = View.VISIBLE
    }
    private fun hideClParent(clParent:ConstraintLayout?,clParent2:ConstraintLayout?,clParent3:ConstraintLayout?,clParent4:ConstraintLayout?,clParent5:ConstraintLayout?) {
        clParent?.background = null
        clParent2?.background = null
        clParent3?.background = null
        clParent4?.background = null
        clParent5?.background = null

    }
    private fun showClParent(clParent: ConstraintLayout?,clParent2: ConstraintLayout?,clParent3: ConstraintLayout?,clParent4: ConstraintLayout?,clParent5: ConstraintLayout?) {
        clParent?.setBackgroundResource(R.drawable.rzp_border)
        clParent2?.setBackgroundResource(R.drawable.rzp_border)
        clParent3?.setBackgroundResource(R.drawable.rzp_border)
        clParent4?.setBackgroundResource(R.drawable.rzp_border)
        clParent5?.setBackgroundResource(R.drawable.rzp_border)
    }

    private fun deletePost(session: String,postID: Int,position: Int,list: ArrayList<PostDatas>){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<PostDelete> = service.deletePost(session, postID.toString())
        try {
            call.enqueue(object  : Callback<PostDelete>{
                override fun onResponse(call: Call<PostDelete>, response: Response<PostDelete>) {
                    val responseBody = response.body()!!
                    if (responseBody.message == "Post deleted successfuly"){
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

    private fun showKeyboard() {
        val inputMethodManager =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }


}


