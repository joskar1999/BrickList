package com.oskarjerzyk.bricklist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.model.BrickItemListModel
import kotlinx.android.synthetic.main.brick_item_list_cell.view.*

class BrickListAdapter(
    private val items: ArrayList<BrickItemListModel>,
    private val context: Context
) :
    RecyclerView.Adapter<BrickListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrickListViewHolder {
        return BrickListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.brick_item_list_cell, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BrickListViewHolder, position: Int) {
        holder.brickName.text = items[position].brickName
        holder.brickCode.text = items[position].brickCode
        holder.brickAmount.text = items[position].brickAmount
        holder.minusButton.setOnClickListener {

        }
        holder.addButton.setOnClickListener {

        }
        Glide.with(context)
            .load("https://images.genius.com/c745ae8eec9dd6000f52a07aa84e4457.1000x1000x1.jpg")
            .into(holder.image)
    }
}

class BrickListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.brick_image
    val brickName: TextView = view.brick_name
    val brickCode: TextView = view.brick_code
    val brickAmount: TextView = view.brick_amount
    val minusButton: Button = view.minus_brick_btn
    val addButton: Button = view.plus_brick_btn
}