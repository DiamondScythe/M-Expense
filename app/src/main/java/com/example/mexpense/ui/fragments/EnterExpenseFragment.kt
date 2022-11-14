package com.example.mexpense.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.R
import com.example.mexpense.data.ExpenseViewModel
import com.example.mexpense.data.ExpenseViewModelFactory
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.databinding.FragmentEnterExpenseBinding

//var used to keep track of the currentExpenseType of the spinner
var currentExpenseType: String = ""

class EnterExpenseFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var _binding: FragmentEnterExpenseBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by activityViewModels{
        ExpenseViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database2.expenseDao()
        )
    }
    lateinit var expense: Expense

    private val navigationArgs: EnterExpenseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    //checks for the validity of the entry
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.expenseName.text.toString(),
            binding.expenseDetails.text.toString(),
            binding.expenseAmount.text.toString(),
            navigationArgs.tripId
            )
    }

    private fun addNewItem() {
        //checks for valid entry
        if (isEntryValid()) {
            viewModel.addNewExpense(
                binding.expenseName.text.toString(),
                binding.expenseDetails.text.toString(),
                currentExpenseType,
                binding.expenseAmount.text.toString().toInt(),
                navigationArgs.tripId
            )
            Toast.makeText(requireContext(), "Expense added!", Toast.LENGTH_SHORT).show()
            val action = EnterExpenseFragmentDirections.actionEnterExpenseFragmentToTripDetailFragment(navigationArgs.tripId)
            findNavController().navigate(action)
        }
        //if entry isn't valid, toast an error message
        else{
            Toast.makeText(requireContext(), "Please fill in all the required fields.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.expense_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.expenseType.adapter = adapter
            binding.expenseType.onItemSelectedListener = this
        }

        binding.apply{
            binding.ButtonEnter.setOnClickListener{
                addNewItem()
            }
        }
    }

    //handles item select event for spinner
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        currentExpenseType = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}