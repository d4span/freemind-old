/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 10.01.2006
 */
package freemind.modes.browsemode

import freemind.common.TextTranslator
import freemind.main.Resources
import freemind.main.Tools.SingleDesEncrypter
import freemind.main.XMLParseException
import freemind.modes.MapFeedback
import freemind.modes.MindIcon
import freemind.modes.ModeController
import freemind.modes.NodeAdapter
import freemind.modes.common.dialogs.EnterPasswordDialog
import java.io.IOException
import java.io.StringReader
import java.util.logging.Logger
import javax.swing.ImageIcon

/**
 * @author foltin
 */
class EncryptedBrowseNode(userObject: Any?, private val mMapFeedback: MapFeedback) :
    BrowseNodeModel(userObject, mMapFeedback.map) {
    private var encryptedContent: String? = null
    private var isDecrypted = false

    /**
     */
    constructor(pMapFeedback: MapFeedback) : this(null, pMapFeedback) {}

    /**
     */
    init {
        if (logger == null) logger = Resources.getInstance().getLogger(
            this.javaClass.name
        )
        if (encryptedIcon == null) {
            encryptedIcon = MindIcon.factory("encrypted").icon
        }
        if (decryptedIcon == null) {
            decryptedIcon = MindIcon.factory("decrypted").icon
        }
        updateIcon()
    }

    fun updateIcon() {
        setStateIcon("encryptedNode", if (isDecrypted) decryptedIcon else encryptedIcon)
    }

    override fun setFolded(folded: Boolean) {
        if (isDecrypted || folded) {
            super.setFolded(folded)
            return
        }
        // get password:
        val pwdDialog = EnterPasswordDialog(
            null,
            object : TextTranslator {
                override fun getText(pKey: String?): String? {
                    return mMapFeedback.getResourceString(pKey)
                }
            },
            false
        )
        pwdDialog.isModal = true
        pwdDialog.isVisible = true
        if (pwdDialog.result == EnterPasswordDialog.CANCEL) {
            return
        }
        val encrypter = SingleDesEncrypter(
            pwdDialog.password
        )
        // Decrypt
        val decrypted = encrypter.decrypt(encryptedContent) ?: return
        val IDToTarget = HashMap<String, NodeAdapter>()
        val childs = decrypted.split(ModeController.NODESEPARATOR).toTypedArray()
        // and now? paste it:
        for (i in childs.indices.reversed()) {
            val string = childs[i]
            logger!!.finest("Decrypted '$string'.")
            // if the encrypted node is empty, we skip the insert.
            if (string.length == 0) continue
            try {
                val node = map
                    .createNodeTreeFromXml(StringReader(string), IDToTarget) as NodeAdapter
                // now, the import is finished. We can inform others about
                // the new nodes:
                val model = mMapFeedback.map
                model.insertNodeInto(node, this, this.childCount)
                mMapFeedback.invokeHooksRecursively(node, model)
                super.setFolded(folded)
                mMapFeedback.nodeChanged(this)
                // mMapFeedback.nodeStructureChanged(this);
                isDecrypted = true
                updateIcon()
            } catch (e: XMLParseException) {
                Resources.getInstance().logException(e)
                return
            } catch (e: IOException) {
                Resources.getInstance().logException(e)
                return
            }
        }
    }

    /**
     *
     */
    override fun setAdditionalInfo(info: String) {
        encryptedContent = info
        isDecrypted = false
    }

    companion object {
        private var encryptedIcon: ImageIcon? = null
        private var decryptedIcon: ImageIcon? = null

        // Logging:
        protected var logger: Logger? = null
    }
}
