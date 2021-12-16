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
/*$Id: HookDescriptorBase.java,v 1.1.2.7 2008/07/09 20:01:00 christianfoltin Exp $*/
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
import freemind.main.Resources
import java.lang.InterruptedException
import java.lang.reflect.InvocationTargetException
import freemind.modes.mindmapmode.actions.xml.ActionPair
import java.io.File
import java.net.URL
import java.util.*
import java.util.logging.Logger

/**
 * @author foltin
 */
open class HookDescriptorBase(val pluginBase: Plugin,
                              protected val mXmlPluginFile: String) {
    /**
     */
    protected fun getFromResourceIfNecessary(string: String?): String? {
        if (string == null) {
            return string
        }
        return if (string.startsWith("%")) {
            Resources.getInstance().getResourceString(string.substring(1))
        } else string
    }

    protected fun getFromPropertiesIfNecessary(string: String?): String? {
        if (string == null) {
            return string
        }
        return if (string.startsWith("%")) {
            Resources.getInstance().getProperty(string.substring(1))
        } else string
    }

    /**
     * @return the relative/absolute(?) position of the plugin xml file.
     */
    private val pluginDirectory: String
        private get() = (Resources.getInstance().freemindBaseDir + "/"
                + File(mXmlPluginFile).parent)
    val pluginClasspath: List<PluginClasspath>
        get() {
            val returnValue = Vector<PluginClasspath>()
            for (obj in pluginBase.listChoiceList) {
                if (obj is PluginClasspath) {
                    returnValue.add(obj)
                }
            }
            return returnValue
        }

    // construct class loader:
    val pluginClassLoader: ClassLoader?
        get() {
            // construct class loader:
            val pluginClasspathList = pluginClasspath
            return getClassLoader(pluginClasspathList)
        }

    /**
     * @param pluginBase
     * @param frame
     * @param xmlPluginFile
     */
    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                    this.javaClass.name)
        }
    }

    /**
     * This string is used to identify known classloaders as they are cached.
     *
     */
    private fun createPluginClasspathString(pluginClasspathList: List<PluginClasspath>): String {
        var result = ""
        for (type in pluginClasspathList) {
            result += type.jar + ","
        }
        return result
    }

    /**
     * @throws MalformedURLException
     */
    private fun getClassLoader(pluginClasspathList: List<PluginClasspath>): ClassLoader? {
        val key = createPluginClasspathString(pluginClasspathList)
        return if (classLoaderCache.containsKey(key)) classLoaderCache[key] else try {
            val urls = arrayOfNulls<URL>(pluginClasspathList.size)
            var j = 0
            for (classPath in pluginClasspathList) {
                val jarString = classPath.jar
                // if(jarString.startsWith(FREEMIND_BASE_DIR_STRING)){
                // jarString = frame.getFreemindBaseDir() +
                // jarString.substring(FREEMIND_BASE_DIR_STRING.length());
                // }
                // new version of classpath resolution suggested by ewl under
                // patch [ 1154510 ] Be able to give absolute classpath entries
                // in plugin.xml
                var file = File(jarString)
                if (!file.isAbsolute) {
                    file = File(pluginDirectory, jarString)
                }
                // end new version by ewl.
                logger!!.info("file " + Tools.fileToUrl(file) + " exists = "
                        + file.exists())
                urls[j++] = Tools.fileToUrl(file)
            }
            val loader: ClassLoader = URLClassLoader(urls,
                    Resources.getInstance().freeMindClassLoader)
            classLoaderCache[key] = loader
            loader
        } catch (e: MalformedURLException) {
            Resources.getInstance().logException(e)
            this.javaClass.classLoader
        }
    }

    companion object {
        const val FREEMIND_BASE_DIR_STRING = "\${freemind.base.dir}"

        // Logging:
        protected var logger: Logger? = null
        private val classLoaderCache = HashMap<String, ClassLoader>()
    }
}