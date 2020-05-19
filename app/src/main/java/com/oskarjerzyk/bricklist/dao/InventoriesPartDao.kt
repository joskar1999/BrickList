package com.oskarjerzyk.bricklist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oskarjerzyk.bricklist.model.InventoriesPart

@Dao
interface InventoriesPartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInventoriesPart(inventoriesPart: InventoriesPart)

    @Query("SELECT * FROM InventoriesParts")
    fun findAll(): List<InventoriesPart>

    @Query("SELECT * FROM InventoriesParts WHERE InventoryID = :inventoryId")
    fun getItemsByInventoryId(inventoryId: Int): List<InventoriesPart>

    @Query("UPDATE InventoriesParts SET QuantityInStore = :quantity WHERE InventoryID = :inventoryId AND ItemID = :itemId")
    fun updateCurrentQuantity(inventoryId: Int, itemId: String, quantity: Int)
}