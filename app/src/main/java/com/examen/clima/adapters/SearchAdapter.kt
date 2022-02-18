package com.examen.clima.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.examen.clima.R
import com.examen.clima.entities.Search
import com.examen.clima.listener.SearchListener
import com.examen.clima.utils.inflate
import kotlinx.android.synthetic.main.item_search.view.*

class SearchAdapter(private val data: ArrayList<Search>, private val listener: SearchListener) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.item_search))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position], listener)

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(search: Search, listener: SearchListener) = with(itemView) {

            tvLocation.text = search.name
            tvRegionCountry.text = "${search.region}, ${search.country}"

            container.setOnClickListener { listener.onClick(search) }
        }
    }

}