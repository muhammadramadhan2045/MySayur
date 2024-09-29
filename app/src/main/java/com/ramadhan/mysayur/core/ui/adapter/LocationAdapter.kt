package com.ramadhan.mysayur.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramadhan.mysayur.core.domain.model.LocationTracker
import com.ramadhan.mysayur.databinding.ItemLocationBinding

class LocationAdapter : ListAdapter<LocationTracker, LocationAdapter.LocationViewHolder>(DIFF_CALLBACK){

    class LocationViewHolder (
        private val binding : ItemLocationBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(location: LocationTracker){
            with(binding){
                tvLatitude.text = location.latitude.toString()
                tvLongitude.text = location.longitude.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocationTracker>(){
            override fun areItemsTheSame(oldItem: LocationTracker, newItem: LocationTracker): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: LocationTracker, newItem: LocationTracker): Boolean {
                return oldItem == newItem
            }
        }
    }
}