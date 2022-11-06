package com.example.mexpense.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.ExpenseListItemBinding

class ExpenseListAdapter(private val OnItemClicked: (Expense) -> Unit ):
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
        holder.itemView.setOnClickListener{
            OnItemClicked(current)
        }
        holder.bind(current)
    }

    fun setData(list: List<Expense>?){
        this.expenseList = (list as MutableList<Expense>?)!!
        submitList(list)
    }

    override fun getFilter(): Filter {
        return customFilter
    }

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

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<Expense>)
        }
    }
}