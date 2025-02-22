package com.oskarjerzyk.bricklist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Parts")
data class Part(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "TypeID") val typeId: Int,
    @ColumnInfo(name = "Code") val code: String,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "NamePL") val namePl: String?,
    @ColumnInfo(name = "CategoryID") val categoryId: Int
)