package freemind.controller.actions.generated.instance

import java.util.Collections

/* PluginAction...*/   class PluginAction {
    var label: String? = null
    var name: String? = null
    var base: String? = null
    var className: String? = null
    var documentation: String? = null
    var iconPath: String? = null
    var keyStroke: String? = null
    var instanciation: String? = null
    var isSelectable = false
    fun addChoice(choice: Any?) {
        choiceList.add(choice)
    }

    fun addAtChoice(position: Int, choice: Any?) {
        choiceList.add(position, choice)
    }

    fun setAtChoice(position: Int, choice: Any?) {
        choiceList.set(position, choice)
    }

    fun getChoice(index: Int): Any {
        return choiceList[index] as Any
    }

    fun sizeChoiceList(): Int {
        return choiceList.size
    }

    fun clearChoiceList() {
        choiceList.clear()
    }

    val listChoiceList: List<*>
        get() = Collections.unmodifiableList(choiceList)
    protected var choiceList: ArrayList<Any?> = ArrayList<Any?>()

    companion object {
        /* constants from enums*/
        const val ONCE = "Once"
        const val ONCEFORROOT = "OnceForRoot"
        const val ONCEFORALLNODES = "OnceForAllNodes"
        const val OTHER = "Other"
        const val APPLYTOROOT = "ApplyToRoot"
    }
} /* PluginAction*/