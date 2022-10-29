package com.example.mexpense

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.TripListItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * [ListAdapter] implementation for the recyclerview.
 */

class TripListAdapter(val parentFragment: ViewDataFragment) :
    ListAdapter<Trip, TripListAdapter.TripViewHolder>(DiffCallback), Filterable {

    private var tripList = mutableListOf<Trip>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        //returns a new TripViewHolder when RecyclerView needs one.
        return TripViewHolder(
            TripListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        //Get the current item using the method getItem(), passing the position.
        val current = getItem(position)
        val tripId = current.tripId
        holder.itemView.setOnClickListener {
            onItemClicked(tripId)
        }
        holder.itemView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu.add("View details").setOnMenuItemClickListener {
                onItemClicked(tripId)
                true
            }
            contextMenu.add("Edit").setOnMenuItemClickListener {
                onItemClicked(tripId)
                true
            }
            contextMenu.add("Delete").setOnMenuItemClickListener {
                showConfirmDialog(tripId)
                true
            }
        }
        holder.bind(current)
    }

    private fun showConfirmDialog(tripId: Int) {
        MaterialAlertDialogBuilder(parentFragment.requireContext())
            .setTitle("Record deletion")
            .setMessage("do u really wanna delete this?")
            .setCancelable(false)
            .setNegativeButton("No") { dialog , _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog , _ ->
                //band-aid solution. Needs replacement
                GlobalScope.launch {
                    deleteItem(tripId)
                }
            }
            .show()
    }

    private suspend fun deleteItem(tripId: Int) {
        parentFragment.deleteStuff(tripId)
    }

    fun setData(list: List<Trip>?){
        this.tripList = (list as MutableList<Trip>?)!!
        submitList(list)
    }

    private fun onItemClicked(tripId: Int){
        val action = ViewDataFragmentDirections.actionViewDataFragmentToTripDetailFragment(tripId)
        parentFragment.findNavController().navigate(action)
    }

    //Define the TripViewHolder class, extend it from RecyclerView.ViewHolder.
    //Override the bind() function, pass in the Trip object.
    class TripViewHolder(private var binding: TripListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trip: Trip) {
            binding.apply {
                tripLocation.text = trip.tripLocation
                tripTime.text = trip.tripTime
            }
        }
    }

    //the ListAdapter will use diffcallback to figure out what changed in the list.
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Trip>() {
            override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
                return oldItem.tripLocation == newItem.tripLocation
            }
        }
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Trip>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(tripList)
            } else {
                for (item in tripList) {
                    if (constraint.toString().lowercase() in item.tripLocation.lowercase()) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Trip>)
        }

    }

}

