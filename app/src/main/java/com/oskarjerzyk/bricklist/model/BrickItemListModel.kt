package com.oskarjerzyk.bricklist.model

data class BrickItemListModel(
    val imagePath: String,
    val brickName: String,
    val brickColor: String,
    val brickAmount: Int,
    val brickCurrentAmount: Int
)