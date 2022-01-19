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
/*$Id: FileNodeModel.java,v 1.11.18.1.4.5 2008/11/01 21:11:43 christianfoltin Exp $*/
package freemind.modes.filemode

import freemind.main.Resources
import freemind.main.Tools
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.modes.NodeAdapter
import java.awt.Color
import java.io.File
import java.net.MalformedURLException
import java.util.Collections
import java.util.LinkedList

/**
 * This class represents a single Node of a Tree. It contains direct handles to
 * its parent and children and to its view.
 */
class FileNodeModel(file: File?, map: MindMap?) : NodeAdapter(null, map) {
    /*
	 * if (file.isFile()) { return MindMapNode.STYLE_FORK; } else { return
	 * MindMapNode.STYLE_BUBBLE; } }
	 */ val file: File?
    private val color: Color? = null

    //
    // Constructors
    //
    init {
        edge = FileEdgeModel(this, mapFeedback)
        this.file = file
        isFolded = if (file != null) !file.isFile else false
    }

    // Overwritten get Methods
    override fun getStyle(): String {
        return STYLE_FORK
        // // This condition shows the code is not quite logical:
        // // ordinary file should not be considered folded and
        // // therefore the clause !isLeaf() should not be necessary.
        // if (isFolded()) { // && !isLeaf()) {
        // return MindMapNode.STYLE_BUBBLE;
        // } else {
        // return MindMapNode.STYLE_FORK;
        // }
    }

    /**
     * This could be a nice feature. Improve it!
     */
    override fun getColor(): Color {
        if (color == null) {

            // float hue = (float)getFile().length() / 100000;
            // float hue = 6.3F;
            // if (hue > 1) {
            // hue = 1;
            // }
            // color = Color.getHSBColor(hue,0.5F, 0.5F);
            // int red = (int)(1 / (getFile().length()+1) * 255);
            // color = new Color(red,0,0);
            color = if (isLeaf) Color.BLACK else Color.GRAY
        }
        return color
    }

    // void setFile(File file) {
    // this.file = file;
    // }
    override fun toString(): String {
        var name = file?.name
        if (name == "") {
            name = "Root"
        }
        return name ?: "null"
    }

    override fun getText(): String {
        return toString()
    }

    override fun hasChildren(): Boolean {
        return if (file != null) !file.isFile else false || children != null && !children.isEmpty()
    }

    /**
     *
     */
    override fun childrenFolded(): ListIterator<MindMapNode?> {
        if (!isRoot) {
            if (isFolded || isLeaf) {
                return Collections.emptyListIterator()
                // return null;//Empty Enumeration
            }
        }
        return childrenUnfolded()
    }

    override fun childrenUnfolded(): ListIterator<MindMapNode?> {
        if (children != null) {
            return children.listIterator()
        }
        // Create new nodes by reading children from file system
        try {
            val files = file?.list()
            if (files != null) {
                children = LinkedList()
                val path = file?.path
                for (i in files.indices) {
                    val childFile = File(path, files[i])
                    if (!childFile.isHidden) {
                        val fileNodeModel = FileNodeModel(
                            childFile, map
                        )
                        fileNodeModel.isLeft = isNewChildLeft
                        insert(fileNodeModel, childCount)
                    }
                }
            }
        } catch (se: SecurityException) {
        }
        // return children.listIterator();
        return if (children != null) children.listIterator() else Collections.emptyListIterator()
    }

    override fun isLeaf(): Boolean {
        return file?.isFile ?: false
    }

    override fun getLink(): String {
        try {
            return Tools.fileToUrl(file).toString()
        } catch (e: MalformedURLException) {
            Resources.getInstance().logException(e)
        }
        return file.toString()
    }

    override fun isWriteable(): Boolean {
        return false
    }
}
