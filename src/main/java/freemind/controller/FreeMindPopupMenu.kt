/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2008 Christian Foltin, Dimitri Polivaev and others.
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
 * Created on 03.01.2008
 *
 */
package freemind.controller

import freemind.controller.StructuredMenuHolder.MenuEventSupplier
import freemind.main.Resources
import java.util.logging.Logger
import javax.swing.JPopupMenu
import javax.swing.event.MenuListener

/**
 * @author foltin
 */
open class FreeMindPopupMenu : JPopupMenu(), MenuEventSupplier {
    private val listeners = HashSet<MenuListener?>()

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
    }

    override fun firePopupMenuWillBecomeVisible() {
        super.firePopupMenuWillBecomeVisible()
        logger!!.fine("Popup firePopupMenuWillBecomeVisible called.")
        for (listener in listeners) {
            listener?.menuSelected(null)
        }
    }

    override fun addMenuListener(listener: MenuListener?) {
        listeners.add(listener)
    }

    override fun removeMenuListener(listener: MenuListener?) {
        listeners.remove(listener)
    }

    override fun firePopupMenuCanceled() {
        super.firePopupMenuCanceled()
        // logger.info("Popup firePopupMenuCanceled called.");
        for (listener in listeners) {
            listener?.menuCanceled(null)
        }
    }

    override fun firePopupMenuWillBecomeInvisible() {
        super.firePopupMenuWillBecomeInvisible()
        // logger.info("Popup firePopupMenuWillBecomeInvisible called.");
        for (listener in listeners) {
            listener?.menuDeselected(null)
        }
    }

    companion object {
        protected var logger: Logger? = null
    }
}