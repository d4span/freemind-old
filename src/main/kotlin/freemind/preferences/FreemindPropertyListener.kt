/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2005  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 10.05.2005
 */
package freemind.preferences

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
import java.awt.CardLayout
import freemind.preferences.layout.VariableSizeCardLayout
import javax.swing.JPanel
import freemind.preferences.layout.OptionPanel.NewTabProperty
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import java.awt.BorderLayout
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
import java.awt.Color
import freemind.main.FreeMindMain
import freemind.preferences.layout.GrabKeyDialog.InputPane
import java.awt.AWTEvent
import java.awt.GridLayout
import javax.swing.WindowConstants
import java.util.Enumeration
import javax.swing.JTextField
import freemind.preferences.layout.KeyEventWorkaround
import freemind.preferences.layout.KeyEventTranslator
import java.lang.StringBuffer
import javax.swing.JOptionPane
import java.awt.event.InputEvent
import java.awt.Dimension
import java.awt.Insets

/**
 * Is issued by the OptionPanel when the user accepted a change of its
 * preferences.
 */
interface FreemindPropertyListener {
    fun propertyChanged(propertyName: String?, newValue: String?, oldValue: String?)
}