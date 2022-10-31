package com.example.mexpense.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.data.ExpenseViewModel
import com.example.mexpense.data.ExpenseViewModelFactory
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.databinding.FragmentEnterExpenseBinding


class EnterExpenseFragment : Fragment() {
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

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.expenseName.text.toString(),
            binding.expenseDetails.text.toString(),
            binding.expenseType.text.toString(),
            binding.expenseAmount.text.toString().toInt(),
            navigationArgs.tripId
            )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            viewModel.addNewExpense(
                binding.expenseName.text.toString(),
                binding.expenseDetails.text.toString(),
                binding.expenseType.text.toString(),
                binding.expenseAmount.text.toString().toInt(),
                navigationArgs.tripId
            )
        }
        binding.expenseName.text.clear()
        binding.expenseDetails.text.clear()
        binding.expenseType.text.clear()
        binding.expenseAmount.text.clear()
        val action = EnterExpenseFragmentDirections.actionEnterExpenseFragmentToTripDetailFragment(navigationArgs.tripId)
        findNavController().navigate(action)
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