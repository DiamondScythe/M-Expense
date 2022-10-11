package com.example.mexpense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.FragmentTripDetailBinding

class TripDetailFragment : Fragment() {

    private var _binding: FragmentTripDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var trip: Trip
    private val viewModel: TripViewModel by activityViewModels{
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
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
        //you passed item id as a navigation argument to ItemDetailFragment from the ItemListFragment
        //Retrieve and assign the navigation argument to this new variable.
        val id = navigationArgs.tripId
    }
}