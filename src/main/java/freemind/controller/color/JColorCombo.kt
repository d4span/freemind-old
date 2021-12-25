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
package freemind.controller.color

import freemind.main.Tools
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer
import javax.swing.WindowConstants

class JColorCombo : JComboBox<ColorPair?>() {
    class ColorIcon(pColor: Color?) : ImageIcon(BufferedImage(ICON_SIZE, ICON_SIZE, BufferedImage.TYPE_INT_RGB)) {
        private val mImage: BufferedImage

        init {
            mImage = image as BufferedImage
            val g = mImage.graphics
            g.color = pColor
            g.fillRect(0, 0, ICON_SIZE, ICON_SIZE)
            g.dispose()
        }

        companion object {
            private val ICON_SIZE = (Tools.getScalingFactor() * 16).toInt()
        }
    }

    /** See [MindMapToolBar]  */
    override fun getMaximumSize(): Dimension {
        return preferredSize
    }

    inner class ComboBoxRenderer : JLabel(), ListCellRenderer<ColorPair?> {
        init {
            isOpaque = true
            horizontalAlignment = LEFT
            verticalAlignment = CENTER
        }

        /*
	     * This method finds the image and text corresponding
	     * to the selected value and returns the label, set up
	     * to display the text and image.
	     */
        override fun getListCellRendererComponent(
            list: JList<out ColorPair?>, value: ColorPair?, index: Int,
            isSelected: Boolean, cellHasFocus: Boolean
        ): Component {
            if (isSelected) {
                background = list.selectionBackground
                foreground = list.selectionForeground
            } else {
                background = list.background
                foreground = list.foreground
            }
            val pair = value
            val icon: ImageIcon = ColorIcon(pair?.color)
            setIcon(icon)
            text = pair?.displayName
            return this
        }
    }

    init {
        val colorList = sColorList
        for (i in colorList.indices) {
            val colorPair = colorList[i]
            addItem(colorPair)
        }
        val renderer: ListCellRenderer<ColorPair?> = ComboBoxRenderer()
        setRenderer(renderer)
        setMaximumRowCount(20)
    }

