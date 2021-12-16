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
 * Created on 10.07.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.modes.attributes

import freemind.modes.MindMapNode
import javax.swing.table.TableModel

/**
 * @author Dimitri Polivaev 10.07.2005
 */
interface AttributeTableModel : TableModel {
    override fun getRowCount(): Int
    fun getColumnWidth(col: Int): Int
    override fun getValueAt(row: Int, col: Int): Any
    override fun setValueAt(o: Any, row: Int, col: Int)
    val node: MindMapNode?
    fun fireTableDataChanged()
}