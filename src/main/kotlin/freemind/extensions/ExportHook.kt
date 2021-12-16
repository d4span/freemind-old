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
 * Created on 16.10.2004
 */
/*$Id: ExportHook.java,v 1.1.4.7.2.12 2010/02/27 09:27:50 christianfoltin Exp $*/
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
import java.io.*
import java.lang.Exception
import javax.swing.filechooser.FileFilter

/**
 * @author foltin
 */
open class ExportHook : ModeControllerHookAdapter() {
    private var view: MapView? = null

    /**
     * @param type
     * @param description
     * @param nameExtension
     * @return
     */
    protected fun chooseFile(type: String, description: String?,
                             nameExtension: String?): File? {
        val controller = controller
        return chooseImageFile(type, description, nameExtension, controller)
    }

    class ImageFilter(private val type: String, private val description: String?) : FileFilter() {
        override fun accept(f: File): Boolean {
            if (f.isDirectory) {
                return true
            }
            val extension = Tools.getExtension(f.name)
            return Tools.safeEqualsIgnoreCase(extension, type)
        }

        override fun getDescription(): String {
            return description ?: type
        }
    }

    protected fun getTranslatableResourceString(resourceName: String?): String? {
        val returnValue = getResourceString(resourceName!!)
        return if (returnValue != null && returnValue.startsWith("%")) {
            controller!!.getText(returnValue.substring(1))
        } else returnValue
    }

    fun createBufferedImage(): BufferedImage? {
        view = controller!!.view
        if (view == null) return null

        // Determine which part of the view contains the nodes of the map:
        // (Needed to eliminate areas of whitespace around the actual rendering
        // of the map)

        // NodeAdapter root = (NodeAdapter) getController().getMap().getRoot();

        // call prepare printing to lay out for printing before getting the
        // inner bounds
        view!!.preparePrinting()
        val innerBounds = view!!.innerBounds

        // Create an image containing the map:
        var myImage = view!!.createImage(
                view!!.width, view!!.height) as BufferedImage

        // Render the mind map nodes on the image:
        val g = myImage.graphics
        g.clipRect(innerBounds.x, innerBounds.y, innerBounds.width,
                innerBounds.height)
        view!!.print(g)
        myImage = myImage.getSubimage(innerBounds.x, innerBounds.y,
                innerBounds.width, innerBounds.height)
        view!!.endPrinting()
        return myImage
        // NodeAdapter root = (NodeAdapter) getController().getMap().getRoot();
        // Rectangle rect = view.getInnerBounds(root.getViewer());
        //
        // BufferedImage image =
        // new BufferedImage(
        // rect.width,
        // rect.height,
        // BufferedImage.TYPE_INT_RGB);
        // Graphics2D g = (Graphics2D) image.createGraphics();
        // g.translate(-rect.getMinX(), -rect.getMinY());
        // view.update(g);
        // return image;
    }

    /**
     */
    protected fun copyFromResource(prefix: String, fileName: String,
                                   destinationDirectory: String) {
        // adapted from http://javaalmanac.com/egs/java.io/CopyFile.html
        // Copies src file to dst file.
        // If the dst file does not exist, it is created
        try {
            logger!!.finest("searching for $prefix$fileName")
            val resource = getResource(prefix + fileName)
            if (resource == null) {
                logger!!.severe("Cannot find resource: $prefix$fileName")
                return
            }
            val `in` = resource.openStream()
            val out: OutputStream = FileOutputStream(destinationDirectory + "/"
                    + fileName)

            // Transfer bytes from in to out
            Tools.copyStream(`in`, out, true)
        } catch (e: Exception) {
            logger!!.severe("File not found or could not be copied. "
                    + "Was earching for " + prefix + fileName
                    + " and should go to " + destinationDirectory)
            Resources.getInstance().logException(e)
        }
    }

    /**
     */
    protected fun copyFromFile(dir: String, fileName: String,
                               destinationDirectory: String) {
        // adapted from http://javaalmanac.com/egs/java.io/CopyFile.html
        // Copies src file to dst file.
        // If the dst file does not exist, it is created
        try {
            logger!!.finest("searching for $dir$fileName")
            val resource = File(dir, fileName)
            val `in`: InputStream = FileInputStream(resource)
            val out: OutputStream = FileOutputStream(destinationDirectory + "/"
                    + fileName)

            // Transfer bytes from in to out
            Tools.copyStream(`in`, out, true)
        } catch (e: Exception) {
            logger!!.severe("File not found or could not be copied. "
                    + "Was earching for " + dir + fileName
                    + " and should go to " + destinationDirectory)
            Resources.getInstance().logException(e)
        }
    }

    companion object {
        fun chooseImageFile(type: String, description: String?,
                            nameExtension: String?, controller: ModeController?): File? {
            val component = controller!!.frame.contentPane
            val filter = ImageFilter(type, description)
            var chooser: FreeMindFileDialog? = null
            chooser = controller.getFileChooser(filter)
            val mmFile = controller.map.file
            if (mmFile != null) {
                val proposedName = (mmFile.absolutePath.replaceFirst(
                        "\\.[^.]*?$".toRegex(), "")
                        + (nameExtension ?: "")
                        + "."
                        + type)
                chooser.selectedFile = File(proposedName)
            }
            val returnVal = chooser.showSaveDialog(component)
            if (returnVal != JFileChooser.APPROVE_OPTION) { // not ok pressed
                return null
            }

            // |= Pressed O.K.
            var chosenFile = chooser.selectedFile
            val ext = Tools.getExtension(chosenFile.name)
            if (!Tools.safeEqualsIgnoreCase(ext, type)) {
                chosenFile = File(chosenFile.parent, chosenFile.name
                        + "." + type)
            }
            if (chosenFile.exists()) { // If file exists, ask before overwriting.
                val overwriteText = MessageFormat.format(controller
                        .getText("file_already_exists"), *arrayOf<Any>(chosenFile
                        .toString()))
                val overwriteMap = JOptionPane.showConfirmDialog(component,
                        overwriteText, overwriteText, JOptionPane.YES_NO_OPTION)
                if (overwriteMap != JOptionPane.YES_OPTION) {
                    return null
                }
            }
            return chosenFile
        }
    }
}