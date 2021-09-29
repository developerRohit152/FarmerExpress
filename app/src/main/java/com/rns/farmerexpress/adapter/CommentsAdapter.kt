package com.rns.farmerexpress.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.datatransport.runtime.backends.BackendResponse.ok
import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.model.Comment
import com.rns.farmerexpress.model.Comments
import com.rns.farmerexpress.ui.activities.CommentActivity
import com.rns.farmerexpress.ui.activities.NewsDescActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_news_desc.view.*
import kotlinx.android.synthetic.main.loading.view.*
import kotlinx.android.synthetic.main.recycler_comments_item.view.*
import kotlinx.android.synthetic.main.recycler_newa_item.view.*
import kotlinx.android.synthetic.main.recycler_newa_item.view.ivImage
import kotlinx.android.synthetic.main.recycler_newa_item.view.tvAuthor
import kotlinx.android.synthetic.main.recycler_newa_item.view.tvHeading
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class CommentsAdapter(private val activity: Activity, val list: ArrayList<Comments>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {

        }
    }
    class showLoading(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.recycler_newa_item, parent, false)
//        return CommentsViewHolder(view)

        if (viewType == NewsAdapter.VIEW_TYPE_ONE) {
            return CommentsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_comments_item, parent, false)
            )
        }
        return showLoading(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.loading, parent, false)
        )


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
//
        if (list[position].viewType === VIEW_TYPE_ONE) {
            Picasso.get().load("https://${list[position].user_image}")
                .into(holder.itemView.ivImage)
//            holder.itemView.tvCat.text = list[position].category
            holder.itemView.tvHeading.text = list[position].user_name
            holder.itemView.tvAuthor.text = list[position].comment
            val session = PreferenceConnector.readString(activity,PreferenceConnector.profilestatus,"")
            holder.itemView.clParentComment.setOnLongClickListener {
                if (list[position].user_name == "You"){
                   val  builder  = AlertDialog.Builder(activity)
                    builder.setMessage("क्या आप कमेन्ट हटाना चाहते हैं?")
                    builder.setCancelable(false)
                        .setPositiveButton("हाँ",object : DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                getComments(session,list[position].postId,"comment_delete",list[position].comment)
                                list.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position,list.size)
                                dialog?.dismiss()
                            }
                        }).setNegativeButton("नहीं",object : DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }
                        })
                    val alertDialog = builder.create()
                    alertDialog.setTitle("Farmer Express")
                    alertDialog.show()
                    return@setOnLongClickListener  true
                }
                    return@setOnLongClickListener true
            }

//            holder.itemView.llNews.setOnClickListener {
//                val postID = list[position].post_id
//                val i = Intent(activity, NewsDescActivity::class.java)
//                i.putExtra("id", postID)
//                activity.startActivity(i)
//            }
            } else {

            }
        }

        override fun getItemCount(): Int = list.size
        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

    private fun getComments(session : String,postID:String?,type : String,commentDes : String) {
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: retrofit2.Call<Comment> = service.comment(session, postID, type, commentDes)
        try {
            call.enqueue(object : retrofit2.Callback<Comment> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                    response.body()!!
                    Log.d("OnCommentRes", "onResponse: ${response.body()}")

                }

                override fun onFailure(call: Call<Comment>, t: Throwable) {
                    Log.d("OnCommentResFail", "onResponse: ${t.message}")
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




}