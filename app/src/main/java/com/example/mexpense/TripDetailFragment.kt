package com.example.mexpense

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.mexpense.data.trip.Trip

class TripDetailFragment : Fragment() {

    lateinit var trip: Trip
    private val viewModel: TripViewModel by activityViewModels{
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }

    private val navigationArgs: TripDetailFragmentArgs by navArgs()
}