package freemind.controller.actions.generated.instance

import java.util.*

/* HookNodeAction...*/   class HookNodeAction : NodeAction() {
    /* constants from enums*/
    var hookName: String? = null
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
    fun addNodeChildParameter(nodeChildParameter: NodeChildParameter?) {
        nodeChildParameterList.add(nodeChildParameter)
    }

    fun addAtNodeChildParameter(position: Int, nodeChildParameter: NodeChildParameter?) {
        nodeChildParameterList.add(position, nodeChildParameter)
    }

    fun getNodeChildParameter(index: Int): NodeChildParameter {
        return nodeChildParameterList[index] as NodeChildParameter
    }

    fun removeFromNodeChildParameterElementAt(index: Int) {
        nodeChildParameterList.removeAt(index)
    }

    fun sizeNodeChildParameterList(): Int {
        return nodeChildParameterList.size
    }

    fun clearNodeChildParameterList() {
        nodeChildParameterList.clear()
    }

    val listNodeChildParameterList: List<*>
        get() = Collections.unmodifiableList(nodeChildParameterList)
    protected var nodeChildParameterList: MutableList<NodeChildParameter?> = mutableListOf()
} /* HookNodeAction*/