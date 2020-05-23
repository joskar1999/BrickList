package com.oskarjerzyk.bricklist.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.dao.InventoriesPartDao
import com.oskarjerzyk.bricklist.dao.InventoryDao
import com.oskarjerzyk.bricklist.dao.ItemTypeDao
import com.oskarjerzyk.bricklist.model.InventoriesPart
import com.oskarjerzyk.bricklist.model.Inventory
import com.oskarjerzyk.bricklist.util.BrickListDatabase
import com.oskarjerzyk.bricklist.util.InventoryXML
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_new_project.*

class NewProjectActivity : AppCompatActivity() {

    private var responseData: String? = null
    private var sharedPreferences: SharedPreferences? = null
    private val xmlMapper = XmlMapper()
    private var inventoryXML: InventoryXML? = null
    private var inventoryDao: InventoryDao? = null
    private var inventoriesPartDao: InventoriesPartDao? = null
    private var itemTypeDao: ItemTypeDao? = null
    private var database: BrickListDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_project)

        xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        xmlMapper.setDefaultUseWrapper(false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        database = BrickListDatabase.getInstance(context = this)
        inventoryDao = database?.inventoryDao()
        inventoriesPartDao = database?.inventoriesPartDao()
        itemTypeDao = database?.itemTypeDao()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        check_set_button.setOnClickListener {
            getLegoSetXml(new_set_id.text.toString())
        }
        add_set_button.setOnClickListener {
            Observable.fromCallable {
                if (!checkIfInventoryExists(new_set_name.text.toString())) {
                    val inventoryId: Long = saveInventory()
                    saveInventoryItems(inventoryId)
                    runOnUiThread {
                        Toast.makeText(this, "Set added to inventory", Toast.LENGTH_LONG).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Inventory with given name already exists",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getLegoSetXml(legoSetId: String) {
        val urlPrefix = sharedPreferences?.getString("url_prefix", "")
        val httpRequest = "$urlPrefix/$legoSetId.xml".httpGet()
            .responseString { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        responseData = result.get()
                        inventoryXML = xmlMapper.readValue(responseData, InventoryXML::class.java)
                        runOnUiThread {
                            Toast.makeText(this, "You can add this set", Toast.LENGTH_LONG).show()
                            add_set_button.isEnabled = true
                        }
                    }
                    is Result.Failure -> {
                        runOnUiThread {
                            Toast.makeText(this, "You cannot add this set", Toast.LENGTH_LONG)
                                .show()
                            add_set_button.isEnabled = false
                        }
                        inventoryXML = null
                    }
                }
            }
        httpRequest.join()
    }

    private fun saveInventory(): Long {
        var inventoryId: Long? = 0
        if (new_set_name.text.toString().isNotEmpty()) {
            inventoryId = inventoryDao?.insertInventory(
                Inventory(
                    0,
                    new_set_name.text.toString(),
                    1,
                    (System.currentTimeMillis() / 1000L).toInt()
                )
            )
        }
        return inventoryId!!
    }

    private fun saveInventoryItems(inventoryId: Long) {
        if (new_set_name.text.toString().isNotEmpty()) {
            inventoryXML?.items?.forEach {
                val typeId = itemTypeDao?.getItemTypeIdByCode(it.itemType)
                inventoriesPartDao?.insertInventoriesPart(
                    InventoriesPart(
                        inventoryId = inventoryId.toInt(),
                        colorId = it.color,
                        extra = it.extra,
                        quantityInSet = it.quantity,
                        itemId = it.itemId,
                        typeId = typeId!!
                    )
                )
            }
        }
    }

    private fun checkIfInventoryExists(name: String): Boolean {
        return inventoryDao?.findAll()?.any { item -> item.name == name.trimEnd().trimStart() }!!
    }
}
