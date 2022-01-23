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
package freemind.dependencies.view.swing

import freemind.controller.filter.FilterInfo
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHook
import freemind.main.XMLElement
import freemind.modes.HistoryInformation
import freemind.modes.MapFeedback
import freemind.modes.MindIcon
import freemind.modes.MindMap
import freemind.modes.MindMapCloud
import freemind.modes.MindMapEdge
import freemind.modes.MindMapLinkRegistry
import freemind.modes.ModeController
import freemind.modes.attributes.Attribute
import java.awt.Color
import java.awt.Font
import java.io.IOException
import java.io.Writer
import java.util.SortedMap
import javax.swing.ImageIcon
import javax.swing.event.EventListenerList
import javax.swing.event.TreeModelListener
import javax.swing.tree.MutableTreeNode
import javax.swing.tree.TreePath

interface NodeRepresentation : MutableTreeNode {
    /**
     * @return the text representation of the nodes content. HTML is represented
     * as <html>....</html> see getXmlText
     */
    /**
     * Sets both text and xmlText.
     */
    var text: String?
    /**
     * @return the text representation of the nodes content as valid XML. HTML
     * is represented as <html>....</html> with proper tags (like \<br></br>/\>
     * instead of \<br></br>\>
     * and so on).
     */
    /**
     * Sets both text and xmlText.
     */
    var xmlText: String?
    /**
     * @return the text representation of the notes content as valid XML. HTML
     * is represented as <html>....</html> with proper tags (like <br></br>
     * instead of <br></br>
     * and so on).
     */
    /**
     * Sets both noteText and xmlNoteText.
     */
    var xmlNoteText: String?
    /**
     * @return the text representation of the notes content as valid HTML 3.2.
     */
    /**
     * Sets both noteText and xmlNoteText.
     */
    var noteText: String?

    /**
     * @return returns the unique id of the node. It is generated using the
     * LinkRegistry.
     */
    fun getObjectId(controller: ModeController?): String?

    /**
     * @return returns a ListIterator of all children of the node if the node is
     * unfolded. EMPTY_LIST_ITERATOR otherwise.
     */
    fun childrenFolded(): ListIterator<NodeRepresentation?>?

    /**
     * @return returns a ListIterator of all (and not only the unfolded ones!!)
     * children of the node.
     */
    fun childrenUnfolded(): ListIterator<NodeRepresentation?>?

    /**
     * @return returns a ListIterator of all (and not only the unfolded ones!!)
     * children of the node sorted in the way they occur (if called from root, this
     * has the effect to sort the children first left then right).
     */
    fun sortedChildrenUnfolded(): ListIterator<NodeRepresentation?>?

    /**
     * @return a list of (unmodifiable) children (all ones, folded and unfolded)
     * of type MindMapNode.
     */
    val children: List<NodeRepresentation?>?
    fun hasChildren(): Boolean
    val filterInfo: FilterInfo?

    /** @return -1 if the argument childNode is not a child.
     */
    fun getChildPosition(childNode: NodeRepresentation?): Int
    val nodeLevel: Int
    var link: String?

    /**
     * returns a short textual description of the text contained in the node.
     * Html is filtered out.
     */
    fun getShortText(controller: ModeController?): String?
    val edge: MindMapEdge?
    var color: Color?

    /**
     * @return the pure style string (even if the style is "AS_PARENT" or null)
     */
    val bareStyle: String?

    /**
     * currently the style may be one of MindMapNode.STYLE_BUBBLE or
     * MindMapNode.STYLE_FORK.
     */
    var style: String?

    // returns false if and only if the style is inherited from parent
    fun hasStyle(): Boolean
    val parentNode: NodeRepresentation?
    val isBold: Boolean
    val isItalic: Boolean
    val isUnderlined: Boolean
    val isStrikethrough: Boolean
    var font: Font?
    fun getFontSize(): String?
    val fontFamilyName: String?
    override fun toString(): String
    val plainTextContent: String?
    val path: TreePath?

    /**
     * Returns whether the argument is parent or parent of one of the grandpa's
     * of this node. (transitive)
     */
    fun isDescendantOf(node: NodeRepresentation?): Boolean

    /**
     * If the test node is identical or in the same family and elder as the
     * object. node.isChild..(parent) == true means: parent -> .. -> node exists
     * in the tree.
     *
     * @see isDecendantOf
     */
    fun isDescendantOfOrEqual(pParentNode: NodeRepresentation?): Boolean
    val isRoot: Boolean
    var isFolded: Boolean
    var isLeft: Boolean
    var shiftY: Int
    fun calcShiftY(): Int
    var vGap: Int
    var hGap: Int
    fun setFontSize(fontSize: Int)
    // fc, 06.10.2003:
    /** Is a vector of MindIcon s  */
    val icons: List<MindIcon?>?
    fun addIcon(icon: MindIcon?, position: Int)

    /* @return returns the new amount of icons. */
    fun removeIcon(position: Int): Int

    // end, fc, 24.9.2003
    // clouds, fc, 08.11.2003:
    var cloud: MindMapCloud?

    // end clouds.
    // fc, 24.2.2004: background color:
    var backgroundColor: Color?
    // hooks, fc 28.2.2004:
    /**
     * After a map creation, all hooks are present via this method, but still
     * not activated.
     *
     * @return a list of PermanentNodeHook elements.
     */
    val hooks: List<PermanentNodeHook?>?

