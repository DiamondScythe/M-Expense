package com.example.mexpense

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mexpense.databinding.FragmentSelectionBinding


class SelectionFragment : Fragment() {

    private var binding: FragmentSelectionBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSelectionBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply{
            InputButton.setOnClickListener{
                findNavController().navigate(R.id.action_selectionFragment_to_enterTripFragment)}
            ViewDataButton.setOnClickListener{
                findNavController().navigate(R.id.action_selectionFragment_to_viewDataFragment)}
        }
    }
}