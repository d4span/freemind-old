package freemind.controller.actions.generated.instance

import java.util.Collections

/* Reversegeocode...*/   class Reversegeocode : XmlAction() {
    /* constants from enums*/
    var attribution: String? = null
    var querystring: String? = null
    var timestamp: String? = null
    fun addResult(result: Result?) {
        resultList.add(result)
    }

    fun addAtResult(position: Int, result: Result?) {
        resultList.add(position, result)
    }

    fun getResult(index: Int): Result {
        return resultList[index] as Result
    }

    fun removeFromResultElementAt(index: Int) {
        resultList.removeAt(index)
    }

    fun sizeResultList(): Int {
        return resultList.size
    }

    fun clearResultList() {
        resultList.clear()
    }

    val listResultList: List<*>
        get() = Collections.unmodifiableList(resultList)
    protected var resultList: ArrayList<Result?> = ArrayList()
} /* Reversegeocode*/