package freemind.controller.actions.generated.instance

import java.util.Collections

/* TimeWindowConfigurationStorage...*/ class TimeWindowConfigurationStorage : WindowConfigurationStorage() {
    /* constants from enums*/
    var viewFoldedNodes = false
    fun addTimeWindowColumnSetting(timeWindowColumnSetting: TimeWindowColumnSetting?) {
        timeWindowColumnSettingList.add(timeWindowColumnSetting)
    }

    fun addAtTimeWindowColumnSetting(position: Int, timeWindowColumnSetting: TimeWindowColumnSetting?) {
        timeWindowColumnSettingList.add(position, timeWindowColumnSetting)
    }

    fun getTimeWindowColumnSetting(index: Int): TimeWindowColumnSetting {
        return timeWindowColumnSettingList[index] as TimeWindowColumnSetting
    }

    fun removeFromTimeWindowColumnSettingElementAt(index: Int) {
        timeWindowColumnSettingList.removeAt(index)
    }

    fun sizeTimeWindowColumnSettingList(): Int {
        return timeWindowColumnSettingList.size
    }

    fun clearTimeWindowColumnSettingList() {
        timeWindowColumnSettingList.clear()
    }

    val listTimeWindowColumnSettingList: List<TimeWindowColumnSetting?>
        get() = Collections.unmodifiableList(timeWindowColumnSettingList)
    protected var timeWindowColumnSettingList: ArrayList<TimeWindowColumnSetting?> = ArrayList()
} /* TimeWindowConfigurationStorage*/
