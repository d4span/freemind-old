/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
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
package freemind.common

import freemind.main.Resources
import tests.freemind.FreeMindMainMock
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSplitPane

/**
 * Should display one or two JComponents. If two, it should use a JSplitPane
 * internally. Future: if more than two, it can use a JTabbedPane.
 *
 * @author foltin
 * @date 26.08.2014
 */
class JOptionalSplitPane : JPanel() {
    private val mComponentHash = HashMap<Int, JComponent>()
    private var mBasicComponent: JComponent? = null
    /**
     * @return the lastDividerPosition
     */
    /**
     * @param pLastDividerPosition the lastDividerPosition to set
     */
    var lastDividerPosition = -1

    init {
        layout = BorderLayout()
    }

    val amountOfComponents: Int
        get() = mComponentHash.size

    fun setComponent(pComponent: JComponent, index: Int) {
        checkIndex(index)
        var formerComponent: JComponent? = null
        if (mComponentHash.containsKey(index)) {
            formerComponent = mComponentHash[index]
            if (pComponent === formerComponent) {
                // Already present.
                return
            }
        }
        mComponentHash[index] = pComponent
        when (mComponentHash.size) {
            1 -> if (mBasicComponent !is JPanel) {
                setSingleJPanel(pComponent)
            } else {
                // remove former:
                mBasicComponent?.remove(formerComponent)
                mBasicComponent?.add(pComponent, BorderLayout.CENTER)
                revalidate()
            }
            2 -> if (mBasicComponent !is JSplitPane) {
                remove(mBasicComponent)
                // TODO: Make configurable.
                val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
                mBasicComponent = splitPane
                splitPane.leftComponent = mComponentHash[0]
                splitPane.rightComponent = mComponentHash[1]
                if (lastDividerPosition >= 0) {
                    // Restore divider location
                    splitPane.dividerLocation = lastDividerPosition
                }
                add(mBasicComponent, BorderLayout.CENTER)
                revalidate()
            } else {
                // some component has changed:
                val splitPane = mBasicComponent as JSplitPane
                lastDividerPosition = splitPane.dividerLocation
                splitPane.remove(formerComponent)
                splitPane.leftComponent = mComponentHash[0]
                splitPane.rightComponent = mComponentHash[1]
                splitPane.dividerLocation = lastDividerPosition
                revalidate()
            }
            else -> throw IllegalArgumentException("Too many indices: " + mComponentHash.size)
        }
    }

    /**
     * @param pComponent
     */
    private fun setSingleJPanel(pComponent: JComponent) {
        if (mBasicComponent != null) {
            if (mBasicComponent is JSplitPane) {
                lastDividerPosition = (mBasicComponent as JSplitPane).dividerLocation
            }
            remove(mBasicComponent)
        }
        mBasicComponent = JPanel()
        mBasicComponent?.setLayout(BorderLayout())
        mBasicComponent?.add(pComponent, BorderLayout.CENTER)
        add(mBasicComponent, BorderLayout.CENTER)
        revalidate()
    }

    fun removeComponent(index: Int) {
        checkIndex(index)
        if (!mComponentHash.containsKey(index)) {
            return
        }
        // 		JComponent formerComponent = mComponentHash.get(index);
        mComponentHash.remove(index)
        when (mComponentHash.size) {
            0 -> {
                if (mBasicComponent != null) {
                    remove(mBasicComponent)
                }
                mBasicComponent = JLabel()
                add(mBasicComponent)
                revalidate()
            }
            1 -> // TODO: Too complicated:
                setSingleJPanel(mComponentHash.values.iterator().next())
            else -> throw IllegalArgumentException("Wrong indices: " + mComponentHash.size)
        }
    }

    /**
     * @param index
     */
    private fun checkIndex(index: Int) {
        require(!(index < 0 || index > 1)) { "Wrong index: $index" }
    }

    /**
     * @return the dividerPosition or last location is currently no split pane is visible
     */
    val dividerPosition: Int
        get() {
            if (mBasicComponent is JSplitPane) {
                val pane = mBasicComponent as JSplitPane
                return pane.dividerLocation
            }
            return lastDividerPosition
        }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Resources.createInstance(FreeMindMainMock())
            val frame = JFrame("JOptionalSplitPane")
            frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            val panel = JOptionalSplitPane()
            val contentPane = frame.contentPane
            contentPane.layout = GridLayout(5, 1)
            frame.pack()
            frame.setSize(800, 400)
            // 		// focus fix after startup.
// 		frame.addWindowFocusListener(new WindowAdapter() {
//
// 			public void windowGainedFocus(WindowEvent e) {
// 				frame.removeWindowFocusListener(this);
// 				jcalendar.getDayChooser().getSelectedDay().requestFocus();
// 			}
// 		});
            contentPane.add(
                JButton(object : AbstractAction("Add 0") {
                    private var index = 0
                    override fun actionPerformed(pE: ActionEvent) {
                        panel.setComponent(JLabel("links " + index++), 0)
                    }
                })
            )
            contentPane.add(
                JButton(object : AbstractAction("Add 1") {
                    private var index = 0
                    override fun actionPerformed(pE: ActionEvent) {
                        panel.setComponent(JLabel("rechts " + index++), 1)
                    }
                })
            )
            contentPane.add(
                JButton(object : AbstractAction("Remove 0") {
                    override fun actionPerformed(pE: ActionEvent) {
                        panel.removeComponent(0)
                    }
                })
            )
            contentPane.add(
                JButton(object : AbstractAction("Remove 1") {
                    override fun actionPerformed(pE: ActionEvent) {
                        panel.removeComponent(1)
                    }
                })
            )
            contentPane.add(panel)
            frame.isVisible = true
        }
    }
}
