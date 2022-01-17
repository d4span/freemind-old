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
package freemind.modes.browsemode

import freemind.main.Resources
import freemind.modes.ControllerAdapter
import freemind.modes.common.dialogs.PersistentEditableComboBox
import java.awt.event.ActionListener
import java.net.URL
import javax.swing.JLabel
import javax.swing.JToolBar

class BrowseToolBar(private val c: ControllerAdapter) : JToolBar() {
    private var urlfield: PersistentEditableComboBox? = null

    init {
        this.isRollover = true
        this.add(c.controller.showFilterToolbarAction)
        urlfield = PersistentEditableComboBox(
            c,
            BROWSE_URL_STORAGE_KEY
        )
        urlfield?.addActionListener(
            ActionListener { e ->
                val urlText = urlfield?.text
                if ("" == urlText || e.actionCommand == "comboBoxEdited") return@ActionListener
                try {
                    c.load(URL(urlText))
                } catch (e1: Exception) {
                    Resources.getInstance().logException(e1)
                    // FIXME: Give a good error message.
                    c.controller.errorMessage(e1)
                }
            }
        )
        add(JLabel("URL:"))
        add(urlfield)
    }

    fun setURLField(text: String?) {
        urlfield!!.text = text
    }

    companion object {
        const val BROWSE_URL_STORAGE_KEY = "browse_url_storage"
    }
}
