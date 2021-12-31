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

import freemind.main.Resources
import freemind.main.Tools
import freemind.modes.FreeMindFileDialog
import freemind.modes.ModeController
import freemind.view.mindmapview.MapView
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.MessageFormat
import javax.swing.JFileChooser
import javax.swing.JOptionPane
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
    protected fun chooseFile(
        type: String,
        description: String?,
        nameExtension: String?
    ): File? {
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
        val returnValue = getResourceString(resourceName)
        return if (returnValue != null && returnValue.startsWith("%")) {
            controller?.getText(returnValue.substring(1))
        } else returnValue
    }

    fun createBufferedImage(): BufferedImage? {
        view = controller?.view
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
            view!!.width, view!!.height
        ) as BufferedImage

        // Render the mind map nodes on the image:
        val g = myImage.graphics
        g.clipRect(
            innerBounds.x, innerBounds.y, innerBounds.width,
            innerBounds.height
        )
        view!!.print(g)
        myImage = myImage.getSubimage(
            innerBounds.x, innerBounds.y,
            innerBounds.width, innerBounds.height
        )
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
    protected fun copyFromResource(
        prefix: String,
        fileName: String,
        destinationDirectory: String
    ) {
        // adapted from http://javaalmanac.com/egs/java.io/CopyFile.html
        // Copies src file to dst file.
        // If the dst file does not exist, it is created
        try {
            logger?.finest("searching for $prefix$fileName")
            val resource = getResource(prefix + fileName)
            val `in` = resource.openStream()
            val out: OutputStream = FileOutputStream(
                destinationDirectory + "/" +
                    fileName
            )

            // Transfer bytes from in to out
            Tools.copyStream(`in`, out, true)
        } catch (e: Exception) {
            logger?.severe(
                "File not found or could not be copied. " +
                    "Was earching for " + prefix + fileName +
                    " and should go to " + destinationDirectory
            )
            Resources.getInstance().logException(e)
        }
    }

    /**
     */
    protected fun copyFromFile(
        dir: String,
        fileName: String,
        destinationDirectory: String
    ) {
        // adapted from http://javaalmanac.com/egs/java.io/CopyFile.html
        // Copies src file to dst file.
        // If the dst file does not exist, it is created
        try {
            logger?.finest("searching for $dir$fileName")
            val resource = File(dir, fileName)
            val `in`: InputStream = FileInputStream(resource)
            val out: OutputStream = FileOutputStream(
                destinationDirectory + "/" +
                    fileName
            )

            // Transfer bytes from in to out
            Tools.copyStream(`in`, out, true)
        } catch (e: Exception) {
            logger?.severe(
                "File not found or could not be copied. " +
                    "Was earching for " + dir + fileName +
                    " and should go to " + destinationDirectory
            )
            Resources.getInstance().logException(e)
        }
    }

    companion object {
        @JvmStatic
        fun chooseImageFile(
            type: String,
            description: String?,
            nameExtension: String?,
            controller: ModeController?
        ): File? {
            val component = controller?.frame?.contentPane
            val filter = ImageFilter(type, description)
            var chooser: FreeMindFileDialog?
            chooser = controller?.getFileChooser(filter)
            val mmFile = controller?.map?.file
            if (mmFile != null) {
                val proposedName = (
                    mmFile.absolutePath.replaceFirst(
                        "\\.[^.]*?$".toRegex(), ""
                    ) +
                        (nameExtension ?: "") +
                        "." +
                        type
                    )
                chooser?.selectedFile = File(proposedName)
            }
            val returnVal = chooser?.showSaveDialog(component)
            if (returnVal != JFileChooser.APPROVE_OPTION) { // not ok pressed
                return null
            }

            // |= Pressed O.K.
            var chosenFile = chooser?.selectedFile
            val ext = Tools.getExtension(chosenFile?.name)
            if (!Tools.safeEqualsIgnoreCase(ext, type)) {
                chosenFile = File(
                    chosenFile?.parent,
                    chosenFile?.name +
                        "." + type
                )
            }
            if (chosenFile?.exists() ?: false) { // If file exists, ask before overwriting.
                val overwriteText = MessageFormat.format(
                    controller
                        ?.getText("file_already_exists"),
                    *arrayOf<Any>(
                        chosenFile
                            .toString()
                    )
                )
                val overwriteMap = JOptionPane.showConfirmDialog(
                    component,
                    overwriteText, overwriteText, JOptionPane.YES_NO_OPTION
                )
                if (overwriteMap != JOptionPane.YES_OPTION) {
                    return null
                }
            }
            return chosenFile
        }
    }
}
