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
/*$Id: MindMapHook.java,v 1.1.4.4.2.2 2006/07/25 20:28:20 christianfoltin Exp $*/
package freemind.extensions

import freemind.controller.Controller.obtainFocusForSelected
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
import freemind.controller.actions.generated.instance.PluginClasspath
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
import freemind.controller.actions.generated.instance.PluginAction
import freemind.controller.actions.generated.instance.PluginMenu
import freemind.controller.actions.generated.instance.PluginProperty
import freemind.controller.actions.generated.instance.PluginMode
import freemind.controller.actions.generated.instance.PluginRegistration

/**
 * This is the most general hook interface. It is a base class for all hooks.
 * [ModeControllerHook] for hooks that implement actions independent of nodes
 * [NodeHook] for hooks that implement actions belonging to nodes
 *
 * @author foltin
 */
interface MindMapHook {
    /**
     * @return The name of the hook. In the xml description, this is the unique
     * label.
     */
    var name: String?

    /**
     * @param properties
     * the properties of the property file belonging to the hook are
     * passed.
     */
    fun setProperties(properties: Properties?)

    /**
     * looks for a property in the plugin properties file, or from the localized resources.
     */
    fun getResourceString(property: String): String
    fun setController(controller: MapFeedback?)

    /**
     * If a base class is specified in the plugin declaration via a
     * plugin_registration entry with isPluginBase==true, this object is
     * returned here. You can use it to realize something like the state of a
     * plugin (eg. is the plugin switched on or off?).<br></br>
     *
     * An example is the menu status of the encrypted nodes. If the node is not
     * encrypted, the encryption state cannot be toggled (see EncryptNode.java).<br></br>
     *
     * Another example arises from the collaboration mode. The state (connected,
     * wait for second party, map sharing etc.) can be stored in the plugin
     * base.<br></br>
     *
     * Remember, that it is most likely that you havn't specified the base class
     * and that you get NULL here.
     *
     * @return The object returned is of HookRegistration type but has to be
     * casted anyway.
     */
    fun getPluginBaseClass(): Any?

    /**
     * This indirection is necessary, as all stored PermanentNodeHooks are
     * created during the map's creation time but the registrations are
     * underdone on ModeController's startup method later.
     */
    interface PluginBaseClassSearcher {
        /**
         * @return the plugin base object [HookRegistration].
         */
        val pluginBaseObject: Any?
    }

    fun setPluginBaseClass(baseClass: PluginBaseClassSearcher?)
    /* Hooks */
    /**
     * This method is also called, if the hook is created in the map.
     */
    fun startupMapHook()

    /**
     * This method is also called, if the node, this hook belongs to, is removed
     * from the map.
     */
    fun shutdownMapHook()
}