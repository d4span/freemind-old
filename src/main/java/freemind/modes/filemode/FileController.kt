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
package freemind.modes.filemode

import freemind.controller.MenuBar
import freemind.controller.StructuredMenuHolder
import freemind.extensions.HookFactory
import freemind.main.XMLParseException
import freemind.modes.MapAdapter
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.modes.Mode
import freemind.modes.ModeController
import freemind.modes.common.actions.NewMapAction
import freemind.modes.viewmodes.ViewControllerAdapter
import freemind.view.mindmapview.MainView
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.io.File
import java.io.IOException
import java.net.URISyntaxException
import java.net.URL
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JOptionPane
import javax.swing.JPopupMenu
import javax.swing.JToolBar

class FileController(mode: Mode?) : ViewControllerAdapter(mode) {
    @JvmField
    var newMap: Action = NewMapAction(this)
    @JvmField
    var center: Action = CenterAction()
    var openPath: Action = OpenPathAction()
    private val popupmenu: JPopupMenu = FilePopupMenu(this)
    override fun getModeToolBar(): JToolBar? {
        return (mode as FileMode).toolbar
    }

    override fun newModel(modeController: ModeController): MapAdapter {
        val model = FileMapModel(modeController)
        modeController.setModel(model)
        return model
    }

    override fun newNode(userObject: Any, map: MindMap): MindMapNode {
        return FileNodeModel(userObject as File, map)
    }

    override fun getPopupMenu(): JPopupMenu {
        return popupmenu
    }

    // -----------------------------------------------------------------------------------
    // Private
    //
    private inner class CenterAction internal constructor() : AbstractAction(controller.getResourceString("center")) {
        override fun actionPerformed(e: ActionEvent) {
            if (selected != null) {
                val map: MindMap = FileMapModel(
                    (selected as FileNodeModel).file, /*
						 * DON'T COPY THIS, AS THIS IS A BAD HACK! The
						 * Constructor needs a new instance of a modecontroller.
						 */
                    this@FileController
                )
                newMap(map, this@FileController)
            }
        }
    }

    private inner class OpenPathAction internal constructor() : AbstractAction(controller.getResourceString("open")) {
        override fun actionPerformed(e: ActionEvent) {
            val inputValue = JOptionPane.showInputDialog(
                controller
                    .view!!.selected,
                getText("open"), ""
            )
            if (inputValue != null) {
                val newCenter = File(inputValue)
                if (newCenter.exists()) { // and is a folder
                    val map: MindMap = FileMapModel(
                        newCenter, /*
					 * DON'T COPY THIS, AS THIS IS A BAD HACK! The Constructor
					 * needs a new instance of a modecontroller.
					 */
                        this@FileController
                    )
                    newMap(map, this@FileController)
                }
            }
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.modes.ModeController#updateMenus(freemind.controller.
	 * StructuredMenuHolder)
	 */
    override fun updateMenus(holder: StructuredMenuHolder) {
        add(holder, MenuBar.EDIT_MENU + "/find", find, "keystroke_find")
        add(
            holder, MenuBar.EDIT_MENU + "/findNext", findNext,
            "keystroke_find_next"
        )
        add(holder, MenuBar.EDIT_MENU + "/openPath", openPath, null)
    }

    override fun getHookFactory(): HookFactory {
        throw IllegalArgumentException("Not implemented yet.")
    }

    override fun plainClick(e: MouseEvent) {
        /* perform action only if one selected node. */
        if (selecteds.size != 1) return
        val component = e.component as MainView
        if (component.isInFollowLinkRegion(e.x.toDouble())) {
            loadURL()
        } else {
            val node = component.nodeView.model
            toggleFolded(node)
        }
    }

    private fun toggleFolded(node: MindMapNode) {
        if (node.hasChildren() && !node.isRoot) {
            setFolded(node, !node.isFolded)
        }
    }

    /* (non-Javadoc)
	 * @see freemind.modes.ControllerAdapter#loadInternally(java.net.URL, freemind.modes.MapAdapter)
	 */
    @Throws(URISyntaxException::class, XMLParseException::class, IOException::class)
    override fun loadInternally(pUrl: URL, pModel: MapAdapter) {
        // empty on purpose.
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap.MapFeedback#out(java.lang.String)
	 */
    override fun out(pFormat: String) {
        frame.out(pFormat)
    }
}
