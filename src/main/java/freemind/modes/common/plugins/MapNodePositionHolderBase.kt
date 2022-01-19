/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2012 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package freemind.modes.common.plugins

import freemind.extensions.PermanentNodeHookAdapter
import freemind.main.Resources
import freemind.main.Tools
import freemind.main.XMLElement
import freemind.modes.MindMapNode
import freemind.view.ImageFactory.Companion.instance
import java.io.File
import javax.swing.ImageIcon

/**
 * This base class is free of openstreetmap and similar classes.
 * Thus, it doesn't know much about its position.
 *
 * @author foltin
 * @date 16.08.2012
 */
open class MapNodePositionHolderBase :
/**
     *
     */
        PermanentNodeHookAdapter() {
        @JvmField
        protected var mTooltipLocation: String? = null
        protected var mTooltipFile: File? = null
        private var mValues: HashMap<String, String>? = null
        protected fun setStateIcon(node: MindMapNode?, enabled: Boolean) {
            node!!.setStateIcon(
                NODE_MAP_LOCATION_ICON,
                if (enabled) mapLocationIcon else null
            )
        }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.extensions.PermanentNodeHookAdapter#shutdownMapHook()
	 */
        override fun shutdownMapHook() {
            setStateIcon(getNode(), false)
            hideTooltip()
            super.shutdownMapHook()
        }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.extensions.NodeHookAdapter#invoke(freemind.modes.MindMapNode)
	 */
        override fun invoke(node: MindMapNode?) {
            super.invoke(node)
            setStateIcon(node, true)
            showTooltip()
        }

        open fun showTooltip() {
            if (isTooltipDesired) {
                if (mTooltipLocation != null) {
                    setTooltip()
                }
            }
        }

        protected val isTooltipDesired: Boolean
            get() = Resources.getInstance().getBoolProperty(NODE_MAP_SHOW_TOOLTIP) &&
                !Tools.safeEquals(mTooltipLocation, "false")

        override fun loadFrom(child: XMLElement?) {
            super.loadFrom(child)
            mValues = loadNameValuePairs(child!!)
            // if no value stored, the get method returns null.
            mTooltipLocation = mValues!![XML_STORAGE_MAP_TOOLTIP_LOCATION]
        }

        fun setTooltip() {
            val imageHtml = imageHtml
            setToolTip(NODE_MAP_HOOK_NAME, imageHtml)
        }

        open val imageHtml: String
            get() {
                val imageTag = "<img src=\"file:./$mTooltipLocation\"/>"
                val imageHtml = "<html><body>$imageTag</body></html>"
                logger!!.fine("Tooltip at $imageTag")
                return imageHtml
            }

        /**
         *
         */
        protected fun hideTooltip() {
            setToolTip(NODE_MAP_HOOK_NAME, null)
        }

        val barePosition: Array<String?>
            get() = arrayOf(
                mValues!![XML_STORAGE_POS_LAT],
                mValues!![XML_STORAGE_POS_LON],
                mValues!![XML_STORAGE_MAP_LAT],
                mValues!![XML_STORAGE_MAP_LON],
                mValues!![XML_STORAGE_ZOOM],
                mValues!![XML_STORAGE_TILE_SOURCE]
            )

        companion object {
            const val NODE_MAP_HOOK_NAME = "plugins/map/MapNodePositionHolder.properties"
            const val NODE_MAP_LOCATION_ICON = "node_map_location_icon"
            protected const val XML_STORAGE_POS_LON = "XML_STORAGE_POS_LON"
            protected const val XML_STORAGE_POS_LAT = "XML_STORAGE_POS_LAT"
            protected const val XML_STORAGE_MAP_LON = "XML_STORAGE_MAP_LON"
            protected const val XML_STORAGE_MAP_LAT = "XML_STORAGE_MAP_LAT"
            protected const val XML_STORAGE_ZOOM = "XML_STORAGE_ZOOM"
            protected const val XML_STORAGE_TILE_SOURCE = "XML_STORAGE_TILE_SOURCE"
            protected const val XML_STORAGE_MAP_TOOLTIP_LOCATION = "XML_STORAGE_MAP_TOOLTIP_LOCATION"
            protected const val NODE_MAP_SHOW_TOOLTIP = "node_map_show_tooltip"
            const val TILE_SOURCE_MAP_QUEST_OPEN_MAP = "plugins.map.FreeMindMapController.MapQuestOpenMap"
            const val TILE_SOURCE_TRANSPORT_MAP = "plugins.map.FreeMindMapController.TransportMap"
            const val TILE_SOURCE_CYCLE_MAP = "org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource\$CycleMap"
            const val TILE_SOURCE_MAPNIK = "org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource\$Mapnik"
            const val SHORT_MAP_QUEST_OPEN_MAP = "Q"
            const val SHORT_TRANSPORT_MAP = "T"
            const val SHORT_CYCLE_MAP = "C"
            const val SHORT_MAPNIK = "M"
            var sMapLocationIcon: ImageIcon? = null

            // icon
            @JvmStatic
            val mapLocationIcon: ImageIcon?
                get() {
                    // icon
                    if (sMapLocationIcon == null) {
                        sMapLocationIcon = instance!!.createUnscaledIcon(
                            Resources.getInstance()
                                .getResource("images/map_location.png")
                        )
                    }
                    return sMapLocationIcon
                }

            @JvmStatic
            fun getBaseHook(node: MindMapNode): MapNodePositionHolderBase? {
                for (element in node.activatedHooks) {
                    if (element is MapNodePositionHolderBase) {
                        return element
                    }
                }
                return null
            }
        }
    }
