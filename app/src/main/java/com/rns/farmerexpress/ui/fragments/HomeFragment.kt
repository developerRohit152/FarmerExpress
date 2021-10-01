package com.rns.farmerexpress.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonArray
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.HomeAdapter
import com.rns.farmerexpress.adapter.NewsAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.FragmentHomesBinding
import com.rns.farmerexpress.model.*
import com.rns.farmerexpress.ui.activities.PostActivity
import kotlinx.android.synthetic.main.fragment_all_news.view.*
import kotlinx.android.synthetic.main.fragment_homes.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import javax.security.auth.callback.Callback


class HomeFragment : Fragment() {

    private var _binding: FragmentHomesBinding? = null
    private lateinit var  recyclerView : RecyclerView
     var list: ArrayList<PostDatas> = ArrayList()
    lateinit var adapter: HomeAdapter
    var snackbar: Snackbar? = null
    lateinit var layoutManager: LinearLayoutManager
    var notLoading = true
    var count = 2
    var flagGetAll = true




    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()

                Toast.makeText(
                    requireContext(),
                    "Got Location: " + location.latitude,
                    Toast.LENGTH_LONG
                )
                    .show()
                Log.d("onLocationRes", "onLocationResult: ${location},${location.latitude} ${location.longitude}")
            }
        }
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomesBinding.inflate(inflater, container, false)
        val root: View = binding.root
