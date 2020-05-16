package com.oskarjerzyk.bricklist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oskarjerzyk.bricklist.R
import kotlinx.android.synthetic.main.inventory_item_list_cell.view.*

class InventoryListAdapter(private val items: ArrayList<String>, private val context: Context) :
    RecyclerView.Adapter<InventoryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryListViewHolder {
        return InventoryListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.inventory_item_list_cell, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: InventoryListViewHolder, position: Int) {
        holder.inventoryItem.text = items[position]
    }
}

class InventoryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val inventoryItem: TextView = view.inventory_item_name
    val isArchived: Switch = view.archive_switch
}