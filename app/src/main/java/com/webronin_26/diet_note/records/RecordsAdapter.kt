package com.webronin_26.diet_note.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.webronin_26.diet_note.data.Record
import com.webronin_26.diet_note.databinding.RecordItemBinding

class RecordsAdapter(private val viewModel: RecordsViewModel) :
    PagedListAdapter<Record, RecordsAdapter.RecordsViewHolder>(RecordsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsViewHolder {
        return RecordsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecordsViewHolder, position: Int) {

        val item = getItem(position)

        if(item != null) { holder.bind(viewModel, item) }
    }

    class RecordsViewHolder private constructor(private val binding: RecordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: RecordsViewModel, item: Record) {
            binding.viewmodel = viewModel
            binding.record = item

            binding.recordItemDeleteImageView.setOnClickListener { viewModel.deleteRecord(item) }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecordsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecordItemBinding.inflate(layoutInflater, parent, false)

                return RecordsViewHolder(binding)
            }
        }
    }
}

class RecordsDiffCallback : DiffUtil.ItemCallback<Record>() {
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }
}