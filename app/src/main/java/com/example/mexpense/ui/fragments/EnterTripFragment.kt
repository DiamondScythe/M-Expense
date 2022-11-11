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
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.FragmentEnterTripBinding
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.R
import com.example.mexpense.TripViewModel
import com.example.mexpense.TripViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


class EnterTripFragment : Fragment() {
    private var _binding: FragmentEnterTripBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }
    lateinit var trip: Trip
    private var tripRisk: String = "No"

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
        _binding = FragmentEnterTripBinding.inflate(inflater, container, false)
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

    private fun addNewItem() {
        if (isEntryValid()) {
            val action = EnterTripFragmentDirections.actionEnterTripFragmentToConfirmTripFragment(
                binding.tripName.text.toString(),
                binding.tripLocation.text.toString(),
                binding.tripTime.text.toString(),
                tripRisk,
                binding.tripDescription.text.toString(),
            )
            findNavController().navigate(action)
        }
        else{
            Toast.makeText(requireContext(), "Please fill in all the required fields.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            binding.ButtonEnter.setOnClickListener {
                addNewItem()
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