package com.example.mexpense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mexpense.databinding.FragmentEnterTripBinding
import com.example.mexpense.databinding.FragmentViewDataBinding
import com.google.firebase.database.FirebaseDatabase


class ViewDataFragment : Fragment() {
    private var _binding: FragmentViewDataBinding? = null
    private val binding get() = _binding!!

//Use by delegate to hand off the property initialization to the activityViewModels class. Pass in the InventoryViewModelFactory constructor.
    private val viewModel: TripViewModel by activityViewModels{
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TripListAdapter {
        }
        binding.recyclerView.adapter = adapter
        //listens to data changes
        viewModel.allTrips.observe(this.viewLifecycleOwner) { trips ->
            //This will update the RecyclerView with the new items on the list.
            trips.let {
                adapter.submitList(it)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }
}