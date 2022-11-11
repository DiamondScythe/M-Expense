package com.example.mexpense.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.R
import com.example.mexpense.TripViewModel
import com.example.mexpense.TripViewModelFactory
import com.example.mexpense.data.ExpenseViewModel
import com.example.mexpense.data.ExpenseViewModelFactory
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
            textViewName.text = trip.tripName
            textViewDetails.text = trip.tripDescription
            textViewLocation.text = trip.tripLocation
            textViewRisk.text = trip.tripRiskAssessment
            textViewTime.text = trip.tripTime
        }
    }

    private val navigationArgs: TripDetailFragmentArgs by navArgs()
    suspend fun deleteStuff(expenseId: Int){
        expenseViewModel.removeExpense(expenseId)
    }

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
        val adapter = ExpenseListAdapter(this)
        binding.recyclerView.adapter = adapter
        expenseViewModel.retrieveTripExpense(id).observe(this.viewLifecycleOwner) { expenses ->
            //This will update the RecyclerView with the new items on the list.
            expenses.let {
                adapter.setData(it)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        binding.addExpenseButton.setOnClickListener{
            val action = TripDetailFragmentDirections.actionTripDetailFragmentToEnterExpenseFragment(navigationArgs.tripId)
            this.findNavController().navigate(action)
        }

        binding.editButton.setOnClickListener {
            val action = TripDetailFragmentDirections.actionTripDetailFragmentToEditTripFragment(navigationArgs.tripId)
            this.findNavController().navigate(action)
        }

        //register the items in recylcer view for context menu (long click menu)
        registerForContextMenu(binding.recyclerView)

        //setting up menu search item
        val menuHost: MenuHost = requireActivity()
        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.menu_trip, menu)
                    val searchItem = menu.findItem(R.id.action_search)
                    val searchView = searchItem.actionView as SearchView?
                    searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            if (query != null) {
                                adapter.filter.filter(query)
                            }
                            return true
                        }

                        //this is where we're going to implement the filter logic
                        override fun onQueryTextChange(newText: String?): Boolean {
                            if (newText != null) {
                                adapter.filter.filter(newText)
                            }
                            return true
                        }
                    })
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Handle the menu selection. getting search view of our item.
                    return when (menuItem.itemId) {
                        R.id.action_search -> {
                            false
                        }
                        else -> false
                    }
                }
            },
            // this line makes the menu item lifecycle aware
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }
}