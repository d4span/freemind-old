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
 * Created on 31.12.2005
 */
package freemind.modes.browsemode

import freemind.extensions.HookFactory.RegistrationContainer
import freemind.extensions.HookFactoryAdapter
import freemind.extensions.HookInstanciationMethod
import freemind.extensions.ModeControllerHook
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHookSubstituteUnknown
import freemind.modes.common.plugins.MapNodePositionHolderBase
import freemind.modes.common.plugins.ReminderHookBase
import java.util.Properties
import java.util.Vector

/**
 * @author foltin
 */
class BrowseHookFactory :
/**
     *
     */
        HookFactoryAdapter() {
    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.extensions.HookFactory#getPossibleNodeHooks()
	 */
        override val possibleNodeHooks: Vector<String?>
            get() = Vector()

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.extensions.HookFactory#getPossibleModeControllerHooks()
	 */
        override val possibleModeControllerHooks: Vector<String?>
            get() = Vector()

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.extensions.HookFactory#createModeControllerHook(java.lang.String
	 * )
	 */
        override fun createModeControllerHook(hookName: String?): ModeControllerHook? {
            // TODO Auto-generated method stub
            return null
        }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.extensions.HookFactory#createNodeHook(java.lang.String)
	 */
        override fun createNodeHook(hookName: String?): NodeHook? {
            // System.out.println("create node hook:"+hookName);
            val hook: NodeHook
            hook = if (hookName == ReminderHookBase.PLUGIN_LABEL) {
                BrowseReminderHook()
            } else if (hookName == MapNodePositionHolderBase.NODE_MAP_HOOK_NAME) {
                MapNodePositionHolderBase()
            } else {
                PermanentNodeHookSubstituteUnknown(hookName)
            }
            // decorate hook.
            hook.setProperties(Properties())
            hook.name = hookName
            hook.setPluginBaseClass(null)
            return hook
        }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.extensions.HookFactory#getHookMenuPositions(java.lang.String)
	 */
        override fun getHookMenuPositions(hookName: String?): List<String?>? {
            // TODO Auto-generated method stub
            return null
        }

    /*
	 * (non-Javadoc)
	 *
	 * @see
	 * freemind.extensions.HookFactory#getInstanciationMethod(java.lang.String)
	 */
        override fun getInstanciationMethod(hookName: String?): HookInstanciationMethod? {
            // TODO Auto-generated method stub
            return null
        } // TODO Auto-generated method stub

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.extensions.HookFactory#getRegistrations()
	 */
        override val registrations: List<RegistrationContainer>?
            get() = // TODO Auto-generated method stub
                null

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.extensions.HookFactory#getPluginBaseClass(java.lang.String)
	 */
        override fun getPluginBaseClass(hookName: String?): Any? {
            // TODO Auto-generated method stub
            return null
        }
    }
