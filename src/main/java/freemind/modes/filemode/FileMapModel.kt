/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
package freemind.modes.filemode

import freemind.modes.ArrowLinkAdapter
import freemind.modes.ArrowLinkTarget
import freemind.modes.CloudAdapter
import freemind.modes.EdgeAdapter
import freemind.modes.MapAdapter
import freemind.modes.MindMap
import freemind.modes.MindMapLinkRegistry
import freemind.modes.ModeController
import freemind.modes.NodeAdapter
import java.io.File
import java.io.IOException
import java.io.Writer

class FileMapModel(
    root: File?,
    modeController: ModeController?
) : MapAdapter(modeController) {
    private val linkRegistry: MindMapLinkRegistry

    //
    // Constructors
    //
    constructor(modeController: ModeController?) : this(
        File(File.separator),
        modeController
    ) {
    }

    init {
        setRoot(FileNodeModel(root, this))
        rootNode.isFolded = false
        linkRegistry = MindMapLinkRegistry()
    }

    //
    // Other methods
    //
    override fun getLinkRegistry(): MindMapLinkRegistry {
        return linkRegistry
    }

    //
    // Other methods
    //
    override fun save(file: File): Boolean {
        return true
    }

    /**
     *
     */
    override fun destroy() {
        /*
		 * fc, 8.8.2004: don't call super.destroy as this method tries to remove
		 * the hooks recursively. This must fail.
		 */
        // super.destroy();
        cancelFileChangeObservationTimer()
    }

    override fun isSaved(): Boolean {
        return true
    }

    override fun toString(): String {
        return "File: " + getRoot().toString()
    }

    fun changeNode() {
        // File file = ((FileNodeModel)node).getFile();
        // File newFile = new File(file.getParentFile(), newText);
        // file.renameTo(newFile);
        // System.out.println(file);
        // FileNodeModel parent = (FileNodeModel)node.getParent();
        // // removeNodeFromParent(node);

        // insertNodeInto(new FileNodeModel(newFile),parent,0);

        // nodeChanged(node);
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.modes.MindMap#setLinkInclinationChanged()
	 */
    fun setLinkInclinationChanged() {}

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.modes.MindMap#getXml(java.io.Writer)
	 */
    @Throws(IOException::class)
    override fun getXml(fileout: Writer) {
        // nothing.
        // FIXME: Implement me if you need me.
        throw RuntimeException("Unimplemented method called.")
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.modes.MindMap#getFilteredXml(java.io.Writer)
	 */
    @Throws(IOException::class)
    override fun getFilteredXml(fileout: Writer) {
        // nothing.
        // FIXME: Implement me if you need me.
        throw RuntimeException("Unimplemented method called.")
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap#createNodeAdapter(freemind.modes.MindMap, java.lang.String)
	 */
    override fun createNodeAdapter(pMap: MindMap, pNodeClass: String): NodeAdapter {
        // FIXME: Implement me if you need me.
        throw RuntimeException("Unimplemented method called.")
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap#createEdgeAdapter(freemind.modes.NodeAdapter)
	 */
    override fun createEdgeAdapter(pNode: NodeAdapter): EdgeAdapter {
        // FIXME: Implement me if you need me.
        throw RuntimeException("Unimplemented method called.")
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap#createCloudAdapter(freemind.modes.NodeAdapter)
	 */
    override fun createCloudAdapter(pNode: NodeAdapter): CloudAdapter {
        // FIXME: Implement me if you need me.
        throw RuntimeException("Unimplemented method called.")
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap#createArrowLinkAdapter(freemind.modes.NodeAdapter, freemind.modes.NodeAdapter)
	 */
    override fun createArrowLinkAdapter(
        pSource: NodeAdapter,
        pTarget: NodeAdapter
    ): ArrowLinkAdapter {
        // FIXME: Implement me if you need me.
        throw RuntimeException("Unimplemented method called.")
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap#createArrowLinkTarget(freemind.modes.NodeAdapter, freemind.modes.NodeAdapter)
	 */
    override fun createArrowLinkTarget(
        pSource: NodeAdapter,
        pTarget: NodeAdapter
    ): ArrowLinkTarget {
        // FIXME: Implement me if you need me.
        throw RuntimeException("Unimplemented method called.")
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap#createEncryptedNode(java.lang.String)
	 */
    override fun createEncryptedNode(pAdditionalInfo: String): NodeAdapter {
        // FIXME: Implement me if you need me.
        throw RuntimeException("Unimplemented method called.")
    }
}
