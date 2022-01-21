package freemind.controller.actions.generated.instance

import java.util.Collections

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

    val listNodeListMemberList: List<NodeListMember?>
        get() = Collections.unmodifiableList(nodeListMemberList)
    protected var nodeListMemberList: ArrayList<NodeListMember?> = ArrayList()
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

    val listNodeChildParameterList: List<NodeChildParameter?>
        get() = Collections.unmodifiableList(nodeChildParameterList)
    protected var nodeChildParameterList: ArrayList<NodeChildParameter?> = ArrayList()
} /* HookNodeAction*/