//        val searchView : androidx.appcompat.widget.SearchView = binding.searchTemp.searchView

        recyclerView = binding.rvHomes
        val btnPost = binding.fab
        val session = PreferenceConnector.readString(context,PreferenceConnector.profilestatus,"")
        if (flagGetAll) {
            binding.pbHome.visibility = View.VISIBLE
            getHomeData(session)
        }
        binding.cvLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ){
                checkLocationPermission()
            }else{
                fusedLocationProvider?.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            }
        }
        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())

        checkLocationPermission()
        binding.rvHomes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                addDataOnScroll(session)

            }
        })


        layoutManager =
            LinearLayoutManager(requireContext(), LinearLayout.VERTICAL, false)
        adapter = HomeAdapter(requireActivity(), list)

        getWeatherData("d","d")
        btnPost.setOnClickListener{
            startActivity(Intent(requireActivity(),PostActivity::class.java))
        }
        return root
    }


    private fun addDataOnScroll(session: String) {
        if (notLoading && layoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1) {
            list.add(PostDatas(HomeAdapter.VIEW_TYPE_LOADING, 0, 0, "", "","","","","","","","",
                                                        0,"","",1,"","","",""))

            adapter.notifyItemInserted(list.size - 1)
            notLoading = false
            val i = count++
//        Toast.makeText(requireContext(), "count $i", Toast.LENGTH_SHORT).show()
            val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
            val call: retrofit2.Call<GetPostData> = service.getPost(session, i,10)
            try {
                call.enqueue(object : retrofit2.Callback<GetPostData> {
                    @SuppressLint("WrongConstant", "ShowToast", "ResourceType")
                    override fun onResponse(
                        call: Call<GetPostData>,
                        response: Response<GetPostData>
                    ) {
                        list.removeAt(list.size - 1)
                        adapter.notifyItemRemoved(list.size)
                        val responseBody = response.body()
                        if (!responseBody!!.post.isEmpty()) {
                            for (data in responseBody!!.post){
                                if (data.type == "image") {
                                    list.add(
                                        PostDatas(
                                            HomeAdapter.VIEW_TYPE_POST,
                                            data.post_id,
                                            data.user_id,
                                            data.user_name,
                                            data.user_image,
                                            data.post_images,
                                            data.post_tags,
                                            data.discription,
                                            data.contact,
                                            data.location,
                                            data.type,
                                            data.color,
                                            data.likes,
                                            data.comments,
                                            data.shares,
                                            data.isLiked,
                                            data.isPollSelected,
                                            data.selectedPoll,
                                            data.poll,
                                            data.date
                                        )
                                    )
//                                    adapter = HomeAdapter(requireActivity(), list)
//
//                                    rvHomes.layoutManager = layoutManager
//                                    rvHomes.adapter = adapter
                                }else if (data.type == "bg"){
                                    list.add(
                                        PostDatas(
                                            HomeAdapter.VIEW_TYPE_BG,
                                            data.post_id,
                                            data.user_id,
                                            data.user_name,
                                            data.user_image,
                                            data.post_images,
                                            data.post_tags,
                                            data.discription,
                                            data.contact,
                                            data.location,
                                            data.type,
                                            data.color,
                                            data.likes,
                                            data.comments,
                                            data.shares,
                                            data.isLiked,
                                            data.isPollSelected,
                                            data.selectedPoll,
                                            data.poll,
                                            data.date
                                        )
                                    )
//                                    adapter = HomeAdapter(requireActivity(), list)
//                                    rvHomes.layoutManager = layoutManager
//                                    rvHomes.adapter = adapter
                                }else{
                                    list.add(
                                        PostDatas(
                                            HomeAdapter.VIEW_TYPE_POLL,
                                            data.post_id,
                                            data.user_id,
                                            data.user_name,
                                            data.user_image,
                                            data.post_images,
                                            data.post_tags,
                                            data.discription,
                                            data.contact,
                                            data.location,
                                            data.type,
                                            data.color,
                                            data.likes,
                                            data.comments,
                                            data.shares,
                                            data.isLiked,
                                            data.isPollSelected,
                                            data.selectedPoll,
                                            data.poll,
                                            data.date
                                        )
                                    )
//                                    adapter = HomeAdapter(requireActivity(), list)
//
//                                    rvHomes.layoutManager = layoutManager
//                                    rvHomes.adapter = adapter
                                }
                            }
//                            rvHomes?.scrollToPosition(list.size - 18)
                            notLoading = true
                        } else {
//                        Toast.makeText(requireContext(), "End of page", Toast.LENGTH_SHORT).show()
                            snackbar =
                                Snackbar.make(
                                    requireView(),
                                    "पेज समाप्त हुआ",
                                    Snackbar.LENGTH_INDEFINITE
                                )
                                    .setAction("ठीक हैं ") { snackbar?.dismiss() }
                            snackbar!!.setActionTextColor(Color.WHITE)
                            val sbView = snackbar!!.view
                            snackbar!!.setBackgroundTint(Color.rgb(239, 127, 62))
                            val textView =
                                sbView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
                            textView.setTextColor(Color.WHITE)
                            snackbar!!.show()
                        }
                    }

                    override fun onFailure(call: Call<GetPostData>, t: Throwable) {
                        Log.d("onFailRes", "onFailure: ${t.message}")
                    }
                })

            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }

    private fun getHomeData(session:String){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<GetPostData> = service.getPost(session,page = 1,limit = 10)
        try {
            call.enqueue(object : retrofit2.Callback<GetPostData>{
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<GetPostData>, response: Response<GetPostData>) {
                    response.body()!!
                    val responseBody = response.body()
                    for (data in responseBody!!.post){
                        if (data.type == "image") {
                            list.add(
                                PostDatas(
                                    HomeAdapter.VIEW_TYPE_POST,
                                    data.post_id,
                                    data.user_id,
                                    data.user_name,
                                    data.user_image,
                                    data.post_images,
                                    data.post_tags,
                                    data.discription,
                                    data.contact,
                                    data.location,
                                    data.type,
                                    data.color,
                                    data.likes,
                                    data.comments,
                                    data.shares,
                                    data.isLiked,
                                    data.isPollSelected,
                                    data.selectedPoll,
                                    data.poll,
                                    data.date
                                )
                            )
                            adapter = HomeAdapter(requireActivity(), list)

                            rvHomes.layoutManager = layoutManager
                            rvHomes.adapter = adapter
                        }else if (data.type == "bg"){
                            list.add(
                                PostDatas(
                                    HomeAdapter.VIEW_TYPE_BG,
                                    data.post_id,
                                    data.user_id,
                                    data.user_name,
                                    data.user_image,
                                    data.post_images,
                                    data.post_tags,
                                    data.discription,
                                    data.contact,
                                    data.location,
                                    data.type,
                                    data.color,
                                    data.likes,
                                    data.comments,
                                    data.shares,
                                    data.isLiked,
                                    data.isPollSelected,
                                    data.selectedPoll,
                                    data.poll,
                                    data.date
                                )
                            )
                            adapter = HomeAdapter(requireActivity(), list)
                            rvHomes.layoutManager = layoutManager
                            rvHomes.adapter = adapter
                        }else{
                            list.add(
                                PostDatas(
                                    HomeAdapter.VIEW_TYPE_POLL,
                                    data.post_id,
                                    data.user_id,
                                    data.user_name,
                                    data.user_image,
                                    data.post_images,
                                    data.post_tags,
                                    data.discription,
                                    data.contact,
                                    data.location,
                                    data.type,
                                    data.color,
                                    data.likes,
                                    data.comments,
                                    data.shares,
                                    data.isLiked,
                                    data.isPollSelected,
                                    data.selectedPoll,
                                    data.poll,
                                    data.date
                                )
                            )
                            adapter = HomeAdapter(requireActivity(), list)

                            rvHomes.layoutManager = layoutManager
                            rvHomes.adapter = adapter
                        }
                    }
                    flagGetAll = false
                    binding.pbHome.visibility = View.INVISIBLE
                    Log.d("OnPostRes", "onResponse: ${responseBody?.post}")
                }

                override fun onFailure(call: Call<GetPostData>, t: Throwable) {
                    Log.d("OnPostResFail", "onResponse: ${t.message}")
                }

            })
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun getWeatherData(latitude:String,longitude:String){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<WeatherModel> = service.weathers(language = "hi-IN","26.9110671","75.7443329")
        try {
            call.enqueue(object  : retrofit2.Callback<WeatherModel>{
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                     val responseBody = response.body()!!
                    if (responseBody.city.locale.locale3 != "null") {
                        binding.tvCity.text = responseBody.city.locale.locale2 +","+ responseBody.city.locale.locale3
                    }
                    if (responseBody.city.locale.locale4 != "null"){
                        binding.tvCity.text = responseBody.city.locale.locale2 +","+ responseBody.city.locale.locale3 +","+responseBody.city.locale.locale4
                    }
                    Log.w("OnWeatherRes", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    Log.w("OnWeatherResFail", "onResponse: ${t.message}")

                }
            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

//
//    override fun onResume() {
//        super.onResume()
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//
//            fusedLocationProvider?.requestLocationUpdates(
//                locationRequest,
//                locationCallback,
//                Looper.getMainLooper()
//            )
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (ContextCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//
//            fusedLocationProvider?.removeLocationUpdates(locationCallback)
//        }
//    }


    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
//            checkBackgroundLocation()
        }
    }

//    private fun checkBackgroundLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestBackgroundLocationPermission()
//        }
//    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

//    private fun requestBackgroundLocationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(
//                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                ),
//                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
//            )
//        } else {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                MY_PERMISSIONS_REQUEST_LOCATION
//            )
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            null
                        )

                        // Now check background location
//                        checkBackgroundLocation()
                        checkLocationPermission()
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        fusedLocationProvider?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            null
                        )

                        Toast.makeText(
                            requireContext(),
                            "Granted Background Location Permission",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
                }
                return

            }
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}