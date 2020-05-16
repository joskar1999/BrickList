package com.oskarjerzyk.bricklist.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.adapter.BrickListAdapter
import com.oskarjerzyk.bricklist.model.BrickItemListModel
import kotlinx.android.synthetic.main.activity_lego_set.*

class LegoSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lego_set)

        val items: ArrayList<BrickItemListModel> = ArrayList()
        items.add(BrickItemListModel("", "Chuj", "2137JP2", "12"))
        items.add(BrickItemListModel("", "Dupa", "2137JP3", "13"))
        items.add(BrickItemListModel("", "Pizda", "2137JP4", "14"))

        brick_list.layoutManager = LinearLayoutManager(this)
        brick_list.adapter = BrickListAdapter(items, this)
    }
}
