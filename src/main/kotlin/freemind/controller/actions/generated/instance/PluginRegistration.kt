package freemind.controller.actions.generated.instance

import java.util.*

/* PluginRegistration...*/   class PluginRegistration {
    /* constants from enums*/
    var className: String? = null
    var isPluginBase = false
    fun addPluginMode(pluginMode: PluginMode?) {
        pluginModeList.add(pluginMode)
    }

    fun addAtPluginMode(position: Int, pluginMode: PluginMode?) {
        pluginModeList.add(position, pluginMode)
    }

    fun getPluginMode(index: Int): PluginMode {
        return pluginModeList[index] as PluginMode
    }

    fun removeFromPluginModeElementAt(index: Int) {
        pluginModeList.removeAt(index)
    }

    fun sizePluginModeList(): Int {
        return pluginModeList.size
    }

    fun clearPluginModeList() {
        pluginModeList.clear()
    }

    val listPluginModeList: List<*>
        get() = Collections.unmodifiableList(pluginModeList)
    protected var pluginModeList: MutableList<PluginMode?> = mutableListOf()
} /* PluginRegistration*/