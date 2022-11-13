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
import com.example.mexpense.databinding.FragmentEditExpenseBinding

class EditExpenseFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentEditExpenseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExpenseViewModel by activityViewModels{
        ExpenseViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database2.expenseDao()
        )
    }

    private val navigationArgs: EditExpenseFragmentArgs by navArgs()

    //get current expenseId. Use lazy to get the current expenseId when it's actually needed
    private val expenseId by lazy {
        navigationArgs.expenseId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    //checks for the validity of the entry
    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.expenseName.text.toString(),
            binding.expenseDetails.text.toString(),
            binding.expenseAmount.text.toString(),
            //dummy value
            1
        )
    }

    //updates the item with the given id
    private fun updateItem(expenseId: Int, currentTripOwnerId: Int) {
        if (isEntryValid()) {
            viewModel.updateWithNewExpense(
                expenseId,
                binding.expenseName.text.toString(),
                binding.expenseDetails.text.toString(),
                currentExpenseType,
                binding.expenseAmount.text.toString().toInt(),
                currentTripOwnerId
            )
            Toast.makeText(requireContext(), "Saved successfully!", Toast.LENGTH_SHORT).show()
            //checks for previous fragment to choose the proper navigation
            if (navigationArgs.previousFragment == "TripDetailFragment"){
                val action = EditExpenseFragmentDirections.actionEditExpenseFragmentToExpenseDetailFragment(expenseId)
                findNavController().navigate(action)
            }
            else
            {
                //returns to previous fragment
                findNavController().popBackStack()
            }
        }
        else{
            //if entry valid check returns false, toast an error message
            Toast.makeText(requireContext(), "Please fill in all the required fields.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expense = viewModel.retrieveStaticExpense(expenseId)

        val currentTripOwnerId = viewModel.retrieveTripOwnerId(expenseId)
        val expenseType = viewModel.retrieveExpenseType(expenseId)

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

        //sets the expense type spinner selection to the one that's already associated with the current id
        binding.expenseType.setSelection(resources.getStringArray(R.array.expense_types).indexOf(expenseType))

        //sets the expense data to the edittextviews
        binding.apply{
            binding.expenseName.setText(expense.expenseName)
            binding.expenseDetails.setText(expense.expenseDetails)
            binding.expenseAmount.setText(expense.expenseAmount.toString())

            binding.ButtonEnter.setOnClickListener{
                updateItem(expenseId, currentTripOwnerId)
            }
        }
    }

    //used to handle item select action
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        currentExpenseType = parent.getItemAtPosition(pos).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}