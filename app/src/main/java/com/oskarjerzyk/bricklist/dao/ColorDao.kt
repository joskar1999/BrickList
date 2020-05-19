package com.oskarjerzyk.bricklist.dao

import androidx.room.Dao
import androidx.room.Query
import com.oskarjerzyk.bricklist.model.Color

@Dao
interface ColorDao {

    @Query("SELECT * FROM Colors WHERE Code = :code")
    fun getColorByCode(code: Int): Color
}