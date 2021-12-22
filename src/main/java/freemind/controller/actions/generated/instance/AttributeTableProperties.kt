package freemind.controller.actions.generated.instance

import java.util.Collections

/* AttributeTableProperties...*/ class AttributeTableProperties : XmlAction() {
    /* constants from enums*/
    fun addTableColumnOrder(tableColumnOrder: TableColumnOrder?) {
        tableColumnOrderList.add(tableColumnOrder)
    }

    fun addAtTableColumnOrder(position: Int, tableColumnOrder: TableColumnOrder?) {
        tableColumnOrderList.add(position, tableColumnOrder)
    }

    fun getTableColumnOrder(index: Int): TableColumnOrder {
        return tableColumnOrderList[index] as TableColumnOrder
    }

    fun removeFromTableColumnOrderElementAt(index: Int) {
        tableColumnOrderList.removeAt(index)
    }

    fun sizeTableColumnOrderList(): Int {
        return tableColumnOrderList.size
    }

    fun clearTableColumnOrderList() {
        tableColumnOrderList.clear()
    }

    val listTableColumnOrderList: List<TableColumnOrder?>
        get() = Collections.unmodifiableList(tableColumnOrderList)
    protected var tableColumnOrderList: ArrayList<TableColumnOrder?> = ArrayList()
} /* AttributeTableProperties*/
