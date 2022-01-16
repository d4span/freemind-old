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
/*$Id: NodeMotionListenerView.java,v 1.1.4.4.4.9 2009/03/29 19:37:23 christianfoltin Exp $*/
package freemind.view.mindmapview

import freemind.main.Resources
import freemind.main.Tools
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Cursor
import java.awt.Graphics
import java.awt.Graphics2D
import java.util.logging.Logger
import javax.swing.JComponent

/**
 *
 * The oval appearing to move nodes to other positions.
 *
 * @author Dimitri
 */
class NodeMotionListenerView(view: NodeView) : JComponent() {
    val movedView: NodeView
    var isMouseEntered = false
        private set

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
        movedView = view
        val map = view.map
        addMouseListener(map.nodeMotionListener)
        addMouseMotionListener(map.nodeMotionListener)
        // fc, 16.6.2005: to emphasis the possible movement.
        cursor = Cursor(Cursor.MOVE_CURSOR)
        val helpMsg = Resources.getInstance().getResourceString(
            "node_location_help"
        )
        this.toolTipText = helpMsg
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (isMouseEntered) {
            val g2 = g as Graphics2D
            // set antialiasing.
            val renderingHint = movedView.map.setEdgesRenderingHint(g2)
            val color = g2.color
            val oldStroke = g2.stroke
            g2.stroke = BasicStroke()
            if (movedView.model.hGap <= 0) {
                g2.color = Color.RED
                g.fillOval(0, 0, width - 1, height - 1)
            } else {
                g2.color = Color.BLACK
                g.drawOval(0, 0, width - 1, height - 1)
            }
            g2.stroke = oldStroke
            g2.color = color
            Tools.restoreAntialiasing(g2, renderingHint)
        }
    }

    fun setMouseEntered() {
        isMouseEntered = true
        // fc, 13.3.2008: variable is not used:
        // final FreeMindMain frame =
        // movedView.getMap().getModel().getModeController().getFrame();
        repaint()
    }

    fun setMouseExited() {
        isMouseEntered = false
        repaint()
    }

    companion object {
        protected var logger: Logger? = null
    }
}
