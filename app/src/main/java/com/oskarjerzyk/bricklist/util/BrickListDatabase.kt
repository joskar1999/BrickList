package com.oskarjerzyk.bricklist.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oskarjerzyk.bricklist.dao.*
import com.oskarjerzyk.bricklist.model.*

@Database(
    entities = [
        Category::class,
        Code::class,
        Color::class,
        InventoriesPart::class,
        Inventory::class,
        ItemType::class,
        Part::class
    ],
    version = 1
)
abstract class BrickListDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun codeDao(): CodeDao
    abstract fun colorDao(): ColorDao
    abstract fun inventoriesPartDao(): InventoriesPartDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun itemTypeDao(): ItemTypeDao
    abstract fun partDao(): PartDao

    companion object {
        private var INSTANCE: BrickListDatabase? = null

        fun getInstance(context: Context): BrickListDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    BrickListDatabase::class.java,
                    "BrickList"
                )
                    .createFromAsset("BrickList.db")
                    .build()
            }
            return INSTANCE
        }
    }
}