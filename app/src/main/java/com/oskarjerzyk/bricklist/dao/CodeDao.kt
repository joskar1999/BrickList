package com.oskarjerzyk.bricklist.dao

import androidx.room.Dao
import androidx.room.Query
import com.oskarjerzyk.bricklist.model.Code

@Dao
interface CodeDao {

    @Query("SELECT * FROM Codes WHERE ItemID = (SELECT Id FROM Parts WHERE Code = :itemId) AND ColorID = :colorId")
    fun getCodeByItemIdAndColor(itemId: String, colorId: Int): Code
}