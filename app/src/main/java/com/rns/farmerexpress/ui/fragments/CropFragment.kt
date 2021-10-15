package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.BuyItemAdapter

import com.rns.farmerexpress.adapter.HomeAdapter
import com.rns.farmerexpress.apihandler.APIClient
import com.rns.farmerexpress.apihandler.ApiInterface
import com.rns.farmerexpress.commonUtility.PreferenceConnector
import com.rns.farmerexpress.databinding.FragmentAEquipmentBinding
import com.rns.farmerexpress.databinding.FragmentCropBinding
import com.rns.farmerexpress.model.HomeModel
import com.rns.farmerexpress.model.SubBuyData
import com.rns.farmerexpress.model.SubBuyModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class CropFragment : Fragment() {
    private var _binding: FragmentCropBinding? = null
    private val binding get() = _binding!!
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var adapter : BuyItemAdapter
    var catid = 8
    var list = ArrayList<SubBuyData>()
    lateinit var layoutManager: LinearLayoutManager
    @SuppressLint("WrongConstant")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCropBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.rvSubBuy
        progressBar = binding.pbSubBuy
        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
            catid = getInt(ARG_OBJECT)
        }

        val session = PreferenceConnector.readString(requireContext(),
            PreferenceConnector.profilestatus,"")
//        val catid : String = intent.getStringExtra("catid") as String
        val latitude = PreferenceConnector.readString(requireContext(), PreferenceConnector.LATITUDE,"")
        val longitude = PreferenceConnector.readString(requireContext(), PreferenceConnector.LONGITUDE,"")

        getSubBuyData(session,latitude,longitude,catid.toString())

        adapter = BuyItemAdapter(requireActivity(),list)
        layoutManager = LinearLayoutManager(requireContext(),LinearLayout.VERTICAL,false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

//        initViews(root)
        return root


    }
    private fun getSubBuyData(session : String,latitude:String,longitude : String,catid:String){
        val service: ApiInterface = APIClient.getClient()!!.create(ApiInterface::class.java)
        val call: Call<SubBuyModel> = service.getBuyData(session,"user","1","10",latitude,longitude,"1000",catid,"")
        try {
            call.enqueue(object : Callback<SubBuyModel> {
                @SuppressLint("WrongConstant")
                override fun onResponse(call: Call<SubBuyModel>, response: Response<SubBuyModel>) {
                    val responseBody = response.body()!!
                    for (data in responseBody.store){
                        list.add(
                            SubBuyData(data.store_id,data.user_name,data.user_image,data.place,data.description,data.store_images,data.contact,data.distance,data.likes,data.isLiked
                            ,data.date)
                        )
                    }
                    adapter = BuyItemAdapter(requireActivity(),list)
                    layoutManager = LinearLayoutManager(requireContext(),LinearLayout.VERTICAL,false)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    Log.d("onResSubBuyData", "onResponse: ${responseBody.store}")
                }

                override fun onFailure(call: Call<SubBuyModel>, t: Throwable) {
                    Log.d("onResSubBuyDataFail", "onResponse: ${t.message}")
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(),"डेटा लोड नहीं हुआ , कुछ देर बाद प्रयास करे",
                        Toast.LENGTH_LONG).show()
                }

            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    companion object {
        const val ARG_OBJECT = "object"
        fun newInstance(): CropFragment {
            return CropFragment()
        }
    }
}