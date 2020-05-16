package com.oskarjerzyk.bricklist.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.dao.ItemTypeDao
import com.oskarjerzyk.bricklist.model.ItemType
import com.oskarjerzyk.bricklist.util.BrickListDatabase
import com.oskarjerzyk.bricklist.util.BrickListDatabase.Companion.getInstance
import com.oskarjerzyk.bricklist.util.InventoryXML
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private var itemTypes: List<ItemType>? = null
    private var database: BrickListDatabase? = null
    private var itemTypeDao: ItemTypeDao? = null
    private var responseData: String? = null
    private var inventoryXML: InventoryXML? = null
    private val xmlMapper = XmlMapper()
    private val inventoryUrl = "http://fcds.cs.put.poznan.pl/MyWeb/BL/615.xml"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        xmlMapper.setDefaultUseWrapper(false)

        Observable.fromCallable {
            database = getInstance(context = this)
            itemTypeDao = database?.itemTypeDao()
            itemTypes = itemTypeDao?.findAll()
        }.doOnNext {
            Log.i("database", "ItemTypes fetched")
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()

        val httpAsync = inventoryUrl.httpGet()
            .responseString { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        responseData = result.get()
                        inventoryXML = xmlMapper.readValue(responseData, InventoryXML::class.java)
                        Log.i("fuel", "data xml fetched")
                    }
                    is Result.Failure -> {
                        val exception = result.getException()
                        Log.i("fuel", exception.toString())
                    }
                }
            }
        httpAsync.join()
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
        R.id.action_project -> {
            val projectActivity = Intent(this, LegoSetActivity::class.java)
            startActivity(projectActivity)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
