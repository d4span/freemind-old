/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on ???
 */
/*$Id: MindMapNodesSelection.java,v 1.2.18.2.12.3 2007/02/04 22:02:02 dpolivaev Exp $*/
package freemind.controller

import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.ByteArrayInputStream

class MindMapNodesSelection(
    private val nodesContent: String,
    private val imageContent: String,
    private val stringContent: String,
    private val rtfContent: String?,
    private val htmlContent: String?,
    private var dropActionContent: String,
    private val fileList: List<*>,
    private val nodeIdsContent: List<String>
) : Transferable, ClipboardOwner {
    @Throws(UnsupportedFlavorException::class)
    override fun getTransferData(flavor: DataFlavor): Any {
        if (flavor.equals(DataFlavor.imageFlavor)) {
            return imageContent
        }
        if (flavor.equals(DataFlavor.stringFlavor)) {
            return stringContent
        }
        if (flavor.equals(mindMapNodesFlavor)) {
            return nodesContent
        }
        if (flavor.equals(dropActionFlavor)) {
            return dropActionContent
        }
        if (flavor.equals(rtfFlavor)) {
            val byteArray = rtfContent!!.toByteArray()
            // for (int i = 0; i < byteArray.length; ++i) {
            // System.out.println(byteArray[i]); }
            return ByteArrayInputStream(byteArray)
        }
        if (flavor.equals(htmlFlavor) && htmlContent != null) {
            return htmlContent
        }
        if (flavor.equals(fileListFlavor)) {
            return fileList
        }
        if (flavor.equals(copyNodeIdsFlavor)) {
            return nodeIdsContent
        }
        throw UnsupportedFlavorException(flavor)
    }

    override fun getTransferDataFlavors(): Array<DataFlavor?> {
        return arrayOf(
            DataFlavor.imageFlavor,
            DataFlavor.stringFlavor, mindMapNodesFlavor, rtfFlavor,
            htmlFlavor, dropActionFlavor, copyNodeIdsFlavor
        )
    }

    override fun isDataFlavorSupported(flavor: DataFlavor): Boolean {
        if (flavor.equals(DataFlavor.imageFlavor)) {
            return true
        }
        if (flavor.equals(DataFlavor.stringFlavor)) {
            return true
        }
        if (flavor.equals(mindMapNodesFlavor)) {
            return true
        }
        if (flavor.equals(rtfFlavor) && rtfContent != null) {
            return true
        }
        if (flavor.equals(dropActionFlavor)) {
            return true
        }
        if (flavor.equals(htmlFlavor) && htmlContent != null) {
            return true
        }
        if (flavor.equals(fileListFlavor) && fileList.size > 0
        ) {
            return true
        }
        return flavor.equals(copyNodeIdsFlavor)
    }

    override fun lostOwnership(clipboard: Clipboard, contents: Transferable) {}
    fun setDropAction(dropActionContent: String) {
        this.dropActionContent = dropActionContent
    }

    companion object {
        @JvmField
        var mindMapNodesFlavor: DataFlavor? = null
        @JvmField
        var rtfFlavor: DataFlavor? = null
        @JvmField
        var htmlFlavor: DataFlavor? = null
        @JvmField
        var fileListFlavor: DataFlavor? = null

        /**
         * fc, 7.8.2004: This is a quite interesting flavor, but how does it
         * works???
         */
        @JvmField
        var dropActionFlavor: DataFlavor? = null

        /**
         * This flavor contains the node ids only. Thus, it works only on the same
         * map.
         */
        @JvmField
        var copyNodeIdsFlavor: DataFlavor? = null

        init {
            try {
                mindMapNodesFlavor = DataFlavor(
                    "text/freemind-nodes; class=java.lang.String"
                )
                rtfFlavor = DataFlavor("text/rtf; class=java.io.InputStream")
                htmlFlavor = DataFlavor("text/html; class=java.lang.String")
                fileListFlavor = DataFlavor(
                    "application/x-java-file-list; class=java.util.List"
                )
                dropActionFlavor = DataFlavor(
                    "text/drop-action; class=java.lang.String"
                )
                copyNodeIdsFlavor = DataFlavor(
                    "application/freemind-node-ids; class=java.util.List"
                )
            } catch (e: Exception) {
                System.err.println(e)
            }
        }
    }
}
