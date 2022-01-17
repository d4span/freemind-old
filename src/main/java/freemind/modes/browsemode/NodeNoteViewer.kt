/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Christian Foltin and others
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

import freemind.controller.Controller.SplitComponentType
import freemind.modes.ControllerAdapter
import freemind.modes.MindMapNode
import freemind.modes.ModeController.NodeSelectionListener
import freemind.modes.common.plugins.NodeNoteBase
import freemind.view.ImageFactory.Companion.instance
import freemind.view.mindmapview.NodeView
import java.awt.Color
import java.awt.Dimension
import javax.swing.ImageIcon
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JScrollPane

/**
 * @author foltin
 */
class NodeNoteViewer(private val mBrowseController: ControllerAdapter) : NodeNoteBase(), NodeSelectionListener {
    private var noteScrollPane: JComponent? = null
    private var noteViewer: JLabel? = null
    protected fun getNoteViewerComponent(): JComponent? {
        if (noteViewer == null) {
            noteViewer = JLabel()
            noteViewer!!.background = Color.WHITE
            noteViewer!!.verticalAlignment = JLabel.TOP
            noteViewer!!.isOpaque = true
            noteScrollPane = JScrollPane(noteViewer)
            noteScrollPane?.setPreferredSize(Dimension(1, 200))
        }
        return noteScrollPane
    }

    override fun onLostFocusNode(pNode: NodeView) {
        mBrowseController.controller.removeSplitPane(SplitComponentType.NOTE_PANEL)
    }

    override fun onFocusNode(pNode: NodeView) {
        val noteText = pNode.model.noteText
        if (noteText != null && noteText != "") {
            // logger.info("Panel added");
            mBrowseController.controller.insertComponentIntoSplitPane(
                getNoteViewerComponent(), SplitComponentType.NOTE_PANEL
            )
            noteViewer!!.text = noteText
        }
    }

    override fun onSaveNode(pNode: MindMapNode) {}
    override fun onUpdateNodeHook(pNode: MindMapNode) {
        setStateIcon(pNode, true)
    }

    /** Copied from NodeNoteRegistration.  */
    protected fun setStateIcon(node: MindMapNode, enabled: Boolean) {
        // icon
        if (noteIcon == null) {
            noteIcon = instance!!.createUnscaledIcon(
                mBrowseController.getResource("images/knotes.png")
            )
        }
        node.setStateIcon(NODE_NOTE_ICON, if (enabled) noteIcon else null)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.ModeController.NodeSelectionListener#onSelectionChange(freemind.modes.MindMapNode, boolean)
	 */
    override fun onSelectionChange(pNode: NodeView, pIsSelected: Boolean) {}

    companion object {
        private var noteIcon: ImageIcon? = null
    }
}
