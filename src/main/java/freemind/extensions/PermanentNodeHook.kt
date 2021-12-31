/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
/*
 * Created on 06.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.extensions

import freemind.main.XMLElement
import freemind.modes.MindMapNode
import freemind.view.mindmapview.NodeView
import java.io.IOException
import java.io.Writer

/**
 * Basic interface for all node hooks that are permanent.
 * Thus, there are methods that inform the plugin about changes on the node, it is stick to.
 * Moreover, methods for loading and saving the hook are present.
 *
 *
 * @author foltin
 */
interface PermanentNodeHook : NodeHook {
    fun onFocusNode(nodeView: NodeView?)

    /**
     *
     */
    fun onLostFocusNode(nodeView: NodeView?)

    /**
     * Fired after node is getting visible (is unfolded after having been folded).
     */
    fun onViewCreatedHook(nodeView: NodeView?)

    /**
     * Fired after a node is getting invisible (folded).
     */
    fun onViewRemovedHook(nodeView: NodeView?)

    /**
     * If the node I belong to is changed, I get this notification.
     */
    fun onUpdateNodeHook()

    /**
     * Is called if the addedChildNode is inserted as a direct child of the
     * node, this hook is attached to. The cases in which this method is called
     * contain new nodes, paste, move, etc.
     *
     * Ah, don't call propagate in this method, as paste introduces nodes with
     * the hook and you'll have them twice, ... see onNewChild
     */
    fun onAddChild(addedChildNode: MindMapNode?)

    /**
     * Is only called, if a new nodes is inserted as a child. Remark: In this
     * case onAddChild is called too and moreover *before* this method. see
     * onAddChild.
     */
    fun onNewChild(newChildNode: MindMapNode?)

    /**
     * This method is called, if a child is added to me or to any of my
     * children. (See onUpdateChildrenHook)
     */
    fun onAddChildren(addedChild: MindMapNode?)
    fun onRemoveChild(oldChildNode: MindMapNode?)

    /**
     * This method is called, if a child is removed to me or to any of my
     * children. (See onUpdateChildrenHook)
     *
     * @param oldDad
     * TODO
     */
    fun onRemoveChildren(oldChildNode: MindMapNode?, oldDad: MindMapNode?)

    /**
     * If any of my children is updated, I get this notification.
     */
    fun onUpdateChildrenHook(updatedNode: MindMapNode?)

    /**
     */
    fun save(hookElement: XMLElement?)

    /**
     */
    fun loadFrom(child: XMLElement?)

    /**
     * Can be used to adjust some things after a paste action. (Currently it is used for clones).
     */
    fun processUnfinishedLinks()

    /**
     * Can be used to contribute to the standard html export.
     * @param pFileout
     * @throws IOException
     */
    @Throws(IOException::class)
    fun saveHtml(pFileout: Writer?)
}
