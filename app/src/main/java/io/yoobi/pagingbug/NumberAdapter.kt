package io.yoobi.pagingbug

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class NumberAdapter : PagingDataAdapter<Int, LaunchViewHolder>(LaunchDiffItemCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LaunchViewHolder(layoutInflater.inflate(R.layout.item_paging, parent, false))
    }

    override fun onBindViewHolder(holder: LaunchViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }
}

class LaunchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(number: Int) {
        itemView.findViewById<TextView>(R.id.textview).text = "$number"
    }
}

private object LaunchDiffItemCallback : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }
}