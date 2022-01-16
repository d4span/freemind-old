/*FreeMindget - A Program for creating and viewing Mindmaps
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
/*$Id: MultipleImage.java,v 1.1.18.2 2006/03/14 21:56:28 christianfoltin Exp $*/
package freemind.view.mindmapview

import java.awt.Component
import java.awt.Graphics
import java.awt.Image
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.util.Vector
import javax.swing.ImageIcon

class MultipleImage(zoom: Double) : ImageIcon() {
    private val mImages = Vector<ImageIcon>()
    private var zoomFactor = 1.0
    private var isDirty: Boolean

    init {
        zoomFactor = zoom
        isDirty = true
    }

    val imageCount: Int
        get() = mImages.size

    fun addImage(image: ImageIcon) {
        mImages.add(image)
        setImage(image.image)
        isDirty = true
    }

    override fun getImage(): Image? {
        if (!isDirty) return super.getImage()
        val w = iconWidth
        val h = iconHeight
        if (w == 0 || h == 0) {
            return null
        }
        val outImage = BufferedImage(
            w, h,
            BufferedImage.TYPE_INT_ARGB
        )
        val g = outImage.createGraphics()
        for (currentIcon in mImages) {
            // py = /* center: */ ( myHeight -
            // (int)(currentIcon.getIconHeight()* zoomFactor)) /2;
            // int pheight = (int) (currentIcon.getIconHeight() * zoomFactor);
            val pwidth = currentIcon.iconWidth * zoomFactor
            val inttrans = AffineTransform.getScaleInstance(
                zoomFactor, zoomFactor
            )
            g.drawImage(currentIcon.image, inttrans, null)
            g.translate(pwidth, 0.0)
        }
        g.dispose()
        setImage(outImage)
        isDirty = false
        return super.getImage()
    }

    override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
        if (image != null) {
            super.paintIcon(c, g, x, y)
        }
    }

    // public void paintIcon(Component c,
    // Graphics g,
    // int x,
    // int y)
    // {
    // int myX = x;
    // int myHeight = getIconHeight();
    // for(int i = 0 ; i < mImages.size(); i++) {
    // ImageIcon currentIcon = ((ImageIcon) mImages.get(i));
    // int px,py,pwidth, pheight;
    // px = myX;
    // py = y /* center: */ + ( myHeight - (int)(currentIcon.getIconHeight()*
    // zoomFactor)) /2;
    // pwidth = (int) (currentIcon.getIconWidth() * zoomFactor);
    // pheight = (int) (currentIcon.getIconHeight() * zoomFactor);
    // /* code from ImageIcon.*/
    // if(currentIcon.getImageObserver() == null) {
    // g.drawImage(currentIcon.getImage(), px, py, pwidth, pheight, c);
    // } else {
    // g.drawImage(currentIcon.getImage(), px, py, pwidth, pheight,
    // currentIcon.getImageObserver());
    // }
    // /* end code*/
    // myX += pwidth;
    // }
    // };
    override fun getIconWidth(): Int {
        var myX = 0
        for (i in mImages.indices) {
            myX += (mImages[i] as ImageIcon).iconWidth
        }
        // System.out.println("width: "+myX);
        return (myX * zoomFactor).toInt()
    }

    override fun getIconHeight(): Int {
        var myY = 0
        for (i in mImages.indices) {
            val otherHeight = (mImages[i] as ImageIcon).iconHeight
            if (otherHeight > myY) myY = otherHeight
        }
        // System.out.println("height: "+myY);
        return (myY * zoomFactor).toInt()
    }
}
