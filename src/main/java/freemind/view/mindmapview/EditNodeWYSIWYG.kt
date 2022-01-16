/*FreeMind - a program for creating and viewing mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *See COPYING for details
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
 *
 */
/*$Id: EditNodeWYSIWYG.java,v 1.1.4.46 2010/05/25 20:09:32 christianfoltin Exp $*/
package freemind.view.mindmapview

import accessories.plugins.NodeNoteRegistration.SimplyHtmlResources
import com.inet.jortho.SpellChecker
import com.lightdev.app.shtm.SHTMLPanel
import freemind.main.HtmlTools
import freemind.main.Resources
import freemind.main.Tools
import freemind.modes.ModeController
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.event.KeyEvent
import java.net.MalformedURLException
import java.net.URL
import javax.swing.JButton
import javax.swing.JPanel

/**
 * @author Daniel Polansky
 */
class EditNodeWYSIWYG(
    node: NodeView?,
    text: String?,
    private val firstEvent: KeyEvent,
    controller: ModeController?,
    editControl: EditControl?
) : EditNodeBase(node!!, text!!, controller!!, editControl!!) {
    private class HTMLDialog internal constructor(base: EditNodeBase) : EditDialog(base) {
        /**
         * @return Returns the htmlEditorPanel.
         */
        var htmlEditorPanel: SHTMLPanel? = null
            private set

        init {
            createEditorPanel()
            contentPane.add(htmlEditorPanel, BorderLayout.CENTER)
            Tools.addEscapeActionToDialog(this, CancelAction())
            val okButton = JButton()
            val cancelButton = JButton()
            val splitButton = JButton()
            Tools.setLabelAndMnemonic(okButton, base.getText("ok"))
            Tools.setLabelAndMnemonic(cancelButton, base.getText("cancel"))
            Tools.setLabelAndMnemonic(splitButton, base.getText("split"))
            okButton.addActionListener { submit() }
            cancelButton.addActionListener { cancel() }
            splitButton.addActionListener { split() }
            Tools.addKeyActionToDialog(
                this, SubmitAction(), "alt ENTER",
                "submit"
            )
            Tools.addKeyActionToDialog(
                this, SubmitAction(),
                "control ENTER", "submit"
            )
            val buttonPane = JPanel()
            buttonPane.add(okButton)
            buttonPane.add(cancelButton)
            buttonPane.add(splitButton)
            buttonPane.maximumSize = Dimension(1000, 20)
            contentPane.add(buttonPane, BorderLayout.SOUTH)
            htmlEditorPanel!!.setOpenHyperlinkHandler { pE ->
                try {
                    base.getController().frame
                        .openDocument(URL(pE.actionCommand))
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                }
            }
            if (checkSpelling) {
                SpellChecker.register(htmlEditorPanel!!.editorPane)
            }
        }

        @Throws(Exception::class)
        private fun createEditorPanel(): SHTMLPanel? {
            if (htmlEditorPanel == null) {
                SHTMLPanel.setResources(SimplyHtmlResources())
                htmlEditorPanel = SHTMLPanel.createSHTMLPanel()
                // 				htmlEditorPanel.getEditorPane().addMouseListener(new MouseAdapter () {
// 					public void mousePressed(MouseEvent e) {
// 						conditionallyShowPopup(e);
// 					}
//
// 					public void mouseReleased(MouseEvent e) {
// 						conditionallyShowPopup(e);
// 					}
//
// 					private void conditionallyShowPopup(MouseEvent e) {
// 						if (e.isPopupTrigger()) {
// 							System.out.println("fooooooooooooooooooooo");
// 							JPopupMenu popupMenu =
// 									((SHTMLEditorPane) e.getSource()).getPopup();
// 							if (checkSpelling && popupMenu != null) {
// //								popupMenu.add(SpellChecker.createCheckerMenu(), 0);
// //								popupMenu.add(SpellChecker.createLanguagesMenu(), 1);
// //								popupMenu.addSeparator();
// //								popupMenu.show(e.getComponent(), e.getX(), e.getY());
// 							}
// //							e.consume();
// 						}
// 					}
// 				});
            }
            return htmlEditorPanel
        }

        /*
		 * (non-Javadoc)
		 *
		 * @see freemind.view.mindmapview.EditNodeBase.Dialog#close()
		 */
        override fun submit() {
            removeBodyStyle()
            if (htmlEditorPanel!!.needsSaving()) {
                base.editControl.ok(
                    HtmlTools.unescapeHTMLUnicodeEntity(
                        htmlEditorPanel
                            ?.getDocumentText()
                    )
                )
            } else {
                base.editControl.cancel()
            }
            super.submit()
        }

        private fun removeBodyStyle() {
            htmlEditorPanel!!.document.styleSheet.removeStyle("body")
        }

        /*
		 * (non-Javadoc)
		 *
		 * @see freemind.view.mindmapview.EditNodeBase.Dialog#split()
		 */
        override fun split() {
            removeBodyStyle()
            base.editControl.split(
                HtmlTools.unescapeHTMLUnicodeEntity(
                    htmlEditorPanel
                        ?.getDocumentText()
                ),
                htmlEditorPanel!!.caretPosition
            )
            super.split()
        }

        /*
		 * (non-Javadoc)
		 *
		 * @see freemind.view.mindmapview.EditNodeBase.Dialog#close()
		 */
        override fun cancel() {
            removeBodyStyle()
            base.editControl.cancel()
            super.cancel()
        }

        override val isChanged: Boolean
            get() = htmlEditorPanel!!.needsSaving()

        override fun getMostRecentFocusOwner(): Component {
            return if (isFocused) {
                focusOwner
            } else {
                htmlEditorPanel!!.mostRecentFocusOwner
            }
        }

        companion object {
            private const val serialVersionUID = 2862979626489782521L
        }
    }

    fun show() {
        // Return true if successful.
        try {
            val frame = frame
            if (htmlEditorWindow == null) {
                htmlEditorWindow = HTMLDialog(this)
            }
            htmlEditorWindow!!.base = this
            val htmlEditorPanel = htmlEditorWindow?.htmlEditorPanel
            var rule = "BODY {"
            var font = node.textFont
            if (Resources.getInstance().getBoolProperty(
                    "experimental_font_sizing_for_long_node_editors"
                )
            ) {
                /*
				 * This is a proposal of Dan, but it doesn't work as expected.
				 *
				 * http://sourceforge.net/tracker/?func=detail&aid=2800933&group_id
				 * =7118&atid=107118
				 */
                font = Tools.updateFontSize(
                    font, view.getZoom(),
                    font.size
                )
            }
            val nodeTextBackground = node.textBackground
            rule += "font-family: " + font.family + ";"
            rule += "font-size: " + font.size + "pt;"
            // Daniel said:, but no effect:
            // rule += "font-size: "+node.getFont().getSize()+"pt;";
            if (node.model.isItalic) {
                rule += "font-style: italic; "
            }
            if (node.model.isBold) {
                rule += "font-weight: bold; "
            }
            val nodeTextColor = node.textColor
            rule += "color: " + Tools.colorToXml(nodeTextColor) + ";"
            rule += "}\n"
            rule += "p {"
            rule += "margin-top:0;"
            rule += "}\n"
            val document = htmlEditorPanel?.document
            val editorPane = htmlEditorPanel?.editorPane
            editorPane?.foreground = nodeTextColor
            editorPane?.background = nodeTextBackground
            editorPane?.caretColor = nodeTextColor
            document?.styleSheet?.addRule(rule)
            try {
                document?.base = node.map.model?.url
            } catch (e: MalformedURLException) {
            }

            // { -- Set size (can be refactored to share code with long node
            // editor)
            var preferredHeight = ((node.getMainView()?.height ?: 0) * 1.2).toInt()
            preferredHeight = Math.max(
                preferredHeight,
                frame
                    .getProperty("el__min_default_window_height").toInt()
            )
            preferredHeight = Math.min(
                preferredHeight,
                frame
                    .getProperty("el__max_default_window_height").toInt()
            )
            var preferredWidth = ((node.getMainView()?.width ?: 0) * 1.2).toInt()
            preferredWidth = Math.max(
                preferredWidth,
                frame
                    .getProperty("el__min_default_window_width").toInt()
            )
            preferredWidth = Math.min(
                preferredWidth,
                frame
                    .getProperty("el__max_default_window_width").toInt()
            )
            htmlEditorPanel?.setContentPanePreferredSize(
                Dimension(
                    preferredWidth, preferredHeight
                )
            )
            // }
            htmlEditorWindow!!.pack()
            Tools.setDialogLocationRelativeTo(htmlEditorWindow, node)
            var content: String? = node.model.toString()
            if (!HtmlTools.isHtmlNode(content)) {
                content = HtmlTools.plainToHTML(content)
            }
            htmlEditorPanel?.setCurrentDocumentContent(content)
            if (true) {
                val currentPane = htmlEditorPanel?.editorPane
                if (currentPane === htmlEditorPanel?.mostRecentFocusOwner) {
                    redispatchKeyEvents(currentPane, firstEvent)
                }
            } // 1st key event defined
            else {
                editorPane?.caretPosition = htmlEditorPanel?.document?.length ?: 0
            }
            htmlEditorPanel?.mostRecentFocusOwner?.requestFocus()
            htmlEditorWindow!!.isVisible = true
        } catch (ex: Exception) { // Probably class not found exception
            Resources.getInstance().logException(ex)
            System.err
                .println("Loading of WYSIWYG HTML editor failed. Use the other editors instead.")
        }
    }

    companion object {
        private var htmlEditorWindow: HTMLDialog? = null
    }
}
