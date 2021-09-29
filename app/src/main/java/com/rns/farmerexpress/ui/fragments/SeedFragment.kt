




package com.rns.farmerexpress.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.rns.farmerexpress.R
import com.rns.farmerexpress.adapter.HomeAdapter
import com.rns.farmerexpress.databinding.FragmentSeedBinding
import com.rns.farmerexpress.model.HomeModel

class SeedFragment : Fragment() {
    private var _binding: FragmentSeedBinding? = null
    lateinit var rvSeedPost : RecyclerView
    private val imageList = ArrayList<SlideModel>()
    private val imageList2  = ArrayList<SlideModel>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSeedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        rvSeedPost = binding.rvSeedPost

        addImageSlider()
        addSeedPostData()

        return root

    }
    private fun addImageSlider() {
        imageList2.add(SlideModel(R.drawable.tractorimg, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.timg, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.tt, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.tractorimg, ScaleTypes.CENTER_INSIDE))    }

    @SuppressLint("WrongConstant")
    private fun addSeedPostData() {
        val list =  ArrayList<HomeModel>()
//        list.add(
//            HomeModel(
//                HomeAdapter.VIEW_TYPE_ONE,R.drawable.ic_user,"रमेश सोनी","जयपुर | राजस्थान ","मुझे अपनी भेस  बेचनी  हैं, जो ईछुक हो करपिया फोन करे","1",imageList,
//                "25","12","23")
//        )
//        list.add(
//            HomeModel(
//                HomeAdapter.VIEW_TYPE_ONE,R.drawable.ic_user,"महेश मीणा","भीलवाड़ा | राजस्थान ","मुझे अपनी गाय बेचनी हैं , जो ईछुक हो करपिया फोन करे","6",imageList2,
//                "20000","55","50")
//        )


//        val adapter = HomeAdapter(requireActivity(),list)
        rvSeedPost.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayout.VERTICAL,false)
//        rvSeedPost.adapter = adapter
    }
    companion object {

    }
}