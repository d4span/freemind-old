/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Dimitri Polivaev, Christian Foltin and others.
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
 * Created on 18.08.2006
 */
/*$Id: HookDescriptorRegistration.java,v 1.1.2.1 2006/08/20 19:34:25 christianfoltin Exp $*/
package freemind.extensions

import freemind.controller.actions.generated.instance.Plugin
import freemind.controller.actions.generated.instance.PluginMode
import freemind.controller.actions.generated.instance.PluginRegistration

/**
 * @author foltin
 */
class HookDescriptorRegistration(
    xmlPluginFile: String?,
    pluginBase: Plugin?,
    private val mRegistration: PluginRegistration
) : HookDescriptorBase(pluginBase!!, xmlPluginFile!!) {
    // public PluginRegistration getPluginRegistration() {
    // return mRegistration;
    // }
    val className: String?
        get() = mRegistration.className
    val isPluginBase: Boolean
        get() = mRegistration.isPluginBase
    val listPluginModeList: List<PluginMode?>
        get() = mRegistration.listPluginModeList
}
