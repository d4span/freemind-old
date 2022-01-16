/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2007  Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package freemind.view.mindmapview

import freemind.main.Resources
import freemind.main.Tools
import freemind.modes.MindMapNode
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.Point
import java.util.logging.Logger
import javax.swing.JComponent

/**
 * @author foltin
 * @date 11.07.2013
 */
abstract class NodeViewLayoutAdapter : NodeViewLayout {
    @JvmField
    protected val LISTENER_VIEW_WIDTH = 10
    @JvmField
    protected var location = Point()

    /**
     * @return Returns the view.
     */
    protected var view: NodeView? = null
        private set

    /**
     * @return Returns the model.
     */
    protected var model: MindMapNode? = null
        private set

    /**
     * @return Returns the childCount.
     */
    protected var childCount = 0
        private set

    /**
     * @return Returns the content.
     */
    protected var content: JComponent? = null
        private set

    /**
     * @return Returns the vGap.
     */
    var vGap: Int? = 0
        private set

    /**
     * @return Returns the spaceAround.
     */
    var spaceAround = 0
        private set

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
    }

    override fun addLayoutComponent(arg0: String, arg1: Component) {}

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
    override fun removeLayoutComponent(arg0: Component) {}

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
    override fun minimumLayoutSize(arg0: Container): Dimension {
        if (minDimension == null) minDimension = Dimension(0, 0)
        return minDimension!!
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
    override fun preferredLayoutSize(c: Container): Dimension {
        if (!c.isValid) {
            c.validate()
        }
        return c.size
    }

    override fun layoutContainer(c: Container) {
        setUp(c)
        layout()
        val location2 = view!!.location
        Tools.convertPointToAncestor(view, location2, view!!.map)
        // 		logger.info("Layouting node '" + view.getModel() + "' to " + location2);
        layoutOtherItems()
        shutDown()
    }

    /**
     *
     */
    private fun layoutOtherItems() {
        val componentCount = view!!.componentCount
        for (i in 0 until componentCount) {
            val component = view!!.getComponent(i)
            if (component is NodeMotionListenerView) {
                layoutNodeMotionListenerView(component)
            } else if (component is NodeFoldingComponent) {
                layoutNodeFoldingComponent(component)
            }
        }
    }

    protected abstract fun layout()
    private fun setUp(c: Container) {
        val localView = c as NodeView
        val localChildCount = localView.componentCount - 1
        for (i in 0 until localChildCount) {
            localView.getComponent(i).validate()
        }
        view = localView
        model = localView.model
        childCount = localChildCount
        content = localView.content
        if (model?.isVisible() ?: false) {
            vGap = view!!.vGap
        } else {
            vGap = view!!.visibleParentView?.vGap
        }
        spaceAround = view!!.map.getZoomed(NodeView.SPACE_AROUND)
    }

    private fun shutDown() {
        view = null
        model = null
        content = null
        childCount = 0
        vGap = 0
        spaceAround = 0
    }

    protected fun getChildContentHeight(isLeft: Boolean): Int {
        val childCount = childCount
        if (childCount == 0) {
            return 0
        }
        var height = 0
        var count = 0
        for (i in 0 until childCount) {
            val component = view!!.getComponent(i)
            if (component is NodeView) {
                val child = component
                if (child.isLeft == isLeft) {
                    val additionalCloudHeigth = child
                        .additionalCloudHeigth
                    val contentHeight = child.content?.height
                    height = height + (contentHeight?.plus(additionalCloudHeigth) ?: 0)
                    count++
                }
            }
        }
        return height + (vGap?.times(count - 1) ?: 0)
    }

    /**
     * @param isLeft
     * @return a shift, which is less than or equal zero
     */
    protected fun getChildVerticalShift(isLeft: Boolean): Int {
        var shift = 0
        var isFirstNodeView = false
        var foundNodeView = false
        for (i in 0 until childCount) {
            val component = view!!.getComponent(i)
            if (component is NodeView) {
                val child = component
                if (child.isLeft == isLeft) {
                    val childShift = child.shift
                    if (childShift < 0 || isFirstNodeView) {
                        shift += childShift
                        isFirstNodeView = false
                    }
                    shift -= child.content?.y?.minus(spaceAround) ?: 0
                    foundNodeView = true
                }
            }
        }
        if (foundNodeView) {
            shift -= spaceAround
        }
        return shift
    }

    protected val childHorizontalShift: Int
        get() {
            if (childCount == 0) return 0
            var shift = 0
            for (i in 0 until childCount) {
                val component = view!!.getComponent(i)
                if (component is NodeView) {
                    val child = component
                    var shiftCandidate: Int
                    if (child.isLeft) {
                        shiftCandidate = (0.minus(child.content?.x ?: 0) - (child.content?.width ?: 0))
                        if (child.isContentVisible) {
                            shiftCandidate -= (
                                child.hGap +
                                    child.additionalCloudHeigth / 2
                                )
                        }
                    } else {
                        shiftCandidate = 0.minus(child.content?.x ?: 0)
                        if (child.isContentVisible) {
                            shiftCandidate += child.hGap
                        }
                    }
                    shift = Math.min(shift, shiftCandidate)
                }
            }
            return shift
        }

    /**
     * Implemented in the base class, as the root layout needs it, too.
     * @param childVerticalShift
     */
    protected fun placeRightChildren(childVerticalShift: Int) {
        val baseX = content!!.x + content!!.width
        var y = content!!.y + childVerticalShift
        var right = baseX + spaceAround
        var child: NodeView? = null
        for (i in 0 until childCount) {
            val componentC = view!!.getComponent(i)
            if (componentC is NodeView) {
                val component = componentC
                if (component.isLeft) {
                    continue
                }
                child = component
                val additionalCloudHeigth = child.additionalCloudHeigth / 2
                y += additionalCloudHeigth
                val shiftY = child.shift
                val childHGap = if (child.content?.isVisible ?: false) child
                    .hGap else 0
                val x = baseX + childHGap - (child.content?.x ?: 0)
                if (shiftY < 0) {
                    child.setLocation(x, y)
                    y -= shiftY
                } else {
                    y += shiftY
                    child.setLocation(x, y)
                }
                // 				logger.info("Place of child " + component.getModel().getText() + ": " + child.getLocation());
                y += (
                    child.height - 2 * spaceAround + (vGap ?: 0) +
                        additionalCloudHeigth
                    )
                right = Math.max(
                    right,
                    x + child.width +
                        additionalCloudHeigth
                )
            }
        }
        val bottom = (
            content!!.y + content!!.height +
                spaceAround
            )
        if (child != null) {
            view!!.setSize(
                right,
                Math.max(
                    bottom,
                    child.y + child.height +
                        child.additionalCloudHeigth / 2
                )
            )
        } else {
            view!!.setSize(right, bottom)
        }
    }

    /**
     * Implemented in the base class, as the root layout needs it, too.
     * @param childVerticalShift
     */
    protected fun placeLeftChildren(childVerticalShift: Int) {
        val baseX = content!!.x
        var y = content!!.y + childVerticalShift
        var right = baseX + content!!.width + spaceAround
        var child: NodeView? = null
        for (i in 0 until childCount) {
            val componentC = view!!.getComponent(i)
            if (componentC is NodeView) {
                val component = componentC
                if (!component.isLeft) {
                    continue
                }
                child = component
                val additionalCloudHeigth = child.additionalCloudHeigth / 2
                y += additionalCloudHeigth
                val shiftY = child.shift
                val childHGap = if (child.content?.isVisible ?: false) child
                    .hGap else 0
                val x = (
                    baseX - childHGap - (child.content?.x ?: 0) -
                        (child.content?.width ?: 0)
                    )
                if (shiftY < 0) {
                    child.setLocation(x, y)
                    y -= shiftY
                } else {
                    y += shiftY
                    child.setLocation(x, y)
                }
                y += (
                    child.height - 2 * spaceAround + (vGap ?: 0) +
                        additionalCloudHeigth
                    )
                right = Math.max(right, x + child.width)
            }
        }
        val bottom = (
            content!!.y + content!!.height +
                spaceAround
            )
        if (child != null) {
            view!!.setSize(
                right,
                Math.max(
                    bottom,
                    child.y + child.height +
                        child.additionalCloudHeigth / 2
                )
            )
        } else {
            view!!.setSize(right, bottom)
        }
    }

    /* (non-Javadoc)
	 * @see freemind.view.mindmapview.NodeViewLayout#layoutNodeFoldingComponent(freemind.view.mindmapview.NodeFoldingListenerView)
	 */
    override fun layoutNodeFoldingComponent(
        pFoldingComponent: NodeFoldingComponent?
    ) {
        val movedView = pFoldingComponent!!.nodeView
        val location = movedView.foldingMarkPosition
        val content = movedView.content
        Tools.convertPointToAncestor(content, location, pFoldingComponent.parent)
        pFoldingComponent.setCorrectedLocation(location)
        val preferredSize = pFoldingComponent.preferredSize
        pFoldingComponent.setSize(preferredSize.width, preferredSize.height)
    }

    companion object {
        protected var logger: Logger? = null
        private var minDimension: Dimension? = null
    }
}
