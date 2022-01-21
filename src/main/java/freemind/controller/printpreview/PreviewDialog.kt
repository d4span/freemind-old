/*
 *  Preview Dialog - A Preview Dialog for your Swing Applications
 *
 *  Copyright (C) 2003 Jens Kaiser.
 *  Created by Dimitri Polivaev.
 *
 *  Written by: 2003 Jens Kaiser <jens.kaiser@web.de>
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package freemind.controller.printpreview

import freemind.main.Tools
import freemind.view.ImageFactory
import freemind.view.mindmapview.MapView
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.print.PageFormat
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JToolBar

class PreviewDialog(title: String?, protected var view: MapView, pPageFormat: PageFormat?) :
    JDialog(
        JOptionPane.getFrameForComponent(
            view
        ),
        title, true
    ),
    ActionListener {
    private val pageNumber: JLabel
    private fun getButton(iconName: String, action: AbstractAction): JButton {
        return getButton(null, iconName, action)
    }

    private fun getButton(
        name: String?,
        iconName: String,
        action: AbstractAction?
    ): JButton {
        var result: JButton?
        var icon: ImageIcon? = null
        val imageURL = javaClass.classLoader.getResource(
            "images/$iconName"
        )
        if (imageURL != null) icon = ImageFactory.getInstance().createIcon(imageURL)
        result = if (action != null) {
            if (icon != null) action.putValue(
                Action.SMALL_ICON,
                ImageFactory.getInstance().createIcon(imageURL)
            )
            if (name != null) action.putValue(Action.NAME, name)
            JButton(action)
        } else JButton(name, icon)
        return result
    }

    override fun actionPerformed(e: ActionEvent) {
        dispose()
    }

    init {
        val preview = Preview(view, 1.0, pPageFormat!!)
        val scrollPane = JScrollPane(preview)
        contentPane.add(scrollPane, "Center")
        val toolbar = JToolBar()
        // toolbar.setRollover(true);
        contentPane.add(toolbar, "North")
        pageNumber = JLabel("- 1 -")
        val button = getButton(
            "Back24.gif",
            BrowseAction(
                preview, pageNumber, -1
            )
        )
        toolbar.add(button)
        pageNumber.preferredSize = button.preferredSize
        pageNumber.horizontalAlignment = JLabel.CENTER
        toolbar.add(pageNumber)
        toolbar.add(
            getButton(
                "Forward24.gif",
                BrowseAction(
                    preview,
                    pageNumber, 1
                )
            )
        )
        toolbar.add(JToolBar.Separator())
        toolbar.add(
            getButton(
                "ZoomIn24.gif",
                ZoomAction(
                    preview,
                    DEFAULT_ZOOM_FACTOR_STEP
                )
            )
        )
        toolbar.add(
            getButton(
                "ZoomOut24.gif",
                ZoomAction(
                    preview,
                    -DEFAULT_ZOOM_FACTOR_STEP
                )
            )
        )
        toolbar.add(JToolBar.Separator())
        val dialog = JPanel()
        dialog.layout = FlowLayout(FlowLayout.RIGHT)
        val ok = JButton("OK")
        ok.addActionListener(this)
        dialog.add(ok)
        contentPane.add(dialog, "South")
        Tools.addEscapeActionToDialog(this)
    }

    companion object {
        private const val DEFAULT_ZOOM_FACTOR_STEP = 0.1
    }
}
