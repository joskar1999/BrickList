package com.oskarjerzyk.bricklist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.oskarjerzyk.bricklist.model.Inventory

@Dao
interface InventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInventory(inventory: Inventory): Long

    @Query("SELECT * FROM Inventories")
    fun findAll(): List<Inventory>

    @Query("UPDATE Inventories SET Active = :state WHERE Name = :name")
    fun toggleInventoryState(name: String, state: Int)

    @Query("SELECT * FROM Inventories WHERE Id = :id")
    fun getInventoryById(id: Int): Inventory
}