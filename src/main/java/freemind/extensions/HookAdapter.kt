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
 * Created on 29.02.2004
 *
 */
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
import freemind.main.Resources
import java.net.URL
import java.util.logging.Logger

/**
 * Implments MindMapHook as an Adapter class. Implementation is straight
 * forward.
 *
 * @author foltin
 */
open class HookAdapter : MindMapHook {
    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#getName()
	 */  override var name: String? = null
    private var properties: Properties? = null
    private var controller: ModeController? = null

    // Logging:
    protected var logger: Logger? = null

    /**
     * Stores the plugin base class as declared by the
     * plugin_registration/isBaseClass attribute.
     */
    private var baseClass: PluginBaseClassSearcher?
    protected var mapFeedback: MapFeedback? = null

    /**
     */
    init {
        if (logger == null) logger = Resources.getInstance().getLogger(
                this.javaClass.name)
        baseClass = null
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#startupMapHook(java.lang.String)
	 */
    override fun startupMapHook() {
        // TODO Auto-generated method stub
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#shutdownMapHook()
	 */
    override fun shutdownMapHook() {
        controller = null
    }

    /**
     */
    protected fun getController(): ModeController? {
        return controller
    }

    /**
     */
    protected fun getProperties(): Properties? {
        return properties
    }

    /**
     */
    override fun setProperties(properties: Properties?) {
        this.properties = properties
    }

    /**
     */
    override fun setController(controller: MapFeedback?) {
        mapFeedback = controller
        if (controller is ModeController) {
            this.controller = controller
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.MindMapHook#getResourceString(java.lang.String)
	 */
    override fun getResourceString(property: String): String {
        var result = properties!!.getProperty(property)
        if (result == null) {
            result = getController()!!.getText(property)
        }
        if (result == null) {
            logger!!.warning("The following property was not found:$property")
        }
        return result
    }

    fun getResource(resourceName: String?): URL {
        return this.javaClass.classLoader.getResource(resourceName)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.MindMapHook#getPluginBaseClass()
	 */
    override fun getPluginBaseClass(): Any? {
        return baseClass.getPluginBaseObject()
    }

    override fun setPluginBaseClass(baseClass: PluginBaseClassSearcher?) {
        this.baseClass = baseClass
    }

    /**
     * After tree node change, the focus must be obtained as it is invalid.
     */
    protected fun obtainFocusForSelected() {
        // Focus fix
        getController()!!.controller.obtainFocusForSelected()
    }
}