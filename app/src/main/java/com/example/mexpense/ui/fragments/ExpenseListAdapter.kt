package com.example.mexpense.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mexpense.data.expense.Expense
import com.example.mexpense.databinding.ExpenseListItemBinding

class ExpenseListAdapter(private val OnItemClicked: (Expense) -> Unit ):
    ListAdapter<Expense, ExpenseListAdapter.ExpenseViewHolder>(DiffCallback){

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
}