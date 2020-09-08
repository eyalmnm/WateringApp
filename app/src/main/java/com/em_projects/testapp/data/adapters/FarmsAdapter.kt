package com.em_projects.testapp.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.em_projects.testapp.R
import com.em_projects.testapp.data.entity.FarmData
import com.em_projects.testapp.utils.listen


interface ItemClickListener {
    fun onItemClick(view: View?, position: Int)
}

class FarmsAdapter(var farms: List<FarmData>, val listener: ItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.view_farms_list_item,
            parent,
            false
        )
        return ViewHolder(view).listen { pos, type ->
            listener.onItemClick(view, pos)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as ViewHolder
        itemViewHolder.name.text = farms[position].name
        itemViewHolder.lat.text = "Lat: ${farms[position].lat}"
        itemViewHolder.lng.text = "Lng: ${farms[position].lng}"
    }

    override fun getItemCount(): Int {
        return farms.size
    }


    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameTextView)
        val lat: TextView = itemView.findViewById(R.id.latTextView)
        val lng: TextView = itemView.findViewById(R.id.lngTextView)
    }
}