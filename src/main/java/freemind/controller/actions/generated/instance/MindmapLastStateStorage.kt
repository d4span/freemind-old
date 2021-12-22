package freemind.controller.actions.generated.instance

import java.util.Collections

/* MindmapLastStateStorage...*/ class MindmapLastStateStorage {
    /* constants from enums*/
    var lastChanged: Long = 0
    var tabIndex = 0
    var restorableName: String? = null
    var lastZoom = 0f
    var x = 0
    var y = 0
    var lastSelected: String? = null
    fun addNodeListMember(nodeListMember: NodeListMember?) {
        nodeListMemberList.add(nodeListMember)
    }

    fun addAtNodeListMember(position: Int, nodeListMember: NodeListMember?) {
        nodeListMemberList.add(position, nodeListMember)
    }

    fun getNodeListMember(index: Int): NodeListMember {
        return nodeListMemberList[index] as NodeListMember
    }

    fun removeFromNodeListMemberElementAt(index: Int) {
        nodeListMemberList.removeAt(index)
    }

    fun sizeNodeListMemberList(): Int {
        return nodeListMemberList.size
    }

    fun clearNodeListMemberList() {
        nodeListMemberList.clear()
    }

    val listNodeListMemberList: List<NodeListMember?>
        get() = Collections.unmodifiableList(nodeListMemberList)
    protected var nodeListMemberList: ArrayList<NodeListMember?> = ArrayList()
} /* MindmapLastStateStorage*/
