package com.oskarjerzyk.bricklist.model

data class BrickItemListModel(
    val imagePath: String,
    val brickName: String,
    val brickCode: String,
    val brickAmount: Int,
    val brickCurrentAmount: Int
)