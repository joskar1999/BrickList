package com.oskarjerzyk.bricklist.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.adapter.BrickListAdapter
import com.oskarjerzyk.bricklist.dao.*
import com.oskarjerzyk.bricklist.model.BrickItemListModel
import com.oskarjerzyk.bricklist.model.InventoryExportModel
import com.oskarjerzyk.bricklist.model.ItemExportModel
import com.oskarjerzyk.bricklist.util.BrickListDatabase
import com.oskarjerzyk.bricklist.util.BrickListDatabase.Companion.getInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_lego_set.*
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.collections.ArrayList

class LegoSetActivity : AppCompatActivity() {

    private var database: BrickListDatabase? = null
    private var inventoriesPartDao: InventoriesPartDao? = null
    private var partDao: PartDao? = null
    private var colorDao: ColorDao? = null
    private var codeDao: CodeDao? = null
    private var itemTypeDao: ItemTypeDao? = null
    private var inventoryDao: InventoryDao? = null
    private val xmlMapper = XmlMapper()
    private var items: ArrayList<BrickItemListModel> = ArrayList()
    private var inventoryId: Int = 0
    private val primaryRequestUrl = "https://www.lego.com/service/bricks/5/2/"
    private val secondaryRequestUrl = "http://img.bricklink.com/P/"
    private val tertiaryRequestUrl = "https://www.bricklink.com/PL/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lego_set)

        database = getInstance(context = this)
        inventoriesPartDao = database?.inventoriesPartDao()
        partDao = database?.partDao()
        colorDao = database?.colorDao()
        codeDao = database?.codeDao()
        itemTypeDao = database?.itemTypeDao()
        inventoryDao = database?.inventoryDao()
        inventoryId = intent.getIntExtra("inventoryId", 0)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        xmlMapper.setDefaultUseWrapper(false)

        brick_list.layoutManager = LinearLayoutManager(this)
        brick_list.adapter = BrickListAdapter(items, this)
        brick_list.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onResume() {
        super.onResume()
        Observable.fromCallable {
            getInventoryBricks()
            runOnUiThread { brick_list.adapter = BrickListAdapter(items, this) }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.project_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            Observable.fromCallable {
                exportToXml()
                runOnUiThread {
                    Toast.makeText(this, "Exported to xml", Toast.LENGTH_LONG).show()
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun exportToXml() {
        val exportItems: ArrayList<ItemExportModel> = ArrayList()
        items.filter { item -> item.brickAmount != item.brickCurrentAmount }
            .forEach {
                exportItems.add(
                    ItemExportModel(
                        itemType = getItemType(it.typeId),
                        itemId = it.itemId,
                        color = getColorCode(it.brickColor),
                        qtyFilled = it.brickAmount - it.brickCurrentAmount
                    )
                )
            }
        val inventoryExportModel = InventoryExportModel(item = exportItems)
        val xmlOutput = mapXmlOutputToUpperCase(xmlMapper.writeValueAsString(inventoryExportModel))
        writeDataToXmlFile(xmlOutput, getInventoryName(inventoryId))
    }

    private fun getInventoryBricks() {
        val inventoriesParts = inventoriesPartDao?.getItemsByInventoryId(inventoryId)
        inventoriesParts?.forEach {
            items.add(
                BrickItemListModel(
                    inventoryId = it.inventoryId,
                    itemId = it.itemId,
                    primaryImagePath = "$primaryRequestUrl${getBrickCode(it.itemId, it.colorId)}",
                    secondaryImagePath = "$secondaryRequestUrl${it.colorId}/${it.itemId}.jpg",
                    tertiaryImagePath = "$tertiaryRequestUrl${it.itemId}.jpg",
                    brickName = getBrickName(it.itemId),
                    brickColor = getBrickColorName(it.colorId),
                    brickAmount = it.quantityInSet,
                    brickCurrentAmount = it.quantityInStore,
                    typeId = it.typeId
                )
            )
        }
    }

    private fun writeDataToXmlFile(data: String, fileName: String) {
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val inputSource = InputSource(StringReader(data))

        val path = this.filesDir
        val outDir = File(path, "output")
        outDir.mkdir()
        val file = File(outDir, "$fileName.xml")

        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transformer.transform(DOMSource(documentBuilder.parse(inputSource)), StreamResult(file))
    }

    private fun mapXmlOutputToUpperCase(output: String): String {
        val openingTagRegex = "<[A-Za-z]+>".toRegex()
        val closingTagRegex = "</[A-Za-z]+>".toRegex()
        return output.replace(openingTagRegex) { res -> res.value.toUpperCase(Locale.ROOT) }
            .replace(closingTagRegex) { res -> res.value.toUpperCase(Locale.ROOT) }
    }

    private fun getBrickName(itemId: String): String {
        return partDao?.getPartByCode(itemId)?.name ?: ""
    }

    private fun getBrickColorName(colorCode: Int): String {
        return colorDao?.getColorByCode(colorCode)?.name ?: ""
    }

    private fun getBrickCode(itemCode: String, colorId: Int): Int {
        return codeDao?.getCodeByItemIdAndColor(itemCode, colorId)?.code ?: -1
    }

    private fun getItemType(typeId: Int): String {
        return itemTypeDao?.getItemTypeByTypeId(typeId)?.code ?: ""
    }

    private fun getColorCode(color: String): Int {
        return colorDao?.getColorByName(color)?.code ?: 0
    }

    private fun getInventoryName(id: Int): String {
        return inventoryDao?.getInventoryById(id)?.name ?: ""
    }
}
