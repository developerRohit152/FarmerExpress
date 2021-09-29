package com.rns.farmerexpress.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.ActivityPostActiviytBinding
import com.rns.farmerexpress.model.PostData
import kotlinx.android.synthetic.main.activity_post_activiyt.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import kotlinx.android.synthetic.main.nav_header_main.*

import android.widget.LinearLayout

import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_profile_edit.*


class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostActiviytBinding
    lateinit var editText: EditText
    lateinit var cvWhite: CardView;
    lateinit var cvBrown: CardView;
    lateinit var cvGreen: CardView;
    lateinit var cvOranges: CardView;
    lateinit var cvBlue: CardView
    lateinit var cvReds: CardView;
    lateinit var cvYellows: CardView
    lateinit var llBottomParent: LinearLayout
    lateinit var llCamera: LinearLayout;
    lateinit var llSpeak: LinearLayout;
    lateinit var llVideo: LinearLayout;
    lateinit var llYoutube: LinearLayout;
    lateinit var tvCamera: TextView;
    lateinit var tvSpeak: TextView;
    lateinit var tvVideo: TextView;
    lateinit var tvYoutube: TextView;
    var sImage: String = ""
    lateinit var images: ArrayList<String>
    lateinit var polls: ArrayList<String>
    lateinit var tags: ArrayList<String>
    var types = "bg"
    var flag = false
    var tagFlag = false
    lateinit var editTextTag: EditText
    private var bg = "FFFFFFFF"
    var count = 0
    var allEt = ArrayList<EditText>()


    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostActiviytBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        initializeComponents()
        setEditTextBgColor()
