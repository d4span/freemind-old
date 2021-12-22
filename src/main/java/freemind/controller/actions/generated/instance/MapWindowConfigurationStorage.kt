package freemind.controller.actions.generated.instance

import java.util.Collections

/* MapWindowConfigurationStorage...*/ class MapWindowConfigurationStorage : WindowConfigurationStorage() {
    /* constants from enums*/
    var mapCenterLongitude = 0.0
    var mapCenterLatitude = 0.0
    var cursorLongitude = 0.0
    var cursorLatitude = 0.0
    var zoom = 0
    var lastDividerPosition = 0
    var tileSource: String? = null
    var showMapMarker = false
    var tileGridVisible = false
    var zoomControlsVisible = false
    var searchControlVisible = false
    var hideFoldedNodes = false
    var limitSearchToVisibleArea = false
    var mapLocationStorageIndex = 0
    fun addTableColumnSetting(tableColumnSetting: TableColumnSetting?) {
        tableColumnSettingList.add(tableColumnSetting)
    }

    fun addAtTableColumnSetting(position: Int, tableColumnSetting: TableColumnSetting?) {
        tableColumnSettingList.add(position, tableColumnSetting)
    }

    fun getTableColumnSetting(index: Int): TableColumnSetting {
        return tableColumnSettingList[index] as TableColumnSetting
    }

    fun removeFromTableColumnSettingElementAt(index: Int) {
        tableColumnSettingList.removeAt(index)
    }

    fun sizeTableColumnSettingList(): Int {
        return tableColumnSettingList.size
    }

    fun clearTableColumnSettingList() {
        tableColumnSettingList.clear()
    }

    val listTableColumnSettingList: List<TableColumnSetting?>
        get() = Collections.unmodifiableList(tableColumnSettingList)
    protected var tableColumnSettingList: ArrayList<TableColumnSetting?> = ArrayList()
    fun addMapLocationStorage(mapLocationStorage: MapLocationStorage?) {
        mapLocationStorageList.add(mapLocationStorage)
    }

    fun addAtMapLocationStorage(position: Int, mapLocationStorage: MapLocationStorage?) {
        mapLocationStorageList.add(position, mapLocationStorage)
    }

    fun getMapLocationStorage(index: Int): MapLocationStorage {
        return mapLocationStorageList[index] as MapLocationStorage
    }

    fun removeFromMapLocationStorageElementAt(index: Int) {
        mapLocationStorageList.removeAt(index)
    }

    fun sizeMapLocationStorageList(): Int {
        return mapLocationStorageList.size
    }

    fun clearMapLocationStorageList() {
        mapLocationStorageList.clear()
    }

    val listMapLocationStorageList: List<MapLocationStorage?>
        get() = Collections.unmodifiableList(mapLocationStorageList)
    protected var mapLocationStorageList: ArrayList<MapLocationStorage?> = ArrayList()
} /* MapWindowConfigurationStorage*/
