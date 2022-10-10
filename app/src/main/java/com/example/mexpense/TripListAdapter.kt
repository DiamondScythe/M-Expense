package com.example.mexpense

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.TripListItemBinding

/**
 * [ListAdapter] implementation for the recyclerview.
 */

class TripListAdapter(private val onItemClicked: (Trip) -> Unit) :
    ListAdapter<Trip, TripListAdapter.TripViewHolder>(DiffCallback) {

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
}