//        keyboardListener()
        editTextTag = EditText(this)
        ivBack.setOnClickListener {
            finish()
        }
        llCamera.setOnClickListener {
            openGalleryForImage()
        }
        images = ArrayList()
        polls = ArrayList()
        tags = ArrayList()
        val session =
            PreferenceConnector.readString(this, PreferenceConnector.profilestatus, "")
        val number = PreferenceConnector.readString(this, PreferenceConnector.USER_PHONE, "")


        btnPost.setOnClickListener {
            if (editText.text.isEmpty()) {
                editText.error = "पोस्ट के बारे मे लिखे"
            } else {
                getETVal()
                if (tagFlag){

                }else {
                    if(tags.isNullOrEmpty()){
                         tags.add("#"+PreferenceConnector.readString(this@PostActivity,PreferenceConnector.stateName,"").toString().trim())
                    }
                    if (flag) {
                        if (etOption1.text.isEmpty() || etOption2.text.isEmpty()) {
                            etOption1.error = "कम से कम दो  ऑप्शन लिखे"
                            etOption2.error = "कम से कम दो  ऑप्शन लिखे"
                        } else {
                            pollEditTextCondition()
                            bg = ""
                            postData(session.toString(),types,editText.text.toString(),images,polls,tags,number,bg)
                            pbPost.visibility = View.VISIBLE
                            clParentPost.visibility = View.GONE
                        }
                    } else if (types == "bg") {
                        images.add("blank")
                         postData(session.toString(),types,editText.text.toString(),images,polls,tags,number,bg)
                        pbPost.visibility = View.VISIBLE
                        clParentPost.visibility = View.GONE

                    } else {
                        bg = ""
                        postData(session.toString(),types,editText.text.toString(),images,polls,tags,number,bg)
                        pbPost.visibility = View.VISIBLE
                        clParentPost.visibility = View.GONE
                    }
//                    Toast.makeText(this, "tag: $tags", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, "bg : $bg", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, "poll : $polls", Toast.LENGTH_SHORT).show()
                    tags.clear()
                }

            }
        }
        btnTogglePost.setOnClickListener {
            if (btnTogglePost.text.equals("पोल करे")) {
                llPoll.visibility = View.VISIBLE
                ivPostImage.visibility = View.GONE
                images.clear()
                flag = true
                types = "poll"
                images.add("blank")
                editText.background = null
                editText.setLines(3)
                editText.setTextColor(Color.BLACK)
                editText.gravity = Gravity.TOP
                editText.setHintTextColor(Color.BLACK)
                constraintLayoutPost.visibility = View.GONE
                btnTogglePost.text = "पोस्ट लिखे"
            } else if ((btnTogglePost.text.equals("पोस्ट लिखे"))) {
                constraintLayoutPost.visibility = View.VISIBLE
                llPoll.visibility = View.GONE
                types = "bg"
                flag = false
                images.clear()
                polls.clear()
                editText.setLines(10)
                editText.gravity = Gravity.CENTER
                llColorParent.visibility = View.VISIBLE
                btnTogglePost.text = "पोल करे"

            }
        }

        btnHash.setOnClickListener {
            if (count == 5) {
                Toast.makeText(
                    this,
                    "ज्यादा से ज्यादा 5 हेश टैग्स लिख सकते हैं",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                btnHashClear.visibility = View.VISIBLE
                addEditText()
            }
        }


        btnHashClear.setOnClickListener {
            if (count == 1) {
                btnHashClear.visibility = View.GONE
                removeHashTags()
                tags.clear()
                allEt.clear()
            } else {
                removeHashTags()
            }
        }

    }

    private fun getETVal() {
        val strings = Array(allEt.size) { "n = $it" }
        for (i in 0..allEt.size) {
            try {
                if (allEt[i].equals(allEt.size)) {
                    Toast.makeText(this, allEt[i].toString(), Toast.LENGTH_SHORT).show()
                } else {
                    if (allEt[i].text.isBlank()) {
                        allEt[i].error = "हेश टैग्स रिक्त ना छोड़े"
                        tagFlag = true
                        tags.clear()
                        break
                    }
                    else if (!allEt[i].text.contains("#")) {
                        strings[i] = allEt[i].text.toString()
                        tags.add("#" + strings[i])
                        tagFlag = false
                    } else {
                        strings[i] = allEt[i].text.toString()
                        tags.add(strings[i])
                        tagFlag = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun addEditText() {

        // add edittext
        editTextTag = EditText(this)
        editTextTag.setText("#")
        allEt.add(editTextTag)
        val p = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        p.setMargins(0, 10, 20, 0)
        editTextTag.layoutParams = p
        editTextTag.requestFocus()
        editTextTag.id = count
        editTextTag.setPadding(5, 5, 5, 5)
        editTextTag.setBackgroundColor(Color.parseColor("#E8E8E8"))
        showKeyboard()
        llChildDes.addView(editTextTag)
        count++
    }

    private fun removeHashTags() {
        if (count > 0) {
            for (i in 0..count)
                if (editTextTag.id == i) {
                    llChildDes.removeViewAt(editTextTag.id)
                    allEt.removeAt(i)
                    editTextTag.id--
                    count--
                }
        }
    }


    private fun pollEditTextCondition() {
        polls.clear()
        if (etOption1.text.isNotEmpty()) {
            polls.add(etOption1.text.toString())
        }
        if (etOption2.text.toString().isNotEmpty()) {
            polls.add(etOption2.text.toString());
        }
        if (etOption3.text.toString().isNotEmpty()) {
            polls.add(etOption3.text.toString());
        }
        if (etOption4.text.toString().isNotEmpty()) {
            polls.add(etOption4.text.toString());
        }
        if (etOption5.text.toString().isNotEmpty()) {
            polls.add(etOption5.text.toString());
        }
    }

    private fun postData(
        session: String,
        type: String,
        desc: String,
        image: ArrayList<String>,
        poll: ArrayList<String>,
        tag: ArrayList<String>,
        contact: String,
        bgColor: String
    ) {
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<PostData> =
            service.createPost(session, type, desc, image, poll, tag, contact, bgColor)
        try {
            call.enqueue(object : Callback<PostData> {
                override fun onResponse(call: Call<PostData>, response: Response<PostData>) {
                    response.body()!!
                    val responseBody = response.body()
                    pbPost.visibility = View.GONE
                    clParentPost.visibility = View.VISIBLE

                    if (responseBody?.status == "001"){
                    Toast.makeText(this@PostActivity, "पोस्ट सफलतापूर्वक पोस्ट हुई ", Toast.LENGTH_LONG).show()
                        editText.text.clear()
                    }else{
                        Toast.makeText(this@PostActivity,"पोस्ट अपलोड नहीं हुई, कुछ देर बाद वापस कोशिश करे",Toast.LENGTH_LONG).show()
                    }
//                    Log.d("postRes", "onResponse: $response")
                }

                override fun onFailure(call: Call<PostData>, t: Throwable) {
                    Toast.makeText(this@PostActivity,"पोस्ट अपलोड नहीं हुई, कुछ देर बाद वापस कोशिश करे",Toast.LENGTH_LONG).show()
//                    Toast.makeText(this@PostActivity,t.message,Toast.LENGTH_LONG).show()

                    pbPost.visibility = View.GONE
                    clParentPost.visibility = View.VISIBLE
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //    This function is for future use
    private fun keyboardListener() {
        let {
            KeyboardVisibilityEvent.setEventListener(it, object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    if (isOpen) {
                        llBottomParent.orientation = LinearLayout.HORIZONTAL
                        tvCamera.visibility = View.GONE
                        tvSpeak.visibility = View.GONE
                        tvVideo.visibility = View.GONE
                        tvYoutube.visibility = View.GONE
//                        btnPost.visibility = View.GONE
                    } else {
                        llBottomParent.orientation = LinearLayout.VERTICAL
                        tvCamera.visibility = View.VISIBLE
                        tvSpeak.visibility = View.VISIBLE
                        tvVideo.visibility = View.VISIBLE
                        tvYoutube.visibility = View.VISIBLE
//                        btnPost.visibility = View.VISIBLE

//                        if (count > 0) {
//                            if (tagFlag) {
//                                if(!editTextTag.text.contains("#")){
//                                    tags.add("#${editTextTag.text}")
//                                }
//                                else {
//                                    tags.add(editTextTag.text.toString())
//                                    tagFlag = false
//                                }
//                            }
//                            if (editTextTag.text.isBlank()) {
//                                if (count == 1){
//                                    btnHashClear.visibility = View.GONE
//                                    removeHashTags()
//                                }else {
//                                    removeHashTags()
//                                }
//                            }
//                        }
                    }
                }
            })
        }
    }

    private fun setEditTextBgColor() {
        cvWhite.setOnClickListener {
            editText.setBackgroundColor(Color.WHITE)
            editText.setTextColor(Color.BLACK)
            editText.setHintTextColor(Color.BLACK)
            bg = "FFFFFFFF"
        }
        cvYellows.setOnClickListener {
            editText.setBackgroundColor(Color.parseColor("#FFB6C14B"))
            editText.setTextColor(Color.WHITE)
            editText.setHintTextColor(Color.WHITE)
            bg = "FFB6C14B"


        }
        cvBrown.setOnClickListener {
            editText.setBackgroundColor(Color.parseColor("#FFAC4444"))
            editText.setTextColor(Color.WHITE)
            editText.setHintTextColor(Color.WHITE)
            bg = "FFAC4444"
        }
        cvGreen.setOnClickListener {
            editText.setBackgroundColor(Color.parseColor("#82D16B"))
            editText.setTextColor(Color.WHITE)
            editText.setHintTextColor(Color.WHITE)
            bg = "82D16B"
        }
        cvOranges.setOnClickListener {
            editText.setBackgroundColor(Color.parseColor("#EFAC8B"))
            editText.setTextColor(Color.WHITE)
            editText.setHintTextColor(Color.WHITE)
            bg = "EFAC8B"
        }
        cvBlue.setOnClickListener {
            editText.setBackgroundColor(Color.parseColor("#628DAF"))
            editText.setTextColor(Color.WHITE)
            editText.setHintTextColor(Color.WHITE)
            bg = "628DAF"
        }
        cvReds.setOnClickListener {
            editText.setBackgroundColor(Color.parseColor("#F6756B"))
            editText.setTextColor(Color.WHITE)
            editText.setHintTextColor(Color.WHITE)
            bg = "F6756B"

        }

    }


    private fun initializeComponents() {
        editText = binding.editText
        cvWhite = binding.cvWhite
        cvBlue = binding.cvBlue
        cvBrown = binding.cvBrown
        cvGreen = binding.cvGreen
        cvOranges = binding.cvOrange
        cvReds = binding.cvRed
        cvYellows = binding.cvYellow
        llBottomParent = binding.llBottomParent
        llCamera = binding.llCamera
        llSpeak = binding.llSpeak
        llVideo = binding.llVideo
        llYoutube = binding.llYoutube
        tvCamera = binding.tvCamera
        tvSpeak = binding.tvSpeak
        tvVideo = binding.tvVideo
        tvYoutube = binding.tvYoutube
    }


    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
//            civImage.setImageURI(data?.data) // handle chosen image
            val filePath: Uri? = data?.data
            try {
                val bitmap: Bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byte: ByteArray = byteArrayOutputStream.toByteArray()
                sImage = Base64.encodeToString(byte, Base64.DEFAULT)
                types = "image"
                byteToImage(sImage)
                images.clear()
                bg = ""
                editText.setLines(3)
                images.add(sImage)

            } catch (e: Exception) {

            }
        }
    }

    private fun byteToImage(imageUrl: String) {
        try {
            val byte: ByteArray = Base64.decode(imageUrl, Base64.DEFAULT)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
            ivPostImage.visibility = View.VISIBLE
            llColorParent.visibility = View.GONE
            editText.background = null
            editText.setTextColor(Color.BLACK)
            editText.setHintTextColor(Color.BLACK)
            editText.gravity = Gravity.LEFT
            ivPostImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showKeyboard() {
        val inputMethodManager =
            this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

}