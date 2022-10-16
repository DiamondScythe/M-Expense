package com.example.mexpense

import android.provider.Contacts
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.TripListItemBinding
import java.util.*

/**
 * [ListAdapter] implementation for the recyclerview.
 */

class TripListAdapter(private val onItemClicked: (Trip) -> Unit) :
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
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    fun setData(list: List<Trip>?){
        this.tripList = (list as MutableList<Trip>?)!!
        submitList(list)
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

