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
 * Created on 25.02.2006
 */
/*$Id: ScriptEditorProperty.java,v 1.1.2.6 2008/07/04 20:44:02 christianfoltin Exp $*/
package freemind.common

import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.main.HtmlTools
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.logging.Logger
import javax.swing.JButton
import javax.swing.JPopupMenu

class ScriptEditorProperty(
    override var description: String,
    override var label: String,
    private val mMindMapController: MindMapController
) : PropertyBean(), PropertyControl, ActionListener {
    interface ScriptEditorStarter : MindMapControllerPlugin {
        fun startEditor(scriptInput: String?): String?
    }

    var script: String
    var mButton: JButton
    val menu = JPopupMenu()

    /**
     */
    init {
        if (logger == null) {
            logger = mMindMapController.frame.getLogger(
                this.javaClass.name
            )
        }
        mButton = JButton()
        mButton.addActionListener(this)
        script = ""
    }

    override var value: String?
        get() = HtmlTools.unicodeToHTMLUnicodeEntity(
            HtmlTools
                .toXMLEscapedText(script),
            false
        )
        set(value) {
            setScriptValue(value)
        }

    override fun layout(builder: DefaultFormBuilder?, pTranslator: TextTranslator?) {
        val label = builder!!.append(pTranslator!!.getText(label), mButton)
        label.toolTipText = pTranslator.getText(description)
    }

    override fun actionPerformed(arg0: ActionEvent) {
        // search for plugin that handles the script editor.
        for (plugin in mMindMapController.plugins) {
            if (plugin is ScriptEditorStarter) {
                val resultScript = plugin.startEditor(script)
                if (resultScript != null) {
                    script = resultScript
                    firePropertyChangeEvent()
                }
            }
        }
    }

    /**
     */
    private fun setScriptValue(result: String?) {
        script = HtmlTools.toXMLUnescapedText(
            HtmlTools
                .unescapeHTMLUnicodeEntity(if (result == null) "" else script)
        )
        logger!!.fine("Setting script to $script")
        mButton.text = script
    }

    // /**
    // */
    // private Color getColorValue() {
    // return color;
    // }
    override fun setEnabled(pEnabled: Boolean) {
        mButton.isEnabled = pEnabled
    }

    companion object {
        private var logger: Logger? = null
    }
}
