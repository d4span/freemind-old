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

import freemind.controller.actions.generated.instance.MoveNodesAction
import freemind.controller.actions.generated.instance.NodeListMember
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.util.Collections
import java.util.TreeSet
import java.util.Vector

/**
 * @author foltin
 * @date 08.04.2014
 */
class NodeUpActor
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    /**
     */
    fun moveNodes(selected: MindMapNode, selecteds: List<MindMapNode>, direction: Int) {
        val doAction = createMoveNodesAction(
            selected, selecteds,
            direction
        )
        val undoAction = createMoveNodesAction(
            selected, selecteds,
            -direction
        )
        execute(
            ActionPair(
                doAction, undoAction
            )
        )
    }

    private fun _moveNodes(selected: MindMapNode?, selecteds: List<MindMapNode>, direction: Int) {
        val comparator = if (direction == -1) null else java.util.Comparator<Int> { i1, i2 -> i2 - i1 }
        if (selected != null && !selected.isRoot) {
            val parent = selected.parentNode
            // multiple move:
            val sortedChildren = getSortedSiblings(parent)
            val range = TreeSet(comparator)
            for (node in selecteds) {
                if (node.parent !== parent) {
                    logger!!.warning(
                        "Not all selected nodes (here: " +
                            node.text + ") have the same parent " +
                            parent?.text + "."
                    )
                    return
                }
                range.add(sortedChildren.indexOf(node))
            }
            // test range for adjacent nodes:
            var last = range.iterator().next() as Int
            for (newInt in range) {
                if (Math.abs(newInt - last) > 1) {
                    logger!!.warning("Not adjacent nodes. Skipped. ")
                    return
                }
                last = newInt
            }
            for (position in range) {
                // from above:
                val node = sortedChildren[position] as MindMapNode
                moveNodeTo(node, parent, direction)
            }
        }
    }

    /**
     * The direction is used if side left and right are present. then the next
     * suitable place on the same side# is searched. if there is no such place,
     * then the side is changed.
     *
     * @return returns the new index.
     */
    private fun moveNodeTo(
        newChild: MindMapNode,
        parent: MindMapNode,
        direction: Int
    ): Int? {
        val model = exMapFeedback?.map
        var newIndex: Int?
        val maxIndex = parent.childCount
        val sortedNodesIndices = getSortedSiblings(parent)
        var newPositionInVector = (
            sortedNodesIndices.indexOf(newChild) +
                direction
            )
        if (newPositionInVector < 0) {
            newPositionInVector = maxIndex - 1
        }
        if (newPositionInVector >= maxIndex) {
            newPositionInVector = 0
        }
        val destinationNode = sortedNodesIndices[newPositionInVector] as MindMapNode
        newIndex = model?.getIndexOfChild(parent, destinationNode)
        exMapFeedback?.removeNodeFromParent(newChild)
        exMapFeedback?.insertNodeInto(newChild, parent, newIndex ?: 0)
        exMapFeedback?.nodeChanged(newChild)
        return newIndex
    }

    /**
     * Sorts nodes by their left/right status. The left are first.
     */
    private fun getSortedSiblings(node: MindMapNode?): Vector<MindMapNode> {
        val nodes = Vector<MindMapNode>()
        val i = node?.childrenUnfolded()
        while (i?.hasNext() ?: false) {
            nodes.add(i?.next())
        }
        Collections.sort(nodes) { n1, n2 ->
            val b1 = if (n1.isLeft) 0 else 1
            val b2 = if (n2.isLeft) 0 else 1
            b1 - b2
        }
        // logger.finest("Sorted nodes "+ nodes);
        return nodes
    }

    override fun act(action: XmlAction) {
        if (action is MoveNodesAction) {
            val moveAction = action
            val selected = getNodeFromID(
                moveAction
                    .node
            )
            val selecteds = Vector<MindMapNode>()
            val i = moveAction.listNodeListMemberList.iterator()
            while (i.hasNext()) {
                val node = i.next()
                selecteds.add(getNodeFromID(node?.node))
            }
            _moveNodes(selected, selecteds, moveAction.direction)
        }
    }

    override fun getDoActionClass(): Class<MoveNodesAction> {
        return MoveNodesAction::class.java
    }

    private fun createMoveNodesAction(
        selected: MindMapNode,
        selecteds: List<MindMapNode>,
        direction: Int
    ): MoveNodesAction {
        val moveAction = MoveNodesAction()
        moveAction.direction = direction
        moveAction.node = getNodeID(selected)
        // selectedNodes list
        for (node in selecteds) {
            val nodeListMember = NodeListMember()
            nodeListMember.node = getNodeID(node)
            moveAction.addNodeListMember(nodeListMember)
        }
        return moveAction
    }
}
