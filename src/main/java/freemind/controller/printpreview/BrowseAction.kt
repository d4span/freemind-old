/*
 *  Preview Dialog - A Preview Dialog for your Swing Applications
 *
 *  Copyright (C) 2003 Jens Kaiser.
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

import java.awt.EventQueue
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JLabel

internal class BrowseAction(
    protected var preview: Preview,
    private val pageNumber: JLabel,
    protected var pageStep: Int
) : AbstractAction() {
    override fun actionPerformed(e: ActionEvent) {
        preview.moveIndex(pageStep)
        paintPageIndex()
        preview.repaint()
        EventQueue.invokeLater(pageIndexPainter)
    }

    private fun paintPageIndex() {
        pageNumber.text = pageIndexText
        pageNumber.paintImmediately(
            0, 0, pageNumber.width,
            pageNumber.height
        )
    }

    private val pageIndexText: String
        get() = "- " + (1 + preview.pageIndex).toString() + " -"
    private val pageIndexPainter: Runnable

    init {
        pageIndexPainter = Runnable { paintPageIndex() }
    }
}