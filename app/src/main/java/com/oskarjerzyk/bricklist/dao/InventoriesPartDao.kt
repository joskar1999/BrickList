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
}