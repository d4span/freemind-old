/*
 * FreeMind - A Program for creating and viewing MindmapsCopyright (C) 2000-2015
 * Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and
 * others.
 * 
 * See COPYING for Details
 * 
 * This program is free software; you can redistribute it and/ormodify it under
 * the terms of the GNU General Public Licenseas published by the Free Software
 * Foundation; either version 2of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty ofMERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See theGNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public Licensealong with
 * this program; if not, write to the Free SoftwareFoundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.common

import freemind.common.TextTranslator
import freemind.common.PropertyBean
import freemind.common.PropertyControl
import javax.swing.JComboBox
import java.awt.GraphicsEnvironment
import javax.swing.DefaultComboBoxModel
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.jgoodies.forms.builder.DefaultFormBuilder
import javax.swing.JLabel
import javax.swing.RootPaneContainer
import freemind.common.FreeMindProgressMonitor
import freemind.common.FreeMindTask.ProgressDescription
import javax.swing.JPanel
import java.awt.GridLayout
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
import java.awt.Color
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
import java.util.Arrays
import java.io.PushbackInputStream
import java.io.IOException
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
import java.awt.Dimension
import javax.swing.JOptionPane
import freemind.controller.actions.generated.instance.XmlAction
import org.jibx.runtime.IBindingFactory
import org.jibx.runtime.BindingDirectory
import javax.swing.JPasswordField
import javax.swing.JComponent
import java.awt.BorderLayout
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
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import java.lang.InterruptedException
import freemind.common.OptionalDontShowMeAgainDialog.DontShowPropertyHandler
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.main.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.logging.Logger

/**
 * @author foltin
 * @date 19.06.2015
 * @see http://stackoverflow.com/questions/8183949/swing-scale-a-text-font-of-component
 */
open class ScalableJButton : JButton, ComponentListener {
    constructor(pString: String?) : super(pString) {
        init()
    }

    constructor() : super() {
        init()
    }

    private fun init() {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(this.javaClass.name)
        }

//		mInitialFont = getFont();
//		addComponentListener(this);
    }

    override fun componentResized(pE: ComponentEvent) {
//		// FIXME: This doesn't work in GridBagLayout and GroupLayout.
//		if (mInitialHeight == 0) {
//			mInitialHeight = getHeight();
//		}
//		int resizal = this.getHeight() * mInitialFont.getSize() / mInitialHeight;
//		if(resizal != mCurrentSize){
//			setFont(mInitialFont.deriveFont((float) resizal));
//			mCurrentSize = resizal;
//		}
    }

    override fun componentMoved(pE: ComponentEvent) {}
    override fun componentShown(pE: ComponentEvent) {}
    override fun componentHidden(pE: ComponentEvent) {}

    companion object {
        protected var logger: Logger? = null

        //	int mCurrentSize = 0;
        //	Font mInitialFont = null;
        //	int mInitialHeight;
        private const val serialVersionUID = 1L
    }
}