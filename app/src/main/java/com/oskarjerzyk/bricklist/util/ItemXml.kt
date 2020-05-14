package com.oskarjerzyk.bricklist.util

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

data class ItemXml(
    @JacksonXmlProperty(localName = "ITEMTYPE") val itemType: String,
    @JacksonXmlProperty(localName = "ITEMID") val itemId: String,
    @JacksonXmlProperty(localName = "QTY") val quantity: Int,
    @JacksonXmlProperty(localName = "COLOR") val color: Int,
    @JacksonXmlProperty(localName = "EXTRA") val extra: String,
    @JacksonXmlProperty(localName = "ALTERNATE") val alternate: String,
    @JacksonXmlProperty(localName = "MATCHID") val matchId: Int,
    @JacksonXmlProperty(localName = "COUNTERPART") val counterPart: String
)