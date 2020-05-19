package com.oskarjerzyk.bricklist.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.activities.LegoSetActivity
import com.oskarjerzyk.bricklist.model.Inventory
import kotlinx.android.synthetic.main.inventory_item_list_cell.view.*

class InventoryListAdapter(private val items: ArrayList<Inventory>, private val context: Context) :
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
        holder.inventoryItem.text = items[position].name
        holder.isArchived.isChecked = items[position].active == 0
        holder.listItemLayout.setOnClickListener {
            val setIntent = Intent(context, LegoSetActivity::class.java)
            setIntent.putExtra("setId", items[position].id)
            holder.inventoryItem.context.startActivity(setIntent)
        }
    }
}

class InventoryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val inventoryItem: TextView = view.inventory_item_name
    val isArchived: Switch = view.archive_switch
    val listItemLayout: LinearLayout = view.inventory_list_item_layout
}