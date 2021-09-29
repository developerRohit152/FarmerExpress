package com.rns.farmerexpress.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rns.farmerexpress.R
import com.rns.farmerexpress.ui.activities.GamesActivity
import com.rns.farmerexpress.ui.activities.NewsActivity
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root:View =  inflater.inflate(R.layout.fragment_info, container, false)
        root.tvReadNews.setOnClickListener{
            val i = Intent(requireActivity(),NewsActivity::class.java)
            startActivity(i)
        }
        root.tvPlayGame.setOnClickListener {
            startActivity(Intent(requireActivity(), GamesActivity::class.java))
        }
        return root
    }

    companion object {

    }
}