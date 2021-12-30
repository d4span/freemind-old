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
/*$Id: LastOpenedList.java,v 1.8.18.2.2.2 2008/04/11 16:58:31 christianfoltin Exp $*/
package freemind.controller

import freemind.main.Resources
import freemind.main.Tools
import freemind.main.XMLParseException
import freemind.view.MapModule
import java.io.FileNotFoundException
import java.io.IOException
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.util.LinkedList
import java.util.StringTokenizer

/**
 * This class manages a list of the maps that were opened last. It aims to
 * provide persistence for the last recent maps. Maps should be shown in the
 * format:"mode\:key",ie."Mindmap\:/home/joerg/freemind.mm"
 */
class LastOpenedList(private val mController: Controller, restored: String?) {
    private var maxEntries = 25 // is rewritten from property anyway

    /**
     * Contains Restore strings.
     */
    private val mlastOpenedList: MutableList<String> = LinkedList()

    /**
     * Contains Restore string => map name (map.toString()).
     */
    private val mRestorableToMapName: MutableMap<String?, String> = HashMap()

    init {
        try {
            maxEntries = mController.frame.getProperty(
                "last_opened_list_length"
            ).toInt()
        } catch (e: NumberFormatException) {
            Resources.getInstance().logException(e)
        }
        load(restored)
    }

    fun mapOpened(mapModule: MapModule?) {
        if (mapModule == null || mapModule.model == null) return
        val restoreString = mapModule.model.restorable
        val name = mapModule.toString()
        add(restoreString, name)
    }

    /**
     * For testing purposes, this method is public
     */
    fun add(restoreString: String?, name: String) {
        if (restoreString == null) return
        if (mlastOpenedList.contains(restoreString)) {
            mlastOpenedList.remove(restoreString)
        }
        mlastOpenedList.add(0, restoreString)
        mRestorableToMapName[restoreString] = name
        while (mlastOpenedList.size > maxEntries) {
            mlastOpenedList.removeAt(mlastOpenedList.size - 1) // remove last elt
        }
    }

    fun mapClosed() {
        // hash.remove(map.getModel().getRestoreable());
        // not needed
    }

    /** fc, 8.8.2004: This method returns a string representation of this class.  */
    fun save(): String {
        var str = String()
        val it = listIterator()
        while (it.hasNext()) {
            str = str + it.next() + ";"
        }
        return str
    }

    /**
     *
     */
    fun load(data: String?) {
        // Take care that there are no ";" in restorable names!
        if (data != null) {
            val token = StringTokenizer(data, ";")
            while (token.hasMoreTokens()) mlastOpenedList.add(token.nextToken())
        }
    }

    @Throws(
        FileNotFoundException::class,
        XMLParseException::class,
        MalformedURLException::class,
        IOException::class,
        URISyntaxException::class
    )
    fun open(restoreable: String?): Boolean {
        val changedToMapModule = mController.mapModuleManager
            ?.tryToChangeToMapModule(
                mRestorableToMapName[restoreable]
            )
        if (restoreable != null && (changedToMapModule ?: false)) {
            val mode = Tools.getModeFromRestorable(restoreable)
            val fileName = Tools.getFileNameFromRestorable(restoreable)
            if (mController.createNewMode(mode)) {
                mController.mode!!.restore(fileName)
                return true
            }
        }
        return false
    }

    fun listIterator(): ListIterator<String> {
        return mlastOpenedList.listIterator()
    }
}
