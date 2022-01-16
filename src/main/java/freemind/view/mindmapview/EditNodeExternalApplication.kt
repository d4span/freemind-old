/*FreeMind - a program for creating and viewing mindmaps
 *Copyright (C) 2000-2005  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *
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
 * Created on 02.05.2004
 */
/*$Id: EditNodeExternalApplication.java,v 1.1.4.3 2008/03/14 21:15:24 christianfoltin Exp $*/
package freemind.view.mindmapview

import freemind.main.Resources
import freemind.main.Tools
import freemind.modes.ModeController
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileWriter
import java.text.MessageFormat

/**
 * @author Daniel Polansky
 */
class EditNodeExternalApplication(
    node: NodeView?,
    text: String?,
    protected val firstEvent: KeyEvent,
    controller: ModeController?,
    editControl: EditControl?
) : EditNodeBase(node!!, text!!, controller!!, editControl!!) {

    fun show() {
        frame
        // final Controller controller = getController();
        // mainWindow.setEnabled(false);
        object : Thread() {
            override fun run() {
                var writer: FileWriter? = null
                try {
                    val temporaryFile = File.createTempFile("tmm", ".html")

                    // a. Write the text of the long node to the temporary file
                    writer = FileWriter(temporaryFile)
                    writer.write(text)
                    writer.close()

                    // b. Call the editor
                    val htmlEditingCommand = frame.getProperty(
                        "html_editing_command"
                    )
                    val expandedHtmlEditingCommand = MessageFormat(
                        htmlEditingCommand
                    )
                        .format(arrayOf(temporaryFile.toString()))
                    // System.out.println("External application:"+expandedHtmlEditingCommand);
                    val htmlEditorProcess = Runtime.getRuntime().exec(
                        expandedHtmlEditingCommand
                    )
                    htmlEditorProcess.waitFor() // Here we wait
                    // until the
                    // editor ends
                    // up itself
                    // Waiting does not work if the process starts another
                    // process,
                    // like in case of Microsoft Word. It works with certain
                    // versions of FrontPage,
                    // and with Vim though.

                    // c. Get the text from the temporary file
                    val content = Tools.getFile(temporaryFile)
                    if (content == null) {
                        editControl.cancel()
                    }
                    editControl.ok(content)
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                    try {
                        writer?.close()
                        // if (bufferedReader != null) {
                        // bufferedReader.close();
                        // }
                    } catch (e1: Exception) {
                    }
                }
                // setBlocked(false);
                // mainWindow.setEnabled(true); // Not used as it loses focus on
                // the window
                // controller.obtainFocusForSelected(); }
            }
        }.start()
        return
    }
}
