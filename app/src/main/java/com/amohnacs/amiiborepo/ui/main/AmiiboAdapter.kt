package com.amohnacs.amiiborepo.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.amohnacs.amiiborepo.R
import com.amohnacs.amiiborepo.dagger.ActivityScope
import com.amohnacs.amiiborepo.databinding.FragmentMainItemBinding
import com.amohnacs.amiiborepo.model.Amiibo
import com.bumptech.glide.Glide
import javax.inject.Inject

@ActivityScope
class AmiiboAdapter @Inject constructor() : RecyclerView.Adapter<AmiiboAdapter.ViewHolder>() {

    private var values: List<Amiibo> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentMainItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: ViewBinding) : RecyclerView.ViewHolder(view.root) {
        private val binding = view as FragmentMainItemBinding
        fun bind(amiibo: Amiibo) {
            Glide.with(binding.root.context)
                .load(amiibo.image)
                .fitCenter()
                .into(binding.image)
            binding.name.text = amiibo.name
            binding.series.text = amiibo.amiiboSeries
            binding.game.text = amiibo.gameSeries
        }
    }

    fun updateItems(amiibos: List<Amiibo>) {
        values = amiibos
        notifyDataSetChanged()
    }
}