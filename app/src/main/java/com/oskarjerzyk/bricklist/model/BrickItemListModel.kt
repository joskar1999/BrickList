package com.oskarjerzyk.bricklist.model

data class BrickItemListModel(
    val inventoryId: Int,
    val itemId: String,
    val imagePath: String,
    val brickName: String,
    val brickColor: String,
    val brickAmount: Int,
    var brickCurrentAmount: Int
)