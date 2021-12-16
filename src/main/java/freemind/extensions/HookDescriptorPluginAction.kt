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
 * Created on 22.07.2004
 */
/*$Id: HookDescriptorPluginAction.java,v 1.1.2.2 2008/01/13 20:55:34 christianfoltin Exp $*/
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
import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import freemind.modes.MapFeedback
import freemind.extensions.ModeControllerHook
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHook
import freemind.extensions.HookInstanciationMethod
import freemind.extensions.HookFactory.RegistrationContainer
import freemind.extensions.HookRegistration
import freemind.extensions.ImportWizard
import java.io.IOException
import java.util.zip.ZipFile
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
import java.util.*

/**
 * This is an information class that holds all outer properties of a hook, i.e.
 * all contents of the XML description file.
 *
 * Don't use this class for anything except for the implementation of a
 * HookFactory.
 *
 * @author foltin
 */
class HookDescriptorPluginAction(xmlPluginFile: String,
                                 pluginBase: Plugin, private val pluginAction: PluginAction) : HookDescriptorBase(pluginBase, xmlPluginFile) {
    /**
     */
    val properties: Properties
    var menuPositions: Vector<String?>
    val modes: Vector<String?>

    init {
        if (pluginAction.name == null) {
            pluginAction.name = pluginAction.label
        }
        menuPositions = Vector()
        properties = Properties()
        modes = Vector()
        for (obj in pluginAction.listChoiceList) {
            if (obj is PluginMenu) {
                menuPositions.add(obj.location)
            }
            if (obj is PluginProperty) {
                val property = obj
                properties[property.name] = property.value
            }
            if (obj is PluginMode) {
                modes.add(obj.className)
            }
        }
    }

    override fun toString(): String {
        return ("[HookDescriptor props=" + properties + ", menu positions="
                + menuPositions + "]")
    }

    // this is an error case?
    val instanciationMethod: HookInstanciationMethod?
        get() {
            if (pluginAction.instanciation != null) {
                val allInstMethods: HashMap<String, HookInstanciationMethod> = HookInstanciationMethod.Companion.getAllInstanciationMethods()
                for (name in allInstMethods.keys) {
                    if (pluginAction.instanciation.equals(name, ignoreCase = true)) {
                        return allInstMethods[name]
                    }
                }
            }
            // this is an error case?
            return HookInstanciationMethod.Companion.Other
        }
    val name: String?
        get() = getFromResourceIfNecessary(pluginAction.name)
    val className: String?
        get() = pluginAction.className
    val documentation: String?
        get() = getFromResourceIfNecessary(pluginAction.documentation)
    val iconPath: String?
        get() = pluginAction.iconPath
    val keyStroke: String?
        get() = getFromPropertiesIfNecessary(pluginAction.keyStroke)

    /**
     * @return whether or not the plugin can be on/off and this should be
     * displayed in the menus.
     */
    val isSelectable: Boolean
        get() = pluginAction.getIsSelectable()
}