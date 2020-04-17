package com.webronin_26.diet_note.records

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.webronin_26.diet_note.data.Record

@BindingAdapter("app:items")
fun setItems(recyclerView: RecyclerView, items: PagedList<Record>?) {
    items?.let {
        (recyclerView.adapter as RecordsAdapter).submitList(items)
    }
}

@BindingAdapter("app:name")
fun setItemName(appCompatTextView: AppCompatTextView, string: String?) {
    string?.let {
        appCompatTextView.text = string
    }
}

@BindingAdapter("app:itemValue")
fun setItemValue(appCompatTextView: AppCompatTextView, int: Int?) {
    int?.let {
        appCompatTextView.text = int.toString()
    }
}
