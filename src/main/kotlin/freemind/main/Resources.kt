/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
/*
 * Created on 12.07.2005
 * Copyright (C) 2005-2008 Dimitri Polivaev, Christian Foltin
 */
package freemind.main

import freemind.common.NamedObject
import freemind.common.TextTranslator
import freemind.main.FreeMindMain.VersionInformation
import freemind.modes.FreeMindAwtFileDialog
import freemind.modes.FreeMindFileDialog
import freemind.modes.FreeMindJFileDialog
import tests.freemind.FreeMindMainMock
import java.io.File
import java.net.URL
import java.text.MessageFormat
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import javax.swing.filechooser.FileFilter

/**
 * @author Dimitri Polivaev 12.07.2005
 */
class Resources private constructor(private val main: FreeMindMain) : TextTranslator {
    private var countryMap: HashMap<String, String>? = null
    private var logger: Logger? = null

    init {
        if (logger == null) {
            logger = main.getLogger(this.javaClass.name)
        }
    }

    fun getResource(resource: String): URL? {
        return main.getResource(resource)
    }

    fun getResourceString(resource: String?): String? {
        return main.getResourceString(resource)
    }

    fun getResourceString(key: String?, resource: String?): String? {
        return main.getResourceString(key, resource)
    }

    val freemindDirectory: String?
        get() = main.freemindDirectory
    val freemindBaseDir: String?
        get() = main.freemindBaseDir
    val freemindVersion: VersionInformation?
        get() = main.freemindVersion
    val freeMindClassLoader: ClassLoader?
        get() = main.freeMindClassLoader

    fun getIntProperty(key: String?, defaultValue: Int): Int {
        return main.getIntProperty(key, defaultValue)
    }

    fun getLongProperty(key: String?, defaultValue: Long): Long {
        return try {
            getProperty(key)!!.toLong()
        } catch (nfe: NumberFormatException) {
            defaultValue
        }
    }

    /**
     * @param key
     * Property key
     * @return the boolean value of the property resp. the default.
     */
    fun getBoolProperty(key: String?): Boolean {
        val boolProperty = getProperty(key)
        return Tools.safeEquals("true", boolProperty)
    }

    val properties: Properties?
        get() = main.properties

    fun getProperty(key: String?): String? {
        return main.getProperty(key)
    }

    val resources: ResourceBundle?
        get() = main.resources

    fun getCountryMap(): HashMap<String, String> {
        if (countryMap == null) {
            val countryMapArray = arrayOf("de", "DE", "en", "UK",
                    "en", "US", "es", "ES", "es", "MX", "fi", "FI", "fr", "FR",
                    "hu", "HU", "it", "CH", "it", "IT", "nl", "NL", "no", "NO",
                    "pt", "PT", "ru", "RU", "sl", "SI", "uk", "UA", "zh", "CN")
            countryMap = HashMap()
            var i = 0
            while (i < countryMapArray.size) {
                countryMap!![countryMapArray[i]] = countryMapArray[i + 1]
                i = i + 2
            }
        }
        return countryMap!!
    }

    /* To obtain a logging element, ask here. */
    fun getLogger(forClass: String?): Logger? {
        return main.getLogger(forClass)
    }

    @JvmOverloads
    fun logException(e: Throwable?, comment: String = "") {
        logger!!.log(Level.SEVERE, "An exception occured: $comment", e)
    }

    fun format(resourceKey: String?, messageArguments: Array<Any?>?): String {
        val formatter = MessageFormat(getResourceString(resourceKey))
        return formatter.format(messageArguments)
    }

    fun createTranslatedString(key: String?): NamedObject {
        val fs = getResourceString(key)
        return NamedObject(key, fs)
    }

    override fun getText(pKey: String?): String {
        return getResourceString(pKey)!!
    }

    fun getStandardFileChooser(filter: FileFilter?): FreeMindFileDialog {
        val chooser: FreeMindFileDialog
        chooser = if (!Tools.isMacOsX) {
            FreeMindJFileDialog()
        } else {
            // only for mac
            FreeMindAwtFileDialog()
        }
        if (filter != null) {
            chooser.addChoosableFileFilterAsDefault(filter)
        }
        return chooser
    }

    /**
     * @param baseFileName
     * @return
     */
    fun createThumbnailFileName(baseFileName: File): String {
        return (baseFileName.parent
                + File.separatorChar
                + "." // hidden
                + baseFileName.name.replaceFirst((
                FreeMindCommon.Companion.FREEMIND_FILE_EXTENSION + "$").toRegex(),
                ".png"))
    }

    companion object {
        var resourcesInstance: Resources? = null
        fun createInstance(frame: FreeMindMain) {
            if (resourcesInstance == null) {
                resourcesInstance = Resources(frame)
            }
        }

        val instance: Resources?
            get() {
                if (resourcesInstance == null) {
                    createInstance(FreeMindMainMock())
                    System.err.println("Resources without FreeMind called.")
                }
                return resourcesInstance
            }
    }
}