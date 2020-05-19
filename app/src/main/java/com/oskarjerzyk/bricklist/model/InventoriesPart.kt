package com.oskarjerzyk.bricklist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InventoriesParts")
data class InventoriesPart(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "InventoryID") val inventoryId: Int,
    @ColumnInfo(name = "TypeID") val typeId: Int,
    @ColumnInfo(name = "ItemID") val itemId: String,
    @ColumnInfo(name = "QuantityInSet") val quantityInSet: Int,
    @ColumnInfo(name = "QuantityInStore") val quantityInStore: Int = 0,
    @ColumnInfo(name = "ColorID") val colorId: Int,
    @ColumnInfo(name = "Extra") val extra: String
)