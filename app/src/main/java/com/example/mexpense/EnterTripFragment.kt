package com.example.mexpense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.FragmentEnterTripBinding
import androidx.navigation.fragment.findNavController


class EnterTripFragment : Fragment() {
    private var _binding: FragmentEnterTripBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TripViewModel by activityViewModels{
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }
    lateinit var trip: Trip

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.tripLocation.text.toString(),
            binding.tripTime.text.toString()
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewTrip(
                binding.tripLocation.text.toString(),
                binding.tripTime.text.toString()
            )
        }
        binding.tripLocation.text.clear()
        binding.tripTime.text.clear()
        val action = EnterTripFragmentDirections.actionEnterTripFragmentToSelectionFragment()
//        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            binding.ButtonEnter.setOnClickListener{
                addNewItem()
            }
        }
    }


//    fun uploadData(){
//        binding?.apply{
//            var inputId = binding?.EditTextId?.text.toString().toInt()
//            var inputLocation = binding?.EditTextLocation?.text.toString()
//            var inputTime = binding?.EditTextTime?.text.toString()
//            database.child("Trip").child(inputId.toString()).setValue(Trip(inputLocation, inputTime))
//            binding?.EditTextId?.setText("")
//            binding?.EditTextTime?.setText("")
//            binding?.EditTextLocation?.setText("")
//        }
//    }

}