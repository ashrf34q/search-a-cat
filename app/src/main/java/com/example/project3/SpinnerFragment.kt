package com.example.project3

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.example.project3.databinding.FragmentSpinnerBinding
import java.util.ArrayList
import java.util.Arrays

private const val FIRST_OPTION = "Select Breed"

class SpinnerFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var spinnerBinding: FragmentSpinnerBinding
    private lateinit var activityCallback : BreedSelection

    interface BreedSelection {
        fun onBreedSelected(breedName: String)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


        try {
            activityCallback = context as BreedSelection
        }
        catch (e : ClassCastException) {
            throw ClassCastException("$context must implement BreedSelection")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        spinnerBinding =  FragmentSpinnerBinding.inflate(inflater, container, false)

        val spinner = spinnerBinding.spinner

        val breedsList = arguments?.getStringArray("breed_array")

        if (breedsList != null) {
            if(breedsList.isEmpty()) Log.i("FragmentSpinner", "Breeds list is empty")
        }

        val mutableList = breedsList?.toMutableList()
        mutableList?.add(0,  FIRST_OPTION)

        // breedsList!!.copyOf()

        val array = mutableList!!.toTypedArray()
        println("SpinnerFragment: $array")

        spinner.onItemSelectedListener = this

       val arrayAdapter : ArrayAdapter<String>? =
           context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, array) }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = arrayAdapter
        spinner.setSelection(0)

        return spinnerBinding.root
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(parent?.getItemAtPosition(position).toString() != FIRST_OPTION)
            activityCallback.onBreedSelected(parent?.getItemAtPosition(position).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(context, "Please select a breed", Toast.LENGTH_SHORT).show()
    }
}