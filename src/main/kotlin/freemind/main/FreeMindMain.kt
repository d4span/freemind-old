/*FreeMind - A Program for creating and viewing Mindmaps
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
/*$Id: FreeMindMain.java,v 1.12.14.5.2.12 2008/07/17 19:16:33 christianfoltin Exp $*/
package freemind.main

import freemind.controller.Controller
import freemind.controller.MenuBar
import freemind.view.mindmapview.MapView
import java.awt.Container
import java.io.File
import java.net.URL
import java.util.*
import java.util.logging.Logger
import javax.swing.*

interface FreeMindMain {
    interface StartupDoneListener {
        fun startupDone()
    }

    val jFrame: JFrame
    val isApplet: Boolean
    var view: MapView?
    val controller: Controller?
    fun setWaitingCursor(waiting: Boolean)
    val patternsFile: File?
    val freeMindMenuBar: MenuBar?

    /** Returns the ResourceBundle with the current language  */
    val resources: ResourceBundle?
    fun getResourceString(key: String?): String?
    fun getResourceString(key: String?, defaultResource: String?): String?
    val contentPane: Container?
    fun out(msg: String?)
    fun err(msg: String)

    /**
     * Open url in WWW browser. This method hides some differences between
     * operating systems.
     */
    @Throws(Exception::class)
    fun openDocument(location: URL?)

    /** remove this!  */
    fun repaint()
    fun getResource(name: String?): URL?
    fun getIntProperty(key: String?, defaultValue: Int): Int

    /** @return returns the list of all properties.
     */
    val properties: Properties?

    /**
     * Properties are stored in freemind.properties (internally) and
     * ~/.freemind/auto.properties for user changed values. This method returns
     * the user value (if changed) or the original.
     *
     * @param key
     * The property key as specified in freemind.properties
     * @return the value of the property or null, if not found.
     */
    fun getProperty(key: String?): String?
    fun setProperty(key: String?, value: String?)
    fun saveProperties(pIsShutdown: Boolean)

    /**
     * Returns the path to the directory the freemind auto properties are in, or
     * null, if not present.
     */
    val freemindDirectory: String?
    val layeredPane: JLayeredPane?
    fun setTitle(title: String?)

    // to keep last win size (PN)
    val winHeight: Int
    val winWidth: Int
    val winState: Int
    val winX: Int
    val winY: Int

    class VersionInformation {
        var mMaj = 0
        var mMid = 9
        var mMin = 0
        var mType = VERSION_TYPE_BETA
        var mNum = 17

        constructor(pMaj: Int, pMid: Int, pMin: Int, pType: Int,
                    pNum: Int) : super() {
            mMaj = pMaj
            mMid = pMid
            mMin = pMin
            mType = pType
            mNum = pNum
        }

        /**
         * Sets the version number from a string.
         *
         * @param pString
         * : The version number coding. Example "0.9.0 Beta 1"
         * Keywords are "Alpha", "Beta", "RC". Separation by " " or
         * by ".".
         */
        constructor(pString: String) {
            val t = StringTokenizer(pString, ". ", false)
            val info = arrayOfNulls<String>(t.countTokens())
            var i = 0
            while (t.hasMoreTokens()) {
                info[i++] = t.nextToken()
            }
            require(!(info.size != 3 && info.size != 5)) {
                ("Wrong number of tokens for version information: "
                        + pString)
            }
            mMaj = info[0]!!.toInt()
            mMid = info[1]!!.toInt()
            mMin = info[2]!!.toInt()
            if (info.size == 3) {
                // release.
                mType = VERSION_TYPE_RELEASE
                mNum = 0
                return
            }
            // here,we have info.length == 5!
            val types = Vector<String?>()
            types.add("Alpha")
            types.add("Beta")
            types.add("RC")
            val typeIndex = types.indexOf(info[3])
            require(typeIndex >= 0) {
                ("Wrong version type for version information: "
                        + info[4])
            }
            mType = typeIndex
            mNum = info[4]!!.toInt()
        }

        override fun toString(): String {
            val buf = StringBuffer()
            buf.append(mMaj)
            buf.append('.')
            buf.append(mMid)
            buf.append('.')
            buf.append(mMin)
            when (mType) {
                VERSION_TYPE_ALPHA -> {
                    buf.append(' ')
                    buf.append("Alpha")
                }
                VERSION_TYPE_BETA -> {
                    buf.append(' ')
                    buf.append("Beta")
                }
                VERSION_TYPE_RC -> {
                    buf.append(' ')
                    buf.append("RC")
                }
                VERSION_TYPE_RELEASE -> {}
                else -> throw IllegalArgumentException("Unknown version type "
                        + mType)
            }
            if (mType != VERSION_TYPE_RELEASE) {
                buf.append(' ')
                buf.append(mNum)
            }
            return buf.toString()
        }
    }

    /** version info:  */
    val freemindVersion: VersionInformation

    /** To obtain a logging element, ask here.  */
    fun getLogger(forClass: String?): Logger

    /**
     * Inserts a (south) component into the split pane. If the screen isn't
     * split yet, a split pane should be created on the fly.
     * @param pMindMapComponent
     *
     * @return the split pane in order to move the dividers.
     */
    fun insertComponentIntoSplitPane(pMindMapComponent: JComponent?): JSplitPane?

    /**
     * Indicates that the south panel should be made invisible.
     */
    fun removeSplitPane()

    /**
     * @return a ClassLoader derived from the standard, with freeminds base dir
     * included.
     */
    val freeMindClassLoader: ClassLoader?

    /**
     * @return default ".", but on different os this differs.
     */
    val freemindBaseDir: String?

    /**
     * Makes it possible to have a property different for different
     * localizations. Common example is to put keystrokes to different keys as
     * some are better reachable than others depending on the locale.
     */
    fun getAdjustableProperty(label: String): String?
    fun setDefaultProperty(key: String?, value: String?)
    val contentComponent: JComponent?
    val scrollPane: JScrollPane?
    fun registerStartupDoneListener(
            pStartupDoneListener: StartupDoneListener)

    /**
     * @return a list of all loggers. Used for example for the log file viewer.
     */
    val loggerList: List<Logger>

    companion object {
        const val VERSION_TYPE_ALPHA = 0
        const val VERSION_TYPE_BETA = 1
        const val VERSION_TYPE_RC = 2
        const val VERSION_TYPE_RELEASE = 3
        const val ENABLE_NODE_MOVEMENT = "enable_node_movement"
    }
}