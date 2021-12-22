package freemind.controller.actions.generated.instance

import java.util.Collections

/* PluginStrings...*/ class PluginStrings {
    /* constants from enums*/
    var language: String? = null
    fun addPluginString(pluginString: PluginString?) {
        pluginStringList.add(pluginString)
    }

    fun addAtPluginString(position: Int, pluginString: PluginString?) {
        pluginStringList.add(position, pluginString)
    }

    fun getPluginString(index: Int): PluginString {
        return pluginStringList[index] as PluginString
    }

    fun removeFromPluginStringElementAt(index: Int) {
        pluginStringList.removeAt(index)
    }

    fun sizePluginStringList(): Int {
        return pluginStringList.size
    }

    fun clearPluginStringList() {
        pluginStringList.clear()
    }

    val listPluginStringList: List<*>
        get() = Collections.unmodifiableList(pluginStringList)
    protected var pluginStringList: ArrayList<PluginString?> = ArrayList()
} /* PluginStrings*/
