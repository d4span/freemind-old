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
/*$Id: NodeHookAdapter.java,v 1.1.4.4.2.2 2007/04/21 15:11:20 dpolivaev Exp $*/
package freemind.extensions

import freemind.modes.MindMap
import freemind.modes.MindMapNode

/**
 * Straight forward implementation with some helpers.
 *
 * @author foltin
 *
 */
abstract class NodeHookAdapter
/**
 *
 */
    : HookAdapter(), NodeHook {
    private var map: MindMap? = null
    private var node: MindMapNode? = null

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#invoke()
	 */
    override fun invoke(node: MindMapNode?) {
        logger?.finest("invoke(node) called.")
    }

    /**
     */
    open fun getNode(): MindMapNode? {
        return node
    }

    /**
     */
    override fun setNode(node: MindMapNode?) {
        this.node = node
    }

    /**
     */
    protected fun getMap(): MindMap? {
        return map
    }

    /**
     */
    protected fun nodeChanged(node: MindMapNode?) {
        controller?.nodeChanged(node)
    }

    /**
     */
    override fun setMap(map: MindMap?) {
        this.map = map
    }

    open val nodeId: String?
        get() = controller?.getNodeID(getNode())
}