package com.example.mexpense.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.R
import com.example.mexpense.TripViewModel
import com.example.mexpense.TripViewModelFactory
import com.example.mexpense.databinding.FragmentEditTripBinding
import java.text.SimpleDateFormat
import java.util.*


class EditTripFragment : Fragment() {
    private var _binding: FragmentEditTripBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }
    private var tripRisk: String = "No"

    private val navigationArgs: EditTripFragmentArgs by navArgs()

    private fun onCheckboxClicked(view: View) {
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
        _binding = FragmentEditTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.tripName.text.toString(),
            binding.tripLocation.text.toString(),
            binding.tripTime.text.toString(),
            tripRisk
        )
    }

    private fun updateItem(id: Int) {
        if (isEntryValid()) {
            viewModel.updateWithNewTrip(
                id,
                binding.tripName.text.toString(),
                binding.tripLocation.text.toString(),
                binding.tripTime.text.toString(),
                tripRisk,
                binding.tripDescription.text.toString(),
                )
            //checks for previous fragment name to navigate properly
            if (navigationArgs.previousFragment == "ViewDataFragment"){
                val action = EditTripFragmentDirections.actionEditTripFragmentToTripDetailFragment(id)
                findNavController().navigate(action)
            }
            else
            {
                findNavController().popBackStack()
            }

        }
        else{
            Toast.makeText(requireContext(), "Please fill in all the required fields.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.tripId

        val trip = viewModel.retrieveStaticTrip(id)

        if (trip.tripRiskAssessment.equals("Yes",true)){
            binding.riskCheckBox.isChecked = true
        }

        binding.apply {
            //adding data to the data entry fields
            tripName.setText(trip.tripName)
            tripLocation.setText(trip.tripLocation)
            tripTime.setText(trip.tripTime)
            tripDescription.setText(trip.tripDescription)

            //setting up buttons and stuff
            ButtonEnter.setOnClickListener {
                updateItem(id)
            }
            binding.tripTime.transformIntoDatePicker(requireContext(), "MM/dd/yyyy", Date())
            binding.riskCheckBox.setOnCheckedChangeListener { view, _ ->
                onCheckboxClicked(view)
            }
        }
    }

    fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }
}