package com.oskarjerzyk.bricklist.dao

import androidx.room.Dao
import androidx.room.Query
import com.oskarjerzyk.bricklist.model.ItemType

@Dao
interface ItemTypeDao {

    @Query("SELECT * FROM ItemTypes")
    fun findAll(): List<ItemType>

    @Query("SELECT id FROM ItemTypes WHERE Code = :code")
    fun getItemTypeIdByCode(code: String): Int
}