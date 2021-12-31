/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 22.07.2004
 */
/*$Id: HookDescriptorPluginAction.java,v 1.1.2.2 2008/01/13 20:55:34 christianfoltin Exp $*/
package freemind.extensions

import freemind.controller.actions.generated.instance.Plugin
import freemind.controller.actions.generated.instance.PluginAction
import freemind.controller.actions.generated.instance.PluginMenu
import freemind.controller.actions.generated.instance.PluginMode
import freemind.controller.actions.generated.instance.PluginProperty
import java.util.Properties
import java.util.Vector

/**
 * This is an information class that holds all outer properties of a hook, i.e.
 * all contents of the XML description file.
 *
 * Don't use this class for anything except for the implementation of a
 * HookFactory.
 *
 * @author foltin
 */
class HookDescriptorPluginAction(
    xmlPluginFile: String?,
    pluginBase: Plugin?,
    private val pluginAction: PluginAction
) : HookDescriptorBase(pluginBase!!, xmlPluginFile!!) {
    /**
     */
    val properties: Properties
    @JvmField
    var menuPositions: Vector<String?>
    val modes: Vector<String?>

    init {
        if (pluginAction.name == null) {
            pluginAction.name = pluginAction.label
        }
        menuPositions = Vector()
        properties = Properties()
        modes = Vector()
        for (obj in pluginAction.listChoiceList) {
            if (obj is PluginMenu) {
                menuPositions.add(obj.location)
            }
            if (obj is PluginProperty) {
                val property = obj
                properties[property.name] = property.value
            }
            if (obj is PluginMode) {
                modes.add(obj.className)
            }
        }
    }

    override fun toString(): String {
        return (
            "[HookDescriptor props=" + properties + ", menu positions=" +
                menuPositions + "]"
            )
    }

    // this is an error case?
    val instanciationMethod: HookInstanciationMethod?
        get() {
            if (pluginAction.instanciation != null) {
                val allInstMethods = HookInstanciationMethod
                    .allInstanciationMethods
                for (name in allInstMethods.keys) {
                    if (pluginAction.instanciation.equals(name, ignoreCase = true)) {
                        return allInstMethods[name]
                    }
                }
            }
            // this is an error case?
            return HookInstanciationMethod.Other
        }
    val name: String?
        get() = getFromResourceIfNecessary(pluginAction.name)
    val className: String?
        get() = pluginAction.className
    val documentation: String?
        get() = getFromResourceIfNecessary(pluginAction.documentation)
    val iconPath: String?
        get() = pluginAction.iconPath
    val keyStroke: String?
        get() = getFromPropertiesIfNecessary(pluginAction.keyStroke)
    val baseClass: String?
        get() = pluginAction.base

    /**
     * @return whether or not the plugin can be on/off and this should be
     * displayed in the menus.
     */
    val isSelectable: Boolean
        get() = pluginAction.isSelectable
}
