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
import com.oskarjerzyk.bricklist.dao.InventoriesPartDao
import com.oskarjerzyk.bricklist.model.BrickItemListModel
import com.oskarjerzyk.bricklist.util.BrickListDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.brick_item_list_cell.view.*

class BrickListAdapter(
    private val items: ArrayList<BrickItemListModel>,
    private val context: Context
) :
    RecyclerView.Adapter<BrickListViewHolder>() {

    private val database = BrickListDatabase.getInstance(context)
    private val inventoriesPartDao: InventoriesPartDao? = database?.inventoriesPartDao()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrickListViewHolder {
        return BrickListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.brick_item_list_cell, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BrickListViewHolder, position: Int) {
        val item = items[position]
        holder.brickName.text = item.brickName
        holder.brickCode.text = item.brickColor
        holder.brickAmount.text =
            "${item.brickCurrentAmount} of ${item.brickAmount}"
        holder.minusButton.setOnClickListener {
            if (item.brickCurrentAmount > 0) {
                item.brickCurrentAmount = item.brickCurrentAmount - 1
                updateBrickQuantity(
                    item.inventoryId,
                    item.itemId,
                    item.brickCurrentAmount,
                    item.brickAmount,
                    holder
                )
            }
        }
        holder.addButton.setOnClickListener {
            if (item.brickCurrentAmount < item.brickAmount) {
                item.brickCurrentAmount = item.brickCurrentAmount + 1
                updateBrickQuantity(
                    item.inventoryId,
                    item.itemId,
                    item.brickCurrentAmount,
                    item.brickAmount,
                    holder
                )
            }
        }
        Glide.with(context)
            .load("https://images.genius.com/c745ae8eec9dd6000f52a07aa84e4457.1000x1000x1.jpg")
            .into(holder.image)
    }

    private fun updateBrickQuantity(
        inventoryId: Int,
        itemId: String,
        curQuantity: Int,
        quantity: Int,
        holder: BrickListViewHolder
    ) {
        Observable.fromCallable {
            inventoriesPartDao?.updateCurrentQuantity(inventoryId, itemId, curQuantity)
            holder.brickAmount.text =
                "$curQuantity of $quantity"
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
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