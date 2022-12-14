package com.example.mexpense.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.databinding.ExpenseListItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExpenseListAdapter(val parentFragment: TripDetailFragment):
    ListAdapter<Expense, ExpenseListAdapter.ExpenseViewHolder>(DiffCallback), Filterable{

    private var expenseList = mutableListOf<Expense>()

    //Define the TripViewHolder class, extend it from RecyclerView.ViewHolder.
    //Override the bind() function, pass in the Trip object.
    class ExpenseViewHolder(private var binding: ExpenseListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            binding.apply {
                expenseName.text = expense.expenseName
                expenseAmount.text = expense.expenseAmount.toString()
            }
        }
    }

    //the ListAdapter will use diffcallback to figure out what changed in the list.
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Expense>() {
            override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                return oldItem.expenseName == newItem.expenseName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        //returns a new ExpenseViewHolder when RecyclerView needs one.
        return ExpenseViewHolder(
            ExpenseListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val current = getItem(position)
        val expenseId = current.expenseId

        //sets onclick event for the current viewholder
        holder.itemView.setOnClickListener{
            onItemClicked(expenseId)
        }

        //sets up the long-press context menu for this view holder.
        holder.itemView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu.add("View details").setOnMenuItemClickListener {
                onItemClicked(expenseId)
                true
            }
            contextMenu.add("Edit").setOnMenuItemClickListener {
                navigateToEdit(expenseId)
                true
            }
            contextMenu.add("Delete").setOnMenuItemClickListener {
                showConfirmDialog(expenseId)
                true
            }
        }
        holder.bind(current)
    }

    private fun onItemClicked(expenseId: Int){
        val action = TripDetailFragmentDirections.actionTripDetailFragmentToExpenseDetailFragment(expenseId)
        parentFragment.findNavController().navigate(action)
    }

    //confirm dialog before deletino
    private fun showConfirmDialog(expenseId: Int) {
        MaterialAlertDialogBuilder(parentFragment.requireContext())
            .setTitle("Expense deletion")
            .setMessage("Are you sure you want to delete this expense?")
            .setCancelable(true)
            .setNegativeButton("No") { dialog , _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Yes") { dialog , _ ->
                GlobalScope.launch {
                    deleteItem(expenseId)
                }
            }
            .show()
    }

    private suspend fun deleteItem(expenseId: Int) {
        parentFragment.deleteStuff(expenseId)
    }

    private fun navigateToEdit(expenseId: Int) {
        val action = TripDetailFragmentDirections.actionTripDetailFragmentToEditExpenseFragment(
            expenseId, "TripDetailFragment")
        parentFragment.findNavController().navigate(action)
    }

    //used in TripDetailFragment to set the data to the recycler view
    fun setData(list: List<Expense>?){
        this.expenseList = (list as MutableList<Expense>?)!!
        submitList(list)
    }

    //overriding getFilter() to create a custom filter (for search)
    override fun getFilter(): Filter {
        return customFilter
    }

    //the custom filter used to filter the current expense list using the characters in the searchView
    //returns a filterList of type FilterResults
    private val customFilter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Expense>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(expenseList)
            } else {
                for (item in expenseList) {
                    if (constraint.toString().lowercase() in item.expenseName.lowercase()) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        //after results are returned, it will be submitted to the recycler view
        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Expense>)
        }
    }
}