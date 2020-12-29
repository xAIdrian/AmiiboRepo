package com.amohnacs.amiiborepo.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.amohnacs.amiiborepo.R
import com.amohnacs.amiiborepo.databinding.FragmentMainItemBinding
import com.amohnacs.amiiborepo.model.Amiibo
import com.bumptech.glide.Glide
import javax.inject.Inject

class AmiiboAdapter @Inject constructor() : PagingDataAdapter<Amiibo, RecyclerView.ViewHolder>(COMPARATOR_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(FragmentMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as ViewHolder).bind(it)
        }
    }

    inner class ViewHolder(view: ViewBinding) : RecyclerView.ViewHolder(view.root) {
        private val binding = view as FragmentMainItemBinding

        fun bind(amiibo: Amiibo) {
            Glide.with(binding.root)
                .load(amiibo.image)
                .centerCrop()
                .into(binding.image)
            binding.name.text = amiibo.name
            binding.series.text = amiibo.amiiboSeries
            binding.game.text = amiibo.gameSeries
        }
    }

    companion object {
        val COMPARATOR_DIFF = object : DiffUtil.ItemCallback<Amiibo>() {
            override fun areItemsTheSame(oldItem: Amiibo, newItem: Amiibo): Boolean {
                return oldItem.tail == newItem.tail
            }

            override fun areContentsTheSame(oldItem: Amiibo, newItem: Amiibo): Boolean {
                return oldItem == newItem
            }
        }
    }
}