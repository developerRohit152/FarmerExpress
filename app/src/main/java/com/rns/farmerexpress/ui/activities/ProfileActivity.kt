package com.rns.farmerexpress.ui.activities

import android.R
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.ActivityProfileBinding
import com.rns.farmerexpress.model.*
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.properties.Delegates


class ProfileActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {
     private val  BASE_URL = "http://farmerexpress.rnsitsolutions.com/api/"

    private lateinit var binding : ActivityProfileBinding

    lateinit var spState : Spinner
    lateinit var spDis : Spinner
    lateinit var spSubDis : Spinner
    lateinit var spVillage : Spinner
    lateinit var etName : EditText
    lateinit var btnSaveInfo : Button


         var getStateName: ArrayList<String> = ArrayList()
    var getDisName: ArrayList<String> = ArrayList()
    var getSubDisName: ArrayList<String> = ArrayList()
    var getVillageName: ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeComponents()
        val ad : ArrayAdapter<String> = ArrayAdapter<String>(this,R.layout.simple_spinner_item)
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        getStateData()
        val session = intent.getStringExtra("session")
        btnSaveInfo.setOnClickListener {
            when {
                etName.text.isEmpty() -> {
                    etName.error = "अपना नाम लिखे"
                }
                statePos == 0 -> {
                    Toast.makeText(this, "अपना राज्य चुने",Toast.LENGTH_SHORT).show()
                }
                disPos == 0 -> {
                    Toast.makeText(this, "अपना जिला  चुने",Toast.LENGTH_SHORT).show()
                }
                subDistPos == 0 -> {
                    Toast.makeText(this, "अपना तहसील  चुने",Toast.LENGTH_SHORT).show()
                }
                villagePos == 0 -> {
                    Toast.makeText(this, "अपना गाव या शहर चुने",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    pbProfile.visibility = View.VISIBLE
                    addData(session.toString(),etName.text.trim().toString(), villageID.toString(), villageName)
                    PreferenceConnector.writeString(this,PreferenceConnector.profileName,etName.text.toString())
                    PreferenceConnector.writeString(this@ProfileActivity,PreferenceConnector.loginstatus,"true")

                }
            }
        }
    }

