/*
 *    ImportWizard.java
 *    Copyright (C) 1999 Len Trigg (trigg@cs.waikato.ac.nz)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
// adapted to freemind by christianfoltin, 29.2.2004.
// taken from /usr/share/xemacs/xemacs-packages/etc/jde/java/src/jde/wizards/ImportWizard.java
// changed: package name, commented out the static method.
//			if (current.toLowerCase().endsWith(".properties")) {
//											   // formerly ".class"
// and related changes.
// commented out: // For Java 2! ...
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
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.logging.Logger

/**
 * Converts an unqualified class name to import statements by scanning through
 * the classpath.
 *
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version 1.0 - 6 May 1999
 */
class ImportWizard {
    val lookFor = ".xml"

    /** Stores the list of all classes in the classpath  */
    var CLASS_LIST = Vector<String>(500)

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                    this.javaClass.name)
        }
    }

    /** Build the list of classes  */ // static {
    //
    // // System.err.println("Making class list");
    // buildClassList();
    //
    // // System.err.println("Done (" + CLASS_LIST.size() + " classes)");
    //
    // }
    fun buildClassList() {
        var classPath = System.getProperty("java.class.path")
        val classPathSeparator = File.pathSeparator
        // add the current dir to find more plugins
        classPath = (Resources.getInstance().freemindBaseDir + classPathSeparator
                + classPath)
        logger!!.info("Classpath for plugins:$classPath")
        // to remove duplicates
        val foundPlugins = HashSet<String>()
        val st = StringTokenizer(classPath, classPathSeparator)
        while (st.hasMoreTokens()) {
            val classPathEntry = st.nextToken()
            val classPathFile = File(classPathEntry)
            try {
                val key = classPathFile.canonicalPath
                if (foundPlugins.contains(key)) continue
                logger!!.fine("looking for plugins in $key")
                foundPlugins.add(key)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            }
            if (classPathFile.exists()) {
                val lowerCaseFileName = classPathEntry.lowercase(Locale.getDefault())
                if (lowerCaseFileName.endsWith(".jar")) {
                    logger!!.fine("searching for plugins in: $classPathEntry")
                    addClassesFromZip(CLASS_LIST, classPathFile)
                } else if (lowerCaseFileName.endsWith(".zip")) {
                    logger!!.fine("searching for plugins in: $classPathEntry")
                    addClassesFromZip(CLASS_LIST, classPathFile)
                } else if (classPathFile.isDirectory) {
                    logger!!.fine("searching for plugins in: $classPathEntry")
                    addClassesFromDir(CLASS_LIST, classPathFile, classPathFile,
                            0)
                }
            }
        }
    }

    /**
     * Adds the classes from the supplied Zip file to the class list.
     *
     * @param classList
     * the Vector to add the classes to
     * @param classPathFile
     * the File to scan as a zip file
     */
    fun addClassesFromZip(classList: Vector<String>, classPathFile: File) {
        // System.out.println("Processing jar/zip file: " + classPathFile);
        try {
            val zipFile = ZipFile(classPathFile)
            val enumeration: Enumeration<*> = zipFile.entries()
            while (enumeration.hasMoreElements()) {
                val zipEntry = enumeration.nextElement() as ZipEntry
                var current = zipEntry.name
                if (isInteresting(current)) {
                    current = current.substring(0,
                            current.length - lookFor.length)
                    classList.addElement(current)
                }
            }
        } catch (ex: Exception) {
            Resources.getInstance().logException(ex,
                    "Problem opening $classPathFile with zip.")
        }
    }

    /**
     */
    private fun isInteresting(current: String): Boolean {
        val length = current.length
        if (length < lookFor.length) {
            return false
        }
        val currentPostfix = current.substring(length - lookFor.length)
        return lookFor.equals(currentPostfix, ignoreCase = true)
    }

    /**
     * Adds the classes from the supplied directory to the class list.
     *
     * @param classList
     * the Vector to add the classes to
     * @param currentDir
     * the File to recursively scan as a directory
     * @param recursionLevel
     * To ensure that after a certain depth the recursive directory
     * search stops
     */
    fun addClassesFromDir(classList: Vector<String>, rootDir: File,
                          currentDir: File, recursionLevel: Int) {
        if (recursionLevel >= 6) {
            // search only the first levels
            return
        }
        val files = currentDir.list()
        if (files != null) {
            for (i in files.indices) {
                var current = files[i]
                logger!!.fine("looking at: $current")
                if (isInteresting(current)) {
                    val rootPath = rootDir.path
                    val currentPath = currentDir.path
                    if (!currentPath.startsWith(rootPath)) {
                        logger!!.severe("""
    currentPath doesn't start with rootPath!
    rootPath: $rootPath
    currentPath: $currentPath
    
    """.trimIndent())
                    } else {
                        current = current.substring(0, current.length
                                - lookFor.length)
                        val packageName = currentPath.substring(rootPath
                                .length)
                        var fileName: String
                        fileName = if (packageName.length > 0) {
                            // Not the current directory
                            (packageName.substring(1)
                                    + File.separator + current)
                        } else {
                            // The current directory
                            current
                        }
                        classList.addElement(fileName)
                        logger!!.info("Found: $fileName")
                    }
                } else {
                    // Check if it's a directory to recurse into
                    val currentFile = File(currentDir, current)
                    if (currentFile.isDirectory) {
                        addClassesFromDir(classList, rootDir, currentFile,
                                recursionLevel + 1)
                    }
                }
            }
        }
    }

    companion object {
        protected var logger: Logger? = null
    }
} // ImportWizard
