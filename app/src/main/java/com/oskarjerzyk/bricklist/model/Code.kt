package com.oskarjerzyk.bricklist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Code(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "ItemID") val itemId: Int,
    @ColumnInfo(name = "ColorID") val colorId: Int,
    @ColumnInfo(name = "Code") val code: Int,
    @ColumnInfo(name = "Image", typeAffinity = ColumnInfo.BLOB) val image: ByteArray? = null
)