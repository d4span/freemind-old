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
package freemind.modes.browsemode

import freemind.modes.ArrowLinkAdapter
import freemind.modes.ArrowLinkTarget
import freemind.modes.CloudAdapter
import freemind.modes.EdgeAdapter
import freemind.modes.MapAdapter
import freemind.modes.MapFeedback
import freemind.modes.MindMap
import freemind.modes.MindMapLinkRegistry
import freemind.modes.ModeController
import freemind.modes.NodeAdapter
import java.io.File
import java.io.IOException
import java.io.Writer
import java.net.URL

class BrowseMapModel(root: BrowseNodeModel?, modeController: ModeController) : MapAdapter(modeController) {
    private var url: URL? = null
    private val linkRegistry: MindMapLinkRegistry

    init {
        if (root != null) setRoot(root) else setRoot(
            BrowseNodeModel(
                modeController.getResourceString(
                    "new_mindmap"
                ),
                this
            )
        )
        // register new LinkRegistryAdapter
        linkRegistry = MindMapLinkRegistry()
    }

    //
    // Other methods
    //
    override fun getLinkRegistry(): MindMapLinkRegistry {
        return linkRegistry
    }

    override fun toString(): String {
        return getURL().toString()
    }

    override fun getFile(): File? {
        return null
    }

    protected fun setFile() {}

    /**
     * Get the value of url.
     *
     * @return Value of url.
     */
    override fun getURL(): URL {
        return url!!
    }

    /**
     * Set the value of url.
     *
     * @param v
     * Value to assign to url.
     */
    fun setURL(v: URL?) {
        url = v
    }

    override fun save(file: File): Boolean {
        return true
    }

    override fun isSaved(): Boolean {
        return true
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

    protected fun createNodeAdapter(pMapFeedback: MapFeedback, nodeClass: String?): NodeAdapter {
        return if (nodeClass === ENCRYPTED_BROWSE_NODE) {
            EncryptedBrowseNode(null, pMapFeedback)
        } else BrowseNodeModel(null, pMapFeedback.map)
    }

    override fun createEdgeAdapter(node: NodeAdapter): EdgeAdapter {
        return BrowseEdgeModel(node, mMapFeedback)
    }

    override fun createCloudAdapter(node: NodeAdapter): CloudAdapter {
        return BrowseCloudModel(node, mMapFeedback)
    }

    override fun createArrowLinkAdapter(
        source: NodeAdapter,
        target: NodeAdapter
    ): ArrowLinkAdapter {
        return BrowseArrowLinkModel(source, target, mMapFeedback)
    }

    override fun createArrowLinkTarget(
        source: NodeAdapter,
        target: NodeAdapter
    ): ArrowLinkTarget? {
        // FIXME: Need an implementation here
        return null
    }

    override fun createEncryptedNode(additionalInfo: String): NodeAdapter {
        val node = createNodeAdapter(mMapFeedback, ENCRYPTED_BROWSE_NODE)
        node.additionalInfo = additionalInfo
        return node
    }

    /* (non-Javadoc)
	 * @see freemind.modes.XMLElementAdapter#createNodeAdapter(freemind.modes.MindMap, java.lang.String)
	 */
    override fun createNodeAdapter(pMap: MindMap, pNodeClass: String): NodeAdapter {
        return createNodeAdapter(mMapFeedback, null)
    }

    companion object {
        private val ENCRYPTED_BROWSE_NODE = EncryptedBrowseNode::class.java
            .name
    }
}
