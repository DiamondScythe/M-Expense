package com.example.mexpense.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mexpense.*
import com.example.mexpense.databinding.FragmentViewDataBinding


class ViewDataFragment : Fragment() {
    private var _binding: FragmentViewDataBinding? = null
    private val binding get() = _binding!!

    //Use by delegate to hand off the property initialization to the activityViewModels class. Pass in the InventoryViewModelFactory constructor.
    private val viewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }

    suspend fun deleteStuff(tripId: Int){
        viewModel.removeTrip(tripId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TripListAdapter(this)
        binding.recyclerView.adapter = adapter
        //listens to data changes
        viewModel.allTrips.observe(this.viewLifecycleOwner) { trips ->
            //This will update the RecyclerView with the new items on the list.
            trips.let {
                adapter.setData(it)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

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