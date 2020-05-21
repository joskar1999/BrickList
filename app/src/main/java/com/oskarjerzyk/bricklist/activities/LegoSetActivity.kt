package com.oskarjerzyk.bricklist.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.oskarjerzyk.bricklist.R
import com.oskarjerzyk.bricklist.adapter.BrickListAdapter
import com.oskarjerzyk.bricklist.dao.CodeDao
import com.oskarjerzyk.bricklist.dao.ColorDao
import com.oskarjerzyk.bricklist.dao.InventoriesPartDao
import com.oskarjerzyk.bricklist.dao.PartDao
import com.oskarjerzyk.bricklist.model.BrickItemListModel
import com.oskarjerzyk.bricklist.util.BrickListDatabase
import com.oskarjerzyk.bricklist.util.BrickListDatabase.Companion.getInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_lego_set.*

class LegoSetActivity : AppCompatActivity() {

    private var database: BrickListDatabase? = null
    private var inventoriesPartDao: InventoriesPartDao? = null
    private var partDao: PartDao? = null
    private var colorDao: ColorDao? = null
    private var codeDao: CodeDao? = null
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
        inventoryId = intent.getIntExtra("inventoryId", 0)

        brick_list.layoutManager = LinearLayoutManager(this)
        brick_list.adapter = BrickListAdapter(items, this)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.project_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
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
                    brickCurrentAmount = it.quantityInStore
                )
            )
        }
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
}
