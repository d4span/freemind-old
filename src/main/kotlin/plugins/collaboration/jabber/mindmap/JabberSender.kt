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
 * Created on Mar 4, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package plugins.collaboration.jabber.mindmap

import com.echomine.common.ParseException
import freemind.main.Resources
import java.io.StringWriter
import java.lang.Exception
import java.util.logging.Logger

/**
 * @author RReppel
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class JabberSender(session: JabberSession?, // Freemind commands.
                   private val controller: MapSharingController) : ActionFilter {
    var chat: JabberChatService? = null
    var session: JabberSession? = null
    var sendToUser: String? = null
    var mapShared //True = send FreeMind commands. False = do not send
            = false

    init {
        if (logger == null) {
            logger = controller.controller.frame.getLogger(
                    this.javaClass.name)
        }
        try {
            this.session = session
            chat = this.session.getChatService()
            mapShared = false
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        }
    }

    /**
     * Sends a request to share a map. The receiving user can either accept or
     * decline the request.
     *
     * @param requestingUser
     * The user who requests the map to be shared.
     * @param requestReceiverUser
     * The user who is to receive the request to share a map.
     */
    fun sendMapSharingRequest(requestingUser: String,
                              requestReceiverUser: String?) {
        try {
            val action: CollaborationAction = createCollaborationAction(
                    requestingUser, REQUEST_MAP_SHARING)
            // populate action with filename and map content
            val mapName: String = controller.controller.map.file.getName()
            action.filename = mapName
            val stringWriter = StringWriter()
            controller.controller.map.getXml(stringWriter)
            action.map = stringWriter.buffer.toString()
            sendMessage(requestReceiverUser, action)
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        }
    }

    /**
     * Sends a request to stop sharing a map.
     *
     */
    fun sendMapSharingStopRequest() {
        try {
            val action: CollaborationAction = createCollaborationAction(sendToUser!!,
                    STOP_MAP_SHARING)
            val message = marshal(action)
            sendMessage(sendToUser, action)
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        }
    }

    /**
     * @param requestingUser
     * @param command
     * @throws JAXBException
     */
    private fun createCollaborationAction(
            requestingUser: String, command: String): CollaborationAction {
        val collaboration = CollaborationAction()
        collaboration.cmd = command
        collaboration.user = requestingUser
        collaboration.timestamp = System
                .currentTimeMillis().toString()
        return collaboration
    }

    /**
     * Sends whether a map sharing invitation was accepted or declined.
     *
     * @param sentFromUser
     * The name of the user accepting or declining the invitation.
     * @param sendToUser
     * The user who had requested that his/her map be shared.
     * @param accepted
     * true = accept, false = decline.
     */
    fun sendMapSharingInvitationResponse(sentFromUser: String,
                                         sendToUser: String?, accepted: Boolean) {
        try {
            this.sendToUser = sendToUser
            var message: String
            val action: CollaborationAction
            if (accepted) {
                action = createCollaborationAction(sentFromUser,
                        ACCEPT_MAP_SHARING)
                //                message = "<fmcmd cmd=\"" + ACCEPT_MAP_SHARING + "\" user=\""
                //                        + sentFromUser + "\"/>";
                mapShared = true
            } else {
                action = createCollaborationAction(sentFromUser,
                        DECLINE_MAP_SHARING)
                //                message = "<fmcmd cmd=\"" + DECLINE_MAP_SHARING + "\"
                // user=\""
                //                        + sentFromUser + "\"/>";
                mapShared = false
            }
            sendMessage(sendToUser, action)
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        }
    }

    /**
     * @param action
     * @return
     */
    private fun marshal(action: XmlAction): String {
        return controller.controller.marshall(action)
    }

    /** Sends commands to the other user(s?).
     * @param requestReceiverUser
     * @param action
     * @throws SendMessageFailedException
     * @throws ParseException
     */
    @Throws(SendMessageFailedException::class, ParseException::class)
    private fun sendMessage(requestReceiverUser: String?, action: XmlAction) {
        val message = marshal(action)
        if (!controller.isSendingEnabled) {
            logger
                    .warning("JabberSender should not send messages. In particular the following messages is not sent:"
                            + message)
            return
        }
        requireNotNull(requestReceiverUser) { "sendToUser is null. (Did you specify the user to share with by calling 'setMapShareUser'?)" }
        logger!!.info("Sending message:"
                + if (message.length < 100) message else message
                .substring(0, 50)
                + "..." + message.substring(message.length - 50))
        /*
         * Wait until there is a reply.
         */chat.sendPrivateMessage(JID(requestReceiverUser), Tools.compress(message), false)
    }

    /**
     * True if there is a shared map at present, false otherwise. If this value
     * is false, the sender will ignore requests to send Freemind commands.
     *
     * @param shared
     */
    fun isMapShared(shared: Boolean) {
        mapShared = shared
    }

    /**
     * Sets name of the user with whom the map is shared.
     *
     * @param username
     */
    fun setShareMapUser(username: String?) {
        sendToUser = username
    }

    /**
     * The overloaded filter action. Each action comes here along and is sent to the other
     * participants.
     */
    override fun filterAction(pair: ActionPair?): ActionPair? {
        try {
            val eAction = CompoundAction()
            eAction.listChoiceList.add(
                    pair.doAction)
            eAction.listChoiceList.add(
                    pair.undoAction)
            sendMessage(sendToUser, eAction)
        } catch (e: SendMessageFailedException) {
            Resources.getInstance().logException(e)
        } catch (e: ParseException) {
            Resources.getInstance().logException(e)
        }
        return pair
    }

    companion object {
        const val REQUEST_MAP_SHARING = "request_map_sharing"
        const val ACCEPT_MAP_SHARING = "accept_map_sharing"
        const val DECLINE_MAP_SHARING = "decline_map_sharing"
        const val STOP_MAP_SHARING = "stop_map_sharing"

        // Logging:
        private var logger: Logger? = null
    }
}