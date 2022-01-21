/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2007  Christian Foltin, Dimitry Polivaev and others.
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
 * Created on 31.07.2007
 */
/*$Id: OptionalDontShowMeAgainDialog.java,v 1.1.2.6 2009/12/09 21:57:39 christianfoltin Exp $*/
package freemind.common

import freemind.controller.Controller
import freemind.main.Resources
import freemind.main.Tools
import freemind.view.ImageFactory
import java.awt.Component
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.logging.Logger
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JCheckBox
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane

/**
 * Dialog with a decision that can be disabled.
 *
 * @author foltin
 */
class OptionalDontShowMeAgainDialog(
    pFrame: JFrame, pComponent: Component?,
    pMessageId: String, pTitleId: String, pTextTranslator: TextTranslator,
    pDontShowPropertyHandler: DontShowPropertyHandler, pMessageType: Int
) {
    private val mTitleId: String
    private val mMessageId: String
    private val mTextTranslator: TextTranslator
    private val mParent: JFrame

    /**
     * @return an int from JOptionPane (eg. JOptionPane.OK_OPTION).
     */
    var result = JOptionPane.CANCEL_OPTION
        private set
    private var mDialog: JDialog? = null
    private var mDontShowAgainBox: JCheckBox? = null
    private val mDontShowPropertyHandler: DontShowPropertyHandler
    private val mMessageType: Int
    private val mComponent: Component?

    interface DontShowPropertyHandler {
        /**
         * @return accepted are the following values as return values: * ""
         * (means: show this dialog) * "true" (means: the answer was ok
         * and I want to remember that). * "false" (means: the answer
         * was cancel and I want to remember that).
         */
        var property: String?
    }

    /**
     * Standard property handler, if you have a controller and a property.
     *
     */
    class StandardPropertyHandler(
        private val mController: Controller,
        private val mPropertyName: String
    ) : DontShowPropertyHandler {
        override var property: String?
            get() = mController.getProperty(mPropertyName)
            set(pValue) {
                mController.setProperty(mPropertyName, pValue)
            }
    }

    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
        mComponent = pComponent
        mParent = pFrame
        mMessageId = pMessageId
        mTitleId = pTitleId
        mTextTranslator = pTextTranslator
        mDontShowPropertyHandler = pDontShowPropertyHandler
        mMessageType = pMessageType
    }

    fun show(): OptionalDontShowMeAgainDialog {
        val property = mDontShowPropertyHandler.property
        if (Tools.safeEquals(property, "true")) {
            result = JOptionPane.OK_OPTION
            return this
        }
        if (Tools.safeEquals(property, "false")) {
            result = JOptionPane.CANCEL_OPTION
            return this
        }
        mDialog = null
        mDialog = JDialog(mParent, mTextTranslator.getText(mTitleId))
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
        mDialog!!.contentPane.layout = GridBagLayout()
        mDialog!!.contentPane.add(
            JLabel(mTextTranslator.getText(mMessageId)),
            GridBagConstraints(
                1, 0, 1, 1, 10.0, 4.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 10
            )
        )
        // TODO: Replace by usual java question mark.
        val questionMark = ImageFactory.getInstance().createIcon(
            Resources.getInstance()
                .getResource("images/icons/help.png")
        )
        mDialog!!.contentPane.add(
            JLabel(questionMark),
            GridBagConstraints(
                0, 0, 1, 2, 1.0, 2.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        val boxString: String
        boxString = if (mMessageType == ONLY_OK_SELECTION_IS_STORED) {
            "OptionalDontShowMeAgainDialog.dontShowAgain"
        } else {
            "OptionalDontShowMeAgainDialog.rememberMyDescision"
        }
        mDontShowAgainBox = JCheckBox(mTextTranslator.getText(boxString))
        Tools.setLabelAndMnemonic(mDontShowAgainBox, null)
        mDialog!!.contentPane.add(
            mDontShowAgainBox,
            GridBagConstraints(
                0, 2, 3, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        val okButton = JButton(
            mTextTranslator.getText("OptionalDontShowMeAgainDialog.ok")
        )
        Tools.setLabelAndMnemonic(okButton, null)
        okButton.addActionListener(okAction)
        mDialog!!.contentPane.add(
            okButton,
            GridBagConstraints(
                2, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        val cancelButton = JButton(
            mTextTranslator.getText("OptionalDontShowMeAgainDialog.cancel")
        )
        Tools.setLabelAndMnemonic(cancelButton, null)
        cancelButton.addActionListener(cancelAction)
        mDialog!!.contentPane.add(
            cancelButton,
            GridBagConstraints(
                3, 3, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                Insets(5, 5, 0, 0), 0, 0
            )
        )
        mDialog!!.rootPane.defaultButton = okButton
        mDialog!!.pack()
        if (mComponent != null) {
            Tools.setDialogLocationRelativeTo(mDialog, mComponent)
        }
        mDialog!!.isVisible = true
        return this
    }

    private fun close(pResult: Int) {
        result = pResult
        if (mDontShowAgainBox!!.isSelected) {
            if (mMessageType == ONLY_OK_SELECTION_IS_STORED) {
                if (result == JOptionPane.OK_OPTION) {
                    mDontShowPropertyHandler.property = "true"
                }
            } else {
                mDontShowPropertyHandler.property = if (result == JOptionPane.OK_OPTION) "true" else "false"
            }
        } else {
            mDontShowPropertyHandler.property = ""
        }
        mDialog!!.isVisible = false
        mDialog!!.dispose()
    }

    companion object {
        const val ONLY_OK_SELECTION_IS_STORED = 0
        const val BOTH_OK_AND_CANCEL_OPTIONS_ARE_STORED = 1
        protected var logger: Logger? = null
    }
}