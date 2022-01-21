package freemind.controller.actions.generated.instance

import java.util.Collections

/* Patterns...*/   class Patterns : XmlAction() {
    /* constants from enums*/
    fun addChoice(choice: Pattern?) {
        choiceList.add(choice)
    }

    fun addAtChoice(position: Int, choice: Pattern?) {
        choiceList.add(position, choice)
    }

    fun setAtChoice(position: Int, choice: Pattern?) {
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

    val listChoiceList: List<Pattern?>
        get() = Collections.unmodifiableList(choiceList)
    protected var choiceList: ArrayList<Pattern?> = ArrayList()
} /* Patterns*/