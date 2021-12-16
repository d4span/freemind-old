/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
 * Created on 09.04.2006
 * Created by Dimitri Polivaev
 */
package freemind.preferences.layout

import freemind.main.FreeMind.controller
import freemind.controller.actions.generated.instance.OptionPanelWindowConfigurationStorage.panel
import freemind.common.PropertyBean.label
import freemind.main.FreeMind.getAdjustableProperty
import freemind.common.PropertyBean.value
import freemind.common.PropertyControl.layout
import freemind.main.FreeMind.getResourceString
import freemind.common.PropertyBean.firePropertyChangeEvent
import freemind.common.TextTranslator.getText
import freemind.main.Tools.removeTranslateComment
import freemind.main.FreeMind.getDefaultProperty
import freemind.controller.Controller.modeController
import freemind.modes.IconInformation.keystrokeResourceName
import freemind.modes.IconInformation.description
import freemind.modes.IconInformation.icon
import freemind.main.FreeMindMain.getResourceString
import freemind.main.Tools.isMacOsX
import freemind.main.FreeMind
import javax.swing.JDialog
import freemind.preferences.layout.OptionPanel.OptionPanelFeedback
import freemind.common.TextTranslator
import freemind.common.PropertyControl
import freemind.preferences.layout.OptionPanel.KeyProperty
import javax.swing.JButton
import freemind.preferences.layout.OptionPanel.ChangeTabAction
import java.util.Properties
import freemind.common.PropertyBean
import freemind.preferences.layout.OptionPanel
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.preferences.layout.VariableSizeCardLayout
import javax.swing.JPanel
import freemind.preferences.layout.OptionPanel.NewTabProperty
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.jgoodies.forms.builder.ButtonBarBuilder
import kotlin.jvm.JvmOverloads
import javax.swing.ImageIcon
import com.jgoodies.forms.layout.RowSpec
import freemind.preferences.layout.GrabKeyDialog
import java.awt.event.KeyEvent
import javax.swing.JLabel
import freemind.common.SeparatorProperty
import freemind.common.ComboProperty
import freemind.main.FreeMindCommon
import freemind.main.Tools
import freemind.common.BooleanProperty
import freemind.common.NextLineProperty
import freemind.common.PasswordProperty
import freemind.modes.MindMapNode
import javax.swing.UIManager.LookAndFeelInfo
import javax.swing.UIManager
import freemind.controller.StructuredMenuHolder
import freemind.modes.MindIcon
import freemind.modes.ModeController
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.actions.IconAction
import freemind.modes.IconInformation
import freemind.common.RemindValueProperty
import freemind.common.DontShowNotificationProperty
import freemind.preferences.FreemindPropertyContributor
import freemind.controller.actions.generated.instance.OptionPanelWindowConfigurationStorage
import freemind.common.XmlBindingTools
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import freemind.main.FreeMindMain
import freemind.preferences.layout.GrabKeyDialog.InputPane
import javax.swing.WindowConstants
import java.util.Enumeration
import javax.swing.JTextField
import freemind.preferences.layout.KeyEventWorkaround
import freemind.preferences.layout.KeyEventTranslator
import java.awt.*
import java.lang.StringBuffer
import javax.swing.JOptionPane
import java.awt.event.InputEvent

class VariableSizeCardLayout : CardLayout {
    constructor() : super() {}
    constructor(hgap: Int, vgap: Int) : super(hgap, vgap) {}

    /**
     * Determines the preferred size of the container argument using this card
     * layout.
     *
     * @param parent
     * the parent container in which to do the layout
     * @return the preferred dimensions to lay out the subcomponents of the
     * specified container
     * @see java.awt.Container.getPreferredSize
     *
     * @see java.awt.CardLayout.minimumLayoutSize
     */
    override fun preferredLayoutSize(parent: Container): Dimension {
        synchronized(parent.treeLock) {
            val insets = parent.insets
            val ncomponents = parent.componentCount
            var w = 0
            var h = 0
            for (i in 0 until ncomponents) {
                val comp = parent.getComponent(i)
                if (comp.isVisible) {
                    val d = comp.preferredSize
                    if (d.width > w) {
                        w = d.width
                    }
                    if (d.height > h) {
                        h = d.height
                    }
                }
            }
            return Dimension(
                    insets.left + insets.right + w + hgap * 2, insets.top
                    + insets.bottom + h + vgap * 2)
        }
    }
}