/*
 *  Preview Dialog - A Preview Dialog for your Swing Applications
 *
 *  Copyright (C) 2003 Jens Kaiser.
 *
 *  Written by: 2003 Jens Kaiser <jens.kaiser@web.de>
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package freemind.controller.printpreview

import freemind.view.mindmapview.MapView
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.print.PageFormat
import java.awt.print.Printable
import javax.swing.JComponent

internal class Preview(protected var view: MapView, zoom: Double, private val pageFormat: PageFormat) : JComponent() {
    private var previewPageImage: BufferedImage? = null
    private var imageGraphics: Graphics2D? = null
    protected fun paintPaper(g: Graphics, format: PageFormat) {
        g.color = Color.white
        g.fillRect(0, 0, getPageWidth(format), getPageHeight(format))
        g.color = Color.black
        g.drawRect(0, 0, getPageWidth(format) - 1, getPageHeight(format) - 1)
    }

    override fun paint(g: Graphics) {
        val g2d = g as Graphics2D
        val format = pageFormat
        paintPaper(g, format)
        if (previewPageImage == null) {
            previewPageImage = createImage(
                getPageWidth(format) - 1, getPageHeight(format) - 1
            ) as BufferedImage
            val imageGraphics = previewPageImage!!.createGraphics()
            imageGraphics?.scale(zoom, zoom)
            while (Printable.NO_SUCH_PAGE == view.print(
                    imageGraphics, format,
                    pageIndex
                ) && pageIndex > 0
            ) {
                pageIndex -= 1
            }
        }
        g2d.drawImage(previewPageImage, 0, 0, this)
    }

    private fun getPageHeight(format: PageFormat): Int {
        return (format.height * zoom).toInt()
    }

    private fun getPageWidth(format: PageFormat): Int {
        return (format.width * zoom).toInt()
    }

    fun moveIndex(indexStep: Int) {
        val newIndex = pageIndex + indexStep
        if (newIndex >= 0) {
            pageIndex = newIndex
            previewPageImage = null
        }
    }

    fun changeZoom(zoom: Double) {
        this.zoom = Math.max(MINIMUM_ZOOM_FACTOR, this.zoom + zoom)
        resize()
    }

    fun resize() {
        val size = Math.max(
            pageFormat.width * zoom,
            pageFormat.height * zoom
        ).toInt()
        preferredSize = Dimension(size, size)
        previewPageImage = null
        revalidate()
    }

    override fun getMinimumSize(): Dimension {
        return preferredSize
    }

    var pageIndex = 0
        protected set
    protected var zoom = 0.0

    init {
        val format = pageFormat
        if (zoom == 0.0) {
            if (format.orientation == PageFormat.PORTRAIT) this.zoom =
                DEFAULT_PREVIEW_SIZE / format.height else this.zoom = DEFAULT_PREVIEW_SIZE / format.width
        } else this.zoom = zoom
        resize()
    }

    companion object {
        private const val DEFAULT_PREVIEW_SIZE = 300
        private const val MINIMUM_ZOOM_FACTOR = 0.1
    }
}
