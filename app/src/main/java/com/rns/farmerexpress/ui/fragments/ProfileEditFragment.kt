package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rns.farmerexpress.R
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.FragmentProfileEditBinding
import com.rns.farmerexpress.model.SessionRes
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class ProfileEditFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentProfileEditBinding? = null
    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etVillage: EditText
    lateinit var spGender: Spinner
    lateinit var etDOB: EditText
    lateinit var etProfession: EditText
    lateinit var etAbout: EditText
    lateinit var etMobile: EditText
    lateinit var civImage: CircleImageView
    lateinit var civChoose: CircleImageView
    lateinit var btnUpdate: Button
    lateinit var progressBar: ProgressBar
    lateinit var tvGender: TextView

    var sImage: String = ""


    val list: Array<String> = arrayOf("अपना लिंग चुने", "पुरुष", "महिला", "अन्य")
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentProfileEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        etName = binding.etName
        etEmail = binding.etEmail
        etVillage = binding.etVillage
        spGender = binding.spGender
        etDOB = binding.etDOB
        etProfession = binding.etProfession
        etAbout = binding.etAbout
        etMobile = binding.etPhone
        civImage = binding.civImage
        btnUpdate = binding.btnUpdate
        tvGender = binding.tvGender
        civChoose = binding.civChoose
        progressBar = binding.pbProfileEdit


        val session =
            PreferenceConnector.readString(requireContext(), PreferenceConnector.profilestatus, "")
        val ad: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, list)
        showData(session.toString())
        spGender.adapter = ad
        var gender: String? = null
        progressBar.visibility = View.VISIBLE
//        llParentP.visibility = View.GONE
        civChoose.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 100
//                )
//            } else {
                openGalleryForImage()
//            }
        }
        spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gender = spGender.selectedItem.toString()
//                Toast.makeText(requireContext(),gender.toString(),Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        btnUpdate.setOnClickListener {
//            if (gender.toString() == "अपना लिंग चुने"){
//                Toast.makeText(requireContext(),"अपना लिंग चुने",Toast.LENGTH_SHORT).show()
//            }else {
            addData(
                session,
                etName.text.toString(),
                sImage,
                etEmail.text.toString(),
                gender.toString(),
                etDOB.text.toString(),
                etProfession.text.toString(),
                etAbout.text.toString()
            )
            progressBar.visibility = View.VISIBLE
            llParentP.visibility = View.GONE
//            }
        }
        return root
    }

    private fun showData(session: String) {
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<SessionRes> = service.verifyNumber(session)
        try {
            call.enqueue(object : Callback<SessionRes> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<SessionRes>, response: Response<SessionRes>) {
                    val responseBody = response.body()!!
                    try {
                        Log.d("verifyNumber", "onResponse: " + response.message())
                        val data = responseBody.data
                        for (sess in data) {
                            etName.setText(sess.name); etMobile.setText(sess.mobile); etEmail.setText(
                                sess.email
                            ); etVillage.setText(sess.village)
                            tvGender.text = sess.sex; etDOB.setText(sess.dob);etProfession.setText(
                                sess.profession
                            );etAbout.setText(sess.about)
                            byteToImage(sess.photo)
                            PreferenceConnector.writeString(
                                requireContext(),
                                PreferenceConnector.profileName,
                                sess.name
                            )
                            PreferenceConnector.writeString(
                                requireContext(),
                                PreferenceConnector.profilePic,
                                sess.photo
                            )


                        }
                        progressBar.visibility = View.GONE
                        llParentP.visibility = View.VISIBLE
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<SessionRes>, t: Throwable) {
                    Log.d("verifyNumberFail", "onFailure: " + t.message)
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun addData(
        session: String,
        name: String,
        photo: String,
        email: String,
        sex: String,
        dob: String,
        profession: String,
        about: String
    ) {
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<SessionRes> =
            service.addProfileFragData(session, name, photo, email, sex, dob, profession, about)
        try {
            call.enqueue(object : Callback<SessionRes> {
                override fun onResponse(call: Call<SessionRes>, response: Response<SessionRes>) {
                    val responseBody = response.body()!!
                    Log.d("sessionRes", "onResponse: " + response.message())
                    Log.d("sessionRes", "onResponse: " + response.body())
                    Log.d("sessionRes", "onResponse: " + response.code())
                    Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                    if (responseBody.status == "001") {
                        progressBar.visibility = View.GONE
                        llParentP.visibility = View.VISIBLE
                    }else if (responseBody.status == "002"){
                        Toast.makeText(requireContext(),"डेटा अपलोड नहीं  हुआ , वापिस प्रयास करे ",Toast.LENGTH_LONG).show()
                    }

                }

                override fun onFailure(call: Call<SessionRes>, t: Throwable) {
                    Log.d("sessionResFail", "onFailure: " + t.message)
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                    Toast.makeText(requireContext(),"डेटा अपलोड नहीं  हुआ , वापिस प्रयास करे ",Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                    llParentP.visibility = View.VISIBLE
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byte: ByteArray = byteArrayOutputStream.toByteArray()
                sImage = Base64.encodeToString(byte, Base64.DEFAULT)
                byteToImage(sImage)

            } catch (e: Exception) {

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGalleryForImage()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    private fun byteToImage(imageUrl: String) {
        try {
            val byte: ByteArray = Base64.decode(imageUrl, Base64.DEFAULT)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
            civImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}