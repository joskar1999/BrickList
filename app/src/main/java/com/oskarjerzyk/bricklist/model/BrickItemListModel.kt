package com.oskarjerzyk.bricklist.model

data class BrickItemListModel(
    val inventoryId: Int,
    val itemId: String,
    val primaryImagePath: String,
    val secondaryImagePath: String,
    val tertiaryImagePath: String,
    val brickName: String,
    val brickColor: String,
    val brickAmount: Int,
    var brickCurrentAmount: Int,
    val typeId: Int
)