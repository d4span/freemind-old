/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2005  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 09.11.2005
 */
package freemind.modes.common

import freemind.main.Tools.BooleanHolder
import freemind.modes.ControllerAdapter
import freemind.modes.MindMapNode
import java.awt.event.ActionEvent
import java.util.logging.Logger
import javax.swing.AbstractAction

/**
 * @author foltin
 */
class CommonToggleFoldedAction(private val modeController: ControllerAdapter) : AbstractAction(
    modeController.getText("toggle_folded")
) {
    private val logger: Logger

    init {
        logger = modeController.frame.getLogger(this.javaClass.name)
    }

    override fun actionPerformed(e: ActionEvent) {
        toggleFolded()
    }

    @JvmOverloads
    fun toggleFolded(listIterator: ListIterator<MindMapNode> = modeController.selecteds.listIterator()) {
        val fold = getFoldingState(reset(listIterator))
        val i = reset(listIterator)
        while (i.hasNext()) {
            val node = i.next()
            modeController.setFolded(node, fold)
        }
    }

    companion object {
        @JvmStatic
        fun reset(iterator: ListIterator<MindMapNode>): ListIterator<MindMapNode> {
            while (iterator.hasPrevious()) {
                iterator.previous()
            }
            return iterator
        }

        /**
         * Determines whether the nodes should be folded or unfolded depending on
         * their states. If not all nodes have the same folding status, the result
         * means folding
         *
         * @param iterator
         * an iterator of MindMapNodes.
         * @return true, if the nodes should be folded.
         */
        @JvmStatic
        fun getFoldingState(iterator: ListIterator<MindMapNode>): Boolean {
            /*
		 * Retrieve the information whether or not all nodes have the same
		 * folding state.
		 */
            var state: BooleanHolder? = null
            var allNodeHaveSameFoldedStatus = true
            while (iterator.hasNext()) {
                val node = iterator.next()
                if (node.childCount == 0) {
                    // no folding state change for unfoldable nodes.
                    continue
                }
                if (state == null) {
                    state = BooleanHolder()
                    state.value = node.isFolded
                } else {
                    if (node.isFolded != state.value) {
                        allNodeHaveSameFoldedStatus = false
                        break
                    }
                }
            }
            /* if the folding state is ambiguous, the nodes are folded. */
            var fold = true
            if (allNodeHaveSameFoldedStatus && state != null) {
                fold = !state.value
            }
            return fold
        }
    }
}
