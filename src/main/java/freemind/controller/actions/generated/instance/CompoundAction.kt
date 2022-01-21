package freemind.controller.actions.generated.instance

import java.util.Collections

/* CompoundAction...*/   class CompoundAction : XmlAction() {
    /* constants from enums*/
    fun addChoice(choice: XmlAction?) {
        choiceList.add(choice)
    }

    fun addAtChoice(position: Int, choice: XmlAction?) {
        choiceList.add(position, choice)
    }

    fun setAtChoice(position: Int, choice: XmlAction?) {
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

    val listChoiceList: List<XmlAction?>
        get() = Collections.unmodifiableList(choiceList)
    protected var choiceList: ArrayList<XmlAction?> = ArrayList()
} /* CompoundAction*/