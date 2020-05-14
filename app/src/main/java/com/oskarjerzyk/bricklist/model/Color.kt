package com.oskarjerzyk.bricklist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Colors")
data class Color(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "Code") val code: Int,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "NamePL") val namePl: String?
)