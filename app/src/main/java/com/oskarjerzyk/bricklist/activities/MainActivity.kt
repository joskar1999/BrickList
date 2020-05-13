package com.oskarjerzyk.bricklist.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.dao.ItemTypeDao
import com.oskarjerzyk.bricklist.model.ItemType
import com.oskarjerzyk.bricklist.util.BrickListDatabase
import com.oskarjerzyk.bricklist.util.BrickListDatabase.Companion.getInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private var itemTypes: List<ItemType>? = null
    private var database: BrickListDatabase? = null
    private var itemTypeDao: ItemTypeDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Observable.fromCallable {
            database = getInstance(context = this)
            itemTypeDao = database?.itemTypeDao()
            itemTypes = itemTypeDao?.findAll()
        }.doOnNext {
            Log.i("database", "ItemTypes fetched")
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}
