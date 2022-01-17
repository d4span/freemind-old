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
package freemind.modes.common.dialogs

import freemind.common.TextTranslator
import freemind.main.Tools
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JPasswordField

/**  */
class EnterPasswordDialog : JDialog {
    /**
     * @return Returns the result.
     */
    var result = CANCEL
        private set
    private var jContentPane: JPanel? = null
    private var jLabel: JLabel? = null
    private var jLabel1: JLabel? = null
    private var jPasswordField: JPasswordField? = null
    private var jPasswordField1: JPasswordField? = null
    private var jOKButton: JButton? = null
    private var jCancelButton: JButton? = null

    /**
     * @return Returns the password.
     */
    var password: StringBuffer? = null
        private set
    private var enterTwoPasswords = true
    private var mTranslator: TextTranslator? = null

    @Deprecated("do not use. This is for visual editor only.")
    constructor() {
    }

    /**
     * This is the default constructor
     */
    constructor(
        caller: JFrame?,
        pTranslator: TextTranslator?,
        enterTwoPasswords: Boolean
    ) : super(caller, "", true /* =modal */) {
        mTranslator = pTranslator
        this.enterTwoPasswords = enterTwoPasswords
        initialize()
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private fun initialize() {
        if (enterTwoPasswords) title = mTranslator?.getText("accessories/plugins/EncryptNode.properties_0") // $NON-NLS-1$
        else title = mTranslator?.getText("accessories/plugins/EncryptNode.properties_8") // $NON-NLS-1$
        this.setSize(350, 200)
        this.contentPane = getJContentPane()
        setLocationRelativeTo(parent)
        defaultCloseOperation = DO_NOTHING_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                cancelPressed()
            }
        })
        Tools.addEscapeActionToDialog(
            this,
            object : AbstractAction() {
                override fun actionPerformed(pE: ActionEvent) {
                    cancelPressed()
                }
            }
        )
    }

    private fun close() {
        dispose()
    }

    private fun okPressed() {
        // check if equal:
        if (!checkEqualAndMinimumSize()) {
            JOptionPane.showMessageDialog(
                this, mTranslator?.getText("accessories/plugins/EncryptNode.properties_1")
            ) // $NON-NLS-1$
            return
        }
        password = StringBuffer()
        password!!.append(jPasswordField!!.password)
        result = OK
        close()
    }

    /**
     */
    private fun checkEqualAndMinimumSize(): Boolean {
        val a1 = jPasswordField!!.password
        if (a1.size < 2) {
            return false
        }
        if (enterTwoPasswords) {
            val a2 = jPasswordField1!!.password
            if (a1.size != a2.size) {
                return false
            }
            for (i in a1.indices) {
                if (a1[i] != a2[i]) {
                    return false
                }
            }
        }
        return true
    }

    private fun cancelPressed() {
        password = null
        result = CANCEL
        close()
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private fun getJContentPane(): JPanel {
        if (jContentPane == null) {
            jLabel1 = JLabel()
            jLabel = JLabel()
            val gridBagConstraints1 = GridBagConstraints()
            val gridBagConstraints2 = GridBagConstraints()
            val gridBagConstraints3 = GridBagConstraints()
            val gridBagConstraints4 = GridBagConstraints()
            val gridBagConstraints6 = GridBagConstraints()
            val gridBagConstraints7 = GridBagConstraints()
            jContentPane = JPanel()
            jContentPane!!.layout = GridBagLayout()
            gridBagConstraints1.gridx = 0
            gridBagConstraints1.gridy = 1
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints1.insets = Insets(0, 5, 0, 0)
            jLabel!!.text = mTranslator?.getText("accessories/plugins/EncryptNode.properties_2") // $NON-NLS-1$
            gridBagConstraints2.gridx = 0
            gridBagConstraints2.gridy = 2
            gridBagConstraints2.insets = Insets(0, 5, 0, 0)
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL
            jLabel1!!.text = mTranslator?.getText("accessories/plugins/EncryptNode.properties_3") // $NON-NLS-1$
            gridBagConstraints3.gridx = 1
            gridBagConstraints3.gridy = 1
            gridBagConstraints3.weightx = 1.0
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints4.gridx = 1
            gridBagConstraints4.gridy = 2
            gridBagConstraints4.weightx = 1.0
            gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL
            gridBagConstraints6.gridx = 0
            gridBagConstraints6.gridy = 3
            gridBagConstraints6.insets = Insets(20, 0, 0, 0)
            gridBagConstraints7.gridx = 1
            gridBagConstraints7.gridy = 3
            gridBagConstraints7.insets = Insets(20, 0, 0, 0)
            jContentPane!!.add(jLabel, gridBagConstraints1)
            jContentPane!!.add(getJPasswordField(), gridBagConstraints3)
            if (enterTwoPasswords) {
                jContentPane!!.add(getJPasswordField1(), gridBagConstraints4)
                jContentPane!!.add(jLabel1, gridBagConstraints2)
            }
            jContentPane!!.add(getJOKButton(), gridBagConstraints6)
            jContentPane!!.add(getJCancelButton(), gridBagConstraints7)
            getRootPane().defaultButton = getJOKButton()
        }
        return jContentPane!!
    }

    /**
     * This method initializes jPasswordField
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordField(): JPasswordField {
        if (jPasswordField == null) {
            jPasswordField = JPasswordField()
        }
        return jPasswordField!!
    }

    /**
     * This method initializes jPasswordField1
     *
     * @return javax.swing.JPasswordField
     */
    private fun getJPasswordField1(): JPasswordField {
        if (jPasswordField1 == null) {
            jPasswordField1 = JPasswordField()
        }
        return jPasswordField1!!
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private fun getJOKButton(): JButton {
        if (jOKButton == null) {
            jOKButton = JButton()
            jOKButton!!.action = object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    okPressed()
                }
            }
            jOKButton!!.text = mTranslator?.getText("accessories/plugins/EncryptNode.properties_6") // $NON-NLS-1$
        }
        return jOKButton!!
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private fun getJCancelButton(): JButton {
        if (jCancelButton == null) {
            jCancelButton = JButton()
            jCancelButton!!.action = object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    cancelPressed()
                }
            }
            jCancelButton!!.text = mTranslator?.getText("accessories/plugins/EncryptNode.properties_7") // $NON-NLS-1$
        }
        return jCancelButton!!
    }

    companion object {
        const val CANCEL = -1
        const val OK = 1
    }
}
