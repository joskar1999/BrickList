package com.oskarjerzyk.bricklist.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

data class ItemExportModel(
    @JacksonXmlProperty(localName = "ITEMTYPE") val itemType: String,
    @JacksonXmlProperty(localName = "ITEMID") val itemId: String,
    @JacksonXmlProperty(localName = "COLOR") val color: Int,
    @JacksonXmlProperty(localName = "QTYFILLED") val qtyFilled: Int
)

@JacksonXmlRootElement(localName = "INVENTORY")
data class InventoryExportModel(

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "ITEM")
    val item: List<ItemExportModel>
)