package io.yoobi.pagingbug

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.yoobi.pagingbug.databinding.ItemTopBinding

class TopAdapter: RecyclerView.Adapter<TopAdapter.TopRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRecyclerViewHolder =
        TopRecyclerViewHolder.from(parent)

    override fun onBindViewHolder(holder: TopRecyclerViewHolder, position: Int) {}

    override fun getItemCount(): Int = 1

    class TopRecyclerViewHolder(binding: ItemTopBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup) = TopRecyclerViewHolder(
                ItemTopBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
            )
        }
    }
}

