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

import freemind.modes.EdgeAdapter
import freemind.modes.MindMapNode
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager
import javax.swing.JComponent

internal class NodeViewFactory // Singleton
private constructor() {
    private class ContentPane() : JComponent() {
        init {
            layout = layoutManager
        }

        companion object {
            private val layoutManager: LayoutManager = ContentPaneLayout()
        }
    }

    private class ContentPaneLayout : LayoutManager {
        override fun addLayoutComponent(name: String, comp: Component) {}
        override fun layoutContainer(parent: Container) {
            val componentCount = parent.componentCount
            val width = parent.width
            var y = 0
            for (i in 0 until componentCount) {
                val component = parent.getComponent(i)
                if (component.isVisible) {
                    val preferredCompSize = component
                        .preferredSize
                    if (component is MainView) {
                        component.setBounds(
                            0, y, width,
                            preferredCompSize.height
                        )
                    } else {
                        val x = (component.alignmentX * (width - preferredCompSize.width)).toInt()
                        component.setBounds(
                            x, y, preferredCompSize.width,
                            preferredCompSize.height
                        )
                    }
                    y += preferredCompSize.height
                }
            }
        }

        override fun minimumLayoutSize(parent: Container): Dimension {
            return preferredLayoutSize(parent)
        }

        override fun preferredLayoutSize(parent: Container): Dimension {
            val prefSize = Dimension(0, 0)
            val componentCount = parent.componentCount
            for (i in 0 until componentCount) {
                val component = parent.getComponent(i)
                if (component.isVisible) {
                    val preferredCompSize = component
                        .preferredSize
                    prefSize.height += preferredCompSize.height
                    prefSize.width = Math.max(
                        prefSize.width,
                        preferredCompSize.width
                    )
                }
            }
            return prefSize
        }

        override fun removeLayoutComponent(comp: Component) {}
    }

    private var sharpBezierEdgeView: EdgeView? = null
        get() {
            if (field == null) {
                field = SharpBezierEdgeView()
            }
            return field
        }
    private var sharpLinearEdgeView: EdgeView? = null
        get() {
            if (field == null) {
                field = SharpLinearEdgeView()
            }
            return field
        }
    private var bezierEdgeView: EdgeView? = null
        get() {
            if (field == null) {
                field = BezierEdgeView()
            }
            return field
        }
    private var linearEdgeView: EdgeView? = null
        get() {
            if (field == null) {
                field = LinearEdgeView()
            }
            return field
        }

    fun getEdge(newView: NodeView): EdgeView {
        val edgeStyle = newView.model.edge.styleAsInt
        return when (edgeStyle) {
            EdgeAdapter.INT_EDGESTYLE_LINEAR -> linearEdgeView!!
            EdgeAdapter.INT_EDGESTYLE_BEZIER -> bezierEdgeView!!
            EdgeAdapter.INT_EDGESTYLE_SHARP_LINEAR -> sharpLinearEdgeView!!
            EdgeAdapter.INT_EDGESTYLE_SHARP_BEZIER -> sharpBezierEdgeView!!
            else -> linearEdgeView!!
        }
    }

    /**
     * Factory method which creates the right NodeView for the model.
     */
    fun newNodeView(
        model: MindMapNode?,
        map: MapView,
        parent: Container?
    ): NodeView? {
        if (model != null) {
            val newView = NodeView(model, map, parent!!)
            if (model.isRoot) {
                val mainView: MainView = RootMainView()
                newView.setMainView(mainView)
                newView.layout = VerticalRootNodeViewLayout.instance
            } else {
                newView.setMainView(newMainView(model))
                if (newView.isLeft) {
                    newView.layout = LeftNodeViewLayout.instance
                } else {
                    newView.layout = RightNodeViewLayout.instance
                }
            }
            map.addViewer(model, newView)
            newView.update()
            fireNodeViewCreated(newView)
            return newView
        } else return null
    }

    fun newMainView(model: MindMapNode): MainView {
        if (model.isRoot) {
            return RootMainView()
        }
        return if (model.style == MindMapNode.STYLE_FORK) {
            ForkMainView()
        } else if (model.style == MindMapNode.STYLE_BUBBLE) {
            BubbleMainView()
        } else {
            System.err.println("Tried to create a NodeView of unknown Style.")
            ForkMainView()
        }
    }

    private fun fireNodeViewCreated(newView: NodeView) {
        newView.map.viewFeedback
            .onViewCreatedHook(newView)
    }

    fun newContentPane(): JComponent {
        return ContentPane()
    }

    companion object {
        private var factory: NodeViewFactory? = null
        val instance: NodeViewFactory?
            get() {
                if (factory == null) {
                    factory = NodeViewFactory()
                }
                return factory
            }
    }
}
