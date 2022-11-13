package com.example.mexpense.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.TripViewModel
import com.example.mexpense.TripViewModelFactory
import com.example.mexpense.databinding.FragmentConfirmTripBinding

class ConfirmTripFragment : Fragment() {

    private var _binding: FragmentConfirmTripBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }

    private val navigationArgs: ConfirmTripFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentConfirmTripBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binds the data to the views
        binding.apply {
            textViewName.text = navigationArgs.tripName
            textViewLocation.text = navigationArgs.tripLocation
            textViewDetails.text = navigationArgs.tripDescription
            textViewRisk.text = navigationArgs.tripRisk
            textViewTime.text = navigationArgs.tripTime
        }

        //sets action for enter button
        binding.enterButton.setOnClickListener {
            viewModel.addNewTrip(
                navigationArgs.tripName,
                navigationArgs.tripLocation,
                navigationArgs.tripTime,
                navigationArgs.tripRisk,
                navigationArgs.tripDescription
            )
            Toast.makeText(requireContext(), "Trip added!", Toast.LENGTH_SHORT).show()
            val action = ConfirmTripFragmentDirections.actionConfirmTripFragmentToSelectionFragment()
            findNavController().navigate(action)
        }
    }

}