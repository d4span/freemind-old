/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2015 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
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
package freemind.view

import freemind.main.Resources
import freemind.main.Tools
import java.io.IOException
import java.net.URL
import javax.swing.ImageIcon

/**
 * @author foltin
 * @date 24.05.2015
 */
class ImageFactory {
    fun createIcon(pUrl: URL?): ImageIcon {
        if (Tools.getScalingFactorPlain() == 100) {
            return createUnscaledIcon(pUrl)
        }
        val icon = ScalableImageIcon(pUrl)
        icon.scale = Tools.getScalingFactor()
        return icon
    }

    /**
     * All icons directly displayed in the mindmap view are scaled by the zoom.
     */
    fun createUnscaledIcon(pResource: URL?): ImageIcon {
        return ImageIcon(pResource)
    }

    /**
     * @param pString
     * @return
     */
    fun createIcon(pFilePath: String): ImageIcon {
        if (Tools.getScalingFactorPlain() == 200) {
            // test for existence  of a scaled icon:
            if (pFilePath.endsWith(".png")) {
                try {
                    val url = Resources.getInstance().getResource(pFilePath.replace(".png$".toRegex(), "_32.png"))
                    val connection = url.openConnection()
                    if (connection.contentLength > 0) {
                        return createUnscaledIcon(url)
                    }
                } catch (e: IOException) {
                    Resources.getInstance().logException(e)
                }
            }
        }
        return createIcon(Resources.getInstance().getResource(pFilePath))
    }

    companion object {
        private var mInstance: ImageFactory? = null
        @JvmStatic
        val instance: ImageFactory?
            get() {
                if (mInstance == null) {
                    mInstance = ImageFactory()
                }
                return mInstance
            }
    }
}
