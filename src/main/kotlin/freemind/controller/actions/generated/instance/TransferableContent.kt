package freemind.controller.actions.generated.instance

import java.util.*

/* TransferableContent...*/   class TransferableContent {
    /* constants from enums*/
    var transferable: String? = null
    var transferableAsPlainText: String? = null
    var transferableAsRTF: String? = null
    var transferableAsDrop: String? = null
    var transferableAsHtml: String? = null
    var transferableAsImage: String? = null
    fun addTransferableFile(transferableFile: TransferableFile?) {
        transferableFileList.add(transferableFile)
    }

    fun addAtTransferableFile(position: Int, transferableFile: TransferableFile?) {
        transferableFileList.add(position, transferableFile)
    }

    fun getTransferableFile(index: Int): TransferableFile {
        return transferableFileList[index] as TransferableFile
    }

    fun removeFromTransferableFileElementAt(index: Int) {
        transferableFileList.removeAt(index)
    }

    fun sizeTransferableFileList(): Int {
        return transferableFileList.size
    }

    fun clearTransferableFileList() {
        transferableFileList.clear()
    }

    val listTransferableFileList: List<*>
        get() = Collections.unmodifiableList(transferableFileList)
    protected var transferableFileList: MutableList<TransferableFile?> = mutableListOf()
} /* TransferableContent*/