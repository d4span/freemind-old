package freemind.controller.actions.generated.instance

import java.util.*

/* MindmapLastStateMapStorage...*/   class MindmapLastStateMapStorage : XmlAction() {
    /* constants from enums*/
    var lastFocusedTab = 0
    fun addMindmapLastStateStorage(mindmapLastStateStorage: MindmapLastStateStorage?) {
        mindmapLastStateStorageList.add(mindmapLastStateStorage)
    }

    fun addAtMindmapLastStateStorage(position: Int, mindmapLastStateStorage: MindmapLastStateStorage?) {
        mindmapLastStateStorageList.add(position, mindmapLastStateStorage)
    }

    fun getMindmapLastStateStorage(index: Int): MindmapLastStateStorage {
        return mindmapLastStateStorageList[index] as MindmapLastStateStorage
    }

    fun removeFromMindmapLastStateStorageElementAt(index: Int) {
        mindmapLastStateStorageList.removeAt(index)
    }

    fun sizeMindmapLastStateStorageList(): Int {
        return mindmapLastStateStorageList.size
    }

    fun clearMindmapLastStateStorageList() {
        mindmapLastStateStorageList.clear()
    }

    val listMindmapLastStateStorageList: List<*>
        get() = Collections.unmodifiableList(mindmapLastStateStorageList)
    protected var mindmapLastStateStorageList: MutableList<MindmapLastStateStorage?> = mutableListOf()
} /* MindmapLastStateMapStorage*/