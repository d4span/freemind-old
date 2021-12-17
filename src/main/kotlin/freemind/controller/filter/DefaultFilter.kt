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
 * Created on 07.05.2005
 *
 */
package freemind.controller.filter

import freemind.controller.*
import freemind.controller.filter.condition.*
import freemind.modes.MindMapNode
import freemind.view.mindmapview.MapView
import freemind.view.mindmapview.NodeView

/**
 * @author dimitri 07.05.2005
 */
class DefaultFilter(override val condition: Condition?, areAnchestorsShown: Boolean,
                    areDescendantsShown: Boolean) : Filter {
    private var options = 0

    /**
     */
    init {
        options = Filter.Companion.FILTER_INITIAL_VALUE or Filter.Companion.FILTER_SHOW_MATCHED
        if (areAnchestorsShown) options += Filter.Companion.FILTER_SHOW_ANCESTOR
        options += Filter.Companion.FILTER_SHOW_ECLIPSED
        if (areDescendantsShown) options += Filter.Companion.FILTER_SHOW_DESCENDANT
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.Filter#applyFilter(freemind.modes.MindMap)
	 */
    override fun applyFilter(c: Controller) {
        if (condition != null) {
            try {
                c.frame.setWaitingCursor(true)
                val map = c.model
                val mapView = c.view
                val root = map!!.rootNode
                resetFilter(root)
                if (filterChildren(root, c, condition.checkNode(c, root), false)) {
                    addFilterResult(root, Filter.Companion.FILTER_SHOW_ANCESTOR)
                }
                selectVisibleNode(mapView)
            } finally {
                c.frame.setWaitingCursor(false)
            }
        }
    }

    /**
     * @param c
     * TODO
     */
    private fun filterChildren(parent: MindMapNode, c: Controller,
                               isAncestorSelected: Boolean, isAncestorEclipsed: Boolean): Boolean {
        val iterator = parent.childrenUnfolded()
        var isDescendantSelected = false
        while (iterator?.hasNext() ?: false) {
            val node = iterator?.next() as MindMapNode
            isDescendantSelected = applyFilter(node, c, isAncestorSelected,
                    isAncestorEclipsed, isDescendantSelected)
        }
        return isDescendantSelected
    }

    private fun applyFilter(node: MindMapNode, c: Controller,
                            isAncestorSelected: Boolean, isAncestorEclipsed: Boolean,
                            isDescendantSelected: Boolean): Boolean {
        var isDescendantSelected = isDescendantSelected
        resetFilter(node)
        if (isAncestorSelected) addFilterResult(node, Filter.Companion.FILTER_SHOW_DESCENDANT)
        val conditionSatisfied = condition!!.checkNode(c, node)
        if (conditionSatisfied) {
            isDescendantSelected = true
            addFilterResult(node, Filter.Companion.FILTER_SHOW_MATCHED)
        } else {
            addFilterResult(node, Filter.Companion.FILTER_SHOW_HIDDEN)
        }
        if (isAncestorEclipsed) {
            addFilterResult(node, Filter.Companion.FILTER_SHOW_ECLIPSED)
        }
        if (filterChildren(node, c, conditionSatisfied || isAncestorSelected,
                        !conditionSatisfied || isAncestorEclipsed)) {
            addFilterResult(node, Filter.Companion.FILTER_SHOW_ANCESTOR)
            isDescendantSelected = true
        }
        return isDescendantSelected
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.filter.Filter#isVisible(freemind.modes.MindMapNode)
	 */
    override fun isVisible(node: MindMapNode): Boolean {
        if (condition == null) return true
        val filterResult = node.filterInfo.get()
        return ((options and Filter.Companion.FILTER_SHOW_ANCESTOR != 0 || options and Filter.Companion.FILTER_SHOW_ECLIPSED >= filterResult and Filter.Companion.FILTER_SHOW_ECLIPSED)
                && options and filterResult and Filter.Companion.FILTER_SHOW_ECLIPSED.inv() != 0)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areMatchedShown()
	 */
    override fun areMatchedShown(): Boolean {
        return true
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areHiddenShown()
	 */
    override fun areHiddenShown(): Boolean {
        return false
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areAncestorsShown()
	 */
    override fun areAncestorsShown(): Boolean {
        return 0 != options and Filter.Companion.FILTER_SHOW_ANCESTOR
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areDescendantsShown()
	 */
    override fun areDescendantsShown(): Boolean {
        return 0 != options and Filter.Companion.FILTER_SHOW_DESCENDANT
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.filter.Filter#areEclipsedShown()
	 */
    override fun areEclipsedShown(): Boolean {
        return true
    }

    companion object {
        fun selectVisibleNode(mapView: MapView?) {
            val selectedNodes = mapView?.selecteds
            val lastSelectedIndex = if (selectedNodes != null) selectedNodes.size - 1 else -1
            if (lastSelectedIndex == -1) {
                return
            }
            val iterator = selectedNodes?.listIterator(lastSelectedIndex)
            while (iterator?.hasPrevious() ?: false) {
                val previous = iterator?.previous()
                if (previous?.model?.isContentVisible ?: false) {
                    mapView?.toggleSelected(previous)
                }
            }
            var selected = mapView?.getSelected()
            if (selected?.model?.isContentVisible ?: false) {
                selected = getNearestVisibleParent(selected)
                mapView?.selectAsTheOnlyOneSelected(selected)
            }
            mapView?.siblingMaxLevel = selected?.model?.nodeLevel ?: Int.MIN_VALUE
        }

        private fun getNearestVisibleParent(selectedNode: NodeView?): NodeView? {
            return if (selectedNode?.model?.isContentVisible ?: false) selectedNode else getNearestVisibleParent(selectedNode?.parentView)
        }

        fun resetFilter(node: MindMapNode) {
            node.filterInfo.reset()
        }

        fun addFilterResult(node: MindMapNode, flag: Int) {
            node.filterInfo.add(flag)
        }
    }
}