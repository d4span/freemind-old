/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 31.01.2006
 */
package freemind.modes.common.dialogs

import freemind.modes.ModeController
import java.awt.event.ActionListener
import javax.swing.JComboBox

class PersistentEditableComboBox(
    private val mModeController: ModeController,
    private val pStorageKey: String
) : JComboBox<Any?>() {
    private var actionListener: ActionListener? = null
    private var sendExternalEvents = true

    init {
        setEditable(true)
        addUrl("", false)
        val storedUrls = mModeController.frame.getProperty(pStorageKey)
        if (storedUrls != null) {
            val array = storedUrls.split("\t").toTypedArray()
            for (i in array.indices) {
                val string = array[i]
                addUrl(string, false)
            }
        }
        selectedIndex = 0
        super.addActionListener { arg0 ->
            addUrl(text, false)
            // notification only if a new string is entered.
            if (sendExternalEvents && actionListener != null) {
                actionListener!!.actionPerformed(arg0)
            }
        }
    }

    override fun addActionListener(arg0: ActionListener) {
        actionListener = arg0
    }

    private fun addUrl(selectedItem: String?, calledFromSetText: Boolean): Boolean {
        // search:
        for (i in 0 until model.size) {
            val element = model.getElementAt(i) as String
            if (element == selectedItem) {
                if (calledFromSetText) {
                    selectedIndex = i
                }
                return false
            }
        }
        addItem(selectedItem)
        selectedIndex = model.size - 1
        if (calledFromSetText) {
            val resultBuffer = StringBuffer()
            for (i in 0 until model.size) {
                val element = model.getElementAt(i) as String
                resultBuffer.append(element)
                resultBuffer.append("\t")
            }
            mModeController.frame.setProperty(
                pStorageKey,
                resultBuffer.toString()
            )
        }
        return true
    }

    var text: String?
        get() = selectedItem.toString()
        set(text) {
            sendExternalEvents = false
            addUrl(text, true)
            sendExternalEvents = true
        }
}
