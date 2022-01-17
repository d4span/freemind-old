/*
 * FreeMind - A Program for creating and viewing MindmapsCopyright (C) 2000-2015
 * Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and
 * others.
 *
 * See COPYING for Details
 *
 * This program is free software; you can redistribute it and/ormodify it under
 * the terms of the GNU General Public Licenseas published by the Free Software
 * Foundation; either version 2of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty ofMERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See theGNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public Licensealong with
 * this program; if not, write to the Free SoftwareFoundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package freemind.view

import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.net.URL
import javax.swing.ImageIcon

class ScalableImageIcon(pURL: URL?) : ImageIcon(pURL) {
    var scale = 2.0f
    private var mScaledImage: Image? = null
    override fun getIconHeight(): Int {
        return (super.getIconHeight() * scale).toInt()
    }

    override fun getIconWidth(): Int {
        return (super.getIconWidth() * scale).toInt()
    }

    @Synchronized
    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        var observer = imageObserver
        if (observer == null) {
            observer = c
        }
        val image = super.getImage()
        val width = image.getWidth(observer)
        val height = image.getHeight(observer)
        val g2d = g.create(x, y, (width * scale).toInt(), (height * scale).toInt()) as Graphics2D
        g2d.scale(scale.toDouble(), scale.toDouble())
        g2d.drawImage(image, 0, 0, observer)
        g2d.scale(1.0, 1.0)
        g2d.dispose()
    }

    override fun getImage(): Image? {
        if (mScaledImage != null) {
            return mScaledImage
        }
        mScaledImage = super.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)
        return mScaledImage
    }

    val unscaledIcon: ImageIcon
        get() {
            val image = super.getImage()
            return ImageIcon(image)
        }

    companion object {
        private const val serialVersionUID = 1110980781217268145L
    }
}
