/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2005  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 06.02.2005
 */
package freemind.modes.common.plugins

import freemind.extensions.PermanentNodeHookAdapter
import freemind.main.XMLElement
import freemind.modes.MindIcon
import freemind.modes.MindMapNode
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.text.MessageFormat
import java.util.Date
import javax.swing.ImageIcon
import javax.swing.Timer

/**
 * @author foltin
 */
abstract class ReminderHookBase : // private Vector dateVector = new Vector();
/**
     *
     */
        PermanentNodeHookAdapter() {
        var remindUserAt: Long = 0
        private var timer: Timer? = null
        override fun loadFrom(child: XMLElement?) {
            super.loadFrom(child)
            val hash = loadNameValuePairs(child!!)
            if (hash.containsKey(REMINDUSERAT)) {
                remindUserAt = hash[REMINDUSERAT]!!.toLong()
            }
        }

        override fun save(hookElement: XMLElement?) {
            super.save(hookElement)
            val nameValuePairs = HashMap<String?, Any?>()
            nameValuePairs[REMINDUSERAT] = remindUserAt
            saveNameValuePairs(nameValuePairs, hookElement!!)
        }

        override fun shutdownMapHook() {
            setToolTip(getNode(), name, null)
            if (timer != null) {
                timer!!.stop()
            }
            displayState(REMOVE_CLOCK, getNode(), true)
            super.shutdownMapHook()
        }

        override fun invoke(node: MindMapNode?) {
            super.invoke(node)
            if (remindUserAt == 0L) {
                return
            }
            if (timer == null) {
                scheduleTimer()
                val date = Date(remindUserAt)
                val messageArguments = arrayOf<Any>(date)
                val formatter = MessageFormat(
                    getResourceString("plugins/TimeManagement.xml_reminderNode_tooltip")
                )
                val message = formatter.format(messageArguments)
                setToolTip(node, name, message)
                displayState(CLOCK_VISIBLE, getNode(), false)
            }
            logger!!.info("Invoke for node: " + node!!.getObjectId(controller))
        }

        /**
         */
        private fun scheduleTimer() {
            timer = Timer(
                remindUserAtAsSecondsFromNow,
                TimerBlinkTask(false)
            )
            timer!!.delay = BLINK_INTERVAL_IN_MILLIES
            timer!!.start()
        }

        // icon
        private val clockIcon: ImageIcon?
            get() {
                // icon
                if (Companion.clockIcon == null) {
                    Companion.clockIcon = MindIcon.factory("clock2").icon
                }
                return Companion.clockIcon
            }

        // icon
        private val bellIcon: ImageIcon?
            get() {
                // icon
                if (Companion.bellIcon == null) {
                    Companion.bellIcon = MindIcon.factory("bell").icon
                }
                return Companion.bellIcon
            }

        // icon
        private val flagIcon: ImageIcon?
            get() {
                // icon
                if (Companion.flagIcon == null) {
                    Companion.flagIcon = MindIcon.factory("flag").icon
                }
                return Companion.flagIcon
            }

        inner class TimerBlinkTask(stateAdded: Boolean) : ActionListener {
            private var stateAdded = false

            /**
             */
            init {
                this.stateAdded = stateAdded
            }

            override fun actionPerformed(pE: ActionEvent) {
                // check for time over:
                val remindAt = remindUserAtAsSecondsFromNow
                if (remindAt > BLINK_INTERVAL_IN_MILLIES) {
                    // time not over (maybe due to integer-long conversion too big)
                    timer!!.stop()
                    scheduleTimer()
                    return
                }
                // time is over, we add the new icon until
                // the user removes the reminder.
                //
                stateAdded = !stateAdded
                remindUserAt = (
                    System.currentTimeMillis() +
                        BLINK_INTERVAL_IN_MILLIES
                    ) // 3
                displayState(
                    if (stateAdded) CLOCK_VISIBLE else CLOCK_INVISIBLE,
                    getNode(), true
                )
            }
        }

        fun displayState(stateAdded: Int, pNode: MindMapNode?, recurse: Boolean) {
            var icon: ImageIcon? = null
            if (stateAdded == CLOCK_VISIBLE) {
                icon = clockIcon
            } else if (stateAdded == CLOCK_INVISIBLE) {
                icon = if (pNode === getNode()) {
                    bellIcon
                } else {
                    flagIcon
                }
            }
            pNode!!.setStateIcon(stateKey, icon)
            nodeRefresh(pNode)
            if (recurse && !pNode.isRoot) {
                displayState(stateAdded, pNode.parentNode, recurse)
            }
        }

        protected abstract fun nodeRefresh(node: MindMapNode?)
        abstract override fun setToolTip(
            node: MindMapNode?,
            key: String?,
            value: String?
        )

        val remindUserAtAsSecondsFromNow: Int
            get() {
                val timeDiff = remindUserAt - System.currentTimeMillis()
                if (timeDiff > Int.MAX_VALUE) {
                    return Int.MAX_VALUE
                }
                return if (timeDiff < Int.MIN_VALUE) {
                    Int.MIN_VALUE
                } else timeDiff.toInt()
            }
        private val STATE_TOOLTIP = (
            TimerBlinkTask::class.java.name +
                "_STATE_"
            )
        private var mStateTooltipName: String? = null

        // + getNode().getObjectId(getController());
        val stateKey: String?
            get() {
                if (mStateTooltipName == null) {
                    mStateTooltipName = STATE_TOOLTIP
                    // + getNode().getObjectId(getController());
                }
                return mStateTooltipName
            }

        companion object {
            const val PLUGIN_LABEL = "plugins/TimeManagementReminder.xml"
            private const val CLOCK_INVISIBLE = 0
            private const val CLOCK_VISIBLE = 1
            private const val REMOVE_CLOCK = -1
            const val REMINDUSERAT = "REMINDUSERAT"
            private const val BLINK_INTERVAL_IN_MILLIES = 3000
            private var clockIcon: ImageIcon? = null
            private var bellIcon: ImageIcon? = null
            private var flagIcon: ImageIcon? = null
        }
    }