    private fun addData(session : String,name: String,location:String,village: String) {
        val service : ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call : Call<SessionRes> = service.addProfileData(session,name,location,village)
        try {
            call.enqueue(object : Callback<SessionRes>{
                override fun onResponse(call: Call<SessionRes>, response: Response<SessionRes>) {
                    val responseBody = response.body()!!
                    Log.d("sessionRes", "onResponse: "+response.message())
                    Log.d("sessionRes", "onResponse: "+response.body())
                    Log.d("sessionRes", "onResponse: "+response.code())
//                    Toast.makeText(this@ProfileActivity,response.message(),Toast.LENGTH_LONG).show()
                    if (responseBody.status == "001"){
                        pbProfile.visibility = View.GONE
                        startActivity(Intent(this@ProfileActivity,MainActivity::class.java))
                        finishAffinity()
                    }
                }
                override fun onFailure(call: Call<SessionRes>, t: Throwable) {
                    Log.d("sessionResFail", "onFailure: "+t.message)
                    pbProfile.visibility = View.GONE
                    Toast.makeText(this@ProfileActivity,t.message,Toast.LENGTH_LONG).show()
                }
            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun getStateData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)
        val retrofitData = retrofitBuilder.getAllStates()
        retrofitData.enqueue(object :
            Callback<List<StateData>?> {
            override fun onResponse(
                call: Call<List<StateData>?>, response: Response<List<StateData>?>
            ) {
                try {
                    val responseBody = response.body()!!
                    val getStateDatas: ArrayList<StateData> = ArrayList<StateData>()
                    getStateDatas.add(StateData(-1,"-- अपना राज्य चुने --"))
                    for (stateData in responseBody) {
                        val name = StateData(stateData.StateCode, stateData.StateName)
                        getStateDatas.add(name)
                    }
                    if (response.body() != null) {
                        try {
                            for (s in 0..getStateDatas.size) {
//                                getStateName.add(-1,"-- अपना राज्य चुने --")
                                getStateName.add(getStateDatas[s].StateName.toString())
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    val adapter: ArrayAdapter<Any?> = object : ArrayAdapter<Any?>(
                        this@ProfileActivity, android.R.layout.simple_spinner_dropdown_item,
                        getStateName as List<Any?>
                    ) {
                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view: TextView = super.getDropDownView(
                                position,
                                convertView,
                                parent
                            ) as TextView

                            // set item text bold and sans serif font
                            view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

                            // set spinner item padding
                            view.setPadding(
                                25.toDp(context), // left
                                10.toDp(context), // top
                                50.toDp(context), // right
                                10.toDp(context) // bottom
                            )

                            // set spinner items alternate color
//                    if (position %2 == 1){
//                        view.background = ColorDrawable(Color.parseColor("#F0FFFF"))
//                    }else{
//                        view.background = ColorDrawable(Color.parseColor("#ACE5EE"))
//                    }

                            return view
                        }
                    }
                    spState.adapter = adapter
                    spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            val i = getStateDatas.get(p2).StateCode
                            statePos = p2
//                            Toast.makeText(this@ProfileActivity,p2.toString(), Toast.LENGTH_SHORT).show()

                            getDisName.clear()
                            getDist(i)
                        }
                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<List<StateData>?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getDist(i: Int) {
        pbProfile.visibility = View.VISIBLE
        val service : ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call :Call<List<DisData>> = service.getDisData(i)
        try {
            call.enqueue(object : Callback<List<DisData>> {
                override fun onResponse(
                    call: Call<List<DisData>>,
                    response: Response<List<DisData>>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            try {
                                val responseBody = response.body()!!
                                val getDistDatas: ArrayList<DisData> = ArrayList<DisData>()
                                getDistDatas.add(DisData(-1,"-- अपना जिला  चुने --"))
                                for (disData in responseBody) {
                                    val name: DisData =
                                        DisData(disData.DisttCode, disData.DistrictName)
                                    getDistDatas.add(name)
                                }
                                if (response.body() != null) {
                                    try {
                                        for (s in 0..getDistDatas.size) {
                                            getDisName.add(getDistDatas.get(s).DistrictName.toString())
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                val adapter: ArrayAdapter<Any?> = object : ArrayAdapter<Any?>(this@ProfileActivity,android.R.layout.simple_spinner_dropdown_item, getDisName as List<Any?>
                                ) {
                                    override fun getDropDownView(
                                        position: Int,
                                        convertView: View?,
                                        parent: ViewGroup
                                    ): View {
                                        val view: TextView = super.getDropDownView(
                                            position,
                                            convertView,
                                            parent
                                        ) as TextView

                                        // set item text bold and sans serif font
                                        view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

                                        // set spinner item padding
                                        view.setPadding(
                                            25.toDp(context), // left
                                            10.toDp(context), // top
                                            50.toDp(context), // right
                                            10.toDp(context) // bottom
                                        )
                                        return view
                                    }
                                }
                                spDis.adapter = adapter
                                pbProfile.visibility = View.GONE
                                spDis.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            p0: AdapterView<*>?,
                                            p1: View?,
                                            p2: Int,
                                            p3: Long
                                        ) {
                                            val i: Int = getDistDatas[p2].DisttCode
//                                            Toast.makeText(
//                                                this@ProfileActivity,
//                                                i.toString(),
//                                                Toast.LENGTH_SHORT
//                                            ).show()
                                            disPos = p2
                                            getSubDisName.clear()
                                            getSubDisData(i)

                                        }


                                        override fun onNothingSelected(p0: AdapterView<*>?) {
                                            TODO("Not yet implemented")
                                        }
                                    }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }
                }

                override fun onFailure(call: Call<List<DisData>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

        }catch (e : Exception){
            e.printStackTrace()
        }


    }

    private fun getSubDisData(distCode : Int){
        pbProfile.visibility = View.VISIBLE
        val service : ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call :Call<List<SubDist>> = service.getSubDisData(distCode)
        try {
            call.enqueue(object : Callback<List<SubDist>> {
                override fun onResponse(call: Call<List<SubDist>>, response: Response<List<SubDist>>) {
                    val responseBody = response.body()!!
                    val getSubDistDatas: ArrayList<SubDist> = ArrayList<SubDist>()
                    getSubDistDatas.add(SubDist(-1,"-- अपनी  तहसील  चुने --"))


                    for (subDisData in responseBody){

                        val name: SubDist =
                            SubDist(subDisData.SubdistCode, subDisData.SubdistName)
                        getSubDistDatas.add(name)
                    }
                    if (response.body() != null) {
                        try {
                            for (s in 0..getSubDistDatas.size) {
                                getSubDisName.add(getSubDistDatas.get(s).SubdistName.toString())
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    val adapter:ArrayAdapter<Any?> = object: ArrayAdapter<Any?>(this@ProfileActivity,android.R.layout.simple_spinner_dropdown_item, getSubDisName as List<Any?>){
                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view:TextView = super.getDropDownView(
                                position,
                                convertView,
                                parent
                            ) as TextView

                            // set item text bold and sans serif font
                            view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

                            // set spinner item padding
                            view.setPadding(
                                25.toDp(context), // left
                                10.toDp(context), // top
                                50.toDp(context), // right
                                10.toDp(context) // bottom
                            )
                            return view
                        }
                    }
                    spSubDis.adapter = adapter
                    pbProfile.visibility = View.GONE
                    spSubDis.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            val i : Int = getSubDistDatas[p2].SubdistCode
//                            Toast.makeText(this@ProfileActivity,i.toString(),Toast.LENGTH_SHORT).show()
                            subDistPos = p2
                            getVillageName.clear()
                            getVillageData(i)
                        }


                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }
                }


                override fun onFailure(call: Call<List<SubDist>>, t: Throwable) {
                    Log.d("TAG", "onFailure: "+t.message)
                }
            })
        }catch ( e : Exception){
            e.printStackTrace()
        }
    }

    private fun getVillageData(subDistCode : Int){
        pbProfile.visibility = View.VISIBLE
        val service : ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call :Call<List<Village>> = service.getVillageData(subDistCode)
        try {
            call.enqueue(object : Callback<List<Village>> {
                override fun onResponse(call: Call<List<Village>>, response: Response<List<Village>>) {
                    val responseBody = response.body()!!
                    val getVillageDatas: ArrayList<Village> = ArrayList<Village>()
                    getVillageDatas.add(Village(-1,"-- अपना गाव या शहर चुने --"))


                    for (villageData in responseBody){

                        val name: Village =
                            Village(villageData.RevenueVillagecode, villageData.RevenueVillageName)
                        getVillageDatas.add(name)
                    }
                    if (response.body() != null) {
                        try {
                            for (s in 0..getVillageDatas.size) {
                                getVillageName.add(getVillageDatas.get(s).RevenueVillageName.toString())
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    val adapter:ArrayAdapter<Any?> = object: ArrayAdapter<Any?>(this@ProfileActivity,android.R.layout.simple_spinner_dropdown_item, getVillageName as List<Any?>){
                        override fun getDropDownView(
                            position: Int,
                            convertView: View?,
                            parent: ViewGroup
                        ): View {
                            val view:TextView = super.getDropDownView(
                                position,
                                convertView,
                                parent
                            ) as TextView

                            // set item text bold and sans serif font
                            view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

                            // set spinner item padding
                            view.setPadding(
                                25.toDp(context), // left
                                10.toDp(context), // top
                                50.toDp(context), // right
                                10.toDp(context) // bottom
                            )
                            return view
                        }
                    }
                    spVillage.adapter = adapter
                    pbProfile.visibility = View.GONE
                    spVillage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            villagePos = p2
                            villageID  = getVillageDatas[p2].RevenueVillagecode
                            villageName = getVillageDatas[p2].RevenueVillageName
//                            Toast.makeText(this@ProfileActivity,i.toString(),Toast.LENGTH_SHORT).show()

                        }


                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }
                    }
                }


                override fun onFailure(call: Call<List<Village>>, t: Throwable) {
                    Log.d("TAG", "onFailure: "+t.message)
                }
            })
        }catch ( e : Exception){
            e.printStackTrace()
        }
    }



    private fun initializeComponents() {
        spState = binding.spState
        spDis = binding.spDis
        spSubDis = binding.spSubDist
        spVillage = binding.spVillage
        etName = binding.etName
        btnSaveInfo = binding.btnSaveInfo
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
    ).toInt()

    companion object{
        var statePos by Delegates.notNull<Int>()
         var disPos by Delegates.notNull<Int>()
        var subDistPos by Delegates.notNull<Int>()
        var villagePos by Delegates.notNull<Int>()
        var villageID  by Delegates.notNull<Int>()
        var villageName  by Delegates.notNull<String>()


    }
}