/*
 *    ImportWizard.java
 *    Copyright (C) 1999 Len Trigg (trigg@cs.waikato.ac.nz)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
// adapted to freemind by christianfoltin, 29.2.2004.
// taken from /usr/share/xemacs/xemacs-packages/etc/jde/java/src/jde/wizards/ImportWizard.java
// changed: package name, commented out the static method.
// 			if (current.toLowerCase().endsWith(".properties")) {
// 											   // formerly ".class"
// and related changes.
// commented out: // For Java 2! ...
package freemind.extensions

import freemind.main.Resources
import java.io.File
import java.io.IOException
import java.util.Enumeration
import java.util.Locale
import java.util.StringTokenizer
import java.util.Vector
import java.util.logging.Logger
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Converts an unqualified class name to import statements by scanning through
 * the classpath.
 *
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version 1.0 - 6 May 1999
 */
class ImportWizard {
    @JvmField
    val lookFor = ".xml"

    /** Stores the list of all classes in the classpath  */
    @JvmField
    var CLASS_LIST = Vector<String>(500)

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
    }

    /** Build the list of classes  */ // static {
    //
    // // System.err.println("Making class list");
    // buildClassList();
    //
    // // System.err.println("Done (" + CLASS_LIST.size() + " classes)");
    //
    // }
    fun buildClassList() {
        var classPath = System.getProperty("java.class.path")
        val classPathSeparator = File.pathSeparator
        // add the current dir to find more plugins
        classPath = (
            Resources.getInstance().freemindBaseDir + classPathSeparator +
                classPath
            )
        logger!!.info("Classpath for plugins:$classPath")
        // to remove duplicates
        val foundPlugins = HashSet<String>()
        val st = StringTokenizer(classPath, classPathSeparator)
        while (st.hasMoreTokens()) {
            val classPathEntry = st.nextToken()
            val classPathFile = File(classPathEntry)
            try {
                val key = classPathFile.canonicalPath
                if (foundPlugins.contains(key)) continue
                logger!!.fine("looking for plugins in $key")
                foundPlugins.add(key)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            }
            if (classPathFile.exists()) {
                val lowerCaseFileName = classPathEntry.lowercase(Locale.getDefault())
                if (lowerCaseFileName.endsWith(".jar")) {
                    logger!!.fine("searching for plugins in: $classPathEntry")
                    addClassesFromZip(CLASS_LIST, classPathFile)
                } else if (lowerCaseFileName.endsWith(".zip")) {
                    logger!!.fine("searching for plugins in: $classPathEntry")
                    addClassesFromZip(CLASS_LIST, classPathFile)
                } else if (classPathFile.isDirectory) {
                    logger!!.fine("searching for plugins in: $classPathEntry")
                    addClassesFromDir(
                        CLASS_LIST, classPathFile, classPathFile,
                        0
                    )
                }
            }
        }
    }

    /**
     * Adds the classes from the supplied Zip file to the class list.
     *
     * @param classList
     * the Vector to add the classes to
     * @param classPathFile
     * the File to scan as a zip file
     */
    fun addClassesFromZip(classList: Vector<String>, classPathFile: File) {
        // System.out.println("Processing jar/zip file: " + classPathFile);
        try {
            val zipFile = ZipFile(classPathFile)
            val enumeration: Enumeration<*> = zipFile.entries()
            while (enumeration.hasMoreElements()) {
                val zipEntry = enumeration.nextElement() as ZipEntry
                var current = zipEntry.name
                if (isInteresting(current)) {
                    current = current.substring(
                        0,
                        current.length - lookFor.length
                    )
                    classList.addElement(current)
                }
            }
        } catch (ex: Exception) {
            Resources.getInstance().logException(
                ex,
                "Problem opening $classPathFile with zip."
            )
        }
    }

    /**
     */
    private fun isInteresting(current: String): Boolean {
        val length = current.length
        if (length < lookFor.length) {
            return false
        }
        val currentPostfix = current.substring(length - lookFor.length)
        return lookFor.equals(currentPostfix, ignoreCase = true)
    }

    /**
     * Adds the classes from the supplied directory to the class list.
     *
     * @param classList
     * the Vector to add the classes to
     * @param currentDir
     * the File to recursively scan as a directory
     * @param recursionLevel
     * To ensure that after a certain depth the recursive directory
     * search stops
     */
    fun addClassesFromDir(
        classList: Vector<String>,
        rootDir: File,
        currentDir: File,
        recursionLevel: Int
    ) {
        if (recursionLevel >= 6) {
            // search only the first levels
            return
        }
        val files = currentDir.list()
        if (files != null) {
            for (i in files.indices) {
                var current = files[i]
                logger!!.fine("looking at: $current")
                if (isInteresting(current)) {
                    val rootPath = rootDir.path
                    val currentPath = currentDir.path
                    if (!currentPath.startsWith(rootPath)) {
                        logger!!.severe(
                            """
    currentPath doesn't start with rootPath!
    rootPath: $rootPath
    currentPath: $currentPath
    
                            """.trimIndent()
                        )
                    } else {
                        current = current.substring(
                            0,
                            current.length -
                                lookFor.length
                        )
                        val packageName = currentPath.substring(
                            rootPath
                                .length
                        )
                        var fileName: String
                        fileName = if (packageName.length > 0) {
                            // Not the current directory
                            (
                                packageName.substring(1) +
                                    File.separator + current
                                )
                        } else {
                            // The current directory
                            current
                        }
                        classList.addElement(fileName)
                        logger!!.info("Found: $fileName")
                    }
                } else {
                    // Check if it's a directory to recurse into
                    val currentFile = File(currentDir, current)
                    if (currentFile.isDirectory) {
                        addClassesFromDir(
                            classList, rootDir, currentFile,
                            recursionLevel + 1
                        )
                    }
                }
            }
        }
    }

    companion object {
        protected var logger: Logger? = null
    }
} // ImportWizard
