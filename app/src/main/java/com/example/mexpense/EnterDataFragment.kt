package com.example.mexpense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.data.Trip
import com.example.mexpense.databinding.FragmentEnterDataBinding
import com.google.firebase.database.FirebaseDatabase


class EnterDataFragment : Fragment() {
    private var binding: FragmentEnterDataBinding? = null
    var database = FirebaseDatabase.getInstance().reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentEnterDataBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply{
            binding?.ButtonEnter?.setOnClickListener{
                uploadData()
            }
        }
    }

    fun uploadData(){
        binding?.apply{
            var inputId = binding?.EditTextId?.text.toString().toInt()
            var inputLocation = binding?.EditTextLocation?.text.toString()
            var inputTime = binding?.EditTextTime?.text.toString()
            database.child("Trip").child(inputId.toString()).setValue(Trip(inputLocation, inputTime))
            binding?.EditTextId?.setText("")
            binding?.EditTextTime?.setText("")
            binding?.EditTextLocation?.setText("")
        }
    }

}