    /**
     * After activation, this method returns the hooks of this node.
     *
     * @return a list of PermanentNodeHook elements
     */
    val activatedHooks: Collection<PermanentNodeHook?>?

    /**
     * Adds the hook to the list of hooks to my node. Does not invoke the hook!
     *
     * @return returns the input parameter hook
     */
    fun addHook(hook: PermanentNodeHook?): PermanentNodeHook?
    fun invokeHook(hook: NodeHook?)

    /**
     * Removes the hook from the activated hooks, calls shutdown method of the
     * hook and removes the hook from allHook belonging to the node afterwards.
     */
    fun removeHook(hook: PermanentNodeHook?)

    /**
     * Removes all hooks from this node.
     *
     * @param node
     */
    fun removeAllHooks()

    // end hooks
    // tooltips,fc 29.2.2004
    fun setToolTip(key: String?, tip: String?)
    val toolTip: SortedMap<String?, String?>?
    // additional info, fc, 15.12.2004
    /**
     * Is only used to store encrypted content of an encrypted mind map node.
     *
     * @see NodeRepresentation.setAdditionalInfo
     */
    /**
     * This method can be used to store non-visual additions to a node.
     * Currently, it is used for encrypted nodes to store the encrypted content.
     */
    var additionalInfo: String?

    /**
     * @return a flat copy of this node including all extras like notes, etc.
     * But the children are not copied!
     */
    fun shallowCopy(): NodeRepresentation?

    /**
     * @param saveHidden
     * TODO: Seems not to be used. Remove or fill with live.
     * @param saveChildren
     * if true, the save recurses to all of the nodes children.
     */
    @Throws(IOException::class)
    fun save(
        writer: Writer?, registry: MindMapLinkRegistry?,
        saveHidden: Boolean, saveChildren: Boolean
    ): XMLElement?
    // fc, 10.2.2005:
    /**
     * State icons are icons that are not saved. They indicate that this node is
     * special.
     */
    val stateIcons: Map<String?, ImageIcon?>?

    /**
     * @param icon
     * use null to remove the state icon. Then it is not required,
     * that the key already exists.
     */
    fun setStateIcon(key: String?, icon: ImageIcon?)

    // fc, 11.4.2005:
    var historyInformation: HistoryInformation?
    val isVisible: Boolean

    /**
     * @return true, if there is exactly one visible child.
     */
    fun hasExactlyOneVisibleChild(): Boolean

    /**
     * @return true, if there is at least one visible child.
     */
    fun hasVisibleChilds(): Boolean
    val mapFeedback: MapFeedback?
    val map: MindMap?

    /**
     * @return an unmodifiable list of all attribute keys as String. There can
     * be double entries.
     */
    val attributeKeyList: List<String?>?

    /**
     * @return an unmodifiable list of all attributes.
     */
    val attributes: List<Attribute?>?

    /**
     * @return the amount of attributes.
     */
    val attributeTableLength: Int

    /**
     * @param pPosition
     * the null based position.
     * @return a copy of the node's attribute.
     * @throws IllegalArgumentException if position is out of range
     */
    fun getAttribute(pPosition: Int): Attribute?

    /**
     * Searches for the first attribute with the given key. This is a
     * convenience function. see MindMapActions.editAttribute to set the value
     * to a different one.
     *
     * @param pKey
     * is the name of the attribute
     * @return the value of the attribute or null, if not found.
     */
    fun getAttribute(pKey: String?): String?

    /**
     * @param key
     * the name of the attribute
     * @return the index of the first occurence of an attribute with this key,
     * or -1 if not found.
     */
    fun getAttributePosition(key: String?): Int

    /**
     * Sets the attribute to the given value.
     * Don't set the attributes directly here. Use the [MindMapActions] methods instead.
     */
    fun setAttribute(pPosition: Int, pAttribute: Attribute?)

    /**
     * Insert the attribute to the given value.
     * Don't set the attributes directly here. Use the [MindMapActions] methods instead.
     */
    fun insertAttribute(pPosition: Int, pAttribute: Attribute?)

    /**
     * @param pAttribute
     * @return the index of the new attribute
     */
    fun addAttribute(pAttribute: Attribute?): Int

    /**
     * @param pPosition
     */
    fun removeAttribute(pPosition: Int)
    fun addTreeModelListener(l: TreeModelListener?)
    fun removeTreeModelListener(l: TreeModelListener?)
    val listeners: EventListenerList?
    val isNewChildLeft: Boolean

    /**
     * Some nodes can't get new children or have other changes (encrypted nodes
     * for example).
     */
    val isWriteable: Boolean

    /**
     * @return true, if one of its parents is folded. If itself is folded, doesn't matter.
     */
    fun hasFoldedParents(): Boolean

    companion object {
        @JvmField
        public final val STYLE_BUBBLE = "bubble"
        @JvmField
        public final val STYLE_FORK = "fork"
        @JvmField
        public final val STYLE_COMBINED = "combined"
        @JvmField
        public final val STYLE_AS_PARENT = "as_parent"
        @JvmField
        public final val NODE_STYLES = arrayOf(
            STYLE_FORK,
            STYLE_BUBBLE, STYLE_AS_PARENT, STYLE_COMBINED
        )
    }
}