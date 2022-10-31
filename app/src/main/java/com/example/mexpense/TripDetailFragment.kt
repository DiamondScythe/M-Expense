package com.example.mexpense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mexpense.data.ExpenseViewModel
import com.example.mexpense.data.ExpenseViewModelFactory
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.FragmentTripDetailBinding

class TripDetailFragment : Fragment() {

    private var _binding: FragmentTripDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var trip: Trip
    private val tripViewModel: TripViewModel by activityViewModels{
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }

    private val expenseViewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(
            (activity?.application as MExpenseApplication).database2.expenseDao()
        )
    }

    private fun bind(trip: Trip){
        binding.apply{
            textView.text = "Location: ${trip.tripLocation}"
            textView2.text = "Time: ${trip.tripTime}"
        }
    }

    private val navigationArgs: TripDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTripDetailBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //you passed item id as a navigation argument to TripDetailFragment from the ViewDataFragment
        //Retrieve and assign the navigation argument to this new variable.
        val id = navigationArgs.tripId
        tripViewModel.retrieveTrip(id).observe(this.viewLifecycleOwner) { selectedItem ->
            trip = selectedItem
            bind(trip)
        }
        val adapter = ExpenseListAdapter {
            //passes the lambda for OnItemClicked
        }
        binding.recyclerView.adapter = adapter
        expenseViewModel.retrieveTripExpense(id).observe(this.viewLifecycleOwner) { expenses ->
            //This will update the RecyclerView with the new items on the list.
            expenses.let {
                adapter.submitList(it)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        binding.addExpenseButton.setOnClickListener{
            val action = TripDetailFragmentDirections.actionTripDetailFragmentToEnterExpenseFragment(navigationArgs.tripId)
            this.findNavController().navigate(action)
        }
    }
}