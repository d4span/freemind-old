/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package freemind.view.mindmapview

import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHookSubstituteUnknown
import freemind.main.Resources
import freemind.main.Tools
import freemind.main.Tools.FileReaderCreator
import freemind.modes.MapAdapter
import freemind.modes.MapFeedbackAdapter
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.modes.mindmapmode.MindMapMapModel
import java.awt.BorderLayout
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URISyntaxException
import javax.imageio.ImageIO
import javax.swing.JPanel

/**
 * @author foltin
 * @date 28.09.2011
 */
class IndependantMapViewCreator : MapFeedbackAdapter() {
    private var mMap: MindMapMapModel? = null
    @Throws(FileNotFoundException::class, IOException::class, URISyntaxException::class)
    fun createMapViewForFile(
        inputFileName: String?,
        parent: JPanel
    ): MapView {
        mMap = MindMapMapModel(this)
        val readerCreator = FileReaderCreator(File(inputFileName))
        val node = mMap!!.loadTree(readerCreator, MapAdapter.sDontAskInstance)
        mMap!!.setRoot(node)
        val mapView = MapView(mMap, this)
        parent.add(mapView, BorderLayout.CENTER)
        mapView.bounds = parent.bounds
        Tools.waitForEventQueue()
        mapView.addNotify()
        return mapView
    }

    @Throws(FileNotFoundException::class, IOException::class, URISyntaxException::class)
    fun exportFileToPng(
        inputFileName: String?,
        outputFileName: String?
    ) {
        val parent = JPanel()
        val bounds = Rectangle(0, 0, 400, 600)
        parent.bounds = bounds
        val mapView = createMapViewForFile(
            inputFileName, parent
        )
        // layout components:
        mapView.root?.getMainView()?.doLayout()
        parent.isOpaque = true
        parent.isDoubleBuffered = false // for better performance
        parent.doLayout()
        parent.validate() // this might not be necessary
        mapView.preparePrinting()
        parent.bounds = mapView.bounds
        printToFile(mapView, outputFileName, false, 0)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#getMap()
	 */
    override fun getMap(): MindMap {
        return mMap!!
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MapFeedback#createNodeHook(java.lang.String, freemind.modes.MindMapNode)
	 */
    override fun createNodeHook(pLoadName: String, pNode: MindMapNode): NodeHook {
        return PermanentNodeHookSubstituteUnknown(pLoadName)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty("java.awt.headless", "true")
            if (args.size != 2) {
                println("Export map to png.\nUsage:\n java -jar lib/freemind.jar freemind.view.mindmapview.IndependantMapViewCreator <map_path>.mm <picture_path>.png")
                System.exit(0)
            }
            val creator = IndependantMapViewCreator()
            try {
                val outputFileName = args[1]
                creator.exportFileToPng(args[0], outputFileName)
                println("Export to $outputFileName done.")
                System.exit(0)
            } catch (e: FileNotFoundException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            } catch (e: URISyntaxException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            }
            System.err.println("Error.")
            System.exit(1)
        }

        /**
         * @param parent
         * @param mapView
         * @param outputFileName
         * @param scale
         * @throws FileNotFoundException
         * @throws IOException
         */
        @JvmStatic
        @Throws(FileNotFoundException::class, IOException::class)
        fun printToFile(
            mapView: MapView,
            outputFileName: String?,
            scale: Boolean,
            destSize: Int
        ) {
            val parent = mapView.parent
            val dimI = mapView.innerBounds
            val dim = mapView.bounds
            // do print
            var backBuffer = BufferedImage(
                dimI.width, dimI.height,
                BufferedImage.TYPE_INT_ARGB
            )
            val g: Graphics = backBuffer.createGraphics()
            val newX = -dim.x - dimI.x
            val newY = -dim.y - dimI.y
            g.translate(newX, newY)
            g.clipRect(-newX, -newY, dimI.width, dimI.height)
            parent.print(g)
            g.dispose()
            if (scale) {
                val maxDim = Math.max(dimI.getHeight(), dimI.getWidth())
                val newWidth = (dimI.getWidth() * destSize / maxDim).toInt()
                val newHeight = (dimI.getHeight() * destSize / maxDim).toInt()
                val resized = BufferedImage(
                    newWidth, newHeight,
                    backBuffer.type
                )
                val g2 = resized.createGraphics()
                g2.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
                )
                g2.drawImage(
                    backBuffer, 0, 0, newWidth, newHeight, 0, 0,
                    backBuffer.width, backBuffer.height, null
                )
                g2.dispose()
                backBuffer = resized
            }
            val out1 = FileOutputStream(outputFileName)
            ImageIO.write(backBuffer, "png", out1)
            out1.close()
        }
    }
}
