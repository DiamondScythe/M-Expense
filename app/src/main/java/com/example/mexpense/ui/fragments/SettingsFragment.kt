package com.example.mexpense.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.TripViewModel
import com.example.mexpense.TripViewModelFactory
import com.example.mexpense.data.ExpenseViewModel
import com.example.mexpense.data.ExpenseViewModelFactory
import com.example.mexpense.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val tripViewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }

    private val expenseViewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database2.expenseDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resetDatabaseButton.setOnClickListener {
            dropDatabaseConfirm()
        }
    }

    private fun dropDatabaseConfirm() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("RESET DATABASE")
            .setMessage("You are about to reset the database. Are you sure you want to proceed? This action cannot be undone.")
            .setCancelable(true)
            .setNegativeButton("No") { dialog , _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog , _ ->
                GlobalScope.launch {
                    dropDatabase()
                }
                Toast.makeText(requireContext(), "Database reset successfully", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun dropDatabase() {
        tripViewModel.dropTrip()
        expenseViewModel.dropExpense()
    }
}