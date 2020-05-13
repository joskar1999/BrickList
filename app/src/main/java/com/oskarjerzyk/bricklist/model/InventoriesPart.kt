package com.oskarjerzyk.bricklist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InventoriesPart(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "InventoryID") val inventoryId: Int,
    @ColumnInfo(name = "TypeID") val typeId: Int,
    @ColumnInfo(name = "ItemID") val itemId: Int,
    @ColumnInfo(name = "QuantityInSet") val quantityInSet: Int,
    @ColumnInfo(name = "QuantityInStore") val quantityInStore: Int,
    @ColumnInfo(name = "ColorID") val colorId: Int,
    @ColumnInfo(name = "Extra") val extra: Int
)