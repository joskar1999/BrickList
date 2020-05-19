package com.oskarjerzyk.bricklist.dao

import androidx.room.Dao
import androidx.room.Query
import com.oskarjerzyk.bricklist.model.Part

@Dao
interface PartDao {

    @Query("SELECT * FROM Parts WHERE Code = :code")
    fun getPartByCode(code: String): Part
}