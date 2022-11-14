package com.example.mexpense.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.R
import com.example.mexpense.TripViewModelFactory
import com.example.mexpense.data.ExpenseViewModel
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.databinding.FragmentExpenseDetailBinding

class ExpenseDetailFragment : Fragment() {

    private var _binding: FragmentExpenseDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var expense: Expense

    private val expenseViewModel: ExpenseViewModel by activityViewModels{
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }

    //bind the current expense object to the views
    private fun bind(expense: Expense){
        binding.apply{
            textViewName.text = expense.expenseName
            textViewDetails.text = expense.expenseDetails
            textViewType.text = expense.expenseType
            textViewAmount.text = expense.expenseAmount.toString()
        }
        //if expense details is empty, set the textview to let the user know
        if (expense.expenseDetails.isEmpty())
        {
            binding.textViewDetails.setTextColor(resources.getColor(R.color.dark_gray))
            binding.textViewDetails.text = "(No details available)"
        }
    }

    private val navigationArgs: ExpenseDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExpenseDetailBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.expenseId

        //observe the expense object, then bind it to the view
        expenseViewModel.retrieveExpense(id).observe(this.viewLifecycleOwner){ selectedExpense ->
            expense = selectedExpense
            bind(expense)
        }
        binding.editButton.setOnClickListener {
            //the previousFragment param is used to let the edit fragment know which fragment to go back to
            //after operations are finished
            val action = ExpenseDetailFragmentDirections.actionExpenseDetailFragmentToEditExpenseFragment(
                navigationArgs.expenseId, "ExpenseDetailFragment")
            this.findNavController().navigate(action)
        }
    }
}