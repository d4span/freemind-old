/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Dimitri Polivaev, Christian Foltin and others.
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
 * Created on 18.08.2006
 */
/*$Id: HookDescriptorRegistration.java,v 1.1.2.1 2006/08/20 19:34:25 christianfoltin Exp $*/
package freemind.extensions

import freemind.controller.Controller.obtainFocusForSelected
import freemind.controller.actions.generated.instance.*
import freemind.controller.actions.generated.instance.Plugin.listChoiceList
import freemind.controller.actions.generated.instance.PluginClasspath.jar
import freemind.controller.actions.generated.instance.Plugin.label
import freemind.controller.actions.generated.instance.PluginAction.name
import freemind.controller.actions.generated.instance.PluginAction.label
import freemind.controller.actions.generated.instance.PluginAction.listChoiceList
import freemind.controller.actions.generated.instance.PluginMenu.location
import freemind.controller.actions.generated.instance.PluginProperty.name
import freemind.controller.actions.generated.instance.PluginProperty.value
import freemind.controller.actions.generated.instance.PluginMode.className
import freemind.controller.actions.generated.instance.PluginAction.instanciation
import freemind.controller.actions.generated.instance.PluginAction.base
import freemind.controller.actions.generated.instance.PluginAction.className
import freemind.controller.actions.generated.instance.PluginAction.documentation
import freemind.controller.actions.generated.instance.PluginAction.iconPath
import freemind.controller.actions.generated.instance.PluginAction.keyStroke
import freemind.controller.actions.generated.instance.PluginRegistration.className
import freemind.controller.actions.generated.instance.PluginRegistration.listPluginModeList
import freemind.extensions.MindMapHook
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.extensions.ModeControllerHookAdapter
import freemind.view.mindmapview.MapView
import freemind.modes.ModeController
import freemind.extensions.ExportHook
import freemind.main.Tools
import java.awt.image.BufferedImage
import java.awt.Rectangle
import java.awt.Graphics
import java.io.FileOutputStream
import java.io.FileInputStream
import freemind.modes.FreeMindFileDialog
import javax.swing.JFileChooser
import java.text.MessageFormat
import javax.swing.JOptionPane
import java.util.Properties
import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import freemind.modes.MapFeedback
import freemind.extensions.ModeControllerHook
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHook
import freemind.extensions.HookInstanciationMethod
import freemind.extensions.HookFactory.RegistrationContainer
import freemind.extensions.HookRegistration
import freemind.extensions.ImportWizard
import java.util.StringTokenizer
import java.io.IOException
import java.util.Locale
import java.util.zip.ZipFile
import java.util.Enumeration
import java.util.zip.ZipEntry
import freemind.extensions.HookAdapter
import freemind.main.XMLElement
import kotlin.Throws
import freemind.extensions.HookDescriptorBase
import java.net.URLClassLoader
import java.net.MalformedURLException
import freemind.extensions.HookFactory
import freemind.extensions.HookInstanciationMethod.DestinationNodesGetter
import freemind.extensions.HookInstanciationMethod.DefaultDestinationNodesGetter
import freemind.extensions.HookInstanciationMethod.RootDestinationNodesGetter
import freemind.extensions.HookInstanciationMethod.AllDestinationNodesGetter
import freemind.extensions.NodeHookAdapter
import freemind.extensions.PermanentNodeHookAdapter
import java.lang.InterruptedException
import java.lang.reflect.InvocationTargetException
import freemind.modes.mindmapmode.actions.xml.ActionPair

/**
 * @author foltin
 */
class HookDescriptorRegistration(xmlPluginFile: String,
                                 pluginBase: Plugin, private val mRegistration: PluginRegistration) : HookDescriptorBase(pluginBase, xmlPluginFile) {
    // public PluginRegistration getPluginRegistration() {
    // return mRegistration;
    // }
    val className: String?
        get() = mRegistration.className
    val isPluginBase: Boolean
        get() = mRegistration.getIsPluginBase()
    val listPluginModeList: List<PluginMode?>
        get() = mRegistration.listPluginModeList
}