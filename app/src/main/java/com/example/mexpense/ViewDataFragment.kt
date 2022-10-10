package com.example.mexpense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mexpense.databinding.FragmentEnterDataBinding
import com.google.firebase.database.FirebaseDatabase


class ViewDataFragment : Fragment() {
    private var binding: FragmentEnterDataBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentEnterDataBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }
}