package com.oskarjerzyk.bricklist.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.adapter.InventoryListAdapter
import com.oskarjerzyk.bricklist.dao.InventoryDao
import com.oskarjerzyk.bricklist.model.Inventory
import com.oskarjerzyk.bricklist.util.BrickListDatabase
import com.oskarjerzyk.bricklist.util.BrickListDatabase.Companion.getInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var database: BrickListDatabase? = null
    private var inventoryDao: InventoryDao? = null
    private var items: List<Inventory> = ArrayList()
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        database = getInstance(context = this)
        inventoryDao = database?.inventoryDao()

        inventories_list.layoutManager = LinearLayoutManager(this)
        inventories_list.adapter = InventoryListAdapter(items as ArrayList<Inventory>, this)
        inventories_list.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        fab.setOnClickListener {
            val newProjectIntent = Intent(this, NewProjectActivity::class.java)
            startActivity(newProjectIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        Observable.fromCallable {
            getInventories()
            runOnUiThread {
                inventories_list.adapter = InventoryListAdapter(items as ArrayList<Inventory>, this)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun getInventories() {
        items = inventoryDao?.findAll() ?: ArrayList()
        if (!sharedPreferences?.getBoolean("show_archived", true)!!) {
            items = items.filter { item -> item.active == 1 }
        }
    }
}
