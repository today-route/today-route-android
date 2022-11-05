package com.maru.todayroute.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maru.data.model.Route
import com.maru.data.model.SimpleRoute
import com.maru.data.network.response.RouteOfMonthResponse
import com.maru.todayroute.R
import com.maru.todayroute.databinding.ItemRouteBinding

class RouteListAdapter: ListAdapter<SimpleRoute, RouteListAdapter.RouteViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_route, parent, false))
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RouteViewHolder(val binding: ItemRouteBinding) : RecyclerView.ViewHolder(binding.root) {

        private var currentRouteId = -1

        init {
            binding.root.setOnClickListener {
                moveToRouteFragment(currentRouteId)
            }
        }

        fun bind(route: SimpleRoute) {
            currentRouteId = route.id
            with (binding) {
                tvLocation.text = route.location
                tvRouteTitle.text = route.title
            }
        }

        private fun moveToRouteFragment(routeId: Int) {
            binding.root.findNavController().navigate(CalendarFragmentDirections.actionCalendarFragmentToRouteFeragment(routeId))
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<SimpleRoute> = object : DiffUtil.ItemCallback<SimpleRoute>() {
            override fun areItemsTheSame(oldItem: SimpleRoute, newItem: SimpleRoute): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: SimpleRoute, newItem: SimpleRoute): Boolean =
                oldItem == newItem
        }
    }
}