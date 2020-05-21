package com.oskarjerzyk.bricklist.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.activities.LegoSetActivity
import com.oskarjerzyk.bricklist.model.Inventory
import com.oskarjerzyk.bricklist.util.BrickListDatabase.Companion.getInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val showArchived = sharedPreferences?.getBoolean("show_archived", true)
        holder.inventoryItem.text = items[position].name
        holder.archiveSwitch.isChecked = items[position].active == 0
        holder.archiveSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                toggleInventoryState(items[position].name, 0)
                if (holder.archiveSwitch.isChecked && !showArchived!!) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                }
            } else {
                toggleInventoryState(items[position].name, 1)
            }
        }
        holder.listItemLayout.setOnClickListener {
            val setIntent = Intent(context, LegoSetActivity::class.java)
            setIntent.putExtra("inventoryId", items[position].id)
            holder.inventoryItem.context.startActivity(setIntent)
        }
    }

    private fun toggleInventoryState(name: String, state: Int) {
        val database = getInstance(context)
        val inventoryDao = database?.inventoryDao()
        Observable.fromCallable {
            inventoryDao?.toggleInventoryState(name, state)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}

class InventoryListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val inventoryItem: TextView = view.inventory_item_name
    val archiveSwitch: Switch = view.archive_switch
    val listItemLayout: LinearLayout = view.inventory_list_item_layout
}