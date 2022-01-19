/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
*
*See COPYING for Details
*
*This program is free software; you can redistribute it and/or
*modify it under the terms of the GNU General Public License
*as published by the Free Software Foundation; either version 2
*of the License, or (at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program; if not, write to the Free Software
*Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package freemind.modes.mindmapmode.actions.xml.actors

import freemind.controller.MindMapNodesSelection
import freemind.controller.actions.generated.instance.PasteNodeAction
import freemind.controller.actions.generated.instance.TransferableContent
import freemind.controller.actions.generated.instance.UndoPasteNodeAction
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.FreeMind
import freemind.main.FreeMindCommon
import freemind.main.HtmlTools
import freemind.main.HtmlTools.NodeCreator
import freemind.main.Resources
import freemind.main.Tools
import freemind.main.Tools.StringReaderCreator
import freemind.main.XMLParseException
import freemind.modes.ControllerAdapter
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MapAdapter
import freemind.modes.MindMapNode
import freemind.modes.ModeController
import freemind.modes.NodeAdapter
import freemind.modes.mindmapmode.MindMapNodeModel
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.StringReader
import java.util.Vector
import java.util.logging.Level
import java.util.logging.Logger
import java.util.regex.Pattern
import javax.imageio.ImageIO
import javax.swing.JOptionPane

/**
 * @author foltin
 * @date 20.03.2014
 */
class PasteActor(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.controller.actions.ActorXml#act(freemind.controller.actions.
	 * generated.instance.XmlAction)
	 */
    override fun act(action: XmlAction) {
        val pasteAction = action as PasteNodeAction
        _paste(
            getTransferable(pasteAction.transferableContent),
            getNodeFromID(pasteAction.node),
            pasteAction.asSibling, pasteAction.isLeft
        )
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
    override fun getDoActionClass(): Class<PasteNodeAction> {
        return PasteNodeAction::class.java
    }

    /**
     * @param t
     * @param coord
     * @param pUndoAction
     * is filled automatically when not null.
     * @return a new PasteNodeAction.
     */
    fun getPasteNodeAction(
        t: Transferable?,
        coord: NodeCoordinate,
        pUndoAction: UndoPasteNodeAction?
    ): PasteNodeAction {
        val pasteAction = PasteNodeAction()
        val targetId = getNodeID(coord.target)
        pasteAction.node = targetId
        pasteAction.transferableContent = getTransferableContent(
            t,
            pUndoAction
        )
        pasteAction.asSibling = coord.asSibling
        pasteAction.isLeft = coord.isLeft
        if (pUndoAction != null) {
            pUndoAction.node = targetId
            pUndoAction.asSibling = coord.asSibling
            pUndoAction.isLeft = coord.isLeft
            if (logger!!.isLoggable(Level.FINE)) {
                val s = Tools.marshall(pUndoAction)
                logger!!.fine("Undo action: $s")
            }
        }
        return pasteAction
    }

    /** URGENT: Change this method.  */
    fun paste(node: MindMapNode?, parent: MindMapNode) {
        if (node != null) {
            insertNodeInto(node, parent)
            exMapFeedback?.map?.nodeStructureChanged(parent)
        }
    }

    /**
     * @param t
     * the content
     * @param target
     * where to add the content
     * @param asSibling
     * if true, the content is added beside the target, otherwise as
     * new children
     * @param isLeft
     * if something is pasted as a sibling to root, it must be
     * decided on which side of root
     * @return true, if successfully executed.
     */
    fun paste(
        t: Transferable,
        target: MindMapNode?,
        asSibling: Boolean,
        isLeft: Boolean
    ): Boolean? {
        val undoAction = UndoPasteNodeAction()
        val pasteAction: PasteNodeAction
        pasteAction = getPasteNodeAction(
            t,
            NodeCoordinate(
                target,
                asSibling, isLeft
            ),
            undoAction
        )
        // Undo-action
        /*
		 * how to construct the undo action for a complex paste? a) Paste pastes
		 * a number of new nodes that are adjacent. This number should be
		 * determined.
		 *
		 *
		 * d) But, as there are many possibilities which data flavor is pasted,
		 * it has to be determined before, which one will be taken.
		 */return exMapFeedback?.doTransaction(
            "paste",
            ActionPair(pasteAction, undoAction)
        )
    }

    class NodeCoordinate {
        var target: MindMapNode? = null
        var asSibling = false
        var isLeft: Boolean

        constructor(
            target: MindMapNode?,
            asSibling: Boolean,
            isLeft: Boolean
        ) {
            this.target = target
            this.asSibling = asSibling
            this.isLeft = isLeft
        }

        val node: MindMapNode
            get() = if (asSibling) {
                val parentNode = target!!.parentNode
                parentNode.getChildAt(
                    parentNode
                        .getChildPosition(target) - 1
                ) as MindMapNode
            } else {
                logger!!.finest(
                    "getChildCount = " + target!!.childCount +
                        ", target = " + target
                )
                target?.getChildAt(target!!.childCount - 1) as MindMapNode
            }

        constructor(node: MindMapNode, isLeft: Boolean) {
            this.isLeft = isLeft
            val parentNode = node.parentNode
            val childPosition = parentNode.getChildPosition(node)
            if (childPosition == parentNode.childCount - 1) {
                target = parentNode
                asSibling = false
            } else {
                target = parentNode.getChildAt(childPosition + 1) as MindMapNode
                asSibling = true
            }
        }
    }

    private interface DataFlavorHandler {
        @Throws(UnsupportedFlavorException::class, IOException::class)
        fun paste(
            TransferData: Any,
            target: MindMapNode?,
            asSibling: Boolean,
            isLeft: Boolean,
            t: Transferable
        )

        val dataFlavor: DataFlavor?
    }

    private inner class FileListFlavorHandler : DataFlavorHandler {
        override val dataFlavor: DataFlavor
            get() {
                TODO()
            }

        override fun paste(
            TransferData: Any,
            target: MindMapNode?,
            asSibling: Boolean,
            isLeft: Boolean,
            t: Transferable
        ) {
            TODO()
            // TODO: Does not correctly interpret asSibling.
//            val fileList = TransferData as List<File>
//            for (file in fileList) {
//                val node = exMapFeedback?.newNode(
//                    file.name,
//                    target?.map
//                )
//                node?.isLeft = isLeft
//                node?.link = Tools.fileToRelativeUrlString(
//                    file,
//                    exMapFeedback?.map?.file
//                )
//                insertNodeInto(
//                    node as MindMapNodeModel, target, asSibling,
//                    isLeft, false
//                )
//                // addUndoAction(node);
//            }
        }
    }

    private inner class MindMapNodesFlavorHandler : DataFlavorHandler {
        override val dataFlavor: DataFlavor
            get() {
                TODO()
            }

        override fun paste(
            TransferData: Any,
            target: MindMapNode?,
            asSibling: Boolean,
            isLeft: Boolean,
            t: Transferable
        ) {
            val textFromClipboard = TransferData as String
            val textLines = textFromClipboard
                .split(ModeController.NODESEPARATOR).toTypedArray()
            if (textLines.size > 1) {
                setWaitingCursor(true)
            }
            // and now? paste it:
            var mapContent = (
                MapAdapter.MAP_INITIAL_START +
                    FreeMind.XML_VERSION + "\"><node TEXT=\"DUMMY\">"
                )
            for (j in textLines.indices) {
                mapContent += textLines[j]
            }
            mapContent += "</node></map>"
            // logger.info("Pasting " + mapContent);
            try {
                val node = exMapFeedback?.map?.loadTree(
                    StringReaderCreator(
                        mapContent
                    ),
                    MapAdapter.sDontAskInstance
                )
                run {
                    val i = node?.childrenUnfolded()
                    while (i?.hasNext() ?: false) {
                        val importNode = i
                            ?.next() as MindMapNodeModel
                        insertNodeInto(
                            importNode, target, asSibling, isLeft,
                            true
                        )
                    }
                }
                run {
                    val i = node?.childrenUnfolded()
                    while (i?.hasNext() ?: false) {
                        val importNode = i?.next() as MindMapNodeModel
                        exMapFeedback?.invokeHooksRecursively(
                            importNode,
                            exMapFeedback?.map
                        )
                    }
                }
                val i = node?.childrenUnfolded()
                while (i?.hasNext() ?: false) {
                    val importNode = i
                        ?.next() as MindMapNodeModel
                    processUnfinishedLinksInHooks(importNode)
                }
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }
    }

    private inner class DirectHtmlFlavorHandler : DataFlavorHandler {
        private var mNodeCreator: NodeCreator

        override val dataFlavor: DataFlavor
            get() {
                TODO()
            }

        /**
         * @param pNodeCreator the nodeCreator to set
         */
        fun setNodeCreator(pNodeCreator: NodeCreator) {
            mNodeCreator = pNodeCreator
        }

        /**
         *
         */
        init {
            mNodeCreator = object : NodeCreator {
                override fun createChild(pParent: MindMapNode): MindMapNode? {
                    val node = exMapFeedback?.newNode(
                        "",
                        exMapFeedback?.map
                    )
                    insertNodeInto(node, pParent)
                    node?.setParent(pParent)
                    exMapFeedback?.nodeChanged(pParent)
                    return node
                }

                override fun setText(pText: String, pNode: MindMapNode) {
                    pNode.text = pText
                    exMapFeedback?.nodeChanged(pNode)
                }

                override fun setLink(pLink: String, pNode: MindMapNode) {
                    pNode.link = pLink
                    exMapFeedback?.nodeChanged(pNode)
                }
            }
        }

        @Throws(UnsupportedFlavorException::class, IOException::class)
        override fun paste(
            TransferData: Any,
            target: MindMapNode?,
            asSibling: Boolean,
            isLeft: Boolean,
            t: Transferable
        ) {
            var textFromClipboard = TransferData as String
            // workaround for java decoding bug
            // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6740877
            textFromClipboard = textFromClipboard.replace(65533.toChar(), ' ')
            // 			if (textFromClipboard.charAt(0) == 65533) {
// 				throw new UnsupportedFlavorException(
// 						MindMapNodesSelection.htmlFlavor);
// 			}
            // ^ This outputs transfer data to standard output. I don't know
            // why.
            // { Alternative pasting of HTML
            setWaitingCursor(true)
            logger!!.finer("directHtmlFlavor (original): $textFromClipboard")
            textFromClipboard = textFromClipboard
                .replace("(?i)(?s)<meta[^>]*>".toRegex(), "")
                .replace("(?i)(?s)<head>.*?</head>".toRegex(), "")
                .replace("(?i)(?s)</?html[^>]*>".toRegex(), "")
                .replace("(?i)(?s)</?body[^>]*>".toRegex(), "")
                .replace("(?i)(?s)<script.*?>.*?</script>".toRegex(), "") // Java HTML Editor
                // does not like
                // the tag.
                .replace("(?i)(?s)</?tbody.*?>".toRegex(), "").replace( // Java HTML Editor
                    // shows comments in
                    // not very nice
                    // manner.
                    "(?i)(?s)<!--.*?-->".toRegex(), ""
                ).replace( // Java HTML Editor
                    // does not like
                    // Microsoft Word's
                    // <o> tag.
                    "(?i)(?s)</?o[^>]*>".toRegex(), ""
                )
            textFromClipboard = "<html><body>$textFromClipboard</body></html>"
            logger!!.finer("directHtmlFlavor: $textFromClipboard")
            if (Resources.getInstance().getBoolProperty(
                    FreeMind.RESOUCES_PASTE_HTML_STRUCTURE
                )
            ) {
                HtmlTools.getInstance().insertHtmlIntoNodes(
                    textFromClipboard,
                    target, mNodeCreator
                )
            } else {
                if (Tools.safeEquals(
                        exMapFeedback?.getProperty(
                                "cut_out_pictures_when_pasting_html"
                            ),
                        "true"
                    )
                ) {
                    textFromClipboard = textFromClipboard.replace(
                        "(?i)(?s)<img[^>]*>".toRegex(), ""
                    )
                } // Cut out images.
                textFromClipboard = HtmlTools
                    .unescapeHTMLUnicodeEntity(textFromClipboard)
                val node = exMapFeedback?.newNode(
                    textFromClipboard,
                    exMapFeedback?.map
                )
                // if only one <a>...</a> element found, set link
                val m = HREF_PATTERN.matcher(textFromClipboard)
                if (m.matches()) {
                    val body = m.group(2)
                    if (!body.matches(Regex.fromLiteral(".*<\\s*a.*"))) {
                        val href = m.group(1)
                        node?.link = href
                    }
                }
                insertNodeInto(node, target)
                // addUndoAction(node);
            }
            setWaitingCursor(false)
        }
    }

    private inner class StringFlavorHandler : DataFlavorHandler {
        @Throws(UnsupportedFlavorException::class, IOException::class)
        override fun paste(
            TransferData: Any,
            target: MindMapNode?,
            asSibling: Boolean,
            isLeft: Boolean,
            t: Transferable
        ) {
            pasteStringWithoutRedisplay(t, target, asSibling, isLeft)
        }

        override val dataFlavor: DataFlavor?
            get() = DataFlavor.stringFlavor
    }

    private inner class ImageFlavorHandler : DataFlavorHandler {
        @Throws(UnsupportedFlavorException::class, IOException::class)
        override fun paste(
            TransferData: Any,
            target: MindMapNode?,
            asSibling: Boolean,
            isLeft: Boolean,
            t: Transferable
        ) {
            logger!!.info("imageFlavor")
            setWaitingCursor(true)
            val mindmapFile = exMapFeedback?.map?.file
            val imgfile: String
            if (mindmapFile == null) {
                JOptionPane.showMessageDialog(
                    exMapFeedback?.viewAbstraction?.selected,
                    exMapFeedback?.getResourceString("map_not_saved"),
                    "FreeMind", JOptionPane.ERROR_MESSAGE
                )
                return
            }
            val parentFile = mindmapFile.parentFile
            var filePrefix = mindmapFile.name.replace(
                FreeMindCommon.FREEMIND_FILE_EXTENSION, "_"
            )
            /* prefix for createTempFile must be at least three characters long.
			 See  [bugs:#1261] Unable to paste images from clipboard */while (filePrefix.length < 3) {
                filePrefix += "_"
            }
            val tempFile = File
                .createTempFile(filePrefix, ".jpeg", parentFile)
            val fos = FileOutputStream(tempFile)
            fos.write(Tools.fromBase64(TransferData.toString()))
            fos.close()

            // Absolute, if not in the correct directory!
            imgfile = tempFile.name
            logger!!.info("Writing image to $imgfile")
            val strText = (
                "<html><body><img src=\"" + imgfile +
                    "\"/></body></html>"
                )
            val node = exMapFeedback?.newNode(
                strText,
                exMapFeedback?.map
            )
            // if only one <a>...</a> element found, set link
            insertNodeInto(node, target)
            // addUndoAction(node);
            setWaitingCursor(false)
        }

        override val dataFlavor: DataFlavor?
            get() = DataFlavor.imageFlavor
    }

    /*
     *
     */
    private fun _paste(
        t: Transferable?,
        target: MindMapNode?,
        asSibling: Boolean,
        isLeft: Boolean
    ) {
        if (t == null) {
            return
        }
        // Uncomment to print obtained data flavors

        /*
		 * DataFlavor[] fl = t.getTransferDataFlavors(); for (int i = 0; i <
		 * fl.length; i++) { System.out.println(fl[i]); }
		 */
        val dataFlavorHandlerList = flavorHandlers
        for (i in dataFlavorHandlerList.indices) {
            val handler = dataFlavorHandlerList[i]
            val flavor = handler.dataFlavor
            if (t.isDataFlavorSupported(flavor)) {
                try {
                    handler.paste(
                        t.getTransferData(flavor), target, asSibling,
                        isLeft, t
                    )
                    break
                } catch (e: UnsupportedFlavorException) {
                    Resources.getInstance().logException(e)
                } catch (e: IOException) {
                    Resources.getInstance().logException(e)
                }
            }
        }
        setWaitingCursor(false)
    } // %%% Make dependent on an option?: new HtmlFlavorHandler(),

    /**
     */
    private val flavorHandlers: Array<DataFlavorHandler>
        get() = // %%% Make dependent on an option?: new HtmlFlavorHandler(),
            arrayOf(
                FileListFlavorHandler(), MindMapNodesFlavorHandler(),
                DirectHtmlFlavorHandler(), StringFlavorHandler(),
                ImageFlavorHandler()
            )

    @Throws(XMLParseException::class)
    fun pasteXMLWithoutRedisplay(
        pasted: String,
        target: MindMapNode,
        asSibling: Boolean,
        changeSide: Boolean,
        isLeft: Boolean,
        pIDToTarget: HashMap<String?, NodeAdapter?>?
    ): MindMapNodeModel? {
        // Call nodeStructureChanged(target) after this function.
        logger!!.fine("Pasting $pasted to $target")
        return try {
            val node = exMapFeedback?.map
                ?.createNodeTreeFromXml(StringReader(pasted), pIDToTarget) as MindMapNodeModel
            insertNodeInto(node, target, asSibling, isLeft, changeSide)
            exMapFeedback?.invokeHooksRecursively(
                node,
                exMapFeedback?.map
            )
            processUnfinishedLinksInHooks(node)
            node
        } catch (ee: IOException) {
            Resources.getInstance().logException(ee)
            null
        }
    }

    private fun insertNodeInto(
        node: MindMapNodeModel,
        target: MindMapNode?,
        asSibling: Boolean,
        isLeft: Boolean,
        changeSide: Boolean
    ) {
        val parent: MindMapNode?
        parent = if (asSibling) {
            target?.parentNode
        } else {
            target
        }
        if (changeSide) {
            node.setParent(parent)
            node.isLeft = isLeft
        }
        // now, the import is finished. We can inform others about the new
        // nodes:
        if (asSibling) {
            insertNodeInto(node, parent, parent?.getChildPosition(target) ?: 0)
        } else {
            insertNodeInto(node, target)
        }
    }

    /**
     * @param pMapFeedback
     */
    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
    }

    /**
     * Paste String (as opposed to other flavors)
     *
     * Split the text into lines; determine the new tree structure by the number
     * of leading spaces in lines. In case that trimmed line starts with
     * protocol (http:, https:, ftp:), create a link with the same content.
     *
     * If there was only one line to be pasted, return the pasted node, null
     * otherwise.
     *
     * @param isLeft
     * TODO
     */
    @Throws(UnsupportedFlavorException::class, IOException::class)
    private fun pasteStringWithoutRedisplay(
        t: Transferable,
        parent: MindMapNode?,
        asSibling: Boolean,
        isLeft: Boolean
    ): MindMapNode? {
        var p = parent
        val textFromClipboard = t
            .getTransferData(DataFlavor.stringFlavor) as String
        val mailPattern = Pattern.compile("([^@ <>\\*']+@[^@ <>\\*']+)")
        val textLines = textFromClipboard.split("\n").toTypedArray()
        if (textLines.size > 1) {
            setWaitingCursor(true)
        }
        if (asSibling) {
            // When pasting as sibling, we use virtual node as parent. When the
            // pasting to
            // virtual node is completed, we insert the children of that virtual
            // node to
            // the parent of real parent.
            p = MindMapNodeModel(exMapFeedback?.map)
        }
        val parentNodes = ArrayList<MindMapNode?>()
        val parentNodesDepths = ArrayList<Int>()
        parentNodes.add(p)
        parentNodesDepths.add(-1)
        val linkPrefixes = arrayOf("http://", "ftp://", "https://")
        var pastedNode: MindMapNode? = null
        for (i in textLines.indices) {
            var text = textLines[i]
            // 			System.out.println("Text to paste: "+text);
            text = text.replace("\t".toRegex(), "        ")
            if (text.matches(Regex.fromLiteral(" *"))) {
                continue
            }
            var depth = 0
            while (depth < text.length && text[depth] == ' ') {
                ++depth
            }
            var visibleText = text.trim { it <= ' ' }

            // If the text is a recognizable link (e.g.
            // http://www.google.com/index.html),
            // make it more readable by look nicer by cutting off obvious prefix
            // and other
            // transforamtions.
            if (visibleText.matches(Regex.fromLiteral("^http://(www\\.)?[^ ]*$"))) {
                visibleText = visibleText.replace("^http://(www\\.)?".toRegex(), "")
                    .replace("(/|\\.[^\\./\\?]*)$".toRegex(), "")
                    .replace("((\\.[^\\./]*\\?)|\\?)[^/]*$".toRegex(), " ? ...")
                    .replace("_|%20".toRegex(), " ")
                val textParts = visibleText.split("/").toTypedArray()
                visibleText = ""
                for (textPartIdx in textParts.indices) {
                    if (textPartIdx > 0) {
                        visibleText += " > "
                    }
                    visibleText += if (textPartIdx == 0) textParts[textPartIdx] else Tools.firstLetterCapitalized(
                        textParts[textPartIdx]
                            .replace("^~*".toRegex(), "")
                    )
                }
            }
            val node = exMapFeedback?.newNode(
                visibleText,
                p?.map
            )
            if (textLines.size == 1) {
                pastedNode = node
            }

            // Heuristically determine, if there is a mail.
            val mailMatcher = mailPattern.matcher(visibleText)
            if (mailMatcher.find()) {
                node?.link = "mailto:" + mailMatcher.group()
            }

            // Heuristically determine, if there is a link. Because this is
            // heuristic, it is probable that it can be improved to include
            // some matches or exclude some matches.
            for (j in linkPrefixes.indices) {
                val linkStart = text.indexOf(linkPrefixes[j])
                if (linkStart != -1) {
                    var linkEnd = linkStart
                    while (linkEnd < text.length &&
                        !nonLinkCharacter.matcher(
                                text.substring(linkEnd, linkEnd + 1)
                            )
                            .matches()
                    ) {
                        linkEnd++
                    }
                    node?.link = text.substring(linkStart, linkEnd)
                }
            }

            // Determine parent among candidate parents
            // Change the array of candidate parents accordingly
            for (j in parentNodes.indices.reversed()) {
                if (depth > parentNodesDepths[j].toInt()) {
                    for (k in j + 1 until parentNodes.size) {
                        if (parentNodes[k]?.parentNode === p) {
                            // addUndoAction(n);
                        }
                        parentNodes.removeAt(k)
                        parentNodesDepths.removeAt(k)
                    }
                    node?.isLeft = isLeft
                    insertNodeInto(node, parentNodes[j])
                    parentNodes.add(node)
                    parentNodesDepths.add(depth)
                    break
                }
            }
        }
        for (k in parentNodes.indices) {
            if (parentNodes[k]?.parentNode === p) {
                // addUndoAction(n);
            }
        }
        return pastedNode
    }

    /**
     */
    private fun insertNodeInto(node: MindMapNodeModel, parent: MindMapNode?, i: Int) {
        exMapFeedback?.insertNodeInto(node, parent, i)
    }

    private fun insertNodeInto(node: MindMapNode?, parent: MindMapNode?) {
        exMapFeedback?.insertNodeInto(node, parent, parent?.childCount ?: 0)
    }

    private fun getTransferableContent(
        t: Transferable?,
        pUndoAction: UndoPasteNodeAction?
    ): TransferableContent? {
        var amountAlreadySet = false
        try {
            val trans = TransferableContent()
            if (t?.isDataFlavorSupported(MindMapNodesSelection.fileListFlavor) ?: false) {
                /*
				 * Since the JAXB-generated interface TransferableContent
				 * doesn't supply a setTranserableAsFileList method, we have to
				 * get the fileList, clear it, and then set it to the new value.
				 */
                TODO()
//                val fileList = t?.getTransferData(MindMapNodesSelection.fileListFlavor) as List<File>
//                val iter = fileList.iterator()
//                while (iter.hasNext()) {
//                    val fileName = iter.next()
//                    val transferableFile = TransferableFile()
//                    transferableFile.fileName = fileName.absolutePath
//                    trans.addTransferableFile(transferableFile)
//                }
//                if (pUndoAction != null && !amountAlreadySet) {
//                    pUndoAction.nodeAmount = fileList.size
//                    amountAlreadySet = true
//                }
            }
            if (t?.isDataFlavorSupported(MindMapNodesSelection.mindMapNodesFlavor) ?: false) {
                val textFromClipboard: String
                textFromClipboard = t?.getTransferData(MindMapNodesSelection.mindMapNodesFlavor) as String
                trans.transferable = HtmlTools.makeValidXml(textFromClipboard)
                if (pUndoAction != null && !amountAlreadySet) {
                    pUndoAction
                        .nodeAmount = Tools.countOccurrences(
                        textFromClipboard,
                        ControllerAdapter.NODESEPARATOR
                    ) + 1
                    amountAlreadySet = true
                }
            }
            if (t?.isDataFlavorSupported(MindMapNodesSelection.htmlFlavor) ?: false) {
                val textFromClipboard: String
                textFromClipboard = t?.getTransferData(MindMapNodesSelection.htmlFlavor) as String
                trans.transferableAsHtml = HtmlTools
                    .makeValidXml(textFromClipboard)
                if (pUndoAction != null && !amountAlreadySet) {
                    // on html paste, the string text is taken and "improved".
                    // Thus, we count its lines.
                    try {
                        pUndoAction.nodeAmount = determineAmountOfNewNodes(t)
                        amountAlreadySet = true
                    } catch (e: Exception) {
                        Resources.getInstance().logException(e)
                        // ok, something went wrong, but this breaks undo, only.
                        pUndoAction.nodeAmount = 1
                    }
                }
            }
            if (t?.isDataFlavorSupported(DataFlavor.stringFlavor) ?: false) {
                val textFromClipboard: String
                textFromClipboard = t?.getTransferData(DataFlavor.stringFlavor) as String
                trans.transferableAsPlainText = HtmlTools
                    .makeValidXml(textFromClipboard)
                if (pUndoAction != null && !amountAlreadySet) {
                    // determine amount of new nodes using the algorithm:
                    val childCount = determineAmountOfNewTextNodes(t)
                    pUndoAction.nodeAmount = childCount
                    amountAlreadySet = true
                }
            }
            if (t?.isDataFlavorSupported(MindMapNodesSelection.rtfFlavor) ?: false) {
                // byte[] textFromClipboard = (byte[])
                // t.getTransferData(MindMapNodesSelection.rtfFlavor);
                // trans.setTransferableAsRTF(textFromClipboard.toString());
            }
            if (t?.isDataFlavorSupported(DataFlavor.imageFlavor) ?: false) {
                logger!!.info("image...")
                try {
                    // Get data from clipboard and assign it to an image.
                    // clipboard.getData() returns an object, so we need to cast
                    // it to a BufferdImage.
                    val image = t?.getTransferData(DataFlavor.imageFlavor) as BufferedImage
                    logger!!.info("Starting to write clipboard image $image")
                    val baos = ByteArrayOutputStream()
                    ImageIO.write(image, "jpg", baos)
                    val base64String = Tools.toBase64(baos.toByteArray())
                    trans.transferableAsImage = base64String
                    if (pUndoAction != null && !amountAlreadySet) {
                        pUndoAction.nodeAmount = 1
                    }
                } // getData throws this.
                catch (ufe: UnsupportedFlavorException) {
                    Resources.getInstance().logException(ufe)
                } catch (ioe: IOException) {
                    Resources.getInstance().logException(ioe)
                }
            }
            return trans
        } catch (e: UnsupportedFlavorException) {
            Resources.getInstance().logException(e)
        } catch (e: IOException) {
            Resources.getInstance().logException(e)
        }
        return null
    }

    /*
	 * TODO: This is a bit dirty here. Better would be to separate the algorithm
	 * from the node creation and use the pure algo.
	 */
    @Throws(UnsupportedFlavorException::class, IOException::class)
    protected fun determineAmountOfNewTextNodes(t: Transferable): Int {
        // create a new node for testing purposes.
        val parent = MindMapNodeModel(
            exMapFeedback?.map
        )
        pasteStringWithoutRedisplay(t, parent, false, false)
        return parent.childCount
    }

    /**
     * Only for HTML nodes.
     * @param t
     * @return
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    @Throws(UnsupportedFlavorException::class, IOException::class)
    fun determineAmountOfNewNodes(t: Transferable): Int {
        // create a new node for testing purposes.
        val parent = MindMapNodeModel(
            exMapFeedback?.map
        )
        parent.text = "ROOT"
        val handler = DirectHtmlFlavorHandler()
        // creator, that only creates dummy nodes.
        handler.setNodeCreator(object : NodeCreator {
            override fun createChild(pParent: MindMapNode): MindMapNode? {
                try {
                    val newNode = MindMapNodeModel(
                        "",
                        exMapFeedback?.map
                    )
                    pParent.insert(newNode, pParent.childCount)
                    newNode.setParent(pParent)
                    return newNode
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                }
                return null
            }

            override fun setText(pText: String, pNode: MindMapNode) {
                pNode.text = pText
            }

            override fun setLink(pLink: String, pNode: MindMapNode) {}
        })
        handler.paste(t.getTransferData(handler.dataFlavor), parent, false, true, t)
        return parent.childCount
    }

    private fun getTransferable(trans: TransferableContent?): Transferable {
        // create Transferable:
        // Add file list to this selection.
        val fileList = Vector<File?>()
        val iter = trans!!.listTransferableFileList.iterator()
        while (iter.hasNext()) {
            val tFile = iter.next()
            fileList.add(File(tFile?.fileName))
        }
        return MindMapNodesSelection(
            trans.transferable!!,
            trans.transferableAsImage!!,
            trans.transferableAsPlainText!!,
            trans.transferableAsRTF, trans.transferableAsHtml,
            trans.transferableAsDrop!!, fileList, null
        )
    }

    protected fun setWaitingCursor(waitingCursor: Boolean) {
        exMapFeedback?.setWaitingCursor(waitingCursor)
    }

    /**
     *
     */
    fun processUnfinishedLinksInHooks(node: MindMapNode) {
        val i: Iterator<MindMapNode> = node.childrenUnfolded()
        while (i.hasNext()) {
            val child = i.next()
            processUnfinishedLinksInHooks(child)
        }
        for (hook in node.hooks) {
            hook.processUnfinishedLinks()
        }
    }

    companion object {
        protected var logger: Logger? = null
        private val HREF_PATTERN = Pattern
            .compile("<html>\\s*<body>\\s*<a\\s+href=\"([^>]+)\">(.*)</a>\\s*</body>\\s*</html>")
        val nonLinkCharacter = Pattern.compile("[ \n()'\",;]")
    }
}