    companion object {
        @JvmStatic
        fun main(s: Array<String>) {
            val frame = JFrame("JColorChooser")
            val colorChooser = JColorCombo()
            frame.contentPane.add(colorChooser)
            frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            frame.pack()
            frame.isVisible = true
        }

        var sColorList = arrayOf( // taken from http://wiki.selfhtml.org/wiki/Grafik/Farbpaletten#Farbnamen
            // default 16bit colors
            ColorPair(Color(0x000000), "black"),
            ColorPair(Color(0x808080), "gray"),
            ColorPair(Color(0x800000), "maroon"),
            ColorPair(Color(0xFF0000), "red"),
            ColorPair(Color(0x008000), "green"),
            ColorPair(Color(0x00FF00), "lime"),
            ColorPair(Color(0x808000), "olive"),
            ColorPair(Color(0xFFFF00), "yellow"),
            ColorPair(Color(0x000080), "navy"),
            ColorPair(Color(0x0000FF), "blue"),
            ColorPair(Color(0x800080), "purple"),
            ColorPair(Color(0xFF00FF), "fuchsia"),
            ColorPair(Color(0x008080), "teal"),
            ColorPair(Color(0x00FFFF), "aqua"),
            ColorPair(Color(0xC0C0C0), "silver"),
            ColorPair(Color(0xFFFFFF), "white"),  // automatic layout colors:
            ColorPair(Color(0x0033ff), "level1"),
            ColorPair(Color(0x00b439), "level2"),
            ColorPair(Color(0x990000), "level3"),
            ColorPair(Color(0x111111), "level4"),  // netscape colors
            ColorPair(Color(0xFFC0CB), "pink"),
            ColorPair(Color(0xFFB6C1), "lightpink"),
            ColorPair(Color(0xFF69B4), "hotpink"),
            ColorPair(Color(0xFF1493), "deeppink"),
            ColorPair(Color(0xDB7093), "palevioletred"),
            ColorPair(Color(0xC71585), "mediumvioletred"),
            ColorPair(Color(0xFFA07A), "lightsalmon"),
            ColorPair(Color(0xFA8072), "salmon"),
            ColorPair(Color(0xE9967A), "darksalmon"),
            ColorPair(Color(0xF08080), "lightcoral"),
            ColorPair(Color(0xCD5C5C), "indianred"),
            ColorPair(Color(0xDC143C), "crimson"),
            ColorPair(Color(0xB22222), "firebrick"),
            ColorPair(Color(0x8B0000), "darkred"),
            ColorPair(Color(0xFF0000), "red"),
            ColorPair(Color(0xFF4500), "orangered"),
            ColorPair(Color(0xFF6347), "tomato"),
            ColorPair(Color(0xFF7F50), "coral"),
            ColorPair(Color(0xFF8C00), "darkorange"),
            ColorPair(Color(0xFFA500), "orange"),
            ColorPair(Color(0xFFFF00), "yellow"),
            ColorPair(Color(0xFFFFE0), "lightyellow"),
            ColorPair(Color(0xFFFACD), "lemonchiffon"),
            ColorPair(Color(0xFFEFD5), "papayawhip"),
            ColorPair(Color(0xFFE4B5), "moccasin"),
            ColorPair(Color(0xFFDAB9), "peachpuff"),
            ColorPair(Color(0xEEE8AA), "palegoldenrod"),
            ColorPair(Color(0xF0E68C), "khaki"),
            ColorPair(Color(0xBDB76B), "darkkhaki"),
            ColorPair(Color(0xFFD700), "gold"),
            ColorPair(Color(0xFFF8DC), "cornsilk"),
            ColorPair(Color(0xFFEBCD), "blanchedalmond"),
            ColorPair(Color(0xFFE4C4), "bisque"),
            ColorPair(Color(0xFFDEAD), "navajowhite"),
            ColorPair(Color(0xF5DEB3), "wheat"),
            ColorPair(Color(0xDEB887), "burlywood"),
            ColorPair(Color(0xD2B48C), "tan"),
            ColorPair(Color(0xBC8F8F), "rosybrown"),
            ColorPair(Color(0xF4A460), "sandybrown"),
            ColorPair(Color(0xDAA520), "goldenrod"),
            ColorPair(Color(0xB8860B), "darkgoldenrod"),
            ColorPair(Color(0xCD853F), "peru"),
            ColorPair(Color(0xD2691E), "chocolate"),
            ColorPair(Color(0x8B4513), "saddlebrown"),
            ColorPair(Color(0xA0522D), "sienna"),
            ColorPair(Color(0xA52A2A), "brown"),
            ColorPair(Color(0x800000), "maroon"),
            ColorPair(Color(0x556B2F), "darkolivegreen"),
            ColorPair(Color(0x808000), "olive"),
            ColorPair(Color(0x6B8E23), "olivedrab"),
            ColorPair(Color(0x9ACD32), "yellowgreen"),
            ColorPair(Color(0x32CD32), "limegreen"),
            ColorPair(Color(0x00FF00), "lime"),
            ColorPair(Color(0x7CFC00), "lawngreen"),
            ColorPair(Color(0x7FFF00), "chartreuse"),
            ColorPair(Color(0xADFF2F), "greenyellow"),
            ColorPair(Color(0x00FF7F), "springgreen"),
            ColorPair(Color(0x00FA9A), "mediumspringgreen"),
            ColorPair(Color(0x90EE90), "lightgreen"),
            ColorPair(Color(0x98FB98), "palegreen"),
            ColorPair(Color(0x8FBC8F), "darkseagreen"),
            ColorPair(Color(0x3CB371), "mediumseagreen"),
            ColorPair(Color(0x2E8B57), "seagreen"),
            ColorPair(Color(0x228B22), "forestgreen"),
            ColorPair(Color(0x008000), "green"),
            ColorPair(Color(0x006400), "darkgreen"),
            ColorPair(Color(0x66CDAA), "mediumaquamarine"),
            ColorPair(Color(0x00FFFF), "aqua"),
            ColorPair(Color(0x00FFFF), "cyan"),
            ColorPair(Color(0xE0FFFF), "lightcyan"),
            ColorPair(Color(0xAFEEEE), "paleturquoise"),
            ColorPair(Color(0x7FFFD4), "aquamarine"),
            ColorPair(Color(0x40E0D0), "turquoise"),
            ColorPair(Color(0x48D1CC), "mediumturquoise"),
            ColorPair(Color(0x00CED1), "darkturquoise"),
            ColorPair(Color(0x20B2AA), "lightseagreen"),
            ColorPair(Color(0x5F9EA0), "cadetblue"),
            ColorPair(Color(0x008B8B), "darkcyan"),
            ColorPair(Color(0x008080), "teal"),
            ColorPair(Color(0xB0C4DE), "lightsteelblue"),
            ColorPair(Color(0xB0E0E6), "powderblue"),
            ColorPair(Color(0xADD8E6), "lightblue"),
            ColorPair(Color(0x87CEEB), "skyblue"),
            ColorPair(Color(0x87CEFA), "lightskyblue"),
            ColorPair(Color(0x00BFFF), "deepskyblue"),
            ColorPair(Color(0x1E90FF), "dodgerblue"),
            ColorPair(Color(0x6495ED), "cornflowerblue"),
            ColorPair(Color(0x4682B4), "steelblue"),
            ColorPair(Color(0x4169E1), "royalblue"),
            ColorPair(Color(0x0000FF), "blue"),
            ColorPair(Color(0x0000CD), "mediumblue"),
            ColorPair(Color(0x00008B), "darkblue"),
            ColorPair(Color(0x000080), "navy"),
            ColorPair(Color(0x191970), "midnightblue"),
            ColorPair(Color(0xE6E6FA), "lavender"),
            ColorPair(Color(0xD8BFD8), "thistle"),
            ColorPair(Color(0xDDA0DD), "plum"),
            ColorPair(Color(0xEE82EE), "violet"),
            ColorPair(Color(0xDA70D6), "orchid"),
            ColorPair(Color(0xFF00FF), "fuchsia"),
            ColorPair(Color(0xFF00FF), "magenta"),
            ColorPair(Color(0xBA55D3), "mediumorchid"),
            ColorPair(Color(0x9370DB), "mediumpurple"),
            ColorPair(Color(0x8A2BE2), "blueviolet"),
            ColorPair(Color(0x9400D3), "darkviolet"),
            ColorPair(Color(0x9932CC), "darkorchid"),
            ColorPair(Color(0x8B008B), "darkmagenta"),
            ColorPair(Color(0x800080), "purple"),
            ColorPair(Color(0x4B0082), "indigo"),
            ColorPair(Color(0x483D8B), "darkslateblue"),
            ColorPair(Color(0x6A5ACD), "slateblue"),
            ColorPair(Color(0x7B68EE), "mediumslateblue"),
            ColorPair(Color(0xFFFFFF), "white"),
            ColorPair(Color(0xFFFAFA), "snow"),
            ColorPair(Color(0xF0FFF0), "honeydew"),
            ColorPair(Color(0xF5FFFA), "mintcream"),
            ColorPair(Color(0xF0FFFF), "azure"),
            ColorPair(Color(0xF0F8FF), "aliceblue"),
            ColorPair(Color(0xF8F8FF), "ghostwhite"),
            ColorPair(Color(0xF5F5F5), "whitesmoke"),
            ColorPair(Color(0xFFF5EE), "seashell"),
            ColorPair(Color(0xF5F5DC), "beige"),
            ColorPair(Color(0xFDF5E6), "oldlace"),
            ColorPair(Color(0xFFFAF0), "floralwhite"),
            ColorPair(Color(0xFFFFF0), "ivory"),
            ColorPair(Color(0xFAEBD7), "antiquewhite"),
            ColorPair(Color(0xFAF0E6), "linen"),
            ColorPair(Color(0xFFF0F5), "lavenderblush"),
            ColorPair(Color(0xFFE4E1), "mistyrose"),
            ColorPair(Color(0xDCDCDC), "gainsboro"),
            ColorPair(Color(0xD3D3D3), "lightgray"),
            ColorPair(Color(0xC0C0C0), "silver"),
            ColorPair(Color(0xA9A9A9), "darkgray"),
            ColorPair(Color(0x808080), "gray"),
            ColorPair(Color(0x696969), "dimgray"),
            ColorPair(Color(0x778899), "lightslategray"),
            ColorPair(Color(0x708090), "slategray"),
            ColorPair(Color(0x2F4F4F), "darkslategray"),
            ColorPair(Color(0x000000), "black")
        )
    }
}