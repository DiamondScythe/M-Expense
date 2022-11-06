package com.example.mexpense.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mexpense.databinding.FragmentSelectionBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SelectionFragment : Fragment() {
    private var _binding: FragmentSelectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }
}