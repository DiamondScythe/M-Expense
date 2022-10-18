package com.example.mexpense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
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
    private var tripRisk: String = "No"

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.riskCheckBox -> {
                    if (checked) {
                        tripRisk = "Yes"
                        Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show()
                    } else {
                        tripRisk = "No"
                        Toast.makeText(context, "unchecked", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

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
            binding.tripTime.text.toString(),
            tripRisk
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewTrip(
                binding.tripLocation.text.toString(),
                binding.tripTime.text.toString(),
                tripRisk,
                binding.tripDescription.text.toString(),


            )
            binding.tripLocation.text.clear()
            binding.tripTime.text.clear()
            binding.tripDescription.text.clear()
        }

        val action = EnterTripFragmentDirections.actionEnterTripFragmentToSelectionFragment()
//        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            binding.ButtonEnter.setOnClickListener{
                addNewItem()
            }
            binding.riskCheckBox.setOnCheckedChangeListener{ view, _ ->
                onCheckboxClicked(view)
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