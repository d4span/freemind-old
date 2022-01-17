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
package freemind.modes.common.actions

import freemind.main.FreeMind
import freemind.main.HtmlTools
import freemind.main.Resources
import freemind.main.Tools
import freemind.modes.ControllerAdapter
import freemind.modes.FreemindAction
import freemind.modes.MindMapNode
import freemind.view.ImageFactory.Companion.instance
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.LinkedList
import java.util.Locale
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JTextField

class FindAction(private val controller: ControllerAdapter) :
    FreemindAction("find", "images/filefind.png", controller) {
    private var findNodesUnfoldedByLastFind: ArrayList<MindMapNode>? = null
    private var findFromNode: MindMapNode? = null
    var searchTerm: String? = null
        private set

    /**
     * @return Returns the subterms.
     */
    var subterms: Collection<String>? = null
        private set
    val findFromText: String
        get() {
            val plainNodeText = HtmlTools.htmlToPlain(findFromNode.toString())
                .replace("\n".toRegex(), " ")
            return if (plainNodeText.length <= 30) plainNodeText else plainNodeText
                .substring(0, 30) + "..."
        }
    private var findCaseSensitive = false
    private var findNodeQueue: LinkedList<MindMapNode>? = null
    private var mDialog: JDialog? = null
    private var mResult = 0
    private var mFindInNotesTooBox: JCheckBox? = null
    private var mSearchField: JTextField? = null

    /** This isn't stored and is per map.  */
    private var mLastSearchString: String? = null
    override fun actionPerformed(e: ActionEvent) {
        displayDialog()
        if (mResult != JOptionPane.OK_OPTION) {
            return
        }
        val what = mSearchField!!.text
        if (what == null || what == "") {
            return
        }
        val subterms = breakSearchTermIntoSubterms(what)
        searchTerm = what
        // System.err.println(subterms);
        /* caseSensitive=false */
        val found = find(controller.selected, subterms, false)
        controller.view.repaint()
        if (!found) {
            val messageText = controller.getText("no_found_from")
            val searchTerm = getSearchTermAsEscapedString(messageText)
            controller.controller.informationMessage(
                messageText!!.replace("\\$1".toRegex(), searchTerm).replace(
                    "\\$2".toRegex(), findFromText
                ),
                controller.view.selected
            )
        }
    }

    private fun close(pResult: Int) {
        mResult = pResult
        mDialog!!.isVisible = false
        mDialog!!.dispose()
        // Store "find in notes too" value to prop.
        if (pResult == JOptionPane.OK_OPTION) {
            Resources
                .getInstance()
                .properties
                .setProperty(
                    FreeMind.RESOURCES_SEARCH_IN_NOTES_TOO,
                    if (mFindInNotesTooBox!!.isSelected) "true" else "false"
                )
            mLastSearchString = mSearchField!!.text
        }
    }

    fun displayDialog() {
        mDialog = null
        mDialog = JDialog(
            controller.frame.jFrame,
            controller.getText("find")
        )
        mDialog!!.isModal = true
        mDialog!!.defaultCloseOperation = JDialog.DO_NOTHING_ON_CLOSE
        val cancelAction: AbstractAction = object : AbstractAction() {
            override fun actionPerformed(pE: ActionEvent) {
                close(JOptionPane.CANCEL_OPTION)
            }
        }
        val okAction: AbstractAction = object : AbstractAction() {
            override fun actionPerformed(pE: ActionEvent) {
                close(JOptionPane.OK_OPTION)
            }
        }
        Tools.addEscapeActionToDialog(mDialog, cancelAction)
        mDialog!!.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(pE: WindowEvent) {
                close(JOptionPane.CANCEL_OPTION)
            }
        })
        val contentPane = mDialog!!.contentPane
        contentPane.layout = GridBagLayout()
        contentPane.add(
            JLabel(controller.getText("find_what")),
            GridBagConstraints(
                1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        mSearchField = JTextField(mLastSearchString)
        mSearchField!!.selectAll()
        mSearchField!!.minimumSize = Dimension(500, 14)
        contentPane.add(
            mSearchField,
            GridBagConstraints(
                2, 0, 10, 1, 1.0,
                1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        val findImage = instance!!.createIcon(
            Resources.getInstance()
                .getResource("images/filefind_big.png")
        )
        contentPane.add(
            JLabel(findImage),
            GridBagConstraints(
                0, 0, 1,
                2, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        mFindInNotesTooBox = JCheckBox(
            controller
                .getText("ExtendedFindDialog.find_search_in_notes_too")
        )
        mFindInNotesTooBox!!.isSelected = Resources.getInstance().getBoolProperty(
            FreeMind.RESOURCES_SEARCH_IN_NOTES_TOO
        )
        Tools.setLabelAndMnemonic(mFindInNotesTooBox, null)
        contentPane.add(
            mFindInNotesTooBox,
            GridBagConstraints(
                0, 2, 3, 1,
                1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        val okButton = JButton(
            controller.getText("ExtendedFindDialog.ok")
        )
        Tools.setLabelAndMnemonic(okButton, null)
        okButton.addActionListener(okAction)
        contentPane.add(
            okButton,
            GridBagConstraints(
                2, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(
                    5,
                    5, 0, 0
                ),
                0, 0
            )
        )
        val cancelButton = JButton(
            controller.getText("ExtendedFindDialog.cancel")
        )
        Tools.setLabelAndMnemonic(cancelButton, null)
        cancelButton.addActionListener(cancelAction)
        contentPane.add(
            cancelButton,
            GridBagConstraints(
                3, 3, 1, 1, 1.0,
                1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        mDialog!!.rootPane.defaultButton = okButton
        mDialog!!.pack()
        Tools.setDialogLocationRelativeTo(mDialog, controller.selectedView)
        mDialog!!.isVisible = true
    }

    private fun getSearchTermAsEscapedString(messageText: String?): String {
        var searchTerm = if (messageText!!.startsWith("<html>")) HtmlTools
            .toXMLEscapedText(searchTerm) else searchTerm!!
        // Fix for
        // https://sourceforge.net/tracker/?func=detail&aid=3200783&group_id=7118&atid=107118
        // Patch
        // https://sourceforge.net/tracker/?func=detail&aid=3276562&group_id=7118&atid=307118,
        // thanks to the author
        searchTerm = searchTerm.replace("$", "\\$")
        return searchTerm
    }

    class FindNextAction(private val controller: ControllerAdapter, private val find: FindAction) :
        FreemindAction("find_next", controller) {
        override fun actionPerformed(e: ActionEvent) {
            val subterms = find.subterms
            if (subterms == null) {
                controller.controller.informationMessage(
                    controller.getText("no_previous_find")!!,
                    controller.view.selected
                )
                return
            }
            val found = find.findNext()
            controller.view.repaint()
            if (!found) {
                val messageText = controller.getText("no_more_found_from")
                val searchTerm = find
                    .getSearchTermAsEscapedString(messageText)
                controller.controller.informationMessage(
                    messageText!!.replace("\\$1".toRegex(), searchTerm).replace(
                        "\\$2".toRegex(), find.findFromText
                    ),
                    controller.view.selected
                )
            }
        }
    }

    fun find(
        node: MindMapNode,
        subterms: MutableCollection<String>,
        caseSensitive: Boolean
    ): Boolean {
        findNodesUnfoldedByLastFind = ArrayList()
        val nodes = LinkedList<MindMapNode>()
        nodes.addFirst(node)
        findFromNode = node
        val finalizedSubterms: MutableCollection<String>
        if (!caseSensitive) {
            finalizedSubterms = ArrayList()
            for (subterm in subterms) {
                finalizedSubterms.add(subterm.lowercase(Locale.getDefault()))
            }
        } else {
            finalizedSubterms = subterms
        }
        return find(nodes, finalizedSubterms, caseSensitive)
    }

    private fun find(
        nodes: LinkedList<MindMapNode>?,
        subterms: Collection<String>,
        caseSensitive: Boolean
    ): Boolean {
        // Precondition: if !caseSensitive then >>what<< is in lowercase.
        val searchInNotesToo = Resources.getInstance().getBoolProperty(
            FreeMind.RESOURCES_SEARCH_IN_NOTES_TOO
        )
        if (!findNodesUnfoldedByLastFind!!.isEmpty()) {

            // if (false) {
            val i: ListIterator<MindMapNode> = findNodesUnfoldedByLastFind!!.listIterator(
                findNodesUnfoldedByLastFind!!.size
            )
            while (i.hasPrevious()) {
                val node = i.previous()
                try {
                    controller.setFolded(node, true)
                } catch (e: Exception) {
                }
            }
            findNodesUnfoldedByLastFind = ArrayList()
        }

        // We implement width-first search.
        while (!nodes!!.isEmpty()) {
            val node = nodes.removeFirst() as MindMapNode
            // Add children to the queue
            val i = node.childrenUnfolded()
            while (i.hasNext()) {
                nodes.addLast(i.next())
            }
            if (!node.isVisible) continue

            // Bug fix for
            // http://sourceforge.net/tracker/?func=detail&aid=3035387&group_id=7118&atid=107118
            var nodeText: String? = node.toString()
            nodeText = prepareTextContent(caseSensitive, nodeText)
            // End bug fix.
            var noteText = node.noteText
            noteText = prepareTextContent(caseSensitive, noteText)
            var found = true
            var foundInNotes = false
            for (subterm in subterms) {
                if (nodeText!!.indexOf(subterm) < 0) {
                    // Subterm not found
                    found = false
                    break
                }
            }
            if (!found && searchInNotesToo) {
                /* now, search the notes. */
                found = true
                for (subterm in subterms) {
                    if (noteText!!.indexOf(subterm) < 0) {
                        // Subterm not found
                        found = false
                        break
                    }
                }
                foundInNotes = true
            }
            if (found) { // Found
                displayNode(node, findNodesUnfoldedByLastFind)
                centerNode(node)
                if (foundInNotes) {
                    // TODO: Select text in notes window.
                }
                // Save the state for find next
                this.subterms = subterms
                findCaseSensitive = caseSensitive
                findNodeQueue = nodes
                return true
            }
        }
        centerNode(findFromNode)
        return false
    }

    fun prepareTextContent(caseSensitive: Boolean, nodeText: String?): String? {
        var text = nodeText
        if (text == null) {
            text = ""
        }
        if (HtmlTools.isHtmlNode(text)) {
            text = HtmlTools.removeHtmlTagsFromString(text)
            text = HtmlTools.unescapeHTMLUnicodeEntity(text)
        }
        if (!caseSensitive) {
            text = text!!.lowercase(Locale.getDefault())
        }
        return text
    }

    private fun breakSearchTermIntoSubterms(searchTerm: String): MutableCollection<String> {
        val subterms = ArrayList<String>()
        val subterm = StringBuffer()
        val len = searchTerm.length
        var myChar: Char
        var withinQuotes = false
        for (i in 0 until len) {
            myChar = searchTerm[i]
            if (myChar == ' ' && withinQuotes) {
                subterm.append(myChar)
            } else if (myChar == ' ' && !withinQuotes) {
                subterms.add(subterm.toString())
                subterm.setLength(0)
            } else if (myChar == '"' && i > 0 && i < len - 1 && searchTerm[i - 1] != ' ' && searchTerm[i + 1] != ' ') {
                // Character " surrounded by non-spaces
                subterm.append(myChar)
            } else if (myChar == '"' && withinQuotes) {
                withinQuotes = false
            } else if (myChar == '"' && !withinQuotes) {
                withinQuotes = true
            } else {
                subterm.append(myChar)
            }
        }
        subterms.add(subterm.toString())
        return subterms
    }

    /**
     * Display a node in the display (used by find and the goto action by arrow
     * link actions).
     */
    fun displayNode(node: MindMapNode?, nodesUnfoldedByDisplay: ArrayList<MindMapNode>?) {
        // Unfold the path to the node
        val path = controller.map.getPathToRoot(node)
        // Iterate the path with the exception of the last node
        for (i in 0 until path.size - 1) {
            val nodeOnPath = path[i] as MindMapNode
            // System.out.println(nodeOnPath);
            if (nodeOnPath.isFolded) {
                nodesUnfoldedByDisplay?.add(nodeOnPath)
                controller.setFolded(nodeOnPath, false)
            }
        }
    }

    fun findNext(): Boolean {
        // Precodition: subterms != null. We check the precodition but give no
        // message.

        // The logic of find next is vulnerable. find next relies on the queue
        // of nodes from previous find / find next. However, between previous
        // find / find next and this find next, nodes could have been deleted
        // or moved. The logic expects that no changes happened, even that no
        // node has been folded / unfolded.

        // You may want to come with more correct solution, but this one
        // works for most uses, and does not cause any big trouble except
        // perhaps for some uncaught exceptions. As a result, it is not very
        // nice, but far from critical and working quite fine.
        return if (subterms != null) {
            find(findNodeQueue, subterms!!, findCaseSensitive)
        } else false
    }

    /**
     */
    private fun centerNode(node: MindMapNode?) {
        // Select the node and scroll to it.
        controller.centerNode(node)
    }
}
