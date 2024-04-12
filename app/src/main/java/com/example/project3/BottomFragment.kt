package com.example.project3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.project3.databinding.FragmentBottomBinding
import java.io.Serializable

class BottomFragment : Fragment() {

    private lateinit var bottomBinding: FragmentBottomBinding
    private var mCatData: CatData = CatData()

    companion object {
        fun newInstance(catData: CatData): BottomFragment {
            val fragment = BottomFragment()
            val bundle = Bundle()
            bundle.putSerializable("serializable", catData)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bottomBinding = FragmentBottomBinding.inflate(inflater, container, false)

        mCatData = arguments?.getSerializable("serializable") as CatData

        bottomBinding.name.text = mCatData.name
        bottomBinding.origin.text = mCatData.origin
        bottomBinding.temperament.text = mCatData.temperament


        context?.let {
            Glide.with(it)
                .load(mCatData.imageUrl)
                .into(bottomBinding.catImage)
        }


        Log.i("BottomFragment", "Bottom Fragment loaded")
        return bottomBinding.root
    }

}