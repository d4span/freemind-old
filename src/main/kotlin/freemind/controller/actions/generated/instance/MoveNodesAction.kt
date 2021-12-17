package freemind.controller.actions.generated.instance

import java.util.*

/* MoveNodesAction...*/   class MoveNodesAction : NodeAction() {
    /* constants from enums*/
    var direction = 0
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

    val listNodeListMemberList: List<*>
        get() = Collections.unmodifiableList(nodeListMemberList)
    protected var nodeListMemberList: MutableList<NodeListMember?> = mutableListOf()
} /* MoveNodesAction*/