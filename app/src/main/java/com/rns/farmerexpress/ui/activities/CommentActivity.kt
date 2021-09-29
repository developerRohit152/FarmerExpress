package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ContextUtils.getActivity
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.CommentsAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.Comment
import com.rns.farmerexpress.model.Comments
import com.rns.farmerexpress.model.LikeCommentShareModel
import kotlinx.android.synthetic.main.activity_comment.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class CommentActivity : AppCompatActivity() {
    val type = ""
    val list = ArrayList<Comments>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        loading.visibility = View.VISIBLE
        recyclerView2.visibility = View.GONE
        val session = PreferenceConnector.readString(this,PreferenceConnector.profilestatus,"")
        val i = intent.getStringExtra("postId")
        getComments(session,i,"comment_get","")
        ivBack.setOnClickListener{
            finish()
        }
        btnComment.setOnClickListener {
            if (etCommentP.text.isEmpty()){
                etCommentP.error = "कमेन्ट बॉक्स को रिक्त ना छोड़े"
            }else{
                getComments(session,i,"comment",etCommentP.text.toString())
                etCommentP.text.clear()
                list.clear()
            }
        }
        recyclerView2.addOnLayoutChangeListener(View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            recyclerView2.scrollToPosition(
                list.size - 1
            )
        })

    }

    private fun getComments(session : String,postID:String?,type : String,commentDes : String){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<Comment> = service.comment(session, postID,type,commentDes)
        try {
            call.enqueue(object : retrofit2.Callback<Comment>{
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                    response.body()!!
                    if(response.body()!!.comments.isEmpty()){
                        tvNoComments.visibility = View.VISIBLE
                        nestedScrollView.visibility = View.GONE
                        loading.visibility = View.GONE
                    }else {
                        for (comment in response.body()!!.comments) {
                            list.add(
                                Comments(
                                    CommentsAdapter.VIEW_TYPE_ONE,
                                    comment.user_image,
                                    comment.user_name,
                                    comment.comment,
                                    postID
                                )
                            )
                        }
                        val adapter = CommentsAdapter(this@CommentActivity, list)
                        val layoutManager =
                            LinearLayoutManager(this@CommentActivity, LinearLayout.VERTICAL, false)
                        recyclerView2.layoutManager = layoutManager
                        recyclerView2.adapter = adapter
                        loading.visibility = View.GONE
                        recyclerView2.visibility = View.VISIBLE
                        Log.d("OnCommentRes", "onResponse: ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<Comment>, t: Throwable) {
                    Log.d("OnCommentResFail", "onResponse: ${t.message}")
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }

    }






}