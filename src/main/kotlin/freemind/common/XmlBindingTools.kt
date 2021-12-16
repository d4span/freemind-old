/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 23.06.2004
 */
/*$Id: XmlBindingTools.java,v 1.1.2.2.2.5 2009/05/20 19:19:11 christianfoltin Exp $*/
package freemind.common

import freemind.common.TextTranslator
import freemind.common.PropertyBean
import freemind.common.PropertyControl
import javax.swing.JComboBox
import javax.swing.DefaultComboBoxModel
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.jgoodies.forms.builder.DefaultFormBuilder
import javax.swing.JLabel
import javax.swing.RootPaneContainer
import freemind.common.FreeMindProgressMonitor
import freemind.common.FreeMindTask.ProgressDescription
import javax.swing.JPanel
import java.awt.event.MouseAdapter
import java.awt.event.MouseMotionAdapter
import java.awt.event.KeyAdapter
import freemind.common.FreeMindTask
import java.lang.Runnable
import kotlin.Throws
import freemind.modes.MindIcon
import javax.swing.JButton
import freemind.modes.IconInformation
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import java.util.Arrays
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
import javax.swing.event.ChangeListener
import javax.swing.event.ChangeEvent
import java.lang.NumberFormatException
import javax.swing.JTable
import javax.swing.JTextField
import java.awt.event.KeyEvent
import freemind.common.BooleanProperty
import javax.swing.JCheckBox
import java.awt.event.ItemListener
import java.awt.event.ItemEvent
import java.util.Locale
import java.awt.event.ComponentListener
import freemind.common.ScalableJButton
import java.awt.event.ComponentEvent
import org.jibx.runtime.IMarshallingContext
import freemind.common.XmlBindingTools
import org.jibx.runtime.JiBXException
import org.jibx.runtime.IUnmarshallingContext
import javax.swing.JDialog
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import javax.swing.JOptionPane
import freemind.controller.actions.generated.instance.XmlAction
import org.jibx.runtime.IBindingFactory
import org.jibx.runtime.BindingDirectory
import javax.swing.JPasswordField
import javax.swing.JComponent
import javax.swing.JSplitPane
import kotlin.jvm.JvmStatic
import tests.freemind.FreeMindMainMock
import javax.swing.JFrame
import freemind.common.JOptionalSplitPane
import freemind.common.ThreeCheckBoxProperty
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin
import freemind.common.ScriptEditorProperty
import freemind.common.ScriptEditorProperty.ScriptEditorStarter
import javax.swing.Icon
import javax.swing.ImageIcon
import freemind.controller.BlindIcon
import javax.swing.JProgressBar
import java.lang.InterruptedException
import freemind.common.OptionalDontShowMeAgainDialog.DontShowPropertyHandler
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.controller.Controller
import freemind.main.*
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.*

/**
 * @author foltin Singleton
 */
class XmlBindingTools private constructor() {
    fun createMarshaller(): IMarshallingContext? {
        return try {
            mBindingFactory!!.createMarshallingContext()
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            null
        }
    }

    fun createUnmarshaller(): IUnmarshallingContext? {
        return try {
            mBindingFactory!!.createUnmarshallingContext()
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            null
        }
    }

    fun storeDialogPositions(controller: Controller, dialog: JDialog,
                             storage: WindowConfigurationStorage,
                             window_preference_storage_property: String?) {
        val result = storeDialogPositions(storage, dialog)
        controller.setProperty(window_preference_storage_property, result)
    }

    fun storeDialogPositions(storage: WindowConfigurationStorage,
                             dialog: JDialog): String? {
        storage.x = dialog.x
        storage.y = dialog.y
        storage.width = dialog.width
        storage.height = dialog.height
        return marshall(storage)
    }

    fun decorateDialog(controller: Controller,
                       dialog: JDialog, window_preference_storage_property: String?): WindowConfigurationStorage? {
        val marshalled = controller
                .getProperty(window_preference_storage_property)
        return decorateDialog(marshalled, dialog)
    }

    fun decorateDialog(marshalled: String?,
                       dialog: JDialog): WindowConfigurationStorage? {
        // String unmarshalled = controller.getProperty(
        // propertyName);
        if (marshalled != null) {
            val storage = unMarshall(marshalled) as WindowConfigurationStorage?
            if (storage != null) {
                // Check that location is on current screen.
                val screenSize: Dimension
                if (Resources.getInstance().getBoolProperty(
                                "place_dialogs_on_first_screen")) {
                    val defaultToolkit = Toolkit.getDefaultToolkit()
                    screenSize = defaultToolkit.screenSize
                } else {
                    screenSize = Dimension()
                    screenSize.height = Int.MAX_VALUE
                    screenSize.width = Int.MAX_VALUE
                }
                val delta = 20
                dialog.setLocation(
                        Math.min(storage.x, screenSize.width - delta),
                        Math.min(storage.y, screenSize.height - delta))
                dialog.size = Dimension(storage.width, storage
                        .height)
                return storage
            }
        }

        // set standard dialog size of no size is stored
        val rootFrame = JOptionPane.getFrameForComponent(dialog)
        val prefSize = rootFrame.size
        prefSize.width = prefSize.width * 3 / 4
        prefSize.height = prefSize.height * 3 / 4
        dialog.size = prefSize
        return null
    }

    fun marshall(action: XmlAction?): String? {
        // marshall:
        // marshal to StringBuffer:
        val writer = StringWriter()
        val m = instance
                .createMarshaller()
        try {
            m!!.marshalDocument(action, "UTF-8", null, writer)
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            return null
        }
        return writer.toString()
    }

    fun unMarshall(inputString: String?): XmlAction? {
        return unMarshall(StringReader(inputString))
    }

    /**
     */
    fun unMarshall(reader: Reader?): XmlAction? {
        return try {
            // unmarshall:
            val u = instance
                    .createUnmarshaller()
            u!!.unmarshalDocument(reader, null) as XmlAction
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            null
        }
    }

    companion object {
        var instance: XmlBindingTools? = null
            get() {
                if (field == null) {
                    field = XmlBindingTools()
                    try {
                        mBindingFactory = BindingDirectory.getFactory(XmlAction::class.java)
                    } catch (e: JiBXException) {
                        Resources.getInstance().logException(e)
                    }
                }
                return field
            }
            private set
        private var mBindingFactory: IBindingFactory? = null
    }
}