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
package plugins.collaboration.jabber.mindmap

import com.echomine.jabber.Jabber
import freemind.main.Resources
import java.lang.Exception
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author RReppel - Connects to a jabber server. - Establishes a private chat
 * with another user. - Listens to a limited number of FreeMind commands
 * sent by the other user. - Performs the FreeMind actions corresponding
 * to the commands sent.
 */
class JabberListener(c: MindMapController?,
                     sharingWizardController: MapSharingController, jabberServer: String?,
                     port: Int, userName: String?, password: String?) {
    var controller: MindMapController?

    //A queue ensuring FIFO processing of user commands.
    var commandQueue: LinkedList<*>
    var session: JabberSession

    init {
        controller = c
        if (logger == null) {
            logger = controller.controller.frame.getLogger(
                    this.javaClass.name)
        }
        commandQueue = LinkedList<Any?>()
        val context = JabberContext(userName, password,
                jabberServer)
        val jabber = Jabber()
        session = jabber.createSession(context)
        try {
            session.connect(jabberServer, port)
            session.getUserService().login()
            logger!!.info("User logged in.\n")
            session.getPresenceService().setToAvailable("FreeMind Session",
                    null, false)

            //Send a test message:
            //JabberChatService chat = session.getChatService();
            //chat.sendPrivateMessage(new JID("lucy@rreppel-linux"), "FreeMind
            // launched.", false);
            session.addMessageListener(FreeMindJabberMessageListener(
                    sharingWizardController)) //end addMessageListener
        } //end MindMapJabberController
        catch (ex: Exception) {
            Resources.getInstance().logException(ex)
            val message: String
            //TODO: Descriptive error message on Jabber server connection
            // failure.
            message = if (ex.javaClass.name.compareTo(
                            "com.echomine.jabber.JabberMessageException") == 0) {
                val jabberMessageException: JabberMessageException = ex as JabberMessageException
                jabberMessageException.getErrorMessage()
            } else {
                """
     ${ex.javaClass.name}
     
     ${ex.message}
     """.trimIndent()
            } //endif
            val frame = JFrame()
            JOptionPane.showMessageDialog(frame, message, "Error",
                    JOptionPane.ERROR_MESSAGE)
            //TODO: Bug: Do not move to the next screen when a connection error
            // has occurred. Do not set status to "connected".
        }
    }

    /**
     * @return
     */
    fun getSession(): JabberSession {
        return session
    }

    /**
     *
     * @author RReppel
     *
     * Listens to received Jabber messages and initiates the appropriate
     * FreeMind actions.
     */
    private inner class FreeMindJabberMessageListener(
            var sharingWizardController: MapSharingController) : JabberMessageListener {
        fun messageReceived(event: JabberMessageEvent) {
            if (event.getMessageType() !== JabberCode.MSG_CHAT) return
            val latestMsg: JabberChatMessage = event
                    .getMessage() as JabberChatMessage
            if (latestMsg.getType().equals(JabberChatMessage.TYPE_CHAT)
                    || latestMsg.getType()
                            .equals(JabberChatMessage.TYPE_NORMAL)) {
                commandQueue.addLast(latestMsg) //Add the message to the end
                // of the list of commands to
                // be applied.
                logger!!.info("Queue has " + commandQueue.size + " items.")
                val msg: JabberChatMessage = commandQueue
                        .removeFirst() as JabberChatMessage //Process the first command in the
                val msgString: String = Tools.decompress(msg.getBody())
                // list.
                if (logger!!.isLoggable(Level.INFO)) {
                    val displayMessage = "Sending message:" + if (msgString
                                    .length < 100) msgString else msgString
                            .substring(0, 50)
                    +"..." + msgString
                            .substring(msgString.length - 50)
                    logger!!.info("message " + displayMessage + " from "
                            + msg.getFrom().getUsername()
                            + " is reply required:" + msg.isReplyRequired())
                }
                val action: XmlAction = controller.unMarshall(msgString)
                if (action is CollaborationAction) {
                    val xml: CollaborationAction = action as CollaborationAction
                    val cmd: String = xml.cmd
                    val username: String = xml.user
                    try {
                        if (cmd.compareTo(JabberSender.Companion.REQUEST_MAP_SHARING) == 0) {
                            sharingWizardController
                                    .setMapSharingRequested(username, xml.map, xml.filename)
                        } else if (cmd
                                        .compareTo(JabberSender.Companion.ACCEPT_MAP_SHARING) == 0) {
                            sharingWizardController.setMapShareRequestAccepted(
                                    username, true)
                        } else if (cmd
                                        .compareTo(JabberSender.Companion.DECLINE_MAP_SHARING) == 0) {
                            sharingWizardController.setMapShareRequestAccepted(
                                    username, false)
                        } else if (cmd.compareTo(JabberSender.Companion.STOP_MAP_SHARING) == 0) {
                            sharingWizardController.setSharingStopped(username)
                        } else {
                            logger!!.warning("Unknown command:$cmd")
                        }
                    } catch (e: Exception) {
                        Resources.getInstance().logException(e)
                    } //end catch
                } else if (action is CompoundAction) {
                    val pair: CompoundAction = action as CompoundAction
                    if (pair
                                    .listChoiceList
                                    .size != 2) {
                        //FIXME: Warn the user
                        logger!!.warning("Cannot process the message "
                                + msgString)
                        return
                    }
                    executeRemoteCommand(pair)
                } else {
                    logger!!.warning("Unknown collaboration message:$msgString")
                } //endif
            } //endif
        } //end messageReceived

        /** Executes a command that was received via the jabber channel.
         * @param pair
         */
        private fun executeRemoteCommand(pair: CompoundAction) {
            val doAction: XmlAction = pair
                    .listChoiceList
                    .get(0) as XmlAction
            val undoAction: XmlAction = pair
                    .listChoiceList
                    .get(1) as XmlAction
            val ePair = ActionPair(doAction,
                    undoAction)
            SwingUtilities.invokeLater(Runnable {
                sharingWizardController.isSendingEnabled = false
                try {
                    sharingWizardController.controller
                            .getActionFactory()
                            .executeAction(ePair)
                } catch (e: Exception) {
                    // TODO: handle exception
                    Resources.getInstance().logException(e)
                }
                sharingWizardController.isSendingEnabled = true
            })
        }
    }

    companion object {
        // Logging:
        private var logger: Logger? = null
    }
}