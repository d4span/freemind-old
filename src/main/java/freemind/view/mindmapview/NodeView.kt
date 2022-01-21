/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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

import freemind.controller.Controller.Companion.addPropertyChangeListenerAndPropagate
import freemind.main.FreeMind
import freemind.main.FreeMindMain
import freemind.main.HtmlTools
import freemind.main.Resources
import freemind.main.Tools
import freemind.modes.MindMapNode
import freemind.modes.NodeAdapter
import freemind.preferences.FreemindPropertyListener
import freemind.view.ImageFactory
import freemind.view.mindmapview.CloudView.Companion.getAdditionalHeigth
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.dnd.DnDConstants
import java.awt.dnd.DragGestureListener
import java.awt.dnd.DragSource
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetListener
import java.net.MalformedURLException
import java.util.LinkedList
import java.util.TreeMap
import java.util.logging.Logger
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.SwingConstants
import javax.swing.ToolTipManager
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener

/**
 * This class represents a single Node of a MindMap (in analogy to
 * TreeCellRenderer).
 */
class NodeView(
    model: MindMapNode,
    map: MapView,
    parent: Container
) : JComponent(), TreeModelListener {
    override fun setFocusCycleRoot(pFocusCycleRoot: Boolean) {
        // FIXME: On purpose removed. test this!
        // super.setFocusCycleRoot(pFocusCycleRoot);
    }

    var model: MindMapNode
        protected set
    var map: MapView
        protected set
    private var mainView: MainView? = null

    // appearing
    // GradientBox
    // on
    // drag over
    var isLong = false
        private set
    var maxToolTipWidth = 0
        get() {
            if (field == 0) {
                field = try {
                    viewFeedback.getProperty(
                        "max_tooltip_width"
                    )!!.toInt()
                } catch (e: NumberFormatException) {
                    600
                }
            }
            return field
        }
        private set
    private var preferredChild: NodeView? = null
    private var contentPane: JComponent? = null
    var motionListenerView: NodeMotionListenerView? = null
        protected set
    private var mFoldingListener: NodeFoldingComponent? = null

    init {
        if (logger == null) {
            logger = Resources.getInstance()
                .getLogger(this.javaClass.name)
        }
        if (sListener == null) {
            val listener = object : FreemindPropertyListener {
                override fun propertyChanged(
                    propertyName: String?,
                    newValue: String?,
                    oldValue: String?
                ) {
                    if (Tools.safeEquals(
                            propertyName,
                            FreeMind.TOOLTIP_DISPLAY_TIME
                        )
                    ) {
                        // control tooltip times:
                        ToolTipManager.sharedInstance().dismissDelay = Resources.getInstance().getIntProperty(
                            FreeMind.TOOLTIP_DISPLAY_TIME, 4000
                        )
                    }
                }
            }
            sListener = listener
            addPropertyChangeListenerAndPropagate(listener)
        }
        isFocusCycleRoot = true
        this.model = model
        this.map = map
        val parentNode = model.parent
        val index = parentNode?.getIndex(model) ?: 0
        parent.add(this, index)
        addFoldingListener()
    }

    protected fun addFoldingListener() {
        if (mFoldingListener == null && model.hasVisibleChilds() && !model.isRoot) {
            mFoldingListener = NodeFoldingComponent(this)
            add(mFoldingListener, componentCount - 1)
            mFoldingListener!!.addActionListener { viewFeedback.setFolded(model, !model.isFolded) }
        }
    }

    protected fun removeFoldingListener() {
        if (mFoldingListener != null) {
            mFoldingListener!!.dispose()
            remove(mFoldingListener)
            mFoldingListener = null
        }
    }

    fun propertyChanged() {
    }

    fun setMainView(newMainView: MainView?) {
        if (mainView != null) {
            val c = mainView!!.parent
            var i: Int
            i = c.componentCount - 1
            while (i >= 0 &&
                mainView !== c.getComponent(i)
            ) {
                i--
            }
            c.remove(i)
            ToolTipManager.sharedInstance().unregisterComponent(mainView)
            mainView!!.removeMouseListener(map.nodeMouseMotionListener)
            mainView!!.removeMouseMotionListener(
                map
                    .nodeMouseMotionListener
            )
            c.add(newMainView, i)
        } else {
            add(newMainView)
        }
        mainView = newMainView
        ToolTipManager.sharedInstance().registerComponent(mainView)
        mainView!!.addMouseListener(map.nodeMouseMotionListener)
        mainView!!.addMouseMotionListener(map.nodeMouseMotionListener)
        addDragListener(map.nodeDragListener)
        addDropListener(map.nodeDropListener)
        if (!model.isRoot && "true" == map.viewFeedback.getProperty(FreeMindMain.ENABLE_NODE_MOVEMENT)) {
            motionListenerView = NodeMotionListenerView(this)
            add(motionListenerView)
        }
    }

    protected fun removeFromMap() {
        isFocusCycleRoot = false
        parent.remove(this)
        if (motionListenerView != null) {
            remove(motionListenerView)
            motionListenerView = null
        }
        removeFoldingListener()
        ToolTipManager.sharedInstance().unregisterComponent(mainView)
    }

    fun addDragListener(dgl: DragGestureListener?) {
        if (dgl == null) return
        val dragSource = DragSource.getDefaultDragSource()
        dragSource.createDefaultDragGestureRecognizer(
            getMainView(),
            DnDConstants.ACTION_COPY or DnDConstants.ACTION_MOVE
                or DnDConstants.ACTION_LINK,
            dgl
        )
    }

    fun addDropListener(dtl: DropTargetListener?) {
        if (dtl == null) return
        val dropTarget = DropTarget(getMainView(), dtl)
        dropTarget.isActive = true
    }

    val isRoot: Boolean
        get() = model.isRoot

    /* fc, 25.1.2004: Refactoring necessary: should call the model. */
    fun isSiblingOf(myNodeView: NodeView): Boolean {
        return parentView === myNodeView.parentView
    }

    /* fc, 25.1.2004: Refactoring necessary: should call the model. */
    fun isChildOf(myNodeView: NodeView): Boolean {
        return parentView === myNodeView
    }

    /* fc, 25.1.2004: Refactoring necessary: should call the model. */
    fun isParentOf(myNodeView: NodeView?): Boolean {
        return this === myNodeView?.parentView
    }

    /**
     * Returns the coordinates occupied by the node and its children as a vector
     * of four point per node.
     */
    fun getCoordinates(inList: LinkedList<Point>) {
        getCoordinates(inList, 0, false, 0, 0)
    }

    private fun getCoordinates(
        inList: LinkedList<Point>,
        additionalDistanceForConvexHull: Int,
        byChildren: Boolean,
        transX: Int,
        transY: Int
    ) {
        var distanceForConvexHull = additionalDistanceForConvexHull
        if (!isVisible) return
        if (isContentVisible) {
            val cloud = model.cloud

            // consider existing clouds of children
            if (byChildren && cloud != null) {
                distanceForConvexHull += getAdditionalHeigth(cloud, this) / 5
            }
            val x = transX + content!!.x - deltaX
            val y = transY + content!!.y - deltaY
            val width = mainViewWidthWithFoldingMark
            val heightWithFoldingMark = mainViewHeightWithFoldingMark
            val height = Math.max(
                heightWithFoldingMark, content?.getHeight() ?: 0
            )
            inList.addLast(
                Point(
                    -distanceForConvexHull + x,
                    -distanceForConvexHull + y
                )
            )
            inList.addLast(
                Point(
                    -distanceForConvexHull + x,
                    distanceForConvexHull + y + height
                )
            )
            inList.addLast(
                Point(
                    distanceForConvexHull + x +
                        width,
                    distanceForConvexHull + y + height
                )
            )
            inList.addLast(
                Point(
                    distanceForConvexHull + x +
                        width,
                    -distanceForConvexHull + y
                )
            )
        }
        val childrenViews = childrenViews
        val children_it: ListIterator<NodeView> = childrenViews.listIterator()
        while (children_it.hasNext()) {
            val child = children_it.next()
            child.getCoordinates(
                inList, distanceForConvexHull, true,
                transX + child.x, transY + child.y
            )
        }
    }
    /**
     */
    /**
     */
    var text: String?
        get() = mainView!!.text
        set(string) {
            mainView!!.text = string
        }
    protected val mainViewWidthWithFoldingMark: Int
        get() = mainView!!.mainViewWidthWithFoldingMark

    /** get height including folding symbol  */
    protected val mainViewHeightWithFoldingMark: Int
        get() = mainView!!.mainViewHeightWithFoldingMark

    /** get x coordinate including folding symbol  */
    val deltaX: Int
        get() = mainView!!.deltaX

    /** get y coordinate including folding symbol  */
    val deltaY: Int
        get() = mainView!!.deltaY
    //
    // get/set methods
    //
    /**
     * Calculates the tree height increment because of the clouds.
     */
    val additionalCloudHeigth: Int
        get() {
            if (!isContentVisible) {
                return 0
            }
            val cloud = model.cloud
            return if (cloud != null) {
                getAdditionalHeigth(cloud, this)
            } else {
                0
            }
        }
    val isSelected: Boolean
        get() = map.isSelected(this)

    /** Is the node left of root?  */
    val isLeft: Boolean
        get() = model.isLeft
    val viewFeedback: ViewFeedback
        get() = map.viewFeedback
    val isParentHidden: Boolean
        get() {
            val parent = parent as? NodeView ?: return false
            return !parent.isContentVisible
        }
    val parentView: NodeView?
        get() {
            val parent = parent
            return if (parent is NodeView) parent else null
        }
    val visibleParentView: NodeView?
        get() {
            val parent = parent as? NodeView ?: return null
            val parentView = parent
            return if (parentView.isContentVisible) {
                parentView
            } else parentView.visibleParentView
        } // child.getViewer() );

    /**
     * This method returns the NodeViews that are children of this node.
     */
    val childrenViews: LinkedList<NodeView>
        get() {
            val childrenViews = LinkedList<NodeView>()
            val components = components
            for (i in components.indices) {
                if (components[i] !is NodeView) {
                    continue
                }
                val view = components[i] as NodeView
                childrenViews.add(view) // child.getViewer() );
            }
            return childrenViews
        }
    val siblingViews: LinkedList<NodeView>
        get() = parentView!!.childrenViews

    /**
     * Returns the point the edge should start given the point of the child node
     * that should be connected.
     *
     * @param targetView
     * TODO
     */
    fun getMainViewOutPoint(targetView: NodeView?, destinationPoint: Point?): Point? {
        val layoutManager = layout as NodeViewLayout
        return layoutManager.getMainViewOutPoint(
            this, targetView,
            destinationPoint
        )
    }

    /**
     * Returns the Point where the InEdge should arrive the Node.
     */
    val mainViewInPoint: Point?
        get() {
            val layoutManager = layout as NodeViewLayout
            return layoutManager.getMainViewInPoint(this)
        }

    /**
     * Returns the Point where the Links should arrive the Node.
     */
    fun getLinkPoint(declination: Point?): Point {
        var x: Int
        val y: Int
        val linkPoint: Point
        if (declination != null) {
            x = map.getZoomed(declination.x)
            y = map.getZoomed(declination.y)
        } else {
            x = 1
            y = 0
        }
        if (isLeft) {
            x = -x
        }
        if (y != 0) {
            val ctgRect = Math.abs(content!!.width.toDouble() / content!!.height)
            val ctgLine = Math.abs(x.toDouble() / y)
            val absLinkX: Int
            val absLinkY: Int
            if (ctgRect > ctgLine) {
                absLinkX = Math.abs(x * content!!.height / (2 * y))
                absLinkY = content!!.height / 2
            } else {
                absLinkX = content!!.width / 2
                absLinkY = Math.abs(y * content!!.width / (2 * x))
            }
            linkPoint = Point(
                content!!.width / 2 +
                    if (x > 0) absLinkX else -absLinkX,
                content!!.height /
                    2 + if (y > 0) absLinkY else -absLinkY
            )
        } else {
            linkPoint = Point(
                if (x > 0) content!!.width else 0,
                content!!.height / 2
            )
        }
        linkPoint.translate(content!!.x, content!!.y)
        convertPointToMap(linkPoint)
        return linkPoint
    }

    protected fun convertPointToMap(p: Point?): Point {
        return Tools.convertPointToAncestor(this, p, map)
    }

    /**
     * Returns the relative position of the Edge. This is used by bold edge to
     * know how to shift the line.
     */
    val alignment: Int
        get() = mainView!!.alignment
    //
    // Navigation
    // // has the same position after one page?
    // last on the page
// at the end
    // if (sibling.getParentView() != this.getParentView()) {
    // return sibling; // sibling on another page (has different parent)
    // }
// I'm root// from root we cannot jump
    /**
     * The algorithm should be here the following (see Eclipse editor):
     * Selected is the n-th node from above.
     * Look for the last node visible on the screen and make this node the first one.
     * Now select the n-th node from above.
     *
     * Easier idea to implement:
     * Store node y position as y0.
     * Search for a node with the same parent with y position y0+height
     * Scroll the window by height.
     */
    val nextPage: NodeView?
        get() {
            // from root we cannot jump
            if (model.isRoot) {
                return this // I'm root
            }
            val y0 = inPointInMap.y + map.viewportSize!!.height
            var sibling = nextVisibleSibling
            if (sibling === this) {
                return this // at the end
            }
            // if (sibling.getParentView() != this.getParentView()) {
            // return sibling; // sibling on another page (has different parent)
            // }
            var nextSibling = sibling!!.nextVisibleSibling
            while (nextSibling !== sibling &&
                sibling!!.parentView === nextSibling!!.parentView
            ) {
                // has the same position after one page?
                if (nextSibling!!.inPointInMap.y >= y0) {
                    break
                }
                sibling = nextSibling
                nextSibling = nextSibling.nextVisibleSibling
            }
            return sibling // last on the page
        }

    /**
     * @return the position of the in-point of this node in view coordinates
     */
    protected val inPointInMap: Point
        get() = convertPointToMap(mainViewInPoint) // has the same position after one page?

    // last on the page
// at the end
    // if (sibling.getParentView() != this.getParentView()) {
    // return sibling; // sibling on another page (has different parent)
    // }
    // I'm root
    val previousPage: NodeView?
        get() {
            if (model.isRoot) {
                return this // I'm root
            }
            val y0 = inPointInMap.y - map.viewportSize!!.height
            var sibling = previousVisibleSibling
            if (sibling === this) {
                return this // at the end
            }
            // if (sibling.getParentView() != this.getParentView()) {
            // return sibling; // sibling on another page (has different parent)
            // }
            var previousSibling = sibling!!.previousVisibleSibling
            while (previousSibling !== sibling &&
                sibling!!.parentView === previousSibling!!.parentView
            ) {
                // has the same position after one page?
                if (previousSibling!!.inPointInMap.y <= y0) {
                    break
                }
                sibling = previousSibling
                previousSibling = previousSibling.previousVisibleSibling
            }
            return sibling // last on the page
        } // didn't find (we are at the end)// can we drill down?// found sibling

    // we have the nextSibling, search in childs
    // untill: leaf, closed node, max level
    // get next sibling even in higher levels
    val nextVisibleSibling: NodeView?
        get() {
            var sibling: NodeView?
            var lastSibling: NodeView? = this
            // get next sibling even in higher levels
            sibling = this
            while (!sibling!!.model.isRoot) {
                lastSibling = sibling
                sibling = sibling.nextSiblingSingle
                if (sibling !== lastSibling) {
                    break // found sibling
                }
                sibling = sibling
                    .parentView
            }

            // we have the nextSibling, search in childs
            // untill: leaf, closed node, max level
            while (sibling!!.model.nodeLevel < map
                .siblingMaxLevel
            ) {
                // can we drill down?
                val first = sibling.getFirst(if (sibling.isRoot) lastSibling else null, isLeft, !isLeft) ?: break
                sibling = first
            }
            return if (sibling.isRoot) {
                this // didn't find (we are at the end)
            } else sibling
        }

    /**
     * @param startAfter
     * TODO
     */
    fun getFirst(startAfter: Component?, leftOnly: Boolean, rightOnly: Boolean): NodeView? {
        var after = startAfter
        val components = components
        for (i in components.indices) {
            if (after != null) {
                if (components[i] === after) {
                    after = null
                }
                continue
            }
            if (components[i] !is NodeView) {
                continue
            }
            val view = components[i] as NodeView
            if (leftOnly && !view.isLeft || rightOnly && view.isLeft) {
                continue
            }
            if (view.isContentVisible) {
                return view
            }
            val child = view.getFirst(null, leftOnly, rightOnly)
            if (child != null) {
                return child
            }
        }
        return null
    }

    /**
     */
    val isContentVisible: Boolean
        get() = model.isVisible

    private fun getLast(
        startBefore: Component?,
        leftOnly: Boolean,
        rightOnly: Boolean
    ): NodeView? {
        var before = startBefore
        val components = components
        for (i in components.indices.reversed()) {
            if (before != null) {
                if (components[i] === before) {
                    before = null
                }
                continue
            }
            if (components[i] !is NodeView) {
                continue
            }
            val view = components[i] as NodeView
            if (leftOnly && !view.isLeft || rightOnly && view.isLeft) {
                continue
            }
            if (view.isContentVisible) {
                return view
            }
            val child = view.getLast(null, leftOnly, rightOnly)
            if (child != null) {
                return child
            }
        }
        return null
    }

    fun getLeft(): LinkedList<NodeView> {
        val all = childrenViews
        val left = LinkedList<NodeView>()
        for (node in all) {
            if (node.isLeft) left.add(node)
        }
        return left
    }

    fun getRight(): LinkedList<NodeView> {
        val all = childrenViews
        val right = LinkedList<NodeView>()
        for (node in all) {
            if (!node.isLeft) right.add(node)
        }
        return right
    } // didn't find (we are at the end)// found sibling

    // we have the PreviousSibling, search in childs
    // untill: leaf, closed node, max level
    // get Previous sibling even in higher levels
    val previousVisibleSibling: NodeView?
        get() {
            var sibling: NodeView?
            var previousSibling: NodeView? = this

            // get Previous sibling even in higher levels
            sibling = this
            while (!sibling!!.model.isRoot) {
                previousSibling = sibling
                sibling = sibling.previousSiblingSingle
                if (sibling !== previousSibling) {
                    break // found sibling
                }
                sibling = sibling
                    .parentView
            }
            // we have the PreviousSibling, search in childs
            // untill: leaf, closed node, max level
            while (sibling!!.model.nodeLevel < map
                .siblingMaxLevel
            ) {
                val last = sibling.getLast(if (sibling.isRoot) previousSibling else null, isLeft, !isLeft) ?: break
                sibling = last
            }
            return if (sibling.isRoot) {
                this // didn't find (we are at the end)
            } else sibling
        }
    protected val nextSiblingSingle: NodeView
        get() {
            var v: LinkedList<NodeView>?
            v = if (parentView!!.model.isRoot) {
                if (isLeft) {
                    parentView!!.getLeft()
                } else {
                    parentView!!.getRight()
                }
            } else {
                parentView!!.childrenViews
            }
            val index = v.indexOf(this)
            for (i in index + 1 until v.size) {
                val nextView = v[i]
                if (nextView.isContentVisible) {
                    return nextView
                } else {
                    val first = nextView.getFirst(null, false, false)
                    if (first != null) {
                        return first
                    }
                }
            }
            return this
        }
    protected val previousSiblingSingle: NodeView
        get() {
            var v: LinkedList<NodeView>?
            v = if (parentView!!.model.isRoot) {
                if (isLeft) {
                    parentView!!.getLeft()
                } else {
                    parentView!!.getRight()
                }
            } else {
                parentView!!.childrenViews
            }
            val index = v.indexOf(this)
            for (i in index - 1 downTo 0) {
                val nextView = v[i]
                if (nextView.isContentVisible) {
                    return nextView
                } else {
                    val last = nextView.getLast(null, false, false)
                    if (last != null) {
                        return last
                    }
                }
            }
            return this
        }

    //
    // Update from Model
    //
    fun insert() {
        val it = model.childrenFolded()
        while (it.hasNext()) {
            insert(it.next())
        }
    }

    /**
     * Create views for the newNode and all his descendants, set their isLeft
     * attribute according to this view.
     */
    fun insert(newNode: MindMapNode?): NodeView? {
        val newView = NodeViewFactory.instance?.newNodeView(
            newNode,
            map, this
        )
        newView?.insert()
        return newView
    }

    /**
     * This is a bit problematic, because getChildrenViews() only works if model
     * is not yet removed. (So do not _really_ delete the model before the view
     * removed (it needs to stay in memory)
     */
    fun remove() {
        val e: ListIterator<NodeView> = childrenViews.listIterator()
        while (e.hasNext()) {
            e.next().remove()
        }
        if (isSelected) {
            map.deselect(this)
        }
        viewFeedback.onViewRemovedHook(this)
        removeFromMap()
        map.removeViewer(model, this) // Let the model know he is invisible
    }

    fun update() {
        updateStyle()
        if (!isContentVisible) {
            // not visible at all
            removeFoldingListener()
            mainView!!.isVisible = false
            return
        }
        mainView!!.isVisible = true
        updateTextColor()
        updateFont()
        updateIcons()
        // visible. has it still visible children?
        if (model.hasVisibleChilds()) {
            addFoldingListener()
        } else {
            removeFoldingListener()
        }
        updateText()
        updateToolTip()
        revalidate() // Because of zoom?
    }

    fun repaintSelected() {
        updateTextColor()
        repaint()
    }

    private fun updateText() {
        var nodeText = model.toString()
        val isHtml = nodeText.startsWith("<html>")
        // 6) Set the text
        // Right now, this implementation is quite logical, although it allows
        // for nonconvex feature of nodes starting with <html>.

        // For plain text, tell if node is long and its width has to be
        // restricted
        // boolean isMultiline = nodeText.indexOf("\n") >= 0;
        var widthMustBeRestricted = false
        if (!isHtml) {
            val lines = nodeText.split("\n").toTypedArray()
            for (line in lines.indices) {
                // Compute the width the node would spontaneously take,
                // by preliminarily setting the text.
                text = lines[line]
                widthMustBeRestricted = mainView!!.preferredSize.width > map
                    .getZoomed(map.maxNodeWidth)
                +mainView!!.iconWidth
                if (widthMustBeRestricted) {
                    break
                }
            }
            isLong = widthMustBeRestricted || lines.size > 1
        }
        if (isHtml) {
            // Make it possible to use relative img references in HTML using tag
            // <base>.
            if (nodeText.indexOf("<img") >= 0 && nodeText.indexOf("<base ") < 0) {
                try {
                    nodeText = (
                        "<html><base href=\"" + map.model!!.url +
                            "\">" + nodeText.substring(6)
                        )
                } catch (e: MalformedURLException) {
                }
            }
            // If user does not want us to set the width automatically, he'll
            // use <body width="">,
            // <body width="800">, or avoid the <body> tag altogether.

            // Set user HTML head
            val htmlLongNodeHead = viewFeedback
                .getProperty("html_long_node_head")
            if (htmlLongNodeHead != null && htmlLongNodeHead != "") {
                nodeText = if (nodeText.matches(Regex.fromLiteral("(?ims).*<head>.*"))) {
                    nodeText.replaceFirst(
                        "(?ims).*<head>.*".toRegex(),
                        "<head>$htmlLongNodeHead"
                    )
                } else {
                    nodeText.replaceFirst(
                        "(?ims)<html>".toRegex(),
                        "<html><head>$htmlLongNodeHead</head>"
                    )
                }
            }

            // Find out if the width has to be restricted.
            if (nodeText.length < 30000) {
                // Empirically determined limit, above which we restrict the
                // width without actually checking it.
                // The purpose of that is to speed up rendering of very long
                // nodes.
                text = nodeText
                widthMustBeRestricted = mainView!!.preferredSize.width > map
                    .getZoomed(map.maxNodeWidth)
                +mainView!!.iconWidth
            } else {
                widthMustBeRestricted = true
            }
            if (widthMustBeRestricted) {
                nodeText = nodeText.replaceFirst(
                    "(?i)<body>".toRegex(),
                    "<body width=\"" +
                        map.maxNodeWidth + "\">"
                )
            }
            text = nodeText
        } else if (nodeText.startsWith("<table>")) {
            val lines = nodeText.split("\n").toTypedArray()
            lines[0] = lines[0].substring(7) // remove <table> tag
            val startingLine = if (lines[0].matches(Regex.fromLiteral("\\s*"))) 1 else 0
            // ^ If the remaining first line is empty, do not draw it
            var text: String? = "<html><table border=1 style=\"border-color: white\">"
            // String[] lines = nodeText.split("\n");
            for (line in startingLine until lines.size) {
                text += (
                    "<tr><td style=\"border-color: white;\">" +
                        HtmlTools.toXMLEscapedText(lines[line]).replace(
                            "\t".toRegex(), "<td style=\"border-color: white\">"
                        )
                    )
            }
        } else {
            text = nodeText
        }
    }

    private fun updateFont() {
        var font = model.font
        font = font ?: viewFeedback.defaultFont
        if (font != null) {
            mainView!!.font = font
        } else {
            // We can survive this trouble.
            System.err.println("NodeView.update(): default font is null.")
        }
    }

    private fun updateIcons() {
        updateIconPosition()
        val iconImages = MultipleImage(1.0)
        var iconPresent = false
        /* fc, 06.10.2003: images? */
        val stateIcons = model.stateIcons
        for (key in stateIcons.keys) {
            iconPresent = true
            iconImages.addImage(stateIcons[key]!!)
        }
        if (SHOW_ATTRIBUTE_ICON && model.attributeTableLength > 0) {
            if (sAttributeIcon == null) {
                sAttributeIcon = ImageFactory.getInstance().createUnscaledIcon(
                    Resources.getInstance().getResource(
                        "images/showAttributes.gif"
                    )
                )
            }
            iconImages.addImage(sAttributeIcon!!)
            iconPresent = true
        }
        val icons = model.icons
        for (myIcon in icons) {
            iconPresent = true
            // System.out.println("print the icon " + myicon.toString());
            iconImages.addImage(myIcon.unscaledIcon)
        }
        val link = (model as NodeAdapter).link
        if (link != null) {
            iconPresent = true
            var iconPath = "images/Link.png"
            if (link.startsWith("#")) {
                iconPath = "images/LinkLocal.png"
            } else if (link.startsWith("mailto:")) {
                iconPath = "images/Mail.png"
            } else if (Tools.executableByExtension(link)) {
                iconPath = "images/Executable.png"
            }
            val icon = ImageFactory.getInstance().createUnscaledIcon(Resources.getInstance().getResource(iconPath))
            iconImages.addImage(icon)
        }
        // /* Folded icon by Matthias Schade (mascha2), fc, 20.12.2003*/
        // if (((NodeAdapter)getModel()).isFolded()) {
        // iconPresent = true;
        // ImageIcon icon = new
        // ImageIcon(((NodeAdapter)getModel()).getFrame().getResource("images/Folded.png"));
        // iconImages.addImage(icon);
        // }
        // DanielPolansky: set icon only if icon is present, because
        // we don't want to insert any additional white space.
        setIcon(if (iconPresent) iconImages else null)
    }

    private fun updateIconPosition() {
        getMainView()!!.horizontalTextPosition = if (isLeft) SwingConstants.LEADING else SwingConstants.TRAILING
    }

    private fun updateTextColor() {
        var color: Color?
        color = model.color
        if (color == null) {
            color = MapView.standardNodeTextColor
        }
        mainView!!.foreground = color
    }

    fun useSelectionColors(): Boolean {
        return (
            isSelected && !MapView.standardDrawRectangleForSelection &&
                !map.isCurrentlyPrinting
            )
    }

    fun updateStyle() {
        if (mainView != null &&
            (
                mainView!!.style == model.style || model
                    .isRoot
                )
        ) {
            return
        }
        val newMainView = NodeViewFactory.instance?.newMainView(
            model
        )
        setMainView(newMainView)
    }

    /**
     * Updates the tool tip of the node.
     */
    fun updateToolTip() {
        val tooltips: Map<String, String> = TreeMap(
            model.toolTip
        )
        if (tooltips.size == 0) {
            mainView!!.toolTipText = null
        } else {
            // html table
            val text = StringBuffer(
                "<html><table width=\"" +
                    maxToolTipWidth + "\">"
            )
            val i = tooltips.keys.iterator()
            while (i.hasNext()) {
                var value = tooltips[i.next()]
                // no html end inside the value:
                value = value!!.replace("</html>".toRegex(), "")
                text.append("<tr><td>")
                text.append(value)
                text.append("</td></tr>")
            }
            text.append("</table></html>")
            mainView!!.toolTipText = text.toString()
        }
    }

    /**
     */
    fun setIcon(image: MultipleImage?) {
        mainView!!.icon = image
    }

    fun updateAll() {
        update()
        invalidate()
        val e: ListIterator<NodeView> = childrenViews.listIterator()
        while (e.hasNext()) {
            val child = e.next()
            child.updateAll()
        }
    }

    val style: String?
        get() = mainView!!.style

    /**
     * @return Returns the sHIFT.
     */
    val shift: Int
        get() = map.getZoomed(model.calcShiftY())

    /**
     * @return Returns the VGAP.
     */
    val vGap: Int
        get() = map.getZoomed(model.vGap)
    val hGap: Int
        get() = map.getZoomed(model.hGap)

    fun getMainView(): MainView? {
        return mainView
    }

    val textFont: Font
        get() = getMainView()!!.font
    val textColor: Color?
        get() {
            var color = model.color
            if (color == null) {
                color = MapView.standardNodeTextColor
            }
            return color
        }

    fun getPreferredVisibleChild(left: Boolean): NodeView? { // mind preferred
        // child
        // :-) (PN)
        if (preferredChild != null && left == preferredChild!!.isLeft &&
            preferredChild!!.parent === this
        ) {
            if (preferredChild!!.isContentVisible) {
                return preferredChild
            } else {
                val newSelected = preferredChild!!
                    .getPreferredVisibleChild(left)
                if (newSelected != null) {
                    return newSelected
                }
            }
        }
        if (!model.isLeaf) {
            var yGap = Int.MAX_VALUE
            val baseComponent: NodeView?
            baseComponent = if (isContentVisible) {
                this
            } else {
                visibleParentView
            }
            val ownY = (
                baseComponent!!.getMainView()!!.y +
                    baseComponent.getMainView()!!.height / 2
                )
            var newSelected: NodeView? = null
            for (i in 0 until componentCount) {
                val c = getComponent(i) as? NodeView ?: continue
                var childView: NodeView? = c
                if (childView!!.isLeft != left) {
                    continue
                }
                if (!childView.isContentVisible) {
                    childView = childView.getPreferredVisibleChild(left)
                    if (childView == null) {
                        continue
                    }
                }
                val childPoint = Point(
                    0, childView.getMainView()?.getHeight()?.div(2) ?: 0
                )
                Tools.convertPointToAncestor(
                    childView.getMainView(),
                    childPoint, baseComponent
                )
                val gapToChild = Math.abs(childPoint.y - ownY)
                if (gapToChild < yGap) {
                    newSelected = childView
                    preferredChild = c
                    yGap = gapToChild
                } else {
                    break
                }
            }
            return newSelected
        }
        return null
    }

    fun setPreferredChild(view: NodeView?) {
        preferredChild = view
        val parent = parent
        if (view == null) {
            return
        } else if (parent is NodeView) {
            // set also preffered child of parents...
            parent.setPreferredChild(this)
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event
	 * .TreeModelEvent)
	 */
    override fun treeNodesChanged(e: TreeModelEvent) {
        update()
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event
	 * .TreeModelEvent)
	 */
    override fun treeNodesInserted(e: TreeModelEvent) {
        addFoldingListener()
        if (model.isFolded) {
            return
        }
        val childIndices = e.childIndices
        for (i in childIndices.indices) {
            val index = childIndices[i]
            insert(model.getChildAt(index) as MindMapNode)
        }
        revalidate()
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event
	 * .TreeModelEvent)
	 */
    override fun treeNodesRemoved(e: TreeModelEvent) {
        if (!model.hasVisibleChilds()) {
            removeFoldingListener()
        }
        map.resetShiftSelectionOrigin()
        if (model.isFolded) {
            return
        }
        val childIndices = e.childIndices
        val preferredChildIsLeft = (
            preferredChild != null &&
                preferredChild!!.isLeft
            )
        for (i in childIndices.indices.reversed()) {
            val index = childIndices[i]
            val node = getComponent(index) as NodeView
            if (node === preferredChild) { // mind preferred child :-) (PN)
                preferredChild = null
                for (j in index + 1 until componentCount) {
                    val c = getComponent(j) as? NodeView ?: break
                    val candidate = c
                    if (candidate.isVisible &&
                        node.isLeft == candidate.isLeft
                    ) {
                        preferredChild = candidate
                        break
                    }
                }
                if (preferredChild == null) {
                    for (j in index - 1 downTo 0) {
                        val c = getComponent(j) as? NodeView ?: break
                        val candidate = c
                        if (candidate.isVisible &&
                            node.isLeft == candidate.isLeft
                        ) {
                            preferredChild = candidate
                            break
                        }
                    }
                }
            }
            node.remove()
        }
        val preferred = getPreferredVisibleChild(preferredChildIsLeft)
        if (preferred != null) { // after delete focus on a brother (PN)
            map.selectAsTheOnlyOneSelected(preferred)
        } else {
            map.selectAsTheOnlyOneSelected(this)
        }
        revalidate()
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.
	 * event.TreeModelEvent)
	 */
    override fun treeStructureChanged(e: TreeModelEvent) {
        map.resetShiftSelectionOrigin()
        val i: ListIterator<NodeView> = childrenViews.listIterator()
        while (i.hasNext()) {
            i.next().remove()
        }
        insert()
        if (map.selected == null) {
            map.selectAsTheOnlyOneSelected(this)
        }
        map.revalidateSelecteds()
        revalidate()
    }

    val zoomedFoldingSymbolHalfWidth: Int
        get() {
            val preferredFoldingSymbolHalfWidth = (
                foldingSymbolWidth * map
                    .getZoom() / 2
                ).toInt()
            return Math.min(preferredFoldingSymbolHalfWidth, height / 2)
        }

    /**
     * @return the left/right point of the folding circle. To receive its
     * center, the amount has to be moved to left/right (depending on its side)
     * by the folding circle width.
     */
    val foldingMarkPosition: Point?
        get() = getMainViewOutPoint(this, Point())
    val content: JComponent?
        get() = if (contentPane == null) mainView else contentPane

    fun getContentPane(): Container? {
        if (contentPane == null) {
            val pane = NodeViewFactory.instance?.newContentPane()
            remove(mainView)
            pane?.add(mainView)
            add(pane)

            contentPane = pane
        }
        return contentPane
    }

    override fun setBounds(x: Int, y: Int, width: Int, height: Int) {
        super.setBounds(x, y, width, height)
        if (motionListenerView != null) {
            motionListenerView!!.invalidate()
        }
        if (mFoldingListener != null) {
            mFoldingListener!!.invalidate()
        }
    }

    override fun setVisible(isVisible: Boolean) {
        super.setVisible(isVisible)
        if (motionListenerView != null) {
            motionListenerView!!.isVisible = isVisible
        }
        if (mFoldingListener != null) {
            mFoldingListener!!.isVisible = isVisible
        }
    }

    private fun paintCloudsAndEdges(g: Graphics2D) {
        val renderingHint = map.setEdgesRenderingHint(g)
        for (i in 0 until componentCount) {
            val component = getComponent(i) as? NodeView ?: continue
            val nodeView = component
            if (nodeView.isContentVisible) {
                val p = Point()
                Tools.convertPointToAncestor(nodeView, p, this)
                g.translate(p.x, p.y)
                nodeView.paintCloud(g)
                g.translate(-p.x, -p.y)
                val edge = NodeViewFactory.instance?.getEdge(nodeView)
                edge?.paint(nodeView, g)
            } else {
                nodeView.paintCloudsAndEdges(g)
            }
        }
        Tools.restoreAntialiasing(g, renderingHint)
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
    override fun paint(g: Graphics) {
        val isRoot = isRoot
        if (isRoot) {
            paintCloud(g)
        }
        if (isContentVisible) {
            val g2d = g as Graphics2D
            paintCloudsAndEdges(g2d)
            super.paint(g)
            // return to std stroke
            g2d.stroke = BubbleMainView.DEF_STROKE
            // 			if (!isRoot) {
// 				paintFoldingMark(g2d);
// 			}
        } else {
            super.paint(g)
        }
        // g.setColor(Color.BLACK);
        // g.drawRect(0, 0, getWidth()-1, getHeight()-1);
    }

    private fun paintCloud(g: Graphics) {
        if (isContentVisible && model.cloud != null) {
            val cloud = CloudView(model.cloud, this)
            cloud.paint(g)
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see java.awt.Component#toString()
	 */
    override fun toString(): String {
        return model.toString() + ", " + super.toString()
    }

    val innerBounds: Rectangle
        get() {
            val space = map.getZoomed(SPACE_AROUND)
            return Rectangle(
                space, space, width - 2 * space,
                height -
                    2 * space
            )
        }

    override fun contains(x: Int, y: Int): Boolean {
        val space = map.getZoomed(SPACE_AROUND) - 2 * zoomedFoldingSymbolHalfWidth
        return (
            x >= space && x < width - space && y >= space &&
                y < height - space
            )
    }

    val textBackground: Color
        get() {
            val modelBackgroundColor = model.backgroundColor
            return modelBackgroundColor ?: backgroundColor
        }
    private val backgroundColor: Color
        get() {
            val cloud = model.cloud
            if (cloud != null) {
                return cloud.color
            }
            return if (isRoot) {
                map.background
            } else parentView!!.backgroundColor
        }

    companion object {
        private var FOLDING_SYMBOL_WIDTH = -1
        @JvmField
        val dragColor = Color.lightGray // the Color of
        const val DRAGGED_OVER_NO = 0
        const val DRAGGED_OVER_SON = 1
        const val DRAGGED_OVER_SIBLING = 2

        /** For RootNodeView.  */
        const val DRAGGED_OVER_SON_LEFT = 3
        const val ALIGN_BOTTOM = -1
        const val ALIGN_CENTER = 0

        /**
         * Returns the relative position of the Edge
         */val alignment: Int
            get() {
                return NodeView.ALIGN_CENTER
            }
        const val ALIGN_TOP = 1
        private var logger: Logger? = null
        private var sListener: FreemindPropertyListener? = null
        private val SHOW_ATTRIBUTE_ICON = Resources.getInstance()
            .getBoolProperty("el__show_icon_for_attributes")
        private var sAttributeIcon: ImageIcon? = null
        const val SPACE_AROUND = 50
        /*
	 * http://groups.google.de/groups?hl=de&lr=&ie=UTF-8&threadm=9i5bbo%24h1kmi%243
	 * %
	 * 40ID-77081.news.dfncis.de&rnum=1&prev=/groups%3Fq%3Djava%2520komplement%25
	 * C3
	 * %25A4rfarbe%2520helligkeit%26hl%3Dde%26lr%3D%26ie%3DUTF-8%26sa%3DN%26as_qdr
	 * %3Dall%26tab%3Dwg
	 */
        /**
         * Determines to a given color a color, that is the best contrary color. It
         * is different from [.getAntiColor2].
         *
         * @since PPS 1.1.1
         */
        protected fun getAntiColor1(c: Color): Color {
            val hsb = Color.RGBtoHSB(
                c.red, c.green, c.blue,
                null
            )
            hsb[0] += 0.40.toFloat()
            if (hsb[0] > 1) hsb[0]--
            hsb[1] = 1f
            hsb[2] = 0.7f
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2])
        }

        /**
         * Determines to a given color a color, that is the best contrary color. It
         * is different from [.getAntiColor1].
         *
         * @since PPS 1.1.1
         */
        protected fun getAntiColor2(c: Color): Color {
            val hsb = Color.RGBtoHSB(
                c.red, c.green, c.blue,
                null
            )
            hsb[0] -= 0.40.toFloat()
            if (hsb[0] < 0) hsb[0]++
            hsb[1] = 1f
            hsb[2] = 0.8.toFloat()
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2])
        }

        val foldingSymbolWidth: Int
            get() {
                if (FOLDING_SYMBOL_WIDTH == -1) {
                    FOLDING_SYMBOL_WIDTH = Resources.getInstance().getIntProperty(
                        "foldingsymbolwidth", 8
                    )
                }
                return FOLDING_SYMBOL_WIDTH
            }

        /**
         * @return returns the color that should used to select the node.
         */
        var selectedColor: Color? = null
            get() {
                // Color backgroundColor = getModel().getBackgroundColor();
                // // if(backgroundColor != null) {
                // // Color backBrighter = backgroundColor.brighter();
                // // // white?
                // // if(backBrighter.getRGB() == Color.WHITE.getRGB()) {
                // // return standardSelectColor;
                // // }
                // // // == standard??
                // // if (backBrighter.equals (standardSelectColor) ) {
                // // return backgroundColor.darker();
                // // }
                // // return backBrighter;
                // // }
                // // == standard??
                // if (backgroundColor != null /*&&
                // backgroundColor.equals(standardSelectColor)*/ ) {
                // // bad hack:
                // return getAntiColor1(backgroundColor);
                // // return new Color(0xFFFFFF - backgroundColor.getRGB());
                // }
                return MapView.standardSelectColor
            }
    }
}
